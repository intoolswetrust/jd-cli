# jd-cmd

jd-cmd is a thin-wrapper for the [Java Decompiler](http://java.decompiler.free.fr/).

This is a hack around the IntelliJ IDE plugin. It fakes the interfaces of the
IDE, and provides access to JD-Core.

This was originally [jd-core-java](https://github.com/nviennot/jd-core-java) 
and [JDCommandLine](https://github.com/betterphp/JDCommandLine) 

## Supported Platforms

jd-cmd supports:

- Linux 32/64-bit
- Windows 32/64-bit
- Mac OSX 32/64-bit on x86 hardware


## Usage

You can control the decompiler behavior with 3 boolean flags, which are read as system properties:

	// propertyName = defaultValue
	jd.lineNumbers       = false  # add line numbers as comments
	jd.metadata          = true   # add metadata to the end of file
    jd.discardLocation   = true   # remove Location value from the metadata

### Command line

	# Outputs all the sources of compiled.jar into out_dir
	# if out_dir is not provided, then name of the directory is the same as the JAR name with .src suffix
	java -Djd.lineNumbers=true -jar jd-cmd.jar <compiled.jar> [out_dir]


### Programmatically

	Decompiler decompiler = new jd.core.Decompiler();
	// let's display line numbers
	System.setProperty("jd.lineNumbers","true");
	
	/* Returns the source of SomeClass from compiled.jar as a String */
	String src = decompiler.decompile("compiled.jar", "com/namespace/SomeClass.class");
	
	/*
	 * Decompiles whole JAR to the given directory and returns the number of decompiled classes.
	 */
	int count = decompiler.decompileToDir("compiled.jar", "out_dir");


## License

JDCommandLine is released under the MIT license.

JD-IntelliJ is free for non-commercial use. This means that JD-IntelliJ shall
not be included or embedded into commercial software products. Nevertheless,
this project may be freely used for personal needs in a commercial or
non-commercial environments.

