package com.palomamobile.android.sdk.media;

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
        File imageFile = MediaTestUtil.fileFromAsset(getInstrumentation().getContext(), "cat.jpg");
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
        File imageFile = MediaTestUtil.fileFromAsset(getInstrumentation().getContext(), "cat.jpg");
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
        File imageFile = MediaTestUtil.fileFromAsset(getInstrumentation().getContext(), "cat.jpg");
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
        File catImageFile = MediaTestUtil.fileFromAsset(getInstrumentation().getContext(), "cat.jpg");
        if (catImageFile == null) {
            throw new RuntimeException("Unable to create a file from asset");
        }
        JobUploadUserMedia catMediaUploadJob = new JobUploadUserMedia(mime, catImageFile.getAbsolutePath());
        MediaInfo catMediaInfo1 = catMediaUploadJob.syncRun(false);
        MediaInfo catMediaInfo2 = catMediaUploadJob.syncRun(false);

        //upload 2 dogs
        File dogImageFile = MediaTestUtil.fileFromAsset(getInstrumentation().getContext(), "dog.jpg");
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


}
