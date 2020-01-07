package com.github.kwart.jd;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.jd.core.v1.api.loader.LoaderException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.kwart.jd.input.DirInput;
import com.github.kwart.jd.input.JDInput;
import com.github.kwart.jd.options.DecompilerOptions;
import com.github.kwart.jd.output.DirOutput;
import com.github.kwart.jd.output.JDOutput;

/**
 * Basic test for {@link JavaDecompiler} class.
 */
public class JavaDecompilerTest {

    /**
     * Temporary folder.
     */
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void test() throws LoaderException, IOException {
        JDInput input = new DirInput("target/test-classes");
        File tmpFolder = temporaryFolder.getRoot();
        JDOutput output = new DirOutput(tmpFolder.getAbsoluteFile());
        JavaDecompiler decompiler = new JavaDecompiler(new DecompilerOptions() {

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
        });
        input.decompile(decompiler, output);
        File decompiledFile = new File(tmpFolder, "com/github/kwart/jd/HelloWorld.java");
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
