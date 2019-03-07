package au.org.libraryforall.epubsquash.tests;

import au.org.libraryforall.epubsquash.api.EPUBSquasherConfiguration;
import au.org.libraryforall.epubsquash.vanilla.EPUBSquashers;

import java.io.IOException;
import java.nio.file.Paths;

public final class Try
{
  private Try()
  {

  }

  public static void main(final String[] args)
    throws IOException
  {
    final var squashers = new EPUBSquashers();

    final var configuration =
      EPUBSquasherConfiguration.builder()
        .setInputFile(Paths.get("/tmp/input.epub"))
        .setOutputFile(Paths.get("/tmp/output.epub"))
        .setTemporaryDirectory(Paths.get("/tmp/epubsquash"))
        .build();

    final var squasher = squashers.createSquasher(configuration);
    squasher.squash();
  }
}
