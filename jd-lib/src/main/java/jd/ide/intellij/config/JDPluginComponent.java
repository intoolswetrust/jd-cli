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
package jd.ide.intellij.config;

import jd.core.options.OptionsManager;

/**
 * Class, which provides basic configuration settings to jd native library. An IJ plugin related code.
 */
public class JDPluginComponent {

    /**
     * Returns true if line numbers should be included in the decompiled code.
     * 
     * @return true if decompile should use line numbers, false otherwise.
     */
    public boolean isShowLineNumbersEnabled() {
        return OptionsManager.getOptions().isDisplayLineNumbers();
    }

    /**
     * Returns true if a class metadata should be appended to the end of the decompiled class.
     * 
     * @return
     */
    public boolean isShowMetadataEnabled() {
        return OptionsManager.getOptions().isDisplayMetadata();
    }

}
