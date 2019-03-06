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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CommandRoot implements CommandType
{
  @Parameter(
    names = "--verbose",
    converter = ESLogLevelConverter.class,
    description = "Set the minimum logging verbosity level")
  private ESLogLevel verbose = ESLogLevel.LOG_INFO;

  CommandRoot()
  {

  }

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
