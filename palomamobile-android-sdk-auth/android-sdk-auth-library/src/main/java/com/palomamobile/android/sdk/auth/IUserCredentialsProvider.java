package com.palomamobile.android.sdk.auth;

/**
 * Implementing class provides user credentials used for authentication with the Paloma Mobile platform. This can be
 * done by delegating to UI (prompt user for username/password) or by integration with the Facebook SDK.
 *
 * <br/>
 */
public interface IUserCredentialsProvider {

    /**
     * @return user credential for authentication purposes.
     */
    IUserCredential getUserCredential();

}
