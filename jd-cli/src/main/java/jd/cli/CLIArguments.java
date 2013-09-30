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
	private List<String> files = new ArrayList<String>();

	@Parameter(names = { "--help", "-h" }, description = "Show this help", help = true)
	private boolean help;

	@Parameter(names = { "--outputZipFile", "-oz" }, description = "Output to a zipped file with given path", converter = FileConverter.class)
	private File zipOutFile;

	@Parameter(names = { "--outputDir", "-od" }, description = "Output to a directory with given path", converter = FileConverter.class)
	private File dirOutFile;

	@Parameter(names = { "--outputConsole", "-oc" }, description = "Output to system output stream")
	private boolean consoleOut;

	@Parameter(names = { "--skipResources", "-sr" }, description = "Skip processing resources")
	private boolean skipResources;

	@Parameter(names = { "--displayLineNumbers", "-n" }, description = "Include line numbers in decompiled classes")
	private boolean displayLineNumbers;

	@Parameter(names = { "--skipMetadata", "-sm" }, description = "Don't include metadata in decompiled classes")
	private boolean skipMetadata;

	@Parameter(names = { "--showLocation", "-l" }, description = "Include Location info in decompiled classes metadata part")
	private boolean showLocation;

	@Parameter(names = { "--logLevel", "-g" }, description = "Log level, one of: ALL, TRACE, DEBUG, INFO, WARN, ERROR, OFF", converter = LogLevelConverter.class)
	private Level logLevel = Level.INFO;

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

	public Level getLogLevel() {
		return logLevel;
	}

	public boolean isOutputPluginSpecified() {
		return consoleOut || zipOutFile != null || dirOutFile != null;
	}
}
