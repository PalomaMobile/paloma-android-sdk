package com.palomamobile.android.sdk.verification.email;

import java.io.Serializable;

/**
 * Info required to preform an update of the email address of an existing user.
 */
public class UserVerifiedEmailUpdate implements Serializable {
    private String code;
    private String emailAddress;

    public UserVerifiedEmailUpdate(String emailAddress, String code) {
        this.emailAddress = emailAddress;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserVerifiedEmailUpdate that = (UserVerifiedEmailUpdate) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        return !(emailAddress != null ? !emailAddress.equals(that.emailAddress) : that.emailAddress != null);

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserVerifiedEmailUpdate{" +
                "emailAddress='" + emailAddress + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
