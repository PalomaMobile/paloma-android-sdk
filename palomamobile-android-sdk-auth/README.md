# Paloma Auth SDK for Android
## Overview
The Auth SDK module is not normally called directly from client apps. It is required by other SDK modules in order to authenticate
their server API calls.

Auth SDK module supports the auth features of the 
[User Service provided by the Paloma Mobile platform cloud](http://46.137.242.200/docs/user-service/index.html#_service_description). Note that User management functionality is implemented in the [User SDK](../palomamobile-android-sdk-user)
not in the Auth SDK.

At a high level the Auth SDK supports the following functionality:

* OAuth2 Client authentication for Client/App functionality such as registering a new user 
* OAuth2 User authentication for User functionality such as sending a message to another user


The [javadoc is available here](http://palomamobile.github.io/paloma-android-sdk/docs/index.html) under package _com.palomamobile.android.sdk.auth_

Before diving deeper into a specific SDK module we suggest you first have a look at the [Paloma SDK for Android overview]
 (https://github.com/PalomaMobile/paloma-android-sdk) to get an idea of:

* which services are supported
* how the SDK hangs together
* how to get an account so that you can start writing apps


## Dependencies on other SDK modules
The Auth SDK depends on:

* [Core SDK](../palomamobile-android-sdk-core)
