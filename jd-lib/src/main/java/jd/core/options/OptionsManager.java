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
