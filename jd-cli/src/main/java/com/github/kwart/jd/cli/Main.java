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

import static com.beust.jcommander.JCommander.getConsole;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kwart.jd.IOUtils;
import com.github.kwart.jd.JavaDecompiler;
import com.github.kwart.jd.JavaDecompilerConstants;
import com.github.kwart.jd.input.CachedDirInput;
import com.github.kwart.jd.input.ClassFileInput;
import com.github.kwart.jd.input.DirInput;
import com.github.kwart.jd.input.JDInput;
import com.github.kwart.jd.input.ZipFileInput;
import com.github.kwart.jd.output.DirOutput;
import com.github.kwart.jd.output.JDOutput;
import com.github.kwart.jd.output.MultiOutput;
import com.github.kwart.jd.output.PrintStreamOutput;
import com.github.kwart.jd.output.StructuredDirOutput;
import com.github.kwart.jd.output.ZipOutput;

import ch.qos.logback.classic.Level;

/**
 * Main class of jd-cli.
 */
@SuppressWarnings("checkstyle:ClassDataAbstractionCoupling")
public final class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private Main() {
    }

    /**
     * The {@link #main(String[])}!
     *
     * @param args
     */
    /**
     * @param args
     */
    public static void main(String[] args) {
        final CLIArguments cliArguments = new CLIArguments();
        final ExtCommander jCmd = initCommander(args, cliArguments);

        setLoggingLevel(cliArguments.getLogLevel());

        if (jCmd.getUnknownOptions().contains("-")) {
            cliArguments.getFiles().add("-");
        }

        if (cliArguments.isHelp()) {
            jCmd.usage();
            System.exit(0);
        }
        if (cliArguments.isVersion()) {
            getConsole().println(getVersionLine());
            System.exit(0);
        }

        if (cliArguments.getFiles().isEmpty()) {
            jCmd.usage();
            System.exit(1);
        }

        JDOutput outputPlugin = initOutput(cliArguments);

        final JavaDecompiler javaDecompiler = new JavaDecompiler(cliArguments);

        boolean decompiled = false;
        for (String path : cliArguments.getFiles()) {
            File file;
            if ("-".equals(path)) {
                LOGGER.info("Decompiling from STD_IN");
                file = readSystemIn();
            } else {
                //LOGGER.info("Decompiling {}", path);
                file = new File(path);
            }

            if (file.exists()) {
                try {
                    InputOutputPair inOut = getInOutPlugins(file, outputPlugin, cliArguments.getPattern(),
                            cliArguments.isParallelProcessingAllowed());
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

    private static ExtCommander initCommander(String[] args, final CLIArguments cliArguments) {
        final ExtCommander jCmd = new ExtCommander(cliArguments);
        jCmd.setAcceptUnknownOptions(true);
        jCmd.parse(args);
        jCmd.setProgramName("java -jar jd-cli.jar");
        jCmd.setUsageHead(getVersionLine() + "\n"
            + "\nThe jd-cli is a command line interface for the Java Decompiler (http://jd.benow.ca/). "
            + "The application decompile classes, zip archives "
            + "(.zip, .jar, .war, ...) and directories containing classes. "
            + "Each supported input type has configured corresponding "
            + "default output type (class->screen, zip->zip, directory->directory). Man can simply override the output type "
            + "by specifying a command line parameter (-oc, -od, -oz). Multiple output type parameters can be used at once.");
        jCmd.setUsageTail("Examples:\n\n"
            + "$ java -jar jd-cli.jar HelloWorld.class\n"
            + " Shows decompiled class on a screen\n\n"
            + "$ java -jar jd-cli.jar --skipResources -n -g ALL app.jar\n"
            + " Decompiles app.jar to app.src.jar; It doesn't copy resources to the output jar, the decompiled classes contain "
            + "line numbers as comments and the jd-cli prints the most verbose debug information about decompilation\n\n"
            + "$ java -jar jd-cli.jar myapp.jar -od decompiled -oc\n"
            + " Decompiles content of myapp.jar to directory named 'decompiled' and also on a screen\n"
            + "\n"
            + "This program comes with ABSOLUTELY NO WARRANTY. This is free software, and you are welcome to redistribute it "
            + "under GPLv3 conditions."
        );
        return jCmd;
    }

    private static JDOutput initOutput(final CLIArguments cliArguments) {
        JDOutput outputPlugin = null;

        if (cliArguments.isOutputPluginSpecified()) {
            List<JDOutput> outPlugins = new ArrayList<JDOutput>();
            if (cliArguments.isConsoleOut()) {
                outPlugins.add(new PrintStreamOutput(System.out));
            }
            File zipFile = cliArguments.getZipOutFile();
            if (zipFile != null) {
                try {
                    outPlugins.add(new ZipOutput(zipFile));
                } catch (Exception e) {
                    LOGGER.warn("Unable to create the zip output", e);
                }
            }
            File dir = cliArguments.getDirOutFile();
            if (dir != null) {
                try {
                    outPlugins.add(new DirOutput(dir));
                } catch (Exception e) {
                    LOGGER.warn("Unable to create the directory output", e);
                }
            }
            dir = cliArguments.getDirOutFileStructured();
            if (dir != null) {
                try {
                    outPlugins.add(new StructuredDirOutput(dir));
                } catch (Exception e) {
                    LOGGER.warn("Unable to create the structured directory output", e);
                }
            }
            if (outPlugins.size() == 1) {
                outputPlugin = outPlugins.get(0);
            } else if (outPlugins.size() > 1) {
                outputPlugin = new MultiOutput(outPlugins);
            }
        }
        return outputPlugin;
    }

    /**
     * Helper method which creates correct {@link JDInput} instance for the input file and if outPlugin is null, then provides a
     * default {@link JDOutput} instance for the given input file type too.
     *
     * @param inputFile
     * @param outPlugin
     * @param useParallel
     * @return
     * @throws NullPointerException
     * @throws IOException
     */
    public static InputOutputPair getInOutPlugins(final File inputFile, JDOutput outPlugin, String pattern, boolean useParallel)
            throws NullPointerException, IOException {
        JDInput jdIn = null;
        JDOutput jdOut = null;
        if (inputFile.isDirectory()) {
            jdIn = useParallel ? new CachedDirInput(inputFile.getPath(), pattern) : new DirInput(inputFile.getPath(), pattern);
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
                    jdIn = new ClassFileInput(inputFile.getPath(), pattern);
                    jdOut = new PrintStreamOutput(System.out);
                    break;
                case JavaDecompilerConstants.MAGIC_NR_ZIP_FILE:
                    jdIn = new ZipFileInput(inputFile.getPath(), pattern);
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
                    throw new IllegalArgumentException("File type of the source was not recognized: " + inputFile);
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

    /**
     * Creates a temporary file from STD_IN input.
     *
     * @return newly created file with content received from {@link System#in} occurs)
     */
    private static File readSystemIn() {
        FileOutputStream os = null;
        File tempFile = null;
        FileInputStream fis = null;
        try {
            tempFile = File.createTempFile("jdTemp-", "-stdin");
            LOGGER.debug("Created a temporary file from the STD_IN: {}", tempFile.getAbsolutePath());
            os = new FileOutputStream(tempFile);
            IOUtils.copy(System.in, os);
        } catch (IOException e) {
            LOGGER.error("Copying STD_IN failed", e);
        } finally {
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(os);
        }
        return tempFile;
    }

    private static String getVersionLine() {
        return "jd-cli version " + JavaDecompilerConstants.VERSION + " - Copyright (C) 2015-2021 Josef Cacek";
    }
}
