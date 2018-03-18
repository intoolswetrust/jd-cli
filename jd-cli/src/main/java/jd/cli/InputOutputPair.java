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
package jd.cli;

import jd.core.input.JDInput;
import jd.core.output.JDOutput;

/**
 * Holds a pair of {@link JDInput} and {@link JDOutput} instances.
 */
public class InputOutputPair {

    private final JDInput jdInput;
    private final JDOutput jdOutput;

    public InputOutputPair(final JDInput jdIn, final JDOutput jdOut, final JDOutput jdOutFallback) {
        jdInput = jdIn;
        jdOutput = (jdOut != null ? jdOut : jdOutFallback);
    }

    /**
     * @return the jdInput
     */
    public JDInput getJdInput() {
        return jdInput;
    }

    /**
     * @return the jdOutput
     */
    public JDOutput getJdOutput() {
        return jdOutput;
    }

}
