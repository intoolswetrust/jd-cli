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

import static jd.core.JavaDecompilerConstants.*;

import java.io.File;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract parent for file-based input plugins (JAR, directory, ...).
 * 
 * @author Josef Cacek
 */
public abstract class AbstractFileJDInput implements JDInput {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileJDInput.class);

    protected final File file;

    /**
     * Constructor based on an existing file path.
     * 
     * @param filePath path to input file
     * @throws IllegalArgumentException path doesn't denote an existing file
     */
    public AbstractFileJDInput(final String filePath) throws IllegalArgumentException {
        LOGGER.trace("Creating JDInput instance for file {}", filePath);
        file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("Path doesn't denote an existing file.");
        }
    }

    /**
     * Returns true if given file path ends with ".class"
     * 
     * @param filePath
     * @return
     */
    protected boolean isClassFile(final String filePath) {
        return filePath.toLowerCase(Locale.ENGLISH).endsWith(CLASS_SUFFIX);
    }

    /**
     * Returns true if given file path ends with ".class" and it contains "$" in the name.
     * 
     * @param filePath
     * @return
     */
    protected boolean isInnerClass(final String filePath) {
        return filePath.toLowerCase(Locale.ENGLISH).matches("\\$.*\\.class$");
    }

    /**
     * Removes ".class".length() number of character from the end of the input stream.
     * 
     * @param classFilePath
     * @return
     */
    protected String cutClassSuffix(final String classFilePath) {
        return classFilePath.substring(0, classFilePath.length() - CLASS_SUFFIX_LEN);
    }
}
