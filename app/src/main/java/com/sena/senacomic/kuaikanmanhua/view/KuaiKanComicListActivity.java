package com.sena.senacomic.kuaikanmanhua.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.sena.senacomic.R;
import com.sena.senacomic.RetrofitService;
import com.sena.senacomic.kuaikanmanhua.adapter.KuaiKanComicListRecyclerAdapter;
import com.sena.senacomic.kuaikanmanhua.bean.KuaiKanComicListBean;
import com.sena.senacomic.databinding.ActivityKuaiKanComicListBinding;
import com.sena.senacomic.util.LogUtil;
import com.sena.senacomic.util.RetrofitHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class KuaiKanComicListActivity extends AppCompatActivity {

    // 请求条目
    private String TAG_ID = "0";
    private String UPDATE_STATUS = "0";
    private String SORT = "1";
    private int page = 1;
    private int size = 20;
    private TextView tagTextView;
    private TextView updateStatusTextView;
    private TextView sortTextView;

    private ActivityKuaiKanComicListBinding binding;
    private RetrofitService retrofitService;
    private KuaiKanComicListRecyclerAdapter adapter;
    private List<KuaiKanComicListBean.TopicMessageList>  comicList = new ArrayList<KuaiKanComicListBean.TopicMessageList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKuaiKanComicListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 分类初始状态默认全部
        if (tagTextView == null) {
            tagTextView = binding.tagAll;
            updateStatusTextView = binding.updateStatusAll;
            sortTextView = binding.sortAll;
        }

        initTextView();

        retrofitService = RetrofitHelper.getKuaiKanInstance(this).getServer();
        initAdapter();

    }

    private void initAdapter() {
        // 分类改变后，需要重新设置recyclerView的布局管理器，原因未知
        binding.recyclerComicBrowser.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new KuaiKanComicListRecyclerAdapter(KuaiKanComicListActivity.this);
        binding.recyclerComicBrowser.setAdapter(adapter);
        adapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        adapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getResponse();
            }
        });
        // 禁止自动加载更多
        adapter.getLoadMoreModule().setAutoLoadMore(false);
        // 禁止布局无法填满时自动加载更多。设置true，导致卡顿，最终程序闪退
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        // 分类已改变，page重置为1
        page = 1;
        getResponse();
    }

    private void getResponse() {
        LogUtil.d("tag_id: " + TAG_ID + "update-status: " + UPDATE_STATUS + "sort: " + SORT + "page: " + page);
        retrofitService.refreshKuaiKanComicList(TAG_ID, UPDATE_STATUS, SORT, page, size, 1, 0, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<KuaiKanComicListBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull KuaiKanComicListBean bean) {
                        comicList = bean.getHits().getTopicMessageList();
                        if (page == 1) {
                            adapter.setList(comicList);
                        } else {
                            adapter.addData(comicList);
                            adapter.notifyDataSetChanged();
                        }
                        // 如果获取的数据小于一页能显示的数据个数，表示服务器无更多数据
                        // 此时，在底部显示“No more data”，禁止用户继续加载
                        // 每次服务器返回非定值(size)，即使还有更多数据
                        // comicList.size() ==  20
                        if (comicList.size() == 0) {
                            adapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            adapter.getLoadMoreModule().loadMoreComplete();
                            page++;
                        }


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof HttpException) {
                            HttpException exception = (HttpException) e;
                            LogUtil.d("onError: " + exception);
                        } else {
                            LogUtil.d("onError: " + "1001");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.toolbar_search);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent();
                intent.setClass(KuaiKanComicListActivity.this, KuaiKanSearchActivity.class);
                intent.putExtra("search_query", query);
                startActivity(intent);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initTextView() {
        registerTagListener(binding.tagAll);
        registerTagListener(binding.tagLove);
        registerTagListener(binding.tagPureLove);
        registerTagListener(binding.tagCampus);
        registerTagListener(binding.tagPlot);
        registerTagListener(binding.tagAntiquity);
        registerTagListener(binding.tagFantasy);

        registerTagListener(binding.tagPassionate);
        registerTagListener(binding.tagJapaneseComic);
        registerTagListener(binding.tagKoreaComic);
        registerTagListener(binding.tagHeroine);
        registerTagListener(binding.tagEnd);
        registerTagListener(binding.tagTraverse);
        registerTagListener(binding.tagCute);

        registerTagListener(binding.tagTerror);
        registerTagListener(binding.tagSuspense);
        registerTagListener(binding.tagXuanhuan);
        registerTagListener(binding.tagFunny);
        registerTagListener(binding.tagCity);
        registerTagListener(binding.tagPositiveEnergy);
        registerTagListener(binding.tagBoss);

        registerTagListener(binding.tagContribute);

        registerUpdateStatusListener(binding.updateStatusAll);
        registerUpdateStatusListener(binding.updateStatusLoading);
        registerUpdateStatusListener(binding.updateStatusEnding);

        registerSortListener(binding.sortAll);
        registerSortListener(binding.sortHottest);
        registerSortListener(binding.sortNewArrival);
    }

    private void registerTagListener(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新分类
                tagTextView.getPaint().setFakeBoldText(false);
                tagTextView.setTextColor(Color.parseColor("#555555"));
                tagTextView.invalidate();
                tagTextView = textView;
                textView.getPaint().setFakeBoldText(true);
                textView.setTextColor(Color.BLACK);
                textView.invalidate();
                TAG_ID = textView.getContentDescription().toString();
                //发送请求，更新页面
                initAdapter();
            }
        });
    }

    private void registerUpdateStatusListener(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新分类
                updateStatusTextView.getPaint().setFakeBoldText(false);
                updateStatusTextView.setTextColor(Color.parseColor("#555555"));
                updateStatusTextView.invalidate();
                updateStatusTextView = textView;
                textView.getPaint().setFakeBoldText(true);
                textView.setTextColor(Color.BLACK);
                textView.invalidate();
                UPDATE_STATUS = textView.getContentDescription().toString();
                //发送请求，更新页面
                initAdapter();
            }
        });
    }

    private void registerSortListener(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新分类
                sortTextView.getPaint().setFakeBoldText(false);
                sortTextView.setTextColor(Color.parseColor("#555555"));
                updateStatusTextView.invalidate();
                sortTextView = textView;
                textView.getPaint().setFakeBoldText(true);
                textView.setTextColor(Color.BLACK);
                textView.invalidate();
                SORT = textView.getContentDescription().toString();
                //发送请求，更新页面
                initAdapter();
            }
        });
    }
}