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
	 * Closes given {@link InputStream} without throwing exception out of the
	 * method.
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
	 * Closes given {@link OutputStream} without throwing exception out of the
	 * method.
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
	 * Returns <code>true</code> iff the system property named by the
	 * <code>propName</code> argument exists and is equal to the string "true"
	 * or it doesn't exist and the <code>defaultVal</code> parameter is
	 * <code>true</code>.
	 * <p>
	 * This method is similar to {@link Boolean#getBoolean(String)}, but with
	 * default value as second parameter.
	 * 
	 * @param propName
	 * @param defaultVal
	 * @return
	 */
	public static boolean getBoolean(final String propName, final boolean defaultVal) {
		final boolean result = Boolean.parseBoolean(System.getProperty(propName, Boolean.toString(defaultVal)));
		LOGGER.trace("Reading boolean value for system property {} with default {}. Result: {}", propName, defaultVal,
				result);
		return result;
	}

}
