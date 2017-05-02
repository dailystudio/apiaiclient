package com.dailystudio.apiaiwebclient.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.dailystudio.apiaiwebclient.Constants;
import com.dailystudio.apiaiwebclient.R;
import com.dailystudio.app.activity.ActionBarFragmentActivity;
import com.dailystudio.datetime.CalendarUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class GeneratorActivity extends ActionBarFragmentActivity {

    private EditText mAgentUrlInput;
    private View mGenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generator);

        mGenButton = findViewById(R.id.btn_add);
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
                }
            });
        }

        mAgentUrlInput = (EditText) findViewById(R.id.agent_url);
        if (mAgentUrlInput != null) {
        }
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
