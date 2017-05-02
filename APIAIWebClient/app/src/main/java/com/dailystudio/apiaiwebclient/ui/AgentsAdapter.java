package com.dailystudio.apiaiwebclient.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.dailystudio.apiaiwebclient.R;
import com.dailystudio.apiaiwebclient.database.AgentObject;
import com.dailystudio.app.ui.AbsArrayRecyclerAdapter;

/**
 * Created by nanye on 17/5/2.
 */

public class AgentsAdapter extends AbsArrayRecyclerAdapter<AgentObject, AgentViewHolder> {

    public AgentsAdapter(Context context) {
        super(context);
    }

    @Override
    public AgentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.layout_agent, null);

        return new AgentViewHolder(view);
    }

}
