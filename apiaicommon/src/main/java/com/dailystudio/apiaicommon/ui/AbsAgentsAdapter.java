package com.dailystudio.apiaicommon.ui;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;

import com.dailystudio.apiaicommon.R;
import com.dailystudio.apiaicommon.database.AgentObject;
import com.dailystudio.app.ui.AbsArrayRecyclerAdapter;

/**
 * Created by nanye on 17/5/2.
 */

public abstract class AbsAgentsAdapter extends AbsArrayRecyclerAdapter<AgentObject, AbsAgentViewHolder> {

    private FragmentManager mFragmentManager;

    public AbsAgentsAdapter(Context context, FragmentManager frgMgr) {
        super(context);

        mFragmentManager = frgMgr;
    }

    @Override
    public AbsAgentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.layout_agent, null);

        return createViewHolder(view, mFragmentManager);
    }

    protected FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    abstract protected AbsAgentViewHolder createViewHolder(View view,
                                                           FragmentManager frgMgr);

}
