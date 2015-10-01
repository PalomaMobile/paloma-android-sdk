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
    private long timeSent;
    private String type;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("id=").append(id);
        sb.append(", timeSent=").append(timeSent);
        sb.append(", type=\'").append(type).append('\'');
        sb.append(", contentList=").append(contentList);
        sb.append('}');
        return sb.toString();
    }

    public long getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(long timeSent) {
        this.timeSent = timeSent;
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
        return !(contentList != null ? !contentList.equals(that.contentList) : that.contentList != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (messageThreadId ^ (messageThreadId >>> 32));
        result = 31 * result + (int) (timeSent ^ (timeSent >>> 32));
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (contentList != null ? contentList.hashCode() : 0);
        return result;
    }
}
