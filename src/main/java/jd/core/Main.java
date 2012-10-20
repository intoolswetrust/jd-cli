package jd.core;

import jd.core.Decompiler;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.out.println("Usage: jd <jar> [internalClassName]");
                return;
            }
            String basePath = args[0];

            if (args.length == 2) {
                String internalClassName = args[1];
                String result = new Decompiler().decompile(basePath, internalClassName);
                System.out.println(result);
            } else {
                Map<String, String> classToJava = new Decompiler().decompile(basePath);
                for (Map.Entry<String, String> entry : classToJava.entrySet()) {
                    System.out.println("/* " + entry.getKey() + " */");
                    System.out.println(entry.getValue());
                    System.out.println("");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
