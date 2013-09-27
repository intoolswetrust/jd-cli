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
package jd.core.input;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jd.core.IOUtils;
import jd.core.options.DecompilerOptions;
import jd.core.options.OptionsManager;
import jd.core.output.JDOutput;
import jd.ide.intellij.JavaDecompiler;

public class ZipFileInput extends AbstractFileJDInput {

    public ZipFileInput(String path) {
        super(path);
    }

    @Override
    public void decompile(JavaDecompiler javaDecompiler, JDOutput jdOutput) {
        if (javaDecompiler == null || jdOutput == null) {
            return;
        }

        final DecompilerOptions options = OptionsManager.getOptions();
        final boolean debug = options.isDebug();
        final boolean skipResources = options.isSkipResources();

        jdOutput.init(file.getPath());
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new FileInputStream(file));
            ZipEntry entry = null;

            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    final String entryName = entry.getName();
                    if (isClassFile(entryName)) {
                        if (isInnerClass(entryName)) {
                            // don't handle inner classes
                            break;
                        }
                        jdOutput.processClass(cutClassSuffix(entryName),
                                javaDecompiler.decompileClass(file.getPath(), entryName));
                    } else if (!skipResources) {
                        jdOutput.processResource(entryName, zis);
                    }
                }
            }
        } catch (IOException e) {
            if (debug) {
                e.printStackTrace();
            }
        } finally {
            IOUtils.closeQuietly(zis);
        }
        jdOutput.commit();
    }
}
