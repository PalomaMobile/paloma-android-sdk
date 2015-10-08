package com.palomamobile.android.sdk.user;

import com.palomamobile.android.sdk.auth.IUserCredential;

/**
 * Class {@code User} represents the information available about the currently registered local user.<br/>
 * To register a user call {@link IUserManager#createJobRegisterUser(IUserCredential)}
 * <br/>
 *
 */
public class User extends UserUpdate {

    private long id;
    private String username;
    private String mobileNumber;
    private String emailAddress;


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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", displayName='" + displayName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", custom=" + custom +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        if (mobileNumber != null ? !mobileNumber.equals(user.mobileNumber) : user.mobileNumber != null) return false;
        if (dateOfBirth != null ? !dateOfBirth.equals(user.dateOfBirth) : user.dateOfBirth != null) return false;
        if (displayName != null ? !displayName.equals(user.displayName) : user.displayName != null) return false;
        if (emailAddress != null ? !emailAddress.equals(user.emailAddress) : user.emailAddress != null) return false;
        return !(custom != null ? !custom.equals(user.custom) : user.custom != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (mobileNumber != null ? mobileNumber.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        result = 31 * result + (custom != null ? custom.hashCode() : 0);
        return result;
    }
}
