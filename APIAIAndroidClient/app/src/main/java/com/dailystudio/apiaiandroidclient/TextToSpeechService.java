package com.dailystudio.apiaiandroidclient;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.dailystudio.development.Logger;

import java.util.Locale;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by nanye on 17/5/7.
 */

public class TextToSpeechService extends IntentService {

    private final static String SRV_NAME = "speak-out-service";

    private static TextToSpeech sTTSInstance = null;
    private static boolean sTTSInitialized = false;

    Queue<String> sPendingSpeeches = new ConcurrentLinkedQueue<>();

    public TextToSpeechService() {
        super(SRV_NAME);
    }

    public static void textToSpeech(Context context, CharSequence speech) {
        if (context == null
                || TextUtils.isEmpty(speech)) {
            return;
        }

        Intent i = new Intent(Constants.ACTION_TTS);

        i.setClass(context.getApplicationContext(),
                TextToSpeechService.class);
        i.putExtra(Constants.EXTRA_SPEECH, speech.toString());

        context.startService(i);
    }

    public static void reset(Context context) {
        if (context == null) {
            return;
        }

        Intent i = new Intent(Constants.ACTION_RESET_TTS);

        i.setClass(context.getApplicationContext(),
                TextToSpeechService.class);

        context.startService(i);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }

        final String action = intent.getAction();
        if (Constants.ACTION_TTS.equals(action)) {
            final String speech = intent.getStringExtra(Constants.EXTRA_SPEECH);
            if (TextUtils.isEmpty(speech)) {
                Logger.warn("empty speech, skip");

                return;
            }

            final TextToSpeech tts = getTTSEngine();
            if (tts == null) {
                Logger.warn("TTS engine is unavailable.");

                return;
            }

            if (sTTSInitialized == false) {
                sPendingSpeeches.add(speech);
            } else {
                Logger.debug("speak now: %s", speech);
                tts.speak(speech, TextToSpeech.QUEUE_ADD,
                        null, String.valueOf(System.currentTimeMillis()));
            }
        } else if (Constants.ACTION_RESET_TTS.equals(action)) {
            Logger.warn("reset tts");
            if (sTTSInstance != null) {
                sTTSInstance.shutdown();
                sTTSInstance = null;
            }

            sTTSInitialized = false;
        }

    }

    private synchronized TextToSpeech getTTSEngine() {
        if (sTTSInstance == null) {
            sTTSInstance = new TextToSpeech(getApplicationContext(),
                    mOnInitListener);
        }

        return sTTSInstance;
    }

    private TextToSpeech.OnInitListener mOnInitListener = new TextToSpeech.OnInitListener() {

        @Override
        public void onInit(int status) {
            Logger.debug("tts init status: %d", status);
            if (status != TextToSpeech.SUCCESS) {
                return;
            }

            sTTSInitialized = true;

            TextToSpeech tts = getTTSEngine();
            if (tts != null) {
                tts.setLanguage(Locale.ENGLISH);
            }

            final String voice = AppPrefs.getVoiceModal(getApplicationContext());
            if (!TextUtils.isEmpty(voice)) {
                Set<Voice> voices = tts.getVoices();
                if (voices != null) {
                    for (Voice v : voices) {
                        if (voice.equals(v.getName())) {
                            Logger.debug("set voice to : %s", v.getName());

                            tts.setVoice(v);
                        }
                    }
                }
            }

            if (sPendingSpeeches.size() > 0) {
                String speech;
                for (;;) {
                    speech = sPendingSpeeches.poll();
                    if (speech == null) {
                        break;
                    }

                    Logger.debug("flush speech: %s", speech);
                    tts.speak(speech, TextToSpeech.QUEUE_ADD,
                            null, String.valueOf(System.currentTimeMillis()));
                }
            }
        }

    };
}
