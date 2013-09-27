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
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jd.core.IOUtils;

public class ZipOutput extends AbstractJDOutput {

    private final ZipOutputStream zos;
    private final boolean close;

    public ZipOutput(final OutputStream os) {
        if (os == null) {
            throw new NullPointerException("OutputStream can't be null.");
        }
        zos = new ZipOutputStream(os);
        close = false;
    }

    public ZipOutput(final File file) throws FileNotFoundException {
        zos = new ZipOutputStream(new FileOutputStream(file));
        close = true;
    }

    public void processClass(String className, String src) {
        if (className == null || src == null)
            return;
        try {
            zos.putNextEntry(new ZipEntry(className + JAVA_SUFFIX));
            zos.write(src.getBytes(UTF_8));
            zos.closeEntry();
        } catch (IOException e) {
            if (debug) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void commit() {
        try {
            if (close) {
                zos.close();
            } else {
                zos.finish();
            }
        } catch (IOException e) {
            if (debug) {
                e.printStackTrace();
            }
        }
    }

    public void processResource(String fileName, InputStream is) {
        if (skipResources || fileName == null || is == null) {
            return;
        }
        try {
            zos.putNextEntry(new ZipEntry(fileName));
            IOUtils.copy(is, zos);
            zos.closeEntry();
        } catch (IOException e) {
            if (debug) {
                e.printStackTrace();
            }
        }
    }

}
