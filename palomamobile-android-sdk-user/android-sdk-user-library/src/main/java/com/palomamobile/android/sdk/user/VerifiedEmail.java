package com.palomamobile.android.sdk.user;

import android.support.annotation.NonNull;

/**
 * Represents an email address with it's one-shot verification code as provided by the verification service.
 * Created by Karel Herink
 */
public class VerifiedEmail {
    @NonNull
    private String verifiedEmailAddress;
    @NonNull
    private String verificationCode;

    public VerifiedEmail(@NonNull String verifiedEmailAddress, @NonNull String verificationCode) {
        this.verifiedEmailAddress = verifiedEmailAddress;
        this.verificationCode = verificationCode;
    }

    public String getVerifiedEmailAddress() {
        return verifiedEmailAddress;
    }

    public void setVerifiedEmailAddress(@NonNull String verifiedEmailAddress) {
        this.verifiedEmailAddress = verifiedEmailAddress;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(@NonNull String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VerifiedEmail that = (VerifiedEmail) o;

        if (!verifiedEmailAddress.equals(that.verifiedEmailAddress)) return false;
        return verificationCode.equals(that.verificationCode);

    }

    @Override
    public int hashCode() {
        int result = verifiedEmailAddress.hashCode();
        result = 31 * result + verificationCode.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "VerifiedEmail{" +
                "verifiedEmailAddress='" + verifiedEmailAddress + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }
}
