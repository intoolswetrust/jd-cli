# jd-cmd - Command line Java Decompiler

## Build the software yourself

You should have [git](http://git-scm.com/) installed

	$ git clone git://github.com/kwart/jd-cmd.git

or you can download [current sources as a zip file](https://github.com/kwart/jd-cmd/archive/master.zip)

Then you need to have [Maven 3.*](http://maven.apache.org/) installed

	$ cd jd-cmd
	$ mvn clean package

Test it by running:
	
	$ java -jar jd-cli/target/jd-cli.jar

## Usage

The **jd-cmd** comes with 2 modules:

 1. **jd-cli** command line interface for the decompiler. It's uses jd-lib module.
 1. **jd-lib** core API functionality.

### Command line

Use all-in-one **jd-cli** jar located in `jd-cli/target/jd-cli.jar`

    Usage: java -jar jd-cli.jar [options] [Files to decompile]
      Options:
        --displayLineNumbers, -n
           displays line numbers in decompiled classes
           Default: false
        --dontMergeEmptyLines, -dm
           disables merging multiple empty lines into one in the decompiled classes
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
           takes a [directoryPath] as a parameter and configures DIR output for this
           path
        --outputZipFile, -oz
           takes a [zipFilePath] as a parameter and configures ZIP output for this
           path
        --realignLineNumbers, -rn
           realign line numbers in decompiled classes
           Default: false
        --showDefaultConstructor, -dc
           show default constructor in decompiled classes
           Default: false
        --showLocation, -l
           displays Location info in decompiled classes metadata part
           Default: false
        --showPrefixThis, -st
           prefix with 'this' where possible in decompiled classes
           Default: false
        --skipMetadata, -sm
           skips metadata in decompiled classes
           Default: false
        --skipResources, -sr
           skips processing resources
           Default: false


### Programmatically

Add `jd-lib` as a dependency for your App and then do something like

	import java.io.File;
	import jd.core.input.JDInput;
	import jd.core.input.ZipFileInput;
	import jd.core.output.DirOutput;
	import jd.core.output.JDOutput;
	import jd.core.JavaDecompiler;
	
	public class App {
		public static void main(String[] args) {
			JavaDecompiler javaDecompiler = new JavaDecompiler();
			//choose input plugin for your decompiled file (class, zip, directory)
			JDInput jdIn = new ZipFileInput("path/to/myFavorityLib.jar");
			//choose output plugin (zip, directory, console)
			JDOutput jdOut = new DirOutput(new File("/tmp/decompiled"));
			//decompile
			jdIn.decompile(javaDecompiler, jdOut);
		}
	}
