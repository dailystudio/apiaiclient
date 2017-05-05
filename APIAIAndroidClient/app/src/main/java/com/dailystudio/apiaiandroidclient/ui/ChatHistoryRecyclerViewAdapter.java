package com.dailystudio.apiaiandroidclient.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.dailystudio.apiaiandroidclient.R;
import com.dailystudio.apiaiandroidclient.database.ChatHistoryObject;
import com.dailystudio.app.ui.AbsArrayRecyclerAdapter;

/**
 * Created by nanye on 17/3/6.
 */

public class ChatHistoryRecyclerViewAdapter
        extends AbsArrayRecyclerAdapter<ChatHistoryObject, ChatHistoryObjectViewHolder> {

    public ChatHistoryRecyclerViewAdapter(Context context) {
        super(context);
    }

    @Override
    public ChatHistoryObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(
                R.layout.layout_chat_history_item, null);
        ChatHistoryObjectViewHolder viewHolder = new ChatHistoryObjectViewHolder(view);

        return viewHolder;
    }

}