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

import static jd.core.JavaDecompilerConstants.SystemProperties.*;
import jd.core.IOUtils;

/**
 * {@link DecompilerOptions} implementation, which reads values from System properties.
 *
 * @author Josef Cacek
 * @see jd.core.JavaDecompilerConstants.SystemProperties
 */
public class SystemPropertiesOptions implements DecompilerOptions {

    public boolean isDisplayLineNumbers() {
        return IOUtils.getBoolean(LINE_NUMBERS, LINE_NUMBERS_DEFAULT);
    }

    public boolean isDisplayMetadata() {
        return IOUtils.getBoolean(METADATA, METADATA_DEFAULT);
    }

    public boolean isDiscardLocation() {
        return IOUtils.getBoolean(DISCARD_LOCATION, DISCARD_LOCATION_DEFAULT);
    }

    public boolean isSkipResources() {
        return IOUtils.getBoolean(SKIP_RESOURCES, SKIP_RESOURCES_DEFAULT);
    }

    public boolean isEscapeUnicodeCharacters() {
        return IOUtils.getBoolean(ESCAPE_UNICODE_CHARACTERS, ESCAPE_UNICODE_CHARACTERS_DEFAULT);
    }

    public boolean isShowPrefixThis() {
        return IOUtils.getBoolean(SHOW_PREFIX_THIS, SHOW_PREFIX_THIS_DEFAULT);
    }

    public boolean isRealignLineNumbers() {
        return IOUtils.getBoolean(REALIGN_LINE_NUMBERS, REALIGN_LINE_NUMBERS_DEFAULT);
    }

    public boolean isShowDefaultConstructor() {
        return IOUtils.getBoolean(SHOW_DEFAULT_CONSTRUCTOR, SHOW_DEFAULT_CONSTRUCTOR_DEFAULT);
    }

    public boolean isMergeEmptyLines() {
        return IOUtils.getBoolean(MERGE_EMPTY_LINES, MERGE_EMPTY_LINES_DEFAULT);
    }
}
