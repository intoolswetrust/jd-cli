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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jd.core.IOUtils;
import jd.core.options.DecompilerOptions;
import jd.core.options.OptionsManager;
import jd.core.output.JDOutput;
import jd.ide.intellij.JavaDecompiler;

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
	 * Parses all entres in the zip and decompiles it writing results to
	 * {@link JDOutput} instance.
	 * 
	 * @see jd.core.input.JDInput#decompile(jd.ide.intellij.JavaDecompiler,
	 *      jd.core.output.JDOutput)
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
