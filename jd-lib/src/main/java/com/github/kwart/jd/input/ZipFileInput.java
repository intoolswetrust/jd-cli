/*******************************************************************************
 * Copyright (C) 2015 Josef Cacek
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.github.kwart.jd.input;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.jd.core.v1.api.loader.LoaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kwart.jd.IOUtils;
import com.github.kwart.jd.JavaDecompiler;
import com.github.kwart.jd.loader.ByteArrayLoader;
import com.github.kwart.jd.output.JDOutput;

/**
 * Input plugin for ZIP files (e.g. jar, war, ...)
 *
 * @author Josef Cacek
 */
public class ZipFileInput extends AbstractFileJDInput {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipFileInput.class);

    /**
     * Constructor.
     */
    public ZipFileInput(String path) {
        super(path);
    }

    /**
     * Constructor.
     */
    public ZipFileInput(String filePath, String pattern) throws IllegalArgumentException {
        super(filePath, pattern);
    }

    /**
     * Parses all entres in the zip and decompiles it writing results to {@link JDOutput} instance.
     *
     * @see com.github.kwart.jd.input.JDInput#decompile(com.github.kwart.jd.JavaDecompiler, com.github.kwart.jd.output.JDOutput)
     */
    @Override
    public void decompile(JavaDecompiler javaDecompiler, JDOutput jdOutput) {
        if (javaDecompiler == null || jdOutput == null) {
            LOGGER.warn("Decompiler or JDOutput are null");
            return;
        }

        boolean skipResources = javaDecompiler.getOptions().isSkipResources();

        LOGGER.debug("Initializing decompilation of a zip file {}", file);

        jdOutput.init(javaDecompiler.getOptions(), file.getPath());
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new FileInputStream(file));
            ZipEntry entry = null;

            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    final String entryName = entry.getName();
                    if (skipThePath(entryName)) {
                        continue;
                    }
                    if (IOUtils.isClassFile(entryName)) {
                        if (IOUtils.isInnerClass(entryName)) {
                            // don't handle inner classes
                            LOGGER.trace("Skipping inner class {}", entryName);
                            continue;
                        }
                        LOGGER.debug("Decompiling {}", entryName);
                        try {
                            ByteArrayLoader bal = new ByteArrayLoader(zis, entryName);
                            jdOutput.processClass(IOUtils.cutClassSuffix(entryName),
                                    javaDecompiler.decompileClass(bal, entryName));
                        } catch (LoaderException e) {
                            LOGGER.error("LoaderException occured", e);
                        }
                    } else if (!skipResources) {
                        LOGGER.debug("Processing resource file {}", entryName);
                        jdOutput.processResource(entryName, zis);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("IOException occured", e);
        } finally {
            IOUtils.closeQuietly(zis);
        }
        jdOutput.commit();
    }
}
