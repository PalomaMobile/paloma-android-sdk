package com.palomamobile.android.sdk.core.qos;

import android.test.InstrumentationTestCase;
import android.util.Log;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.Params;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.ConversionException;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 */
public class DefaultRetryPolicyProviderTest extends InstrumentationTestCase {
    private static final String TAG = DefaultRetryPolicyProviderTest.class.getSimpleName();

    private JobManager jobManager;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ServiceSupport.Instance.init(getInstrumentation().getContext());
        jobManager = ServiceSupport.Instance.getJobManager();
    }

    public void testRuntimeExceptionFail() throws Throwable {
        long workDurationMs = 1000;
        int maxAttempts = 10;
        long initialBackOffMs = 2000;
        TestFailJob job = new TestFailJob(workDurationMs, new RuntimeException(), maxAttempts, initialBackOffMs);
        jobManager.addJob(job);
        Thread.sleep(2100);
        long actualJobDuration = job.failTime - job.startTime;
        assertEquals(1, job.getCurrentRunCount());
        assertTrue(actualJobDuration >= job.workDurationMs);
    }


    public void testRetrofitNetworkErrorFailQuick() throws Throwable {
        long workDurationMs = 2000;
        int maxAttempts = 1;
        long initialBackOffMs = 1000;
        TestFailJob job = new TestFailJob(workDurationMs, RetrofitError.networkError("http://www.google.com", new IOException()), maxAttempts, initialBackOffMs);
        jobManager.addJob(job);
        Thread.sleep(initialBackOffMs + workDurationMs + 200);
        assertEquals(1, job.getCurrentRunCount());

        Thread.sleep(5000);
        long actualJobDuration = job.failTime - job.startTime;
        Log.d(TAG, "testRetrofitNetworkErrorFailQuick() actualJobDuration: " + actualJobDuration + ", job.workDurationMs: "+job.workDurationMs);
        assertEquals(maxAttempts, job.getCurrentRunCount());
        assertTrue(actualJobDuration >= job.workDurationMs);
    }


    public void testRetrofitNetworkErrorFail() throws Throwable {
        long workDurationMs = 2000;
        int maxAttempts = 5;
        long initialBackOffMs = 1000;
        TestFailJob job = new TestFailJob(workDurationMs, RetrofitError.networkError("http://www.google.com", new IOException()), maxAttempts, initialBackOffMs);
        jobManager.addJob(job);
        Thread.sleep(initialBackOffMs + workDurationMs + 200);
        assertEquals(2, job.getCurrentRunCount());

        Thread.sleep(30000);
        long actualJobDuration = job.failTime - job.startTime;
        assertEquals(maxAttempts, job.getCurrentRunCount());
        assertTrue(actualJobDuration >= job.workDurationMs);
    }

    public void testRetrofitConversionErrorFail() throws Throwable {
        long workDurationMs = 1000;
        int maxAttempts = 10;
        long initialBackOffMs = 2000;
        TestFailJob job = new TestFailJob(workDurationMs, RetrofitError.conversionError(null, null, null, null, new ConversionException("")), maxAttempts, initialBackOffMs);
        jobManager.addJob(job);
        Thread.sleep(2100);
        long actualJobDuration = job.failTime - job.startTime;
        Log.d(TAG, "testRetrofitConversionErrorFail() actualJobDuration: " + actualJobDuration + " , job.workDurationMs: " + job.workDurationMs);
        assertEquals(1, job.getCurrentRunCount());
        assertTrue(actualJobDuration >= job.workDurationMs);
    }

    public void testRetrofitHttpErrorRetryableFail() throws Throwable {
        long workDurationMs = 2000;
        int maxAttempts = 5;
        long initialBackOffMs = 1000;
        //http status 500 is re-tryable
        TestFailJob job = new TestFailJob(workDurationMs, RetrofitError.httpError("", new Response("", 500, "", new ArrayList<Header>(), null), null, null), maxAttempts, initialBackOffMs);
        assertEquals(0, job.getCurrentRunCount());
        jobManager.addJob(job);
        Thread.sleep(initialBackOffMs + workDurationMs + 200);
        assertEquals(2, job.getCurrentRunCount());

        Thread.sleep(30000);
        long actualJobDuration = job.failTime - job.startTime;
        assertEquals(maxAttempts, job.getCurrentRunCount());
        assertTrue(actualJobDuration >= job.workDurationMs);
    }

    public void testRetrofitHttpErrorNonRetryableFail() throws Throwable {
        long workDurationMs = 1000;
        int maxAttempts = 10;
        long initialBackOffMs = 2000;
        //http status 400 is NOT re-tryable
        TestFailJob job = new TestFailJob(workDurationMs, RetrofitError.httpError("", new Response("", 400, "", new ArrayList<Header>(), null), null, null), maxAttempts, initialBackOffMs);
        jobManager.addJob(job);
        Thread.sleep(1100);
        long actualJobDuration = job.failTime - job.startTime;
        Log.d(TAG, "testRetrofitHttpErrorNonRetryableFail() actualJobDuration: " + actualJobDuration + " , job.workDurationMs: " + job.workDurationMs);
        assertEquals(1, job.getCurrentRunCount());
        assertTrue(actualJobDuration >= job.workDurationMs);
    }

    public static class TestFailJob extends BaseRetryPolicyAwareJob<String> {

        public static long lastRunTime = 0;

        private static final String TAG = TestFailJob.class.getSimpleName();


        private final long workDurationMs;
        private final Throwable upchuck;
        private long initialBackOffMs;

        private long startTime;
        private long failTime;

        /**
         * Create a new job with the supplied params.
         */
        public TestFailJob(long workDurationMs, Throwable upchuck, int maxAttempts, long initialBackOffMs) {
            super(new Params(1));
            setMaxAttempts(maxAttempts);

            this.workDurationMs = workDurationMs;
            this.upchuck = upchuck;
            this.initialBackOffMs = initialBackOffMs;
        }

        @Override
        protected void postFailure(Throwable throwable) {
            failTime = System.currentTimeMillis();
            Log.i(TAG, "postFailure() failTime: " + failTime);
        }

        @Override
        public void onAdded() {
            super.onAdded();
            startTime = System.currentTimeMillis();
            Log.i(TAG, "onAdded() startTime: " + startTime);
        }

        @Override
        public String syncRun(boolean postEvent) throws Throwable {
            Log.i(TAG, "syncRun() runCount = " + getCurrentRunCount() + ", last executed: " + (lastRunTime == 0 ? 0 : (System.currentTimeMillis() - lastRunTime)) + " ms ago");
            lastRunTime = System.currentTimeMillis();
            Thread.sleep(workDurationMs);
            throw upchuck;

        }

        @Override
        public int getCurrentRunCount() {
            return super.getCurrentRunCount();
        }

        @Override
        protected long getInitialBackOffInMs() {
            return initialBackOffMs;
        }

        @Override
        public String toString() {
            return "TestFailJob{" +
                    "failTime=" + failTime +
                    ", workDurationMs=" + workDurationMs +
                    ", upchuck=" + upchuck +
                    ", initialBackOffMs=" + initialBackOffMs +
                    ", startTime=" + startTime +
                    ", runCount=" + getCurrentRunCount() +
                    "} " + super.toString();
        }
    }

}
