# jd-cmd - Command line Java Decompiler

jd-cmd is a command line Java Decompiler which uses natives from [Java Decompiler GUI](http://java.decompiler.free.fr/) project. It's based on native libraries for IntelliJ plugin. 

This project was originally forked from [jd-core-java](https://github.com/nviennot/jd-core-java) 
and then from [JDCommandLine](https://github.com/betterphp/JDCommandLine).

## Supported Platforms

jd-cmd supports:

* Linux 32/64-bit
* Windows 32/64-bit
* Mac OSX 32/64-bit on x86 hardware

### Requirements

[Java runtime](http://java.com/en/download/) is required, at least version 6 is needed but we suggest usage of Java 7. Native libraries (at least the Linux ones) crashes occasionally with Java 6.

For an initial build you should install Git and Maven.

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
	       Include line numbers in decompiled classes
	       Default: false
	    --help, -h
	       Show this help
	       Default: false
	    --logLevel, -g
	       Log level, one of: ALL, TRACE, DEBUG, INFO, WARN, ERROR, OFF
	       Default: INFO
	    --outputConsole, -oc
	       Output to system output stream
	       Default: false
	    --outputDir, -od
	       Output to a directory with given path
	    --outputZipFile, -oz
	       Output to a zipped file with given path
	    --showLocation, -l
	       Include Location info in decompiled classes metadata part
	       Default: false
	    --skipMetadata, -sm
	       Don't include metadata in decompiled classes
	       Default: false
	    --skipResources, -sr
	       Skip processing resources
	       Default: false

### Programmatically

Add `jd-lib` as a dependency for your App and then do something like

	import java.io.File;
	import jd.core.input.JDInput;
	import jd.core.input.ZipFileInput;
	import jd.core.output.DirOutput;
	import jd.core.output.JDOutput;
	import jd.ide.intellij.JavaDecompiler;
	
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

## License

* jd-cmd is licensed under [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
* this software is based on [MIT licensed](http://opensource.org/licenses/MIT) project [JDCommandLine](https://github.com/betterphp/JDCommandLine)
* JD-IntelliJ is free for non-commercial use - check details in the [IntelliJ plugin repository](http://plugins.jetbrains.com/plugin/7100)

