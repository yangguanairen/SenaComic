package com.sena.senacomic.mangabz.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.senacomic.R;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MangabzViewAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;

    public MangabzViewAdapter(Context context) {
        super(R.layout.item_mangabz_view);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, String s) {
        Observable.create(new ObservableOnSubscribe<RequestBuilder<Drawable>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<RequestBuilder<Drawable>> emitter) throws Throwable {
                RequestBuilder<Drawable> drawableRequestBuilder = Glide.with(mContext).load(s);
                emitter.onNext(drawableRequestBuilder);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RequestBuilder<Drawable>>() {
                    @Override
                    public void accept(RequestBuilder<Drawable> drawableRequestBuilder) throws Throwable {
                        drawableRequestBuilder.into((ImageView) holder.getView(R.id.comic_image));
                    }
                });
    }
}
