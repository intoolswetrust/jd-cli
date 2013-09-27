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
import java.io.PrintStream;

public class PrintStreamOutput extends AbstractJDOutput {

    private final PrintStream ps;

    public PrintStreamOutput(final PrintStream ps) {
        if (ps == null) {
            throw new NullPointerException("PrintStream can't be null.");
        }
        this.ps = ps;
    }

    public void processClass(final String className, final String src) {
        if (debug)
            ps.println("Class " + className);
        ps.println(src);
    }

    public void processResource(String fileName, InputStream is) {
        if (debug)
            ps.println("Skipping resource " + fileName);
    }

}
