# jd-cli - Command line Java Decompiler

The *jd-cli* is a simple command line wrapper around [JD Core](https://github.com/java-decompiler/jd-core)  Java Decompiler project. 

## Download

Find latest bits in **[GitHub Releases](https://github.com/kwart/jd-cli/releases/latest)**.

## Requirements

[Java runtime](http://java.com/en/download/) is required in version 8 or newer (for version 1.0.0 and newer) and Java 6 for older versions.

## Usage

### Command line

You can use the `jd-cli.bat` (Windows) or `jd-cli` (Linux/Unix) scripts to run the the JAR file.

```
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
    --pattern, -p
       RegExp pattern which the to-be-decompiled file has to match. Not matching
       entries are skipped.
    --serialProcessing, -sp
       don't use parallel processing
       Default: false
    --skipResources, -sr
       skips processing resources
       Default: false
    --version, -v
       shows the version
       Default: false
```


### Docker

You can use a Docker distribution to decompile your classes.

For instance, the following command decompiles the `file-to-decompile.jar`
in the current directory and the decompiled files are stored in
the `decompiled-src` directory.

```bash
docker run -it --rm -v `pwd`:/mnt --user $(id -u):$(id -g) \
  kwart/jd-cli /mnt/file-to-decompile.jar -od /mnt/deccompiled-src
```

## Using as a Maven dependency

```xml
<dependency>
    <groupId>com.github.kwart.jd</groupId>
    <artifactId>jd-lib</artifactId>
    <version>${jd-cli.version}</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-simple</artifactId>
    <version>${slf4j.version}</version>
</dependency>
```

```java
public static void main(String[] args) {
    JDInput input = new ZipFileInput("/opt/hazelcast/hazelcast.jar");
    JDOutput output = new DirOutput(new File("/tmp/hz-src"));
    JavaDecompiler decompiler = new JavaDecompiler(new DecompilerOptions() {

        @Override
        public boolean isSkipResources() {
            return true;
        }

        @Override
        public boolean isEscapeUnicodeCharacters() {
            return false;
        }

        @Override
        public boolean isDisplayLineNumbers() {
            return true;
        }

        @Override
        public boolean isParallelProcessingAllowed() {
            return true;
        }
    });
    input.decompile(decompiler, output);
}
```

## Credits

This project was originally forked from [JDCommandLine](https://github.com/betterphp/JDCommandLine). 

## License

* jd-cli is licensed under [GPLv3](http://www.gnu.org/licenses/gpl-3.0.html) as the original JD-Core library.
