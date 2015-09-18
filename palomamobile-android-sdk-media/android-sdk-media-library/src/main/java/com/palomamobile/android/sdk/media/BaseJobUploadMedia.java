package com.palomamobile.android.sdk.media;

import android.util.Log;
import com.google.gson.Gson;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.qos.BaseRetryPolicyAwareJob;
import com.path.android.jobqueue.Params;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.GsonConverter;

/**
 * Abstract class provides base functionality required to upload media content.
 *
 */
public abstract class BaseJobUploadMedia extends BaseRetryPolicyAwareJob<MediaInfo> {

    private static final String TAG = BaseJobUploadMedia.class.getSimpleName();

    private static final String LOCATION_HEADER_NAME = "Location";

    private String mime;
    private String file;

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
        Log.d(TAG, "posting " + file + " '" + mime + "'");
        MediaInfo result = callService(mime, file);
        if (postEvent) {
            ServiceSupport.Instance.getEventBus().post(new EventMediaUploaded(this, result));
        }
        return result;
    }

    @Override
    protected void postFailure(Throwable throwable) {
        ServiceSupport.Instance.getEventBus().post(new EventMediaUploaded(this, throwable));
    }

    protected abstract MediaInfo callService(String mime, String file) throws Exception;

    protected MediaInfo getMediaInfo(Response response) throws ConversionException {
        GsonConverter converter = new GsonConverter(new Gson());
        MediaInfo mediaInfo = (MediaInfo) converter.fromBody(response.getBody(), MediaInfo.class);
        mediaInfo.setUrl(getLocationFromHeaders(response));
        return mediaInfo;
    }

    private String getLocationFromHeaders(Response response) {
        for (Header header : response.getHeaders()) {
            if (LOCATION_HEADER_NAME.toLowerCase().equals(header.getName().toLowerCase())) {
                return header.getValue();
            }
        }
        return null;
    }

}
