package com.palomamobile.android.sdk.media;

import android.util.Base64;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.core.util.Utilities;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.NoSuchAlgorithmException;

/**
 * Abstract class provides base functionality required to upload media content.
 *
 */
public abstract class BaseJobUploadMedia extends BaseRetryPolicyAwareJob<MediaInfo> {

    private static final Logger logger = LoggerFactory.getLogger(BaseJobUploadMedia.class);

    private String mime;
    private String file;

    private IChunkingStrategy chunkingStrategy = new IChunkingStrategy.SimpleChunkingStrategy();
    protected String trailingMediaUri;

    /**
     * Create a new job
     * @param mime file content type as a MIME string
     * @param file containing the media
     */
    public BaseJobUploadMedia(String mime, String file) {
        this(new Params(0).requireNetwork(), mime, file);
    }

    /**
     * Create a new job
     * @param params custom job parameters
     * @param mime file content type as a MIME string
     * @param file containing the media
     */
    public BaseJobUploadMedia(Params params, String mime, String file) {
        super(params);
        this.mime = mime;
        this.file = file;
    }

    @Override
    public MediaInfo syncRun(boolean postEvent) throws Throwable {
        logger.debug("posting " + file + " '" + mime + "'");
        MediaInfo result = upload(mime, file);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMediaUploaded(this, result));
        }
        return result;
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        RetryConstraint retryConstraint = super.shouldReRunOnThrowable(throwable, runCount, maxRunCount);
        if (retryConstraint.shouldRetry()) {
            getChunkingStrategy().adjustStrategyAfterFailure(throwable);
        }
        return retryConstraint;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMediaUploaded(this, throwable));
    }

    public String getFile() {
        return file;
    }

    public String getMime() {
        return mime;
    }

    protected IChunkingStrategy getChunkingStrategy() {
        return chunkingStrategy;
    }

    public String getTrailingMediaUri() {
        return trailingMediaUri;
    }

    public void setChunkingStrategy(IChunkingStrategy chunkingStrategy) {
        this.chunkingStrategy = chunkingStrategy;
    }

    public String getTransferId() {
        String fileName = getFile();
        try {
            return Utilities.md5ToString(Utilities.getMD5(fileName));
        } catch (Exception e) {
            logger.warn("Unable to generate TransferId using MD5, will use the file name (less than ideal).", e);
            return fileName;
        }
    }

    protected abstract MediaInfo uploadSingleFile(TypedFile typedFile) throws IOException;

    protected abstract MediaInfo uploadFileChunk(String transferId, String contentRangeHeaderValue, TypedInput chunk, String contentMd5) throws IOException;

    protected MediaInfo upload(String mime, String file) throws IOException {
        IChunkingStrategy chunkingStrategy = getChunkingStrategy();
        logger.debug(chunkingStrategy.toString());

        MediaInfo response;
        if (chunkingStrategy.isApplyChunking()) {
            logger.debug("uploading in chunks");
            response = uploadInChunks(mime, file);
        }
        else {
            logger.debug("uploading simply");
            TypedFile typedFile = new TypedFile(mime, new File(file));
            response = uploadSingleFile(typedFile);
        }
        return response;
    }

    private MediaInfo uploadInChunks(String mime, String file) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        int offset = 0;
        int bufferSize = getChunkingStrategy().getChunkSize();
        byte[] bytes = new byte[bufferSize];
        String transferId = String.valueOf(getTransferId());

        MediaInfo mediaInfo;
        do {
            int readCount = randomAccessFile.read(bytes);
            byte[] chunkBytes;
            if (readCount == bufferSize) {
                chunkBytes = bytes;
            }
            else {
                chunkBytes = new byte[readCount];
                System.arraycopy(bytes, 0, chunkBytes, 0, readCount);
            }

            String contentMd5 = null;
            TypedByteArray chunk = new TypedByteArray(mime, chunkBytes);
            try {
                byte[] contentMd5Bytes = Utilities.getMD5(chunkBytes);
                contentMd5 = Base64.encodeToString(contentMd5Bytes, Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            mediaInfo = uploadFileChunk(transferId, buildContentRangeHeaderValue(offset, readCount, randomAccessFile.length()), chunk, contentMd5);
            offset += readCount;
        } while (offset < randomAccessFile.length());
        return mediaInfo;
    }

    private static String buildContentRangeHeaderValue(long offset, int byteCount, long totalContentLength) {
        return "bytes " + offset + "-" + (offset + byteCount -1) +
                (totalContentLength == -1 ? "/*" : "/" + totalContentLength);
    }
}
