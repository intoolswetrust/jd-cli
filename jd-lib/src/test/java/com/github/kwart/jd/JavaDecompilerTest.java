package com.github.kwart.jd;

import static com.github.kwart.jd.JavaDecompilerConstants.CLASS_SUFFIX;
import static org.junit.Assert.assertTrue;

import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.api.loader.LoaderException;
import org.junit.Test;

import com.github.kwart.jd.loader.ByteArrayLoader;
import com.github.kwart.jd.options.DecompilerOptions;

/**
 * Basic test for {@link JavaDecompiler} class.
 */
public class JavaDecompilerTest {

    @Test
    public void test() throws LoaderException {

        Class<HelloWorld> clazz = HelloWorld.class;
        String internalName = clazz.getName();

        Loader loader = new ByteArrayLoader(clazz.getResourceAsStream("/" + internalName.replace('.', '/') + CLASS_SUFFIX),
                internalName);
        String decompiled = new JavaDecompiler(new DecompilerOptions() {

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
        }).decompileClass(loader, internalName);
        System.out.println(decompiled);
        assertTrue(decompiled.contains("public final class HelloWorld {"));
        assertTrue(decompiled.contains("public static void main(String[] args)"));
        assertTrue(decompiled.contains("System.out.println(\"xxx\");"));
        assertTrue(decompiled.contains("private HelloWorld() {"));
    }

}
