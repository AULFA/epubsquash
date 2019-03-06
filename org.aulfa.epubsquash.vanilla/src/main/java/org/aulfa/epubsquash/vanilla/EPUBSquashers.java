package org.aulfa.epubsquash.vanilla;

import org.aulfa.epubsquash.api.EPUBSquasherConfiguration;
import org.aulfa.epubsquash.api.EPUBSquasherProviderType;
import org.aulfa.epubsquash.api.EPUBSquasherType;

/**
 * A provider of EPUB squashers.
 */

public final class EPUBSquashers implements EPUBSquasherProviderType
{
  /**
   * Construct a provider.
   */

  public EPUBSquashers()
  {

  }

  @Override
  public EPUBSquasherType createSquasher(
    final EPUBSquasherConfiguration configuration)
  {
    return new EPUBSquasher(configuration);
  }
}
