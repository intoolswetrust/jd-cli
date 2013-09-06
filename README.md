JDCommandLine
=============

JDCommandLine is a thin-wrapper for the [Java Decompiler](http://java.decompiler.free.fr/).

This is hack around the IntelliJ IDE plugin. It fakes the interfaces of the
IDE, and provides access to JD-Core.

Since the Author of JD-Core is not willing to provide a library, as seen on
[this thread](http://java.decompiler.free.fr/?q=node/116), and we all want
to batch decompilation, this is pretty much our only option.

I hope this will motivate the author to release a proper library.

This was originally [jd-core-java](https://github.com/nviennot/jd-core-java)

Supported Platforms
-------------------

JD supports:

- Linux 32/64-bit
- Windows 32/64-bit
- Mac OSX 32/64-bit on x86 hardware


Usage
------

Programmatically:

```java
/* Returns the source of SomeClass from compiled.jar as a String */
new jd.core.Decompiler.decompile("compiled.jar", "com/namespace/SomeClass.class");

/*
 * Returns the number of classes decompiled and saved into out_dir
 */
new jd.core.Decompiler.decompileToDir("compiled.jar", "out_dir");
```

From the command line:
```shell
# Outputs all the sources of compiled.jar into out_dir
java -jar JDCommandLine.jar <compiled.jar> <out_dir>
```

License
-------

JDCommandLine is released under the MIT license.

JD-IntelliJ is free for non-commercial use. This means that JD-IntelliJ shall
not be included or embedded into commercial software products. Nevertheless,
this project may be freely used for personal needs in a commercial or
non-commercial environments.
