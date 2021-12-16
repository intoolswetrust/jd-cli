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
package com.github.kwart.jd;

import static com.github.kwart.jd.JavaDecompilerConstants.CLASS_SUFFIX;
import static com.github.kwart.jd.JavaDecompilerConstants.CLASS_SUFFIX_LEN;
import static com.github.kwart.jd.JavaDecompilerConstants.JAR_SUFFIX;
import static com.github.kwart.jd.JavaDecompilerConstants.JAR_SUFFIX_LEN;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class with helper IO and System methods.
 */
public final class IOUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);

    private static final int BUFFER_SIZE = 4096;

    /**
     * Private ctor.
     */
    private IOUtils() {
    }

    /**
     * Copies data from an {@link InputStream} to an {@link OutputStream}.
     *
     * @param is
     * @param os
     * @return
     * @throws IOException
     */
    public static long copy(final InputStream is, final OutputStream os) throws IOException {
        LOGGER.trace("Copying inputStream to outputStream");
        final byte[] buffer = new byte[BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = is.read(buffer))) {
            os.write(buffer, 0, n);
            count += n;
        }
        LOGGER.trace("{} bytes copied from IS to OS", count);
        return count;
    }

    /**
     * Closes given {@link InputStream} without throwing exception out of the method.
     *
     * @param is
     */
    public static void closeQuietly(final InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException ioe) {
            LOGGER.debug("Closing InputStream failed.", ioe);
        }
    }

    /**
     * Closes given {@link OutputStream} without throwing exception out of the method.
     *
     * @param os
     */
    public static void closeQuietly(final OutputStream os) {
        try {
            if (os != null) {
                os.close();
            }
        } catch (IOException ioe) {
            LOGGER.debug("Closing OutputStream failed.", ioe);
        }
    }

    /**
     * Returns true if given file path ends with ".class"
     *
     * @param filePath
     * @return
     */
    /**
     * @param filePath
     * @return
     */
    public static boolean isClassFile(final String filePath) {
        return filePath.toLowerCase(Locale.ENGLISH).endsWith(CLASS_SUFFIX);
    }

    /**
     * Returns true if given file path ends with ".jar"
     *
     * @param filePath
     * @return
     */
    /**
     * @param filePath
     * @return
     */
    public static boolean isJarFile(final String filePath) {
        return filePath.toLowerCase(Locale.ENGLISH).endsWith(JAR_SUFFIX);
    }

    /**
     * Returns true if given file path ends with ".class" and it contains "$" in the name.
     *
     * @param filePath
     * @return
     */
    public static boolean isInnerClass(final String filePath) {
        return filePath.toLowerCase(Locale.ENGLISH).matches("^.+\\$.+\\.class$");
    }

    /**
     * Removes ".class".length() number of character from the end of the input stream.
     *
     * @param classFilePath
     * @return
     */
    public static String cutClassSuffix(final String classFilePath) {
        return classFilePath.substring(0, classFilePath.length() - CLASS_SUFFIX_LEN);
    }
    /**
     * Removes ".jar".length() number of character from the end of the input stream.
     *
     * @param jarFilePath
     * @return
     */
    public static String cutJarSuffix(final String jarFilePath) {
        return jarFilePath.substring(0, jarFilePath.length() - JAR_SUFFIX_LEN);
    }
}
