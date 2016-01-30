package com.palomamobile.android.sdk.media;

import android.test.InstrumentationTestCase;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.util.LatchedBusListener;
import com.path.android.jobqueue.Params;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Karel Herink
 */
public class JobReliableDownloadTest extends InstrumentationTestCase {

    private final static String url = "https://raw.githubusercontent.com/PalomaMobile/paloma-android-sdk/app_chunking/palomamobile-android-sdk-media/android-sdk-media-library/src/androidTest/assets/10.bytes";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ServiceSupport.Instance.init(getInstrumentation().getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testNoChunkingStrategy() throws InterruptedException {
        final LatchedBusListener<EventReliableDownloadCompleted> busListener = new LatchedBusListener<>(EventReliableDownloadCompleted.class);
        ServiceSupport.Instance.getEventBus().register(busListener);

        JobReliableDownload downloadJob = new JobReliableDownload(url);
        //set NOT chunking strategy
        downloadJob.setForcedChunkingStrategy(new IChunkingStrategy.SimpleChunkingStrategy(-1));
        //download the entire file
        ServiceSupport.Instance.getEventBus().post(downloadJob);

        busListener.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(busListener);

        EventReliableDownloadCompleted event = busListener.getEvent();
        assertNotNull(event);
        assertNull(event.getFailure());
        assertNotNull(event.getSuccess());
    }

    public void testNoChunkingStrategyResume() {
        //set NOT chunking strategy
        //append correct 5 bytes to progress file to simulate partial download
        //download the rest of file
    }

    public void testNoChunkingNoRemoteContentLengthAvailable() {
        //use CustomTestNoRemoteLengthDownloadJob
        //set NOT chunking strategy
        //download the entire file
    }

    public void testNoChunkingNoRemoteContentLengthAvailableResume() {
        //use CustomTestNoRemoteLengthDownloadJob
        //set NOT chunking strategy
        //append correct 5 bytes to progress file to simulate partial download
        //download the rest of file
    }

    public void testWithChunkingStrategy() {
        //set CUSTOM strategy of 2 bytes per chunk
        //download the entire file
    }

    public void testWithChunkingStrategyResume() {
        //set CUSTOM strategy of 2 bytes per chunk
        //append correct 5 bytes to progress file to simulate partial download
        //download the rest of file
    }

    public void testBadProgressFilePath() {

    }

    public void testProgressFileBiggerThanRemoteFile() {

    }

    private class CustomTestNoRemoteLengthDownloadJob extends JobReliableDownload {

        public CustomTestNoRemoteLengthDownloadJob(Params params, String url) {
            super(params, url);
        }

        public CustomTestNoRemoteLengthDownloadJob(Params params, String url, String resultFilePath) {
            super(params, url, resultFilePath);
        }

        public CustomTestNoRemoteLengthDownloadJob(String url) {
            super(url);
        }

        public CustomTestNoRemoteLengthDownloadJob(String url, String resultFilePath) {
            super(url, resultFilePath);
        }

        @Override
        protected long getServerContentLength(OkHttpClient httpClient) throws IOException {
            return -1;
        }

    }

}
