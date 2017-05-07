package com.dailystudio.apiaiandroidclient;

import android.content.Context;

import com.dailystudio.app.prefs.AbsPrefs;

/**
 * Created by nanye on 17/4/28.
 */

public class AppPrefs extends AbsPrefs {

    private final static String PREF_NAME = "app-prefs";

    private final static String KEY_TTS_VOICE = "tts-voice";

    private final static AppPrefs INSTANCE = new AppPrefs();

    @Override
    protected String getPrefName() {
        return PREF_NAME;
    }

    public static synchronized void setTTSVoice(Context context, String voiceName) {
        INSTANCE.setStringPrefValue(context, KEY_TTS_VOICE, voiceName);
    }

    public static synchronized String getTTSVoice(Context context) {
        return INSTANCE.getStringPrefValue(context, KEY_TTS_VOICE);
    }

}
