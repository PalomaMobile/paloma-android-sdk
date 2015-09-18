package com.palomamobile.android.sdk.notification;

/**
 * Response to Gcm Registration Id update request.
 */
public class GcmRegistrationIdResponse {

    /**
     * User Id.
     */
    private long id;

    private String gcmRegistrationId;

    public String getGcmRegistrationId() {
        return gcmRegistrationId;
    }

    public void setGcmRegistrationId(String gcmRegistrationId) {
        this.gcmRegistrationId = gcmRegistrationId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GcmRegistrationIdResponse{");
        sb.append("id=").append(id);
        sb.append(", gcmRegistrationId='").append(gcmRegistrationId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
