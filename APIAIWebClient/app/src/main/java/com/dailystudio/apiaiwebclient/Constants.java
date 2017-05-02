package com.dailystudio.apiaiwebclient;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by nanye on 17/4/27.
 */

public class Constants {

    public final static String AGENT_URL_PREFIX =
            "https://console.api.ai/api-client/demo/embedded/";

    public final static String ACTION_RESOLVE_AGENT =
            "apiaiwebclient.intent.ACTION_RESOLVE_AGENT";

    public final static String ACTION_RESOLVE_AGENTS =
            "apiaiwebclient.intent.ACTION_RESOLVE_AGENTS";

    public final static String EXTRA_AGENT_ID =
            "apiaiwebclient.intent.EXTRA_AGENT_ID";

    public final static DisplayImageOptions DEFAULT_IMAGE_LOADER_OPTIONS =
            new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .showImageOnLoading(null)
                    .showImageOnLoading(R.mipmap.ic_launcher)
                    .showImageOnFail(R.mipmap.ic_launcher)
                    .resetViewBeforeLoading(true)
                    .build();

}
