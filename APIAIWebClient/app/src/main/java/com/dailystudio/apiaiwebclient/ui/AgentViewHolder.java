package com.dailystudio.apiaiwebclient.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dailystudio.apiaiwebclient.Constants;
import com.dailystudio.apiaiwebclient.R;
import com.dailystudio.apiaiwebclient.database.AgentObject;
import com.dailystudio.app.ui.AbsArrayItemViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by nanye on 17/5/2.
 */

public class AgentViewHolder extends AbsArrayItemViewHolder<AgentObject> {

    private TextView mNameView;
    private TextView mIdView;
    private ImageView mIconView;

    public AgentViewHolder(View itemView) {
        super(itemView);

        setupViews(itemView);
    }

    private void setupViews(View itemView) {
        if (itemView == null) {
            return;
        }

        mNameView = (TextView) itemView.findViewById(R.id.agent_name);
        mIdView = (TextView) itemView.findViewById(R.id.agent_id);
        mIconView = (ImageView) itemView.findViewById(R.id.agent_icon);
    }
    @Override
    public void bindItem(Context context, AgentObject agentObject) {
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
    }

}
