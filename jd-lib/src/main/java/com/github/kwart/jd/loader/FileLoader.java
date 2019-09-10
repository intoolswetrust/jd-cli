package com.github.kwart.jd.loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.api.loader.LoaderException;

/**
 * Loads bytes from given file name.
 */
public final class FileLoader implements Loader {

    /**
     * Singleton instance.
     */
    public static final FileLoader INSTANCE = new FileLoader();

    private FileLoader() {
    }

    @Override
    public byte[] load(String internalName) throws LoaderException {
        try {
            return Files.readAllBytes(Paths.get(internalName));
        } catch (IOException e) {
            throw new LoaderException(e);
        }
    }

    @Override
    public boolean canLoad(String internalName) {
        return Files.isReadable(Paths.get(internalName));
    }
}
