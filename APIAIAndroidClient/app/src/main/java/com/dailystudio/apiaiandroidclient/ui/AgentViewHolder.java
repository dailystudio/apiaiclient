package com.dailystudio.apiaiandroidclient.ui;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.dailystudio.apiaicommon.database.AgentObject;
import com.dailystudio.apiaicommon.ui.AbsAgentViewHolder;

/**
 * Created by nanye on 17/5/2.
 */

public class AgentViewHolder extends AbsAgentViewHolder {

    public AgentViewHolder(View itemView, FragmentManager frgMgr) {
        super(itemView, frgMgr);
    }

    @Override
    protected void createAgentShortcut(Context context,
                                       AgentObject agentObject) {
        if (context == null || agentObject == null) {
            return;
        }
    }

}
