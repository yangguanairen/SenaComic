package com.sena.senacomic.history;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.senacomic.R;
import com.sena.senacomic.dmzj.view.DMZJViewActivity;
import com.sena.senacomic.kuaikanmanhua.view.KuaiKanBrowserActivity;
import com.sena.senacomic.mangabz.view.MangabzViewActivity;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.GlideUtil;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HistoryAdapter extends BaseQuickAdapter<HistoryBean, BaseViewHolder> {

    private Context mContext;
    private TextView mTextView;
    private String mFlag;
    private String mSelectFlag;

    private SimpleDateFormat format;
    private List<BaseViewHolder> holderList = new ArrayList<>();

    public HistoryAdapter(Context context) {
        super(R.layout.item_history);
        this.mContext = context;
        format = new SimpleDateFormat("YYYY-MM-dd hh-mm");
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, HistoryBean bean) {

        // 存储加载的item的holder，用于视图操作
        holderList.add(holder);
        // holder数量变化，立即改变item视图格式
        mTextView.setText(holder.toString());

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
                    public void accept(RequestBuilder<Drawable> s) throws Throwable {
                        s.into((ImageView) holder.getView(R.id.comic_cover_image));
                    }
                });

        holder.setText(R.id.comic_title, bean.getComicTitle());
        holder.setText(R.id.comic_author, bean.getAuthor());
        holder.setText(R.id.last_read_time, format.format(bean.getLastReadTime()));
        // 每个漫画源章节命名格式不同，导致长短不一，故取消设置
//        holder.setText(R.id.chapter_name, bean.getChapterName());
        ((CheckBox) holder.getView(R.id.edit_check)).setEnabled(false);

        holder.getView(R.id.continue_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                int origin = bean.getOrigin();
                if (origin == AppConstants.KUAI_KAN_ORIGIN) {
                    intent.setClass(mContext, KuaiKanBrowserActivity.class);
                } else if (origin == AppConstants.DMZJ_ORIGIN) {
                    intent.setClass(mContext, DMZJViewActivity.class);
                } else if (origin == AppConstants.MANGABZ_ORIGIN) {
                    intent.setClass(mContext, MangabzViewActivity.class);
                }
                intent.putExtra(AppConstants.CHAPTER_ID, bean.getChapterId());
                intent.putExtra(AppConstants.COMIC_ID, bean.getComicId());
                mContext.startActivity(intent);
            }
        });
        String comicResource = null;
        switch (bean.getOrigin()) {
            case AppConstants.KUAI_KAN_ORIGIN: comicResource = "快看漫画";break;
            case AppConstants.DMZJ_ORIGIN: comicResource = "动漫之家";break;
            case AppConstants.MANGABZ_ORIGIN: comicResource = "Mangabz";break;
            default:break;
        }
        holder.setText(R.id.comic_source, "漫画源: " + comicResource);

    }

    // 实现1对1，n对1响应。编辑对TextView，item对TextView。
    // 达到编辑发出一个事件，能让所有的item都能共享
    public void setOtherObject(TextView textView) {
        this.mTextView = textView;
        mTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ("0".equals(mFlag)) {
                    for (BaseViewHolder holder: holderList) {
                        ((CheckBox) holder.getView(R.id.edit_check)).setVisibility(View.VISIBLE);
                        ((TextView) holder.getView(R.id.continue_read)).setVisibility(View.GONE);
                    }
                    // 全选功能的实现，
                    if ("all".equals(mSelectFlag)) {
                        for (BaseViewHolder holder: holderList) {
                            ((CheckBox) holder.getView(R.id.edit_check)).setChecked(true);
                        }
                    } else {
                        for (BaseViewHolder holder: holderList) {
                            ((CheckBox) holder.getView(R.id.edit_check)).setChecked(false);
                        }
                    }

                } else {
                    for (BaseViewHolder holder: holderList) {
                        ((CheckBox) holder.getView(R.id.edit_check)).setVisibility(View.GONE);
                        ((TextView) holder.getView(R.id.continue_read)).setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    // 设置Toolbar编辑的标识量，"0"表示编辑，展开checkBox; "1"表示取消, 隐藏checkBox
    public void setFlag(String flag) {
        this.mFlag = flag;
    }

    // 设置selectAll的标识量，”all“表示全部选中，”none“表示全部不选中
    public void setSelectFlag(String selectFlag) {
        this.mSelectFlag  = selectFlag;
    }
}
