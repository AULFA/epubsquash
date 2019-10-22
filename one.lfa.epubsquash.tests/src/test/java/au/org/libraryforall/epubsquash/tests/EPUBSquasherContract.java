package au.org.libraryforall.epubsquash.tests;

import au.org.libraryforall.epubsquash.api.EPUBSquasherProviderType;
import org.junit.jupiter.api.Test;

public abstract class EPUBSquasherContract
{
  @Test
  public void testUnpackDoNothing()
  {
    final var squashers = this.createSquashers();


  }

  protected abstract EPUBSquasherProviderType createSquashers();
}
