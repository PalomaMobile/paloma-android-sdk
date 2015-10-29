package com.palomamobile.android.sdk.media;

import com.path.android.jobqueue.Params;

/**
 * Created by Karel Herink
 */
public abstract class BaseJobUploadNamedMedia extends BaseJobUploadMedia {

    protected String trailingMediaUri;

    public BaseJobUploadNamedMedia(Params params, String mime, String file) {
        super(params, mime, file);
    }

    public BaseJobUploadNamedMedia(String mime, String file) {
        super(mime, file);
    }

    public String getTrailingMediaUri() {
        return trailingMediaUri;
    }
}
