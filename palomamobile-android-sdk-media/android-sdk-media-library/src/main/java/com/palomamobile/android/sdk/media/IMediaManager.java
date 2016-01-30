package com.palomamobile.android.sdk.media;

import android.net.Uri;
import com.palomamobile.android.sdk.core.IServiceManager;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import retrofit.mime.TypedInput;

import java.io.IOException;

/**
 * This interface provides access
 * to the underlying {@link IMediaService}. App developers can either use {@link BaseRetryPolicyAwareJob}
 * implementations in this package or create custom jobs that invoke
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
     * User may wish to share private media that was created using {@link JobUploadUserMedia} or directly via a call
     * to {@link IMediaService#postUserMedia(String, long, TypedInput)}. Access to such media requires authentication.
     * This method returns a short-lived publicly accessible Url that can be shared.
     *
     * @param mediaUrl url of a media resource
     * @return short-lived publicly accessible Url if media resource located at {@code mediaUrl} is a private, unmodified {@code mediaUrl} otherwise
     * @throws IOException
     */
    Uri requestExpiringPublicUrl(Uri mediaUrl) throws IOException;

    /**
     * Returns the chunking strategy that is applied by default to all jobs that extend {@link BaseJobUploadMedia} as well as
     * {@link JobReliableDownload}.
     * By default returns the same instance of {@link com.palomamobile.android.sdk.media.IChunkingStrategy.SimpleChunkingStrategy}.
     * Custom strategy can be set via {@link #setChunkingStrategy(IChunkingStrategy)}.
     * @return current chunking strategy
     */
    IChunkingStrategy getChunkingStrategy();

    /**
     * Set custom chunking strategy.
     * @see #getChunkingStrategy()
     * @param chunkingStrategy new strategy
     */
    void setChunkingStrategy(IChunkingStrategy chunkingStrategy);
}
