package com.dailystudio.apiaiwebclient;

import com.dailystudio.app.DevBricksApplication;
import com.facebook.stetho.Stetho;

/**
 * Created by nanye on 17/4/27.
 */

public class APIAIWebClientApplication extends DevBricksApplication {

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
