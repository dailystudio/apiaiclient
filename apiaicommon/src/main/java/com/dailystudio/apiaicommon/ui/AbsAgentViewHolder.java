package com.dailystudio.apiaicommon.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dailystudio.apiaicommon.Constants;
import com.dailystudio.apiaicommon.R;
import com.dailystudio.apiaicommon.database.AgentObject;
import com.dailystudio.apiaicommon.fragment.RemoveAgentDialogFragment;
import com.dailystudio.app.ui.AbsArrayItemViewHolder;
import com.dailystudio.development.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by nanye on 17/5/2.
 */

public abstract class AbsAgentViewHolder extends AbsArrayItemViewHolder<AgentObject> {

    private FragmentManager mFragmentManager;

    private TextView mNameView;
    private TextView mIdView;
    private ImageView mIconView;

    private View mBtnShortcut;
    private View mBtnRemove;

    public AbsAgentViewHolder(View itemView, FragmentManager frgMgr) {
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
            } else {
                mIconView.setImageResource(R.mipmap.ic_launcher);
            }
        }

        if (mBtnRemove != null) {
            if (agentObject.isPredefined()) {
                mBtnRemove.setVisibility(View.GONE);
                mBtnRemove.setOnClickListener(null);
            } else {
                mBtnRemove.setVisibility(View.VISIBLE);
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

                            args.putInt(Constants.EXTRA_ID,
                                    agentObject.getId());
                            Fragment fragment = Fragment.instantiate(context,
                                    RemoveAgentDialogFragment.class.getName(), args);

                            if (fragment instanceof RemoveAgentDialogFragment) {
                                FragmentTransaction ft =
                                        mFragmentManager.beginTransaction();
                                ((RemoveAgentDialogFragment) fragment).show(
                                        ft, "remove-agent");
                            }
                        }
                    }

                });
            }
        }

        if (mBtnShortcut != null) {
            mBtnShortcut.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Logger.debug("create shortcut for agent: %s", agentObject);

                    if (v != null) {
                        EventBus.getDefault().post(Constants.ActionEvent.CREATING_SHORTCUT);
                        createAgentShortcut(v.getContext(), agentObject);
                    }
                }

            });
        }
    }

    abstract protected void createAgentShortcut(Context context,
                                                AgentObject agentObject);

}
