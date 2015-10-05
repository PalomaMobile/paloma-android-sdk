[![Build Status](https://magnum.travis-ci.com/PalomaMobile/paloma-android-sdk.svg?token=MqkF7WcptxY6tzunqsBa)](https://magnum.travis-ci.com/PalomaMobile/paloma-android-sdk)
[![Coverage Status](https://coveralls.io/repos/PalomaMobile/paloma-android-sdk/badge.svg)](https://coveralls.io/r/PalomaMobile/paloma-android-sdk)


# Paloma SDK for Android

## Feature overview

The Paloma SDK for Android provides a library, and documentation for developers to build connected mobile applications using the Paloma Mobile Platform Services.
For in-depth information check out the detailed description of the [Paloma Mobile Platform Services](http://54.251.112.144/index.html#_platform_description).
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

## Internal dependencies

To get started you will need to set-up a [platform application](http://54.251.112.144/index.html#_authenticating_client_applications).

## External dependencies

The SDK depends on the following open source projects that we hold in high regard:

* [EventBus](https://github.com/greenrobot/EventBus) for updating your app with changes, this is how the SDK communicates with your app. 
* [gson](https://github.com/google/gson) for parsing and constructing JSON.
* [okhttp](https://github.com/square/okhttp) for HTTP comms.
* [retrofit](https://github.com/square/retrofit) to talk to our restful platform APIs.
* [android-priority-jobqueue](https://github.com/yigit/android-priority-jobqueue) for Job management. Whenever your app asks our SDK to do something (eg: create a user) it's a job
that can be persisted across application restarts, retried on network failures, prioritized, canceled ...

### ... and this is a sample of what the code looks like

First declare dependencies in your `gradle.build` file:

```groovy
dependencies {

    ...

    //you may already use some of these and that is OK
    compile 'de.greenrobot:eventbus:2.4.0'
    compile 'com.google.code.gson:gson:2.3.1'
    compile 'com.android.support:support-annotations:22.2.0'
    compile 'com.squareup.okhttp:okhttp-urlconnection:2.5.0'
    compile 'com.squareup.okhttp:okhttp:2.5.0'
    compile 'com.squareup.retrofit:retrofit:1.9.0'
    compile 'com.birbit:android-priority-jobqueue:1.3.3'

    //Paloma Platform SDK modules
    compile 'com.palomamobile.android.sdk:core:2.6@aar'
    compile 'com.palomamobile.android.sdk:auth:2.6@aar'
    compile 'com.palomamobile.android.sdk:user:2.6@aar'
    
    ...
    
}
```

Then register a user:

``` java
public class UserRegistrationActivity extends Activity {

    private IUserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //listen for events
        ServiceSupport.Instance.getEventBus().register(this);
        
        userManager = ServiceSupport.Instance.getServiceManager(IUserManager.class);
        ...
        
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ...
                JobRegisterUser jobRegisterUserViaPassword = userManager.createJobRegisterUserViaPassword(userName, password);
                ServiceSupport.Instance.getJobManager().addJobInBackground(jobRegisterUserViaPassword);
            }
        });
    }
    
    @Override
    protected void onDestroy() {
        //stop listening for events
        ServiceSupport.Instance.getEventBus().unregister(this);
        super.onDestroy();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(EventLocalUserUpdated event) {
        Throwable throwable = event.getFailure();
        if (throwable == null) {
            User user = event.getSuccess();
            ...
        }
        else {
            Toast.makeText(getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
```


## Building the SDK from sources

You can build from source via Gradle using the [Android Tools Gradle Plugin](http://tools.android.com/tech-docs/new-build-system/user-guide#TOC-Dependencies-Android-Libraries-and-Multi-project-setup). 
Building the SDK requires the Java 7 SDK and Android SDK.

Checkout the source code:

`git clone https://github.com/PalomaMobile/paloma-android-sdk.git`

Switch to the SDK directory

`cd paloma-android-sdk/`

Create a file called `local.properties` add this line to it (replace the path value):

`sdk.dir=/your/path/to/android_sdk`

To build of all library .aar files and sample app .apk files run:

`./gradlew clean build`

To build and install library .aar files for all modules into your local maven repo run:

`./gradlew clean installLibraries`

To unit tests in VM during build run:

`./gradlew clean assembleDebug testDebug`

To execute tests (instrumentation or unit) on device run:

`./gradlew clean connectedAndroidTest`

To execute all tests run:

`./gradlew clean assembleDebug testDebug connectedAndroidTest`
