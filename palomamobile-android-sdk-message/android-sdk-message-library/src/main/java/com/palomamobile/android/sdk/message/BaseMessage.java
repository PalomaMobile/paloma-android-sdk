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
        sb.append(", type=\'").append(type).append('\'');
        sb.append(", contentList=").append(contentList);
        sb.append('}');
        return sb.toString();
    }
}
