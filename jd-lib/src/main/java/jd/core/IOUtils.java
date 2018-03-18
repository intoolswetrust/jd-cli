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
package jd.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class with helper IO and System methods.
 */
public final class IOUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);

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
        final byte[] buffer = new byte[4096];
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
     * Returns <code>true</code> iff the system property named by the <code>propName</code> argument exists and is equal to the
     * string "true" or it doesn't exist and the <code>defaultVal</code> parameter is <code>true</code>.
     * <p>
     * This method is similar to {@link Boolean#getBoolean(String)}, but with default value as second parameter.
     *
     * @param propName
     * @param defaultVal
     * @return
     */
    public static boolean getBoolean(final String propName, final boolean defaultVal) {
        final boolean result = Boolean.parseBoolean(System.getProperty(propName, Boolean.toString(defaultVal)));
        LOGGER.trace("Reading boolean value for system property {} with default {}. Result: {}", propName, defaultVal, result);
        return result;
    }

}
