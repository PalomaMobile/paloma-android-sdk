package com.palomamobile.android.sdk.message;

import java.io.Serializable;
import java.util.List;

/**
 * Abstract class {@code BaseMessage} represents common information available about a message in the context of the Message SDK.
 * <br/>
 *
 */
public abstract class BaseMessage implements Serializable {

    private long id;
    private long messageThreadId;
    private String type;

    @SuppressWarnings("unused")
    private long timeSent;

    private String replyTo;

    @SuppressWarnings("unused")
    private String replyToken;

    @SuppressWarnings("unused")
    private String replyChain;

    private List<MessageContentDetail> contentList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<MessageContentDetail> getContentList() {
        return contentList;
    }

    public void setContentList(List<MessageContentDetail> contentList) {
        this.contentList = contentList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getMessageThreadId() {
        return messageThreadId;
    }

    public void setMessageThreadId(long messageThreadId) {
        this.messageThreadId = messageThreadId;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getReplyChain() {
        return replyChain;
    }

    public String getReplyToken() {
        return replyToken;
    }

    public long getTimeSent() {
        return timeSent;
    }

    @Override
    public String toString() {
        return "BaseMessage{" +
                "id=" + id +
                ", messageThreadId=" + messageThreadId +
                ", timeSent=" + timeSent +
                ", type='" + type + '\'' +
                ", replyTo='" + replyTo + '\'' +
                ", replyToken='" + replyToken + '\'' +
                ", replyChain='" + replyChain + '\'' +
                ", contentList=" + contentList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseMessage that = (BaseMessage) o;

        if (id != that.id) return false;
        if (messageThreadId != that.messageThreadId) return false;
        if (timeSent != that.timeSent) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (replyTo != null ? !replyTo.equals(that.replyTo) : that.replyTo != null) return false;
        if (replyToken != null ? !replyToken.equals(that.replyToken) : that.replyToken != null) return false;
        if (replyChain != null ? !replyChain.equals(that.replyChain) : that.replyChain != null) return false;
        return !(contentList != null ? !contentList.equals(that.contentList) : that.contentList != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (messageThreadId ^ (messageThreadId >>> 32));
        result = 31 * result + (int) (timeSent ^ (timeSent >>> 32));
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (replyTo != null ? replyTo.hashCode() : 0);
        result = 31 * result + (replyToken != null ? replyToken.hashCode() : 0);
        result = 31 * result + (replyChain != null ? replyChain.hashCode() : 0);
        result = 31 * result + (contentList != null ? contentList.hashCode() : 0);
        return result;
    }
}
