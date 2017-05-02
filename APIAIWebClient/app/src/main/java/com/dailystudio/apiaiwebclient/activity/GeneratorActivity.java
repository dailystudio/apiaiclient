package com.dailystudio.apiaiwebclient.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.dailystudio.apiaiwebclient.Agent;
import com.dailystudio.apiaiwebclient.Constants;
import com.dailystudio.apiaiwebclient.R;
import com.dailystudio.development.Logger;

public class GeneratorActivity extends AppCompatActivity {

    private static class GenerateAgentShortcutAsyncTask extends AsyncTask<Context, Void, Void> {

        private String mAgentUrl;

        private GenerateAgentShortcutAsyncTask(String agentUrl) {
            mAgentUrl = agentUrl;
        }

        @Override
        protected Void doInBackground(Context... params) {
            if (params == null || params.length <= 0) {
                return null;
            }

            final Context context = params[0];

            if (TextUtils.isEmpty(mAgentUrl)) {
                return null;
            }

            Agent agent = new Agent(mAgentUrl);
            Logger.debug("agent = %s", agent);
            agent.resolve();

            final String name = agent.getName();
            final String iconUrl = agent.getIconUrl();
            if (TextUtils.isEmpty(name)
                    || TextUtils.isEmpty(iconUrl)) {
                return null;
            }

            Bitmap icon = Agent.getIconBitmap(iconUrl);

            Intent shortcutIntent = new Intent(Intent.ACTION_VIEW);
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            shortcutIntent.setData(Uri.parse(mAgentUrl));

            Intent addIntent = new Intent();
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, agent.getName());

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

            return null;
        }
    }

    private EditText mAgentUrlInput;
    private View mGenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generator);

        mGenButton = findViewById(R.id.btn_gen);
        if (mGenButton != null) {
            mGenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAgentUrlInput == null) {
                        return;
                    }

                    Editable editable = mAgentUrlInput.getText();
                    if (editable == null) {
                        return;
                    }

                    final String url = editable.toString();

                    new GenerateAgentShortcutAsyncTask(url).executeOnExecutor(
                            AsyncTask.THREAD_POOL_EXECUTOR, getApplicationContext());
                }
            });
        }

        mAgentUrlInput = (EditText) findViewById(R.id.agent_url);
        if (mAgentUrlInput != null) {
            mAgentUrlInput.setText(Constants.AGENT_URL_PREFIX);
        }
    }

}
