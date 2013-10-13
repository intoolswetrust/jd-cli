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
package jd.core;

/**
 * Constants used by the library classes.
 * 
 * @author Josef Cacek
 */
public final class JavaDecompilerConstants {

	public static final String VERSION = "${project.version}";

	public static final String TMP_DIR = System.getProperty("java.io.tmpdir");

	public static final String UTF_8 = "UTF-8";

	public static final String NATIVE_LIB_NAME = "libjd-intellij";
	public static final String NATIVE_LIB_TMP_PREFIX = NATIVE_LIB_NAME + "-tmp";

	public static final String CLASS_SUFFIX = ".class";
	public static final int CLASS_SUFFIX_LEN = CLASS_SUFFIX.length();
	public static final String JAVA_SUFFIX = ".java";

	public static final int MAGIC_NR_CLASS_FILE = 0xCAFEBABE;
	public static final int MAGIC_NR_ZIP_FILE = 0x504B0304;

	public static class SystemProperties {
		public static final String LINE_NUMBERS = "jd.lineNumbers";
		public static final boolean LINE_NUMBERS_DEFAULT = false;
		public static final String METADATA = "jd.metadata";
		public static final boolean METADATA_DEFAULT = true;
		public static final String DISCARD_LOCATION = "jd.discardLocation";
		public static final boolean DISCARD_LOCATION_DEFAULT = true;
		public static final String SKIP_RESOURCES = "jd.skipResources";
		public static final boolean SKIP_RESOURCES_DEFAULT = false;
	}

}
