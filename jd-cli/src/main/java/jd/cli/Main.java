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

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jd.core.IOUtils;
import jd.core.JavaDecompilerConstants;
import jd.core.input.ClassFileInput;
import jd.core.input.DirInput;
import jd.core.input.JDInput;
import jd.core.input.ZipFileInput;
import jd.core.options.OptionsManager;
import jd.core.output.DirOutput;
import jd.core.output.JDOutput;
import jd.core.output.MultiOutput;
import jd.core.output.PrintStreamOutput;
import jd.core.output.ZipOutput;
import jd.ide.intellij.JavaDecompiler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

/**
 * Main class of jd-cli.
 */
public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	/**
	 * The {@link #main(String[])}!
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final CLIArguments cliArguments = new CLIArguments();
		final ExtCommander jCmd = new ExtCommander(cliArguments, args);
		jCmd.setProgramName("java -jar jd-cli.jar");
		jCmd.setUsageHead("jd-cli version "
				+ JavaDecompilerConstants.VERSION
				+ "\nThe jd-cli is a command line interface for the Java Decompiler (http://java.decompiler.free.fr/), "
				+ "it decompile classes, zip archives "
				+ "(.zip, .jar, .war, ...) and directories containing classes. Each supported input type has configured corresponding "
				+ "default output type (class->screen, zip->zip, directory->directory). Man can simply override the output type "
				+ "by specifying a command line parameter (-oc, -od, -oz). Multiple output type parameters can be used at once.");
		jCmd.setUsageTail("Examples:\n\n" //
				+ "$ java -jar jd-cli.jar HelloWorld.class\n" //
				+ " Shows decompiled class on a screen\n\n" //
				+ "$ java -jar jd-cli.jar --skipResources -n -g ALL app.jar\n" //
				+ " Decompiles app.jar to app.src.jar; It doesn't copy resources to the output jar, the decompiled classes contain "
				+ "line numbers as comments and the jd-cli prints the most verbose debug information about decompilation\n\n" //
				+ "$ java -jar jd-cli.jar myapp.jar -od decompiled -oc\n" //
				+ " Decompiles content of myapp.jar to directory named 'decompiled' and also on a screen");

		setLoggingLevel(cliArguments.getLogLevel());

		if (cliArguments.getFiles().isEmpty()) {
			jCmd.usage();
			System.exit(1);
		}

		OptionsManager.setOptions(cliArguments);

		JDOutput outputPlugin = null;

		if (cliArguments.isOutputPluginSpecified()) {
			List<JDOutput> outPlugins = new ArrayList<JDOutput>();
			if (cliArguments.isConsoleOut()) {
				outPlugins.add(new PrintStreamOutput(System.out));
			}
			final File zipFile = cliArguments.getZipOutFile();
			if (zipFile != null) {
				try {
					outPlugins.add(new ZipOutput(zipFile));
				} catch (Exception e) {
					LOGGER.warn("Unable to create zip output", e);
				}
			}
			final File dir = cliArguments.getDirOutFile();
			if (dir != null) {
				try {
					outPlugins.add(new DirOutput(dir));
				} catch (Exception e) {
					LOGGER.warn("Unable to create directory output", e);
				}
			}
			if (outPlugins.size() > 0) {
				outputPlugin = new MultiOutput(outPlugins);
			}
		}

		final JavaDecompiler javaDecompiler = new JavaDecompiler();

		boolean decompiled = false;
		for (String path : cliArguments.getFiles()) {
			LOGGER.info("Decompiling {}", path);
			final File file = new File(path);
			if (file.exists()) {
				try {
					InputOutputPair inOut = getInOutPlugins(file, outputPlugin);
					inOut.getJdInput().decompile(javaDecompiler, inOut.getJdOutput());
					decompiled = true;
				} catch (Exception e) {
					LOGGER.warn("Problem occured during instantiating plugins", e);
				}
			} else {
				LOGGER.warn("Input file {} doesn't exist", file);
			}
		}

		if (!decompiled) {
			jCmd.usage();
			System.exit(2);
		}

	}

	/**
	 * Helper method which creates correct {@link JDInput} instance for the
	 * input file and if outPlugin is null, then provides a default
	 * {@link JDOutput} instance for the given input file type too.
	 * 
	 * @param inputFile
	 * @param outPlugin
	 * @return
	 * @throws NullPointerException
	 * @throws IOException
	 */
	public static InputOutputPair getInOutPlugins(final File inputFile, JDOutput outPlugin)
			throws NullPointerException, IOException {
		JDInput jdIn = null;
		JDOutput jdOut = null;
		if (inputFile.isDirectory()) {
			jdIn = new DirInput(inputFile.getPath());
			jdOut = new DirOutput(new File(inputFile.getName() + ".src"));
		} else {
			DataInputStream dis = new DataInputStream(new FileInputStream(inputFile));
			int magic = 0;
			try {
				magic = dis.readInt();
			} finally {
				IOUtils.closeQuietly(dis);
			}
			switch (magic) {
			case JavaDecompilerConstants.MAGIC_NR_CLASS_FILE:
				jdIn = new ClassFileInput(inputFile.getPath());
				jdOut = new PrintStreamOutput(System.out);
				break;
			case JavaDecompilerConstants.MAGIC_NR_ZIP_FILE:
				jdIn = new ZipFileInput(inputFile.getPath());
				String decompiledZipName = inputFile.getName();
				int suffixPos = decompiledZipName.lastIndexOf(".");
				if (suffixPos >= 0) {
					decompiledZipName = decompiledZipName.substring(0, suffixPos) + ".src"
							+ decompiledZipName.substring(suffixPos);
				} else {
					decompiledZipName = decompiledZipName + ".src";
				}
				jdOut = new ZipOutput(new File(decompiledZipName));
				break;
			default:
				throw new IllegalArgumentException("File type of was not recognized: " + inputFile);
			}
		}
		return new InputOutputPair(jdIn, outPlugin, jdOut);
	}

	/**
	 * Configures Logback log level.
	 * 
	 * @param level
	 */
	private static void setLoggingLevel(final Level level) {
		((ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ROOT_LOGGER_NAME)).setLevel(level);
	}
}
