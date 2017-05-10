package com.dailystudio.apiaiandroidclient.loader;

import android.content.Context;
import android.text.TextUtils;

import com.dailystudio.apiaicommon.database.AgentDatabaseModal;
import com.dailystudio.apiaicommon.database.AgentObject;
import com.dailystudio.app.loader.AbsAsyncDataLoader;

/**
 * Created by nanye on 17/5/10.
 */

public class AgentLoader extends AbsAsyncDataLoader<AgentObject> {

    private String mAgentId;

    public AgentLoader(Context context, String agentId) {
        super(context);

        mAgentId = agentId;
    }

    @Override
    public AgentObject loadInBackground() {
        if (TextUtils.isEmpty(mAgentId)) {
            return null;
        }

        return AgentDatabaseModal.findAgent(getContext(), mAgentId);
    }

}
