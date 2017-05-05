package com.dailystudio.apiaiandroidclient.loader;

import android.content.Context;

import com.dailystudio.apiaiandroidclient.database.ChatHistoryObject;
import com.dailystudio.app.dataobject.loader.DatabaseObjectsLoader;
import com.dailystudio.dataobject.query.ExpressionToken;
import com.dailystudio.dataobject.query.OrderingToken;
import com.dailystudio.dataobject.query.Query;

/**
 * Created by nanye on 17/3/31.
 */

public class ChatHistoryLoader extends DatabaseObjectsLoader<ChatHistoryObject> {

    private String mAgent;
    private String mUser;

    public ChatHistoryLoader(Context context, String agent, String user) {
        super(context);

        mAgent = agent;
        mUser = user;
    }

    @Override
    protected Query getQuery(Class<ChatHistoryObject> klass) {
        Query query = super.getQuery(klass);

        if (query == null) {
            return query;
        }

        ExpressionToken userToken =
                ChatHistoryObject.COLUMN_AGENT_ID.eq(mAgent)
                        .and(ChatHistoryObject.COLUMN_USER_ID.eq(mUser));

        ExpressionToken selToken =
                query.getSelection();
        if (selToken == null) {
            selToken = userToken;
        } else {
            if (userToken != null) {
                selToken = selToken.and(userToken);
            }
        }

        query.setSelection(selToken);

        OrderingToken orderByToken =
                ChatHistoryObject.COLUMN_TIME.orderByAscending();

        query.setOrderBy(orderByToken);

        return query;
    }

    @Override
    protected Class<ChatHistoryObject> getObjectClass() {
        return ChatHistoryObject.class;
    }

}
