# Paloma Message SDK for Android

## Overview
Message SDK module supports the [Message Service provided by the Paloma Mobile platform cloud](http://54.251.112.144/docs/message-service/index.html#_service_description).

At a high level the Message SDK supports the following functionality:

* User to single user or multiple users communication between users who have a mutual friend relationship
* Simple text messaging
* Link sharing (one or more HTTP accessible resources)
* Deleting messages

The [javadoc is available here](http://palomamobile.github.io/paloma-android-sdk/docs/index.html) under package _com.palomamobile.android.sdk.message_

Before diving deeper into a specific SDK module we suggest you first have a look at the [Paloma SDK for Android overview]
 (https://github.com/PalomaMobile/paloma-android-sdk) to get an idea of:

* which services are supported
* how the SDK hangs together
* how to get an account so that you can start writing apps


## Dependencies on other SDK modules
The Message SDK depends on:

* [Core SDK](../palomamobile-android-sdk-core)
* [Auth SDK](../palomamobile-android-sdk-auth)
* [User SDK](../palomamobile-android-sdk-user)

Optional but recommended dependency on the [Notification SDK](../palomamobile-android-sdk-notification)
will enable server push notifications for [events of interest](http://54.251.112.144/docs/message-service/index.html#_notifications).


## Quick start

For a complete working project see the [android-sdk-message-sample-app](../palomamobile-android-sdk-message/android-sdk-message-sample-app)

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
    compile 'com.squareup.okhttp:okhttp-urlconnection:2.3.0'
    compile 'com.squareup.okhttp:okhttp:2.4.0'
    compile 'com.squareup.retrofit:retrofit:1.9.0'
    compile 'com.birbit:android-priority-jobqueue:1.3.3'

    //Paloma Platform SDK modules
    compile 'com.palomamobile.android.sdk:core:2.5@aar'
    compile 'com.palomamobile.android.sdk:auth:2.5@aar'
    compile 'com.palomamobile.android.sdk:user:2.5@aar'
    compile 'com.palomamobile.android.sdk:message:2.5@aar'

    //Enable optional notifications of messages received
    compile 'com.palomamobile.android.sdk:notification:2.5@aar'
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

In your Activity class request to send a message and listen for messages from friends:


```java
public class MessageSampleActivity extends Activity {


    private IMessageManager messageManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //listen for events
        ServiceSupport.Instance.getEventBus().register(this);
        
        messageManager = ServiceSupport.Instance.getServiceManager(IMessageManager.class);
        ...
        
        Button buttonSendMessageToAll = (Button) findViewById(R.id.buttonSendMessageToAll);
        buttonSendMessageToAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobPostMessage jobPostMessageToFriends = messageManager.createJobPostMessageToFriends("text/plain", getMessageText(), null, friendIds);
                ServiceSupport.Instance.getJobManager().addJobInBackground(jobPostMessageToFriends);
            }
        });


        Button buttonGetMessages = (Button) findViewById(R.id.buttonGetMessages);
        buttonGetMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobGetMessagesReceived jobGetMessagesReceived = messageManager.createJobGetMessagesReceived();
                jobGetMessagesReceived.setServiceRequestParams(new ServiceRequestParams().sort("id", ServiceRequestParams.Sort.Order.Desc));
                ServiceSupport.Instance.getJobManager().addJobInBackground(jobGetMessagesReceived);
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
    public void onEventMainThread(EventMessagesReceived event) {
        //act on events
        Throwable throwable = event.getFailure();
        if (throwable == null) {
            showMessagesReceived(event.getSuccess());
        }
        else {
            Toast.makeText(getApplicationContext(), "Err: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            Log.w(TAG, throwable.getMessage());
        }
    }
    
...

}
```
