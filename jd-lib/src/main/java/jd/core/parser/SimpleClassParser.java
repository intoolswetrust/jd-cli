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
package jd.core.parser;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import jd.core.JavaDecompilerConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple class file parser, which reads only a header part of a class and extracts class version info and class name.
 *
 * @author Josef Cacek
 */
public final class SimpleClassParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleClassParser.class);

    private static final byte CONSTTYPE_UTF8 = 1;
    private static final byte CONSTTYPE_INTEGER = 3;
    private static final byte CONSTTYPE_FLOAT = 4;
    private static final byte CONSTTYPE_LONG = 5;
    private static final byte CONSTTYPE_DOUBLE = 6;
    private static final byte CONSTTYPE_CLASS = 7;
    private static final byte CONSTTYPE_FIELDREF = 9;
    private static final byte CONSTTYPE_STRING = 8;
    private static final byte CONSTTYPE_METHODREF = 10;
    private static final byte CONSTTYPE_INTERFACEMETHODREF = 11;
    private static final byte CONSTTYPE_NAMEANDTYPE = 12;

    private final int major, minor;
    private final String className;

    /**
     * Constructor which parses basic class information from the given stream. It stops reading right after className index is
     * found.
     *
     * @param is Input stream
     */
    public SimpleClassParser(final InputStream is) throws IOException, ClassFormatException {
        LOGGER.trace("Parsing class from an InputStream");
        final DataInputStream dataInputStream;

        if (is instanceof DataInputStream) {
            dataInputStream = (DataInputStream) is;
        } else {
            dataInputStream = new DataInputStream(new BufferedInputStream(is));
        }

        if (dataInputStream.readInt() != JavaDecompilerConstants.MAGIC_NR_CLASS_FILE) {
            throw new ClassFormatException("Magic number doesn't match a class file.");
        }
        minor = dataInputStream.readUnsignedShort();
        major = dataInputStream.readUnsignedShort();
        LOGGER.trace("Class version: {}.{}", major, minor);

        final int constantPoolSize = dataInputStream.readUnsignedShort();
        final Object[] constantPool = new Object[constantPoolSize];
        // constantPool[0] is unused by the compiler and may be used freely by the implementation.
        for (int i = 1; i < constantPoolSize; i++) {
            final byte tag = dataInputStream.readByte(); // Read tag byte
            switch (tag) {
                case CONSTTYPE_NAMEANDTYPE:
                case CONSTTYPE_FIELDREF:
                case CONSTTYPE_METHODREF:
                case CONSTTYPE_INTERFACEMETHODREF:
                    // two shorts
                    dataInputStream.readUnsignedShort();
                case CONSTTYPE_STRING:
                    // one short
                    dataInputStream.readUnsignedShort();
                    break;
                case CONSTTYPE_INTEGER:
                    dataInputStream.readInt();
                    break;
                case CONSTTYPE_FLOAT:
                    dataInputStream.readFloat();
                    break;
                case CONSTTYPE_CLASS:
                    // we need ref
                    constantPool[i] = dataInputStream.readUnsignedShort();
                    break;
                case CONSTTYPE_LONG:
                    dataInputStream.readLong();
                    i++;
                    break;
                case CONSTTYPE_DOUBLE:
                    dataInputStream.readDouble();
                    i++;
                    break;
                case CONSTTYPE_UTF8:
                    constantPool[i] = dataInputStream.readUTF();
                    break;
                default:
                    throw new ClassFormatException("Invalid byte tag in constant pool: " + tag);
            }
        }

        // final int accessFlags =
        dataInputStream.readUnsignedShort();
        final int classIdx = dataInputStream.readUnsignedShort();
        LOGGER.trace("Parsed class index in constant pool: {}", classIdx);
        if (classIdx < 1 || classIdx >= constantPoolSize || !(constantPool[classIdx] instanceof Integer)) {
            throw new ClassFormatException("Wrong class type index.");
        }
        final int classNameIdx = (Integer) constantPool[classIdx];
        LOGGER.trace("Parsed class name index in constant pool: {}", classNameIdx);
        if (classNameIdx < 1 || classNameIdx >= constantPoolSize || !(constantPool[classNameIdx] instanceof String)) {
            throw new ClassFormatException("Wrong class name index.");
        }
        className = (String) constantPool[classNameIdx];
        LOGGER.trace("Parsed class name: {}", className);
        // final int superClassNameIdx = dataInputStream.readUnsignedShort();
    }

    /**
     * Returns the class major version.
     *
     * @return major
     */
    public int getMajor() {
        return major;
    }

    /**
     * Returns the class minor version.
     *
     * @return minor
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Returns class name
     *
     * @return
     */
    public String getClassName() {
        return className;
    }

    /**
     * Returns class version in a human friendly way e.g. "49.0 (Java 1.5)"
     *
     * @return
     */
    public String getVersionString() {
        return major + "." + minor + " (Java 1." + (major - 44) + ")";
    }

}
