package com.github.kwart.jd.loader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jd.core.v1.api.loader.Loader;

import com.github.kwart.jd.IOUtils;

/**
 * Loader instance which caches byte arrays with class data.
 */
public class CachedLoader implements Loader {

    private final ConcurrentMap<String, byte[]> classCache = new ConcurrentHashMap<>();

    public void addClass(String name, byte[] bytecode) {
        if (name == null || bytecode == null) {
            return;
        }
        classCache.put(name.replace('\\', '/'), bytecode);
    }

    public void addClass(String name, InputStream is) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            IOUtils.copy(is, baos);
            addClass(name, baos.toByteArray());
        }
    }

    public Set<String> getClassNames() {
        return classCache.keySet();
    }

    @Override
    public byte[] load(String internalName) throws IOException {
        return findInCache(internalName);
    }

    @Override
    public boolean canLoad(String internalName) {
        return findInCache(internalName) != null;
    }

    private byte[] findInCache(String internalName) {
        if (internalName == null) {
            return null;
        }
        internalName = internalName.replace('\\', '/');
        byte[] bytes = classCache.get(internalName);
        return bytes != null ? bytes : classCache.get(internalName + ".class");
    }
}
