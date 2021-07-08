package com.sena.senacomic.kuaikanmanhua.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.sena.senacomic.RetrofitService;
import com.sena.senacomic.databinding.FragmentKuaiKanComicListBinding;
import com.sena.senacomic.kuaikanmanhua.adapter.KuaiKanComicListRecyclerAdapter;
import com.sena.senacomic.kuaikanmanhua.bean.KuaiKanComicListBean;
import com.sena.senacomic.kuaikanmanhua.view.KuaiKanAllChapterActivity;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.LogUtil;
import com.sena.senacomic.util.RetrofitHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;


public class KuaiKanComicListFragment extends Fragment implements View.OnClickListener {

    private FragmentKuaiKanComicListBinding binding;


    private int page = 1;
    private int size = 20;

    private RetrofitService retrofitService;
    private KuaiKanComicListRecyclerAdapter adapter;
    private List<KuaiKanComicListBean.TopicMessageList> comicList = new ArrayList<KuaiKanComicListBean.TopicMessageList>();

    private Context mContext;

    public KuaiKanComicListFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentKuaiKanComicListBinding.inflate(getLayoutInflater());

        retrofitService = RetrofitHelper.getKuaiKanInstance(mContext).getServer();

        initTextView();
        initAdapter();

        return binding.getRoot();
    }

    private void initAdapter() {
        // 分类改变后，需要重新设置recyclerView的布局管理器，原因未知
        binding.recyclerComicBrowser.setLayoutManager(new GridLayoutManager(mContext, 3));
        adapter = new KuaiKanComicListRecyclerAdapter(mContext);
        binding.recyclerComicBrowser.setAdapter(adapter);
        adapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        // 跳转Info页面，传递漫画id
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@androidx.annotation.NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @androidx.annotation.NonNull @NotNull View view, int position) {
                Intent intent = new Intent(mContext, KuaiKanAllChapterActivity.class);
                intent.putExtra(AppConstants.COMIC_ID, ((KuaiKanComicListBean.TopicMessageList)adapter.getData().get(position)).getId());
                mContext.startActivity(intent);
            }
        });
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
        retrofitService.refreshKuaiKanComicList(binding.theme.getContentDescription().toString(),
                binding.schedule.getContentDescription().toString(),
                binding.other.getContentDescription().toString(),
                page, size, 1, 0, 0)
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

    private void initTextView() {

        binding.themeLinear.setOnClickListener(this::onClick);
        binding.scheduleLinear.setOnClickListener(this::onClick);
        binding.otherLinear.setOnClickListener(this::onClick);

        binding.themeBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.themeSort.setVisibility(View.GONE);
            }
        });
        binding.scheduleBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.scheduleSort.setVisibility(View.GONE);
            }
        });
        binding.otherBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.otherSort.setVisibility(View.GONE);
            }
        });


        List<TextView> tagList = Arrays.asList(binding.tagAll, binding.tagLove, binding.tagPureLove, binding.tagCampus, binding.tagPlot, binding.tagAntiquity, binding.tagFantasy,
                binding.tagPassionate, binding.tagJapaneseComic, binding.tagKoreaComic, binding.tagHeroine, binding.tagEnd, binding.tagTraverse, binding.tagCute,
                binding.tagTerror, binding.tagSuspense, binding.tagXuanhuan, binding.tagFunny, binding.tagCity, binding.tagPositiveEnergy, binding.tagBoss,
                binding.tagContribute);
        for (TextView v: tagList) {
            registerTagListener(v);
        }
        List<TextView> statusList = Arrays.asList(binding.updateStatusAll, binding.updateStatusLoading, binding.updateStatusEnding);
        for (TextView v: statusList) {
            registerUpdateStatusListener(v);
        }
        List<TextView> sortList = Arrays.asList(binding.sortAll, binding.sortHottest, binding.sortNewArrival);
        for (TextView v: sortList) {
            registerSortListener(v);
        }
    }

    private void registerTagListener(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新分类
                binding.theme.setText(textView.getText().toString());
                binding.theme.setContentDescription(textView.getContentDescription().toString());
                binding.themeSort.setVisibility(View.GONE);
                // 重新设置adapter
                initAdapter();
            }
        });
    }

    private void registerUpdateStatusListener(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新分类
                binding.schedule.setText(textView.getText().toString());
                binding.schedule.setContentDescription(textView.getContentDescription().toString());
                binding.scheduleSort.setVisibility(View.GONE);
                // 重新设置adapter
                initAdapter();
            }
        });
    }

    private void registerSortListener(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新分类
                binding.other.setText(textView.getText().toString());
                binding.other.setContentDescription(textView.getContentDescription().toString());
                binding.otherSort.setVisibility(View.GONE);
                // 重新设置adapter
                initAdapter();
            }
        });
    }

    @Override
    public void onClick(View v) {
        List<LinearLayout>  list = Arrays.asList(binding.themeLinear, binding.scheduleLinear, binding.otherLinear);
        HashMap<LinearLayout, LinearLayout> hashMap = new HashMap<>();
        hashMap.put(binding.themeLinear, binding.themeSort);
        hashMap.put(binding.scheduleLinear, binding.scheduleSort);
        hashMap.put(binding.otherLinear, binding.otherSort);
        for (LinearLayout layout: list) {
            if (v == layout) {
                hashMap.get(layout).setVisibility(View.VISIBLE);
            } else {
                hashMap.get(layout).setVisibility(View.GONE);
            }
        }
    }
}