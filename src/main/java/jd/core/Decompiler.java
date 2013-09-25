package jd.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import jd.core.options.DecompilerOptions;
import jd.core.options.OptionsManager;
import jd.core.output.JDOutput;
import jd.ide.intellij.JavaDecompiler;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

public class Decompiler {

	private static final JavaDecompiler decompiler = new JavaDecompiler();

	public Decompiler() {

	}

	public String decompile(String basePath, String className, DecompilerOptions options) {
		if (className == null || basePath == null)
			return null;

		OptionsManager.setOptions(options);
		JavaClass javaClass = null;

		String src = decompiler.decompile(basePath, className);
		//TODO is windows OK with \n?
		if (src.startsWith("class \n{")) {
//			decompilation failed - wrong package?
			File baseFile = new File(basePath);
			if (baseFile.isDirectory()) {
				File classFile = new File(baseFile, className);
				if (!classFile.isFile())
					return null;
				try {
					javaClass = new ClassParser(classFile.getAbsolutePath()).parse();
					String correctPath = javaClass.getClassName().replace('.', '/') + ".class";
					File tempJar = createTempJar(classFile, correctPath);
					src = decompiler.decompile(tempJar.getPath(), correctPath);
					tempJar.delete();
				} catch (Exception e) {
					return null;
				}
			}
		}

		if (OptionsManager.getOptions().isDisplayMetadata() && OptionsManager.getOptions().isDiscardLocation()) {
			src = src.replaceFirst("(/\\* Location:).*", "$1");
		}

		//TODO metadata++ from BCEL JavaClass - e.g. class version - major.minor 
		return src;
	}

	public void decompile(String path, DecompilerOptions options, JDOutput... outPlugins) {
		File file = new File(path).getAbsoluteFile();
		String fileName = file.getName();
		for (JDOutput out : outPlugins) {
			out.init(file.getPath());
		}
// files - .class vs. others (zips), directories?
		if (file.isFile() && fileName.toLowerCase(Locale.ENGLISH).endsWith(".class")) {
			final String src = decompile(file.getParent(), file.getName(), options);
			for (JDOutput out : outPlugins) {
				out.processClass(file.getName(), src);
			}
		}

		for (JDOutput out : outPlugins) {
			out.init(file.getPath());
		}
	}

	// TODO handle failures
	private File createTempJar(File classFile, String correctPath) throws IOException {
		File tempJar = File.createTempFile("jdTemp-", ".jar");
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempJar));
		zos.putNextEntry(new ZipEntry(correctPath));
		FileInputStream fis = new FileInputStream(classFile);
		IOUtils.copy(fis, zos);
		zos.closeEntry();
		zos.close();
		return tempJar;
	}

	public void decompileToDir(String jarPath, String outDir) throws IOException {
		ZipInputStream zip = new ZipInputStream(new FileInputStream(jarPath));
		ZipEntry entry = null;

		while ((entry = zip.getNextEntry()) != null) {
			String entryName = entry.getName();

			if (!entry.isDirectory()) {
				if (entryName.endsWith(".class")) {
					String classPath = entryName.replaceAll("\\$.*\\.class$", ".class");
					String javaPath = classPath.replaceAll("\\.class$", ".java");

					File outFile = new File(outDir, javaPath);
					outFile.getParentFile().mkdirs();

					FileOutputStream output = new FileOutputStream(outFile);

					output.write(this.decompile(jarPath, classPath, null).getBytes("UTF-8"));

					IOUtils.closeQuietly(output);
				} else {
					File outFile = new File(outDir, entryName);
					outFile.getParentFile().mkdirs();

					final FileOutputStream output = new FileOutputStream(outFile);
					try {
						IOUtils.copy(zip, output);
					} finally {
						IOUtils.closeQuietly(output);
					}
				}
			}
		}

		zip.close();
	}

}
