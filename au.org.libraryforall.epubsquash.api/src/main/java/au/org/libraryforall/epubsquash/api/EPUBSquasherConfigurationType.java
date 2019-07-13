package au.org.libraryforall.epubsquash.api;

import com.io7m.immutables.styles.ImmutablesStyleType;
import org.immutables.value.Value;

import java.nio.file.Path;

/**
 * Configuration values for EPUB squashers.
 */

@Value.Immutable
@ImmutablesStyleType
public interface EPUBSquasherConfigurationType
{
  /**
   * @return The input file
   */

  Path inputFile();

  /**
   * @return The temporary directory used to unpack files
   */

  Path temporaryDirectory();

  /**
   * @return The output file
   */

  Path outputFile();

  /**
   * Each image will be scaled by a given amount, where {@code 1.0} keeps the original size,
   * {@code 0.5} scales by 50%, etc.
   *
   * @return The amount by which to scale the image
   */

  @Value.Default
  default double scale() {
    return 1.0;
  }

  /**
   * @return The maximum image width in pixels
   */

  double maximumImageWidth();

  /**
   * @return The maximum image height in pixels
   */

  double maximumImageHeight();
}
