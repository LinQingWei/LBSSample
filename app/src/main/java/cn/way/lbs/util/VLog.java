package cn.way.lbs.util;

import android.text.TextUtils;
import android.util.Log;

public final class VLog {
    private static final String TAG = "LBS";

    private VLog() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void d(String string) {
        Log.d(TAG, string);
    }

    public static void d(String localTag, String msg) {
        Log.d(TAG, formatMessage(localTag, msg));
    }

    public static void i(String string) {
        Log.i(TAG, string);
    }

    public static void w(String string) {
        Log.w(TAG, string);
    }

    public static void w(String localTag, String string) {
        Log.w(TAG, formatMessage(localTag, string));
    }

    public static void e(String string) {
        Log.e(TAG, string);
    }

    public static void e(String string, Throwable tr) {
        Log.e(TAG, string, tr);
    }

    private static final String FORMAT_TAG_WITH_MSG = "%s: %s";

    private static String formatMessage(String tag, String msg) {
        if (TextUtils.isEmpty(tag)) {
            return msg;
        }

        return String.format(FORMAT_TAG_WITH_MSG, tag, msg);
    }
}
