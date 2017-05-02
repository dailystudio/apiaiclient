package com.dailystudio.apiaiwebclient.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dailystudio.apiaiwebclient.R;
import com.dailystudio.apiaiwebclient.database.AgentObject;
import com.dailystudio.apiaiwebclient.loader.AgentsLoader;
import com.dailystudio.apiaiwebclient.loader.LoaderIds;
import com.dailystudio.apiaiwebclient.ui.AgentViewHolder;
import com.dailystudio.apiaiwebclient.ui.AgentsAdapter;
import com.dailystudio.app.fragment.AbsArrayRecyclerViewFragment;

import java.util.List;

/**
 * Created by nanye on 17/5/2.
 */

public class AgentsListFragment extends AbsArrayRecyclerViewFragment<AgentObject, AgentViewHolder> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, null);

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
    protected RecyclerView.Adapter onCreateAdapter() {
        return new AgentsAdapter(getActivity());
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

}
