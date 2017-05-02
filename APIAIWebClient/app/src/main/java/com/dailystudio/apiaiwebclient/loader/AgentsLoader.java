package com.dailystudio.apiaiwebclient.loader;

import android.content.Context;

import com.dailystudio.apiaiwebclient.database.AgentObject;
import com.dailystudio.app.dataobject.loader.DatabaseObjectsLoader;

/**
 * Created by nanye on 17/5/2.
 */

public class AgentsLoader extends DatabaseObjectsLoader<AgentObject> {

    public AgentsLoader(Context context) {
        super(context);
    }

    @Override
    protected Class<AgentObject> getObjectClass() {
        return AgentObject.class;
    }

}
