package com.palomamobile.android.sdk.media;

import android.util.Base64;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.util.Utilities;
import com.path.android.jobqueue.Params;
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

    @Override
    protected MediaInfo callService(String mime, String file) throws Exception {
        IChunkingStrategy chunkingStrategy = getChunkingStrategy();
        logger.debug(chunkingStrategy.toString());

        IMediaService mediaService = ServiceSupport.Instance.getServiceManager(IMediaManager.class).getService();
        MediaInfo response;
        if (chunkingStrategy.isApplyChunking()) {
            logger.debug("uploading in chunks");
            response = uploadInChunks(mime, file);
        }
        else {
            logger.debug("uploading simply");
            if (trailingMediaUri == null) {
                TypedFile typedFile = new TypedFile(mime, new File(file));
                response = mediaService.postMedia(getRetryId(), typedFile);
            }
            else {
                TypedFile typedFile = new TypedFile(mime, new File(file));
                response = mediaService.postMedia(getRetryId(), trailingMediaUri, typedFile);
            }
        }
        return response;
    }

    private MediaInfo uploadInChunks(String mime, String file) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        int offset = 0;
        int bufferSize = getChunkingStrategy().getChunkSize();
        byte[] bytes = new byte[bufferSize];
        String transferId = String.valueOf(getTransferId());
        IMediaService mediaService = ServiceSupport.Instance.getServiceManager(IMediaManager.class).getService();

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

            if (trailingMediaUri == null) {
                mediaInfo = mediaService.postMediaChunk(getRetryId(), transferId, buildContentRangeHeaderValue(offset, readCount, randomAccessFile.length()), chunk, contentMd5);
                trailingMediaUri = mediaInfo.getTrailingMediaUri();
            }
            else {
                mediaInfo = mediaService.putMediaChunk(getRetryId(), transferId, buildContentRangeHeaderValue(offset, readCount, randomAccessFile.length()), trailingMediaUri, chunk, contentMd5);
            }
            offset += readCount;
        } while (offset < randomAccessFile.length());
        return mediaInfo;
    }

    private static String buildContentRangeHeaderValue(long offset, int byteCount, long totalContentLength) {
        return "bytes " + offset + "-" + (offset + byteCount -1) +
                (totalContentLength == -1 ? "/*" : "/" + totalContentLength);
    }

}
