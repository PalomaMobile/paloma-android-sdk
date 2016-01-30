package com.palomamobile.android.sdk.media;

import android.os.Environment;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import okio.BufferedSink;
import okio.Okio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * Created by Karel Herink
 */
public class JobReliableDownload extends BaseRetryPolicyAwareJob<File> {

    public static final String PROGRESS_FILE_POSTFIX = ".inProgress";

    private static final Logger logger = LoggerFactory.getLogger(JobReliableDownload.class);

    private String url;
    private String filePath;

    private IChunkingStrategy forcedChunkingStrategy;


    public JobReliableDownload(String url) {
        this(new Params(0).requireNetwork(), url);
    }

    public JobReliableDownload(String url, String resultFilePath) {
        this(new Params(0).requireNetwork(), url, resultFilePath);
    }

    /**
     * Create a new job with the supplied params.
     *
     * @param params
     */
    public JobReliableDownload(Params params, String url) {
        super(params);
        this.url = url;
        this.filePath = getRetryId();
    }

    /**
     * Create a new job with the supplied params.
     *
     * @param params
     */
    public JobReliableDownload(Params params, String url, String resultFilePath) {
        super(params);
        this.url = url;
        this.filePath = resultFilePath;
    }

    /**
     * @return the current applicable chunking strategy for this job.
     */
    protected IChunkingStrategy getChunkingStrategy() {
        return forcedChunkingStrategy != null ? forcedChunkingStrategy : ServiceSupport.Instance.getServiceManager(IMediaManager.class).getChunkingStrategy();
    }

    /**
     * Set persistent custom chunking strategy for this job to override the default value as returned by {@link IMediaManager#getChunkingStrategy()}.
     * @param forcedChunkingStrategy
     */
    public void setForcedChunkingStrategy(IChunkingStrategy forcedChunkingStrategy) {
        this.forcedChunkingStrategy = forcedChunkingStrategy;
    }

    public String getUrl() {
        return url;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventReliableDownloadCompleted(this, throwable));
    }

    @Override
    public File syncRun(boolean postEvent) throws Throwable {
        File progressFile = getProgressFile();

        IChunkingStrategy chunkingStrategy = getChunkingStrategy();

        OkHttpClient httpClient = new OkHttpClient();

        long remoteContentLength = getServerContentLength(httpClient);

        if (progressFile.length() != remoteContentLength) {

            //something has gone awfully wrong and we need to restart the download by clearing the progress file
            if (progressFile.length() > remoteContentLength) {
                clearProgressFile(progressFile);
            }

            Request.Builder builder = new Request.Builder().get().url(getUrl());

            boolean canApplyChunking = chunkingStrategy.isApplyChunking() && remoteContentLength != -1;

            if (canApplyChunking) {
                long remoteBytesToGet = remoteContentLength - progressFile.length();
                do {
                    long chunkSize = Math.min(chunkingStrategy.getChunkSize(), remoteBytesToGet);
                    builder.addHeader("Range: bytes", progressFile.length() + "-" + (progressFile.length() + chunkSize));
                    //do actual download
                    Call call = httpClient.newCall(builder.build());
                    download(call, progressFile);
                    remoteBytesToGet -= chunkSize;
                } while (remoteBytesToGet > 0);
            }
            else {
                builder.addHeader("Range: bytes", progressFile.length() + "-");
                Call call = httpClient.newCall(builder.build());
                download(call, progressFile);
            }
        }

        File result = finalizeProgressFile(progressFile);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventReliableDownloadCompleted(this, result));
        }
        return result;
    }


    private void download(Call call, File progressFile) throws IOException {
        Response response = call.execute();
        if (response.isSuccessful()) {
            BufferedSink sink = Okio.buffer(Okio.sink(progressFile));
            sink.writeAll(response.body().source());
            sink.close();
        }
        else {
            if (isResponseFailTemporary(response)) {
                throw new IOException("HTTP response contains a temporary err status code: " + response.code() + ", " + response.message());
            }
            throw new IllegalStateException("HTTP response is a hard fail. Err status code: " + response.code() + ", " + response.message());
        }

    }

    /**
     *
     * @param response unsuccessful (defined by {@link Response#isSuccessful()} returns {code}false{code}) )
     * @return
     */
    public boolean isResponseFailTemporary(Response response) {
        if (response.isSuccessful()) {
            throw new IllegalArgumentException("Response is successful");
        }
        return response.code() >= 500; //server errors should be temporary :)
    }

    @Override
    public boolean isExceptionTemporary(Throwable throwable) {
        if (super.isExceptionTemporary(throwable)) {
            return true;
        }
        if (throwable instanceof IOException) {
            return true; //most likely connectivity issues or disk full
        }
        return false;
    }

    /**
     *
     * @return
     */
    public File getProgressFile() throws IOException {
        File progressFile = new File(filePath + PROGRESS_FILE_POSTFIX);
        try {
            progressFile.createNewFile();
        } catch (IOException e) {
            logger.info("Unable to create a new progress file at: " + progressFile);
            return createRandomProgressFile();
        }
        if (!progressFile.isFile()) {
            logger.info("progressFile NOT a file: " + progressFile);
            return createRandomProgressFile();
        }
        return progressFile;
    }

    private File createRandomProgressFile() throws IOException {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), UUID.randomUUID().toString() + PROGRESS_FILE_POSTFIX);
        file.createNewFile();
        return file;
    }

    /**
     * Rename the completed progress file to final file name.
     * Return the renamed file, or the temporary file if rename was not possible.
     */
    private File finalizeProgressFile(File progressFile) {
        File result = new File(filePath);
        if (progressFile.renameTo(result)) {
            return result;
        }
        return progressFile;
    }

    private void clearProgressFile(File progressFile) throws IOException {
        if (progressFile.exists()) {
            (new RandomAccessFile(progressFile, "rw")).setLength(0);
        }
    }

    protected long getServerContentLength(OkHttpClient httpClient) throws IOException {
        long remoteContentLength = -1;
        Request.Builder builder = new Request.Builder().head().url(getUrl());
        Call call = httpClient.newCall(builder.build());
        Response response = call.execute();
        if (isResponseCodeSuccess(response.code())) {
            String lengthValue = response.header("Content-Length");
            if (lengthValue != null) {
                remoteContentLength = Long.parseLong(lengthValue);
            }
        }
        response.body().close();
        return remoteContentLength;
    }

}
