package com.palomamobile.android.sdk.auth;

import android.support.annotation.Nullable;

import java.util.Map;

/**
 * Created by Karel Herink
 */
public abstract class BaseUserCredential implements IUserCredential {

    protected Map<String, String> credential;
    @Nullable protected String username;
    @Nullable private Map<String, String> custom;
    @Nullable private String dateOfBirth;
    @Nullable private String displayName;


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String userName) {
        this.username = userName;
    }

    @Nullable
    public Map<String, String> getCustom() {
        return custom;
    }

    public void setCustom(@Nullable Map<String, String> custom) {
        this.custom = custom;
    }

    @Nullable
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth Must be of the form  yyyy-MM-dd
     */
    public void setDateOfBirth(@Nullable String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Nullable
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseUserCredential that = (BaseUserCredential) o;

        if (credential != null ? !credential.equals(that.credential) : that.credential != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (custom != null ? !custom.equals(that.custom) : that.custom != null) return false;
        if (dateOfBirth != null ? !dateOfBirth.equals(that.dateOfBirth) : that.dateOfBirth != null) return false;
        return !(displayName != null ? !displayName.equals(that.displayName) : that.displayName != null);

    }

    @Override
    public int hashCode() {
        int result = credential != null ? credential.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (custom != null ? custom.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BaseUserCredential{" +
                "credential=" + credential +
                ", username='" + username + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", displayName='" + displayName + '\'' +
                ", custom=" + custom +
                '}';
    }
}
