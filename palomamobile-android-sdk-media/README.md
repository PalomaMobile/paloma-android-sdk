# Paloma Media SDK for Android

## Overview
Media SDK module supports the [Media Service provided by the Paloma Mobile platform cloud](http://54.251.112.144/docs/media-service/index.html#_service_description).

At a high level the Media SDK supports the following functionality:

* API to upload public media (available to anyone with the hard-to-guess URL)
* API to upload private media (available to authenticated media owner or to unauthenticated users via an expiring, short lived URL provided by the media owner)
* Standard HTTP download mechanism via URL

The [javadoc is available here](http://palomamobile.github.io/paloma-android-sdk/docs/index.html) under package _com.palomamobile.android.sdk.media_

Before diving deeper into a specific SDK module we suggest you first have a look at the [Paloma SDK for Android overview]
 (https://github.com/PalomaMobile/paloma-android-sdk) to get an idea of:

* which services are supported
* how the SDK hangs together
* how to get an account so that you can start writing apps


## Dependencies on other SDK modules
The Media SDK depends on:

* [Core SDK](../palomamobile-android-sdk-core)
* [Auth SDK](../palomamobile-android-sdk-auth)
* [User SDK](../palomamobile-android-sdk-user)


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
        compile 'com.palomamobile.android.sdk:media:2.5@aar'
        
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


    public class MediaSampleActivity extends Activity {


        private IMediaManager mediaManager ;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            //listen for events
            ServiceSupport.Instance.getEventBus().register(this);
        }

        
        @Override
        protected void onDestroy() {
            //stop listening for events
            ServiceSupport.Instance.getEventBus().unregister(this);
            super.onDestroy();
        }
        
        
        private void onImageSelected(String imgPath) {
            mediaManager = ServiceSupport.Instance.getServiceManager(IMediaManager.class);
            ...
            
            JobUploadMediaPublic mediaUploadJob = ServiceSupport.Instance.getServiceManager(IMediaManager.class).createJobMediaUploadPublic("image/jpg", imgPath);
            ServiceSupport.Instance.getJobManager().addJobInBackground(mediaUploadJob);
        }
        
        
        @SuppressWarnings("unused")
        public void onEventMainThread(EventMediaUploaded event) {
            //act on events
            Throwable throwable = event.getFailure();
            if (throwable == null) {
                displayUploadSuccess(event.getSuccess().getUrl());
            }
            else {
                Toast.makeText(getApplicationContext(), "Err: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.w(TAG, throwable.getMessage());
            }
        }
        
    ...
    
    }

For a complete working project see the [android-sdk-media-sample-app](../palomamobile-android-sdk-media/android-sdk-media-sample-app)
