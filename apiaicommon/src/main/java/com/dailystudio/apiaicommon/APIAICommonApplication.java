package com.dailystudio.apiaicommon;

import com.dailystudio.app.DevBricksApplication;
import com.facebook.stetho.Stetho;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by nanye on 17/4/27.
 */

public class APIAICommonApplication extends DevBricksApplication {


    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.USE_STETHO) {
            Stetho.initializeWithDefaults(this);
        }

        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(this).build();

        ImageLoader.getInstance().init(config);
    }

}
