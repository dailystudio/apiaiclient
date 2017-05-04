package com.dailystudio.apiaicommon.database;

import android.content.Context;

public class AuthorityResolver {

    private final static String HELP_AUTHORITY_SUFFIX = ".agent";

    public static String resolveAuthority(Context context) {
        if (context == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(HELP_AUTHORITY_SUFFIX);

        builder.insert(0, context.getPackageName());

        return builder.toString();
    }

}
