package jd.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class IOUtils {

    public static long copy(final InputStream input, final OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static void closeQuietly(final InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException ioe) {
        }
    }

    public static void closeQuietly(final OutputStream os) {
        try {
            if (os != null) {
                os.close();
            }
        } catch (IOException ioe) {
        }
    }

}
