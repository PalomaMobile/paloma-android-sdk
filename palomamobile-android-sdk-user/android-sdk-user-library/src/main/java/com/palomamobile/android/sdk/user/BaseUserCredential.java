package com.palomamobile.android.sdk.user;

import android.support.annotation.Nullable;
import com.palomamobile.android.sdk.auth.IUserCredential;

import java.util.Map;

/**
 * Created by Karel Herink
 */
public abstract class BaseUserCredential extends UserUpdate implements IUserCredential {

    protected Map<String, String> credential;
    @Nullable protected String username;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String userName) {
        this.username = userName;
    }

    @Override
    public String toString() {
        return "BaseUserCredential{" +
                "credential=" + credential +
                ", username='" + username + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseUserCredential that = (BaseUserCredential) o;

        if (credential != null ? !credential.equals(that.credential) : that.credential != null) return false;
        return !(username != null ? !username.equals(that.username) : that.username != null);

    }

    @Override
    public int hashCode() {
        int result = credential != null ? credential.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
