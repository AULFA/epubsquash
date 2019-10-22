package one.lfa.epubsquash.vanilla;

import one.lfa.epubsquash.api.EPUBSquasherProviderType;
import one.lfa.epubsquash.api.EPUBSquasherType;
import one.lfa.epubsquash.api.EPUBSquasherConfiguration;

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
