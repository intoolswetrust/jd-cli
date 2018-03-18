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

import ch.qos.logback.classic.Level;

import com.beust.jcommander.IStringConverter;

/**
 * Logback Level type converter for JCommander.
 */
public class LogLevelConverter implements IStringConverter<Level> {

    @Override
    public Level convert(String value) {
        return Level.toLevel(value, Level.INFO);
    }

}
