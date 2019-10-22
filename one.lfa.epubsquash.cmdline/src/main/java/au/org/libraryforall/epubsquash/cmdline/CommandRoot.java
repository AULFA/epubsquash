package au.org.libraryforall.epubsquash.cmdline;

import com.beust.jcommander.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CommandRoot implements CommandType
{
  @Parameter(
    names = "--verbose",
    converter = ESLogLevelConverter.class,
    description = "Set the minimum logging verbosity level")
  private ESLogLevel verbose = ESLogLevel.LOG_INFO;

  CommandRoot()
  {

  }

  @Override
  public Void call()
    throws Exception
  {
    final var root =
      (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(
        Logger.ROOT_LOGGER_NAME);
    root.setLevel(this.verbose.toLevel());
    return null;
  }
}
