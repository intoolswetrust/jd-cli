/*
 * Copyright 2013 kwart, betterphp, nviennot
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
