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

import jd.ide.intellij.config.JDPluginComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intellij.openapi.components.ComponentManager;

/**
 * ComponentManager, which returns a {@link JDPluginComponent} instance.
 */
public class Application implements ComponentManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static final JDPluginComponent jdpc = new JDPluginComponent();

    @SuppressWarnings("unchecked")
    public <T> T getComponent(Class<T> interfaceClass) {
        LOGGER.trace("getComponent() called for {}", interfaceClass);
        return (T) (jdpc);
    }

}
