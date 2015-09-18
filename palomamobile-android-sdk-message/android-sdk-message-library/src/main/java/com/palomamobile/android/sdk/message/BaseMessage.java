package com.palomamobile.android.sdk.message;

import java.io.Serializable;
import java.util.List;

/**
 * Abstract class {@code Message} represents the information available about a message in the context of the Message SDK.
 * <br/>
 *
 */
public abstract class BaseMessage implements Serializable {

    private long id;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("id=").append(id);
        sb.append(", contentList=").append(contentList);
        sb.append('}');
        return sb.toString();
    }
}
