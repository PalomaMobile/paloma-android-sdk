package com.palomamobile.android.sdk.notification;

/**
 * Response to Gcm Registration Id update and retrieval request.
 */
public class GcmRegistrationIdResponse {

    private long userId;

    private String gcmRegistrationId;

    private String deviceId;

    public String getGcmRegistrationId() {
        return gcmRegistrationId;
    }

    public void setGcmRegistrationId(String gcmRegistrationId) {
        this.gcmRegistrationId = gcmRegistrationId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long id) {
        this.userId = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GcmRegistrationIdResponse that = (GcmRegistrationIdResponse) o;

        if (userId != that.userId) return false;
        if (gcmRegistrationId != null ? !gcmRegistrationId.equals(that.gcmRegistrationId) : that.gcmRegistrationId != null)
            return false;
        return !(deviceId != null ? !deviceId.equals(that.deviceId) : that.deviceId != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (gcmRegistrationId != null ? gcmRegistrationId.hashCode() : 0);
        result = 31 * result + (deviceId != null ? deviceId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GcmRegistrationIdResponse{" +
                "userId=" + userId +
                ", deviceId='" + deviceId + '\'' +
                ", gcmRegistrationId='" + gcmRegistrationId + '\'' +
                '}';
    }
}
