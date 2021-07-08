package com.sena.senacomic.mangabz.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.sena.senacomic.R;
import com.sena.senacomic.RetrofitService;
import com.sena.senacomic.databinding.FragmentMangabzListBinding;
import com.sena.senacomic.mangabz.adapter.MangabzListAdapter;
import com.sena.senacomic.mangabz.bean.MangabzListBean;
import com.sena.senacomic.mangabz.view.MangabzInfoActivity;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.RetrofitHelper;
import com.sena.senacomic.util.RetrofitPostUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MangabzListFragment extends Fragment {

    private FragmentMangabzListBinding binding;

    private Context mContext;

    private RetrofitService retrofitService;
    private MangabzListAdapter adapter;

    private String tagId = "0";
    private String status = "0";
    private String sort = "10";
    private TextView tagTV;
    private TextView statusTV;
    private TextView sortTV;

    private int pageIndex = 1;


    public MangabzListFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMangabzListBinding.inflate(getLayoutInflater());

        retrofitService = RetrofitHelper.getMangabzInstance(mContext).getServer();

        initTextView();
        initAdapter();

        return binding.getRoot();
    }

    private void initAdapter() {
        adapter = new MangabzListAdapter(mContext);
        binding.recyclerViewList.setLayoutManager(new GridLayoutManager(mContext, 3));
        binding.recyclerViewList.setAdapter(adapter);
        adapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getResponse();
            }
        });
        // 禁止自动加载更多
        adapter.getLoadMoreModule().setAutoLoadMore(false);
        // 禁止未能填充满一页时自动加载更多
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        // item设置点击响应
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@androidx.annotation.NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @androidx.annotation.NonNull @NotNull View view, int position) {
                Intent intent = new Intent(mContext, MangabzInfoActivity.class);
                intent.putExtra(AppConstants.COMIC_ID, ((MangabzListBean.UpdateComicItems) adapter.getData().get(position)).getUrlKey());
                startActivity(intent);

            }
        });
        // 分类改变，page重置
        pageIndex = 1;
        getResponse();
    }

    private void getResponse() {
        // Post请求,form-data数据格式，采用RequestBodyMap附加数据
        retrofitService.refreshMangabzList(getString(R.string.mangabz_list_id, tagId, status, sort),
                RetrofitPostUtil.getInstance(mContext).generateRequestBody(tagId, status, sort, pageIndex+""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MangabzListBean>() {
                    @Override
                    public void accept(MangabzListBean bean) throws Throwable {
                        if (pageIndex == 1) {
                            adapter.setList(bean.getUpdateComicItems());
                        } else {
                            adapter.addData(bean.getUpdateComicItems());
                        }
                        // 默认一页显示21个，获取的数量小于21表示无更多数据，结束加载更多
                        if (bean.getUpdateComicItems().size() < 21) {
                            adapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            adapter.getLoadMoreModule().loadMoreComplete();
                            pageIndex++;
                        }
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
        // 点击分类改变，重新设置adapter
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