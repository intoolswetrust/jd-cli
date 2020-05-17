/*******************************************************************************
 * Copyright (C) 2015-2020 Josef Cacek
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
package com.github.kwart.jd.output;

import java.io.File;

import com.github.kwart.jd.options.DecompilerOptions;

/**
 * {@link JDOutput} implementation, which stores decompiled classes to a subdirectory of the output directory. The subdirectory
 * has the same name as the source file or directory. When the source doesn't contain a file name, then {@value #NO_SRC_NAME} is
 * used as the output subdirectory name.
 *
 * @author Josef Cacek
 */
public class StructuredDirOutput extends DirOutput {

    /**
     * Subdirectory name used for output when the decompiler input doesn't come as a file.
     */
    public static final String NO_SRC_NAME = "none";

    private File targetDir;

    /**
     * Constructor which takes directory path as a parameter.
     *
     * @param outputDir base directory path to save output. Must not be {@code null}
     */
    public StructuredDirOutput(File outputDir) {
        super(outputDir);
    }

    @Override
    public void init(DecompilerOptions options, String basePath) {
        super.init(options, basePath);
        if (null == basePath || basePath.isEmpty()) {
            targetDir = new File(dir, "none");
        } else {
            File tmpFile = new File(basePath);
            targetDir = new File(dir, tmpFile.getName());
        }
    }

    @Override
    protected File getTargetDir() {
        return targetDir;
    }
}
