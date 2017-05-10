package com.dailystudio.apiaiandroidclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.text.TextUtils;

import com.dailystudio.app.prefs.AbsPrefs;
import com.dailystudio.development.Logger;

/**
 * Created by nanye on 17/4/28.
 */

public class AppPrefs extends AbsPrefs {

    private final static String PREF_NAME = "app-prefs";

    private final static String KEY_VOICE_ON_RECV = "voice-on-rect";
    private final static String KEY_VOICE_MODAL = "voice-modal";
    private final static String KEY_ACCESS_TOKEN_PREFIX = "access-token-";

    private final static AppPrefs INSTANCE = new AppPrefs();

    @Override
    protected String getPrefName() {
        return PREF_NAME;
    }

    public static synchronized AppPrefs getInstance() {
        return INSTANCE;
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

    public static synchronized void setAgentAccessToken(Context context, String agentId, String token) {
        if (TextUtils.isEmpty(agentId)) {
            return;
        }

        Logger.debug("set agent[%s] access token: token = [%s]",
                agentId, token);

        INSTANCE.setStringPrefValue(context,
                KEY_ACCESS_TOKEN_PREFIX + agentId, token);
    }

    public static synchronized String getAgentAccessToken(Context context, String agentId) {
        if (TextUtils.isEmpty(agentId)) {
            return null;
        }

        return INSTANCE.getStringPrefValue(context,
                KEY_ACCESS_TOKEN_PREFIX + agentId);
    }

}