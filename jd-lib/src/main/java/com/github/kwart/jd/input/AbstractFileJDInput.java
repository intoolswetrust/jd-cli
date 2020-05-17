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

import java.io.File;

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
    private final String pattern;

    /**
     * Constructor based on an existing file path.
     *
     * @param filePath path to input file
     * @throws IllegalArgumentException path doesn't denote an existing file
     */
    public AbstractFileJDInput(String filePath) throws IllegalArgumentException {
        this(filePath, null);
    }

    public AbstractFileJDInput(String filePath, String pattern) throws IllegalArgumentException {
        LOGGER.trace("Creating JDInput instance for file {} and pattern {}", filePath, pattern);
        this.pattern = pattern == null ? pattern : (".*" + pattern + ".*");
        file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("Path doesn't denote an existing file.");
        }
    }

    /**
     * Returns {@code true} when a pattern is configured and given path doesn't match it.
     */
    protected boolean skipThePath(String path) {
        boolean skip = pattern != null && path != null && !path.matches(pattern);
        if (skip) {
            LOGGER.debug("Skipping the path {} as it doesn't match the pattern {}", path, pattern);
        }
        return skip;
    }
}
