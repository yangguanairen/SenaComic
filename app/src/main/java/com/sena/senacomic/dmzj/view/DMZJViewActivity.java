package com.sena.senacomic.dmzj.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sena.senacomic.R;
import com.sena.senacomic.databinding.ActivityDmzjViewBinding;
import com.sena.senacomic.dmzj.adapter.DMZJViewAdapter;
import com.sena.senacomic.dmzj.bean.DMZJViewBean;
import com.sena.senacomic.favorite.FavoriteBean;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.LogUtil;
import com.sena.senacomic.util.RealmHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DMZJViewActivity extends AppCompatActivity {


    private ActivityDmzjViewBinding binding;
    private DMZJViewAdapter adapter;
    private Observable<DMZJViewBean> observable;
    private Consumer<DMZJViewBean> consumer;

    private String comicId;
    private String chapterId;
    private String preChapterId = null;
    private String nextChapterId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDmzjViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(Color.parseColor("#39adff"));

        comicId = getIntent().getStringExtra(AppConstants.COMIC_ID);
        chapterId = getIntent().getStringExtra(AppConstants.CHAPTER_ID);

        binding.recyclerViewView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DMZJViewAdapter(this);
        binding.recyclerViewView.setAdapter(adapter);

        initRxjava();
        setData();

        initSort();

    }

    /**
     * 疑问，相同对象之间的订阅关系是覆盖/叠加/只看第一次
     * 是否要使用Disposable取消订阅，在Observer中
     */
    private void initRxjava() {
        observable = Observable.create(new ObservableOnSubscribe<DMZJViewBean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<DMZJViewBean> emitter) throws Throwable {
                Document doc = null;
                try {
                    LogUtil.d("DMZJViewUrl: " + getString(R.string.dmzj_comic_image_url, comicId, chapterId));
                    doc = Jsoup.connect(getString(R.string.dmzj_comic_image_url, comicId, chapterId)).get();
                } catch (Exception e) {

                }
                Elements scripts = doc.getElementsByTag("script");
                String data = null;
                for (Element e: scripts) {
                    if (e.toString().contains("mReader.initData")) {
                        data = e.toString();
                        break;
                    }
                }
                String s = null;
                for (String tmp: data.split("\n")) {
                    if (tmp.contains("mReader.initData")) {
                        s = tmp;
                        break;
                    }
                }
                int startIndex = s.indexOf("{");
                int endIndex = s.lastIndexOf("}");
                String gsonData = s.substring(startIndex, endIndex+1);
                emitter.onNext(new Gson().fromJson(gsonData, DMZJViewBean.class));
            }
        });

        consumer = new Consumer<DMZJViewBean>() {
            @Override
            public void accept(DMZJViewBean bean) throws Throwable {
                String id = getString(R.string.realm_id, AppConstants.DMZJ_ORIGIN, comicId);
                // 在加载新章节的时候更新历史记录，而不是在点击上一篇/下一篇时更新(因为无法提前拿到chapterName)
                RealmHelper.updateHistory(DMZJViewActivity.this, id,
                        bean.getId(), bean.getChapter_name(), new Date());
                // 是否更新订阅记录里的最后观看章节信息
                FavoriteBean favoriteBean = RealmHelper.queryFavorite(DMZJViewActivity.this, id);
                if (favoriteBean != null) {
                    RealmHelper.updateFavorite(DMZJViewActivity.this, id, bean.getId(), bean.getChapter_name(), new Date());
                }
                adapter.setList(bean.getPage_url());
                preChapterId = bean.getNext_chap_id();
                nextChapterId = bean.getPrev_chap_id();
            }
        };
    }

    private void setData() {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    private void initSort() {
        binding.preComicChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preChapterId == "" || preChapterId == null) {
                    Toast.makeText(DMZJViewActivity.this, "无上一篇", Toast.LENGTH_SHORT).show();
                    return;
                }
                chapterId = preChapterId;
                setData();
            }
        });
        binding.nextComicChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextChapterId == "" || nextChapterId == null) {
                    Toast.makeText(DMZJViewActivity.this, "无下一篇", Toast.LENGTH_SHORT).show();
                    return;
                }
                chapterId = nextChapterId;
                setData();
            }
        });
    }
}