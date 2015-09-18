# Before you begin
Note that for the sake of brevity this sample app doesn't make use of the Paloma Notification Service. 
This means that refreshing of data such as
list of friends must be done manually by user. A more user friendly and data 
efficient app would not do this,
instead it would integrate with the Paloma Notification SDK to receive asynchronous server driven data updates.
Have look here for more info on the [Notification SDK](../palomamobile-android-sdk-notification).

# Paloma Friend SDK for Android Sample App

Sample app that signs-up a new user (with auto generated username and password). After successful sign-up the app 
displays a screen which shows:

1. Local users own User ID
2. Means of entering friends User ID, in order to add another user as a friend
3. Means of refreshing a list of friends

The easiest way to try out the sample app functionality is to run the app on 2 different devices, enter the Local User 
ID from once device as a "Friend User ID" on the other device (do this on both devices, friendship is a 2 way 
relationship), press the "Refresh Friends" button and see what happens :)
