package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.message.UserDetails;

/**
 *
 */
public class MessageThread extends BaseMessageThread {

    private long id;
    private int memberCount;
    private int messageCount;
    private UserDetails owner;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public UserDetails getOwner() {
        return owner;
    }

    public void setOwner(UserDetails owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MessageThread that = (MessageThread) o;

        if (id != that.id) return false;
        if (memberCount != that.memberCount) return false;
        if (messageCount != that.messageCount) return false;
        return !(owner != null ? !owner.equals(that.owner) : that.owner != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + memberCount;
        result = 31 * result + messageCount;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MessageThread{" +
                "id=" + id +
                ", memberCount=" + memberCount +
                ", messageCount=" + messageCount +
                ", owner=" + owner +
                "} " + super.toString();
    }
}
