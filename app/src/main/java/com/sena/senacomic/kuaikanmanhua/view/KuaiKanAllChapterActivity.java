package com.sena.senacomic.kuaikanmanhua.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.sena.senacomic.R;
import com.sena.senacomic.RetrofitService;
import com.sena.senacomic.favorite.FavoriteBean;
import com.sena.senacomic.history.HistoryBean;
import com.sena.senacomic.kuaikanmanhua.adapter.KuaiKanAllChapterRecyclerAdapter;
import com.sena.senacomic.kuaikanmanhua.bean.KuaiKanAllChapterBean;
import com.sena.senacomic.kuaikanmanhua.bean.KuaiKanBrowserBean;
import com.sena.senacomic.databinding.ActivityKuaiKanAllChapterBinding;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.LogUtil;
import com.sena.senacomic.util.RealmHelper;
import com.sena.senacomic.util.RetrofitHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class KuaiKanAllChapterActivity extends AppCompatActivity {


    private ActivityKuaiKanAllChapterBinding binding;
    private KuaiKanAllChapterRecyclerAdapter adapter;
    private CompositeDisposable compositeDisposable;

    private String comicId = "0";
    private List<KuaiKanAllChapterBean.Data.Comic_records> records = new ArrayList<KuaiKanAllChapterBean.Data.Comic_records>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKuaiKanAllChapterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        compositeDisposable = new CompositeDisposable();

        comicId = getIntent().getStringExtra(AppConstants.COMIC_ID);

        binding.recyclerViewComicList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new KuaiKanAllChapterRecyclerAdapter(this);
        binding.recyclerViewComicList.setAdapter(adapter);


        RetrofitService retrofitService = RetrofitHelper.getKuaiKanInstance(this).getServer();
        // 获取所有章节的id，加载RecyclerView
        LogUtil.d("KuaiKanInfoUrl: " + comicId);
        retrofitService.refreshKuaiKanAllChapter(comicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<KuaiKanAllChapterBean>() {
                    @Override
                    public void accept(KuaiKanAllChapterBean bean) throws Throwable {
                        records.addAll(bean.getData().getComic_records());
                        adapter.setList(records);
                        // 获取漫画的简介
                        retrofitService.refreshKuaiKanBrowser(records.get(0).getId() + "")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<KuaiKanBrowserBean>() {
                                    @Override
                                    public void accept(KuaiKanBrowserBean bean) throws Throwable {
                                        KuaiKanBrowserBean.Topic_info info = bean.getData().getTopic_info();
                                        Glide.with(KuaiKanAllChapterActivity.this).load(info.getVertical_image_url()).into(binding.comicCoverImage);
                                        binding.comicTitle.setText(info.getTitle());
                                        binding.isEnding.setText(info.getUpdate_remind() == "" ? "已完结" : "连载中");
                                        binding.comicNickname.setText(info.getUser().getNickname());
                                        binding.comicDescription.setText(info.getDescription());

                                        initSort();
                                        initSubscribe(info);
                                    }
                                });
                    }
                });



    }


    private void initSort() {
        // 初始排序为正序，设置为不可点击
        // 点击后，设置自己为黑体加粗，不可点击；别人取消黑体加粗，可点击
        // 忘记了，这里很蠢，recyclerview的所有数据都要重新请求。最佳做法，把Adapter获取的数据重新排序
        // 问题来了，adapter需要的是comic_records,而我从adapter获取的是topic_info,
        // 也就是adapter实际的数据还需要在请求一次，我无法在ui线程遍历请求未知次数
        // 解决方法: 在activity把所有结果遍历一遍，传给adapter topic_info
        binding.chapterOrderPositive.setEnabled(false);
        binding.chapterOrderPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.reverse(records);
                adapter.setList(records);
                binding.chapterOrderPositive.setEnabled(false);
                binding.chapterOrderReverse.setEnabled(true);
                binding.chapterOrderPositive.setTextColor(Color.parseColor("#39adff"));
                binding.chapterOrderReverse.setTextColor(Color.parseColor("#cccccc"));
            }
        });
        binding.chapterOrderReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.reverse(records);
                adapter.setList(records);
                binding.chapterOrderPositive.setEnabled(true);
                binding.chapterOrderReverse.setEnabled(false);
                binding.chapterOrderReverse.setTextColor(Color.parseColor("#39adff"));
                binding.chapterOrderPositive.setTextColor(Color.parseColor("#cccccc"));

            }
        });
    }

    private void initSubscribe(KuaiKanBrowserBean.Topic_info topicInfo) {
        String id = getString(R.string.realm_id, AppConstants.KUAI_KAN_ORIGIN, topicInfo.getId()+"");
        // 设置subscribe初始显示，有数据未取消订阅，无数据默认订阅漫画(布局文件中已设置)
        if (RealmHelper.queryFavorite(this, id) != null) {
            binding.subscribe.setText("取消订阅");
        }
        // if条件，favorite表是否有数据
        binding.subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RealmHelper.queryFavorite(getApplicationContext(), id) == null) {
                    binding.subscribe.setText("取消订阅");
                    HistoryBean historyBean = RealmHelper.queryHistory(getApplicationContext(), id);
                    FavoriteBean favoriteBean = new FavoriteBean(id, AppConstants.KUAI_KAN_ORIGIN, topicInfo.getId()+"", topicInfo.getVertical_image_url(),
                            topicInfo.getTitle(),topicInfo.getUser().getNickname(),
                            historyBean==null ? "":historyBean.getChapterId(), historyBean==null? "未观看":historyBean.getChapterName(), new Date());
                    RealmHelper.createFavorite(getApplicationContext(), favoriteBean);
                } else {
                    binding.subscribe.setText("订阅漫画");
                    // 倾销订阅
                    RealmHelper.deleteFavorite(getApplicationContext(), id);
                }
            }
        });

        // 跳转View页面，传递章节id
        // 不支持五阅读记录，自动跳转至第一章
        binding.continueRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryBean historyBean = RealmHelper.queryHistory(getApplicationContext(), id);
                if (historyBean != null) {
                    Intent intent = new Intent(KuaiKanAllChapterActivity.this, KuaiKanBrowserActivity.class);
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
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}