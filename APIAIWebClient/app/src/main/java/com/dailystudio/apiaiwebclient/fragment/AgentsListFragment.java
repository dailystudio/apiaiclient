package com.dailystudio.apiaiwebclient.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.dailystudio.apiaicommon.Agent;
import com.dailystudio.apiaicommon.database.AgentObject;
import com.dailystudio.apiaicommon.fragment.AbsAgentsListFragment;
import com.dailystudio.apiaiwebclient.ui.AgentsAdapter;

/**
 * Created by nanye on 17/5/2.
 */

public class AgentsListFragment extends AbsAgentsListFragment {

    @Override
    protected RecyclerView.Adapter onCreateAdapter() {
        return new AgentsAdapter(getActivity(), getFragmentManager());
    }

    @Override
    protected void launchAgent(Context context, AgentObject ao) {
        if (context == null || ao == null) {
            return;
        }

        final String url =
                Agent.agentIdToUrl(ao.getAgentId());
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Intent i = new Intent(Intent.ACTION_VIEW);

        i.setData(Uri.parse(url));

        startActivity(i);
    }

}
