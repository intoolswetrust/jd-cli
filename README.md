# jd-cmd - Command line Java Decompiler

The *jd-cmd* is a simple command line wrapper around [JD Core](https://github.com/java-decompiler/jd-core)  Java Decompiler project. 

## Download

Find latest bits in **[GitHub Releases](https://github.com/kwart/jd-cmd/releases/latest)**.

## Requirements

[Java runtime](http://java.com/en/download/) is required in version 8 or newer (for version 1.0.0 and newer) and Java 6 for older versions.

## Usage - Command line

You can use the `jd-cli.bat` (Windows) or `jd-cli` (Linux/Unix) scripts to run the the JAR file.

    Usage: java -jar jd-cli.jar [options] [Files to decompile]
    Options:
      --displayLineNumbers, -n
         displays line numbers in decompiled classes
         Default: false
      --escapeUnicodeCharacters, -eu
         escape unicode characters in decompiled classes
         Default: false
      --help, -h
         shows this help
         Default: false
      --logLevel, -g
         takes [level] as parameter and sets it as the CLI log level. Possible
         values are: ALL, TRACE, DEBUG, INFO, WARN, ERROR, OFF
         Default: INFO
      --outputConsole, -oc
         enables output to system output stream
         Default: false
      --outputDir, -od
         takes a [directoryPath] as a parameter and configures a flat DIR output
         for this path
      --outputDirStructured, -ods
         takes a [directoryPath] as a parameter and configures a structured DIR
         output for this path
      --outputZipFile, -oz
         takes a [zipFilePath] as a parameter and configures ZIP output for this
         path
      --skipResources, -sr
         skips processing resources
         Default: false
      --version, -v
         shows the version
         Default: false


## Credits

This project was originally forked from [JDCommandLine](https://github.com/betterphp/JDCommandLine). 

## License

* jd-cmd is licensed under [GPLv3](http://www.gnu.org/licenses/gpl-3.0.html) as the original JD-Core library.