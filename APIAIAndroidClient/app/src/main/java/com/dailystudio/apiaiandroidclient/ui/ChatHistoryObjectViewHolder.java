package com.dailystudio.apiaiandroidclient.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.dailystudio.apiaiandroidclient.R;
import com.dailystudio.apiaiandroidclient.database.ChatHistoryObject;
import com.dailystudio.app.ui.AbsArrayItemViewHolder;

import ai.api.model.AIRequest;

/**
 * Created by nanye on 17/3/6.
 */

public class ChatHistoryObjectViewHolder extends AbsArrayItemViewHolder<ChatHistoryObject> {

    private TextView mRecvMessageView;

    private TextView mSendMessageView;

    private View mItemSendCard;
    private View mItemRecvCard;

    public ChatHistoryObjectViewHolder(View itemView) {
        super(itemView);

        setupViews(itemView);
    }

    private void setupViews(View itemView) {
        if (itemView == null) {
            return;
        }

        mItemSendCard = itemView.findViewById(R.id.send_card);
        mItemRecvCard = itemView.findViewById(R.id.recv_card);

        mSendMessageView = (TextView) itemView.findViewById(R.id.send_message);
        mRecvMessageView = (TextView) itemView.findViewById(R.id.recv_message);
    }

    @Override
    public void bindItem(Context context, ChatHistoryObject historyObject) {
        if (context == null || historyObject == null) {
            return;
        }

        final Resources res = context.getResources();
        if (res == null) {
            return;
        }

        boolean send =
                (historyObject.getType() == ChatHistoryObject.TYPE_SEND);

        if (mItemSendCard != null) {
            mItemSendCard.setVisibility(send ? View.VISIBLE : View.GONE);
        }

        if (mItemRecvCard != null) {
            mItemRecvCard.setVisibility(send ? View.GONE : View.VISIBLE);
        }

        if (mRecvMessageView != null) {
            String text = historyObject.getText();
            mRecvMessageView.setText(text);
        }

        if (mSendMessageView != null) {
            String text = historyObject.getText();
            mSendMessageView.setText(text);
        }
    }

}
