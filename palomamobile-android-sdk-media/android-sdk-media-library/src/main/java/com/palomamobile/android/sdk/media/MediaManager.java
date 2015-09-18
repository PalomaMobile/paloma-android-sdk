package com.palomamobile.android.sdk.media;

import android.net.Uri;
import android.support.annotation.NonNull;
import com.palomamobile.android.sdk.auth.IAuthManager;
import com.palomamobile.android.sdk.core.CustomHeader;
import com.palomamobile.android.sdk.core.EventServiceManagerRegistered;
import com.palomamobile.android.sdk.core.IServiceSupport;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 *
 */
class MediaManager implements IMediaManager {

    private IMediaService mediaService;
    private OkHttpClient nonRedirectingHttpClient;

    public MediaManager(IServiceSupport serviceSupport) {
        this.mediaService = serviceSupport.getRestAdapter().create(IMediaService.class);
        serviceSupport.registerServiceManager(IMediaManager.class, this);
        serviceSupport.getEventBus().register(this);
    }

    public void onEvent(EventServiceManagerRegistered event) {
        if (event.getIntrface() == IAuthManager.class) {
            //auth manager is ready, this means the okhttpclient is configured to handle authentication, let's take a copy
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
        Uri resolved = mediaUri;

        //eg: http://ec2-46-137-242-200.ap-southeast-1.compute.amazonaws.com
        Uri endpoint = ServiceSupport.Instance.getEndpoint();
        if (endpoint.getHost().equals(mediaUri.getHost())) {
            Call call = nonRedirectingHttpClient.newCall(new Request.Builder()
                    .get()
                    .addHeader(CustomHeader.HEADER_PALOMA_TARGET_SERVICE_VERSION, BuildConfig.TARGET_SERVICE_VERSION)
                    .addHeader(CustomHeader.HEADER_PALOMA_SDK_MODULE_VERSION, BuildConfig.VERSION_NAME)
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
