package jd.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.Map;
import java.util.HashMap;
import jd.ide.intellij.JavaDecompiler;

public class Decompiler {
	
	private JavaDecompiler decompiler;
	
	public Decompiler(){
		decompiler = new JavaDecompiler();
	}
	
	public String decompile(String jarPath, String internalClassName) throws DecompilerException {
		String decompiled = decompiler.decompile(jarPath, internalClassName);
		
		if (!validContent(decompiled)){
			throw new DecompilerException("cannot decompile " + jarPath + "!" + internalClassName);
		}
		
		return decompiled;
	}
	
	public Map<String, String> decompile(String jarPath) throws DecompilerException, IOException {
		ZipInputStream zip = new ZipInputStream(new FileInputStream(jarPath));
		ZipEntry ze;
		Map<String, String> pathToSrc = new HashMap<String, String>();
		
		while ((ze = zip.getNextEntry()) != null){
			String entryName = ze.getName();
			
			if (entryName.endsWith(".class")){
				String classPath = entryName.replaceAll("\\$.*\\.class$", ".class");
				String javaPath = classPath.replaceAll("\\.class$", ".java");
				
				if (!pathToSrc.containsKey(javaPath)){
					pathToSrc.put(javaPath, decompiler.decompile(jarPath, classPath));
				}
			}
		}
		
		return pathToSrc;
	}
	
	public int decompileToDir(String jarPath, String outDir) throws DecompilerException, IOException {
		Map<String, String> pathToSrc = decompile(jarPath);
		
		for (Map.Entry<String, String> entry : pathToSrc.entrySet()){
			String fileName = entry.getKey();
			File file = new File(outDir, fileName);
			file.getParentFile().mkdirs();
			PrintWriter out = new PrintWriter(file);
			out.print(entry.getValue());
			out.close();
		}
		
		return pathToSrc.size();
	}
	
	private boolean validContent(String decompiled){
		return decompiled != null && !decompiled.matches("(?sm)class\\s*\\{\\s*\\}.*");
	}
	
}
