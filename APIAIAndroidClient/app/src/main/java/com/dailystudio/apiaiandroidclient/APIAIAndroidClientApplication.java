package com.dailystudio.apiaiandroidclient;

import com.dailystudio.app.DevBricksApplication;
import com.facebook.stetho.Stetho;

/**
 * Created by nanye on 17/5/3.
 */

public class APIAIAndroidClientApplication extends DevBricksApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.USE_STETHO) {
            Stetho.initializeWithDefaults(this);
        }
   }

    @Override
    protected boolean isDebugBuild() {
        return BuildConfig.DEBUG;
    }

}
