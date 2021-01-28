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

package one.lfa.epubsquash.cmdline.internal;

import com.beust.jcommander.internal.Console;

public final class StringConsole implements Console
{
  private final StringBuilder stringBuilder;

  public StringConsole()
  {
    this.stringBuilder = new StringBuilder(128);
  }

  @Override
  public void print(
    final String text)
  {
    this.stringBuilder.append(text);
  }

  @Override
  public void println(
    final String text)
  {
    this.stringBuilder.append(text);
    this.stringBuilder.append("\n");
  }

  @Override
  public char[] readPassword(
    final boolean b)
  {
    return new char[0];
  }

  public String text()
  {
    return this.stringBuilder.toString();
  }
}
