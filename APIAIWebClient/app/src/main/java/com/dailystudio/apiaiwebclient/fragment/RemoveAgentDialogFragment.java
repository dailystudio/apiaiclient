package com.dailystudio.apiaiwebclient.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.dailystudio.apiaiwebclient.Constants;
import com.dailystudio.apiaiwebclient.R;
import com.dailystudio.apiaiwebclient.database.AgentDatabaseModal;
import com.dailystudio.app.fragment.BaseIntentDialogFragment;
import com.dailystudio.development.Logger;

/**
 * Created by nanye on 16/9/29.
 */

public class RemoveAgentDialogFragment extends BaseIntentDialogFragment {

    private class RemoveAgentAsyncTask extends AsyncTask<Context, Void, Void> {

        private String mAgentId;

        private RemoveAgentAsyncTask(String agentId) {
            mAgentId = agentId;
        }

        @Override
        protected Void doInBackground(Context... params) {
            Logger.debug("removing agent: id = %s", mAgentId);
            if (params == null || params.length <= 0) {
                return null;
            }

            if (TextUtils.isEmpty(mAgentId)) {
                return null;
            }

            final Context context = params[0];

            AgentDatabaseModal.deleteAgent(context, mAgentId);

            return null;
        }
    }
    private String mAgentId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mAgentId = args.getString(Constants.EXTRA_AGENT_ID);

            Logger.debug("remove agent confirm: agentId = %s",
                    mAgentId);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_remove_title)
                .setMessage(R.string.dialog_remove_message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new RemoveAgentAsyncTask(mAgentId).execute(getContext());
                    }

                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

}