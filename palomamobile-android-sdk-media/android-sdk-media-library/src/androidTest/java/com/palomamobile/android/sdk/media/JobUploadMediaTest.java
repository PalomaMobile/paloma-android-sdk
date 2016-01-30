package com.palomamobile.android.sdk.media;

import android.net.Uri;
import android.test.InstrumentationTestCase;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.user.TestUtilities;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;

/**
 * Created by Karel Herink
 */
public class JobUploadMediaTest extends InstrumentationTestCase {

    private IMediaManager mediaManager;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ServiceSupport.Instance.init(getInstrumentation().getContext());
        mediaManager = ServiceSupport.Instance.getServiceManager(IMediaManager.class);
    }


    public void testUploadDownloadMediaSimple() throws Throwable {
        doUploadDownloadMedia(new IChunkingStrategy.SimpleChunkingStrategy());
    }

    public void testUploadDownloadMediaChunked() throws Throwable {
        IChunkingStrategy.SimpleChunkingStrategy chunkingStrategy = new IChunkingStrategy.SimpleChunkingStrategy();
        chunkingStrategy.forceChunkSize(4 * 1024);
        doUploadDownloadMedia(chunkingStrategy);
    }

    public void doUploadDownloadMedia(IChunkingStrategy strategy) throws Throwable {
        TestUtilities.registerUserSynchronous(this);

        final String mime = "image/jpg";
        File catImageFile =  MediaTestUtil.fileFromAsset(getInstrumentation().getContext(), "cat.jpg");
        if (catImageFile == null) {
            throw new RuntimeException("Unable to create a file from asset");
        }
        JobUploadMedia catMediaUploadJob = new JobUploadMedia(mime, catImageFile.getAbsolutePath());
        catMediaUploadJob.setForcedChunkingStrategy(strategy);
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
        File dogImageFile =  MediaTestUtil.fileFromAsset(getInstrumentation().getContext(), "dog.jpg");
        if (dogImageFile == null) {
            throw new RuntimeException("Unable to create a file from asset");
        }
        String trailingUri = catMediaInfo.getTrailingMediaUri();
        assertNotNull(trailingUri);

        JobUploadMedia mediaUpdateJob = new JobUploadMedia(trailingUri, "image/jpg", dogImageFile.getAbsolutePath());
        mediaUpdateJob.setForcedChunkingStrategy(strategy);
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

}
