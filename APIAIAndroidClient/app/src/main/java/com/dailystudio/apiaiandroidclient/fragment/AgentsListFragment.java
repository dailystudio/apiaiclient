package com.dailystudio.apiaiandroidclient.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.dailystudio.apiaiandroidclient.Constants;
import com.dailystudio.apiaiandroidclient.activity.ChatActivity;
import com.dailystudio.apiaiandroidclient.ui.AgentsAdapter;
import com.dailystudio.apiaicommon.Agent;
import com.dailystudio.apiaicommon.database.AgentObject;
import com.dailystudio.apiaicommon.fragment.AbsAgentsListFragment;
import com.dailystudio.development.Logger;

/**
 * Created by nanye on 17/5/2.
 */

public class AgentsListFragment extends AbsAgentsListFragment {

    @Override
    protected void launchAgent(Context context, AgentObject ao) {
        Logger.debug("launch ao: %s", ao);
        if (context == null || ao == null) {
            return;
        }

        final String url =
                Agent.agentIdToUrl(ao.getAgentId());
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Intent i = new Intent(Constants.ACTION_CHAT);

        i.setClass(context. getApplicationContext(),
                ChatActivity.class);

        i.putExtra(Constants.EXTRA_AGENT_ID, ao.getAgentId());
        i.putExtra(Constants.EXTRA_USER, Constants.DEFAULT_CHAT_USER);
        i.putExtra(Constants.EXTRA_SESSION,
                String.valueOf(System.currentTimeMillis()));

        startActivity(i);
    }

    @Override
    protected RecyclerView.Adapter onCreateAdapter() {
        return new AgentsAdapter(getActivity(), getFragmentManager());
    }

}
