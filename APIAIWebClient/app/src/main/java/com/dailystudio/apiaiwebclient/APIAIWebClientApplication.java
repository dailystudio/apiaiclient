package com.dailystudio.apiaiwebclient;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.dailystudio.apiaiwebclient.database.AgentDatabaseModal;
import com.dailystudio.apiaiwebclient.database.PredefinedAgents;
import com.dailystudio.app.DevBricksApplication;
import com.dailystudio.app.utils.FileUtils;
import com.dailystudio.development.Logger;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

/**
 * Created by nanye on 17/4/27.
 */

public class APIAIWebClientApplication extends DevBricksApplication {

    private static class ImportPredefinedAgentsAsyncTask extends AsyncTask<Context, Void, Context> {

        private final static String AGENTS_JSON = "predefined_agents.json";

        @Override
        protected Context doInBackground(Context... params) {
            if (params == null || params.length <= 0) {
                return null;
            }

            final Context context = params[0];
            if (context == null) {
                return null;
            }

            String jsonContent = null;
            try {
                jsonContent = FileUtils.getAssetFileContent(
                        context, AGENTS_JSON);
            } catch (IOException e) {
                Logger.warnning("could not load agents data from [%s]: %s",
                        AGENTS_JSON, e.toString());

                jsonContent = null;
            }

            if (TextUtils.isEmpty(jsonContent)) {
                return context;
            }

            Gson gson = new Gson();

            PredefinedAgents agents = null;
            try {
                agents = gson.fromJson(jsonContent, PredefinedAgents.class);
            } catch (JsonSyntaxException e) {
                Logger.debug("could not parse agents from jsonstr[%s]: %s",
                        jsonContent, e.toString());

                agents = null;
            }

            if (agents == null) {
                return context;
            }

            final long oldVer = AppPrefs.getPredefinedAgentsVersion(context);
            final long newVer = agents.version;
            Logger.debug("agents ver: old = %d, new = %d", oldVer, newVer);

            if (newVer > oldVer) {
                AgentDatabaseModal.clearAgents(context, true);

                if (agents.agentIds != null) {
                    for (String id: agents.agentIds) {
                        AgentDatabaseModal.addAgent(context,
                                id, true);
                    }
                }

                AppPrefs.setPredefinedAgentsVersion(context, newVer);
            }

            return context;
        }

        @Override
        protected void onPostExecute(Context context) {
            super.onPostExecute(context);

            if (context != null) {
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.USE_STETHO) {
            Stetho.initializeWithDefaults(this);
        }

        new ImportPredefinedAgentsAsyncTask()
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this);
    }

    @Override
    protected boolean isDebugBuild() {
        return BuildConfig.DEBUG;
    }

}
