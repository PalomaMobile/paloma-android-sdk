package com.palomamobile.android.sdk.verification;

/**
 * Contains parameters for update of a verification email.
 */
public class VerificationEmailUpdate {

    private String applicationName;

    /**
     * Code received in the verification email.
     */
    private String code;

    public VerificationEmailUpdate(String applicationName, String code) {
        this.applicationName = applicationName;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VerificationEmailUpdate that = (VerificationEmailUpdate) o;

        if (applicationName != null ? !applicationName.equals(that.applicationName) : that.applicationName != null)
            return false;
        return !(code != null ? !code.equals(that.code) : that.code != null);

    }

    @Override
    public int hashCode() {
        int result = applicationName != null ? applicationName.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VerificationEmailUpdate{" +
                "applicationName='" + applicationName + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
