package com.dailystudio.apiaiwebclient.ui;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.dailystudio.apiaicommon.ui.AbsAgentViewHolder;
import com.dailystudio.apiaicommon.ui.AbsAgentsAdapter;

/**
 * Created by nanye on 17/5/2.
 */

public class AgentsAdapter extends AbsAgentsAdapter {

    public AgentsAdapter(Context context, FragmentManager frgMgr) {
        super(context, frgMgr);
    }

    @Override
    protected AbsAgentViewHolder createViewHolder(View view, FragmentManager frgMgr) {
        return new AgentViewHolder(view, getFragmentManager());
    }

}
