package com.dailystudio.apiaiandroidclient.activity;

import android.os.Bundle;

import com.dailystudio.apiaiandroidclient.R;
import com.dailystudio.app.activity.ActionBarFragmentActivity;

/**
 * Created by nanye on 16/9/21.
 */
public class SettingsActivity extends ActionBarFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        setupViews();
    }

    private void setupViews() {

    }

}
