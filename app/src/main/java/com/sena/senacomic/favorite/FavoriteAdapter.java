package com.sena.senacomic.favorite;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.senacomic.R;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.GlideUtil;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteAdapter extends BaseQuickAdapter<FavoriteBean, BaseViewHolder> {

    private Context mContext;

    public FavoriteAdapter(Context context) {
        super(R.layout.item_favorite);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, FavoriteBean bean) {
        Observable.create(new ObservableOnSubscribe<RequestBuilder<Drawable>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<RequestBuilder<Drawable>> emitter) throws Throwable {
                RequestBuilder<Drawable> d = null;
                if (bean.getOrigin() == AppConstants.DMZJ_ORIGIN) {
                    d = Glide.with(mContext).load(GlideUtil.getInstance(mContext).addDMZJCookie(bean.getCoverUrl()));
                } else {
                    d = Glide.with(mContext).load(bean.getCoverUrl());
                }
                emitter.onNext(d);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RequestBuilder<Drawable>>() {
                    @Override
                    public void accept(RequestBuilder<Drawable> d) throws Throwable {
                        d.into((ImageView) holder.getView(R.id.comic_cover_image));
                    }
                });
        holder.setText(R.id.comic_title, bean.getTitle());
        holder.setText(R.id.last_read_chapter, bean.getLastChapterName());
    }
}
