package com.palomamobile.android.sdk.friend;

import java.io.Serializable;

/**
 * Credential that identifies user to a 3rd party social service. This credential is passed to the friend service API
 * in order to initiate friend discovery.
 * <p>
 *     For example in order to discover friends who use client app via Facebook the client app would initiate friend
 *     discovery like this:
 * <pre>
 *     import com.palomamobile.android.sdk.auth.FbUserCredential;
 *     ...
 *     ..
 *
 *     SocialUserCredential fbCredential = new SocialUserCredential(fbAccessToken.getUserId(), fbAccessToken.getToken(), FbUserCredential.FB_CREDENTIAL_TYPE)
 *     JobPostSocialUserCredential jobPostSocialUserCredential = friendManager.createJobPostSocialUserCredential(fbCredential);
 *     ServiceSupport.Instance.getJobManager().addJobInBackground(jobPostSocialUserCredential);
 * </pre>
 * <br/>
 *
 */
public class SocialUserCredential implements Serializable {

    private final String userId;
    private final String accessToken;
    private final String type;

    public SocialUserCredential(String userId, String accessToken, String type) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.type = type;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getType() {
        return type;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SocialUserCredential that = (SocialUserCredential) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (accessToken != null ? !accessToken.equals(that.accessToken) : that.accessToken != null) return false;
        return !(type != null ? !type.equals(that.type) : that.type != null);

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (accessToken != null ? accessToken.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SocialUserCredential{");
        sb.append("accessToken='").append(accessToken).append('\'');
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
