package com.palomamobile.android.sdk.media;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.palomamobile.android.sdk.core.util.Utilities;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class provides base functionality required to upload media content.
 *
 */
public abstract class BaseJobUploadMedia extends BaseRetryPolicyAwareJob<MediaInfo> {

    private static final Logger logger = LoggerFactory.getLogger(BaseJobUploadMedia.class);

    private String mime;
    private String file;

    private IChunkingStrategy chunkingStrategy = new IChunkingStrategy.SimpleChunkingStrategy();

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
        MediaInfo result = callService(mime, file);
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

    protected abstract MediaInfo callService(String mime, String file) throws Exception;

    public String getFile() {
        return file;
    }

    public String getMime() {
        return mime;
    }

    protected IChunkingStrategy getChunkingStrategy() {
        return chunkingStrategy;
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
}
