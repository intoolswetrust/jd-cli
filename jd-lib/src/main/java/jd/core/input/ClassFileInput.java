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
package jd.core.input;

import jd.core.JavaDecompiler;
import jd.core.output.JDOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link JDInput} implementation which takes a single class file as an input.
 *
 * @author Josef Cacek
 */
public class ClassFileInput extends AbstractFileJDInput {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassFileInput.class);

    public ClassFileInput(String path) {
        super(path);
    }

    /*
     * (non-Javadoc)
     *
     * @see jd.core.input.JDInput#decompile(jd.core.JavaDecompiler, jd.core.output.JDOutput)
     */
    @Override
    public void decompile(JavaDecompiler javaDecompiler, JDOutput jdOutput) {
        if (javaDecompiler == null || jdOutput == null) {
            LOGGER.warn("Decompiler or JDOutput are null");
            return;
        }

        jdOutput.init("");
        final String name = file.getName();
        LOGGER.debug("Decompiling single class file {}", name);
        String nameWithoutClassSfx = isClassFile(name) ? cutClassSuffix(name) : name;
        jdOutput.processClass(nameWithoutClassSfx, javaDecompiler.decompileClass(file.getAbsoluteFile().getParent(), name));
        jdOutput.commit();
    }
}
