package com.palomamobile.android.sdk.media;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.user.IUserManager;
import com.path.android.jobqueue.Params;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;

import java.io.File;

/**
 * Convenience wrapper around {@link IMediaService#postMediaPrivate(String, long, TypedInput)}}
 * used to post media that becomes available only to the posting user.
 * Once this job is completed (with success or failure) it posts {@link EventMediaUploaded} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobUploadMediaPrivate extends BaseJobUploadMedia {

    /**
     * Create a new private media upload job.
     * @param params custom job parameters
     * @param mime file content type as a MIME string
     * @param file path to the file that will be uploaded
     */
    public JobUploadMediaPrivate(Params params, String mime, String file) {
        super(params, mime, file);
    }

    /**
     * Create a new private media upload job.
     * @param mime file content type as a MIME string
     * @param file path to the file that will be uploaded
     */
    public JobUploadMediaPrivate(String mime, String file) {
        super(mime, file);
    }

    @Override
    protected MediaInfo callService(String mime, String file) throws ConversionException {
        IUserManager userManager = ServiceSupport.Instance.getServiceManager(IUserManager.class);
        IMediaManager mediaManager = ServiceSupport.Instance.getServiceManager(IMediaManager.class);
        Response response = mediaManager.getService().postMediaPrivate(getRetryId(), userManager.getUser().getId(), new TypedFile(mime, new File(file)));
        return getMediaInfo(response);
    }

}
