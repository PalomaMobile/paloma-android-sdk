package com.palomamobile.android.sdk.user;

import java.io.Serializable;

/**
 * Info required to perform a password update for a user.
 */
public class PasswordUpdate implements Serializable {

    private String password;
    private String code;

    /**
     * Create a new instance.
     * @param code code received via a verified channel
     * @param password the new password
     */
    public PasswordUpdate(String password, String code) {
        this.password = password;
        this.code = code;
    }

    /**
     * @return code received via a verified channel
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the new password
     */
    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PasswordUpdate that = (PasswordUpdate) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        return !(password != null ? !password.equals(that.password) : that.password != null);

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PasswordUpdate{" +
                "password='" + password + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
