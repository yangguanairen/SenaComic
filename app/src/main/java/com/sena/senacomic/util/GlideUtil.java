package com.sena.senacomic.util;

import android.content.Context;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;


public class GlideUtil {

    private static GlideUtil instance = null;
    private static Context mContext;

    private GlideUtil(Context context) {
        this.mContext = context;
    }

    public static GlideUtil getInstance(Context context) {

        if (instance == null) {
            synchronized (GlideUtil.class) {
                if (instance == null) {
                    instance = new GlideUtil(context);
                }
            }
        }
        return instance;
    }

    public GlideUrl addDMZJCookie(String url) {
        GlideUrl cookieUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Cookie", "UM_distinctid=17a40d20fc8ff8-01d6978396b31d-3373266-144000-17a40d20fc910b5")
                .addHeader("Cookie", "pt_198bb240=uid=EoHhUWApcrAB4n1M3u7hgQ&nid=0&vid=DxLf9RazWMIwz6eFso6E6g&vn=2&pvn=1&sact=1624943049895&to_flag=0&pl=J8gHIAMoYA2Eg1lD2m4zWQ*pt*1624943049895")
                .addHeader("Cookie", "ismy=1")
                .addHeader("Host", "images.dmzj.com")
                .addHeader("Referer", "https://m.dmzj.com/")
                .addHeader("Sec-Fetch-Dest","image")
                .build());
        return cookieUrl;
    }



}
