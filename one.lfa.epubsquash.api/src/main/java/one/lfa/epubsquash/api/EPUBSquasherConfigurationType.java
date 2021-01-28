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

package one.lfa.epubsquash.api;

import com.io7m.immutables.styles.ImmutablesStyleType;
import org.immutables.value.Value;

import java.nio.file.Path;

/**
 * Configuration values for EPUB squashers.
 */

@Value.Immutable
@ImmutablesStyleType
public interface EPUBSquasherConfigurationType
{
  /**
   * @return The input file
   */

  Path inputFile();

  /**
   * @return The temporary directory used to unpack files
   */

  Path temporaryDirectory();

  /**
   * @return The output file
   */

  Path outputFile();

  /**
   * Each image will be scaled by a given amount, where {@code 1.0} keeps the original size,
   * {@code 0.5} scales by 50%, etc.
   *
   * @return The amount by which to scale the image
   */

  @Value.Default
  default double scale()
  {
    return 1.0;
  }

  /**
   * @return The maximum image width in pixels
   */

  double maximumImageWidth();

  /**
   * @return The maximum image height in pixels
   */

  double maximumImageHeight();
}
