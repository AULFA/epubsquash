package one.lfa.epubsquash.tests;

import one.lfa.epubsquash.api.EPUBSquasherProviderType;
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
