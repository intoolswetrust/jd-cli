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
package jd.core.input;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jd.core.IOUtils;
import jd.core.JavaDecompiler;
import jd.core.options.DecompilerOptions;
import jd.core.options.OptionsManager;
import jd.core.output.JDOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Input plugin for ZIP files (e.g. jar, war, ...)
 *
 * @author Josef Cacek
 */
public class ZipFileInput extends AbstractFileJDInput {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipFileInput.class);

    /**
     * Constructor which takes
     *
     * @param path
     */
    public ZipFileInput(String path) {
        super(path);
    }

    /**
     * Parses all entres in the zip and decompiles it writing results to {@link JDOutput} instance.
     *
     * @see jd.core.input.JDInput#decompile(jd.core.JavaDecompiler, jd.core.output.JDOutput)
     */
    @Override
    public void decompile(JavaDecompiler javaDecompiler, JDOutput jdOutput) {
        if (javaDecompiler == null || jdOutput == null) {
            LOGGER.warn("Decompiler or JDOutput are null");
            return;
        }

        final DecompilerOptions options = OptionsManager.getOptions();
        final boolean skipResources = options.isSkipResources();

        LOGGER.debug("Initializing decompilation of a zip file {}", file);

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
                            LOGGER.trace("Skipping inner class {}", entryName);
                            return;
                        }
                        LOGGER.debug("Decompiling {}", entryName);
                        jdOutput.processClass(cutClassSuffix(entryName),
                                javaDecompiler.decompileClass(file.getPath(), entryName));
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
