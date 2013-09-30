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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import jd.core.IOUtils;
import jd.core.options.OptionsManager;
import jd.core.output.JDOutput;
import jd.ide.intellij.JavaDecompiler;

/**
 * Input plugin which takes a directory as an input.
 * 
 * @author Josef Cacek
 */
public class DirInput extends AbstractFileJDInput {

    public DirInput(String path) {
        super(path);
        if (!file.isDirectory())
            throw new IllegalArgumentException("Path doesn't denote a directory.");

    }

    @Override
    public void decompile(JavaDecompiler javaDecompiler, JDOutput jdOutput) {
        if (javaDecompiler == null || jdOutput == null) {
            return;
        }

        jdOutput.init(file.getPath());
        for (File f : file.listFiles()) {
            processFile(javaDecompiler, jdOutput, "", f);
        }
        jdOutput.commit();
    }

    private void processFile(JavaDecompiler javaDecompiler, JDOutput jdOutput, String pathPrefix, File nextFile) {

        final String fileName = nextFile.getName();
        final String nameWithPath = pathPrefix + fileName;
        if (nextFile.isDirectory()) {
            for (File f : nextFile.listFiles()) {
                processFile(javaDecompiler, jdOutput, pathPrefix + fileName + "/", f);
            }
        } else {
            if (isClassFile(fileName)) {
                if (isInnerClass(fileName)) {
                    // don't handle inner classes
                    return;
                }
                jdOutput.processClass(cutClassSuffix(nameWithPath), javaDecompiler.decompileClass(file.getPath(), nameWithPath));
            } else if (!OptionsManager.getOptions().isSkipResources()) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(nextFile);
                    jdOutput.processResource(nameWithPath, fis);
                } catch (IOException ioe) {
                    if (OptionsManager.getOptions().isDebug())
                        ioe.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(fis);
                }
            }

        }
    }
}
