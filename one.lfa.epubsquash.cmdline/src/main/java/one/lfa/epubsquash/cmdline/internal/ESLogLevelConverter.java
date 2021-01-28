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

import com.beust.jcommander.IStringConverter;

import java.util.Objects;

/**
 * A converter for {@link ESLogLevel} values.
 */

public final class ESLogLevelConverter implements IStringConverter<ESLogLevel>
{
  /**
   * Construct a new converter.
   */

  public ESLogLevelConverter()
  {

  }

  @Override
  public ESLogLevel convert(final String value)
  {
    for (final var v : ESLogLevel.values()) {
      if (Objects.equals(value, v.getName())) {
        return v;
      }
    }

    throw new ESLogLevelUnrecognized(
      "Unrecognized verbosity level: " + value);
  }
}
