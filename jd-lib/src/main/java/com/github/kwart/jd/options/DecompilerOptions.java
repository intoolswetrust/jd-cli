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
package com.github.kwart.jd.options;

/**
 * Interface representing decompiler options.
 *
 * @author Josef Cacek
 */
public interface DecompilerOptions {

    boolean isSkipResources();

    boolean isDisplayLineNumbers();

    boolean isEscapeUnicodeCharacters();

    /**
     * Returns {@code true} if jd-core should realign the decompiled code with the line numbers of the original source.
     * <p>
     * This is delegated to jd-core's {@code realignLineNumbers} configuration. The layouter treats the lines with known
     * original line numbers as fixed anchors and fits the flexible parts in between (spacing, members) to them. Where the
     * original numbering leaves too little room, members and inner classes which shared a line in the original source are joined
     * on one line again and members are re-ordered (e.g. a field is moved behind the constructor).
     * The layout is best effort (the search is bounded), so it is not a plain padding with blank lines and a perfect match is
     * not guaranteed.
     * <p>
     * It requires line number debug information in the class files; without it this option has no effect. It can be
     * combined with {@link #isDisplayLineNumbers()}.
     */
    default boolean isRealignLineNumbers() {
        return false;
    }

    boolean isParallelProcessingAllowed();
}
