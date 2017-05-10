package com.dailystudio.apiaiandroidclient.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dailystudio.apiaiandroidclient.AppPrefs;
import com.dailystudio.apiaiandroidclient.Constants;
import com.dailystudio.apiaiandroidclient.R;
import com.dailystudio.development.Logger;

public class EditAccessTokenDialogFragment extends DialogFragment {

    private String mAgentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAgentId = getArguments().getString(Constants.EXTRA_AGENT_ID);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_edit_access_token, null);
        Logger.debug("dialogView = %s", dialogView);
        if (dialogView != null) {
            TextView accessTokenView = (TextView) dialogView.findViewById(
                    R.id.edit_access_token);
            if (accessTokenView != null) {
                String accessToken = AppPrefs.getAgentAccessToken(
                        getContext(), mAgentId);
                if (!TextUtils.isEmpty(accessToken)) {
                    accessTokenView.setText(accessToken);
                    accessTokenView.setSelectAllOnFocus(true);
                }
            }
        }

        builder.setView(dialogView)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editText = (EditText) getDialog().findViewById(
                                R.id.edit_access_token);
                        if (editText != null) {
                            String accessToken = null;

                            Editable editable = editText.getText();
                            if (editable != null) {
                                accessToken = editable.toString();
                            }

                            updateAccessToken(getActivity(), accessToken);
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }

    public void updateAccessToken(Context context, String accessToken) {
        if (context == null) {
            return;
        }

        if (TextUtils.isEmpty(accessToken)) {
            accessToken = "";
        }

        AppPrefs.setAgentAccessToken(context, mAgentId, accessToken);
    }

    public static EditAccessTokenDialogFragment newInstance(String agentId) {
        EditAccessTokenDialogFragment fragment = new EditAccessTokenDialogFragment();

        Bundle args = new Bundle();

        if (!TextUtils.isEmpty(agentId)) {
            args.putString(Constants.EXTRA_AGENT_ID, agentId);
        }

        fragment.setArguments(args);

        return fragment;
    }

}
