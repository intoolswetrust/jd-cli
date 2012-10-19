package jd.ide.intellij;

public class JavaDecompiler {
    static {
        String path = "";
        try {
            path = JavaDecompiler.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            path = java.net.URLDecoder.decode(path, "UTF-8");
            path = new java.io.File(path).getParent();
            System.load(path + "/libjd-intellij.so");
        } catch (Exception e) {
            throw new IllegalStateException("Something got wrong when loading the Java Decompiler native lib at " + path);
        }
    }

    public native String decompile(String basePath, String internalClassName);
}
