package com.dailystudio.apiaicommon.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dailystudio.apiaicommon.Agent;
import com.dailystudio.apiaicommon.R;
import com.dailystudio.apiaicommon.database.AgentObject;
import com.dailystudio.apiaicommon.loader.AgentsLoader;
import com.dailystudio.apiaicommon.loader.LoaderIds;
import com.dailystudio.apiaicommon.ui.AbsAgentViewHolder;
import com.dailystudio.app.fragment.AbsArrayRecyclerViewFragment;

import java.util.List;

/**
 * Created by nanye on 17/5/2.
 */

public abstract class AbsAgentsListFragment extends AbsArrayRecyclerViewFragment<AgentObject, AbsAgentViewHolder> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agents_list, null);

        setupViews(view);

        return view;
    }

    private void setupViews(View fragmentView) {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLoaderId() {
        return LoaderIds.LOADER_AGENTS;
    }

    @Override
    protected Bundle createLoaderArguments() {
        return new Bundle();
    }

    @Override
    protected RecyclerView.LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    protected RecyclerView.ItemDecoration onCreateItemDecoration() {
        return null;
    }

    @Override
    public Loader<List<AgentObject>> onCreateLoader(int id, Bundle args) {
        return new AgentsLoader(getActivity());
    }

    @Override
    protected void onItemClick(View view, Object item) {
        if (item instanceof AgentObject == false) {
            return;
        }

        AgentObject ao = (AgentObject)item;

        final String url =
                Agent.agentIdToUrl(ao.getAgentId());
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Intent i = new Intent(Intent.ACTION_VIEW);

        i.setData(Uri.parse(url));

        startActivity(i);
    }

    protected abstract void launchAgent(AgentObject ao);

}
