package com.github.kwart.jd;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.kwart.jd.input.DirInput;
import com.github.kwart.jd.input.JDInput;
import com.github.kwart.jd.options.DecompilerOptions;
import com.github.kwart.jd.output.DirOutput;
import com.github.kwart.jd.output.JDOutput;
import com.github.kwart.jd.output.MultiOutput;
import com.github.kwart.jd.output.StructuredDirOutput;

/**
 * Basic test for {@link JavaDecompiler} class.
 */
public class JavaDecompilerTest {

    private static final DecompilerOptions DC_OPTS = new DecompilerOptions() {

        @Override
        public boolean isSkipResources() {
            return false;
        }

        @Override
        public boolean isEscapeUnicodeCharacters() {
            return false;
        }

        @Override
        public boolean isDisplayLineNumbers() {
            return false;
        }

        @Override
        public boolean isParallelProcessingAllowed() {
            return true;
        }
    };

    /**
     * Temporary folder.
     */
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void basicTest() throws IOException {
        JDInput input = new DirInput("target/test-classes");
        File tmpRootFolder = temporaryFolder.getRoot();
        File tmpFolder = new File(tmpRootFolder, "flat");
        File tmpStructuredFolder = new File(tmpRootFolder, "struct");
        List<JDOutput> outPlugins = new ArrayList<JDOutput>();
        outPlugins.add(new DirOutput(tmpFolder.getAbsoluteFile()));
        outPlugins.add(new StructuredDirOutput(tmpStructuredFolder.getAbsoluteFile()));
        JDOutput output = new MultiOutput(outPlugins);
        JavaDecompiler decompiler = new JavaDecompiler(DC_OPTS);
        input.decompile(decompiler, output);
        assertDecompiled(tmpFolder);
        assertDecompiled(new File(tmpStructuredFolder, "test-classes"));
    }

    @Test
    public void patternNotMatchingTest() throws IOException {
        JDInput input = new DirInput("target/test-classes", "Not.*Matching");
        File tmpFolder = temporaryFolder.getRoot();
        JDOutput output = new DirOutput(tmpFolder.getAbsoluteFile());
        JavaDecompiler decompiler = new JavaDecompiler(DC_OPTS);
        input.decompile(decompiler, output);
        File decompiledFile = new File(tmpFolder, "com/github/kwart/jd/HelloWorld.java");
        assertFalse(decompiledFile.isFile());
    }

    @Test
    public void patternMatchingTest() throws IOException {
        JDInput input = new DirInput("target/test-classes", "jd.Hello.*World\\.");
        File tmpFolder = temporaryFolder.getRoot();
        JDOutput output = new DirOutput(tmpFolder.getAbsoluteFile());
        JavaDecompiler decompiler = new JavaDecompiler(DC_OPTS);
        input.decompile(decompiler, output);
        assertDecompiled(tmpFolder);
    }

    /**
     * Decompiles DensePacked.java with realigned line numbers and compares the whole output.
     * <p>
     * Why this sample: jd-core sorts fields, methods and inner classes by their original line number whether the option is
     * enabled or not, so a class with one member per line (e.g. methods interleaved with inner classes) comes out in the same
     * order with and without realignment, and could only prove that the lines match. In DensePacked several members share
     * a line, which is where the realignment layout actually does something on top of the sort: members are joined on one line
     * again, and members sharing a line are re-ordered (the field {@code z} of {@code In3} is moved behind its constructor,
     * which is not the case without realignment). The sample is compiled in the test (with {@code --release 17}, so that the
     * output does not depend on the javac version), because such a layout would not pass checkstyle as a source file.
     * <p>
     * Line numbers are displayed: the expected output (DensePacked.expected.txt) shows the original line numbers, so line 3 of
     * DensePacked.java is on line 3 of the output, etc. (0 for the lines without original line number).
     */
    @Test
    public void realignLineNumbersReordersMembersTest() throws IOException {
        File sourceFile = new File("src/test/resources/realign/DensePacked.java");
        File classesDir = temporaryFolder.newFolder("classes");
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assertTrue("A JDK is required to run this test", compiler != null);
        int result = compiler.run(null, null, null, "-g", "--release", "17", "-d", classesDir.getPath(), sourceFile.getPath());
        assertEquals(0, result);

        String expected = new String(Files.readAllBytes(new File("src/test/resources/realign/DensePacked.expected.txt").toPath()),
                StandardCharsets.UTF_8);

        assertEquals(expected.replace("\r\n", "\n"), decompileDensePacked(classesDir).replace("\r\n", "\n"));
    }

    private String decompileDensePacked(File classesDir) throws IOException {
        DecompilerOptions options = new DecompilerOptions() {
            @Override
            public boolean isSkipResources() {
                return true;
            }

            @Override
            public boolean isEscapeUnicodeCharacters() {
                return false;
            }

            @Override
            public boolean isDisplayLineNumbers() {
                return true;
            }

            @Override
            public boolean isRealignLineNumbers() {
                return true;
            }

            @Override
            public boolean isParallelProcessingAllowed() {
                return false;
            }
        };
        File outDir = temporaryFolder.newFolder();
        new DirInput(classesDir.getPath(), "DensePacked\\.").decompile(new JavaDecompiler(options), new DirOutput(outDir));
        return new String(Files.readAllBytes(new File(outDir, "com/github/kwart/jd/sample/DensePacked.java").toPath()));
    }

    private void assertDecompiled(File outputFolder) throws IOException {
        File decompiledFile = new File(outputFolder, "com/github/kwart/jd/HelloWorld.java");
        assertTrue(decompiledFile.isFile());
        String decompiled = new String(Files.readAllBytes(decompiledFile.toPath()));
        assertThat(decompiled, containsString("public final class HelloWorld {"));
        assertThat(decompiled, containsString("public static void main(String[] args)"));
        assertThat(decompiled, containsString("System.out.println(\"xxx\");"));
        assertThat(decompiled, containsString("private HelloWorld() {"));

        assertThat(decompiled, containsString("public static class StaticClass {"));
        assertThat(decompiled, containsString("private class PrivateClass {"));
    }

}
