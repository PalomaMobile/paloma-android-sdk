package com.palomamobile.android.sdk.datatracking.sampleApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.palomamobile.android.sdk.core.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataTrackingSampleActivity extends Activity {

    private static final Logger logger = LoggerFactory.getLogger(DataTrackingSampleActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceSupport.Instance.getEventBus().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceSupport.Instance.getEventBus().unregister(this);
    }

}
