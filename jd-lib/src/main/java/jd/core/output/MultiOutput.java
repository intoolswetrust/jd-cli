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
package jd.core.output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;

import jd.core.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper {@link JDOutput} implementation which takes other {@link JDOutput} instances and copies events to them.
 *
 * @author Josef Cacek
 */
public class MultiOutput extends AbstractJDOutput {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiOutput.class);

    private final JDOutput[] outputPlugins;
    private final int validPlugins;

    /**
     * Constructor.
     *
     * @param plugins
     */
    public MultiOutput(final JDOutput... plugins) {
        LOGGER.trace("Creating MultiOutput");
        outputPlugins = plugins;
        int tmpValid = 0;
        if (outputPlugins != null && outputPlugins.length > 0) {
            for (JDOutput jdOut : outputPlugins) {
                if (jdOut != null) {
                    tmpValid++;
                } else {
                    LOGGER.warn("Null JDOutput provided to MultiOutput constructor.");
                }
            }
        } else {
            LOGGER.warn("No JDOutput provided to MultiOutput constructor.");
        }
        LOGGER.trace("MultiOutput instance wraps {} valid plugin(s).", tmpValid);
        validPlugins = tmpValid;
    }

    /**
     * Constructor.
     *
     * @param plugins
     */
    public MultiOutput(final Collection<JDOutput> plugins) {
        this(plugins.toArray(new JDOutput[plugins.size()]));
    }

    /**
     * Returns true if this instance wraps at least one not-<code>null</code> {@link JDOutput}.
     *
     * @return
     */
    public boolean isValid() {
        return validPlugins > 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see jd.core.output.AbstractJDOutput#init(java.lang.String)
     */
    @Override
    public void init(final String basePath) {
        if (!isValid())
            return;
        for (JDOutput jdOut : outputPlugins) {
            if (jdOut != null) {
                try {
                    jdOut.init(basePath);
                } catch (Exception e) {
                    LOGGER.error("Callling init() of wrapped JDOutput failed.", e);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see jd.core.output.JDOutput#processClass(java.lang.String, java.lang.String)
     */
    public void processClass(final String className, final String src) {
        if (!isValid())
            return;
        for (JDOutput jdOut : outputPlugins) {
            if (jdOut != null) {
                try {
                    jdOut.processClass(className, src);
                } catch (Exception e) {
                    LOGGER.error("Callling processClass() of wrapped JDOutput failed.", e);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see jd.core.output.AbstractJDOutput#commit()
     */
    @Override
    public void commit() {
        if (!isValid())
            return;
        for (JDOutput jdOut : outputPlugins) {
            if (jdOut != null) {
                try {
                    jdOut.commit();
                } catch (Exception e) {
                    LOGGER.error("Callling commit() of wrapped JDOutput failed.", e);
                }
            }
        }
    }

    /**
     * Calls {@link #processResource(String, InputStream)} method of wrapped {@link JDOutput} instances. If only one wrapped
     * instance exist then the given InputStream is directly provided to it, otherwise temporary file is created and for each
     * wrapped {@link JDOutput} instance is a new {@link FileInputStream} created.
     *
     * @see jd.core.output.JDOutput#processResource(java.lang.String, java.io.InputStream)
     */
    public void processResource(String fileName, InputStream is) {
        switch (validPlugins) {
            case 0:
                return;
            case 1:
                for (JDOutput jdOut : outputPlugins) {
                    if (jdOut != null) {
                        try {
                            jdOut.processResource(fileName, is);
                        } catch (Exception e) {
                            LOGGER.error("Callling processResource() of wrapped JDOutput failed.", e);
                        }
                    }
                }
                break;
            default:
                File f = null;
                FileOutputStream fos = null;
                try {
                    try {
                        f = File.createTempFile("jdTemp-", ".res");
                        fos = new FileOutputStream(f);
                        IOUtils.copy(is, fos);
                    } finally {
                        IOUtils.closeQuietly(fos);
                    }
                    for (JDOutput jdOut : outputPlugins) {
                        if (jdOut != null) {
                            FileInputStream fis = null;
                            try {
                                fis = new FileInputStream(f);
                                jdOut.processResource(fileName, fis);
                            } catch (Exception e) {
                                LOGGER.error("Callling processResource() of wrapped JDOutput failed.", e);
                            } finally {
                                IOUtils.closeQuietly(fis);
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Exception occured during handling processResource() for multiple wrapped JDOutput.", e);
                } finally {
                    if (f != null)
                        f.delete();
                }
                break;
        }
    }
}
