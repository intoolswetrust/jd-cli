JD-Core-java
============

**JD-Core-java** is a thin-wrapper for the
[**Java Decompiler**](http://java.decompiler.free.fr/)
made by **Emmanuel Dupuy**.

The library provides an intereface for JD-Core.

Supported Platforms
-------------------

JD supports:

- Linux 32/64-bit
- Windows 32/64-bit
- Mac OSX 32/64-bit on x86 hardware

But this wrapper only supports Linux 64-bit. Please ask if you want more.

Build
-----

Simply use:

    make

I do not include the JD-Core library since the author of JD does not want to
publish a library.

Building will download the JD-IntelliJ plugin from bitbucket, and copy the
libjd-intellij.so library in the root directory of the project.

Once the project is built, the two files jd-core-java-1.0.jar libjd-intellij.so
will be in the root directory of the project.

Usage
------

```java
/**
 * Actual call to the native lib.
 *
 * @param basePath          Path to the root of the classpath, either a path to a directory or a path to a jar file.
 * @param internalClassName internal name of the class.
 * @return Decompiled class text.
 */
jd.core.Decompiler.decompile(basePath, internalClassName);
```

License
-------

Copyright © 2008-2012 Emmanuel Dupuy
