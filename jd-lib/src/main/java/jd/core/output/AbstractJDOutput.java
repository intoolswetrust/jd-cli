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

import jd.core.options.DecompilerOptions;
import jd.core.options.OptionsManager;

/**
 * Abstract parent with more or less empty implementation of
 * {@link #init(String)} and {@link #commit()} methods.
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
	 * Empty implementation of {@link JDOutput#commit()}. Child classes can
	 * override the behavior.
	 * 
	 * @see jd.core.output.JDOutput#commit()
	 */
	public void commit() {
	}

}
