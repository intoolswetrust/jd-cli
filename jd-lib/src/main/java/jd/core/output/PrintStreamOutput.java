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

import java.io.InputStream;
import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link JDOutput} implementation which prints decompiled java source to a {@link PrintStream}. It skips resource processing.
 *
 * @author Josef Cacek
 */
public class PrintStreamOutput extends AbstractJDOutput {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintStreamOutput.class);

    private final PrintStream ps;

    /**
     * Constructor.
     *
     * @param ps {@link PrintStream} to be used by this instance (not- <code>null</code>)
     */
    public PrintStreamOutput(final PrintStream ps) {
        if (ps == null) {
            throw new NullPointerException("PrintStream can't be null.");
        }
        this.ps = ps;
    }

    /**
     * Prints decompiled class to wrapped {@link PrintStream}.
     *
     * @see jd.core.output.JDOutput#processClass(java.lang.String, java.lang.String)
     */
    public void processClass(final String className, final String src) {
        LOGGER.debug("Processing class {}", className);
        ps.println(src);
    }

    /**
     * Empty implementation.
     *
     * @see jd.core.output.JDOutput#processResource(java.lang.String, java.io.InputStream)
     */
    public void processResource(String fileName, InputStream is) {
        LOGGER.debug("Skipping resource {}", fileName);
    }

}
