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
package jd.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jd.core.options.DecompilerOptions;
import ch.qos.logback.classic.Level;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

/**
 * Command line arguments for jd-cli.
 */
public class CLIArguments implements DecompilerOptions {

    @Parameter(description = "[Files to decompile]")
    private final List<String> files = new ArrayList<String>();

    @Parameter(names = { "--help", "-h" }, description = "shows this help", help = true)
    private boolean help;

    @Parameter(names = { "--outputZipFile",
            "-oz" }, description = "takes a [zipFilePath] as a parameter and configures ZIP output for this path", converter = FileConverter.class)
    private File zipOutFile;

    @Parameter(names = { "--outputDir",
            "-od" }, description = "takes a [directoryPath] as a parameter and configures DIR output for this path", converter = FileConverter.class)
    private File dirOutFile;

    @Parameter(names = { "--outputConsole", "-oc" }, description = "enables output to system output stream")
    private boolean consoleOut;

    @Parameter(names = { "--skipResources", "-sr" }, description = "skips processing resources")
    private boolean skipResources;

    @Parameter(names = { "--displayLineNumbers", "-n" }, description = "displays line numbers in decompiled classes")
    private boolean displayLineNumbers;

    @Parameter(names = { "--skipMetadata", "-sm" }, description = "skips metadata in decompiled classes")
    private boolean skipMetadata;

    @Parameter(names = { "--showLocation", "-l" }, description = "displays Location info in decompiled classes metadata part")
    private boolean showLocation;

    @Parameter(names = { "--escapeUnicodeCharacters", "-eu" }, description = "escape unicode characters in decompiled classes")
    private boolean escapeUnicodeCharacters;

    @Parameter(names = { "--showPrefixThis", "-st" }, description = "prefix with 'this' where possible in decompiled classes")
    private boolean showPrefixThis;

    @Parameter(names = { "--realignLineNumbers", "-rn" }, description = "realign line numbers in decompiled classes")
    private boolean realignLineNumbers;

    @Parameter(names = { "--showDefaultConstructor", "-dc" }, description = "show default constructor in decompiled classes")
    private boolean showDefaultConstructor;

    @Parameter(names = { "--dontMergeEmptyLines",
            "-dm" }, description = "disables merging multiple empty lines into one in the decompiled classes")
    private boolean dontMergeEmptyLines;

    @Parameter(names = { "--logLevel",
            "-g" }, description = "takes [level] as parameter and sets it as the CLI log level. Possible values are: ALL, TRACE, DEBUG, INFO, WARN, ERROR, OFF", converter = LogLevelConverter.class)
    private final Level logLevel = Level.INFO;

    public List<String> getFiles() {
        return files;
    }

    public boolean isHelp() {
        return help;
    }

    public File getZipOutFile() {
        return zipOutFile;
    }

    public File getDirOutFile() {
        return dirOutFile;
    }

    public boolean isConsoleOut() {
        return consoleOut;
    }

    public boolean isSkipResources() {
        return skipResources;
    }

    public boolean isDisplayLineNumbers() {
        return displayLineNumbers;
    }

    public boolean isDisplayMetadata() {
        return !skipMetadata;
    }

    public boolean isDiscardLocation() {
        return !showLocation;
    }

    public boolean isEscapeUnicodeCharacters() {
        return escapeUnicodeCharacters;
    }

    public boolean isShowPrefixThis() {
        return showPrefixThis;
    }

    public boolean isRealignLineNumbers() {
        return realignLineNumbers;
    }

    public boolean isShowDefaultConstructor() {
        return showDefaultConstructor;
    }

    public boolean isMergeEmptyLines() {
        return !dontMergeEmptyLines;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public boolean isOutputPluginSpecified() {
        return consoleOut || zipOutFile != null || dirOutFile != null;
    }
}
