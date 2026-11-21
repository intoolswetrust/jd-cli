package com.github.kwart.jd;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /** Number of the println statements in DensePacked.java. */
    private static final int PRINTLN_COUNT = 8;
    private static final Pattern PRINTLN = Pattern.compile("System\\.out\\.println\\(\"\\w+\"\\);");

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
     * Decompiles a class in which several members (and inner classes) share the same source lines. To keep every statement on
     * its original line, jd-core has to join members on one line and to reorder them (e.g. the field of an inner class is moved
     * behind its constructor). This is not possible by merely adding blank lines.
     */
    @Test
    public void realignLineNumbersReordersMembersTest() throws IOException {
        File sourceFile = new File("src/test/resources/realign/DensePacked.java");
        File classesDir = temporaryFolder.newFolder("classes");
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assertTrue("A JDK is required to run this test", compiler != null);
        int result = compiler.run(null, null, null, "-g", "--release", "17", "-d", classesDir.getPath(), sourceFile.getPath());
        assertEquals(0, result);

        String plain = decompileDensePacked(classesDir, false);
        String realigned = decompileDensePacked(classesDir, true);

        // without realignment the field of the inner class precedes its constructor
        assertTrue(plain.indexOf("int z") < plain.indexOf("In3()"));
        // with realignment the members sharing a line are re-ordered, so that each of them keeps its original line
        assertTrue(realigned.indexOf("In3()") < realigned.indexOf("int z"));

        // each statement is on the same line as in the original source
        List<String> original = Files.readAllLines(sourceFile.toPath());
        List<String> decompiled = Arrays.asList(realigned.split("\\R", -1));
        int checked = 0;
        for (int i = 0; i < original.size(); i++) {
            Matcher matcher = PRINTLN.matcher(original.get(i));
            while (matcher.find()) {
                assertThat("line " + (i + 1) + " of the realigned output", decompiled.get(i), containsString(matcher.group()));
                checked++;
            }
        }
        assertEquals(PRINTLN_COUNT, checked);
        // members are joined on shared lines, exactly as in the original source
        assertThat(decompiled.get(2), allOf(containsString("void a()"), containsString("class In1")));
        assertThat(decompiled.get(3), allOf(containsString("DensePacked()"), containsString("class In2")));
    }

    private String decompileDensePacked(File classesDir, boolean realign) throws IOException {
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
                return false;
            }

            @Override
            public boolean isRealignLineNumbers() {
                return realign;
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
