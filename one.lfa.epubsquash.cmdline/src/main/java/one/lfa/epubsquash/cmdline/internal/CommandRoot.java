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

import com.beust.jcommander.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandRoot implements CommandType
{
  @Parameter(
    names = "--verbose",
    converter = ESLogLevelConverter.class,
    description = "Set the minimum logging verbosity level")
  private ESLogLevel verbose = ESLogLevel.LOG_INFO;

  public CommandRoot()
  {

  }

  /**
   * The base method used to set up logging. Must be called by overrides.
   *
   * @return Nothing
   *
   * @throws Exception On errors
   */

  @Override
  public Void call()
    throws Exception
  {
    final var root =
      (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(
        Logger.ROOT_LOGGER_NAME);
    root.setLevel(this.verbose.toLevel());
    return null;
  }
}
