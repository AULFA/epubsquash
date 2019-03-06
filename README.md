epubsquash
===

[![Maven Central](https://img.shields.io/maven-central/v/org.aulfa/org.aulfa.epubsquash.png?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.aulfa%22)
[![Maven Central (snapshot)](https://img.shields.io/nexus/s/https/oss.sonatype.org/org.aulfa/org.aulfa.epubsquash.svg?style=flat-square)](https://oss.sonatype.org/content/repositories/snapshots/org.aulfa/)

A tool for making EPUB files smaller.

## Features

* Squash images in EPUBs to reduce file sizes
* Well designed modular API for use in Java 11+ programs
* Command line interface

## Requirements

* Java 11+

## How To Build

```
$ mvn clean package
```

If the above fails, it's a bug. Report it!

## Usage

```
Usage: epubsquash [options] [command] [command options]
  Options:
    --verbose
      Set the minimum logging verbosity level
      Default: info
      Possible Values: [trace, debug, info, warn, error]
  Commands:
    squash      Squash an EPUB file
      Usage: squash [options]
        Options:
          --image-max-height
            The maximum height of images
            Default: 1170.0
          --image-max-width
            The maximum width of images
            Default: 1600.0
        * --input-file
            The EPUB file to squash
        * --output-file
            The output EPUB file
          --verbose
            Set the minimum logging verbosity level
            Default: info
            Possible Values: [trace, debug, info, warn, error]
```

To squash an epub file `/tmp/input.epub`, writing the result to
`/tmp/output.epub`:

```
$ java -jar org.aulfa.epubsquash.cmdline-0.0.1-main.jar \
  squash \
    --input-file /tmp/input.epub \
    --output-file /tmp/output.epub \
    --verbose trace
```

The `epubsquash` package uses [jcommander](http://jcommander.org) to
parse command line arguments and therefore also supports placing
command line options into a file that can be referenced with `@`:

```
$ cat arguments.txt
squash
--input-file
/tmp/input.epub
--output-file
/tmp/output.epub
--verbose
trace

$ java -jar org.aulfa.epubsquash.cmdline-0.0.1-main.jar @arguments.txt
```

