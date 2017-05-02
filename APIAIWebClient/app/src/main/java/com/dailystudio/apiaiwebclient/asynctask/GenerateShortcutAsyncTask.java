package com.dailystudio.apiaiwebclient.asynctask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.dailystudio.apiaiwebclient.Agent;
import com.dailystudio.apiaiwebclient.Constants;
import com.dailystudio.apiaiwebclient.R;
import com.dailystudio.apiaiwebclient.database.AgentObject;
import com.dailystudio.development.Logger;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by nanye on 17/5/2.
 */

public class GenerateShortcutAsyncTask extends AsyncTask<Context, Void, Void> {

    private AgentObject mAgentObject;

    public GenerateShortcutAsyncTask(AgentObject agentObject) {
        mAgentObject = agentObject;
    }

    @Override
    protected Void doInBackground(Context... params) {
        if (params == null || params.length <= 0) {
            return null;
        }

        final Context context = params[0];

        if (mAgentObject == null) {
            return null;
        }

        final String url = Agent.agentIdToUrl(mAgentObject.getAgentId());
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        String name = mAgentObject.getName();
        String iconUrl = mAgentObject.getIconUrl();

        if (TextUtils.isEmpty(name)
                || TextUtils.isEmpty(iconUrl)) {
            Agent agent = mAgentObject.convertToAgent();
            Logger.debug("resolve agent = %s", agent);
            agent.resolve();

            name = agent.getName();
            iconUrl = agent.getIconUrl();
        }

        if (TextUtils.isEmpty(name)
                || TextUtils.isEmpty(iconUrl)) {
            return null;
        }

        Bitmap icon = Agent.getIconBitmap(iconUrl);

        Intent shortcutIntent = new Intent(Intent.ACTION_VIEW);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        shortcutIntent.setData(Uri.parse(url));

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        if (icon != null) {
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);
        } else {
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(
                            context.getApplicationContext(),
                            R.mipmap.ic_launcher));
        }

        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);

        EventBus.getDefault().post(Constants.ActionEvent.SHORTCUT_CREATED);

        return null;
    }

}