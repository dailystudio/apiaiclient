package com.dailystudio.apiaiandroidclient;

import com.dailystudio.apiaicommon.APIAICommonApplication;

/**
 * Created by nanye on 17/5/3.
 */

public class APIAIAndroidClientApplication extends APIAICommonApplication {

    @Override
    protected boolean isDebugBuild() {
        return BuildConfig.DEBUG;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        TextToSpeechService.shutdown(getApplicationContext());
    }

}
