package com.github.kwart.jd.loader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jd.core.v1.api.loader.Loader;
import com.github.kwart.jd.IOUtils;

/**
 * Loader instance which takes byte array with class data as the constructor argument.
 */
public class ByteArrayLoader implements Loader {

    private final byte[] bytes;
    private final String internalName;

    public ByteArrayLoader(byte[] bytes, String internalName) {
        this.bytes = bytes;
        this.internalName = internalName;
    }

    public ByteArrayLoader(InputStream is, String internalName) throws IOException {
        this.internalName = internalName;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            IOUtils.copy(is, baos);
            bytes = baos.toByteArray();
        }
    }

    @Override
    public byte[] load(String internalName) throws IOException {
        return canLoad(internalName) ? bytes : null;
    }

    @Override
    public boolean canLoad(String internalName) {
        return this.internalName.equals(internalName);
    }
}
