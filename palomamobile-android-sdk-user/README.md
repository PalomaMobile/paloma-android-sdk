# Paloma User SDK for Android

## Overview
User SDK module supports the user management features of the [User Service provided by the Paloma Mobile platform cloud](http://54.251.112.144/docs/user-service/index.html#_service_description). 
Note that Authentication functionality is implemented in the [Auth SDK](../palomamobile-android-sdk-auth)
not in the User SDK.

At a high level the User SDK supports the following functionality:

* User registration and login with username & password
* User registration and login with Facebook
* Management of user attributes

The [javadoc is available here](http://palomamobile.github.io/paloma-android-sdk/docs/index.html) under package _com.palomamobile.android.sdk.user_

Before diving deeper into a specific SDK module we suggest you first have a look at the [Paloma SDK for Android overview]
 (https://github.com/PalomaMobile/paloma-android-sdk) to get an idea of:

* which services are supported
* how the SDK hangs together
* how to get an account so that you can start writing apps

## Note about Tests, Sample App & Facebook
To run instrumentation tests that create Facebook users you will need to provide your own Facebook app id and secret [here](./android-sdk-user-library/src/androidTest/res/values/strings.xml).
We recommend that you use an existing or [create a new](https://developers.facebook.com/quickstarts/?platform=android) Facebook test app.
Neither your Facebook app id or secret are sent to Paloma Mobile services, they are passed only to the Facebook SDK to enable
creation of test users. If you don't provide these values the tests that create Facebook users will simply not run.

Similarily, if you wish to run the Sample App with Facebook login you will need to provide your own value 
for facebook_app_id [here](./android-sdk-user-sample-app/src/main/res/values/strings.xml). If you don't provide this 
value the sample app will still be able to register users via username & password but it will fail with Facebook login.  

## Dependencies on other SDK modules
The User SDK depends on:

* [Core SDK](../palomamobile-android-sdk-core)
* [Auth SDK](../palomamobile-android-sdk-auth)


## Quick start

### Declare dependencies in your `build.gradle`

```groovy
dependencies {

    ...

    //you may already use some of these and that is OK
    compile 'de.greenrobot:eventbus:2.4.0'
    compile 'com.google.code.gson:gson:2.3.1'
    compile 'com.android.support:support-annotations:22.2.0'
    compile 'com.squareup.okhttp:okhttp-urlconnection:2.3.0'
    compile 'com.squareup.okhttp:okhttp:2.4.0'
    compile 'com.squareup.retrofit:retrofit:1.9.0'
    compile 'com.birbit:android-priority-jobqueue:1.3.3'

    //Paloma Platform SDK modules
    compile 'com.palomamobile.android.sdk:core:2.5@aar'
    compile 'com.palomamobile.android.sdk:auth:2.5@aar'
    compile 'com.palomamobile.android.sdk:user:2.5@aar'
    
    ...
    
}
```

### In your code

Initiate the Paloma Mobile platform SDK

```java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceSupport.Instance.init(this.getApplicationContext());
    }
}

```

In your Activity class request to sign-up a user with username and password credentials and listen for results:

```java
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

To use the 'Facebook login' follow the [Facebook Android SDK instructions](https://developers.facebook.com/docs/facebook-login/android) 
to get users `AccessToken`. Here is a code example showing how to use the Facebook credentials to either create a new 
user or sign-in as an existing user into the Paloma Platform:
 
```java

fbCallbackManager = CallbackManager.Factory.create();

buttonFbLogin = (LoginButton) findViewById(R.id.buttonFbLogin);
buttonFbLogin.setReadPermissions("user_friends");
buttonFbLogin.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
    @Override
    public void onSuccess(LoginResult loginResult) {
        AccessToken accessToken = loginResult.getAccessToken();
        Log.d(TAG, "FB login success, received FB AccessToken: " +
                "userId = " + accessToken.getUserId() + ", " +
                "applicationId = " + accessToken.getApplicationId() + ", " +
                "token = " + accessToken.getToken());
        JobRegisterUser jobRegisterUserViaFacebook = userManager.createJobRegisterUserViaFacebook(accessToken.getUserId(), accessToken.getToken());
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobRegisterUserViaFacebook);
    }

    @Override
    public void onCancel() {
        Log.d(TAG, "FB login cancelled");
    }

    @Override
    public void onError(FacebookException exception) {
        Log.w(TAG, "FB login throwable", exception);
    }
});
```     

For a complete working project that uses both username & password as well as Facebook login see the [android-sdk-user-sample-app](../palomamobile-android-sdk-user/android-sdk-user-sample-app)
