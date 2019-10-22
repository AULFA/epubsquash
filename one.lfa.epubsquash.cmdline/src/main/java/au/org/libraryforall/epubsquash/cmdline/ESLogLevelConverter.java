package au.org.libraryforall.epubsquash.cmdline;

import com.beust.jcommander.IStringConverter;

import java.util.Objects;

/**
 * A converter for {@link ESLogLevel} values.
 */

public final class ESLogLevelConverter implements IStringConverter<ESLogLevel>
{
  /**
   * Construct a new converter.
   */

  public ESLogLevelConverter()
  {

  }

  @Override
  public ESLogLevel convert(final String value)
  {
    for (final var v : ESLogLevel.values()) {
      if (Objects.equals(value, v.getName())) {
        return v;
      }
    }

    throw new ESLogLevelUnrecognized(
      "Unrecognized verbosity level: " + value);
  }
}
