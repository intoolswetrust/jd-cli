package jd.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jd.ide.intellij.JavaDecompiler;

public class Decompiler {

    private static final JavaDecompiler decompiler = new JavaDecompiler();

    public Decompiler() {

    }

    public String decompile(String jarPath, String internalClassName) throws DecompilerException {
        final String src = decompiler.decompile(jarPath, internalClassName);

        if (src == null) {
            throw new DecompilerException("cannot decompile " + jarPath + "!" + internalClassName);
        }

        return src;
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

                    try {
                        output.write(this.decompile(jarPath, classPath).getBytes("UTF-8"));
                    } catch (DecompilerException e) {
                        System.err.println("Failed to decompile " + classPath);
                    }

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
