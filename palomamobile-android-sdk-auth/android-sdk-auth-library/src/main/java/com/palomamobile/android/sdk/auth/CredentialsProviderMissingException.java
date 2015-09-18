package com.palomamobile.android.sdk.auth;

/**
 * Indicates illegal state of the SDK, the call to {@link IAuthManager#setUserCredentialsProvider(IUserCredentialsProvider)}
 * was not made prior to making networked SDK calls that require a user access token.
 *
 */
public class CredentialsProviderMissingException extends IllegalStateException {
    public CredentialsProviderMissingException() {
    }

    public CredentialsProviderMissingException(String detailMessage) {
        super(detailMessage);
    }

    public CredentialsProviderMissingException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CredentialsProviderMissingException(Throwable throwable) {
        super(throwable);
    }
}
