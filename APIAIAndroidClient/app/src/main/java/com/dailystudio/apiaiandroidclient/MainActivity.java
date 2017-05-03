package com.dailystudio.apiaiandroidclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dailystudio.app.activity.ActionBarFragmentActivity;
import com.dailystudio.development.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Map;

import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.android.GsonFactory;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.ResponseMessage;
import ai.api.model.Result;

public class MainActivity extends ActionBarFragmentActivity {

    private AIService aiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViewApiAi();
    }

    private void setupViewApiAi() {
        final AIConfiguration config = new AIConfiguration("6b95beccba2349a4b91d3f5838e9bfc5",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        findViewById(R.id.talk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AIDataService aiDataService =
                        new AIDataService(getApplicationContext(), config);

                final AIRequest aiRequest = new AIRequest();
                aiRequest.setQuery("I have sth to tell you");

                Logger.debug("aiRequest: %s", aiRequest);
                new AsyncTask<AIRequest, Void, AIResponse>() {
                    @Override
                    protected AIResponse doInBackground(AIRequest... requests) {
                        final AIRequest request = requests[0];
                        try {
                            Logger.debug("send request: %s", request);
                            final AIResponse response =
                                    aiDataService.request(request);
                            Logger.debug("response: %s", response);

                            return response;
                        } catch (AIServiceException e) {
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(AIResponse aiResponse) {
                        if (aiResponse != null) {
                            Result result = aiResponse.getResult();

                            // Get parameters
                            String parameterString = "";
                            if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                                for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                                    parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                                }
                            }

                            Logger.debug("Original Gson: %s",
                                    GsonFactory.getGson().toJson(aiResponse));
                            // Show results in TextView.
                            Logger.debug("Query:" + result.getResolvedQuery() +
                                    "\nAction: " + result.getAction() +
                                    "\nParameters: " + parameterString +
                                    "\nFulfillment: "
                                    + ((ResponseMessage.ResponseSpeech)result.getFulfillment().getMessages().get(0)).getSpeech());
                        }
                    }
                }.execute(aiRequest);
            }
        });
    }

}
