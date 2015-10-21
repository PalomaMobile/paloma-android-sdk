package com.palomamobile.android.sdk.verification.email;

import java.io.Serializable;

/**
 * Contains parameters for update of a verification email.
 */
public class VerificationEmailUpdate implements Serializable {

    /**
     * Code received in the verification email.
     */
    private String code;

    public VerificationEmailUpdate(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VerificationEmailUpdate that = (VerificationEmailUpdate) o;

        return !(code != null ? !code.equals(that.code) : that.code != null);

    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "VerificationEmailUpdate{" +
                "code='" + code + '\'' +
                '}';
    }
}
