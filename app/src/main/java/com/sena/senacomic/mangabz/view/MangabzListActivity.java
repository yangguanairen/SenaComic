package com.sena.senacomic.mangabz.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.sena.senacomic.R;
import com.sena.senacomic.RetrofitService;
import com.sena.senacomic.databinding.ActivityMangabzListBinding;
import com.sena.senacomic.mangabz.adapter.MangabzListAdapter;
import com.sena.senacomic.mangabz.bean.MangabzListBean;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.LogUtil;
import com.sena.senacomic.util.RetrofitHelper;
import com.sena.senacomic.util.RetrofitPostUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class MangabzListActivity extends AppCompatActivity {


    private ActivityMangabzListBinding binding;
    private RetrofitService retrofitService;
    private MangabzListAdapter adapter;

    private String tagId = "0";
    private String status = "0";
    private String sort = "10";
    private TextView tagTV;
    private TextView statusTV;
    private TextView sortTV;

    private int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMangabzListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        retrofitService = RetrofitHelper.getMangabzInstance(this).getServer();

        initTextView();
        initAdapter();
    }


    private void initAdapter() {
        adapter = new MangabzListAdapter(this);
        binding.recyclerViewList.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recyclerViewList.setAdapter(adapter);
        adapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getResponse();
            }
        });
        adapter.getLoadMoreModule().setAutoLoadMore(false);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@androidx.annotation.NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @androidx.annotation.NonNull @NotNull View view, int position) {
                Intent intent = new Intent(MangabzListActivity.this, MangabzInfoActivity.class);
                intent.putExtra(AppConstants.COMIC_ID, ((MangabzListBean.UpdateComicItems) adapter.getData().get(position)).getUrlKey());
                startActivity(intent);

            }
        });
        pageIndex = 1;
        getResponse();
    }

    private void getResponse() {
        retrofitService.refreshMangabzList(
                getString(R.string.mangabz_list_id, tagId, status, sort),
                RetrofitPostUtil.getInstance(this).generateRequestBody(tagId, status, sort, pageIndex+"")).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MangabzListBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull MangabzListBean bean) {
                        if (pageIndex == 1) {
                            adapter.setList(bean.getUpdateComicItems());

                        } else {
                            adapter.addData(bean.getUpdateComicItems());
                        }
                        if (bean.getUpdateComicItems().size() < 21) {
                            adapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            adapter.getLoadMoreModule().loadMoreComplete();
                            pageIndex++;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.d("onError: " + ((e instanceof HttpException) ? ((HttpException) e).code() : "1001"));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initTextView() {
        // 设置初始状态
        tagTV = binding.tagAll;
        statusTV = binding.updateStatusAll;
        sortTV = binding.sortPopularity;
        List<TextView> tagList = Arrays.asList(binding.tagAll, binding.tagPassionate, binding.taglove, binding.tagCampus, binding.tagAdventure,
                binding.tagScienceFiction, binding.tagLife, binding.tagSuspense, binding.tagMagic, binding.tagSports);
        for (TextView view : tagList) {
            registerListener(view, tagTV, 1);
        }
        List<TextView> statusList = Arrays.asList(binding.updateStatusAll, binding.updateStatusSerializing, binding.updateStatusEnding);
        for (TextView view : statusList) {
            registerListener(view, sortTV, 2);
        }
        List<TextView> sortList = Arrays.asList(binding.sortPopularity, binding.sortUpdateTime, binding.sortNewArrival);
        for (TextView view : sortList) {
            registerListener(view, sortTV, 3);
        }
    }

    private void registerListener(TextView tv, TextView targetTV, int flag) {
        // tv变灰色表示未选中，targetTV变黑色表示选中
        // 点击后分类改变，重新设置adapter
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setTextColor(Color.parseColor("#ff4838"));
                tv.invalidate();
                switch (flag) {
                    case 1:
                        tagTV.setTextColor(Color.BLACK);
                        tagTV.invalidate();
                        tagTV = tv;
                        tagId = tv.getContentDescription().toString();
                        break;
                    case 2:
                        statusTV.setTextColor(Color.BLACK);
                        statusTV.invalidate();
                        statusTV = tv;
                        status = tv.getContentDescription().toString();
                        break;
                    case 3:
                        sortTV.setTextColor(Color.BLACK);
                        sortTV.invalidate();
                        sortTV = tv;
                        sort = tv.getContentDescription().toString();
                        break;
                    default:
                        break;
                }
                initAdapter();

            }
        });


    }
}