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

package one.lfa.epubsquash.cmdline.internal;

import one.lfa.epubsquash.api.EPUBSquasherConfiguration;
import one.lfa.epubsquash.api.EPUBSquasherProviderType;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ServiceLoader;

@Parameters(commandDescription = "Squash an EPUB file")
public final class CommandSquash extends CommandRoot
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
    names = "--image-scale",
    required = false,
    description = "The image scaling value")
  double image_scale = 1.0;

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
          .setScale(this.image_scale)
          .build());

    squasher.squash();
    return null;
  }

  public CommandSquash()
  {

  }
}
