package jd.core;

public class Main {

    public static void main(String[] args) {
        try {
            if (args.length == 0 || args.length > 2) {
                System.err.println("Usage: java -jar jd-cmd <input.jar> [output]");
                return;
            }

            final String outName = args.length == 1 ? args[0] + ".src" : args[1];
            (new Decompiler()).decompileToDir(args[0], outName);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
