package com.dailystudio.apiaiandroidclient;

import android.content.Context;

import com.dailystudio.app.prefs.AbsPrefs;

/**
 * Created by nanye on 17/4/28.
 */

public class AppPrefs extends AbsPrefs {

    private final static String PREF_NAME = "app-prefs";

    private final static String KEY_VOICE_ON_RECV = "voice-on-rect";
    private final static String KEY_VOICE_MODAL = "voice-modal";

    private final static AppPrefs INSTANCE = new AppPrefs();

    @Override
    protected String getPrefName() {
        return PREF_NAME;
    }

    public static synchronized void setVoiceOnRectEnabled(Context context, boolean enabled) {
        INSTANCE.setBooleanPrefValue(context, KEY_VOICE_ON_RECV, enabled);
    }

    public static synchronized boolean isVoiceOnRectEnabled(Context context) {
        return INSTANCE.getBooleanPrefValue(context, KEY_VOICE_ON_RECV);
    }

    public static synchronized void setVoiceModal(Context context, String modalName) {
        INSTANCE.setStringPrefValue(context, KEY_VOICE_MODAL, modalName);
    }

    public static synchronized String getVoiceModal(Context context) {
        return INSTANCE.getStringPrefValue(context, KEY_VOICE_MODAL);
    }

}
