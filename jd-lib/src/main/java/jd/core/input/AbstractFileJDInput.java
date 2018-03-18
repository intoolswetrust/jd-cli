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
