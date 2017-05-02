package com.dailystudio.apiaiwebclient.ui;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dailystudio.apiaiwebclient.Constants;
import com.dailystudio.apiaiwebclient.R;
import com.dailystudio.apiaiwebclient.asynctask.GenerateShortcutAsyncTask;
import com.dailystudio.apiaiwebclient.database.AgentObject;
import com.dailystudio.apiaiwebclient.fragment.RemoveAgentDialogFragment;
import com.dailystudio.app.ui.AbsArrayItemViewHolder;
import com.dailystudio.development.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by nanye on 17/5/2.
 */

public class AgentViewHolder extends AbsArrayItemViewHolder<AgentObject> {

    private FragmentManager mFragmentManager;

    private TextView mNameView;
    private TextView mIdView;
    private ImageView mIconView;

    private View mBtnShortcut;
    private View mBtnRemove;

    public AgentViewHolder(View itemView, FragmentManager frgMgr) {
        super(itemView);

        mFragmentManager = frgMgr;

        setupViews(itemView);
    }

    private void setupViews(View itemView) {
        if (itemView == null) {
            return;
        }

        mNameView = (TextView) itemView.findViewById(R.id.agent_name);
        mIdView = (TextView) itemView.findViewById(R.id.agent_id);
        mIconView = (ImageView) itemView.findViewById(R.id.agent_icon);

        mBtnRemove = itemView.findViewById(R.id.btn_remove);
        mBtnShortcut = itemView.findViewById(R.id.btn_shortcut);
    }

    @Override
    public void bindItem(Context context, final AgentObject agentObject) {
        if (context == null
                || agentObject == null) {
            return;
        }

        if (mNameView != null) {
            mNameView.setText(agentObject.getName());
        }

        if (mIdView != null) {
            mIdView.setText(agentObject.getAgentId());
        }

        if (mIconView != null) {
            final String iconUrl = agentObject.getIconUrl();
            if (!TextUtils.isEmpty(iconUrl)) {
                ImageLoader.getInstance().displayImage(
                        iconUrl,
                        mIconView,
                        Constants.DEFAULT_IMAGE_LOADER_OPTIONS);
            }
        }

        if (mBtnRemove != null) {
            mBtnRemove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Logger.debug("remove agent: %s", agentObject);

                    if (v == null) {
                        return;
                    }

                    final Context context = v.getContext();
                    if (context == null) {
                        return;
                    }

                    if (mFragmentManager != null) {
                        Bundle args = new Bundle();

                        args.putString(Constants.EXTRA_AGENT_ID,
                                agentObject.getAgentId());
                        Fragment fragment = Fragment.instantiate(context,
                                RemoveAgentDialogFragment.class.getName(), args);

                        if (fragment instanceof RemoveAgentDialogFragment) {
                            FragmentTransaction ft =
                                    mFragmentManager.beginTransaction();
                            ((RemoveAgentDialogFragment)fragment).show(
                                    ft, "remove-agent");
                        }
                    }
                }

            });
        }

        if (mBtnShortcut != null) {
            mBtnShortcut.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Logger.debug("create shortcut for agent: %s", agentObject);

                    if (v != null) {
                        new GenerateShortcutAsyncTask(agentObject).executeOnExecutor(
                                AsyncTask.THREAD_POOL_EXECUTOR, v.getContext());
                    }
                }

            });
        }
    }

}
