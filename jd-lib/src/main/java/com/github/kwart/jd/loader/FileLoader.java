package com.github.kwart.jd.loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jd.core.v1.api.loader.Loader;

/**
 * Loads bytes from given file name.
 */
public final class FileLoader implements Loader {

    private final String basePath;

    public FileLoader() {
        this(null);
    }

    public FileLoader(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public byte[] load(String internalName) throws IOException {
        return Files.readAllBytes(fixPath(internalName));
    }

    @Override
    public boolean canLoad(String internalName) {
        Path fixedPath = fixPath(internalName);
        return fixedPath != null;
    }

    public Path fixPath(String internalName) {
        Path path = Paths.get(internalName);
        if (Files.isReadable(path)) {
            return path;
        }
        if (basePath != null) {
            path = Paths.get(basePath, internalName);
            if (Files.isReadable(path)) {
                return path;
            }
        }
        path = Paths.get(internalName + ".class");
        if (Files.isReadable(path)) {
            return path;
        }
        if (basePath != null) {
            path = Paths.get(basePath, internalName + ".class");
            if (Files.isReadable(path)) {
                return path;
            }
        }
        return null;
    }
}
