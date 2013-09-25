package jd.core;

import jd.core.JavaDecompilerConstants.SystemProperties;

public class Main {

	public static void main(String[] args) {
		Decompiler decompiler = new Decompiler();

		System.out.println(decompiler.decompile("/home/share2/Java/Projects/jsignpdf/bin/net/sf/jsignpdf/crl",
				"CRLInfo.class", null));
		System.setProperty(SystemProperties.LINE_NUMBERS, "true");
//		decompiler = new JavaDecompiler();
//		System.out.println(decompiler.decompile("/home/share2/Java/Projects/jd-cmd/target/classes",
//				"jd/core/Decompiler.class", null));

		if (true)
			return;
		try {
			if (args.length == 0 || args.length > 2) {
				System.err.println("Usage: java -jar jd-cmd.jar <input.jar> [output]");
				return;
			}

			// TODO Decompiler.getInstance(), Decompiler.setOptions(DecompilerOptions) - test if the native implementation is
			// thread safe.
			//if only classname provided, try to cycle through parents until found the right package
//			String decompiler.decompile(basePath, internalClassName,options);
//			void decompiler.decompile(path, options, JDOutput...);
			// Single class decompilation - with possible (default?) output to console
			// Decompile to zip (configurable in DecompilerOptions?)
			// instead of the decompileToDir use sth. like decompile

			final String outName = args.length == 1 ? args[0] + ".src" : args[1];
			(new Decompiler()).decompileToDir(args[0], outName);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
