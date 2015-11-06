#!/usr/bin/env bash
rm -rf build/docs;
javadoc \
-d build/docs \
-protected \
-classpath $ANDROID_HOME/platforms/android-15/android.jar \
-sourcepath palomamobile-android-sdk-core/android-sdk-core-library/src/main/java:palomamobile-android-sdk-auth/android-sdk-auth-library/src/main/java:palomamobile-android-sdk-friend/android-sdk-friend-library/src/main/java:palomamobile-android-sdk-media/android-sdk-media-library/src/main/java:palomamobile-android-sdk-message/android-sdk-message-library/src/main/java:palomamobile-android-sdk-notification/android-sdk-notification-library/src/main/java:palomamobile-android-sdk-user/android-sdk-user-library/src/main/java:palomamobile-android-sdk-verification/android-sdk-verification-library/src/main/java \
-linkoffline http://d.android.com/reference/ file:$ANDROID_HOME/docs/reference \
-subpackages com
