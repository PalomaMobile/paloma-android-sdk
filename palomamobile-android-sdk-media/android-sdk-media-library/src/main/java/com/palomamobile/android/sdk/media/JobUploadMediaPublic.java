package com.palomamobile.android.sdk.media;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.path.android.jobqueue.Params;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;

import java.io.File;

/**
 * Convenience wrapper around {@link IMediaService#postMediaPublic(String, TypedInput)}
 * used to post media that becomes available publicly available.
 * Once this job is completed (with success or failure) it posts {@link EventMediaUploaded} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobUploadMediaPublic extends BaseJobUploadMedia {

    private static final String TAG = JobUploadMediaPublic.class.getSimpleName();

    /**
     * Create a new public media upload job.
     * @param params custom job parameters
     * @param mime file content type as a MIME string
     * @param file path to the file that will be uploaded
     */
    public JobUploadMediaPublic(Params params, String mime, String file) {
        super(params, mime, file);
    }

    /**
     * Create a new public media upload job.
     * @param mime file content type as a MIME string
     * @param file path to the file that will be uploaded
     */
    public JobUploadMediaPublic(String mime, String file) {
        super(mime, file);
    }

    @Override
    protected MediaInfo callService(String mime, String file) throws Exception {
        IMediaManager mediaManager = ServiceSupport.Instance.getServiceManager(IMediaManager.class);
        Response response = mediaManager.getService().postMediaPublic(getRetryId(), new TypedFile(mime, new File(file)));
        return getMediaInfo(response);
    }

}
