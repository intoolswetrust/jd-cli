package jd.ide.intellij;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;

import jd.core.IOUtils;
import jd.core.JavaDecompilerConstants;

public class JavaDecompiler {

	static {
		String osName = System.getProperty("os.name").toLowerCase();
		String platform = "linux";
		String arch = (System.getProperty("os.arch").contains("64")) ? "x86_64" : "x86";
		String libExt = ".so";

		if (osName.contains("win")) {
			platform = "win32";
			libExt = ".dll";
		} else if (osName.contains("mac")) {
			platform = "macosx";
			libExt = ".jnilib";
		}

		final File tmpDir = new File(JavaDecompilerConstants.TMP_DIR);
		try {
			String[] oldLibs = tmpDir.list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name != null && name.startsWith(JavaDecompilerConstants.NATIVE_LIB_TMP_PREFIX);
				}
			});
			for (String libName : oldLibs) {
				File oldLibFile = new File(tmpDir, libName);
				if (oldLibFile.isFile()) {
					oldLibFile.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		InputStream is = null;
		OutputStream os = null;
		try {
			final File libTempFile = File.createTempFile(JavaDecompilerConstants.NATIVE_LIB_TMP_PREFIX, libExt, tmpDir);
			libTempFile.deleteOnExit();

			final String pathInJar = "/native/" + platform + "/" + arch + "/" + JavaDecompilerConstants.NATIVE_LIB_NAME
					+ libExt;
			is = JavaDecompiler.class.getResourceAsStream(pathInJar);
			if (is == null) {
				throw new FileNotFoundException("Native library " + pathInJar + " was not found inside the JAR.");
			}
			os = new FileOutputStream(libTempFile);
			IOUtils.copy(is, os);

			System.load(libTempFile.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
	}

	public native String decompile(String basePath, String internalClassName);

}
