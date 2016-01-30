package com.palomamobile.android.sdk.media;

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
public class JobUploadApplicationMediaTest extends InstrumentationTestCase {
    public static final Logger logger = LoggerFactory.getLogger(MediaManagerInstrumentationTest.class);

    private IMediaManager mediaManager;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ServiceSupport.Instance.init(getInstrumentation().getContext());
        mediaManager = ServiceSupport.Instance.getServiceManager(IMediaManager.class);
    }


    public void testUploadDownloadAppMediaSimple() throws Throwable {
        doUploadDownloadAppMedia(new IChunkingStrategy.SimpleChunkingStrategy());
    }

    public void testUploadDownloadAppMediaChunked() throws Throwable {
        IChunkingStrategy.SimpleChunkingStrategy chunkingStrategy = new IChunkingStrategy.SimpleChunkingStrategy();
        chunkingStrategy.forceChunkSize(4 * 1024);
        doUploadDownloadAppMedia(chunkingStrategy);
    }

    public void doUploadDownloadAppMedia(IChunkingStrategy strategy) throws Throwable {
        String trailingMediaUri = "cat_" + UUID.randomUUID().toString();
        TestUtilities.registerUserSynchronous(this);
        final String mime = "image/jpg";
        File imageFile = MediaTestUtil.fileFromAsset(getInstrumentation().getContext(), "cat.jpg");
        if (imageFile == null) {
            throw new RuntimeException("Unable to create a file from asset");
        }
        JobUploadApplicationMedia mediaUploadJob = new JobUploadApplicationMedia(trailingMediaUri, mime, imageFile.getAbsolutePath());
        mediaUploadJob.setForcedChunkingStrategy(strategy);
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


    public void testUploadDownloadNamedAppMediaSimple() throws Throwable {
        doUploadDownloadNamedAppMedia(new IChunkingStrategy.SimpleChunkingStrategy());
    }

    public void testUploadDownloadNamedAppMediaChunked() throws Throwable {
        IChunkingStrategy.SimpleChunkingStrategy chunkingStrategy = new IChunkingStrategy.SimpleChunkingStrategy();
        chunkingStrategy.forceChunkSize(4 * 1024);
        doUploadDownloadNamedAppMedia(chunkingStrategy);
    }

    public void doUploadDownloadNamedAppMedia(IChunkingStrategy strategy) throws Throwable {
        String trailingMediaUri = "cat_" + UUID.randomUUID().toString();
        TestUtilities.registerUserSynchronous(this);
        final String mime = "image/jpg";
        File imageFile = MediaTestUtil.fileFromAsset(getInstrumentation().getContext(), "cat.jpg");
        if (imageFile == null) {
            throw new RuntimeException("Unable to create a file from asset");
        }
        JobUploadApplicationMedia mediaUploadJob = new JobUploadApplicationMedia(trailingMediaUri, mime, imageFile.getAbsolutePath());
        mediaUploadJob.setForcedChunkingStrategy(strategy);
        MediaInfo mediaInfo = mediaUploadJob.syncRun(false);
        assertNotNull(mediaInfo);
        assertEquals(mediaUploadJob.getMime(), mediaInfo.getContentType());
        assertNotNull(mediaInfo.getUrl());
        assertFalse(mediaInfo.isSecured());
        assertEquals(mediaUploadJob.getTrailingMediaUri(), mediaInfo.getTrailingMediaUri());
        assertNull(mediaInfo.getExpiringPublicUrl());

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


}
