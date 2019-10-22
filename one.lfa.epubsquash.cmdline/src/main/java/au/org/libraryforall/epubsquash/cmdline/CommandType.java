package au.org.libraryforall.epubsquash.cmdline;

import java.util.concurrent.Callable;

/**
 * The type of command-line subcommands.
 */

public interface CommandType extends Callable<Void>
{
  // No extra methods
}
