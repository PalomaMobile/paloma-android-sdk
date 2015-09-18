package com.palomamobile.android.sdk.notification;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Class {@code Notification} represents the detailed content information about a notification received
 * through any notification channel (eg: GCM, SMS etc.) The {@code type} and {@code recipientId} fields are
 * required and will be populated with valid values while the other fields may be {@code null}.
 * <br/>
 *
 */
public class Notification implements Serializable {

    /**
     * String identifying the notification type using $source.$notificationType eg: {@code MS.RM}
     * (meaning Messaging Service Message Received)
     */
    @NonNull private String type;

    @NonNull private Long recipientId;
    @Nullable private String content;
    @Nullable private String debug;
    @Nullable private String senderClient;
    @Nullable private String url;
    @Nullable private Long senderId;
    @Nullable private Long timestamp;

    public Notification() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getSenderClient() {
        return senderClient;
    }

    public void setSenderClient(String senderClient) {
        this.senderClient = senderClient;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @see Notification#type
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Notification{");
        sb.append("content='").append(content).append('\'');
        sb.append(", debug='").append(debug).append('\'');
        sb.append(", senderClient='").append(senderClient).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", recipientId=").append(recipientId);
        sb.append(", senderId=").append(senderId);
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;

        if (!type.equals(that.type)) return false;
        if (!recipientId.equals(that.recipientId)) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (debug != null ? !debug.equals(that.debug) : that.debug != null) return false;
        if (senderClient != null ? !senderClient.equals(that.senderClient) : that.senderClient != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (senderId != null ? !senderId.equals(that.senderId) : that.senderId != null) return false;
        return !(timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null);

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + recipientId.hashCode();
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (debug != null ? debug.hashCode() : 0);
        result = 31 * result + (senderClient != null ? senderClient.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (senderId != null ? senderId.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
}
