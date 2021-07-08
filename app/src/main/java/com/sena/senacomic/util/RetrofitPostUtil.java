package com.sena.senacomic.util;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RetrofitPostUtil {

    private static RetrofitPostUtil instance = null;

    private Context mContext;

    public RetrofitPostUtil(Context context) {
        this.mContext = context;
    }

    public static RetrofitPostUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (RetrofitPostUtil.class) {
                instance = new RetrofitPostUtil(context);
            }
        }
        return instance;
    }

    public Map<String, RequestBody> generateRequestBody(String tagId, String status, String sort, String pageIndex) {
        Map<String, String> requestDataMap = new HashMap<>();
        requestDataMap.put("action", "getclasscomics");
        requestDataMap.put("pageindex", pageIndex);
        requestDataMap.put("pagesize", "21");
        requestDataMap.put("tagid", tagId);
        requestDataMap.put("status", status);
        requestDataMap.put("sort", sort);

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        for (String key: requestDataMap.keySet()) {
            RequestBody requestBody = RequestBody.create(requestDataMap.get(key) == null ? "" : requestDataMap.get(key),
                    MediaType.parse("multipart/form-data"));
            requestBodyMap.put(key, requestBody);
        }
        return requestBodyMap;
    }


    public Map<String, String> getFormMaildata(String tagId, String status, String sort, String pageIndex) {
        Map<String, String> formMaildata = new HashMap<>();
        formMaildata.put("action", "getclasscomics");
        formMaildata.put("pageindex", pageIndex);
        formMaildata.put("pagesize", "21");
        formMaildata.put("tagid", tagId);
        formMaildata.put("status", status);
        formMaildata.put("sort", sort);

        return formMaildata;


    }

}
