package com.sena.senacomic.util;

import android.content.Context;
import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupUtil {

    private Context mContext;
    public static JsoupUtil instance = null;

    private JsoupUtil(Context context) {
        this.mContext = context;
    }

    public static JsoupUtil getInstance(Context context) {

        if (instance == null) {
            synchronized (JsoupUtil.class) {
                if (instance == null) {
                    instance = new JsoupUtil(context);
                }
            }
        }

        return instance;
    }

    public String getDMZJGsonData(Elements elements, String flag) {
        String data = "";
        for (Element e: elements) {
            if (e.toString().contains(flag)) {
                data = e.toString();
                break;
            }
        }
        String gsonData = "";
        for (String s: data.split("\n")) {
            if (s.contains(flag)) {
                gsonData = s;
                break;
            }
        }
        int startIndex = gsonData.indexOf("{");
        int endIndex = gsonData.lastIndexOf("}");
        gsonData = gsonData.substring(startIndex, endIndex+1);
        Log.d("jc", "gsonData: " + gsonData);

        return gsonData;

    }
}
