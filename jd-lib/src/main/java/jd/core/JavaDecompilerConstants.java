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
        public static final String ESCAPE_UNICODE_CHARACTERS = "jd.escapeUnicodeCharacters";
        public static final boolean ESCAPE_UNICODE_CHARACTERS_DEFAULT = false;
        public static final String SHOW_PREFIX_THIS = "jd.showPrefixThis";
        public static final boolean SHOW_PREFIX_THIS_DEFAULT = false;
        public static final String REALIGN_LINE_NUMBERS = "jd.realignLineNumbers";
        public static final boolean REALIGN_LINE_NUMBERS_DEFAULT = false;
        public static final String SHOW_DEFAULT_CONSTRUCTOR = "jd.showDefaultConstructor";
        public static final boolean SHOW_DEFAULT_CONSTRUCTOR_DEFAULT = false;
        public static final String MERGE_EMPTY_LINES = "jd.mergeEmptyLines";
        public static final boolean MERGE_EMPTY_LINES_DEFAULT = true;
    }

}
