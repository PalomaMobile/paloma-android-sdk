package com.palomamobile.android.sdk.media;

import android.content.Context;
import android.net.Uri;
import android.test.InstrumentationTestCase;
import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceRequestParams;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.util.LatchedBusListener;
import com.palomamobile.android.sdk.user.TestUtilities;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MediaManagerInstrumentationTest extends InstrumentationTestCase {

    public static final Logger logger = LoggerFactory.getLogger(MediaManagerInstrumentationTest.class);

    private IMediaManager mediaManager;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ServiceSupport.Instance.init(getInstrumentation().getContext());
        mediaManager = ServiceSupport.Instance.getServiceManager(IMediaManager.class);
    }

    public void testUploadDownloadUserMedia() throws Throwable {
        TestUtilities.registerUserSynchronous(this);
        final String mime = "image/jpg";
        File imageFile = fileFromAsset("cat.jpg");
        if (imageFile == null) {
            throw new RuntimeException("Unable to create a file from asset");
        }
        JobUploadUserMedia mediaUploadJob = new JobUploadUserMedia(mime, imageFile.getAbsolutePath());
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
        logger.debug("providedExpiring:  " + providedExpiringPublicUrl);
        logger.debug("requestedExpiring: " + requestedExpiringAuthorizedUrl);

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


    public void testUploadDownloadNamedUserMedia() throws Throwable {
        String trailingMediaUri = "cat_" + UUID.randomUUID().toString();
        TestUtilities.registerUserSynchronous(this);
        final String mime = "image/jpg";
        File imageFile = fileFromAsset("cat.jpg");
        if (imageFile == null) {
            throw new RuntimeException("Unable to create a file from asset");
        }
        JobUploadUserMedia mediaUploadJob = new JobUploadUserMedia(trailingMediaUri, mime, imageFile.getAbsolutePath());
        MediaInfo mediaInfo = mediaUploadJob.syncRun(false);
        assertNotNull(mediaInfo);
        assertEquals(mediaUploadJob.getMime(), mediaInfo.getContentType());
        assertNotNull(mediaInfo.getUrl());
        assertTrue(mediaInfo.isSecured());
        assertEquals(mediaUploadJob.getTrailingMediaUri(), mediaInfo.getTrailingMediaUri());
        assertNotNull(mediaInfo.getExpiringPublicUrl());
        Uri providedExpiringPublicUrl = Uri.parse(mediaInfo.getExpiringPublicUrl());

        String privateUrlStr = mediaInfo.getUrl();
        Uri privateUrl = Uri.parse(privateUrlStr);
        Uri requestedExpiringAuthorizedUrl = mediaManager.requestExpiringPublicUrl(privateUrl);
        assertNotNull(requestedExpiringAuthorizedUrl);
        logger.debug("providedExpiring:  " + providedExpiringPublicUrl);
        logger.debug("requestedExpiring: " + requestedExpiringAuthorizedUrl);

        assertEquals(providedExpiringPublicUrl.getHost(), requestedExpiringAuthorizedUrl.getHost());
        assertEquals(providedExpiringPublicUrl.getPath(), requestedExpiringAuthorizedUrl.getPath());

        //retrieving the providedExpiringPublicUrl via a plain http client should work just fine
        OkHttpClient httpClient = new OkHttpClient();
        Call call = httpClient.newCall(new Request.Builder().get().url(requestedExpiringAuthorizedUrl.toString()).build());
        Response response = call.execute();
        assertNotNull(response);
        assertTrue(response.code() >= 200 && response.code() < 300);
        assertNotNull(response.body());
        assertTrue(response.body().contentType().toString().startsWith(mediaUploadJob.getMime()));
        assertEquals(response.body().contentLength(), new File(mediaUploadJob.getFile()).length());
        response.body().close();

        //retrieving the privateUrl via a plain http client should fail due to lack of auth
        call = httpClient.newCall(new Request.Builder().get().url(privateUrl.toString()).build());
        response = call.execute();
        assertNotNull(response);
        assertTrue(response.code() == 401);
        assertNotNull(response.body());
        response.body().close();
    }

    public void testUploadDownloadNamedApplicationMedia() throws Throwable {
        String trailingMediaUri = "cat_" + UUID.randomUUID().toString();
        TestUtilities.registerUserSynchronous(this);
        final String mime = "image/jpg";
        File imageFile = fileFromAsset("cat.jpg");
        if (imageFile == null) {
            throw new RuntimeException("Unable to create a file from asset");
        }
        JobUploadApplicationMedia mediaUploadJob = new JobUploadApplicationMedia(trailingMediaUri, mime, imageFile.getAbsolutePath());
        MediaInfo mediaInfo = mediaUploadJob.syncRun(false);
        assertNotNull(mediaInfo);
        assertEquals(mediaUploadJob.getMime(), mediaInfo.getContentType());
        assertNotNull(mediaInfo.getUrl());
        assertFalse(mediaInfo.isSecured());
        assertEquals(mediaUploadJob.getTrailingMediaUri(), mediaInfo.getTrailingMediaUri());

        //retrieving the providedExpiringPublicUrl via a plain http client should work just fine
        OkHttpClient httpClient = new OkHttpClient();
        Call call = httpClient.newCall(new Request.Builder().get().url(mediaInfo.getUrl()).build());
        Response response = call.execute();
        assertNotNull(response);
        assertTrue(response.code() >= 200 && response.code() < 300);
        assertNotNull(response.body());
        assertTrue(response.body().contentType().toString().startsWith(mediaUploadJob.getMime()));
        assertEquals(response.body().contentLength(), new File(mediaUploadJob.getFile()).length());
        response.body().close();
    }


    public void testListUserMedia() throws Throwable {
        TestUtilities.registerUserSynchronous(this);
        final String mime = "image/jpg";

        //upload 2 cats
        File catImageFile = fileFromAsset("cat.jpg");
        if (catImageFile == null) {
            throw new RuntimeException("Unable to create a file from asset");
        }
        JobUploadUserMedia catMediaUploadJob = new JobUploadUserMedia(mime, catImageFile.getAbsolutePath());
        MediaInfo catMediaInfo1 = catMediaUploadJob.syncRun(false);
        MediaInfo catMediaInfo2 = catMediaUploadJob.syncRun(false);

        //upload 2 dogs
        File dogImageFile = fileFromAsset("dog.jpg");
        if (dogImageFile == null) {
            throw new RuntimeException("Unable to create a file from asset");
        }
        JobUploadUserMedia dogMediaUploadJob = new JobUploadUserMedia(mime, dogImageFile.getAbsolutePath());
        MediaInfo dogMediaInfo1 = dogMediaUploadJob.syncRun(false);
        MediaInfo dogMediaInfo2 = dogMediaUploadJob.syncRun(false);


        JobListUserMedia jobListUserMedia = new JobListUserMedia();
        jobListUserMedia.setServiceRequestParams(new ServiceRequestParams().sort("id", ServiceRequestParams.Sort.Order.Desc));

        final LatchedBusListener<EventUserMediaListReceived> latchedBusListener = new LatchedBusListener<>(EventUserMediaListReceived.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListener);
        ServiceSupport.Instance.getJobManager().addJob(jobListUserMedia);
        latchedBusListener.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListener);
        assertNotNull(latchedBusListener.getEvent());
        assertNull(latchedBusListener.getEvent().getFailure());
        PaginatedResponse<MediaInfo> success = latchedBusListener.getEvent().getSuccess();
        assertEquals(4, success.getPage().getTotalElements());
        List<MediaInfo> items = success.getEmbedded().getItems();
        assertEquals(4, items.size());
        assertEquals(dogMediaInfo2.getId(), items.get(0).getId());
        assertEquals(dogMediaInfo1.getId(), items.get(1).getId());
        assertEquals(catMediaInfo2.getId(), items.get(2).getId());
        assertEquals(catMediaInfo1.getId(), items.get(3).getId());
    }


    public void testUploadDownloadMedia() throws Throwable {
        TestUtilities.registerUserSynchronous(this);

        final String mime = "image/jpg";
        File catImageFile = fileFromAsset("cat.jpg");
        if (catImageFile == null) {
            throw new RuntimeException("Unable to create a file from asset");
        }
        JobUploadMedia catMediaUploadJob = new JobUploadMedia(mime, catImageFile.getAbsolutePath());
        MediaInfo catMediaInfo = catMediaUploadJob.syncRun(false);
        assertEquals(mime, catMediaInfo.getContentType());
        assertNotNull(catMediaInfo.getUrl());
        Uri uri = Uri.parse(catMediaInfo.getUrl());
        assertFalse(catMediaInfo.isSecured());
        assertNull(catMediaInfo.getExpiringPublicUrl());
        assertEquals(uri, mediaManager.requestExpiringPublicUrl(uri));


        //retrieving the providedExpiringPublicUrl via a plain http client should work just fine
        OkHttpClient httpClient = new OkHttpClient();
        Call catCall = httpClient.newCall(new Request.Builder().get().url(catMediaInfo.getUrl()).build());
        Response catResponse = catCall.execute();
        assertNotNull(catResponse);
        assertTrue(catResponse.code() >= 200 && catResponse.code() < 300);
        assertNotNull(catResponse.body());
        assertTrue(catResponse.body().contentType().toString().startsWith(mime));
        assertEquals(catResponse.body().contentLength(), catImageFile.length());
        catResponse.body().close();


        //we should be able to do a PUT to update the media (to DOG) by it's trailing uri
        File dogImageFile = fileFromAsset("dog.jpg");
        if (dogImageFile == null) {
            throw new RuntimeException("Unable to create a file from asset");
        }
        String trailingUri = catMediaInfo.getTrailingMediaUri();
        assertNotNull(trailingUri);

        JobUploadMedia mediaUpdateJob = new JobUploadMedia(trailingUri, "image/jpg", dogImageFile.getAbsolutePath());
        MediaInfo dogMediaInfo = mediaUpdateJob.syncRun(false);
        assertEquals(mime, dogMediaInfo.getContentType());
        assertNotNull(dogMediaInfo.getUrl());
        Uri updateUri = Uri.parse(dogMediaInfo.getUrl());
        assertFalse(dogMediaInfo.isSecured());
        assertNull(dogMediaInfo.getExpiringPublicUrl());
        assertEquals(uri, mediaManager.requestExpiringPublicUrl(updateUri));
        assertEquals(uri, updateUri);
        assertEquals(trailingUri, dogMediaInfo.getTrailingMediaUri());

        //currently server returns a cached version of CAT
        //XXX enable the below test and set the correct (immediate) cache expiry to make the test pass, after MED-29 is fixed
        boolean MED_29_FIXED = false;
        if (MED_29_FIXED) {
            Call dogCall = httpClient.newCall(new Request.Builder().get().url(dogMediaInfo.getUrl()).build());
            Response dogResponse = dogCall.execute();
            assertNotNull(dogResponse);
            assertTrue(dogResponse.code() >= 200 && dogResponse.code() < 300);
            assertNotNull(dogResponse.body());
            assertTrue(dogResponse.body().contentType().toString().startsWith(mime));
            assertEquals(dogResponse.body().contentLength(), dogImageFile.length());
            dogResponse.body().close();
        }
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
            logger.warn("unable to create cache file.", e);
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
