package com.dailystudio.apiaicommon.database;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.dailystudio.apiaiwebclient.Agent;
import com.dailystudio.apiaiwebclient.Constants;
import com.dailystudio.development.Logger;

import java.util.List;

/**
 * Created by nanye on 17/4/28.
 */

public class ResolveAgentService extends IntentService {

    private final static String SRV_NAME = "resolve-agents";

    public ResolveAgentService() {
        super(SRV_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final String action = intent.getAction();
        if (Constants.ACTION_RESOLVE_AGENT.equals(action)) {
            final String agentId = intent.getStringExtra(Constants.EXTRA_AGENT_ID);
            if (TextUtils.isEmpty(agentId)) {
                return;
            }

            final String agentUrl = Agent.agentIdToUrl(agentId);

            Agent agent = new Agent(agentUrl);
            Logger.debug("resolve agent = %s", agent);
            agent.resolve();

            final String name = agent.getName();
            final String iconUrl = agent.getIconUrl();
            if (TextUtils.isEmpty(name)
                    || TextUtils.isEmpty(iconUrl)) {
                return;
            }

            AgentDatabaseModal.updateAgent(this, agentId, name, iconUrl);
        } else if (Constants.ACTION_RESOLVE_AGENTS.equals(action)) {
            List<AgentObject> unresolvedAgents =
                    AgentDatabaseModal.listUnresolvedAgents(getBaseContext());
            Logger.debug("unresolved agents: %s", unresolvedAgents);

            if (unresolvedAgents != null) {
                Intent i = new Intent(Constants.ACTION_RESOLVE_AGENT);
                i.setClass(getApplicationContext(),
                        ResolveAgentService.class);

                for (AgentObject ao : unresolvedAgents) {
                    i.putExtra(Constants.EXTRA_AGENT_ID, ao.getAgentId());

                    startService(i);
                }
            }
        }
    }
}
