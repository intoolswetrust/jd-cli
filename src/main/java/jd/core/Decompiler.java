package jd.core;

public class Decompiler {
    public static String decompile(String basePath, String internalClassName) throws DecompilerException {
        String decompiled = new jd.ide.intellij.JavaDecompiler().decompile(basePath, internalClassName);
        if (!validContent(decompiled))
            throw new DecompilerException("cannot decompile " + basePath + "!" +  internalClassName);
        return decompiled;
    }

    private static boolean validContent(String decompiled) {
        return decompiled != null && !decompiled.matches("(?sm)class\\s*\\{\\s*\\}.*");
    }
}
