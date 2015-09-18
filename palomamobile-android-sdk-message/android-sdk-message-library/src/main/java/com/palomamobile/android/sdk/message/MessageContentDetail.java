package com.palomamobile.android.sdk.message;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Class {@code MessageContentDetail} represents the detailed content information available in a {@link BaseMessage} in the context of the Message SDK.
 * <br/>
 *
 */
public class MessageContentDetail implements Serializable {

    private String url;
    private String contentType;
    private String content;

    public MessageContentDetail() {
    }

    public MessageContentDetail(@NonNull String contentType, @NonNull String url) {
        this(contentType, url, null);
    }

    public MessageContentDetail(@NonNull String contentType, @NonNull String url, @Nullable String content) {
        this.contentType = contentType;
        this.url = url;
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MessageContentDetail{");
        sb.append("contentType='").append(contentType).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
