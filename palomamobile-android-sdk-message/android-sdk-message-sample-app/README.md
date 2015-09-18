# Before you begin
Note that for the sake of brevity this sample app doesn't make use of the Paloma Notification Service. 
This means that refreshing of data such as
list of friends or messages received must be done manually by user or by a polling mechanism. 
A more user friendly and data efficient app would not do this,
instead it would integrate with the Paloma Notification SDK to receive asynchronous server driven data updates.
Have look here for more info on the [Notification SDK](../palomamobile-android-sdk-notification).

# Paloma Message SDK for Android Sample App


Sample app that signs-up a new user (with auto generated username and password). After successful sign-up the app 
displays a screen which shows:

1. Local users own User ID
2. Means of entering friends User ID, in order to add another user as a friend
3. Means of refreshing a list of friends

The easiest way to try out the sample app functionality is to run the app on 2 different devices.

## First we need to set up a Friends relationship between 2 users
Enter the Local User ID from one device as a "Friend User ID" on the other device. Do this on both devices,
mutual 2 way friendship is required in order to send and receive messages.
Once this friendship is established and users can see each other in the respective list of friends we're ready to 
send messages.

## Now we can send & receive messages
Once a friendship is established the UI for sending receiving messages becomes enabled.
User can enter text into the text input field with "Enter message" hint and send the message to all friends by pressing
the "Send to all" button. Due to the limitations of this sample app (as described in the "Before you begin" section at 
the top of this document) the messages are only refreshed when user presses the "Get messages received" button.
