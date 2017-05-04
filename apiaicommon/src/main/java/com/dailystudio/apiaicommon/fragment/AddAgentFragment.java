package com.dailystudio.apiaicommon.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dailystudio.apiaicommon.Constants;
import com.dailystudio.apiaicommon.R;
import com.dailystudio.apiaicommon.database.AgentDatabaseModal;
import com.dailystudio.apiaicommon.database.ResolveAgentService;
import com.dailystudio.app.fragment.BaseIntentFragment;
import com.dailystudio.development.Logger;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by nanye on 17/5/4.
 */

public class AddAgentFragment extends BaseIntentFragment {

    private EditText mAgentIdInput;
    private View mGenButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_agent, null);

        setupViews(view);

        return view;
    }

    private void setupViews(View fragmentView) {
        if (fragmentView == null) {
            return;
        }

        mGenButton = fragmentView.findViewById(R.id.btn_add);
        if (mGenButton != null) {
            mGenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v == null) {
                        return;
                    }

                    if (mAgentIdInput == null) {
                        return;
                    }

                    Editable editable = mAgentIdInput.getText();
                    if (editable == null) {
                        return;
                    }

                    createAgent(v.getContext(), editable.toString());

                    mAgentIdInput.setText(null);
                }
            });
        }

        mAgentIdInput = (EditText) fragmentView.findViewById(R.id.agent_id);
        if (mAgentIdInput != null) {
        }
    }


    private void createAgent(Context context, String agentId) {
        Logger.debug("create agent: id = %s", agentId);
        if (context == null || TextUtils.isEmpty(agentId)) {
            return;
        }

        AgentDatabaseModal.addAgent(context,
                agentId);

        Intent srvIntent = new Intent(Constants.ACTION_RESOLVE_AGENTS);

        srvIntent.setClass(context.getApplicationContext(),
                ResolveAgentService.class);

        context.startService(srvIntent);

        EventBus.getDefault().post(Constants.ActionEvent.AGENT_CREATED);
    }

}
