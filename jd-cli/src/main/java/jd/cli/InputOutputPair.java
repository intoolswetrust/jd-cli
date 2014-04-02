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
