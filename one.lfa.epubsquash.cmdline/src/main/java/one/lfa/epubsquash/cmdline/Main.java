package one.lfa.epubsquash.cmdline;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The main command-line program.
 */

public final class Main implements Runnable
{
  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  private final Map<String, CommandType> commands;
  private final JCommander commander;
  private final String[] args;
  private int exit_code;

  /**
   * Construct a new main program.
   *
   * @param in_args Command-line arguments
   */

  public Main(final String[] in_args)
  {
    this.args = Objects.requireNonNull(in_args, "args");

    final var r = new CommandRoot();
    final var cmd_squash = new CommandSquash();

    this.commands = new HashMap<>(8);
    this.commands.put("squash", cmd_squash);

    this.commander = new JCommander(r);
    this.commander.setProgramName("epubsquash");
    this.commander.addCommand("squash", cmd_squash);
  }

  /**
   * The main entry point.
   *
   * @param args Command line arguments
   */

  public static void main(final String[] args)
  {
    final var cm = new Main(args);
    cm.run();
    System.exit(cm.exitCode());
  }

  /**
   * @return The program exit code
   */

  public int exitCode()
  {
    return this.exit_code;
  }

  @Override
  public void run()
  {
    try {
      this.commander.parse(this.args);

      final var cmd = this.commander.getParsedCommand();
      if (cmd == null) {
        final var sb = new StringBuilder(128);
        this.commander.usage(sb);
        LOG.info("Arguments required.\n{}", sb.toString());
        return;
      }

      final var command = this.commands.get(cmd);
      command.call();
    } catch (final ParameterException e) {
      final var sb = new StringBuilder(128);
      this.commander.usage(sb);
      LOG.error("{}\n{}", e.getMessage(), sb.toString());
      this.exit_code = 1;
    } catch (final Exception e) {
      LOG.error("{}", e.getMessage(), e);
      this.exit_code = 1;
    }
  }
}
