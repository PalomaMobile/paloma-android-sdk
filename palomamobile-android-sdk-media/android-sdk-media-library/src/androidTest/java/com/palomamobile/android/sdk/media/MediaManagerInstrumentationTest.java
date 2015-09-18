package com.palomamobile.android.sdk.media;

import android.content.Context;
import android.net.Uri;
import android.test.InstrumentationTestCase;
import android.util.Log;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.user.TestUtilities;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 */
public class MediaManagerInstrumentationTest extends InstrumentationTestCase {

    public static final String TAG = MediaManagerInstrumentationTest.class.getSimpleName();

    private IMediaManager mediaManager;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ServiceSupport.Instance.init(getInstrumentation().getContext());
        mediaManager = ServiceSupport.Instance.getServiceManager(IMediaManager.class);
    }

    public void testUploadDownloadMediaPrivate() throws Throwable {
        TestUtilities.registerUserSynchronous(this);
        final String mime = "image/jpg";
        File imageFile = fileFromAsset("cat.jpg");
        if (imageFile == null) {
            throw new RuntimeException("Unable to create a file from asset");
        }
        JobUploadMediaPrivate mediaUploadJob = mediaManager.createJobMediaUploadPrivate(mime, imageFile.getAbsolutePath());
        MediaInfo mediaInfo = mediaUploadJob.syncRun(false);
        assertNotNull(mediaInfo);
        assertEquals(mime, mediaInfo.getContentType());
        assertNotNull(mediaInfo.getUrl());
        assertTrue(mediaInfo.isSecured());
        assertNotNull(mediaInfo.getExpiringPublicUrl());
        Uri providedExpiringPublicUrl = Uri.parse(mediaInfo.getExpiringPublicUrl());

        String privateUrlStr = mediaInfo.getUrl();
        Uri privateUrl = Uri.parse(privateUrlStr);
        Uri requestedExpiringAuthorizedUrl = mediaManager.requestExpiringPublicUrl(privateUrl);
        assertNotNull(requestedExpiringAuthorizedUrl);
        Log.d(TAG, "providedExpiring:  " + providedExpiringPublicUrl);
        Log.d(TAG, "requestedExpiring: " + requestedExpiringAuthorizedUrl);

        assertEquals(providedExpiringPublicUrl.getHost(), requestedExpiringAuthorizedUrl.getHost());
        assertEquals(providedExpiringPublicUrl.getPath(), requestedExpiringAuthorizedUrl.getPath());

        //retrieving the providedExpiringPublicUrl via a plain http client should work just fine
        OkHttpClient httpClient = new OkHttpClient();
        Call call = httpClient.newCall(new Request.Builder().get().url(requestedExpiringAuthorizedUrl.toString()).build());
        Response response = call.execute();
        assertNotNull(response);
        assertTrue(response.code() >= 200 && response.code() < 300);
        assertNotNull(response.body());
        assertTrue(response.body().contentType().toString().startsWith(mime));
        assertEquals(response.body().contentLength(), imageFile.length());
        response.body().close();

        //retrieving the privateUrl via a plain http client should fail due to lack of auth
        call = httpClient.newCall(new Request.Builder().get().url(privateUrl.toString()).build());
        response = call.execute();
        assertNotNull(response);
        assertTrue(response.code() == 401);
        assertNotNull(response.body());
        response.body().close();
    }

    public void testUploadDownloadMediaPublic() throws Throwable {
        TestUtilities.registerUserSynchronous(this);

        final String mime = "image/jpg";
        File imageFile = fileFromAsset("cat.jpg");
        if (imageFile == null) {
            throw new RuntimeException("Unable to create a file from asset");
        }
        JobUploadMediaPublic mediaUploadJob = mediaManager.createJobMediaUploadPublic(mime, imageFile.getAbsolutePath());
        MediaInfo mediaInfo = mediaUploadJob.syncRun(false);
        assertEquals(mime, mediaInfo.getContentType());
        assertNotNull(mediaInfo.getUrl());
        Uri uri = Uri.parse(mediaInfo.getUrl());
        assertFalse(mediaInfo.isSecured());
        assertNull(mediaInfo.getExpiringPublicUrl());
        assertEquals(uri, mediaManager.requestExpiringPublicUrl(uri));


        //retrieving the providedExpiringPublicUrl via a plain http client should work just fine
        OkHttpClient httpClient = new OkHttpClient();
        Call call = httpClient.newCall(new Request.Builder().get().url(mediaInfo.getUrl()).build());
        Response response = call.execute();
        assertNotNull(response);
        assertTrue(response.code() >= 200 && response.code() < 300);
        assertNotNull(response.body());
        assertTrue(response.body().contentType().toString().startsWith(mime));
        assertEquals(response.body().contentLength(), imageFile.length());
        response.body().close();
    }

    private File fileFromAsset(String assetName) {
        File cacheFile = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            cacheFile = createCacheFile(getInstrumentation().getContext(), Long.toString(System.currentTimeMillis()));
            outputStream = new FileOutputStream(cacheFile);
            inputStream = getInstrumentation().getContext().getAssets().open(assetName);
            copy(inputStream, outputStream);
        } catch (IOException e) {
            Log.w(TAG, "unable to create cache file." , e);
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
