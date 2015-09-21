package com.palomamobile.android.sdk.core;

/**
 * Defines names of custom headers available to all Service interfaces across the various SDK modules. The values of those
 * headers are used by the SDK Core module to enhance SDK functionality such as to determine compatibility between the
 * SDK module version and current Server API version.
 */
public interface CustomHeader {

    /**
     * Header with this name is placed on each Service call to describe the SDK compatibility requirements.
     */
    String HEADER_PALOMA_TARGET_SERVICE_VERSION = "Paloma-Target-Service-Version";

    /**
     * Header with this name is placed on each Service call to describe the SDK version for error reporting etc.
     */
    String HEADER_PALOMA_SDK_MODULE_VERSION = "Paloma-SDK-Module-Version";

    /**
     * Header with this name identifies each individual request for the purposes of deduping requests during retries.
     */
    String HEADER_NAME_PALOMA_REQUEST = "X-Paloma-Request";

}
