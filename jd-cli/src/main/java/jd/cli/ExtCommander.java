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

import java.util.ResourceBundle;

import com.beust.jcommander.JCommander;

/**
 * Small extension to JCommander.
 *
 * @author Josef Cacek
 */
public class ExtCommander extends JCommander {

    private String usageHead;
    private String usageTail;

    public ExtCommander() {
        super();
    }

    public ExtCommander(Object object, ResourceBundle bundle, String... args) {
        super(object, bundle, args);
    }

    public ExtCommander(Object object, ResourceBundle bundle) {
        super(object, bundle);
    }

    public ExtCommander(Object object, String... args) {
        super(object, args);
    }

    public ExtCommander(Object object) {
        super(object);
    }

    @Override
    public void usage(StringBuilder out, String indent) {
        final int indentCount = indent.length();
        if (usageHead != null) {
            out.append(wrap(indentCount, usageHead)).append("\n");
        }
        super.usage(out, indent);
        if (usageTail != null) {
            out.append("\n").append(wrap(indentCount, usageTail));
        }
    }

    private String getIndent(int count) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(" ");
        }
        return result.toString();
    }

    protected String wrap(final int indent, final String text) {
        final int max = getColumnSize();
        final String[] lines = text.split("\n", -1);
        final String indentStr = getIndent(indent);

        final StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            final String[] words = line.split(" ", -1);
            final StringBuilder lineSb = new StringBuilder();
            for (String word : words) {
                final int lineLength = lineSb.length();
                if (lineLength > 0) {
                    if (indent + lineLength + word.length() > max) {
                        sb.append(indentStr).append(lineSb).append("\n");
                        lineSb.delete(0, lineSb.length());
                    } else {
                        lineSb.append(" ");
                    }
                }
                lineSb.append(word);
            }
            sb.append(lineSb);
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * @return the usageHead
     */
    public String getUsageHead() {
        return usageHead;
    }

    /**
     * @param usageHead the usageHead to set
     */
    public void setUsageHead(String usageHead) {
        this.usageHead = usageHead;
    }

    /**
     * @return the usageTail
     */
    public String getUsageTail() {
        return usageTail;
    }

    /**
     * @param usageTail the usageTail to set
     */
    public void setUsageTail(String usageTail) {
        this.usageTail = usageTail;
    }

}
