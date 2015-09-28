package com.palomamobile.android.sdk.message;

import java.io.Serializable;

/**
 * Class {@code UserDetail} represents the information available about the user in the context of Messaging.
 * <br/>
 *
 */
public class UserDetails implements Serializable {

    private long userId;
    private String username;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDetails sender = (UserDetails) o;

        if (userId != sender.userId) return false;
        return !(username != null ? !username.equals(sender.username) : sender.username != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserDetails{");
        sb.append("userId=").append(userId);
        sb.append(", username='").append(username).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
