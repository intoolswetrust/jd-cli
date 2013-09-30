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
package com.intellij.openapi.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An IJ plugin related code.
 */
public class ApplicationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static final Application APP = new Application();

    public static Application getApplication() {
        LOGGER.trace("getApplication() called");
        return APP;
    }

}
