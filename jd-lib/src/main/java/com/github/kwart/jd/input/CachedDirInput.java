/*******************************************************************************
 * Copyright (C) 2015 Josef Cacek
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.github.kwart.jd.input;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kwart.jd.IOUtils;
import com.github.kwart.jd.JavaDecompiler;
import com.github.kwart.jd.loader.CachedLoader;
import com.github.kwart.jd.options.DecompilerOptions;
import com.github.kwart.jd.output.JDOutput;

/**
 * Input plugin which takes a directory as an input and before decompiling the classes it caches all the bytecode in the
 * {@link CachedLoader}.
 *
 * @author Josef Cacek
 */
public class CachedDirInput extends AbstractFileJDInput {

    private static final Logger LOGGER = LoggerFactory.getLogger(CachedDirInput.class);

    public CachedDirInput(String path, String pattern) throws IllegalArgumentException {
        super(path, pattern);
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("Path doesn't denote a directory.");
        }
    }

    public CachedDirInput(String path) {
        this(path, null);
    }

    @Override
    public void decompile(JavaDecompiler javaDecompiler, JDOutput jdOutput) {
        if (javaDecompiler == null || jdOutput == null) {
            LOGGER.warn("Decompiler or JDOutput are null");
            return;
        }

        DecompilerOptions options = javaDecompiler.getOptions();
        CachedLoader cachedLoader = new CachedLoader();
        jdOutput.init(javaDecompiler.getOptions(), file.getPath());

        try {
            Path basePath = file.toPath();
            Files.walkFileTree(basePath,
                    new CacheAndCopyFiles(cachedLoader, basePath, options.isSkipResources() ? null : jdOutput));
        } catch (IOException e) {
            LOGGER.error("Caching files failed", e);
        }

        Stream<String> classNamesStream = options.isParallelProcessingAllowed() ? cachedLoader.getClassNames().parallelStream()
                : cachedLoader.getClassNames().stream();
        classNamesStream.filter(s -> !IOUtils.isInnerClass(s)).map(s -> IOUtils.cutClassSuffix(s)).forEach(name -> {
            try {
                jdOutput.processClass(name, javaDecompiler.decompileClass(cachedLoader, name));
            } catch (Exception e) {
                LOGGER.error("Exception when decompiling class " + name, e);
            }
        });
        jdOutput.commit();
    }

    class CacheAndCopyFiles extends SimpleFileVisitor<Path> {

        private final CachedLoader cachedLoader;
        private final Path basePath;
        private final JDOutput jdOutput;

        CacheAndCopyFiles(CachedLoader cachedLoader, Path basePath, JDOutput jdOutput) {
            this.cachedLoader = cachedLoader;
            this.basePath = basePath;
            this.jdOutput = jdOutput;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
            File relativizedFile = basePath.relativize(file).toFile();
            String filePath = relativizedFile.getPath();
            if (skipThePath(filePath)) {
                return CONTINUE;
            }
            if (IOUtils.isClassFile(filePath)) {
                LOGGER.debug("Caching {}", filePath);
                try (InputStream is = Files.newInputStream(file)) {
                    cachedLoader.addClass(filePath, is);
                } catch (Exception e) {
                    LOGGER.error("Exception occured while caching class " + filePath, e);
                }
            } else if (jdOutput != null) {
                LOGGER.debug("Processing resource file {}", filePath);
                try (InputStream is = Files.newInputStream(file)) {
                    jdOutput.processResource(filePath, is);
                } catch (IOException ioe) {
                    LOGGER.error("Resource processing failed for {}", filePath, ioe);
                }
            }
            return CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            LOGGER.debug("Visited directory: {}", dir);
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            LOGGER.warn("Error while processing file " + file, exc);
            return CONTINUE;
        }
    }
}
