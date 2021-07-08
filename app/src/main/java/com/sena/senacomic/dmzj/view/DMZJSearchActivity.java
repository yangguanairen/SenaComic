package com.sena.senacomic.dmzj.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sena.senacomic.R;
import com.sena.senacomic.customize.HomeToolbar;
import com.sena.senacomic.databinding.ActivityDmzjSearchBinding;
import com.sena.senacomic.dmzj.adapter.DMZJSearchAdapter;
import com.sena.senacomic.dmzj.bean.DMZJSearchBean;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.LogUtil;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class DMZJSearchActivity extends AppCompatActivity {


    private ActivityDmzjSearchBinding binding;
    private DMZJSearchAdapter adapter;
    private String query;
    private Observable<List<DMZJSearchBean>> observable;
    private Observer<List<DMZJSearchBean>> observer;
    private Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDmzjSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DMZJSearchAdapter(this);
        binding.recyclerViewSearch.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@androidx.annotation.NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @androidx.annotation.NonNull @NotNull View view, int position) {
                Intent intent = new Intent();
                intent.setClass(DMZJSearchActivity.this, DMZJInfoActivity.class);
                intent.putExtra(AppConstants.COMIC_ID, ((DMZJSearchBean) adapter.getData().get(position)).getComic_py());
                startActivity(intent);
            }
        });


        observable = Observable.create(new ObservableOnSubscribe<List<DMZJSearchBean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DMZJSearchBean>> emitter) throws Throwable {
                Document doc = null;
                try {
                    LogUtil.d("DMZJ_Search_URL: " + getString(R.string.dmzj_search_url, URLEncoder.encode(query, "utf-8")));
                    doc = Jsoup.connect(getString(R.string.dmzj_search_url, URLEncoder.encode(query, "utf-8")))
                            .ignoreContentType(true)
                            .get();
                } catch (Exception e) {
                    if (e instanceof HttpException) {
                        LogUtil.d("Jsoup connect error: " + ((HttpException) e).code());
                    } else {
                        LogUtil.d("Jsoup connect error: " + e.getCause());
                    }
                }
                Elements scripts = doc.getElementsByTag("script");
                String data = null;
                for (Element e: scripts) {
                    if (e.toString().contains("var serchArry=")) {
                        data = e.toString();
                        break;
                    }
                }
                String jsonData = null;
                for (String s: data.split("\n")) {
                    if (s.contains("var serchArry=")) {
                        jsonData = s;
                    }
                }
                int startIndex = jsonData.indexOf("[");
                int endIndex = jsonData.lastIndexOf("]");
                jsonData = jsonData.substring(startIndex, endIndex+1);
                emitter.onNext(new Gson().fromJson(jsonData, new TypeToken<List<DMZJSearchBean>>(){}.getType()));
            }
        });

        observer = new Observer<List<DMZJSearchBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(@NonNull List<DMZJSearchBean> beans) {
                adapter.setList(beans);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                disposable.dispose();
            }
        };

        initToolbar();
    }


    private void initToolbar() {

        @SuppressLint("ResourceType") XmlPullParser parser = getResources().getXml(R.layout.toolbar_home);
        AttributeSet attrs = Xml.asAttributeSet(parser);
        int type = 0;
        while (true) {
            try {
                if (!((type=parser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_TAG))
                    break;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

        }

        HomeToolbar toolbar = new HomeToolbar(this, attrs);
        setSupportActionBar(binding.toolbarSearch);

        getWindow().setStatusBarColor(Color.parseColor("#39adff"));

        binding.toolbarSearch.setSearchViewOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    query = v.getText().toString();
                    observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(observer);
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

    }


}