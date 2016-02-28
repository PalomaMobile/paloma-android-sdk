package com.palomamobile.android.sdk.media;

import android.net.Uri;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Class {@code MediaInfo} represents information available about uploaded media in the context of the Media SDK.<br/>
 * <br/>
 *
 */
public class MediaInfo implements Serializable {
    private long id;
    private long ownerId;
    private long contentLength;
    private String contentType;
    private String url;
    private boolean secured;
    @SerializedName("expiringAuthorizedUrl")
    private String expiringPublicUrl;
    private String editUrl;
    private boolean complete;
    private String cachingControl;
    private String transferId;
    private String etag;
    private ChunkingInfo chunking;

    public static class ChunkingInfo implements Serializable {
        private List<ContentRange> contentRanges;
        private String transferId;
        private String contentType;
        private long contentLength;

        public long getContentLength() {
            return contentLength;
        }

        public List<ContentRange> getContentRanges() {
            return contentRanges;
        }

        public String getContentType() {
            return contentType;
        }

        public String getTransferId() {
            return transferId;
        }

        @Override
        public String toString() {
            return "ChunkingInfo{" +
                    "transferId='" + transferId + '\'' +
                    ", contentType='" + contentType + '\'' +
                    ", contentLength=" + contentLength +
                    ", contentRanges=" + contentRanges +
                    '}';
        }
    }

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
     * @return {@code true} if media is only accessible to the posting user (owner), {@code false}) otherwise
     */
    public boolean isSecured() {
        return secured;
    }

    public String getTrailingMediaUri() {
        return Uri.parse(url).getLastPathSegment();
    }

    public String getCachingControl() {
        return cachingControl;
    }

    public boolean isComplete() {
        return complete;
    }

    public String getEditUrl() {
        return editUrl;
    }

    public String getEtag() {
        return etag;
    }

    public String getTransferId() {
        return transferId;
    }

}
