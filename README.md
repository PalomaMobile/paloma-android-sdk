[![Build Status](https://travis-ci.org/PalomaMobile/paloma-android-sdk.svg)](https://travis-ci.org/PalomaMobile/paloma-android-sdk)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.palomamobile/androidSdk/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.palomamobile/androidSdk)
[![Coverage Status](https://coveralls.io/repos/PalomaMobile/paloma-android-sdk/badge.svg)](https://coveralls.io/r/PalomaMobile/paloma-android-sdk)

# Paloma SDK for Android

## Feature overview

The Paloma SDK for Android provides a library, and documentation for developers to build connected mobile applications using the Paloma Mobile Platform Services.
For in-depth information check out the detailed description of the [Paloma Mobile Platform Services](http://ec2-54-251-61-68.ap-southeast-1.compute.amazonaws.com/index.html#_platform_description).
At a high level the SDK enables the following key features provided by the platform:

* Authentication and User management
* Messaging and Sharing
* Friend management and discovery
* Media storage and retrieval
* Integrated server push notifications

## Architecture overview

The Paloma SDK for Android is written and structured with the following goals in mind:

- make it easy to expand module functionality without breaking existing code
- make it easy to add more modules without breaking existing code
- make it easy to provide custom 3rd party implementation for each module

The SDK is split into several modules clearly defined by their function.

* [Core module](./palomamobile-android-sdk-core) 
* [Auth module](./palomamobile-android-sdk-auth) 
* [User module](./palomamobile-android-sdk-user)
* [Message module](./palomamobile-android-sdk-message)
* [Friend module and discovery](./palomamobile-android-sdk-friend)
* [Media module](./palomamobile-android-sdk-media)
* [Notification module](./palomamobile-android-sdk-notification)

When building an application it is possible to use only a subset of modules rather than the entire SDK.

## Documentation

Complete [javadoc for all modules](http://palomamobile.github.io/paloma-android-sdk/docs/index.html).

Each application facing module provides a <b>Sample app</b> project that demonstrates how to use its functionality.

## External dependencies

The SDK depends on the following open source projects that we hold in high regard:

* [EventBus](https://github.com/greenrobot/EventBus)
* [gson](https://github.com/google/gson)
* [okhttp](https://github.com/square/okhttp)
* [retrofit](https://github.com/square/retrofit)
* [android-priority-jobqueue](https://github.com/yigit/android-priority-jobqueue)

## Building the SDK from sources

You can build from source via Gradle using the [Android Tools Gradle Plugin](http://tools.android.com/tech-docs/new-build-system/user-guide#TOC-Dependencies-Android-Libraries-and-Multi-project-setup). 
Building the SDK requires the Java 7 SDK and Android SDK.

Checkout the source code:

`git clone https://github.com/PalomaMobile/paloma-android-sdk.git`

Switch to the SDK directory

`cd paloma-android-sdk/`

To build of all library .aar files and sample app .apk files run:

`gradle clean build`

To build and install library .aar files for all modules into your local maven repo run:

`gradle clean installLibraries`

To unit tests in VM during build run:

`gradle clean assembleDebug testDebug`

To execute tests (instrumentation or unit) on device run:

`gradle clean connectedAndroidTest`

To execute all tests run:

`gradle clean assembleDebug testDebug connectedAndroidTest`
