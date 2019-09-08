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
package com.github.kwart.jd;

import org.jd.core.v1.ClassFileToJavaSourceDecompiler;
import org.jd.core.v1.api.loader.Loader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kwart.jd.options.DecompilerOptions;
import com.github.kwart.jd.printer.StringBuilderPrinter;

/**
 * Decompiler implementation, which uses an JD Core to decompile classes.
 */
public class JavaDecompiler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaDecompiler.class);

    private final DecompilerOptions options;
    private final ClassFileToJavaSourceDecompiler classDecompiler;
    
    public JavaDecompiler(DecompilerOptions options) {
        this.options = options;
        this.classDecompiler = new ClassFileToJavaSourceDecompiler();
    }

    /**
     * Decompile a single class from given base location.
     *
     * @param loader
     * @param internalName class name (with path relative to basePath)
     * @return decompiled class as a String or null if sth fails
     */
    public String decompileClass(Loader loader, String internalName) {
        LOGGER.debug("Decompiling class {}", internalName);
        if (loader == null) {
            LOGGER.warn("Loader was null");
            return null;
        }

        StringBuilderPrinter sbp = new StringBuilderPrinter(options);
        try {
            classDecompiler.decompile(loader, sbp, internalName);
        } catch (Exception e) {
            LOGGER.error("Can't decompile " + internalName, e);
            return null;
        }
        return sbp.toString();
    }

    public DecompilerOptions getOptions() {
        return options;
    }
}
