package jd.core;

import java.io.File;
import java.net.URLDecoder;

import jd.core.Decompiler;
import jd.ide.intellij.JavaDecompiler;

public class Main {
	
	public static String BASE_PATH;
	
	public static void main(String[] args){
		try{
			File jarFile = new File(URLDecoder.decode(JavaDecompiler.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8"));
			
			if (args.length < 2){
				System.err.println("Usage: java -jar " + jarFile.getName() + " <compiled.jar> <out_dir>");
				return;
			}
			
			Main.BASE_PATH = jarFile.getParent();
			
			String jarPath = args[0];
			String outDirPath = args[1];
			
			(new Decompiler()).decompileToDir(jarPath, outDirPath);
		}catch (Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
}
