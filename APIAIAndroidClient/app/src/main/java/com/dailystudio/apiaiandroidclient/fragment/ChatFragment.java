package com.dailystudio.apiaiandroidclient.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dailystudio.apiaiandroidclient.AppPrefs;
import com.dailystudio.apiaiandroidclient.ChatService;
import com.dailystudio.apiaiandroidclient.Constants;
import com.dailystudio.apiaiandroidclient.R;
import com.dailystudio.apiaiandroidclient.TextToSpeechService;
import com.dailystudio.apiaiandroidclient.database.ChatHistoryObject;
import com.dailystudio.apiaiandroidclient.loader.ChatHistoryLoader;
import com.dailystudio.apiaiandroidclient.loader.LoaderIds;
import com.dailystudio.apiaiandroidclient.ui.ChatHistoryObjectViewHolder;
import com.dailystudio.apiaiandroidclient.ui.ChatHistoryRecyclerViewAdapter;
import com.dailystudio.apiaicommon.database.AgentObject;
import com.dailystudio.app.fragment.*;
import com.dailystudio.development.Logger;

import java.util.List;

/**
 * Created by nanye on 17/3/31.
 */

public class ChatFragment
        extends AbsArrayRecyclerViewFragment<ChatHistoryObject, ChatHistoryObjectViewHolder> {

    private String mUser;
    private String mAgentId;

    private String mSession;

    private EditText mMessageEdit;
    private View mSendBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, null);

        setupViews(view);

        return view;
    }

    private void setupViews(View fragmentView) {
        if (fragmentView == null) {
            return;
        }

        mSendBtn = fragmentView.findViewById(R.id.send_btn);
        if (mSendBtn != null) {
            mSendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String message = getEditText(mMessageEdit);
                    if (TextUtils.isEmpty(message)) {
                        return;
                    }

                    final Context context = getContext();
                    if (context == null) {
                        return;
                    }

                    Intent apiSrvIntent = new Intent(Constants.ACTION_CHAT);

                    apiSrvIntent.setClass(context.getApplicationContext(),
                            ChatService.class);

                    apiSrvIntent.putExtra(Constants.EXTRA_AGENT_ID, mAgentId);
                    apiSrvIntent.putExtra(Constants.EXTRA_USER, mUser);
                    apiSrvIntent.putExtra(Constants.EXTRA_SESSION, mSession);
                    apiSrvIntent.putExtra(Constants.EXTRA_MESSAGES, message);

                    context.startService(apiSrvIntent);

                    if (mMessageEdit != null) {
                        mMessageEdit.setText(null);
                    }
                }

            });
        }

        mMessageEdit = (EditText) fragmentView.findViewById(R.id.message);
        if (mMessageEdit != null) {
            mMessageEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mSendBtn != null) {
                        mSendBtn.setEnabled((s != null && s.length() > 0));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });
        }
    }

    @Override
    public void bindIntent(Intent intent) {
        super.bindIntent(intent);
        if (intent == null) {
            return;
        }

        mUser = intent.getStringExtra(Constants.EXTRA_USER);
        if (TextUtils.isEmpty(mUser)) {
            mUser = Constants.DEFAULT_CHAT_USER;
            Logger.warn("user is empty, use default: %s",
                    mSession);
        }

        mAgentId = intent.getStringExtra(Constants.EXTRA_AGENT_ID);
        if (TextUtils.isEmpty(mAgentId)) {
            Logger.error("agent ID should not be empty");
        }

        mSession = intent.getStringExtra(Constants.EXTRA_SESSION);
        if (TextUtils.isEmpty(mSession)) {
            mSession = String.valueOf(System.currentTimeMillis());
            Logger.error("session is empty, create new: %s",
                    mSession);
        }

        Logger.debug("agent[%s, session: %s] chat for user: %s",
                mAgentId,
                mSession,
                mUser);

        checkAccessToken();
    }

    @Override
    public void onLoadFinished(Loader<List<ChatHistoryObject>> loader, List<ChatHistoryObject> data) {
        super.onLoadFinished(loader, data);
        if (data != null
                && data.size() > 0) {
            RecyclerView rv = getRecyclerView();
            if (rv != null) {
                Logger.debug("scroll to pos: %d", data.size());
                rv.smoothScrollToPosition(data.size());
            }
        }
    }

    @Override
    protected void onItemClick(View view, Object item) {
        super.onItemClick(view, item);

        Logger.debug("chat item: %s", item);
        if (item instanceof ChatHistoryObject == false) {
            return;
        }

    }

    @Override
    protected int getLoaderId() {
        return LoaderIds.LOADER_CHAT_HISTORY;
    }

    @Override
    protected Bundle createLoaderArguments() {
        return new Bundle();
    }

    @Override
    protected RecyclerView.Adapter onCreateAdapter() {
        return new ChatHistoryRecyclerViewAdapter(getActivity());
    }

    @Override
    protected RecyclerView.LayoutManager onCreateLayoutManager() {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setStackFromEnd(true);

        return lm;
    }

    @Override
    protected RecyclerView.ItemDecoration onCreateItemDecoration() {
        return null;
    }

    @Override
    public Loader<List<ChatHistoryObject>> onCreateLoader(int id, Bundle args) {
        return new ChatHistoryLoader(getActivity(), mAgentId, mUser);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        AppPrefs.getInstance().registerPrefChangesReceiver(
                context, mAppPrefsChangedReceiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        AppPrefs.getInstance().unregisterPrefChangesReceiver(
                getContext(), mAppPrefsChangedReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();

        TextToSpeechService.stop(getContext());
    }

    private String getEditText(EditText editText) {
        if (editText == null) {
            return null;
        }

        Editable editable = editText.getText();
        if (editable == null) {
            return null;
        }

        return editable.toString();
    }

    public void setAgentInfo(AgentObject data) {
        RecyclerView.Adapter adapter = getAdapter();
        if (adapter instanceof ChatHistoryRecyclerViewAdapter) {
            ((ChatHistoryRecyclerViewAdapter)adapter).setAgentInfo(data);
        }
    }

    private void checkAccessToken() {
        if (TextUtils.isEmpty(mAgentId)) {
            return;
        }

        boolean valid = false;
        if (!TextUtils.isEmpty(AppPrefs.getAgentAccessToken(
                getContext(), mAgentId))) {
            valid = true;
        }

        if (mMessageEdit != null) {
            mMessageEdit.setEnabled(valid);
        }
    }

    private BroadcastReceiver mAppPrefsChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }

            final String key = intent.getStringExtra(
                    AppPrefs.EXTRA_PREF_KEY);
            if (!TextUtils.isEmpty(key)
                    && key.contains("access-token-")) {
                checkAccessToken();
            }
        }
    };

}
