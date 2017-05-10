package com.dailystudio.apiaiandroidclient.asynctask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.dailystudio.apiaiandroidclient.Constants;
import com.dailystudio.apiaiandroidclient.R;
import com.dailystudio.apiaiandroidclient.activity.ChatActivity;
import com.dailystudio.apiaicommon.Agent;
import com.dailystudio.apiaicommon.database.AgentObject;
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
            Logger.warn("agentUrl is empty, skip");

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
            Logger.warn("name[%s] or iconUrl[%s] is empty, skip",
                    name, iconUrl);

            return null;
        }

        Bitmap icon = Agent.getIconBitmap(iconUrl);

        Intent shortcutIntent = new Intent(Constants.ACTION_CHAT);
        shortcutIntent.setClass(context.getApplicationContext(),
                ChatActivity.class);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shortcutIntent.putExtra(Constants.EXTRA_AGENT_ID, mAgentObject.getAgentId());
        shortcutIntent.putExtra(Constants.EXTRA_USER, Constants.DEFAULT_CHAT_USER);
        shortcutIntent.putExtra(Constants.EXTRA_SESSION,
                String.valueOf(System.currentTimeMillis()));
        Logger.debug("shortcut intent: %s", shortcutIntent);

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