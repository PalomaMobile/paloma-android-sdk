package com.palomamobile.android.sdk.media;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.path.android.jobqueue.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;

import java.io.IOException;

/**
 * Convenience wrapper around {@link IMediaService#postMedia(String, TypedInput)}
 * used to post media that becomes available publicly available.
 * Once this job is completed (with success or failure) it posts {@link EventMediaUploaded} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobUploadMedia extends BaseJobUploadMedia {
    private static final Logger logger = LoggerFactory.getLogger(JobUploadMedia.class);

    private String trailingMediaUri;

    /**
     * Create a new public media upload job.
     * @param params custom job parameters
     * @param trailingMediaUri known media Uri for update (create not supported)
     * @param mime file content type as a MIME string
     * @param file path to the file that will be uploaded
     */
    public JobUploadMedia(Params params, String trailingMediaUri, String mime, String file) {
        super(params, mime, file);
        this.trailingMediaUri = trailingMediaUri;
    }

    /**
     * Create a new public media upload job.
     * @param params custom job parameters
     * @param mime file content type as a MIME string
     * @param file path to the file that will be uploaded
     */
    public JobUploadMedia(Params params, String mime, String file) {
        super(params, mime, file);
    }

    /**
     * Create a new public media upload job.
     * @param mime file content type as a MIME string
     * @param file path to the file that will be uploaded
     */
    public JobUploadMedia(String mime, String file) {
        super(mime, file);
    }

    /**
     * Create a new public media upload job.
     * @param trailingMediaUri known media Uri for update (create not supported)
     * @param mime file content type as a MIME string
     * @param file path to the file that will be uploaded
     */
    public JobUploadMedia(String trailingMediaUri, String mime, String file) {
        super(mime, file);
        this.trailingMediaUri = trailingMediaUri;
    }


    protected MediaInfo uploadSingleFile(TypedFile typedFile) throws IOException {
        IMediaService mediaService = ServiceSupport.Instance.getServiceManager(IMediaManager.class).getService();
        if (trailingMediaUri == null) {
            return mediaService.postMedia(getRetryId(), typedFile);
        }
        else {
            return mediaService.postMedia(getRetryId(), trailingMediaUri, typedFile);
        }
    }


    @Override
    protected MediaInfo uploadFileChunk(String transferId, String contentRangeHeaderValue, TypedInput chunk, String contentMd5) throws IOException {
        IMediaService mediaService = ServiceSupport.Instance.getServiceManager(IMediaManager.class).getService();
        MediaInfo mediaInfo;
        if (trailingMediaUri == null) {
            mediaInfo = mediaService.postMediaChunk(getRetryId(), transferId, contentRangeHeaderValue, chunk, contentMd5);
            trailingMediaUri = mediaInfo.getTrailingMediaUri();
        }
        else {
            mediaInfo = mediaService.putMediaChunk(getRetryId(), transferId, contentRangeHeaderValue, trailingMediaUri, chunk, contentMd5);
        }
        return mediaInfo;
    }
}
