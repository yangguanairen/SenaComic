package com.sena.senacomic.dmzj.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.sena.senacomic.R;
import com.sena.senacomic.RetrofitService;
import com.sena.senacomic.databinding.FragmentDmzjClassifyBinding;
import com.sena.senacomic.dmzj.adapter.DMZJClassifyAdapter;
import com.sena.senacomic.dmzj.bean.DMZJClassifyBean;
import com.sena.senacomic.dmzj.view.DMZJInfoActivity;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.LogUtil;
import com.sena.senacomic.util.RetrofitHelper;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class DMZJClassifyFragment extends Fragment implements View.OnClickListener {
    private FragmentDmzjClassifyBinding binding;

    private int page = 0;

    private RetrofitService retrofitService;
    private DMZJClassifyAdapter adapter;
    private Context mContext;

    public DMZJClassifyFragment(Context context) {
        this.mContext = context;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDmzjClassifyBinding.inflate(getLayoutInflater());

        retrofitService = RetrofitHelper.getDMZJInstance(mContext).getServer();

        initTextView();
        updateAdapterData();

        return binding.getRoot();
    }

    private void updateAdapterData() {
        binding.recyclerComicClassify.setLayoutManager(new GridLayoutManager(mContext, 3));
        adapter = new DMZJClassifyAdapter(mContext);
        binding.recyclerComicClassify.setAdapter(adapter);
        // 跳转，Info页面，传递漫画id
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                Intent intent = new Intent(mContext, DMZJInfoActivity.class);
                intent.putExtra(AppConstants.COMIC_ID, ((DMZJClassifyBean) adapter.getData().get(position)).getId());
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
        // 禁止未填充满一页时自动加载更多
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        // 分类更新，page置零
        page = 0;
        getResponse();
    }

    private void getResponse() {
        String url = getString(R.string.dmzj_classify_id,
                binding.theme.getContentDescription().toString(),
                binding.readership.getContentDescription().toString(),
                binding.schedule.getContentDescription().toString(),
                binding.area.getContentDescription().toString(),
                binding.other.getContentDescription(),
                page);
        // 某些组合下返回的数据为空, 即"[]"
        // 使用Jsoup提前判定数据是否为空
        try {
            if ("[]".equals(Jsoup.connect(url).get().toString())) {
                return;
            }
        } catch (Exception e) {

        }
        LogUtil.d( "DMZJ_Classify_URL: " + url);
        retrofitService.refreshDMZJClassify(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DMZJClassifyBean>>() {
                    @Override
                    public void accept(List<DMZJClassifyBean> bean) throws Throwable {
                        if (bean == null) {
                            return ;
                        }
                        if (page == 0 && bean.size() != 0) {
                            adapter.setList(bean);
                        } else {
                            adapter.addData(bean);
                        }
                        // 第一次请求数量低于15（即第一次请求就得到全部的结果），page++，会闪退
                        if (bean.size() == 0) {
                            adapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            adapter.getLoadMoreModule().loadMoreComplete();
                            page++;
                        }
                    }
                });
    }


    private void registerListener(TextView tv, String flag) {

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (flag) {
                    case "theme":
                        binding.theme.setText(tv.getText().toString());
                        binding.theme.setContentDescription(tv.getContentDescription().toString());
                        binding.themeSort.setVisibility(View.GONE);
                        break;
                    case "readership":
                        binding.readership.setText(tv.getText().toString());
                        binding.readership.setContentDescription(tv.getContentDescription().toString());
                        binding.readershipSort.setVisibility(View.GONE);
                        break;
                    case "schedule":
                        binding.schedule.setText(tv.getText().toString());
                        binding.schedule.setContentDescription(tv.getContentDescription().toString());
                        binding.scheduleSort.setVisibility(View.GONE);
                        break;
                    case "area":
                        binding.area.setText(tv.getText().toString());
                        binding.area.setContentDescription(tv.getContentDescription().toString());
                        binding.areaSort.setVisibility(View.GONE);
                        break;
                    case "sort":
                        binding.other.setText(tv.getText().toString());
                        binding.other.setContentDescription(tv.getContentDescription().toString());
                        binding.otherSort.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
                updateAdapterData();
            }
        });


    }

    private void initTextView() {

        binding.themeLinear.setOnClickListener(this::onClick);
        binding.readershipLinear.setOnClickListener(this::onClick);
        binding.scheduleLinear.setOnClickListener(this::onClick);
        binding.areaLinear.setOnClickListener(this::onClick);
        binding.otherLinear.setOnClickListener(this::onClick);

        binding.themeBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.themeSort.setVisibility(View.GONE);
            }
        });
        binding.readershipBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.readershipSort.setVisibility(View.GONE);
            }
        });
        binding.scheduleBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.scheduleSort.setVisibility(View.GONE);
            }
        });
        binding.areaBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.areaSort.setVisibility(View.GONE);
            }
        });
        binding.otherBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.otherSort.setVisibility(View.GONE);
            }
        });


        // 注册Theme分类下TextView
        List<TextView> themeList = Arrays.asList(binding.themeAll, binding.themeAdventure, binding.themeJoy, binding.themeFighting, binding.themeScienceFiction, binding.themeLove,
                binding.themeSports, binding.themeMagic, binding.themeCampus, binding.themeSuspense, binding.themeTerror, binding.themeAffection,
                binding.themeLily, binding.themeRanssexuals, binding.themeTanmei, binding.themeHarem, binding.themeCute, binding.themeCure,
                binding.themeMartialArts, binding.themeWorkplace, binding.themeFantasy, binding.themeMorality, binding.themeLightNovel, binding.themeFunny);
        for (TextView view: themeList) {
            registerListener(view, AppConstants.DMZJ_CLASSIFY_THEME);
        }
        //
        List<TextView> readershipList = Arrays.asList(binding.readershipAll, binding.readershipJuvenile, binding.readershipMaiden, binding.readershipYouth);
        for (TextView view: readershipList) {
            registerListener(view, AppConstants.DMZJ_CLASSIFY_READERSHIP);
        }
        //
        List<TextView> scheduleList = Arrays.asList(binding.scheduleAll, binding.scheduleSerialize, binding.scheduleEnd);
        for (TextView view: scheduleList) {
            registerListener(view, AppConstants.DMZJ_CLASSIFY_SCHEDULE);
        }
        //
        List<TextView> areaList = Arrays.asList(binding.areaAll, binding.areaJapan, binding.areaMainland, binding.areaOccident, binding.areaHkt, binding.areaKorea, binding.areaOther);
        for (TextView view: areaList) {
            registerListener(view, AppConstants.DMZJ_CLASSIFY_AREA);
        }
        //
        List<TextView> sortList = Arrays.asList(binding.sortPopularity, binding.sortUpdate);
        for (TextView view: sortList) {
            registerListener(view, AppConstants.DMZJ_CLASSIFY_SORT);
        }
    }

    @Override
    public void onClick(View v) {
//        还要取消其他的显示
        List<LinearLayout> list = Arrays.asList(binding.themeLinear, binding.readershipLinear, binding.scheduleLinear, binding.areaLinear,binding.otherLinear);
        HashMap<LinearLayout, LinearLayout> hashMap = new HashMap<>();
        hashMap.put(binding.themeLinear, binding.themeSort);
        hashMap.put(binding.readershipLinear, binding.readershipSort);
        hashMap.put(binding.scheduleLinear, binding.scheduleSort);
        hashMap.put(binding.areaLinear, binding.areaSort);
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