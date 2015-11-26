package com.palomamobile.android.sdk.media;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.user.IUserManager;
import com.path.android.jobqueue.Params;
import retrofit.converter.ConversionException;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;

import java.io.File;

/**
 * Convenience wrapper around {@link IMediaService#postUserMedia(String, long, TypedInput)}}
 * and {@link IMediaService#updateNamedUserMedia(String, long, String, TypedInput)}
 * used to post media that becomes available only to the posting user.
 * Once this job is completed (with success or failure) it posts {@link EventMediaUploaded} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobUploadUserMedia extends BaseJobUploadNamedMedia {

    /**
     * Create a new private media upload job.
     * @param params custom job parameters
     * @param mime file content type as a MIME string
     * @param file path to the file that will be uploaded
     */
    public JobUploadUserMedia(Params params, String mime, String file) {
        super(params, mime, file);
    }

    /**
     * Create a new private media upload job.
     * @param params custom job parameters
     * @param trailingMediaUri known media Uri for update or create
     * @param mime file content type as a MIME string
     * @param file path to the file that will be uploaded
     */
    public JobUploadUserMedia(Params params, String trailingMediaUri, String mime, String file) {
        super(params, mime, file);
        this.trailingMediaUri = trailingMediaUri;
    }

    /**
     * Create a new private media upload job.
     * @param mime file content type as a MIME string
     * @param file path to the file that will be uploaded
     */
    public JobUploadUserMedia(String mime, String file) {
        super(mime, file);
    }

    /**
     * Create a new private media upload job.
     * @param trailingMediaUri known media Uri for update or create
     * @param mime file content type as a MIME string
     * @param file path to the file that will be uploaded
     */
    public JobUploadUserMedia(String trailingMediaUri, String mime, String file) {
        super(mime, file);
        this.trailingMediaUri = trailingMediaUri;
    }

    @Override
    protected MediaInfo callService(String mime, String file) throws ConversionException {
        IUserManager userManager = ServiceSupport.Instance.getServiceManager(IUserManager.class);
        IMediaManager mediaManager = ServiceSupport.Instance.getServiceManager(IMediaManager.class);
        long userId = userManager.getUser().getId();
        TypedFile typedFile = new TypedFile(mime, new File(file));
        MediaInfo response;
        if (trailingMediaUri == null) {
            response = mediaManager.getService().postUserMedia(getRetryId(), userId, typedFile);
        }
        else {
            response = mediaManager.getService().updateNamedUserMedia(getRetryId(), userId, trailingMediaUri, typedFile);
        }
        return response;
    }

}
