package com.dailystudio.apiaiwebclient.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.dailystudio.apiaiwebclient.R;

public class GeneratorActivity extends AppCompatActivity {

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

}
