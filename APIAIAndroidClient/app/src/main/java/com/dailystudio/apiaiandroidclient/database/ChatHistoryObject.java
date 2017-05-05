package com.dailystudio.apiaiandroidclient.database;

import android.content.Context;
import android.text.TextUtils;

import com.dailystudio.dataobject.Column;
import com.dailystudio.dataobject.IntegerColumn;
import com.dailystudio.dataobject.Template;
import com.dailystudio.dataobject.TextColumn;
import com.dailystudio.datetime.dataobject.TimeCapsule;
import com.google.gson.Gson;

import ai.api.GsonFactory;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

/**
 * Created by nanye on 17/5/5.
 */

public class ChatHistoryObject extends TimeCapsule {

    public static final int TYPE_SEND = 0;
    public static final int TYPE_RECEIVE = 1;

    public static final Column COLUMN_AGENT_ID = new TextColumn("agent", false);
    public static final Column COLUMN_USER_ID = new TextColumn("user", false);
    public static final Column COLUMN_SESSION = new TextColumn("session", false);
    public static final Column COLUMN_MESSAGE = new TextColumn("message", false);
    public static final Column COLUMN_TEXT = new TextColumn("text", false);
    public static final Column COLUMN_TYPE = new IntegerColumn("type", false);

    private final static Column[] sCloumns = {
            COLUMN_AGENT_ID,
            COLUMN_USER_ID,
            COLUMN_SESSION,
            COLUMN_MESSAGE,
            COLUMN_TEXT,
            COLUMN_TYPE,
    };

    private static Gson GSON = GsonFactory.getDefaultFactory().getGson();

    public ChatHistoryObject(Context context) {
        super(context);

        final Template templ = getTemplate();

        templ.addColumns(sCloumns);
    }

    public String getAgent() {
        return getTextValue(COLUMN_AGENT_ID);
    }

    public void setAgent(String agent) {
        setValue(COLUMN_AGENT_ID, agent);
    }

    public String getUser() {
        return getTextValue(COLUMN_USER_ID);
    }

    public void setUser(String user) {
        setValue(COLUMN_USER_ID, user);
    }

    public String getSession() {
        return getTextValue(COLUMN_SESSION);
    }

    public void setSession(String session) {
        setValue(COLUMN_SESSION, session);
    }

    public Object getMessage() {
        final String str =
                getTextValue(COLUMN_MESSAGE);
        if (TextUtils.isEmpty(str)) {
            return null;
        }

        return GSON.fromJson(str,
                (getType() == TYPE_SEND
                        ? AIRequest.class : AIResponse.class));
    }

    public void setMessage(Object message) {
        final String str = GSON.toJson(message);

        if (!TextUtils.isEmpty(str)) {
            setValue(COLUMN_MESSAGE, str);
        }
    }

    public String getText() {
        return getTextValue(COLUMN_TEXT);
    }

    public void setText(String text) {
        setValue(COLUMN_TEXT, text);
    }

    public int getType() {
        return getIntegerValue(COLUMN_TYPE);
    }

    public void setType(int type) {
        setValue(COLUMN_TYPE, type);
    }

}