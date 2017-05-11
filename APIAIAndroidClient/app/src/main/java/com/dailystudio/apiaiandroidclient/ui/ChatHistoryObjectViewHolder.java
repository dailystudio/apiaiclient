package com.dailystudio.apiaiandroidclient.ui;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dailystudio.apiaiandroidclient.ChatService;
import com.dailystudio.apiaiandroidclient.R;
import com.dailystudio.apiaiandroidclient.TextToSpeechService;
import com.dailystudio.apiaiandroidclient.database.ChatHistoryObject;
import com.dailystudio.apiaicommon.database.AgentObject;
import com.dailystudio.app.ui.AbsArrayItemViewHolder;

import ai.api.model.AIResponse;

/**
 * Created by nanye on 17/3/6.
 */

public class ChatHistoryObjectViewHolder extends AbsArrayItemViewHolder<ChatHistoryObject> {

    private AgentObject mAgentInfo;

    private TextView mRecvMessageView;
    private TextView mSendMessageView;

    private ImageView mRecvIconView;

    private View mItemSendCard;
    private View mItemRecvCard;

    public ChatHistoryObjectViewHolder(View itemView, AgentObject agentInfo) {
        super(itemView);

        mAgentInfo = agentInfo;

        setupViews(itemView);
    }

    private void setupViews(View itemView) {
        if (itemView == null) {
            return;
        }

        mRecvIconView = (ImageView) itemView.findViewById(R.id.icon_recv);

        mItemSendCard = itemView.findViewById(R.id.send_card);
        mItemRecvCard = itemView.findViewById(R.id.recv_card);

        mSendMessageView = (TextView) itemView.findViewById(R.id.send_message);
        if (mSendMessageView != null) {
            mSendMessageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSendMessageView != null) {
                        ttsTextOnView(mSendMessageView);
                    }
                }
            });
        }

        mRecvMessageView = (TextView) itemView.findViewById(R.id.recv_message);
        if (mRecvMessageView != null) {
            mRecvMessageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRecvMessageView != null) {
                        ttsTextOnView(mRecvMessageView);
                    }
                }
            });
        }
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
            mRecvMessageView.setTag(historyObject);
        }

        if (mSendMessageView != null) {
            String text = historyObject.getText();
            mSendMessageView.setText(text);
            mSendMessageView.setTag(historyObject);
        }

    }

    private void ttsTextOnView(TextView textView) {
        if (textView == null) {
            return;
        }

        Object o = textView.getTag();
        if (o instanceof ChatHistoryObject == false) {
            return;
        }

        ChatHistoryObject historyObject = (ChatHistoryObject)o;

        String speech = null;
        if (historyObject.getType() == ChatHistoryObject.TYPE_SEND) {
            speech = historyObject.getText();
        } else {
            Object recvObject = historyObject.getMessage();
            if (recvObject instanceof AIResponse) {
                AIResponse aiResponse = (AIResponse) recvObject;

                speech = ChatService.dumpSpeechFromResponse(aiResponse);
            }
        }

        if (!TextUtils.isEmpty(speech)) {
            TextToSpeechService.textToSpeech(
                    mSendMessageView.getContext(),
                    speech);
        }
    }

}
