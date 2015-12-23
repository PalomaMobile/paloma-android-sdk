[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.palomamobile.android.sdk/verification/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.palomamobile.android.sdk/verification)

# Paloma Verification SDK for Android

## Overview
Verification SDK module supports the [Verification Service provided by the Paloma Mobile platform cloud](http://46.137.242.200/docs/verification-service/index.html#_service_description).
At a high level the Verification SDK supports the following functionality:

* Verification of email address

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

The verification of an email address is a process that follows these steps:

* Client application requests email address verification (requires Client authentication only)
* Paloma Verification Service sends and email with one time verification code
* User retrieves the one time verification code from the email (thus proving the email address is under their control)
* User requests an update to their user account with the email address and one time verification code (requires User authentication)
* Paloma User Service assigns the verified email address to the user account

### Declare dependencies in your `build.gradle`

```groovy
dependencies {

    ...

    //you may already use some of these and that is OK
    compile 'de.greenrobot:eventbus:2.4.0'
    compile 'com.google.code.gson:gson:2.4'
    compile 'com.android.support:support-annotations:22.2.0'
    compile 'com.squareup.okhttp:okhttp-urlconnection:2.5.0'
    compile 'com.squareup.okhttp:okhttp:2.5.0'
    compile 'com.squareup.retrofit:retrofit:1.9.0'
    compile 'com.birbit:android-priority-jobqueue:1.3.5'

    //Paloma Platform SDK modules
    compile 'com.palomamobile.android.sdk:core:2.7.1@aar'
    compile 'com.palomamobile.android.sdk:auth:2.7.1@aar'
    compile 'com.palomamobile.android.sdk:user:2.7.1@aar'
    compile 'com.palomamobile.android.sdk:verification:2.7.1@aar'
    
}
```

### In your code

Initiate the Paloma Mobile platform SDK.

```java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceSupport.Instance.init(this.getApplicationContext());
        ...
    }
}
```

In your Activity class request email verification and once the one time verification code from the email is available update the user:

```java
public class VerificationSampleActivity extends Activity {

    private IEmailVerificationManager emailVerificationManager = ServiceSupport.Instance.getServiceManager(IEmailVerificationManager.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceSupport.Instance.getEventBus().register(this);

        final EditText etEmail = (EditText) findViewById(R.id.editTextEmail) ;
        Button btnEmail = (Button) findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                JobCreateEmailVerification jobCreateEmailVerification = new JobCreateEmailVerification(email);
                jobManager.addJob(jobCreateEmailVerification);
                textViewStatus.setText("Started step 1. of Email verification");
            }
        });

        final EditText etCode = (EditText) findViewById(R.id.editTextCode);
        Button btnVerify = (Button) findViewById(R.id.btnVerify);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                JobPostUserVerifiedEmail jobPostUserVerifiedEmail = new JobPostUserVerifiedEmail(email, code);
                jobManager.addJob(jobPostUserVerifiedEmail);
                textViewStatus.setText("Started step 2. of Email verification");
            }
        });
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(EventEmailVerificationCreated event) {
        Throwable throwable = event.getFailure();
        if (throwable == null) {
            textViewStatus.setText("Step 1. of User email verification successful. Check your email for the verification code needed in the next step.");
        }
        else {
            textViewStatus.setText("Step 1. of User email verification failed: " + event.getFailure());
        }
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(EventLocalUserUpdated event) {
        Throwable throwable = event.getFailure();
        if (throwable == null) {
            textViewStatus.setText("Step 2. of User email verification successful. Email verification complete.\n" + event.getSuccess().toString());
        }
        else {
            textViewStatus.setText("Step 2. of User email verification failed: " + event.getFailure());
        }
    }

    @Override
    protected void onDestroy() {
        ServiceSupport.Instance.getEventBus().unregister(this);
        super.onDestroy();
    }

```
