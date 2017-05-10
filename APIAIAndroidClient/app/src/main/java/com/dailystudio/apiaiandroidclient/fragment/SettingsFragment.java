package com.dailystudio.apiaiandroidclient.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.TextUtils;

import com.dailystudio.apiaiandroidclient.AppPrefs;
import com.dailystudio.apiaiandroidclient.Constants;
import com.dailystudio.apiaiandroidclient.R;
import com.dailystudio.apiaiandroidclient.TextToSpeechService;
import com.dailystudio.development.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by nanye on 17/5/8.
 */

public class SettingsFragment extends com.dailystudio.app.fragment.SettingsFragment {

    private class GetVoiceModalTask extends AsyncTask<Context, Void, VoiceModalItem[]> {

        private int mStatus;

        private RadioSetting<VoiceModalItem> mSetting;

        private GetVoiceModalTask(RadioSetting<VoiceModalItem> radioSetting) {
            mSetting = radioSetting;
        }

        @Override
        protected VoiceModalItem[] doInBackground(Context... params) {
            if (params == null || params.length <= 0) {
                return null;
            }

            final Context context = params[0];

            TextToSpeech tts = null;
            synchronized (mOnInitListener) {
                tts = new TextToSpeech(context,
                        mOnInitListener);
                try {
                    mOnInitListener.wait();
                } catch (InterruptedException e) {
                    Logger.warn("wait tts interrupted.");
                }
            }

            if (mStatus != TextToSpeech.SUCCESS) {
                return null;
            }

            Set<Voice> voices = tts.getVoices();
            if (voices == null) {
                return null;
            }

            List<VoiceModalItem> items = new ArrayList<>();

            for (Voice voice: voices) {
                if (voice.isNetworkConnectionRequired() == false
                        && Locale.US.equals(voice.getLocale())) {
                    items.add(new VoiceModalItem(voice));
                }
            }

            Collections.sort(items, mVoiceModalComapator);

            tts.shutdown();

            return items.toArray(new VoiceModalItem[0]);
        }

        @Override
        protected void onPostExecute(VoiceModalItem[] items) {
            super.onPostExecute(items);

            if (mSetting != null
                    && items != null) {
                mSetting.clear();
                mSetting.addItems(items);
            }
        }

        private TextToSpeech.OnInitListener mOnInitListener = new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                mStatus = status;

                synchronized (this) {
                    notifyAll();
                }
            }
        };
    }

    private static class VoiceModalItem implements RadioSettingItem {

        private Voice mVoice;

        private VoiceModalItem(Voice voice) {
            mVoice = voice;
        }

        @Override
        public CharSequence getLabel() {
            String name = mVoice.getName();
            if (TextUtils.isEmpty(name)) {
                return name;
            }

            String[] parts = name.split("#");
            if (parts == null || parts.length < 2) {
                return name;
            }

            return parts[1].replace("-local", "").replace("_", " ");
        }

        @Override
        public String getId() {
            return mVoice.getName();
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

        RadioSetting<VoiceModalItem> voiceModals =
                new RadioSetting(context,
                        Constants.SETTING_VOICE_MODAL,
                        R.drawable.ic_setting_voice_modal,
                        R.string.settings_voice_modal,
                        new RadioSettingsLayoutHolder(),
                        null) {

                    @Override
                    protected String getSelectedId() {
                        return AppPrefs.getVoiceModal(getContext());
                    }

                    @Override
                    protected void setSelected(String selectedId) {
                        AppPrefs.setVoiceModal(getContext(), selectedId);

                        TextToSpeechService.reset(getContext());
                        TextToSpeechService.textToSpeech(getContext(),
                                getString(R.string.voice_sample_text));
                    }
                };

        new GetVoiceModalTask(voiceModals)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        getContext());

        return new Setting[] {
                voiceOnRecv,
                voiceModals,
        };
    }

    private Comparator<VoiceModalItem> mVoiceModalComapator = new Comparator<VoiceModalItem>() {

        @Override
        public int compare(VoiceModalItem lhs, VoiceModalItem rhs) {
            return lhs.getId().compareTo(rhs.getId());
        }

    };

}
