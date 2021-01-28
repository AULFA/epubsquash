/*
 * Copyright © 2019 Library For All
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package one.lfa.epubsquash.vanilla.internal;

import one.lfa.epubsquash.api.EPUBSquasherConfiguration;
import one.lfa.epubsquash.api.EPUBSquasherType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.zip.ZipFile.OPEN_READ;

public final class EPUBSquasher implements EPUBSquasherType
{
  private static final Logger LOG = LoggerFactory.getLogger(EPUBSquasher.class);

  private static final Instant TIMESTAMP =
    LocalDateTime.of(2001, 1, 1, 0, 0, 0)
      .toInstant(ZoneOffset.UTC);

  private final EPUBSquasherConfiguration configuration;
  private final AtomicBoolean squashed;
  private final TreeMap<String, Path> unpacked;
  private final EPUBImageSquasher image_squasher;

  public EPUBSquasher(
    final EPUBSquasherConfiguration in_configuration)
  {
    this.configuration =
      Objects.requireNonNull(in_configuration, "configuration");
    this.squashed =
      new AtomicBoolean(false);
    this.unpacked =
      new TreeMap<>();
    this.image_squasher =
      new EPUBImageSquasher();
  }

  private static void repack(
    final Path temp,
    final Path output_file)
    throws IOException
  {
    try (var stream = Files.newOutputStream(
      output_file,
      WRITE,
      TRUNCATE_EXISTING,
      CREATE)) {
      try (var zip_out = new ZipOutputStream(stream, UTF_8)) {
        final var copies =
          Files.walk(temp)
            .filter(path -> Files.isRegularFile(path))
            .filter(path -> !path.getFileName().startsWith("TMP_"))
            .map(path -> new Copy(path, temp.relativize(path).toString()))
            .collect(Collectors.toList());

        final var meta =
          copies.stream()
            .filter(copy -> "mimetype".equals(copy.name))
            .findFirst()
            .orElseThrow();

        packCopy(zip_out, meta);

        for (final var copy : copies) {
          if (Objects.equals(copy.name, "mimetype")) {
            continue;
          }
          packCopy(zip_out, copy);
        }
      }
    }
  }

  private static void packCopy(
    final ZipOutputStream zip_out,
    final Copy copy)
    throws IOException
  {
    LOG.debug("pack: {}", copy.input);

    final var entry = new ZipEntry(copy.name);
    final var size = Files.size(copy.input);
    entry.setSize(size);

    final var crc32 = new CRC32();
    final var buffer = new byte[8192];
    try (var stream = Files.newInputStream(copy.input, READ)) {
      while (true) {
        final var r = stream.read(buffer);
        if (r == -1) {
          break;
        }
        crc32.update(buffer, 0, r);
      }
      entry.setCrc(crc32.getValue());
    }

    entry.setCreationTime(FileTime.from(TIMESTAMP));
    entry.setLastAccessTime(FileTime.from(TIMESTAMP));
    entry.setLastModifiedTime(FileTime.from(TIMESTAMP));
    entry.setMethod(ZipEntry.DEFLATED);
    entry.setExtra(new byte[0]);

    zip_out.putNextEntry(entry);
    Files.copy(copy.input, zip_out);
    Files.delete(copy.input);

    final var parent = copy.input.getParent();
    if (parent != null && Files.isDirectory(parent)) {
      try {
        Files.delete(parent);
      } catch (final IOException e) {
        // Don't care
      }
    }
  }

  private static void deleteRecursive(
    final Path directory)
    throws IOException
  {
    Files.walkFileTree(directory, new EPUBDeletionVisitor(LOG));
  }

  @Override
  public void squash()
    throws IOException
  {
    if (this.squashed.compareAndSet(false, true)) {
      this.runSquash();
    } else {
      throw new IllegalStateException("Squasher has already executed");
    }
  }

  private void runSquash()
    throws IOException
  {
    final var temp = this.configuration.temporaryDirectory();
    Files.createDirectories(temp);

    try {
      this.runUnpack(temp);
      this.runImages();
      repack(temp, this.configuration.outputFile());
    } finally {
      deleteRecursive(temp);
    }
  }

  private EPUBImageSize calculateImageSize(
    final Path path,
    final int width,
    final int height)
  {
    var mWidth = width;
    var mHeight = height;

    final var scale = this.configuration.scale();
    if (scale != 1.0) {
      mWidth = (int) ((double) width * scale);
      mHeight = (int) ((double) height * scale);
    }

    final var aspect = (double) height / (double) width;
    final var maxWidth = this.configuration.maximumImageWidth();
    final var maxHeight = this.configuration.maximumImageHeight();
    if ((double) mWidth > maxWidth && (double) mHeight > maxHeight) {
      mHeight = (int) (mHeight * aspect);
    }

    LOG.debug(
      "rescaling {}: {}x{} -> {}x{}",
      path,
      Double.valueOf(width),
      Double.valueOf(height),
      Double.valueOf(mWidth),
      Double.valueOf(mHeight));

    return new EPUBImageSize(mWidth, mHeight);
  }

  private void runImages()
    throws IOException
  {
    for (final var entry : this.unpacked.entrySet()) {
      final var path = entry.getValue();

      if (this.image_squasher.isImage(path)) {
        LOG.info("squashing: {}", path);

        final var image =
          ImageIO.read(path.toFile());
        final var scaled =
          this.calculateImageSize(path, image.getWidth(), image.getHeight());

        this.image_squasher.squashImage(
          path,
          path.resolveSibling("TMP_" + path.getFileName()),
          path,
          scaled.width(),
          scaled.height());
      }
    }
  }

  private void runUnpack(
    final Path temp)
    throws IOException
  {
    try (var input_zip = new ZipFile(
      this.configuration.inputFile().toFile(),
      OPEN_READ)) {
      final var entries = input_zip.entries();
      while (entries.hasMoreElements()) {
        final var entry = entries.nextElement();
        final var output_path = temp.resolve(entry.getName()).toAbsolutePath();
        if (entry.isDirectory()) {
          LOG.debug("mkdir: {}", temp);
          Files.createDirectories(temp);
        } else {
          final var parent = output_path.getParent();
          if ((parent != null) && !Files.exists(parent)) {
            LOG.debug("mkdir: {}", parent);
            Files.createDirectories(parent);
          }
          if (entry.getSize() == 0) {
            if (!Files.exists(output_path)) {
              LOG.debug("create empty: {} -> {}", entry.getName(), output_path);
              Files.createFile(output_path);
            }
          } else {
            LOG.debug("copy: {} -> {}", entry.getName(), output_path);
            final var input = input_zip.getInputStream(entry);
            Files.copy(input, output_path, REPLACE_EXISTING);
          }
        }

        this.unpacked.put(entry.getName(), output_path);
      }
    }
  }

  private static final class Copy
  {
    private final Path input;
    private final String name;

    private Copy(
      final Path in_input,
      final String in_name)
    {
      this.input =
        Objects.requireNonNull(in_input, "input");
      this.name =
        Objects.requireNonNull(in_name, "name");
    }
  }
}
