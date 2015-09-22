# Paloma Friend SDK for Android

## Overview
Friend SDK module supports the [Friend Service provided by the Paloma Mobile platform cloud](http://54.251.112.144/docs/friend-service/index.html#_service_description).

At a high level the Friend SDK supports the following functionality:

* Managing manual friends list
* Managing relationships with other users
* Friend matching/discovery via 3rd social platforms (currently limited to Facebook)

The [javadoc is available here](http://palomamobile.github.io/paloma-android-sdk/docs/index.html) under package _com.palomamobile.android.sdk.friend_

Before diving deeper into a specific SDK module we suggest you first have a look at the [Paloma SDK for Android overview]
 (https://github.com/PalomaMobile/paloma-android-sdk) to get an idea of:

* which services are supported
* how the SDK hangs together
* how to get an account so that you can start writing apps

## Note about Tests & Facebook
To run instrumentation tests that create Facebook users you will need to provide your own Facebook app id and secret [here](./android-sdk-user-library/src/androidTest/res/values/strings.xml).
We recommend that you use an existing or [create a new](https://developers.facebook.com/quickstarts/?platform=android) Facebook test app.
Neither your Facebook app id or secret are sent to Paloma Mobile services, they are passed only to the Facebook SDK to enable
creation of test users. If you don't provide these values the tests that create Facebook users will simply not run.

## Dependencies on other SDK modules
The Friend SDK depends on:

* [Core SDK](../palomamobile-android-sdk-core)
* [Auth SDK](../palomamobile-android-sdk-auth)
* [User SDK](../palomamobile-android-sdk-user)

Optional but recommended dependency on the [Notification SDK](../palomamobile-android-sdk-notification)
will enable server push notifications for [events of interest](http://54.251.112.144/docs/friend-service/index.html#_notifications).


## Quick start

Before much else can be done the application needs to register a user. If you're not sure how to do this have a look 
at the [User SDK](../palomamobile-android-sdk-user).

### Declare dependencies in your `build.gradle`

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
        compile 'com.palomamobile.android.sdk:friend:2.5@aar'
        
        //Enable optional notifications of friends discovered
        compile 'com.palomamobile.android.sdk:notification:2.5@aar'
        ...
        
    }


### In you code

Initiate the Paloma Mobile platform SDK


    public class App extends Application {
    
        @Override
        public void onCreate() {
            super.onCreate();
            ServiceSupport.Instance.init(this.getApplicationContext());
        }
    }

In your Activity class request a refresh of friends list and listen for results:


    public class FriendSampleActivity extends Activity {


        private IFriendManager friendManager ;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            //listen for events
            ServiceSupport.Instance.getEventBus().register(this);
            
            friendManager = ServiceSupport.Instance.getServiceManager(IFriendManager.class);
            ...
            
            buttonRefreshFriends = (Button) findViewById(R.id.buttonRefreshFriends);
            buttonRefreshFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //create a job to be done, i.e. get a list of friends 
                    JobGetFriends jobGetFriends = friendManager.createJobGetFriends();
                    //put the job on the queue and when the job is done EventFriendsListReceived will be fired 
                    ServiceSupport.Instance.getJobManager().addJobInBackground(jobGetFriends);
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
        public void onEventMainThread(EventFriendsListReceived event) {
            //act on events
            Throwable throwable = event.getFailure();
            if (throwable == null) {
                displayFriends(event.getSuccess());
            }
            else {
                Toast.makeText(getApplicationContext(), "Err: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.w(TAG, throwable.getMessage());
            }
        }
        
    ...
    
    }

For a complete working project see the [android-sdk-friend-sample-app](../palomamobile-android-sdk-friend/android-sdk-friend-sample-app)
