package com.dailystudio.apiaiandroidclient.fragment;

import android.content.Context;

import com.dailystudio.apiaiandroidclient.AppPrefs;
import com.dailystudio.apiaiandroidclient.Constants;
import com.dailystudio.apiaiandroidclient.R;

/**
 * Created by nanye on 17/5/8.
 */

public class SettingsFragment extends com.dailystudio.app.fragment.SettingsFragment {

    private static class FakeVoiceItem implements RadioSettingItem {

        private int mLabelId;
        private Context mContext;

        private FakeVoiceItem(Context context, int labelId) {
            mLabelId = labelId;
            mContext = context;
        }

        @Override
        public CharSequence getLabel() {
            return mContext.getString(mLabelId);
        }

        @Override
        public String getId() {
            return getLabel().toString();
        }

        @Override
        public String toString() {
            return getId();
        }
    }

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

        FakeVoiceItem[] fakeVoices = {
                new FakeVoiceItem(context, R.string.voice_male_1),
                new FakeVoiceItem(context, R.string.voice_male_2),
                new FakeVoiceItem(context, R.string.voice_female_1),
                new FakeVoiceItem(context, R.string.voice_female_2),
        };

        Setting voiceType =
                new RadioSetting(context,
                        Constants.SETTING_VOICE_MODAL,
                        R.drawable.ic_setting_voice_modal,
                        R.string.settings_voice_modal,
                        new RadioSettingsLayoutHolder(),
                        fakeVoices) {

                    @Override
                    protected String getSelectedId() {
                        return AppPrefs.getVoiceModal(getContext());
                    }

                    @Override
                    protected void setSelected(String selectedId) {
                        AppPrefs.setVoiceModal(getContext(), selectedId);
                    }
                };

        return new Setting[] {
                voiceOnRecv,
                voiceType,
        };
    }

}
