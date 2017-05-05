package com.dailystudio.apiaiandroidclient;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dailystudio.apiaiandroidclient.database.ChatHistoryDatabaseModal;
import com.dailystudio.development.Logger;

import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Fulfillment;
import ai.api.model.Result;

/**
 * Created by nan on 2015/9/3.
 */
public class ChatService extends IntentService {

    private final static String SRV_NAME = "apiai-chat-service";

    private final static String ACCESS_TOKEN =
            "6b95beccba2349a4b91d3f5838e9bfc5";

    private static final AIConfiguration AI_CONFIG = new AIConfiguration(ACCESS_TOKEN,
            AIConfiguration.SupportedLanguages.English,
            AIConfiguration.RecognitionEngine.System);

    private static AIDataService sAIDataService;

    private synchronized AIDataService getAIDataService(Context context) {
        if (sAIDataService == null
                && context != null) {
            sAIDataService = new AIDataService(
                    context.getApplicationContext(),
                    AI_CONFIG);
        }

        return sAIDataService;
    }

    public ChatService() {
        super(SRV_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }

        final Context context = getApplicationContext();
        if (context == null) {
            return;
        }

        final String action = intent.getAction();
        if (Constants.ACTION_CHAT.equals(action)) {
            final String agent = intent.getStringExtra(Constants.EXTRA_AGENT_ID);
            final String user = intent.getStringExtra(Constants.EXTRA_USER);
            final String message = intent.getStringExtra(Constants.EXTRA_MESSAGES);
            Logger.debug("agent[%s] chat: user: %s, message = %s",
                    agent,
                    user, message);
            if (TextUtils.isEmpty(agent)
                    || TextUtils.isEmpty(user)
                    || TextUtils.isEmpty(message)) {
                Logger.warn("invalid agent[%s], user[%s] or message[%s]",
                        agent, user, message);
                return;
            }

            AIDataService aiDataService = getAIDataService(context);
            if (aiDataService == null) {
                Logger.warn("API.AI data service is unavailable.");
                return;
            }

            final AIRequest aiRequest = new AIRequest();
            aiRequest.setQuery(message);

            ChatHistoryDatabaseModal.addChatHistory(context,
                    agent,
                    user,
                    message,
                    aiRequest);

            AIResponse aiResponse;
            try {
                aiResponse = aiDataService.request(aiRequest);
            } catch (AIServiceException e) {
                Logger.warn("could not get aiResponse: %s", e.toString());

                aiResponse = null;
            }

            if (aiResponse == null) {
                return;
            }

            ChatHistoryDatabaseModal.addChatHistory(context,
                    agent,
                    user,
                    dumpTextFromResponse(aiResponse), aiResponse);
        }
    }

    private String dumpTextFromResponse(AIResponse aiResponse) {
        if (aiResponse == null) {
            return null;
        }

        final Result result = aiResponse.getResult();
        if (result == null) {
            return null;
        }

        final Fulfillment fulfillment = result.getFulfillment();
        if (fulfillment == null) {
            return null;
        }

        return fulfillment.getSpeech();
    }

}
