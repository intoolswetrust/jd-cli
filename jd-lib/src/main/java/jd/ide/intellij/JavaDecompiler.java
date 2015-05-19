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
package jd.ide.intellij;

import static jd.core.JavaDecompilerConstants.CLASS_SUFFIX;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jd.core.IOUtils;
import jd.core.JavaDecompilerConstants;
import jd.core.options.DecompilerOptions;
import jd.core.options.OptionsManager;
import jd.core.parser.SimpleClassParser;

import com.intellij.openapi.application.ApplicationManager;
import jd.ide.intellij.config.JDPluginComponent;
import jd.commonide.IdeDecompiler;
import jd.commonide.preferences.IdePreferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decompiler implementation, which uses an IJ plugin natives to decompile
 * classes. During the static initialization it unpacks the native library to a
 * temporary location and loads it.
 */
public class JavaDecompiler {

	private static final Logger LOGGER = LoggerFactory.getLogger(JavaDecompiler.class);

	/**
	 * Decompile a single class from given base location.
	 * 
	 * @param basePath
	 *            path to directory or an archive which contains the class in
	 *            its subtree
	 * @param className
	 *            class name (with path relative to basePath)
	 * @return decompiled class as a String or null if sth fails
	 */
	public String decompileClass(String basePath, String className) {
		LOGGER.debug("Decompiling class {} from base path {}", className, basePath);
		if (className == null || basePath == null) {
			LOGGER.warn("Classname or basename was null");
			return null;
		}

		String src = decompile(basePath, className);
		final String LS = System.getProperty("line.separator");
		if (src == null || src.startsWith("class " + LS + "{")) {
			// decompilation failed - wrong package?
			final File baseFile = new File(basePath);
			if (baseFile.isDirectory()) {
				File classFile = new File(baseFile, className);
				if (!classFile.isFile())
					return null;
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(classFile);
					final SimpleClassParser simpleClassParser = new SimpleClassParser(fis);
					final String correctPath = simpleClassParser.getClassName() + CLASS_SUFFIX;
					final File tempJar = createTempJar(classFile, correctPath);
					if (tempJar == null)
						return null;
					src = decompile(tempJar.getPath(), correctPath);
					tempJar.delete();
				} catch (Exception e) {
					LOGGER.error("Exception occured during decompilation", e);
					return null;
				} finally {
					IOUtils.closeQuietly(fis);
				}
			}
		}

		final DecompilerOptions options = OptionsManager.getOptions();
		if (src != null && options.isDisplayMetadata() && options.isDiscardLocation()) {
			src = src.replaceFirst("(/\\* Location:).*", "$1");
		}

		return src;
	}

	/**
	 * Actual call to the native lib.
	 *
	 * @param basePath          Path to the root of the classpath, either a path to a directory or a path to a jar file.
	 * @param internalTypeName  internal name of the type.
	 * @return Decompiled class text.
	 */
	public String decompile(String basePath, String internalTypeName) {
		// Load preferences
		JDPluginComponent jdPluginComponent = ApplicationManager.getApplication().getComponent(JDPluginComponent.class);

		boolean showDefaultConstructor = jdPluginComponent.isShowDefaultConstructorEnabled();
		boolean realignmentLineNumber = jdPluginComponent.isRealignLineNumbersEnabled();
		boolean showPrefixThis = !jdPluginComponent.isOmitPrefixThisEnabled();
		boolean mergeEmptyLines = false;
		boolean unicodeEscape = jdPluginComponent.isEscapeUnicodeCharactersEnabled();
		boolean showLineNumbers = jdPluginComponent.isShowLineNumbersEnabled();
		boolean showMetadata = jdPluginComponent.isShowMetadataEnabled();

		// Create preferences
		IdePreferences preferences = new IdePreferences(
			showDefaultConstructor, realignmentLineNumber, showPrefixThis,
			mergeEmptyLines, unicodeEscape, showLineNumbers, showMetadata);

		// Decompile
		return IdeDecompiler.decompile(preferences, basePath, internalTypeName);
	}

	/**
	 * Creates a single purpose JAR with one class - the main reason is to fix
	 * class location in the directory tree.
	 * 
	 * @param classFile
	 *            a class file
	 * @param correctPath
	 *            correct package-path of the class
	 * @return newly created JAR with the class (or null if {@link IOException}
	 *         occurs)
	 */
	private File createTempJar(File classFile, String correctPath) {
		LOGGER.trace("Creating temporary JAR file for {} with correct path: {}", classFile, correctPath);
		ZipOutputStream zos = null;
		File tempJar = null;
		FileInputStream fis = null;
		try {
			tempJar = File.createTempFile("jdTemp-", ".jar");
			zos = new ZipOutputStream(new FileOutputStream(tempJar));
			zos.putNextEntry(new ZipEntry(correctPath));
			fis = new FileInputStream(classFile);
			IOUtils.copy(fis, zos);
			zos.closeEntry();
		} catch (IOException e) {
			LOGGER.error("Temporary JAR creation failed", e);
		} finally {
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(zos);
		}
		return tempJar;
	}
}
