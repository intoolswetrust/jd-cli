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
package jd.core.output;

import java.io.InputStream;

/**
 * This interface is used for the decompiler output plugins. It contains methods for processing classes and resources.
 * <p>
 * Usual workflow is:
 * <ol>
 * <li>{@link #init(String)} mehod is called to initialize plugin</li>
 * <li>cycle through input resources and call {@link #processClass(String, String)} and
 * {@link #processResource(String, InputStream)} mehods</li>
 * <li>call {@link #commit()}</li>
 * </ol>
 */
public interface JDOutput {

    /**
     * Initialize the plugin for the given input basePath. Resources can be reserved here.
     *
     * @param basePath path to decompiled resource(s) (e.g. directory, jar file, ...)
     */
    void init(String basePath);

    /**
     * Handle a decompiled class.
     *
     * @param classPath a class path within a basePath without ".class" extension (e.g. "java/lang/String")
     * @param decompiledSrc decompiled source code.
     */
    void processClass(String classPath, String decompiledSrc);

    /**
     * Handle resource from
     *
     * @param filePath a path to a resource within a basePath (e.g. "org/myproject/translations/messages_en.properties")
     * @param is the resource input stream
     */
    void processResource(String filePath, InputStream is);

    /**
     * Finish the processing. Resources should be released in this method.
     */
    void commit();
}
