package com.dailystudio.apiaiandroidclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;

import com.dailystudio.apiaiandroidclient.Constants;
import com.dailystudio.apiaiandroidclient.R;
import com.dailystudio.apiaiandroidclient.fragment.ChatFragment;
import com.dailystudio.apiaiandroidclient.loader.AgentLoader;
import com.dailystudio.apiaiandroidclient.loader.LoaderIds;
import com.dailystudio.apiaicommon.database.AgentObject;
import com.dailystudio.app.activity.AbsLoaderActionBarFragmentActivity;

public class ChatActivity extends AbsLoaderActionBarFragmentActivity<AgentObject> {

    private String mAgentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
    }

    @Override
    protected void bindIntent(Intent intent) {
        super.bindIntent(intent);
        if (intent == null) {
            return;
        }

        mAgentId = intent.getStringExtra(Constants.EXTRA_AGENT_ID);

        restartLoader();
    }

    @Override
    protected int getLoaderId() {
        return LoaderIds.LOADER_AGENT;
    }

    @Override
    protected Bundle createLoaderArguments() {
        return new Bundle();
    }

    @Override
    public Loader<AgentObject> onCreateLoader(int id, Bundle args) {
        return new AgentLoader(this, mAgentId);
    }

    @Override
    public void onLoadFinished(Loader<AgentObject> loader, AgentObject data) {
        if (data == null) {
            return;
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(data.getName());
        }

        Fragment fragment = findFragment(R.id.fragment_chat);
        if (fragment instanceof ChatFragment) {
            ((ChatFragment)fragment).setAgentInfo(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<AgentObject> loader) {

    }

}
