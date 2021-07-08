package com.sena.senacomic.mangabz.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sena.senacomic.R;
import com.sena.senacomic.databinding.ActivityMangabzViewBinding;
import com.sena.senacomic.mangabz.adapter.MangabzViewAdapter;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class MangabzViewActivity extends AppCompatActivity {

    private String TAG = "jc";


    private ActivityMangabzViewBinding binding;
    private MangabzViewAdapter adapter;

    private String chapterId;

    private List<String> list = new ArrayList<String>();

    private WebView webView;
    private String html = null;
//    private Observable<List<String>> observable;
//    private Consumer<List<String>> consumer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMangabzViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chapterId = getIntent().getStringExtra(AppConstants.CHAPTER_ID);
//        binding.recyclerViewView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new MangabzViewAdapter(this);
//        binding.recyclerViewView.setAdapter(adapter);
//        initRXJava();


        webView = binding.webView;
        // 启用js
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageFinished(WebView view, String url) {
                // 获取js并执行
                webView.evaluateJavascript("document.getElementsByTagName('html')[0].innerHTML;",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                // 去除头尾双引号，去除转移字符\u003C和\"
                                value = value.substring(1, value.length()-1).replace("\\u", "sena")
                                        .replace("sena003C", "<").replace("\\\"", "\"");
                                html =value;
//                                销毁webview，订阅关系建立，加载自己的视图
//                                webView.destroy();
//                                observable.subscribeOn(Schedulers.io())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe(consumer);
                            }
                        });
                super.onPageFinished(view, url);
            }
        });
        LogUtil.d("MangabzViewUrl: " + getString(R.string.mangabz_view_url, chapterId));
        webView.loadUrl(getString(R.string.mangabz_view_url, chapterId));

    }

//    private void initRXJava() {
//        observable = Observable.create(new ObservableOnSubscribe<List<String>>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<List<String>> emitter) throws Throwable {
//                Document doc = null;
//                try {
//                    doc = Jsoup.parse(html);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                String data = null;
//                for (Element e: doc.getElementsByTag("script")) {
//                    if (e.toString().contains("MANGABZ_MID") && e.toString().contains("MANGABZ_CID")) {
//                        data = e.toString();
//                    }
//                }
//                String readModel = null;
//                String mid = null;
//                String cid = null;
//                for (String s: data.split(";")) {
//                    if (s.contains("MANGABZ_READMODE")) {
//                        readModel = s.substring(s.lastIndexOf("=")+1, s.length()).replace(" ", "");
//                    } else if (s.contains("MANGABZ_MID")) {
//                        mid = s.substring(s.lastIndexOf("=")+1, s.length()).replace(" ", "");
//                    } else if (s.contains("MANGABZ_CID")) {
//                        cid = s.substring(s.lastIndexOf("=")+1, s.length()).replace(" ", "");
//                    }
//                }
//                LogUtil.e("readModel: " + readModel);
//                LogUtil.e("mid: " +  mid);
//                LogUtil.e("cid: " + cid);
//                for (Element e: doc.getElementsByClass("lazy")) {
//                    LogUtil.e("url: " + e.attr("src").split(";")[0]);
//                    list.add(e.attr("src").split(";")[0]);
//                }
//                emitter.onNext(list);
//                // 更新pre/next ChapterID
//                // 组装url
//            }
//        });
//        consumer = new Consumer<List<String>>() {
//            @Override
//            public void accept(List<String> strings) throws Throwable {
//                adapter.setList(strings);
//            }
//        };
//
//    }


}