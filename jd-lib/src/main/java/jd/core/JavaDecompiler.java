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

import static jd.core.JavaDecompilerConstants.CLASS_SUFFIX;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jd.commonide.IdeDecompiler;
import jd.commonide.preferences.IdePreferences;
import jd.core.options.DecompilerOptions;
import jd.core.options.OptionsManager;
import jd.core.parser.SimpleClassParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decompiler implementation, which uses an JD Core to decompile classes.
 */
public class JavaDecompiler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaDecompiler.class);

    /**
     * Decompile a single class from given base location.
     *
     * @param basePath path to directory or an archive which contains the class in its subtree
     * @param className class name (with path relative to basePath)
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
     * Actual JD decompiler call.
     *
     * @param basePath Path to the root of the classpath, either a path to a directory or a path to a jar file.
     * @param internalTypeName internal name of the type.
     * @return Decompiled class text.
     */
    public String decompile(String basePath, String internalTypeName) {
        // Load preferences
        DecompilerOptions options = OptionsManager.getOptions();

        boolean showDefaultConstructor = options.isShowDefaultConstructor();
        boolean realignmentLineNumber = options.isRealignLineNumbers();
        boolean showPrefixThis = options.isShowPrefixThis();
        boolean mergeEmptyLines = options.isMergeEmptyLines();
        boolean unicodeEscape = options.isEscapeUnicodeCharacters();
        boolean showLineNumbers = options.isDisplayLineNumbers();
        boolean showMetadata = options.isDisplayMetadata();

        // Create preferences
        IdePreferences preferences = new IdePreferences(showDefaultConstructor, realignmentLineNumber, showPrefixThis,
                mergeEmptyLines, unicodeEscape, showLineNumbers, showMetadata);

        // Decompile
        return IdeDecompiler.decompile(preferences, basePath, internalTypeName);
    }

    /**
     * Creates a single purpose JAR with one class - the main reason is to fix class location in the directory tree.
     *
     * @param classFile a class file
     * @param correctPath correct package-path of the class
     * @return newly created JAR with the class (or null if {@link IOException} occurs)
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
