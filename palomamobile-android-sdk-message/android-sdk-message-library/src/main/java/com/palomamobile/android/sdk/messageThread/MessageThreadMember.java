package com.palomamobile.android.sdk.messageThread;

import com.palomamobile.android.sdk.message.UserDetails;

import java.io.Serializable;

/**
 *
 */
public class MessageThreadMember implements Serializable {
    private long threadId;
    private UserDetails user;

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public UserDetails getUser() {
        return user;
    }

    public void setUser(UserDetails user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageThreadMember that = (MessageThreadMember) o;

        if (threadId != that.threadId) return false;
        return !(user != null ? !user.equals(that.user) : that.user != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (threadId ^ (threadId >>> 32));
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
