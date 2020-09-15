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

    private static String TAG;
    private static String classInfo;

    public static void v(String msg) {
        if (level <= VERBOSE) {
            setStackTraceElement();
            Log.v(TAG, classInfo + msg);
        }
    }

    public static void d(String msg) {
        if (level <= DEBUG) {
            setStackTraceElement();
            Log.d(TAG, classInfo + msg);
        }
    }

    public static void i(String msg) {
        if (level <= INFO) {
            setStackTraceElement();
            Log.i(TAG, classInfo + msg);
        }
    }

    public static void w(String msg) {
        if (level <= WARN) {
            setStackTraceElement();
            Log.w(TAG, classInfo + msg);
        }
    }

    public static void e(String msg) {
        if (level <= ERROR) {
            setStackTraceElement();
            Log.e(TAG, classInfo + msg);
        }
    }

    private static void setStackTraceElement() {
        StackTraceElement[] traces = new Throwable().fillInStackTrace().getStackTrace();
        for (int i = 2; i < traces.length; i++) {
            String className = traces[i].getClassName();
            if (!className.equals(L.class.getName())) {
                TAG = className.substring(className.lastIndexOf('.') + 1);
                classInfo = String.format(Locale.US, "[%d] %s.%s(%s:%s) ", Thread.currentThread().getId(), traces[i].getClassName(), traces[i].getMethodName(), traces[i].getFileName(), traces[i].getLineNumber());
                break;
            }
        }
    }
}