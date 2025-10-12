package com.github.kwart.jd;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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
