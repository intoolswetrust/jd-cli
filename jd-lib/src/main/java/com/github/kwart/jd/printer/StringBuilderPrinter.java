/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package com.github.kwart.jd.printer;


import static com.github.kwart.jd.JavaDecompilerConstants.LS;

import org.jd.core.v1.api.printer.Printer;

import com.github.kwart.jd.options.DecompilerOptions;

public class StringBuilderPrinter implements Printer {
    protected static final String INDENTATION = "  ";

    protected int indentationCount;
    protected StringBuilder sb = new StringBuilder();
    protected String format;
    private final DecompilerOptions options;

    public StringBuilderPrinter(DecompilerOptions options) {
        this.options=options;
    }

    public void init() {
        sb.setLength(0);
        indentationCount = 0;
    }

    public String toString() { return sb.toString(); }

    public void start(int maxLineNumber, int majorVersion, int minorVersion) {
        this.indentationCount = 0;

        if (maxLineNumber == 0) {
            format = "%4d";
        } else {
            int width = 2;

            while (maxLineNumber >= 10) {
                width++;
                maxLineNumber /= 10;
            }

            format = "%" + width + "d";
        }
    }

    public void end() {}

    public void printText(String text) {
        if (options.isEscapeUnicodeCharacters()) {
            for(int i=0, len=text.length(); i<len; i++) {
                char c = text.charAt(i);

                if (c < 128) {
                    sb.append(c);
                } else {
                    int h = (c >> 24);

                    sb.append("\\u");
                    sb.append((h <= 9) ? (h + '0') : (h + 'A'));
                    h = (c >> 16) & 255;
                    sb.append((h <= 9) ? (h + '0') : (h + 'A'));
                    h = (c >> 8) & 255;
                    sb.append((h <= 9) ? (h + '0') : (h + 'A'));
                    h = (c) & 255;
                    sb.append((h <= 9) ? (h + '0') : (h + 'A'));
                }
            }
        } else {
            sb.append(text);
        }
    }

    public void printNumericConstant(String constant) { sb.append(constant); }

    public void printStringConstant(String constant, String ownerInternalName) { printText(constant); }

    public void printKeyword(String keyword) { sb.append(keyword); }

    public void printDeclaration(int type, String internalTypeName, String name, String descriptor) { printText(name); }

    public void printReference(int type, String internalTypeName, String name, String descriptor, String ownerInternalName) { printText(name); }

    public void indent() {
        this.indentationCount++;
    }
    public void unindent() {
        if (this.indentationCount > 0)
            this.indentationCount--;
    }

    public void startLine(int lineNumber) {
        printLineNumber(lineNumber);

        for (int i=0; i<indentationCount; i++)
            sb.append(INDENTATION);
    }

    public void endLine() {
        sb.append(LS);
    }

    public void extraLine(int count) {
        while (count-- > 0) {
            printLineNumber(0);
            sb.append(LS);
        }
    }

    public void startMarker(int type) {}

    public void endMarker(int type) {}

    protected void printLineNumber(int lineNumber) {
        if (options.isDisplayLineNumbers()) {
            sb.append("/* ");
            sb.append(String.format(format, lineNumber));
            sb.append(" */ ");
        }
    }
}
