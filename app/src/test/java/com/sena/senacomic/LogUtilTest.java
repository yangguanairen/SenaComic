package com.sena.senacomic;

import android.util.Log;

import com.sena.senacomic.util.AppConstants;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LogUtilTest {

    private static final String TAG = "jc";

    public static void PrintString(String s, Print print) {
        while (s.length() > 4) {
            print.print(TAG, s.substring(0, 4));
            System.out.println(s.substring(0, 4) + "\n");
            s = s.substring(4, s.length());
        }
        print.print(TAG, s);
        System.out.println(s);

    }


    public static void d(String s) {
        if (AppConstants.log_status) {
            PrintString(s, (a, b) -> Log.d(TAG, b));
        }
    }


    @Test
    public  void main() {
        List<String> list = new ArrayList<>();
        for (String s: list) {
            System.out.println(s);
        }
    }

    interface Print {
        public void print(String tag, String x);
    }
}
