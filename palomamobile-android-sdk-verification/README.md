[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.palomamobile.android.sdk/verification/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.palomamobile.android.sdk/verification)

# Paloma verification SDK for Android

## Overview
verification SDK module supports the [verification Service provided by the Paloma Mobile platform cloud](http://54.251.112.144/docs/verification-service/index.html#_service_description).
At a high level the verification SDK supports the following functionality:

* Receiving push verifications via GCM (in future to be expanded to SMS, and custom socket channel)

The [javadoc is available here](http://palomamobile.github.io/paloma-android-sdk/docs/index.html) under package _com.palomamobile.android.sdk.verification_

Before diving deeper into a specific SDK module we suggest you first have a look at the [Paloma SDK for Android overview]
 (https://github.com/PalomaMobile/paloma-android-sdk) to get an idea of:

* which services are supported
* how the SDK hangs together
* how to get an account so that you can start writing apps using the Paloma Mobile platform


## Dependencies on other SDK modules
The verification SDK depends on:

* [Core SDK](../palomamobile-android-sdk-core)
* [Auth SDK](../palomamobile-android-sdk-auth)
* [User SDK](../palomamobile-android-sdk-user)

## Quick start

For a complete working project see the [android-sdk-verification-sample-app](../palomamobile-android-sdk-verification/android-sdk-verification-sample-app)

Before much else can be done the application needs to register a user. If you're not sure how to do this have a look 
at the [User SDK](../palomamobile-android-sdk-user).

### Declare dependencies in your `build.gradle`

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
    compile 'com.palomamobile.android.sdk:verification:2.6@aar'
    
    //enable verifications via GCM
    compile "com.google.android.gms:play-services:7.5.0"
    ...
    
}
```

### Configure your `AndroidManifest.xml` with Paloma Mobile GCM integration settings

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.foo"
    android:versionCode="1"
    android:versionName="1.0">

    ...

    <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE"/>
    <uses-permission android:name="android.permission.WAKE_LOCK"/>

    <permission android:name="${applicationId}.permission.C2D_MESSAGE" android:protectionLevel="signature" />
    <uses-permission android:name="${applicationId}.permission.C2D_MESSAGE" />
    

    <application android:label="@string/app_name"
        android:icon="@drawable/ic_launcher"
        android:theme="@style/AppTheme"
        android:name=".App">

        <meta-data android:name="com.palomamobile.android.sdk.ClientId" android:value="@string/palomamobile_client_id"/>
        <meta-data android:name="com.palomamobile.android.sdk.Endpoint" android:value="@string/palomamobile_endpoint"/>
        <meta-data android:name="com.palomamobile.android.sdk.GcmSenderId" android:value="@string/palomamobile_gcm_sender_id"/>

        <receiver
            android:name="com.google.android.gms.gcm.GcmReceiver"
            android:permission="com.google.android.c2dm.permission.SEND" >
            <intent-filter>
                <action android:name="com.google.android.c2dm.intent.RECEIVE" />
                <action android:name="com.google.android.c2dm.intent.REGISTRATION" />

                <category android:name="com.foo" />
            </intent-filter>
        </receiver>

    </application>

</manifest>
```

Replace `com.foo` everywhere with the actual package name of your application.

Values for `palomamobile_client_id`, `palomamobile_endpoint`, and `palomamobile_gcm_sender_id` are provided at the time
you register your application with the Paloma Mobile platform.


### In your code

Initiate the Paloma Mobile platform SDK, and register to receive verifications. The type of verifications that will be
received by any given client app depends on the Paloma Mobile platform services the client app uses. Each service lists
the verifications it triggers. For example:

* [Friend SDK verifications](http://54.251.112.144/docs/friend-service/index.html#_verifications)
* [Message SDK verifications](http://54.251.112.144/docs/message-service/index.html#_verifications)


```java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceSupport.Instance.init(this.getApplicationContext());
        ServiceSupport.Instance.getEventBus().register(this);
        ...
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(EventverificationReceived event) {
        verification verification = event.getverification();
        Log.d(TAG, "onEventMainThread(): " + event);
        String type = verification.getType().toLowerCase();
        switch (type) {
            //if this app integrates with the Message SDK it makes sense to listen for new received messages
            case "messageservice.receivedmessage":
                handleMessageverification(verification);
                break;
            ...
        }
    }
}
```
