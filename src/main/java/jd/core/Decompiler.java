package jd.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.Map;
import java.util.HashMap;
import jd.ide.intellij.JavaDecompiler;

public class Decompiler {
    private JavaDecompiler decompiler;

    public Decompiler() {
        decompiler = new JavaDecompiler();
    }

    public String decompile(String jarPath, String internalClassName) throws DecompilerException {
        String decompiled = decompiler.decompile(jarPath, internalClassName);
        if (!validContent(decompiled))
            throw new DecompilerException("cannot decompile " + jarPath + "!" +  internalClassName);
        return decompiled;
    }

    public Map<String, String> decompile(String jarPath) throws DecompilerException, IOException {
        ZipInputStream zip = new ZipInputStream(new FileInputStream(jarPath));
        ZipEntry ze;
        Map<String, String> classToJava = new HashMap<String, String>();

        while ((ze = zip.getNextEntry()) != null ) {
            String entryName = ze.getName();
            if (entryName.endsWith(".class")) {
                String className = entryName.replaceAll("\\$.*\\.class$", ".class");
                if (!classToJava.containsKey(className))
                    classToJava.put(className, decompiler.decompile(jarPath, className));
            }
        }

        return classToJava;
    }

    private boolean validContent(String decompiled) {
        return decompiled != null && !decompiled.matches("(?sm)class\\s*\\{\\s*\\}.*");
    }
}
