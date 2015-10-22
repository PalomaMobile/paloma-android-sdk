package com.palomamobile.android.sdk.media;

import android.net.Uri;
import android.support.annotation.NonNull;
import com.palomamobile.android.sdk.core.CustomHeader;
import com.palomamobile.android.sdk.core.IServiceSupport;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.UUID;

class MediaManager implements IMediaManager {

    private IMediaService mediaService;
    private OkHttpClient nonRedirectingHttpClient;

    public MediaManager(IServiceSupport serviceSupport) {
        this.mediaService = serviceSupport.getRestAdapter().create(IMediaService.class);
        serviceSupport.registerServiceManager(IMediaManager.class, this);
        serviceSupport.getInternalEventBus().register(this);
    }

    private void initNonRedirectingHttpClient() {
        if (nonRedirectingHttpClient == null) {
            OkHttpClient okHttpClient = ServiceSupport.Instance.getOkHttpClient();
            nonRedirectingHttpClient = okHttpClient.clone();
            nonRedirectingHttpClient.setFollowRedirects(false);
        }
    }

    @Override
    public JobUploadMediaPublic createJobMediaUploadPublic(String mime, String filePath) {
        return new JobUploadMediaPublic(mime, filePath);
    }

    @Override
    public JobUploadMediaPrivate createJobMediaUploadPrivate(String mime, String filePath) {
        return new JobUploadMediaPrivate(mime, filePath);
    }

    @Override
    public Uri requestExpiringPublicUrl(Uri mediaUri) throws IOException {
        initNonRedirectingHttpClient();
        Uri resolved = mediaUri;
        Uri endpoint = ServiceSupport.Instance.getEndpoint();
        if (endpoint.getHost().equals(mediaUri.getHost())) {
            Call call = nonRedirectingHttpClient.newCall(new Request.Builder()
                    .get()
                    .addHeader(CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION, BuildConfig.TARGET_SERVICE_VERSION)
                    .addHeader(CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION, BuildConfig.VERSION_NAME)
                    .addHeader(CustomHeader.HEADER_NAME_PALOMA_REQUEST, UUID.randomUUID().toString())
                    .url(mediaUri.toString())
                    .build());
            Response response = call.execute();
            String location = response.header("Location");
            if (location != null) {
                resolved = Uri.parse(location);
            }
        }
        return resolved;
    }

    @NonNull
    @Override
    public IMediaService getService() {
        return mediaService;
    }
}
