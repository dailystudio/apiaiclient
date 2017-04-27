package com.dailystudio.apiaiwebclient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.dailystudio.app.utils.FileUtils;
import com.dailystudio.datetime.CalendarUtils;
import com.dailystudio.development.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by nanye on 17/4/27.
 */

public class Agent {

    private final String ICON_TAG_ID = "agent-avatar";
    private final String NAME_TAG_CLASS = "b-agent-demo_header-agent-name";

    private final String ATTR_SRC = "src";

    private String mIconUrl;
    private String mName;
    private String mUrl;

    public Agent(String agentUrl) {
        mUrl = agentUrl;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public String getName() {
        return mName;
    }

    public void resolve() {
        if (TextUtils.isEmpty(mUrl)) {
            Logger.warn("agent url is empty. give up");
            return;
        }

        try {
            Document doc = Jsoup.connect(mUrl).get();
            if (doc != null) {
                Elements elements;

                elements = doc.select("#" + ICON_TAG_ID);
                if (elements != null) {
                    Element iconElement = elements.first();
                    if (iconElement != null) {
                        mIconUrl = iconElement.attr(ATTR_SRC);

                        Logger.debug("resolved iconUrl = %s", mIconUrl);
                    }
                }

                elements = doc.select("." + NAME_TAG_CLASS);
                if (elements != null) {
                    Element nameElement = elements.first();
                    if (nameElement != null) {
                        mName = nameElement.text();
                        Logger.debug("resolved name = %s", mName);
                    }
                }

            }
        } catch (IOException e) {
            Logger.error("could not resolve the icon, name of agent[url: %s]: %s",
                    mUrl,
                    e.toString());

            mIconUrl = null;
            mName = null;
        }
    }

    public static Bitmap getIconBitmap(String iconUrl) {
        if (TextUtils.isEmpty(iconUrl)) {
            return null;
        }

        final long now = System.currentTimeMillis();

        String iconFile = Directories.getTempFilePath(now + ".png");

        boolean ret = FileUtils.downloadFile(iconUrl, iconFile,
                (int)(10 * CalendarUtils.SECOND_IN_MILLIS),
                FileUtils.DOWNLOAD_READ_TIMEOUT);
        if (ret == false) {
            return null;
        }

        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeFile(iconFile);
        } catch (OutOfMemoryError e) {
            Logger.error("could not decode icon from [%s]: %s",
                    iconFile,
                    e.toString());

            bitmap = null;
        }

        return bitmap;
    }

    @Override
    public String toString() {
        return String.format("%s(0x%08x): url = %s, info [icon: %s, name: %s]",
                getClass().getSimpleName(),
                hashCode(),
                mUrl,
                mIconUrl,
                mName);
    }

}
