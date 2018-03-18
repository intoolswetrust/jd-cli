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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import jd.core.IOUtils;
import jd.core.JavaDecompiler;
import jd.core.options.OptionsManager;
import jd.core.output.JDOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Input plugin which takes a directory as an input.
 *
 * @author Josef Cacek
 */
public class DirInput extends AbstractFileJDInput {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirInput.class);

    public DirInput(String path) {
        super(path);
        if (!file.isDirectory())
            throw new IllegalArgumentException("Path doesn't denote a directory.");

    }

    @Override
    public void decompile(JavaDecompiler javaDecompiler, JDOutput jdOutput) {
        if (javaDecompiler == null || jdOutput == null) {
            LOGGER.warn("Decompiler or JDOutput are null");
            return;
        }

        LOGGER.debug("Initializing decompilation of directory {}", file);
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
            LOGGER.trace("Processing directory {}", nextFile);
            for (File f : nextFile.listFiles()) {
                processFile(javaDecompiler, jdOutput, pathPrefix + fileName + "/", f);
            }
        } else {
            if (isClassFile(fileName)) {
                if (isInnerClass(fileName)) {
                    // don't handle inner classes
                    LOGGER.trace("Skipping inner class {}", nextFile);
                    return;
                }
                LOGGER.debug("Decompiling {}", nextFile);
                jdOutput.processClass(cutClassSuffix(nameWithPath),
                        javaDecompiler.decompileClass(file.getPath(), nameWithPath));
            } else if (!OptionsManager.getOptions().isSkipResources()) {
                LOGGER.debug("Processing resource file {}", nextFile);
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(nextFile);
                    jdOutput.processResource(nameWithPath, fis);
                } catch (IOException ioe) {
                    LOGGER.error("Resource processing failed for {}", nextFile, ioe);
                } finally {
                    IOUtils.closeQuietly(fis);
                }
            }

        }
    }
}
