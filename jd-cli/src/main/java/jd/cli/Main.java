/*
 * Copyright 2013 kwart, betterphp, nviennot
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jd.cli;

import java.io.File;

import jd.core.Decompiler;
import jd.core.input.ClassFileInput;
import jd.core.input.JDInput;
import jd.core.output.ZipOutput;
import jd.ide.intellij.JavaDecompiler;

public class Main {

    public static void main(String[] args) {

        try {
            // JDInput in = new DirInput("/home/jcacek/Java/Projects/jsignpdf/bin");
            JDInput in = new ClassFileInput("/home/jcacek/Java/Projects/jsignpdf/bin/net/sf/jsignpdf/InstallCert.class");
            ZipOutput zo = new ZipOutput(new File(
                    "/home/jcacek/Testing/jboss-eap-6.2.0.ER2/standalone/deployments/spnego-demo-decompiled.zip"));
            in.decompile(new JavaDecompiler(), zo);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        Decompiler decompiler = new Decompiler();
        // System.out.println(decompiler.decompile("/home/share2/Java/Projects/jsignpdf/bin/net/sf/jsignpdf/crl",
        // "CRLInfo.class",
        // null));
        // System.setProperty(SystemProperties.LINE_NUMBERS, "true");
        // decompiler = new JavaDecompiler();
        // System.out.println(decompiler.decompile("/home/share2/Java/Projects/jd-cmd/target/classes",
        // "jd/core/Decompiler.class", null));

        if (true)
            return;
        try {
            if (args.length == 0 || args.length > 2) {
                System.err.println("Usage: java -jar jd-cmd.jar <input.jar> [output]");
                return;
            }

            // TODO Decompiler.getInstance(), Decompiler.setOptions(DecompilerOptions) - test if the native implementation is
            // thread safe.
            // if only classname provided, try to cycle through parents until found the right package
            // String decompiler.decompile(basePath, internalClassName,options);
            // void decompiler.decompile(path, options, JDOutput...);
            // Single class decompilation - with possible (default?) output to console
            // Decompile to zip (configurable in DecompilerOptions?)
            // instead of the decompileToDir use sth. like decompile

            final String outName = args.length == 1 ? args[0] + ".src" : args[1];
            // (new Decompiler()).decompileToDir(args[0], outName);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
