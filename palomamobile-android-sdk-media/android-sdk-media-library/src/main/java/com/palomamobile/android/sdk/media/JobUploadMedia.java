package com.palomamobile.android.sdk.media;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.path.android.jobqueue.Params;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;

import java.io.File;

/**
 * Convenience wrapper around {@link IMediaService#postMedia(String, TypedInput)}
 * used to post media that becomes available publicly available.
 * Once this job is completed (with success or failure) it posts {@link EventMediaUploaded} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobUploadMedia extends BaseJobUploadMedia {

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

    @Override
    protected MediaInfo callService(String mime, String file) throws Exception {
        IMediaManager mediaManager = ServiceSupport.Instance.getServiceManager(IMediaManager.class);
        TypedFile typedFile = new TypedFile(mime, new File(file));
        Response response;
        if (trailingMediaUri == null) {
            response = mediaManager.getService().postMedia(getRetryId(), typedFile);
        }
        else {
            response = mediaManager.getService().postMedia(getRetryId(), trailingMediaUri, typedFile);
        }
        return getMediaInfo(response);
    }

}
