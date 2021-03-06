/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package one.lfa.epubsquash.vanilla.internal;

import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public final class EPUBDeletionVisitor extends SimpleFileVisitor<Path>
{
  private final Logger logger;

  EPUBDeletionVisitor(
    final Logger inLogger)
  {
    this.logger = Objects.requireNonNull(inLogger, "logger");
  }

  @Override
  public FileVisitResult visitFile(
    final Path file,
    final BasicFileAttributes attrs)
    throws IOException
  {
    this.logger.debug("delete {}", file);
    try {
      Files.delete(file);
    } catch (final IOException e) {
      this.logger.error("delete {} failed: ", file, e);
      throw e;
    }
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult postVisitDirectory(
    final Path dir,
    final IOException exc)
    throws IOException
  {
    this.logger.debug("delete {}", dir);
    try {
      Files.delete(dir);
    } catch (final IOException e) {
      this.logger.error("delete {} failed: ", dir, e);
      throw e;
    }
    return FileVisitResult.CONTINUE;
  }
}
