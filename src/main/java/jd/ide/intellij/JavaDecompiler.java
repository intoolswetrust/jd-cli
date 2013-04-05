package jd.ide.intellij;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jd.core.Main;

public class JavaDecompiler {
	
	static{
		String osName = System.getProperty("os.name").toLowerCase();
		String os = "linux";
		String arch = (System.getProperty("os.arch").contains("64")) ? "x86_64" : "x86";
		String libEx = "so";
		
		if (osName.contains("win")){
			os = "win32";
			libEx = "dll";
		}else if (osName.contains("mac")){
			os = "macosx";
			libEx = "jnilib";
		}
		
		String libPath = Main.BASE_PATH + File.separator + "libjd-intellij." + libEx;
		
		File libFile = new File(libPath);
		
		if (!libFile.exists()){
			System.out.println("Downloading native library");
			
			try{
				URL url = new URL("https://bitbucket.org/bric3/jd-intellij/get/default.zip");
				
				URLConnection connection = url.openConnection();
				connection.setReadTimeout(10000);
				connection.setConnectTimeout(5000);
				
				BufferedInputStream input = new BufferedInputStream(connection.getInputStream());
				ZipInputStream zip = new ZipInputStream(input);
				
				ZipEntry entry = null;
				
				while ((entry = zip.getNextEntry()) != null){
					if (entry.getName().contains("native/nativelib/" + os + "/" + arch)){
						FileOutputStream output = new FileOutputStream(libPath);
						
						byte[] buffer = new byte[4096];
						int len;
						
						while ((len = zip.read(buffer)) != -1){
							output.write(buffer, 0, len);
						}
						
						output.close();
						break;
					}
				}
				
				zip.close();
				input.close();
			}catch (Exception e){
				System.err.println("Failed to download library");
				e.printStackTrace();
			}
		}
		
		try{
			System.load(libPath);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public native String decompile(String basePath, String internalClassName);
	
}
