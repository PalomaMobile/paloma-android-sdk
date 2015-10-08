package com.palomamobile.android.sdk.user;

import java.io.Serializable;
import java.util.Map;

public class UserUpdate implements Serializable {

    protected String dateOfBirth;
    protected String displayName;
    protected Map<String, String> custom;

    public Map<String, String> getCustom() {
        return custom;
    }

    public void setCustom(Map<String, String> custom) {
        this.custom = custom;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
