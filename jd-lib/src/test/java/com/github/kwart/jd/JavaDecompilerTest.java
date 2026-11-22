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
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * Lines of the statements in {@link com.github.kwart.jd.sample.BuilderChain}.
     */
    private static final List<Integer> BUILDER_CHAIN_STATEMENT_LINES = Arrays.asList(20, 21, 22, 23, 30, 31, 32, 35, 37, 40,
            43, 46);

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
     * Decompiles {@link com.github.kwart.jd.sample.BuilderChain} with realigned and displayed line numbers and checks that
     * every statement is printed on its original line. Checks this property, not the exact layout, to survive jd-core updates.
     */
    @Test
    public void realignLineNumbersTest() throws IOException {
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
        new DirInput("target/test-classes", "BuilderChain\\.").decompile(new JavaDecompiler(options), new DirOutput(outDir));

        File decompiledFile = new File(outDir, "com/github/kwart/jd/sample/BuilderChain.java");
        List<String> lines = Files.readAllLines(decompiledFile.toPath(), StandardCharsets.UTF_8);

        Pattern lineNumberPrefix = Pattern.compile("^/\\*\\s*(\\d+) \\*/");
        Set<Integer> originalLineNumbers = new TreeSet<>();
        for (int i = 0; i < lines.size(); i++) {
            Matcher matcher = lineNumberPrefix.matcher(lines.get(i));
            assertTrue("Line without line number prefix: " + lines.get(i), matcher.find());
            int originalLineNumber = Integer.parseInt(matcher.group(1));
            if (originalLineNumber > 0) {
                assertEquals("Statement printed on a different line than in the source: " + lines.get(i), originalLineNumber,
                        i + 1);
                originalLineNumbers.add(originalLineNumber);
            }
        }
        assertEquals(new TreeSet<>(BUILDER_CHAIN_STATEMENT_LINES), originalLineNumbers);
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
