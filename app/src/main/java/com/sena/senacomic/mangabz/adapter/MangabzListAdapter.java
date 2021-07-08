package com.sena.senacomic.mangabz.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.senacomic.R;
import com.sena.senacomic.mangabz.bean.MangabzListBean;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MangabzListAdapter extends BaseQuickAdapter<MangabzListBean.UpdateComicItems, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    public MangabzListAdapter(Context context) {
        super(R.layout.item_mangabz_list);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, MangabzListBean.UpdateComicItems items) {
        // 加载速度缓慢，占据主线程，导致崩溃
        Observable<RequestBuilder<Drawable>> observable = Observable.create(new ObservableOnSubscribe<RequestBuilder<Drawable>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<RequestBuilder<Drawable>> emitter) throws Throwable {
                RequestBuilder<Drawable> drawableRequestBuilder = Glide.with(mContext).load(items.getShowPicUrlB());
                emitter.onNext(drawableRequestBuilder);
            }
        });
        Consumer<RequestBuilder<Drawable>> consumer = new Consumer<RequestBuilder<Drawable>>() {
            @Override
            public void accept(RequestBuilder<Drawable> drawableRequestBuilder) throws Throwable {
                drawableRequestBuilder.into((ImageView) holder.getView(R.id.comic_cover_image));
            }
        };
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);

        holder.setText(R.id.comic_title, items.getTitle());
        holder.setText(R.id.comic_latest_chapter_name, items.getShowLastPartName());
    }
}
