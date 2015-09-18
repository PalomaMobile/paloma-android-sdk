package com.palomamobile.android.sdk.message;

import java.io.Serializable;

/**
 * Class {@code Sender} represents the information available about the sender of a {@link MessageReceived}.
 * <br/>
 *
 */
public class Sender implements Serializable {

    private long id;
    private String username;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

        Sender sender = (Sender) o;

        if (id != sender.id) return false;
        return !(username != null ? !username.equals(sender.username) : sender.username != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Sender{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
