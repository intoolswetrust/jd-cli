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
package jd.core.output;

import static jd.core.JavaDecompilerConstants.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jd.core.IOUtils;

public class DirOutput extends AbstractJDOutput {

    private final File dir;

    public DirOutput(final File outputDir) throws FileNotFoundException {
        dir = outputDir;
        if (!outputDir.exists())
            outputDir.mkdirs();
        if (!outputDir.isDirectory()) {
            throw new FileNotFoundException(
                    "Provided file either is not a directory or it didn't exist and the mkdirs() command failed - "
                            + dir.getAbsolutePath());
        }
    }

    public void processClass(String className, String src) {
        if (className == null || src == null)
            return;
        final File decompiledFile = new File(dir, className + JAVA_SUFFIX);
        final File parentDir = decompiledFile.getParentFile();
        if (!parentDir.exists())
            parentDir.mkdirs();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(decompiledFile);
            fos.write(src.getBytes(UTF_8));
        } catch (IOException e) {
            if (debug) {
                e.printStackTrace();
            }
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }

    public void processResource(String fileName, InputStream is) {
        if (skipResources || fileName == null || is == null) {
            return;
        }
        final File tmpFile = new File(dir, fileName);
        final File parentDir = tmpFile.getParentFile();
        if (!parentDir.exists())
            parentDir.mkdirs();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(tmpFile);
            IOUtils.copy(is, fos);
        } catch (IOException e) {
            if (debug) {
                e.printStackTrace();
            }
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }

}
