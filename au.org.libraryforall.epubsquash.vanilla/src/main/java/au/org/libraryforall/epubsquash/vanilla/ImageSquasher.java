package au.org.libraryforall.epubsquash.vanilla;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

final class ImageSquasher
{
  private static final Logger LOG = LoggerFactory.getLogger(ImageSquasher.class);

  private final Set<String> image_types;

  ImageSquasher()
  {
    this.image_types = Set.of("image/jpeg");
  }

  boolean typeIsImage(final String content_type)
  {
    return content_type != null && this.image_types.contains(content_type);
  }

  boolean isImage(final Path path)
    throws IOException
  {
    Objects.requireNonNull(path, "path");
    return Files.isRegularFile(path) && this.typeIsImage(Files.probeContentType(path));
  }

  void squashImage(
    final Path input,
    final Path temp,
    final Path output,
    final double width,
    final double height)
    throws IOException
  {
    Objects.requireNonNull(input, "input");
    Objects.requireNonNull(temp, "temp");
    Objects.requireNonNull(output, "output");

    LOG.debug(
      "squashing image {} to {}x{}",
      input,
      Double.valueOf(width),
      Double.valueOf(height));

    Thumbnails.of(input.toFile())
      .antialiasing(Antialiasing.ON)
      .scalingMode(ScalingMode.BILINEAR)
      .outputFormat("jpg")
      .width((int) width)
      .height((int) height)
      .keepAspectRatio(true)
      .outputQuality(0.9)
      .toFile(temp.toFile());

    Files.move(temp, output, REPLACE_EXISTING, ATOMIC_MOVE);
  }
}
