package com.sena.senacomic.kuaikanmanhua.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.senacomic.R;
import com.sena.senacomic.RetrofitService;
import com.sena.senacomic.kuaikanmanhua.bean.KuaiKanAllChapterBean;
import com.sena.senacomic.kuaikanmanhua.bean.KuaiKanBrowserBean;
import com.sena.senacomic.kuaikanmanhua.bean.KuaiKanSearchBean;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.RetrofitHelper;
import com.sena.senacomic.kuaikanmanhua.view.KuaiKanAllChapterActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class KuaiKanSearchRecyclerAdapter extends BaseQuickAdapter<KuaiKanSearchBean.Hits, BaseViewHolder> {


    private Context mContext;
    private RetrofitService retrofitService;


    public KuaiKanSearchRecyclerAdapter(Context context) {
        super(R.layout.item_kuai_kan_search);
        this.mContext = context;
        retrofitService = RetrofitHelper.getKuaiKanInstance(mContext).getServer();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, KuaiKanSearchBean.Hits hits) {

        retrofitService.refreshKuaiKanAllChapter(hits.getTopic_id())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<KuaiKanAllChapterBean>() {
                    @Override
                    public void accept(KuaiKanAllChapterBean kuaiKanAllChapterBean) throws Throwable {
                        retrofitService.refreshKuaiKanBrowser(String.valueOf(kuaiKanAllChapterBean.getData().getComic_records().get(0).getId()))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<KuaiKanBrowserBean>() {
                                    @Override
                                    public void accept(KuaiKanBrowserBean kuaiKanBrowserBean) throws Throwable {
                                        KuaiKanBrowserBean.Topic_info info = kuaiKanBrowserBean.getData().getTopic_info();
                                        Glide.with(mContext).load(info.getVertical_image_url())
                                                .into((ImageView) holder.getView(R.id.comic_cover_image));
                                        holder.setText(R.id.comic_title, info.getTitle());
                                        holder.getView(R.id.comic_title).setContentDescription(info.getId() + "");
                                        holder.setText(R.id.comic_nickname, info.getUser().getNickname());
                                        holder.setText(R.id.comic_tags, Arrays.toString(info.getTags().toArray()));
                                        if (info.getUpdate_remind() != "") {
                                            holder.setText(R.id.is_ending, "连载中");
                                        } else {
                                            holder.setText(R.id.is_ending, "已完结");
                                        }
                                        holder.getView(R.id.comic_search_result).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent();
                                                intent.setClass(mContext, KuaiKanAllChapterActivity.class);
                                                intent.putExtra(AppConstants.COMIC_ID, holder.getView(R.id.comic_title).getContentDescription().toString());
                                                mContext.startActivity(intent);
                                            }
                                        });
                                    }
                                });
                    }
                });
    }



}
