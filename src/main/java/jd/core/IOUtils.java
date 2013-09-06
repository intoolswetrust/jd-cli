package jd.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Helper method for jd-cmd.
 */
public final class IOUtils {

    /**
     * Copies data from {@link InputStream} to {@link OutputStream}.
     * 
     * @param is
     * @param os
     * @return
     * @throws IOException
     */
    public static long copy(final InputStream is, final OutputStream os) throws IOException {
        final byte[] buffer = new byte[4096];
        long count = 0;
        int n = 0;
        while (-1 != (n = is.read(buffer))) {
            os.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * Closes given {@link InputStream}.
     * 
     * @param is
     */
    public static void closeQuietly(final InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException ioe) {
        }
    }

    /**
     * Closes given {@link OutputStream}.
     * 
     * @param os
     */
    public static void closeQuietly(final OutputStream os) {
        try {
            if (os != null) {
                os.close();
            }
        } catch (IOException ioe) {
        }
    }

    /**
     * Method similar to {@link Boolean#getBoolean(String)}, but with default value as second parameter.
     * 
     * @param propName
     * @param defaultVal
     * @return
     */
    public static boolean getBoolean(final String propName, final boolean defaultVal) {
        return Boolean.parseBoolean(System.getProperty(propName, Boolean.toString(defaultVal)));
    }

}
