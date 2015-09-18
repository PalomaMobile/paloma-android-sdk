package com.palomamobile.android.sdk.friend;

import java.io.Serializable;

/**
 * Class {@code Friend} represents the information available about a friend in the context of the Friend SDK.<br/>
 * To retrieve the local user's list of friends use {@link JobGetFriends}
 * <br/>
 *
 */
public class Friend implements Serializable {

    private long userId;
    private String username;

    public Friend() {
    }

    /**
     * Create a new friend.
     * @param userId friends unique user Id
     * @param username friends unique username
     */
    public Friend(long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    /**
     * @return friends unique user Id.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * @return friends unique username.
     */
    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Friend friend = (Friend) o;

        if (userId != friend.userId) return false;
        return !(username != null ? !username.equals(friend.username) : friend.username != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Friend{");
        sb.append("userId=").append(userId);
        sb.append(", username='").append(username).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
