package com.palomamobile.android.sdk.verification.sampleApp;

import android.app.Application;
import com.palomamobile.android.sdk.core.ServiceSupport;

/**
 *
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceSupport.Instance.init(this.getApplicationContext());
    }
}
