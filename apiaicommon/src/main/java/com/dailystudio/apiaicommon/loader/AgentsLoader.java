package com.dailystudio.apiaicommon.loader;

import android.content.Context;

import com.dailystudio.apiaicommon.database.AgentObject;
import com.dailystudio.apiaicommon.database.AuthorityResolver;
import com.dailystudio.app.dataobject.loader.DatabaseObjectsLoader;
import com.dailystudio.dataobject.DatabaseObject;
import com.dailystudio.dataobject.database.DatabaseConnectivity;
import com.dailystudio.dataobject.query.OrderingToken;
import com.dailystudio.dataobject.query.Query;

/**
 * Created by nanye on 17/5/2.
 */

public class AgentsLoader extends DatabaseObjectsLoader<AgentObject> {

    public AgentsLoader(Context context) {
        super(context);
    }

    @Override
    protected Query getQuery(Class<AgentObject> klass) {
        Query query = super.getQuery(klass);
        if (query == null) {
            return query;
        }

        OrderingToken orderByToken =
                AgentObject.COLUMN_TIME.orderByDescending();

        query.setOrderBy(orderByToken);

        return query;
    }

    @Override
    protected Class<AgentObject> getObjectClass() {
        return AgentObject.class;
    }

    @Override
    protected DatabaseConnectivity getDatabaseConnectivity(Class<? extends DatabaseObject> objectClass) {
        return new DatabaseConnectivity(getContext(),
                AuthorityResolver.resolveAuthority(getContext()),
                objectClass);
    }

}
