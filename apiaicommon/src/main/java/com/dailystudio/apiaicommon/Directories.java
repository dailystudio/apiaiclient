package com.dailystudio.apiaicommon;

import android.os.Environment;

import com.dailystudio.app.utils.FileUtils;

import java.io.File;

/**
 * Created by nanye on 17/4/27.
 */

public class Directories {

    private final static String TEMP_DIR = "temp";

    public static String getTempDir() {
        File sdcardRoot = Environment.getExternalStorageDirectory();
        if (sdcardRoot == null) {
            return null;
        }

        File analysisDir = new File(sdcardRoot, TEMP_DIR);

        return analysisDir.toString();
    }


    public static String getTempFilePath(String filename) {
        File sdcardRoot = Environment.getExternalStorageDirectory();
        if (sdcardRoot == null) {
            return null;
        }

        String tempDir = getTempDir();
        if (!FileUtils.isFileExisted(tempDir)) {
            FileUtils.checkOrCreateNoMediaDirectory(tempDir);
        }

        File tempFile = new File(tempDir, filename);

        return tempFile.toString();
    }

}
