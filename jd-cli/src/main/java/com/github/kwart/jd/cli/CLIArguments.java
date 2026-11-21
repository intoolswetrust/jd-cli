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
package com.github.kwart.jd.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.classic.Level;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import com.github.kwart.jd.options.DecompilerOptions;

/**
 * Command line arguments for jd-cli.
 */
public class CLIArguments implements DecompilerOptions {

    @Parameter(description = "[Files to decompile]")
    private final List<String> files = new ArrayList<String>();

    @Parameter(names = { "--help", "-h" }, description = "shows this help", help = true)
    private boolean help;

    @Parameter(names = { "--version", "-v" }, description = "shows the version", help = true)
    private boolean version;

    @Parameter(names = { "--outputZipFile", "-oz" },
            description = "takes a [zipFilePath] as a parameter and configures ZIP output for this path",
            converter = FileConverter.class)
    private File zipOutFile;

    @Parameter(names = { "--outputDir", "-od" },
            description = "takes a [directoryPath] as a parameter and configures a flat DIR output for this path",
            converter = FileConverter.class)
    private File dirOutFile;

    @Parameter(names = { "--outputDirStructured", "-ods" },
            description = "takes a [directoryPath] as a parameter and configures a structured DIR output for this path",
            converter = FileConverter.class)
    private File dirOutFileStructured;

    @Parameter(names = { "--outputConsole", "-oc" }, description = "enables output to system output stream")
    private boolean consoleOut;

    @Parameter(names = { "--skipResources", "-sr" }, description = "skips processing resources")
    private boolean skipResources;

    @Parameter(names = { "--displayLineNumbers", "-n" }, description = "displays line numbers in decompiled classes")
    private boolean displayLineNumbers;

    @Parameter(names = { "--realignLineNumbers", "-rn" },
            description = "re-lays out the decompiled code (using jd-core's line number realignment) so that statements land "
                    + "on the same line numbers as in the original source, e.g. to set breakpoints or read stack traces against "
                    + "it. Spacing between members is adjusted, members and inner classes which shared a line in the original are "
                    + "joined on one line again and members may be re-ordered (e.g. the constructor of an inner class moved behind "
                    + "its methods) where the original numbering requires it. It is a best-effort layout, not a plain padding of "
                    + "blank lines. Needs line number debug info in "
                    + "the classes (compiled with -g or default javac options); without it nothing changes. Can be combined with "
                    + "--displayLineNumbers")
    private boolean realignLineNumbers;

    @Parameter(names = { "--escapeUnicodeCharacters", "-eu" }, description = "escape unicode characters in decompiled classes")
    private boolean escapeUnicodeCharacters;

    @Parameter(names = { "--serialProcessing", "-sp" }, description = "don't use parallel processing")
    private boolean serialProcessing;

    @Parameter(names = { "--logLevel", "-g" },
            description = "takes [level] as parameter and sets it as the CLI log level."
                    + " Possible values are: ALL, TRACE, DEBUG, INFO, WARN, ERROR, OFF",
            converter = LogLevelConverter.class)
    private final Level logLevel = Level.INFO;

    @Parameter(names = { "--pattern", "-p" }, description =
            "RegExp pattern which the to-be-decompiled file has to match. Not matching entries are skipped.")
    private final String pattern = null;

    public List<String> getFiles() {
        return files;
    }

    public boolean isHelp() {
        return help;
    }

    public boolean isVersion() {
        return version;
    }

    public File getZipOutFile() {
        return zipOutFile;
    }

    public File getDirOutFile() {
        return dirOutFile;
    }

    public File getDirOutFileStructured() {
        return dirOutFileStructured;
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

    public boolean isRealignLineNumbers() {
        return realignLineNumbers;
    }

    public boolean isEscapeUnicodeCharacters() {
        return escapeUnicodeCharacters;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public String getPattern() {
        return pattern;
    }

    public boolean isOutputPluginSpecified() {
        return consoleOut || zipOutFile != null || dirOutFile != null || dirOutFileStructured != null;
    }

    @Override
    public boolean isParallelProcessingAllowed() {
        return ! serialProcessing;
    }
}
