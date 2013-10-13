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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link JDOutput} implementation which prints decompiled java source to a
 * {@link PrintStream}. It skips resource processing.
 * 
 * @author Josef Cacek
 */
public class PrintStreamOutput extends AbstractJDOutput {

	private static final Logger LOGGER = LoggerFactory.getLogger(PrintStreamOutput.class);

	private final PrintStream ps;

	/**
	 * Constructor.
	 * 
	 * @param ps
	 *            {@link PrintStream} to be used by this instance (not-
	 *            <code>null</code>)
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
	 * @see jd.core.output.JDOutput#processClass(java.lang.String,
	 *      java.lang.String)
	 */
	public void processClass(final String className, final String src) {
		LOGGER.debug("Processing class {}", className);
		ps.println(src);
	}

	/**
	 * Empty implementation.
	 * 
	 * @see jd.core.output.JDOutput#processResource(java.lang.String,
	 *      java.io.InputStream)
	 */
	public void processResource(String fileName, InputStream is) {
		LOGGER.debug("Skipping resource {}", fileName);
	}

}
