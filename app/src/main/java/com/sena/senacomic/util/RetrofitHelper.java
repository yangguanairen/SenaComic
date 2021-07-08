package com.sena.senacomic.util;

import android.content.Context;
import android.util.Log;

import com.sena.senacomic.RetrofitService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private String TAG = "jc";

    private static RetrofitHelper mKuaiKanInstance = null;
    private static RetrofitHelper mDMZJInstance = null;
    private static RetrofitHelper mMangabzInstance = null;

    private Context mContext;
    private Retrofit mRetrofit = null;

    private RetrofitHelper(Context context, String url) {
        this.mContext = context;
        switch (url) {
            case AppConstants.KUAI_KAN_URL: setKuaiKanRetrofit(url); break;
            case AppConstants.DMZJ_URL:
            case AppConstants.MANGABZ_URL:
                setCustomRetrofit(url); break;
        }
    }


    public static RetrofitHelper getKuaiKanInstance(Context context) {
        if (mKuaiKanInstance == null) {
            synchronized (RetrofitHelper.class) {
                if (mKuaiKanInstance == null) {
                    mKuaiKanInstance = new RetrofitHelper(context, AppConstants.KUAI_KAN_URL);
                }
            }
        }
        return mKuaiKanInstance;
    }

    public static RetrofitHelper getDMZJInstance(Context context) {
        if (mDMZJInstance == null) {
            synchronized (RetrofitHelper.class) {
                if (mDMZJInstance == null) {
                    mDMZJInstance = new RetrofitHelper(context, AppConstants.DMZJ_URL);
                }
            }
        }
        return mDMZJInstance;
    }

    public static RetrofitHelper getMangabzInstance(Context context) {
        if (mMangabzInstance == null) {
            synchronized (RetrofitHelper.class) {
                if (mMangabzInstance == null) {
                    mMangabzInstance = new RetrofitHelper(context, AppConstants.MANGABZ_URL);
                }
            }
        }

        return mMangabzInstance;
    }

    private void setKuaiKanRetrofit(String url) {

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(5, TimeUnit.SECONDS);
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {

                Request.Builder builder1 = chain.request().newBuilder();
                builder1.addHeader("cookie", "resolution=2436.000096797943*1125.0000447034836; gdt_fp=2f4b84263be3e5a601ba5bac3a8d6462; nickname=X%2596%25u4E61%25uC362%25uD191%25uC856%25u6C70%25D9%25E6%25B8%25BD%25B9%25AD; Hm_lvt_c826b0776d05b85d834c5936296dc1d5=1623845158,1623976479,1624596309,1624621871; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221184476640%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%2217a14b6ba81848-07cff42a47ca0e-3373266-1327104-17a14b6ba821061%22%7D; Hm_lvt_9c2447212ff4c0a9545bad8502b66cc6=1624587805,1624608161,1624612834,1624670648; passToken=v1-GAgAAAAAAAAGrhggh8UNF3ZA-fCxWbm0Te_uv9S3CIm1hRZh1FEG9YsETVkA; uid=1184476640; Hm_lpvt_9c2447212ff4c0a9545bad8502b66cc6=1624671077; kk_s_t=1624671082596");
//                builder1.addHeader("cookie", "resolution=2436.000096797943*1125.0000447034836");
//                builder1.addHeader("cookie", "gdt_fp=2f4b84263be3e5a601ba5bac3a8d6462");
//                builder1.addHeader("cookie", "nickname=X%2596%25u4E61%25uC362%25uD191%25uC856%25u6C70%25D9%25E6%25B8%25BD%25B9%25AD");
//                builder1.addHeader("cookie", "passToken=v1-GAgAAAAAAAAGqhggUebknwBzlxCesN5imvbP3jzsAZxfO-jhX4KmY_9_fTsA");
//                builder1.addHeader("cookie", "uid=1184476640");
//                builder1.addHeader("cookie", "Hm_lvt_9c2447212ff4c0a9545bad8502b66cc6=1623976515,1624587805,1624608161,1624612834");
//                builder1.addHeader("cookie", "Hm_lvt_c826b0776d05b85d834c5936296dc1d5=1623845158,1623976479,1624596309,1624621871");
//                builder1.addHeader("cookie", "Hm_lpvt_c826b0776d05b85d834c5936296dc1d5=1624621917");
//                builder1.addHeader("cookie", "sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221184476640%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%2217a14b6ba81848-07cff42a47ca0e-3373266-1327104-17a14b6ba821061%22%7D");
//                builder1.addHeader("cookie", "kk_s_t=1624628489631");
//                builder1.addHeader("cookie", "Hm_lpvt_9c2447212ff4c0a9545bad8502b66cc6=1624628491");

                Request request = builder1.build();
                return chain.proceed(request);
            }
        });

        mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }


    private void setCustomRetrofit(String url) {
        Log.d(TAG, "setCustomRetrofit: " + url);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    public RetrofitService getServer() {
        return mRetrofit.create(RetrofitService.class);
    }


}
