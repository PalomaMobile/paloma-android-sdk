# Paloma Notification SDK for Android Sample App

Sample app that signs-up or logs in a user with either username/password or Facebook login.

## Note about the Sample App & Facebook
To enable Facebook login you need to provide your own value for facebook_app_id [here](./src/main/res/values/strings.xml).
We recommend that you use an existing or [create a new](https://developers.facebook.com/quickstarts/?platform=android) Facebook test app.
If you don't provide this value the sample app will still be able to register users via username & password but it will 
fail with Facebook login.
