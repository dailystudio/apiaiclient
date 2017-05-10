package com.dailystudio.apiaiandroidclient.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.dailystudio.apiaiandroidclient.AppPrefs;
import com.dailystudio.apiaiandroidclient.Constants;
import com.dailystudio.apiaiandroidclient.R;
import com.dailystudio.apiaiandroidclient.fragment.AboutFragment;
import com.dailystudio.apiaiandroidclient.fragment.ChatFragment;
import com.dailystudio.apiaiandroidclient.fragment.EditAccessTokenDialogFragment;
import com.dailystudio.apiaiandroidclient.loader.AgentLoader;
import com.dailystudio.apiaiandroidclient.loader.LoaderIds;
import com.dailystudio.apiaicommon.database.AgentObject;
import com.dailystudio.app.activity.AbsLoaderActionBarFragmentActivity;
import com.dailystudio.app.utils.ActivityLauncher;

public class ChatActivity extends AbsLoaderActionBarFragmentActivity<AgentObject> {

    private String mAgentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_access_token) {
            DialogFragment fragment =
                    EditAccessTokenDialogFragment.newInstance(mAgentId);
            if (fragment != null) {
                fragment.show(getSupportFragmentManager(), "edit-access-token");
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void bindIntent(Intent intent) {
        super.bindIntent(intent);
        if (intent == null) {
            return;
        }

        mAgentId = intent.getStringExtra(Constants.EXTRA_AGENT_ID);

        restartLoader();

        checkAccessToken();
    }

    @Override
    protected int getLoaderId() {
        return LoaderIds.LOADER_AGENT;
    }

    @Override
    protected Bundle createLoaderArguments() {
        return new Bundle();
    }

    @Override
    public Loader<AgentObject> onCreateLoader(int id, Bundle args) {
        return new AgentLoader(this, mAgentId);
    }

    @Override
    public void onLoadFinished(Loader<AgentObject> loader, AgentObject data) {
        if (data == null) {
            return;
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(data.getName());
        }

        Fragment fragment = findFragment(R.id.fragment_chat);
        if (fragment instanceof ChatFragment) {
            ((ChatFragment)fragment).setAgentInfo(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<AgentObject> loader) {

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        AppPrefs.getInstance().registerPrefChangesReceiver(
                this, mAppPrefsChangedReceiver);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        AppPrefs.getInstance().unregisterPrefChangesReceiver(
                this, mAppPrefsChangedReceiver);
    }

    private void checkAccessToken() {
        if (TextUtils.isEmpty(mAgentId)) {
            return;
        }

        boolean valid = false;
        if (!TextUtils.isEmpty(AppPrefs.getAgentAccessToken(
                this, mAgentId))) {
            valid = true;
        }

        if (valid) {
            hidePrompt();
        } else {
            showPrompt(getString(R.string.prompt_access_token_invalid));
        }
    }


    private BroadcastReceiver mAppPrefsChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }

            final String key = intent.getStringExtra(
                    AppPrefs.EXTRA_PREF_KEY);
            if (!TextUtils.isEmpty(key)
                    && key.contains("access-token-")) {
                checkAccessToken();
            }
        }
    };

}
