package com.dailystudio.apiaiandroidclient.fragment;

import android.content.Context;

import com.dailystudio.apiaiandroidclient.AppPrefs;
import com.dailystudio.apiaiandroidclient.Constants;
import com.dailystudio.apiaiandroidclient.R;

/**
 * Created by nanye on 17/5/8.
 */

public class SettingsFragment extends com.dailystudio.app.fragment.SettingsFragment {

    @Override
    protected Setting[] createSettings(Context context) {
        Setting voiceOnRecv =
                new SwitchSetting(context,
                        Constants.SETTING_VOICE_ON_RECT,
                        R.drawable.ic_setting_voice,
                        R.string.settings_voice_on_recv,
                        new SwitchSettingsLayoutHolder()) {

                    @Override
                    public boolean isSwitchOn(Context context) {
                        return AppPrefs.isVoiceOnRectEnabled(context);
                    }

                    @Override
                    public void setSwitchOn(Context context, boolean on) {
                        AppPrefs.setVoiceOnRectEnabled(context, on);
                    }

                };

        return new Setting[] {
                voiceOnRecv,
        };
    }

}
