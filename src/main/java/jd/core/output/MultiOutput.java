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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import jd.core.IOUtils;

public class MultiOutput extends AbstractJDOutput {

    private final JDOutput[] outputPlugins;
    private final int validPlugins;

    public MultiOutput(final JDOutput... plugins) {
        outputPlugins = plugins;
        int tmpValid = 0;
        if (outputPlugins != null) {
            for (JDOutput jdOut : outputPlugins) {
                if (jdOut != null) {
                    tmpValid++;
                }
            }
        }
        validPlugins = tmpValid;
    }

    public boolean isValid() {
        return validPlugins > 0;
    }

    @Override
    public void init(final String basePath) {
        if (!isValid())
            return;
        for (JDOutput jdOut : outputPlugins) {
            if (jdOut != null) {
                try {
                    jdOut.init(basePath);
                } catch (Exception e) {
                    if (debug)
                        e.printStackTrace();
                }
            }
        }
    }

    public void processClass(final String className, final String src) {
        if (!isValid())
            return;
        for (JDOutput jdOut : outputPlugins) {
            if (jdOut != null) {
                try {
                    jdOut.processClass(className, src);
                } catch (Exception e) {
                    if (debug)
                        e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void commit() {
        if (!isValid())
            return;
        for (JDOutput jdOut : outputPlugins) {
            if (jdOut != null) {
                try {
                    jdOut.commit();
                } catch (Exception e) {
                    if (debug)
                        e.printStackTrace();
                }
            }
        }
    }

    public void processResource(String fileName, InputStream is) {
        switch (validPlugins) {
            case 0:
                return;
            case 1:
                for (JDOutput jdOut : outputPlugins) {
                    if (jdOut != null) {
                        try {
                            jdOut.processResource(fileName, is);
                        } catch (Exception e) {
                            if (debug)
                                e.printStackTrace();
                        }
                    }
                }
            default:
                File f = null;
                FileOutputStream fos = null;
                try {
                    try {
                        f = File.createTempFile("jdTemp-", ".res");
                        fos = new FileOutputStream(f);
                        IOUtils.copy(is, fos);
                    } finally {
                        IOUtils.closeQuietly(fos);
                    }
                    for (JDOutput jdOut : outputPlugins) {
                        if (jdOut != null) {
                            FileInputStream fis = null;
                            try {
                                fis = new FileInputStream(f);
                                jdOut.processResource(fileName, fis);
                            } catch (Exception e) {
                                if (debug)
                                    e.printStackTrace();
                            } finally {
                                IOUtils.closeQuietly(fis);
                            }
                        }
                    }
                } catch (Exception e) {
                    if (debug)
                        e.printStackTrace();
                } finally {
                    if (f != null)
                        f.delete();
                }
                break;
        }
    }
}
