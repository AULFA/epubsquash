/*
 * Copyright © 2019 Library For All
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package one.lfa.epubsquash.cmdline;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import one.lfa.epubsquash.cmdline.internal.CommandRoot;
import one.lfa.epubsquash.cmdline.internal.CommandSquash;
import one.lfa.epubsquash.cmdline.internal.CommandType;
import one.lfa.epubsquash.cmdline.internal.StringConsole;
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
    final var console = new StringConsole();
    this.commander.setConsole(console);

    try {
      this.commander.parse(this.args);

      final var cmd = this.commander.getParsedCommand();
      if (cmd == null) {
        this.commander.usage();
        LOG.info("Arguments required.\n{}", console.text());
        return;
      }

      final var command = this.commands.get(cmd);
      command.call();
    } catch (final ParameterException e) {
      this.commander.usage();
      LOG.error("{}\n{}", e.getMessage(), console.text());
      this.exit_code = 1;
    } catch (final Exception e) {
      LOG.error("{}", e.getMessage(), e);
      this.exit_code = 1;
    }
  }
}
