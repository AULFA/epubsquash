package au.org.libraryforall.epubsquash.cmdline;

import ch.qos.logback.classic.Level;
import com.io7m.junreachable.UnreachableCodeException;

import java.util.Objects;

/**
 * Log level.
 */

public enum ESLogLevel
{
  /**
   * @see Level#TRACE
   */

  LOG_TRACE("trace"),

  /**
   * @see Level#DEBUG
   */

  LOG_DEBUG("debug"),

  /**
   * @see Level#INFO
   */

  LOG_INFO("info"),

  /**
   * @see Level#WARN
   */

  LOG_WARN("warn"),

  /**
   * @see Level#ERROR
   */

  LOG_ERROR("error");


  private final String name;

  ESLogLevel(final String in_name)
  {
    this.name = Objects.requireNonNull(in_name);
  }

  @Override
  public String toString()
  {
    return this.name;
  }

  /**
   * @return The short name of the level
   */

  public String getName()
  {
    return this.name;
  }

  Level toLevel()
  {
    switch (this) {
      case LOG_TRACE:
        return Level.TRACE;
      case LOG_DEBUG:
        return Level.DEBUG;
      case LOG_INFO:
        return Level.INFO;
      case LOG_WARN:
        return Level.WARN;
      case LOG_ERROR:
        return Level.ERROR;
    }

    throw new UnreachableCodeException();
  }
}
