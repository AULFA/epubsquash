package one.lfa.epubsquash.tests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import one.lfa.epubsquash.api.EPUBSquasherConfiguration;
import one.lfa.epubsquash.api.EPUBSquasherProviderType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EPUBSquasherContract
{
  private static final Logger LOG =
    LoggerFactory.getLogger(EPUBSquasherContract.class);

  private Path directory;

  private static InputStream resource(
    final String name)
    throws IOException
  {
    final var path =
      String.format("/one/lfa/epubsquash/tests/%s", name);
    final var url =
      EPUBSquasherContract.class.getResource(path);

    if (url == null) {
      throw new FileNotFoundException(path);
    }
    return url.openStream();
  }

  private static String hashOf(final Path path)
    throws Exception
  {
    try (var stream = Files.newInputStream(path)) {
      final var messageDigest = MessageDigest.getInstance("SHA-256");
      final var buffer = new byte[8192];
      while (true) {
        final var r = stream.read(buffer);
        if (r == -1) {
          break;
        }
        messageDigest.update(buffer, 0, r);
      }

      final var hash = new StringBuilder(128);
      final var digest = messageDigest.digest();
      for (final var b : digest) {
        hash.append(String.format("%02x", Byte.valueOf(b)));
      }
      return hash.toString();
    }
  }

  @BeforeEach
  public void testSetup()
    throws IOException
  {
    this.directory = TestDirectories.temporaryDirectory();
  }

  /**
   * Doing nothing to an epub should result in the same epub each time.
   *
   * @throws Exception On errors
   */

  @Test
  public void testUnpackDoNothingDeterministic()
    throws Exception
  {
    final var squashers = this.createSquashers();

    final var temp =
      this.directory.resolve("tmp");
    final var inputFile =
      this.directory.resolve("input.epub");
    final var outputFile0 =
      this.directory.resolve("output0.epub");
    final var outputFile1 =
      this.directory.resolve("output1.epub");

    Files.createDirectories(temp);

    try (var stream = resource("pg27472-images.epub")) {
      Files.copy(stream, inputFile);
    }

    final var configuration0 =
      EPUBSquasherConfiguration.builder()
        .setInputFile(inputFile)
        .setOutputFile(outputFile0)
        .setTemporaryDirectory(temp)
        .setMaximumImageHeight(4000.0)
        .setMaximumImageWidth(4000.0)
        .setScale(1.0)
        .build();

    final var squasher0 = squashers.createSquasher(configuration0);
    squasher0.squash();

    /*
     * Wait two seconds to increase the chances of there being timestamp
     * differences in the created EPUB files.
     */

    try {
      Thread.sleep(2000L);
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    final var configuration1 =
      EPUBSquasherConfiguration.builder()
        .setInputFile(inputFile)
        .setOutputFile(outputFile1)
        .setTemporaryDirectory(temp)
        .setMaximumImageHeight(4000.0)
        .setMaximumImageWidth(4000.0)
        .setScale(1.0)
        .build();

    final var squasher1 = squashers.createSquasher(configuration1);
    squasher1.squash();

    Assertions.assertTrue(Files.isRegularFile(outputFile0));
    Assertions.assertTrue(Files.isRegularFile(outputFile1));

    final var hash0 = hashOf(outputFile0);
    final var hash1 = hashOf(outputFile1);

    LOG.debug("outputFile0: {}", hash0);
    LOG.debug("outputFile1: {}", hash1);

    Assertions.assertEquals(hash0, hash1);
  }

  protected abstract EPUBSquasherProviderType createSquashers();
}
