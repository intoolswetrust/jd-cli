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
package com.github.kwart.jd;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Constants used by the library classes.
 *
 * @author Josef Cacek
 */
public final class JavaDecompilerConstants {

    public static final String VERSION;

    public static final String TMP_DIR = System.getProperty("java.io.tmpdir");
    public static final String LS = System.getProperty("line.separator");

    public static final String UTF_8 = "UTF-8";

    public static final String CLASS_SUFFIX = ".class";
    public static final int CLASS_SUFFIX_LEN = CLASS_SUFFIX.length();
    public static final String JAVA_SUFFIX = ".java";

    public static final int MAGIC_NR_CLASS_FILE = 0xCAFEBABE;
    public static final int MAGIC_NR_ZIP_FILE = 0x504B0304;

    static {
        String version = "[UNKNOWN]";
        try (InputStream is = JavaDecompilerConstants.class.getResourceAsStream("/META-INF/maven/com.github.kwart.jd/jd-cli/pom.properties")) {
            if (is!=null) {
                Properties props = new Properties();
                props.load(is);
                if (props.containsKey("version")) {
                    version = props.getProperty("version");
                }
            }
        } catch (IOException e) {
            //ignore
        }
        VERSION = version;
    }
}
