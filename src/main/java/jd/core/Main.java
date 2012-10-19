package jd.core;

import jd.core.Decompiler;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                System.out.println("Usage: jd <basePath> <internalClassName>");
                return;
            }

            String basePath = args[0];
            String internalClassName = args[1];

            String result = Decompiler.decompile(basePath, internalClassName);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
