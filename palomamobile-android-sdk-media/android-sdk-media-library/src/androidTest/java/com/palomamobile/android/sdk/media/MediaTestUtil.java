package com.palomamobile.android.sdk.media;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Karel Herink
 */
public class MediaTestUtil {
    public static File fileFromAsset(Context context, String assetName) {
        File cacheFile = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            cacheFile = createCacheFile(context, Long.toString(System.currentTimeMillis()));
            outputStream = new FileOutputStream(cacheFile);
            inputStream = context.getAssets().open(assetName);
            copy(inputStream, outputStream);
        } catch (IOException e) {
            MediaManagerInstrumentationTest.logger.warn("unable to create cache file.", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
            }
        }
        return cacheFile;
    }

    private static int copy(InputStream input, OutputStream output) throws IOException{
        byte[] buffer = new byte[8192];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context    The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File createCacheFile(Context context, String uniqueName) throws IOException {
        File file = new File(context.getCacheDir().getPath(), uniqueName);
        if (file.createNewFile()) {
            return file;
        }
        throw new IOException("Unable to create cache file: " + file.getAbsolutePath());
    }
}
