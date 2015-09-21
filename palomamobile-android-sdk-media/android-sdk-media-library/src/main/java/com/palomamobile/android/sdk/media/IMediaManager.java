package com.palomamobile.android.sdk.media;

import android.net.Uri;
import com.palomamobile.android.sdk.core.IServiceManager;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import retrofit.mime.TypedInput;

import java.io.IOException;

/**
 * This interface provides convenient job creation methods that provide easy access
 * to the underlying {@link IMediaService} functionality. App developers can either use {@link BaseRetryPolicyAwareJob}
 * job instances returned by the {@code createJob...()} methods, or create custom jobs that invoke
 * methods of the {@link IMediaService} returned by {@link IServiceManager#getService()}
 *
 * <br/>
 * To get a concrete implementation of this interface call
 * {@code ServiceSupport.Instance.getServiceManager(IMediaManager.class)}
 * <br/>
 *
 * <br/>
 *
 */
public interface IMediaManager extends IServiceManager<IMediaService> {

    /**
     * Create a new {@link JobUploadMediaPublic}.
     *
     * @param mime mime type describing the media content eg: {@code "text/html; charset=UTF-8"}, charset is optional
     * @param filePath local path to the file with media content
     * @return new job instance
     */
    JobUploadMediaPublic createJobMediaUploadPublic(String mime, String filePath);

    /**
     * Create a new {@link JobUploadMediaPrivate}.
     *
     * @param mime mime type describing the media content eg: {@code "text/html; charset=UTF-8"}, charset is optional
     * @param filePath local path to the file with media content
     * @return new job instance
     */
    JobUploadMediaPrivate createJobMediaUploadPrivate(String mime, String filePath);

    /**
     * User may wish to share private media that was created using {@link JobUploadMediaPrivate} or directly via a call
     * to {@link IMediaService#postMediaPrivate(String, long, TypedInput)}. Access to such media requires authentication.
     * This method returns a short-lived publicly accessible Url that can be shared.
     *
     * @param mediaUrl url of a media resource
     * @return short-lived publicly accessible Url if media resource located at {@code mediaUrl} is a private, unmodified {@code mediaUrl} otherwise
     * @throws IOException
     */
    Uri requestExpiringPublicUrl(Uri mediaUrl) throws IOException;

}
