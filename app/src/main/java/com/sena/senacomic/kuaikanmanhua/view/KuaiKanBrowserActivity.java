package com.sena.senacomic.kuaikanmanhua.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sena.senacomic.R;
import com.sena.senacomic.RetrofitService;
import com.sena.senacomic.kuaikanmanhua.adapter.KuaiKanBrowserRecyclerAdapter;
import com.sena.senacomic.kuaikanmanhua.bean.KuaiKanBrowserBean;
import com.sena.senacomic.databinding.ActivityKuaiKanBrowserBinding;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.RealmHelper;
import com.sena.senacomic.util.RetrofitHelper;

import java.util.Date;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * 简析数据流动，防止自己以后看不懂
 * 初始化页面，直接发送一条请求
 * button点击后，发送请求，更新页面
 */

// 有问题，当前章节为付费且有后续章节时，nextComicId为空

public class KuaiKanBrowserActivity extends AppCompatActivity {

    private ActivityKuaiKanBrowserBinding binding;
    private KuaiKanBrowserRecyclerAdapter adapter;
    private RetrofitService retrofitService;
    private CompositeDisposable compositeDisposable;

    private String preComicId;       // 指向上一章节id，默认为空
    private String nextComicId;      // 指向下一章节id，默认为上一页面发送


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKuaiKanBrowserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        nextComicId = getIntent().getStringExtra(AppConstants.CHAPTER_ID);
        retrofitService = RetrofitHelper.getKuaiKanInstance(this).getServer();
        compositeDisposable = new CompositeDisposable();


        binding.recyclerViewComicImage.setLayoutManager(new LinearLayoutManager(this));
        adapter = new KuaiKanBrowserRecyclerAdapter(this);
        binding.recyclerViewComicImage.setAdapter(adapter);

        binding.nextComicChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNextComic();
            }
        });
        binding.preComicChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPreComic();
            }
        });

        getNextComic();

    }

    private void getNextComic() {
        if (nextComicId == null || nextComicId == "") {
            Toast.makeText(this, "无下一话", Toast.LENGTH_SHORT).show();
            return;
        }
        retrofitService.refreshKuaiKanBrowser(nextComicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<KuaiKanBrowserBean>() {
                    @Override
                    public void accept(KuaiKanBrowserBean bean) throws Throwable {
                        updateData(bean);
                    }
                });
    }

    private void getPreComic() {
        if (preComicId == null || preComicId == "") {
            Toast.makeText(this, "无上一话", Toast.LENGTH_SHORT).show();
            return;
        }
        retrofitService.refreshKuaiKanBrowser(preComicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<KuaiKanBrowserBean>() {
                    @Override
                    public void accept(KuaiKanBrowserBean bean) throws Throwable {
                        updateData(bean);
                    }
                });
    }

    private void updateData(KuaiKanBrowserBean bean) {
        KuaiKanBrowserBean.Comic_info comicInfo = bean.getData().getComic_info();
        String id = getString(R.string.realm_id, AppConstants.KUAI_KAN_ORIGIN, bean.getData().getTopic_info().getId()+"");
        // 更新历史记录
        RealmHelper.updateHistory(this, id, comicInfo.getId(), comicInfo.getTitle(), new Date());

        // 是否更新订阅记录
        if (RealmHelper.queryFavorite(this, id) != null) {
            RealmHelper.updateFavorite(this, id, comicInfo.getId(), comicInfo.getTitle(), new Date());
        }
        preComicId = bean.getData().getPrevious_comic_id();
        nextComicId = bean.getData().getNext_comic_info().getNext_comic_id();
        adapter.setList(bean.getData().getComic_info().getImages());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}