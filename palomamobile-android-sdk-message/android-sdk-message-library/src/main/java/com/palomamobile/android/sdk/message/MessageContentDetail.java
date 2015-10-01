package com.palomamobile.android.sdk.message;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Map;

/**
 * Class {@code MessageContentDetail} represents the detailed content information available in a {@link BaseMessage} in the context of the Message SDK.
 * <br/>
 *
 */
public class MessageContentDetail implements Serializable {

    private String url;
    private String contentType;
    private String payload;
    private Map<String, String> custom;

    public MessageContentDetail() {
    }

    public MessageContentDetail(@NonNull String contentType, @NonNull String url) {
        this(contentType, url, null);
    }

    public MessageContentDetail(@NonNull String contentType, @NonNull String url, @Nullable String payload) {
        this.contentType = contentType;
        this.url = url;
        this.payload = payload;
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

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Map<String, String> getCustom() {
        return custom;
    }

    public void setCustom(Map<String, String> custom) {
        this.custom = custom;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MessageContentDetail{");
        sb.append("contentType='").append(contentType).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", content='").append(payload).append('\'');
        sb.append(", custom=").append(custom);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageContentDetail detail = (MessageContentDetail) o;

        if (url != null ? !url.equals(detail.url) : detail.url != null) return false;
        if (contentType != null ? !contentType.equals(detail.contentType) : detail.contentType != null) return false;
        if (payload != null ? !payload.equals(detail.payload) : detail.payload != null) return false;
        return !(custom != null ? !custom.equals(detail.custom) : detail.custom != null);

    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (custom != null ? custom.hashCode() : 0);
        return result;
    }
}
