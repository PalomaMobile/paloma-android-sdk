package com.palomamobile.android.sdk.user;

import com.palomamobile.android.sdk.auth.IUserCredential;

import java.io.Serializable;

/**
 * Class {@code User} represents the information available about the currently registered local user.<br/>
 * To register a user call {@link IUserManager#requestRegisterUser(IUserCredential)}.
 * <br/>
 *
 */
public class User implements Serializable {

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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id != other.getId())
            return false;
        return true;
    }

    public int hashCode(){
        return  (int)id * username.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
