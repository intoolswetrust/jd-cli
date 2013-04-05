package jd.ide.intellij;

import java.io.File;

public class JavaDecompiler {
	
	static{
		String path = "";
		
		try{
			path = JavaDecompiler.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			path = java.net.URLDecoder.decode(path, "UTF-8");
			path = (new File(path)).getParent();
			
			System.load(path + "/libjd-intellij.so");
		}catch (Exception e){
			throw new IllegalStateException("Something got wrong when loading the Java Decompiler native lib at " + path);
		}
	}
	
	public native String decompile(String basePath, String internalClassName);
	
}
