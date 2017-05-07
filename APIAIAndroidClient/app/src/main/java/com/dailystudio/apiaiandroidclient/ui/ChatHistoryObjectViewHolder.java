package com.dailystudio.apiaiandroidclient.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dailystudio.apiaiandroidclient.Constants;
import com.dailystudio.apiaiandroidclient.R;
import com.dailystudio.apiaiandroidclient.TextToSpeechService;
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
        if (mSendMessageView != null) {
            mSendMessageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSendMessageView != null) {
                        tts(mSendMessageView.getContext(),
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
                        tts(mRecvMessageView.getContext(),
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
    }

    private void tts(Context context, CharSequence speech) {
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

}
