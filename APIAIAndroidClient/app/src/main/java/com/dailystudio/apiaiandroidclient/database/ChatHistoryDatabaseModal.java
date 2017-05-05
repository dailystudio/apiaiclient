package com.dailystudio.apiaiandroidclient.database;

import android.content.Context;
import android.text.TextUtils;

import com.dailystudio.datetime.dataobject.TimeCapsuleDatabaseWriter;

import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

/**
 * Created by nanye on 17/3/31.
 */

public class ChatHistoryDatabaseModal {

    public static void addChatHistory(Context context,
                                      String agentId,
                                      String user,
                                      String text,
                                      AIRequest aiRequest) {
        if (context == null
                || TextUtils.isEmpty(agentId)
                || TextUtils.isEmpty(user)
                || aiRequest == null) {
            return;
        }

        ChatHistoryObject historyObject =
                new ChatHistoryObject(context);

        historyObject.setTime(System.currentTimeMillis());
        historyObject.setType(ChatHistoryObject.TYPE_SEND);
        historyObject.setAgent(agentId);
        historyObject.setText(text);
        historyObject.setMessage(aiRequest);
        historyObject.setUser(user);

        TimeCapsuleDatabaseWriter writer =
                new TimeCapsuleDatabaseWriter(context, ChatHistoryObject.class);

        writer.insert(historyObject);
    }

    public static void addChatHistory(Context context,
                                      String agentId,
                                      String user,
                                      String text,
                                      AIResponse aiResponse) {
        if (context == null
                || TextUtils.isEmpty(agentId)
                || TextUtils.isEmpty(user)
                || aiResponse == null) {
            return;
        }


        ChatHistoryObject historyObject =
                new ChatHistoryObject(context);

        historyObject.setTime(System.currentTimeMillis());
        historyObject.setType(ChatHistoryObject.TYPE_RECEIVE);
        historyObject.setAgent(agentId);
        historyObject.setText(text);
        historyObject.setMessage(aiResponse);
        historyObject.setUser(user);

        TimeCapsuleDatabaseWriter writer =
                new TimeCapsuleDatabaseWriter(context, ChatHistoryObject.class);

        writer.insert(historyObject);
    }

}
