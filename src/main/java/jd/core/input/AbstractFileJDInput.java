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
package jd.core.input;

import static jd.core.JavaDecompilerConstants.*;

import java.io.File;
import java.util.Locale;

public abstract class AbstractFileJDInput implements JDInput {

    protected final File file;

    public AbstractFileJDInput(final String path) {
        file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("Path doesn't denote an existing file.");
        }
    }

    protected boolean isClassFile(final String filePath) {
        return filePath.toLowerCase(Locale.ENGLISH).endsWith(CLASS_SUFFIX);
    }

    protected boolean isInnerClass(final String filePath) {
        return filePath.toLowerCase(Locale.ENGLISH).matches("\\$.*\\.class$");
    }

    protected String cutClassSuffix(final String classFilePath) {
        return classFilePath.substring(0, classFilePath.length() - CLASS_SUFFIX_LEN);
    }
}
