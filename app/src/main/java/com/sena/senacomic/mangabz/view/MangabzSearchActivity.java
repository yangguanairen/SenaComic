package com.sena.senacomic.mangabz.view;

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
import com.sena.senacomic.R;
import com.sena.senacomic.customize.HomeToolbar;
import com.sena.senacomic.databinding.ActivityMangabzSearchBinding;
import com.sena.senacomic.mangabz.adapter.MangabzSearchAdapter;
import com.sena.senacomic.mangabz.bean.MangabzSearchBean;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.LogUtil;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MangabzSearchActivity extends AppCompatActivity {

    private ActivityMangabzSearchBinding binding;
    private MangabzSearchAdapter adapter;
    private Observable<List<MangabzSearchBean>> observable;
    private Observer<List<MangabzSearchBean>> observer;
    private Disposable disposable;

    private String query;

    private String comicId;
    private String coverUrl;
    private String title;
    private String status;
    private String chapterName;
    private String chapterId;
    private List<MangabzSearchBean> list = new ArrayList<MangabzSearchBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMangabzSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MangabzSearchAdapter(this);
        binding.recyclerViewSearch.setAdapter(adapter);
        // item添加点击监听，跳转Info页面，传递漫画id
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@androidx.annotation.NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @androidx.annotation.NonNull @NotNull View view, int position) {
                Intent intent = new Intent(MangabzSearchActivity.this, MangabzInfoActivity.class);
                intent.putExtra(AppConstants.COMIC_ID, ((MangabzSearchBean) adapter.getData().get(position)).getComicId());
                startActivity(intent);
            }
        });

        initRxjava();

        initToolbar();
    }

    private void initRxjava() {

        observable = Observable.create(new ObservableOnSubscribe<List<MangabzSearchBean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<MangabzSearchBean>> emitter) throws Throwable {
                Document doc = Jsoup.connect(getString(R.string.mangabz_search_url, query)).get();
                LogUtil.d("MangabzSearchUrl: " + getString(R.string.mangabz_search_url, query));
                list.clear();
                for (Element e: doc.getElementsByClass("mh-item")) {
                    comicId = e.getElementsByTag("a").attr("href");
                    coverUrl = e.getElementsByTag("img").attr("src");
                    title = e.getElementsByClass("title").text();
                    status = e.getElementsByTag("span").text();
                    chapterName = e.getElementsByClass("chapter").first().getElementsByTag("a").text();
                    chapterId = e.getElementsByClass("chapter").first().getElementsByTag("a").attr("href");
                    list.add(new MangabzSearchBean(comicId, coverUrl, title, status, chapterName, chapterId));

                }
                emitter.onNext(list);
            }
        });
        observer = new Observer<List<MangabzSearchBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(@NonNull List<MangabzSearchBean> beans) {
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
    }

    private void initToolbar() {

        @SuppressLint("ResourceType") XmlPullParser parser = getResources().getXml(R.layout.toolbar_home);
        AttributeSet attrs = Xml.asAttributeSet(parser);
        int type = 0;
        // 遍历xml文件根节点
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
        // 初始化toolbar，参数为Context，attributes(属性集)
        HomeToolbar toolbar = new HomeToolbar(this, attrs);
        setSupportActionBar(binding.customizeToolbar);
        getWindow().setStatusBarColor(Color.parseColor("#39adff"));
        // 设置输入监听器
        binding.customizeToolbar.setSearchViewOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    query = v.getText().toString();
                    LogUtil.d(v.getText().toString());
                    observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(observer);
                    // 隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

    }
}