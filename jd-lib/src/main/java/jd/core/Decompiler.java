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
package jd.core;

import java.io.File;
import java.util.Locale;

import jd.core.options.DecompilerOptions;
import jd.core.options.OptionsManager;
import jd.core.output.JDOutput;
import jd.core.output.MultiOutput;
import jd.ide.intellij.JavaDecompiler;

public class Decompiler {

    private static final JavaDecompiler decompiler = new JavaDecompiler();

    public Decompiler() {

    }

    public String decompile(String basePath, String className, DecompilerOptions options) {
        OptionsManager.setOptions(options);
        return decompiler.decompileClass(basePath, className);
    }

    public void decompile(String path, DecompilerOptions options, JDOutput... outPlugins) {
        final MultiOutput out = new MultiOutput(outPlugins);
        if (!out.isValid()) {
            return;
        }
        final File file = new File(path).getAbsoluteFile();
        final String fileName = file.getName();
        out.init(file.getPath());
        // files - .class vs. others (zips), directories?
        if (file.isFile() && fileName.toLowerCase(Locale.ENGLISH).endsWith(".class")) {
            final String src = decompile(file.getParent(), file.getName(), options);
            out.processClass(file.getName(), src);
        } else {

        }

        out.commit();
    }

}
