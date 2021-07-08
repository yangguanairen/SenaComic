package com.sena.senacomic.dmzj.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.sena.senacomic.R;
import com.sena.senacomic.RetrofitService;
import com.sena.senacomic.databinding.ActivityDmzjClassifyBinding;
import com.sena.senacomic.dmzj.adapter.DMZJClassifyAdapter;
import com.sena.senacomic.dmzj.bean.DMZJClassifyBean;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.RetrofitHelper;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;



public class DMZJClassifyActivity extends AppCompatActivity {

    private String TAG = "jc";

    private String theme = "0";
    private String readership = "0";
    private String schedule = "0";
    private String area = "0";
    private String sort = "0";
    private int page = 0;

    private TextView themeTV;
    private TextView readershipTV;
    private TextView scheduleTV;
    private TextView areaTV;
    private TextView sortTV;

    private ActivityDmzjClassifyBinding binding;
    private RetrofitService retrofitService;
    private DMZJClassifyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDmzjClassifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        retrofitService = RetrofitHelper.getDMZJInstance(this).getServer();

        initTextView();
        updateAdapterData();
    }

    private void updateAdapterData() {
        binding.recyclerComicClassify.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new DMZJClassifyAdapter(this);
        binding.recyclerComicClassify.setAdapter(adapter);
        // 分类更新，page置零
        page = 0;
        adapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getResponse();
            }
        });
        adapter.getLoadMoreModule().setAutoLoadMore(false);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        getResponse();
    }

    private void getResponse() {
        Log.d(TAG, "getResponse: " + getString(R.string.dmzj_classify_id, theme, readership, schedule, area, sort, page));
        retrofitService.refreshDMZJClassify(getString(R.string.dmzj_classify_id, theme, readership, schedule, area, sort, page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DMZJClassifyBean>>() {
                    @Override
                    public void accept(List<DMZJClassifyBean> bean) throws Throwable {
                        if (page == 0 && bean.size() != 0) {
                            adapter.setList(bean);
                        } else {
                            adapter.addData(bean);
                        }
                        // 第一次请求数量低于15（及第一次请求就得到全部的结果），page++，会闪退
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

        String contentDescription = tv.getContentDescription().toString();
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (flag) {
                    case "theme":
                        themeTV.getPaint().setFakeBoldText(false);
                        themeTV.invalidate();
                        theme = contentDescription;
                        themeTV = tv;
                        break;
                    case "readership":
                        readershipTV.getPaint().setFakeBoldText(false);
                        readershipTV.invalidate();
                        readership = contentDescription;
                        readershipTV = tv;
                        break;
                    case "schedule":
                        scheduleTV.getPaint().setFakeBoldText(false);
                        scheduleTV.invalidate();
                        schedule = contentDescription;
                        scheduleTV = tv;
                        break;
                    case "area":
                        areaTV.getPaint().setFakeBoldText(false);
                        areaTV.invalidate();
                        area = contentDescription;
                        areaTV = tv;
                        break;
                    case "sort":
                        sortTV.getPaint().setFakeBoldText(false);
                        sortTV.invalidate();
                        sort = contentDescription;
                        sortTV = tv;
                        break;
                    default:
                        break;
                }

                tv.getPaint().setFakeBoldText(true);
                tv.invalidate();
                updateAdapterData();
            }
        });


    }

    private void initTextView() {

        // 防止空异常
        themeTV = binding.themeAll;
        readershipTV = binding.readershipAll;
        scheduleTV = binding.scheduleAll;
        areaTV = binding.areaAll;
        sortTV = binding.sortPopularity;
        //
        List<TextView> boldList = Arrays.asList(binding.themeAll, binding.readershipAll, binding.scheduleAll, binding.areaAll, binding.sortPopularity);
        for (TextView view: boldList) {
            view.getPaint().setFakeBoldText(true);
            view.invalidate();
        }
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


}