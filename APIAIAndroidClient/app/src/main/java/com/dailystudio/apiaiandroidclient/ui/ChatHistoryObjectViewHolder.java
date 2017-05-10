package com.dailystudio.apiaiandroidclient.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dailystudio.apiaiandroidclient.R;
import com.dailystudio.apiaiandroidclient.TextToSpeechService;
import com.dailystudio.apiaiandroidclient.database.ChatHistoryObject;
import com.dailystudio.apiaicommon.database.AgentObject;
import com.dailystudio.app.ui.AbsArrayItemViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

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
                        TextToSpeechService.textToSpeech(
                                mSendMessageView.getContext(),
                                mSendMessageView.getText());
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
                        TextToSpeechService.textToSpeech(
                                mRecvMessageView.getContext(),
                                mRecvMessageView.getText());
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
        }

        if (mSendMessageView != null) {
            String text = historyObject.getText();
            mSendMessageView.setText(text);
        }

/*
        if (mRecvIconView != null) {
            if (mAgentInfo != null) {
                ImageLoader.getInstance().displayImage(
                        mAgentInfo.getIconUrl(),
                        mRecvIconView,
                        com.dailystudio.apiaicommon.Constants.DEFAULT_IMAGE_LOADER_OPTIONS);
            } else {
                mRecvIconView.setImageResource(R.drawable.ic_chat_robot);
            }
        }
*/
    }

}
