package com.palomamobile.android.sdk.media;

import android.net.Uri;
import android.test.InstrumentationTestCase;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.user.TestUtilities;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;

/**
 * Created by Karel Herink
 */
public class JobUploadUserMediaTest extends InstrumentationTestCase {
    public static final Logger logger = LoggerFactory.getLogger(MediaManagerInstrumentationTest.class);

    private IMediaManager mediaManager;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ServiceSupport.Instance.init(getInstrumentation().getContext());
        mediaManager = ServiceSupport.Instance.getServiceManager(IMediaManager.class);
    }


    public void testUploadDownloadUserMediaSimple() throws Throwable {
        doUploadDownloadUserMedia(new IChunkingStrategy.SimpleChunkingStrategy());
    }

    public void testUploadDownloadUserMediaChunked() throws Throwable {
        IChunkingStrategy.SimpleChunkingStrategy chunkingStrategy = new IChunkingStrategy.SimpleChunkingStrategy();
        chunkingStrategy.forceChunkSize(4 * 1024);
        doUploadDownloadUserMedia(chunkingStrategy);
    }

    public void doUploadDownloadUserMedia(IChunkingStrategy strategy) throws Throwable {
        TestUtilities.registerUserSynchronous(this);
        final String mime = "image/jpg";
        File imageFile = MediaTestUtil.fileFromAsset(getInstrumentation().getContext(), "cat.jpg");
        if (imageFile == null) {
            throw new RuntimeException("Unable to create a file from asset");
        }
        JobUploadUserMedia mediaUploadJob = new JobUploadUserMedia(mime, imageFile.getAbsolutePath());
        mediaUploadJob.setForcedChunkingStrategy(strategy);
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


    public void testUploadDownloadNamedUserMediaSimple() throws Throwable {
        doUploadDownloadNamedUserMedia(new IChunkingStrategy.SimpleChunkingStrategy());
    }

    public void testUploadDownloadNamedUserMediaChunked() throws Throwable {
        IChunkingStrategy.SimpleChunkingStrategy chunkingStrategy = new IChunkingStrategy.SimpleChunkingStrategy();
        chunkingStrategy.forceChunkSize(4 * 1024);
        doUploadDownloadNamedUserMedia(chunkingStrategy);
    }

    public void doUploadDownloadNamedUserMedia(IChunkingStrategy strategy) throws Throwable {
        String trailingMediaUri = "cat_" + UUID.randomUUID().toString();
        TestUtilities.registerUserSynchronous(this);
        final String mime = "image/jpg";
        File imageFile = MediaTestUtil.fileFromAsset(getInstrumentation().getContext(), "cat.jpg");
        if (imageFile == null) {
            throw new RuntimeException("Unable to create a file from asset");
        }
        JobUploadUserMedia mediaUploadJob = new JobUploadUserMedia(trailingMediaUri, mime, imageFile.getAbsolutePath());
        mediaUploadJob.setForcedChunkingStrategy(strategy);
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

}
