package com.dailystudio.apiaiandroidclient;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dailystudio.apiaiandroidclient.database.ChatHistoryDatabaseModal;
import com.dailystudio.development.Logger;

import java.util.HashMap;
import java.util.Map;

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

    private static Map<String, AIDataService> sAIDataServices = new HashMap<>();

    private synchronized AIDataService getAIDataService(Context context, String agentId) {
        if (context == null || TextUtils.isEmpty(agentId)) {
            return null;
        }

        AIDataService service = sAIDataServices.get(agentId);
        if (service == null) {
            String accessToken = AppPrefs.getAgentAccessToken(context, agentId);
            if (TextUtils.isEmpty(accessToken)) {
                Logger.debug("access token is NOT set for agent: %s", agentId);

                return null;
            }

            AIConfiguration aiConfig = new AIConfiguration(accessToken,
                    AIConfiguration.SupportedLanguages.English,
                    AIConfiguration.RecognitionEngine.System);

            service = new AIDataService(
                    context.getApplicationContext(),
                    aiConfig);

            sAIDataServices.put(agentId, service);
        }

        return service;
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
            final String session = intent.getStringExtra(Constants.EXTRA_SESSION);
            Logger.debug("agent[%s, session: %s] chat: user: %s, message = %s",
                    agent,
                    session,
                    user, message);
            if (TextUtils.isEmpty(agent)
                    || TextUtils.isEmpty(user)
                    || TextUtils.isEmpty(session)
                    || TextUtils.isEmpty(message)) {
                Logger.warn("invalid agent[%s], user[%s], session[%s] or message[%s]",
                        agent, user, session, message);
                return;
            }

            AIDataService aiDataService = getAIDataService(context, agent);
            if (aiDataService == null) {
                Logger.warn("API.AI data service is unavailable.");
                return;
            }

            final AIRequest aiRequest = new AIRequest();
            aiRequest.setQuery(message);
            aiRequest.setSessionId(session);

            ChatHistoryDatabaseModal.addChatHistory(context,
                    agent,
                    user,
                    session,
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
                    session,
                    dumpTextFromResponse(aiResponse), aiResponse);

            if (AppPrefs.isVoiceOnRectEnabled(context)) {
                TextToSpeechService.textToSpeech(context,
                        dumpTextFromResponse(aiResponse));
            }
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
