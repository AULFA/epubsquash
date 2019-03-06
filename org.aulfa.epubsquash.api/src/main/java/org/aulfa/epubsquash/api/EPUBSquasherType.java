package org.aulfa.epubsquash.api;

import java.io.IOException;

/**
 * An EPUB squasher
 */

public interface EPUBSquasherType
{
  /**
   * Run the squasher. May be called at most once.
   *
   * @throws IOException On I/O errors
   */

  void squash()
    throws IOException;
}
