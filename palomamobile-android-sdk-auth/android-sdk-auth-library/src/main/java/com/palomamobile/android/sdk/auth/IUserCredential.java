package com.palomamobile.android.sdk.auth;

import java.io.Serializable;

/**
 * User credentials used to authenticate the user and get their User {@link AccessToken}.
 * <br/>
 */
public interface IUserCredential extends Serializable {

    String PROP_NAME = "name";
    String PROP_CREDENTIAL_TYPE = "type";

    String getUsername();

    void setUsername(String userName);

    String getUserPassword();

    String getCredentialType();

}
