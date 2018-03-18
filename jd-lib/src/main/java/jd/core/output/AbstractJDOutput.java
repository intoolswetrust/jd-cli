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

import jd.core.options.DecompilerOptions;
import jd.core.options.OptionsManager;

/**
 * Abstract parent with more or less empty implementation of {@link #init(String)} and {@link #commit()} methods.
 */
public abstract class AbstractJDOutput implements JDOutput {

    protected boolean skipResources;

    /*
     * (non-Javadoc)
     *
     * @see jd.core.output.JDOutput#init(java.lang.String)
     */
    public void init(String basePath) {
        final DecompilerOptions options = OptionsManager.getOptions();
        skipResources = options.isSkipResources();
    }

    /**
     * Empty implementation of {@link JDOutput#commit()}. Child classes can override the behavior.
     *
     * @see jd.core.output.JDOutput#commit()
     */
    public void commit() {
    }

}
