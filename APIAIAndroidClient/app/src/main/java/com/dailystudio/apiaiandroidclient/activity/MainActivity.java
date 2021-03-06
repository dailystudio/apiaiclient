package com.dailystudio.apiaiandroidclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.dailystudio.apiaiandroidclient.R;
import com.dailystudio.apiaiandroidclient.fragment.AboutFragment;
import com.dailystudio.apiaicommon.Constants;
import com.dailystudio.app.activity.ActionBarFragmentActivity;
import com.dailystudio.app.utils.ActivityLauncher;
import com.dailystudio.datetime.CalendarUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends ActionBarFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            AboutFragment fragment = new AboutFragment();

            fragment.show(getSupportFragmentManager(), "about");

            return true;
        } else if (id == R.id.action_settings) {
            Intent i = new Intent();

            i.setClass(getApplicationContext(),
                    SettingsActivity.class);

            ActivityLauncher.launchActivity(this, i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Constants.ActionEvent event) {
        int resId = -1;

        switch (event) {
            case CREATING_SHORTCUT:
                resId = R.string.prompt_creating_shortcut;
                break;

            case SHORTCUT_CREATED:
                resId = R.string.prompt_shortcut_created;
                break;

            case AGENT_CREATED:
                resId = R.string.prompt_agent_created;
                break;

            case AGENT_REMOVED:
                resId = R.string.prompt_agent_removed;
                break;
        }

        if (resId > 0) {
            showPrompt(getString(resId));

            mHandler.removeCallbacks(mHidePromptRunnable);
            mHandler.postDelayed(mHidePromptRunnable,
                    CalendarUtils.SECOND_IN_MILLIS * 2);
        }
    }

    private Runnable mHidePromptRunnable = new Runnable() {
        @Override
        public void run() {
            hidePrompt();
        }
    };

    private Handler mHandler = new Handler();

}
