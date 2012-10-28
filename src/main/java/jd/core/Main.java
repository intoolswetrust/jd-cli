package jd.core;

import jd.core.Decompiler;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                System.err.println("Usage: java -jar jd-core-java.jar <compiled.jar> <out_dir>");
                return;
            }
            String jarPath = args[0];
            String outDirPath = args[1];

            int numDecompiled = new Decompiler().decompileToDir(jarPath, outDirPath);
            System.err.println("Decompiled " + numDecompiled + " classes");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
