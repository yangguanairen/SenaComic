package com.sena.senacomic.util;

import android.util.Log;

public class LogUtil {

    private static final String TAG = "jc";

    private static final int maxSize = 1024;

    /**
     *
     * @param s  //待输出的字符串
     * @param print  //以何种方式输出，例如Log.d(TAG, s)
     */
    private static void PrintString(String s, Print print) {
        while (s.length() > maxSize) {
            print.print(TAG, s.substring(0, maxSize));
            s = s.substring(maxSize, s.length());
        }
        print.print(TAG, s);
    }


    public static void d(String s) {
        if (AppConstants.log_status) {
            PrintString(s, (a, b) -> Log.d(TAG, b));
        }
    }

    public static void e(String s) {
        if (AppConstants.log_status) {
            PrintString(s, (a, b) -> Log.e(TAG, b));
        }
    }

    public static void i(String s) {
        if (AppConstants.log_status) {
            PrintString(s, (a, b) -> Log.i(TAG, b));
        }
    }

    public static void v(String s) {
        if (AppConstants.log_status) {
            PrintString(s, (a, b) -> Log.v(TAG, b));
        }
    }

    public static void w(String s) {
        if (AppConstants.log_status) {
            PrintString(s, (a, b) -> Log.w(TAG, b));
        }
    }


    // 函数式接口，有且仅有一个函数，可定义变量
    private interface Print {
        public void print(String tag, String s);
    }
}
