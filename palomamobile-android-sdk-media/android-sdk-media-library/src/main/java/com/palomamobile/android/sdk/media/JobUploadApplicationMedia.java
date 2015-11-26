package com.palomamobile.android.sdk.media;

import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.util.Utilities;
import com.path.android.jobqueue.Params;
import retrofit.converter.ConversionException;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;

import java.io.File;

/**
 * Convenience wrapper around {@link IMediaService#postApplicationMedia(String, String, TypedInput)} and  {@link IMediaService#updateNamedApplicationMedia(String, String, String, TypedInput)}
 * used to post media that becomes available only to the posting user.
 * Once this job is completed (with success or failure) it posts {@link EventMediaUploaded} on the
 * {@link com.palomamobile.android.sdk.core.IEventBus} (as returned by {@link ServiceSupport#getEventBus()}).
 * </br>
 */
public class JobUploadApplicationMedia extends BaseJobUploadNamedMedia {

    /**
     * Create a new application media upload job.
     *
     * @param params custom job parameters
     * @param mime file content type as a MIME string
     * @param file path to the file that will be uploaded
     */
    public JobUploadApplicationMedia(Params params, String mime, String file) {
        super(params, mime, file);
    }

    /**
     * Create a new application media upload job to update existing media or create
     * new media with a known trailing media Uri (eg. file name).
     *
     * @param params custom job parameters
     * @param trailingMediaUri known media Uri for update or create
     * @param mime file content type as a MIME string
     * @param file path to the file that will be uploaded
     */
    public JobUploadApplicationMedia(Params params, String trailingMediaUri, String mime, String file) {
        super(params, mime, file);
        this.trailingMediaUri = trailingMediaUri;
    }

    /**
     * Create a new application media upload job.
     * @param mime file content type as a MIME string
     * @param file path to the file that will be uploaded
     */
    public JobUploadApplicationMedia(String mime, String file) {
        super(mime, file);
    }

    /**
     * Create a new application media upload job to update existing media or create
     * new media with a known trailing media Uri (eg. file name).
     * @param trailingMediaUri known media Uri for update or create
     * @param mime file content type as a MIME string
     * @param file path to the file that will be uploaded
     */
    public JobUploadApplicationMedia(String trailingMediaUri, String mime, String file) {
        super(mime, file);
        this.trailingMediaUri = trailingMediaUri;
    }

    @Override
    protected MediaInfo callService(String mime, String file) throws ConversionException {
        IMediaManager mediaManager = ServiceSupport.Instance.getServiceManager(IMediaManager.class);
        TypedFile typedFile = new TypedFile(mime, new File(file));
        String applicationName = Utilities.getAppNameFromMetadata(ServiceSupport.Instance.getContext());
        MediaInfo response;
        IMediaService mediaService = mediaManager.getService();
        if (trailingMediaUri == null) {
            response = mediaService.postApplicationMedia(getRetryId(), applicationName, typedFile);
        }
        else {
            response = mediaService.updateNamedApplicationMedia(getRetryId(), applicationName, trailingMediaUri, typedFile);
        }
        return response;
    }

}
