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
package jd.core.options;

/**
 * Options manager which helps to configure decompiler options in a threaded environment.
 * 
 * @author Josef Cacek
 */
public class OptionsManager {

    private static final ThreadLocal<DecompilerOptions> LOCAL_OPTIONS = new ThreadLocal<DecompilerOptions>() {

        private final DecompilerOptions INITIAL = new SystemPropertiesOptions();

        @Override
        protected DecompilerOptions initialValue() {
            return INITIAL;
        }

    };

    private OptionsManager() {
    }

    /**
     * Stores decompiler options. If null is provided then options are removed.
     * 
     * @param options
     */
    public static void setOptions(DecompilerOptions options) {
        if (options != null) {
            LOCAL_OPTIONS.set(options);
        } else {
            LOCAL_OPTIONS.remove();
        }
    }

    /**
     * Returns options used for the current thread.
     * 
     * @return
     */
    public static DecompilerOptions getOptions() {
        return LOCAL_OPTIONS.get();
    }

}
