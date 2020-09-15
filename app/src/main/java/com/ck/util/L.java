package com.ck.util;

import android.util.Log;

import java.util.Locale;

/*定制日志工具*/
public class L {

    public static int level = L.DEBUG;

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;

    public static void v(String tag, String msg) {
        if (level <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (level <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (level <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (level <= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (level <= ERROR) {
            Log.e(tag, msg);
        }
    }

    public static void v(String msg) {
        if (level <= VERBOSE) {
            String[] s = getTrace();
            Log.v(s[0], s[1] + msg);
        }
    }

    public static void d(String msg) {
        if (level <= DEBUG) {
            String[] s = getTrace();
            Log.d(s[0], s[1] + msg);
        }
    }

    public static void i(String msg) {
        if (level <= INFO) {
            String[] s = getTrace();
            Log.i(s[0], s[1] + msg);
        }
    }

    public static void w(String msg) {
        if (level <= WARN) {
            String[] s = getTrace();
            Log.w(s[0], s[1] + msg);
        }
    }

    public static void e(String msg) {
        if (level <= ERROR) {
            String[] s = getTrace();
            Log.e(s[0], s[1] + msg);
        }
    }

    private static String[] getTrace() {
        StackTraceElement[] traces = new Throwable().fillInStackTrace().getStackTrace();
        String[] s = new String[2];
        for (int i = 2; i < traces.length; i++) {
            String className = traces[i].getClassName();
            if (!className.equals(L.class.getName())) {
                s[0] = className.substring(className.lastIndexOf('.') + 1);
                s[1] = String.format(Locale.US, "[%d] %s.%s(%s:%s) ", Thread.currentThread().getId(), traces[i].getClassName(), traces[i].getMethodName(), traces[i].getFileName(), traces[i].getLineNumber());
                return s;
            }
        }
        s[0] = "LOG_ERROR";
        s[1] = String.format(Locale.US, "[%d] %s.%s(%s:%s) ", Thread.currentThread().getId(), traces[0].getClassName(), traces[0].getMethodName(), traces[0].getFileName(), traces[0].getLineNumber());
        return s;
    }
}