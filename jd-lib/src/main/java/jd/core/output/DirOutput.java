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

import jd.core.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link JDOutput} implementation, which stores decompiled classes to a
 * directory.
 * 
 * @author Josef Cacek
 */
public class DirOutput extends AbstractJDOutput {

	private static final Logger LOGGER = LoggerFactory.getLogger(DirOutput.class);

	private final File dir;

	/**
	 * Constructor which takes directory path as a parameter it tries to create
	 * the directory if it doens't exist.
	 * 
	 * @param outputDir
	 *            directory path to save output not-<code>null</code>
	 * @throws FileNotFoundException
	 *             if the directory can't be created or if the path doesn't
	 *             denote a directory.
	 */
	public DirOutput(final File outputDir) throws FileNotFoundException, NullPointerException {
		if (outputDir == null) {
			throw new NullPointerException("Null directory given");
		}
		dir = outputDir;
		if (!outputDir.exists())
			outputDir.mkdirs();
		if (!outputDir.isDirectory()) {
			throw new FileNotFoundException(
					"Provided file either is not a directory or it didn't exist and the mkdirs() command failed - "
							+ dir.getAbsolutePath());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jd.core.output.JDOutput#processClass(java.lang.String, java.lang.String)
	 */
	public void processClass(String className, String src) {
		if (className == null || src == null) {
			LOGGER.warn("Class name or java source is null");
			return;
		}
		final File decompiledFile = new File(dir, className + JAVA_SUFFIX);
		LOGGER.trace("Writing decompiled class to {}", decompiledFile);
		createDir(decompiledFile.getParentFile());
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(decompiledFile);
			fos.write(src.getBytes(UTF_8));
		} catch (IOException e) {
			LOGGER.error("Writing decompiled class to file {} failed.", decompiledFile, e);
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jd.core.output.JDOutput#processResource(java.lang.String, java.io.InputStream)
	 */
	public void processResource(String fileName, InputStream is) {
		if (skipResources || fileName == null || is == null) {
			LOGGER.trace("Skipping resource {}", fileName);
			return;
		}
		final File tmpFile = new File(dir, fileName);
		LOGGER.trace("Storing resource {}", tmpFile);
		createDir(tmpFile.getParentFile());
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(tmpFile);
			IOUtils.copy(is, fos);
		} catch (IOException e) {
			LOGGER.error("Writing resource to file {} failed.", tmpFile, e);
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	/**
	 * Creates given directory if it doesn't exist.
	 * 
	 * @param dir
	 */
	private void createDir(final File dir) {
		if (!dir.exists()) {
			boolean dirCreated = dir.mkdirs();
			LOGGER.trace("Creating directory {} finished with result: {}", dir, dirCreated);
		}
	}

}
