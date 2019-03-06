/*
 * Copyright © 2019 Mark Raynsford <code@io7m.com> http://io7m.com
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

package org.aulfa.epubsquash.cmdline;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.aulfa.epubsquash.api.EPUBSquasherConfiguration;
import org.aulfa.epubsquash.api.EPUBSquasherProviderType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ServiceLoader;

@Parameters(commandDescription = "Squash an EPUB file")
final class CommandSquash extends CommandRoot
{
  // CHECKSTYLE:OFF

  @Parameter(
    names = "--input-file",
    required = true,
    description = "The EPUB file to squash")
  Path input_file;

  @Parameter(
    names = "--output-file",
    required = true,
    description = "The output EPUB file")
  Path output_file;

  @Parameter(
    names = "--image-max-width",
    required = false,
    description = "The maximum width of images")
  double image_max_width = 1600.0;

  @Parameter(
    names = "--image-max-height",
    required = false,
    description = "The maximum height of images")
  double image_max_height = 1170.0;

  // CHECKSTYLE:ON

  @Override
  public Void call()
    throws Exception
  {
    super.call();

    final var squashers =
      ServiceLoader.load(EPUBSquasherProviderType.class)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(
          "No EPUB squasher service available"));

    final var squasher =
      squashers.createSquasher(
        EPUBSquasherConfiguration.builder()
          .setInputFile(this.input_file)
          .setTemporaryDirectory(Files.createTempDirectory("epubsquasher"))
          .setOutputFile(this.output_file)
          .setMaximumImageHeight(this.image_max_height)
          .setMaximumImageWidth(this.image_max_width)
          .build());

    squasher.squash();
    return null;
  }

  CommandSquash()
  {

  }
}
