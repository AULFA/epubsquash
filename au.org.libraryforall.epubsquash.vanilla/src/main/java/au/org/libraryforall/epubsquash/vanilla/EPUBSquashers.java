package au.org.libraryforall.epubsquash.vanilla;

import au.org.libraryforall.epubsquash.api.EPUBSquasherProviderType;
import au.org.libraryforall.epubsquash.api.EPUBSquasherType;
import au.org.libraryforall.epubsquash.api.EPUBSquasherConfiguration;

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
