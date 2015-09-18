package com.palomamobile.android.sdk.auth;

/**
 * Enumeration of the possible OAuth2 authentication types.<br/>
 *
 */
public enum AuthType {
    /**
     * No authentication
     */
    None,
    /**
     * Client app authentication. Requests on behalf of the application such as "create user".
     */
    Client,
    /**
     * User authentication. Requests on behalf of the application user, most authenticated API calls fall under this category.
     */
    User,
}
