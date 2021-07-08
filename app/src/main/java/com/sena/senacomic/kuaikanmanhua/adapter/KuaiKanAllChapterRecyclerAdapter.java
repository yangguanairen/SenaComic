package com.sena.senacomic.kuaikanmanhua.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.senacomic.R;
import com.sena.senacomic.RetrofitService;
import com.sena.senacomic.history.HistoryBean;
import com.sena.senacomic.kuaikanmanhua.bean.KuaiKanBrowserBean;
import com.sena.senacomic.kuaikanmanhua.bean.KuaiKanAllChapterBean;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.RealmHelper;
import com.sena.senacomic.util.RetrofitHelper;
import com.sena.senacomic.kuaikanmanhua.view.KuaiKanBrowserActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class KuaiKanAllChapterRecyclerAdapter extends BaseQuickAdapter<KuaiKanAllChapterBean.Data.Comic_records, BaseViewHolder> {

    private Context mContext;

    public KuaiKanAllChapterRecyclerAdapter(Context context) {
        super(R.layout.item_kuai_kan_all_chapter);
        this.mContext = context;
    }


    @Override
    protected void convert(@NotNull BaseViewHolder holder, KuaiKanAllChapterBean.Data.Comic_records records) {
        RetrofitService retrofitService = RetrofitHelper.getKuaiKanInstance(mContext).getServer();
        String chapterId = records.getId() + "";
        retrofitService.refreshKuaiKanBrowser(chapterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<KuaiKanBrowserBean>() {
                    @Override
                    public void accept(KuaiKanBrowserBean bean) throws Throwable {
                        KuaiKanBrowserBean.Topic_info topicInfo = bean.getData().getTopic_info();
                        KuaiKanBrowserBean.Comic_info comicInfo = bean.getData().getComic_info();
                        holder.setText(R.id.chapter_number, comicInfo.getTitle());
                        holder.getView(R.id.chapter_number).setContentDescription(chapterId);
                        holder.getView(R.id.chapter_number).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String id = mContext.getString(R.string.realm_id, AppConstants.KUAI_KAN_ORIGIN, topicInfo.getId()+"");
                                // 保存历史记录
                                HistoryBean historyBean = new HistoryBean(id, AppConstants.KUAI_KAN_ORIGIN, topicInfo.getId()+"", topicInfo.getVertical_image_url(), topicInfo.getTitle(),topicInfo.getUser().getNickname(),
                                        comicInfo.getId(), comicInfo.getTitle(), new Date());
                                RealmHelper.createOrUpdateHistory(mContext, historyBean);
                                // 是否更新订阅记录
                                if (RealmHelper.queryFavorite(mContext, id) != null) {
                                    RealmHelper.updateFavorite(mContext, id, comicInfo.getId(), comicInfo.getTitle(), new Date());
                                }
                                // 跳转View页面，传递章节id
                                Intent intent = new Intent();
                                intent.setClass(mContext, KuaiKanBrowserActivity.class);
                                intent.putExtra(AppConstants.CHAPTER_ID, holder.getView(R.id.chapter_number).getContentDescription().toString());
                                mContext.startActivity(intent);
                            }
                        });

                    }
                });
    }

//    public List<KuaiKanBrowserBean.Topic_info> getInfoList() {
//        return infoList;
//    }
}
