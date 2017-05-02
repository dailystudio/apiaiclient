package com.dailystudio.apiaiwebclient.database;

import android.content.Context;
import android.text.TextUtils;

import com.dailystudio.dataobject.query.ExpressionToken;
import com.dailystudio.dataobject.query.Query;
import com.dailystudio.datetime.dataobject.TimeCapsuleDatabaseReader;
import com.dailystudio.datetime.dataobject.TimeCapsuleDatabaseWriter;
import com.dailystudio.development.Logger;

import java.util.List;

/**
 * Created by nanye on 16/9/17.
 */
public class AgentDatabaseModal {

    public static AgentObject addAgent(Context context, String agentUrl) {
        return addAgent(context, agentUrl, false);
    }

    public static AgentObject addAgent(Context context, String agentUrl, boolean predefined) {
        if (TextUtils.isEmpty(agentUrl)) {
            return null;
        }

        TimeCapsuleDatabaseWriter<AgentObject> writer =
                new TimeCapsuleDatabaseWriter<>(context, AgentObject.class);

        long now = System.currentTimeMillis();

        AgentObject agentObject = new AgentObject(context);

        agentObject.setTime(now);
        agentObject.setAgentId(agentUrl);
        agentObject.setPredefined(predefined);

        writer.insert(agentObject);

        return agentObject;
    }

    public static void updateAgent(Context context, String agentId, String name, String iconUrl) {
        if (TextUtils.isEmpty(agentId)) {
            return;
        }

        AgentObject agentObject = findAgent(context, agentId);
        if (agentObject == null) {
            Logger.warn("updating agent(%s) is NOT existed", agentId);

            return;
        }

        TimeCapsuleDatabaseWriter<AgentObject> writer =
                new TimeCapsuleDatabaseWriter<>(context, AgentObject.class);

        long now = System.currentTimeMillis();

        agentObject.setTime(now);
        agentObject.setName(name);
        agentObject.setIconUrl(iconUrl);

        writer.update(agentObject);
    }

    public static void clearAgents(Context context, boolean onlyPredefined) {
        TimeCapsuleDatabaseWriter<AgentObject> writer =
                new TimeCapsuleDatabaseWriter<>(context,
                        AgentObject.class);

        Query query = new Query(AgentObject.class);
        if (onlyPredefined) {
            ExpressionToken selToken =
                    AgentObject.COLUMN_PREDEFINED.eq(1);

            if (selToken == null) {
                return;
            }

            query.setSelection(selToken);
        }

        writer.delete(query);
    }

    public static AgentObject findAgent(Context context,
                                        String agentUrl) {
        if (context == null || TextUtils.isEmpty(agentUrl)) {
            return null;
        }

        TimeCapsuleDatabaseReader<AgentObject> reader =
                new TimeCapsuleDatabaseReader<>(context,
                        AgentObject.class);

        Query query = reader.getQuery();

        ExpressionToken selToken =
                AgentObject.COLUMN_AGENT_ID.eq(agentUrl);
        if (selToken == null) {
            return null;
        }

        query.setSelection(selToken);

        return reader.queryLastOne(query);
    }

    public static List<AgentObject> listAgents(Context context, boolean onlyPredefined) {
        if (context == null) {
            return null;
        }

        TimeCapsuleDatabaseReader<AgentObject> reader =
                new TimeCapsuleDatabaseReader<>(context,
                        AgentObject.class);

        Query query = new Query(AgentObject.class);
        if (onlyPredefined) {
            ExpressionToken selToken =
                    AgentObject.COLUMN_PREDEFINED.eq(1);

            if (selToken != null) {
                query.setSelection(selToken);
            }
        }

        return reader.query(query);
    }

    public static List<AgentObject> listUnresolvedAgents(Context context) {
        if (context == null) {
            return null;
        }

        TimeCapsuleDatabaseReader<AgentObject> reader =
                new TimeCapsuleDatabaseReader<>(context,
                        AgentObject.class);

        Query query = new Query(AgentObject.class);

        ExpressionToken selToken =
                AgentObject.COLUMN_ICON_URL.isNull();

        if (selToken != null) {
            query.setSelection(selToken);
        }

        return reader.query(query);
    }

}
