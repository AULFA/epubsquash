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
   * @return The maximum image width in pixels
   */

  double maximumImageWidth();

  /**
   * @return The maximum image height in pixels
   */

  double maximumImageHeight();
}
