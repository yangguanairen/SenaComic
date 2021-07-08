package com.sena.senacomic.mangabz.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.sena.senacomic.R;
import com.sena.senacomic.databinding.ActivityMangabzInfoBinding;
import com.sena.senacomic.favorite.FavoriteBean;
import com.sena.senacomic.history.HistoryBean;
import com.sena.senacomic.mangabz.adapter.MangabzInfoAdapter;
import com.sena.senacomic.mangabz.bean.MangabzInfoBean;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.LogUtil;
import com.sena.senacomic.util.RealmHelper;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MangabzInfoActivity extends AppCompatActivity implements View.OnClickListener {


    private ActivityMangabzInfoBinding binding;
    private MangabzInfoAdapter adapter;
    private String comicId;

    private String coverUrl;
    private String title;
    private String author;
    private String classify = "";
    private String introduce;
    private String status;

    private boolean isError = false;

    private ColorStateList color;
    private List<MangabzInfoBean> infoList = new ArrayList<MangabzInfoBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMangabzInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        comicId = getIntent().getStringExtra(AppConstants.COMIC_ID);


        binding.recyclerViewChapter.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new MangabzInfoAdapter(this);
        binding.recyclerViewChapter.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@androidx.annotation.NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @androidx.annotation.NonNull @NotNull View view, int position) {
                MangabzInfoBean infoBean = (MangabzInfoBean) adapter.getData().get(position);
                String id = getString(R.string.realm_id, AppConstants.MANGABZ_ORIGIN, comicId.replace("/", ""));
                // 保存历史记录
                HistoryBean historyBean = new HistoryBean(id, AppConstants.MANGABZ_ORIGIN, comicId, coverUrl, title, author, infoBean.getChapterId(), infoBean.getName(), new Date());
                RealmHelper.createOrUpdateHistory(MangabzInfoActivity.this, historyBean);
                // 是否更新订阅里的观看记录信息
                FavoriteBean favoriteBean = RealmHelper.queryFavorite(MangabzInfoActivity.this, id);
                if (favoriteBean != null) {
                    RealmHelper.updateFavorite(getApplicationContext(), id, infoBean.getChapterId().replace("/", ""), infoBean.getName(), new Date());
                }
                // 跳转View页面，传递章节id
                Intent intent = new Intent(MangabzInfoActivity.this, MangabzViewActivity.class);
                intent.putExtra(AppConstants.CHAPTER_ID, ((MangabzInfoBean) adapter.getData().get(position)).getChapterId());
                startActivity(intent);
            }
        });

        initRxJava();
        initView();


    }

    private void initRxJava() {
        Observable<List<MangabzInfoBean>> observable = Observable.create(new ObservableOnSubscribe<List<MangabzInfoBean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<MangabzInfoBean>> emitter) throws Throwable {
                Document doc = null;
                try {
                    LogUtil.d("MangabzInfoActivity_URL: " + getString(R.string.mangabz_info_url, comicId));
                    doc = Jsoup.connect(getString(R.string.mangabz_info_url, comicId)).get();
                    // 关键点，用于判断目标页面是否出错，出错在catch复制isError变量true值，加载默认Error页面
                    LogUtil.e("Boolean： " + (doc.getElementsByClass("img-404").text()=="" ? true:false) + "");

                    coverUrl = doc.getElementsByClass("detail-info-cover").attr("src");
                    title = doc.getElementsByClass("detail-info-title").text();
                    author = doc.getElementsByClass("detail-info-tip").first()
                            .getElementsByTag("a").text();
                    status = doc.getElementsByClass("detail-info-tip").first()
                            .getElementsByTag("span").get(1).getElementsByTag("span").get(1).text();
                    for (Element e: doc.getElementsByClass("item")) {
                        classify += e.text() + " ";
                    }
                    introduce = doc.getElementsByClass("detail-info-content").text().replace("[+展開]", "").replace("[-折疊]", "");
                    for (Element e: doc.getElementsByClass("detail-list-form-item")) {
                        Element a = e.getElementsByTag("a").first();
                        infoList.add(new MangabzInfoBean(a.text().split(" ")[0], a.attr("href")));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    isError = true;
                }

                emitter.onNext(infoList);
            }
        });

        Consumer<List<MangabzInfoBean>> consumer = new Consumer<List<MangabzInfoBean>>() {
            @Override
            public void accept(List<MangabzInfoBean> beans) throws Throwable {
                if (isError) {
                    binding.content.setVisibility(View.GONE);
                    binding.tvNoData.setVisibility(View.VISIBLE);
                } else {
                    adapter.setList(beans);
                    Glide.with(MangabzInfoActivity.this).load(coverUrl).into(binding.comicCoverImage);
                    binding.comicTitle.setText(title);
                    binding.comicAuthor.setText(author);
                    binding.comicClassify.setText(classify);
                    binding.comicDescription.setText(introduce);
                    binding.comicStatus.setText(status);

                    binding.subscribe.setClickable(true);
                }
            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    private void initView() {

        getWindow().setStatusBarColor(Color.parseColor("#39adff"));

        // 默认倒序
        color = binding.sortPositive.getTextColors();
        binding.sortReverse.setEnabled(false);
        binding.sortReverse.getPaint().setFakeBoldText(true);
        binding.sortReverse.invalidate();
        binding.sortReverse.setOnClickListener(this::onClick);
        binding.sortPositive.setOnClickListener(this::onClick);

        String id = getString(R.string.realm_id, AppConstants.MANGABZ_ORIGIN, comicId.replace("/", ""));
        // 初始化订阅状态
        FavoriteBean result = RealmHelper.queryFavorite(this, id);
        if (result != null) {
            binding.subscribe.setBackground(getResources().getDrawable(R.drawable.favorite_true, null));
        }

        binding.subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RealmHelper.queryFavorite(getApplicationContext(), id) == null) {
                    v.setBackground(getResources().getDrawable(R.drawable.favorite_true, null));
                    // 查看是否已经阅读过，阅读过把最新阅读记录添加
                    HistoryBean historyBean = RealmHelper.queryHistory(getApplicationContext(), id);
                    FavoriteBean favoriteBean = new FavoriteBean(id, AppConstants.MANGABZ_ORIGIN, comicId, coverUrl, title, author,
                            historyBean==null ? "":historyBean.getChapterId(),
                            historyBean==null ? "未观看":historyBean.getChapterName(), new Date());
                    RealmHelper.createFavorite(getApplicationContext(), favoriteBean);
                } else {
                    v.setBackground(getResources().getDrawable(R.drawable.favorite_false, null));
                    // 取消订阅
                    RealmHelper.deleteFavorite(getApplicationContext(), id);
                }
            }
        });

        binding.continueRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 只有阅读过才能继续阅读，没有自动跳转至第一章的功能
                HistoryBean historyBean = RealmHelper.queryHistory(getApplicationContext(), id);
                if (historyBean != null) {
                    Intent intent = new Intent(MangabzInfoActivity.this, MangabzViewActivity.class);
                    intent.putExtra(AppConstants.CHAPTER_ID, historyBean.getChapterId());
                    startActivity(intent);
                }
            }
        });

        // 简单实现TextView折叠效果
        binding.comicDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.comicDescription.getMaxLines() == 4) {
                    binding.comicDescription.setMaxLines(999);
                } else {
                    binding.comicDescription.setMaxLines(4);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sort_positive:
                binding.sortPositive.getPaint().setFakeBoldText(true);
                binding.sortPositive.setTextColor(Color.BLACK);
                binding.sortPositive.invalidate();
                binding.sortPositive.setEnabled(false);
                binding.sortReverse.getPaint().setFakeBoldText(false);
                binding.sortReverse.setTextColor(color);
                binding.sortReverse.invalidate();
                binding.sortReverse.setEnabled(true);
                break;
            case R.id.sort_reverse:
                binding.sortReverse.getPaint().setFakeBoldText(true);
                binding.sortReverse.setTextColor(Color.BLACK);
                binding.sortReverse.invalidate();
                binding.sortReverse.setEnabled(false);
                binding.sortPositive.getPaint().setFakeBoldText(false);
                binding.sortPositive.setTextColor(color);
                binding.sortPositive.invalidate();
                binding.sortPositive.setEnabled(true);
                break;
        }
        Collections.reverse(infoList);
        adapter.setList(infoList);
    }

}