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
package jd.core.parser;

/**
 * Exception class used to report problems during class file parsing.
 * 
 * @author Josef Cacek
 */
public class ClassFormatException extends Exception {

    private static final long serialVersionUID = 1L;

    public ClassFormatException() {
    }

    public ClassFormatException(String message) {
        super(message);
    }

    public ClassFormatException(Throwable cause) {
        super(cause);
    }

    public ClassFormatException(String message, Throwable cause) {
        super(message, cause);
    }

}
