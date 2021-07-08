package com.sena.senacomic.mangabz.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.senacomic.R;
import com.sena.senacomic.history.HistoryBean;
import com.sena.senacomic.mangabz.bean.MangabzSearchBean;
import com.sena.senacomic.mangabz.view.MangabzViewActivity;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.RealmHelper;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MangabzSearchAdapter extends BaseQuickAdapter<MangabzSearchBean, BaseViewHolder> {

    private Context mContext;

    public MangabzSearchAdapter(Context context) {
        super(R.layout.item_mangabz_search);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, MangabzSearchBean bean) {

        Observable.create(new ObservableOnSubscribe<RequestBuilder<Drawable>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<RequestBuilder<Drawable>> emitter) throws Throwable {
                RequestBuilder<Drawable> drawableRequestBuilder = Glide.with(mContext).load(bean.getCoverUrl());
                emitter.onNext(drawableRequestBuilder);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RequestBuilder<Drawable>>() {
                    @Override
                    public void accept(RequestBuilder<Drawable> drawableRequestBuilder) throws Throwable {
                        drawableRequestBuilder.into((ImageView) holder.getView(R.id.comic_cover_image));
                    }
                });
        holder.setText(R.id.comic_title, bean.getTitle());
        holder.setText(R.id.comic_status, bean.getStatus());
        holder.setText(R.id.comic_chapter, bean.getChapterName());

        // 最新章节点击监听，时间消费优先于父item
        holder.getView(R.id.comic_chapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mContext.getString(R.string.realm_id, AppConstants.MANGABZ_ORIGIN, bean.getComicId().replace("/", ""));
                // 创建历史记录
                HistoryBean historyBean = new HistoryBean(id, AppConstants.MANGABZ_ORIGIN, bean.getComicId(), bean.getCoverUrl(),
                        bean.getTitle(), null, bean.getChapterId(), bean.getChapterName(), new Date());
                RealmHelper.updateHistoryForMangabzSearch(mContext, historyBean);
                //是否更新订阅记录
                if (RealmHelper.queryFavorite(mContext, id) != null) {
                    RealmHelper.updateFavorite(mContext, id, bean.getChapterId(), bean.getChapterName(), new Date());
                }
                // 跳转View页面，传递数据章节id
                Intent intent = new Intent(mContext, MangabzViewActivity.class);
                intent.putExtra(AppConstants.CHAPTER_ID, bean.getChapterId());
                mContext.startActivity(intent);
            }
        });

    }


}
