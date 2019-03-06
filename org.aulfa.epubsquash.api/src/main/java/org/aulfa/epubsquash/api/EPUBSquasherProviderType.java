package org.aulfa.epubsquash.api;

/**
 * A provider of EPUB squashers.
 */

public interface EPUBSquasherProviderType
{
  /**
   * Create a new squasher.
   *
   * @param configuration The squasher configuration
   *
   * @return A new squasher
   */

  EPUBSquasherType createSquasher(
    EPUBSquasherConfiguration configuration);
}
