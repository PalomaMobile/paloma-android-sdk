package com.palomamobile.android.sdk.user;

/**
 * Available verifications methods
 */
public enum VerificationMethod {

    Email {
        @Override
        String getApiChannelName() {
            return "emails";
        }
    },
    ;

    /**
     * The channel name for this verifications method. Used when composing paths for restful API calls.
     * @return API channel name
     */
    abstract String getApiChannelName();

}
