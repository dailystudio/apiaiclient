package com.dailystudio.apiaiwebclient.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.dailystudio.apiaicommon.database.AgentObject;
import com.dailystudio.apiaicommon.ui.AbsAgentViewHolder;
import com.dailystudio.apiaiwebclient.asynctask.GenerateShortcutAsyncTask;

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

        new GenerateShortcutAsyncTask(agentObject).executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR, context);
    }

}
