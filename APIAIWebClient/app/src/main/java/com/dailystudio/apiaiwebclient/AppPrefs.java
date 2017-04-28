package com.dailystudio.apiaiwebclient;

import android.content.Context;

import com.dailystudio.app.prefs.AbsPrefs;

/**
 * Created by nanye on 17/4/28.
 */

public class AppPrefs extends AbsPrefs {

    private final static String PREF_NAME = "app-prefs";

    private final static String KEY_PREDEFINED_AGENTS_VERSION = "predefined-agents-ver";

    private final static AppPrefs INSTANCE = new AppPrefs();

    @Override
    protected String getPrefName() {
        return PREF_NAME;
    }

    public static synchronized void setPredefinedAgentsVersion(Context context, long version) {
        INSTANCE.setLongPrefValue(context, KEY_PREDEFINED_AGENTS_VERSION, version);
    }

    public static synchronized long getPredefinedAgentsVersion(Context context) {
        return INSTANCE.getLongPrefValue(context, KEY_PREDEFINED_AGENTS_VERSION, 0);
    }

}
