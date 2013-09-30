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

import static jd.core.JavaDecompilerConstants.JAVA_SUFFIX;
import static jd.core.JavaDecompilerConstants.UTF_8;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jd.core.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link JDOutput} implementation, which stores decompiled classes to a stream
 * or file as a zip.
 * 
 * @author Josef Cacek
 */
public class ZipOutput extends AbstractJDOutput {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZipOutput.class);

	private final ZipOutputStream zos;
	private final boolean close;

	/**
	 * {@link OutputStream} based constructor.
	 * 
	 * @param os
	 *            OutputStream to which the zip content should be written (not-
	 *            <code>null</code>)
	 */
	public ZipOutput(final OutputStream os) {
		if (os == null) {
			throw new NullPointerException("OutputStream can't be null.");
		}
		zos = new ZipOutputStream(os);
		close = false;
	}

	/**
	 * {@link File} based constructor. It creates the file (and parent
	 * directories).
	 * 
	 * @param file
	 *            instance of {@link File} (not-<code>null</code>)
	 * @throws FileNotFoundException
	 *             file creation failed for some reason
	 */
	public ZipOutput(final File file) throws FileNotFoundException {
		if (file == null) {
			throw new NullPointerException("File can't be null.");
		}
		final File parentDir = file.getAbsoluteFile().getParentFile();
		if (!parentDir.exists()) {
			parentDir.mkdirs();
		}
		if (!parentDir.isDirectory()) {
			throw new FileNotFoundException(
					"Zip file parent directory can't be created, check if the path is corret and you have sufficient permissions.");
		}
		zos = new ZipOutputStream(new FileOutputStream(file));
		close = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jd.core.output.JDOutput#processClass(java.lang.String, java.lang.String)
	 */
	public void processClass(final String className, final String src) {
		if (className == null || src == null)
			return;
		try {
			zos.putNextEntry(new ZipEntry(className + JAVA_SUFFIX));
			zos.write(src.getBytes(UTF_8));
			zos.closeEntry();
		} catch (IOException e) {
			LOGGER.error("Exception occured during writing decompiled class {} to a ZIP output.", className, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jd.core.output.JDOutput#processResource(java.lang.String, java.io.InputStream)
	 */
	public void processResource(final String fileName, final InputStream is) {
		if (skipResources || fileName == null || is == null) {
			return;
		}
		try {
			zos.putNextEntry(new ZipEntry(fileName));
			IOUtils.copy(is, zos);
			zos.closeEntry();
		} catch (IOException e) {
			LOGGER.error("Exception occured during writing resource {} to a ZIP output.", fileName, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jd.core.output.AbstractJDOutput#commit()
	 */
	@Override
	public void commit() {
		try {
			if (close) {
				zos.close();
			} else {
				zos.finish();
			}
		} catch (IOException e) {
			LOGGER.error("Exception occured during finishing ZIP output.", e);
		}
	}

}
