package com.ck.util;

import android.util.Log;

import java.util.Locale;

/*定制日志工具*/
public class L {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static int level = VERBOSE;

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
            Log.v(getTag(), buildMessage(msg));
        }
    }

    public static void d(String msg) {
        if (level <= DEBUG) {
            Log.d(getTag(), buildMessage(msg));
        }
    }

    public static void i(String msg) {
        if (level <= INFO) {
            Log.i(getTag(), buildMessage(msg));
        }
    }

    public static void w(String msg) {
        if (level <= WARN) {
            Log.w(getTag(), buildMessage(msg));
        }
    }

    public static void e(String msg) {
        if (level <= ERROR) {
            Log.e(getTag(), buildMessage(msg));
        }
    }
    private StackTraceElement stackTraceElement=null;
    private StackTraceElement getStackTraceElement(){
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
        String callingClass = "";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(L.class)) {
                stackTraceElement = trace[i];
                break;
            }
        }
    }
    private static String getTag() {
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
        String callingClass = "";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(L.class)) {
                callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                break;
            }
        }
        return callingClass;
    }

    private static String buildMessage(String msg) {
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
        String caller = "";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(L.class)) {
                caller = trace[i].getClassName() + "." + trace[i].getMethodName() + "(" + trace[i].getFileName() + ":" + trace[i].getLineNumber() + ")";
                break;
            }
        }
        return String.format(Locale.US, "[%d] %s %s", Thread.currentThread().getId(), caller, msg);
    }
}