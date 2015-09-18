package com.palomamobile.android.sdk.media;

import com.google.gson.annotations.SerializedName;
import retrofit.mime.TypedInput;

import java.io.Serializable;

/**
 * Class {@code MediaInfo} represents information available about uploaded media in the context of the Media SDK.<br/>
 * <br/>
 *
 */
public class MediaInfo implements Serializable {
    protected long id;
    protected long ownerId;
    protected long contentLength;
    protected String contentType;
    protected String url;
    protected boolean secured;
    @SerializedName("expiringAuthorizedUrl")
    protected String expiringPublicUrl;

    public MediaInfo() {
    }

    public long getId() {
        return id;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public long getContentLength() {
        return contentLength;
    }

    /**
     * @return media content type as MIME type.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @return  privately (if {@link #isSecured()} returns {@code true}) or publicly (if {@link #isSecured()} returns {@code false}) accessible media content url.
     */
    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return short lived publicly accessible url if {@link #isSecured()} returns {@code true}, {@code null} otherwise
     * (since the url returned by {@link #getUrl()} is already public and doesn't expire
     */
    public String getExpiringPublicUrl() {
        return expiringPublicUrl;
    }

    /**
     * @return {@code true} if media was posted via {@link IMediaService#postMediaPrivate(long, TypedInput)} so that it
     * is only accessible to the posting user (owner), {@code false}) otherwise
     */
    public boolean isSecured() {
        return secured;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MediaInfo{");
        sb.append("id=").append(id);
        sb.append(", ownerId=").append(ownerId);
        sb.append(", url=").append(url);
        sb.append(", secured=").append(secured);
        sb.append(", expiringPublicUrl=").append(expiringPublicUrl);
        sb.append(", contentLength=").append(contentLength);
        sb.append(", contentType='").append(contentType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
