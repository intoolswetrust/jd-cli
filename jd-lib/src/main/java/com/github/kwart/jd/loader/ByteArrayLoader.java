package com.github.kwart.jd.loader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.api.loader.LoaderException;

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

    public ByteArrayLoader(InputStream is, String internalName) throws LoaderException {
        this.internalName = internalName;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            IOUtils.copy(is, baos);
            bytes = baos.toByteArray();
        } catch (IOException e) {
            throw new LoaderException(e);
        }
    }

    @Override
    public byte[] load(String internalName) throws LoaderException {
        return canLoad(internalName) ? bytes : null;
    }

    @Override
    public boolean canLoad(String internalName) {
        return this.internalName.equals(internalName);
    }
}
