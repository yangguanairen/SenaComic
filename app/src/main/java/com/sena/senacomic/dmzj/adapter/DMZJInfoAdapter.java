package com.sena.senacomic.dmzj.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.senacomic.R;
import com.sena.senacomic.dmzj.bean.DMZJInfoBean;
import com.sena.senacomic.dmzj.view.DMZJViewActivity;
import com.sena.senacomic.favorite.FavoriteBean;
import com.sena.senacomic.history.HistoryBean;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.RealmHelper;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DMZJInfoAdapter extends BaseQuickAdapter<DMZJInfoBean, BaseViewHolder> {

    private Context mContext;

    private String mCoverUrl;
    private String mAuthor;
    private String mTitle;

    public DMZJInfoAdapter(Context context) {
        super(R.layout.item_dmzj_info);
        this.mContext = context;

    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, DMZJInfoBean bean) {

        // 嵌套recyclerView
        RecyclerView recyclerView = holder.getView(R.id.recyclerView_list_child);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        DMZJInfoChildAdapter mAdapter = new DMZJInfoChildAdapter(mContext);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setList(bean.getData());

        holder.setText(R.id.list_name, bean.getTitle());

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                DMZJInfoBean.Data data = (DMZJInfoBean.Data) adapter.getData().get(position);
                String id = mContext.getString(R.string.realm_id, AppConstants.DMZJ_ORIGIN, data.getComic_id());
                // 保存历史记录
                HistoryBean historyBean = new HistoryBean(id, AppConstants.DMZJ_ORIGIN, data.getComic_id(), mCoverUrl, mTitle, mAuthor,  data.getId(), data.getChapter_name(), new Date());
                RealmHelper.createOrUpdateHistory(mContext, historyBean);
                // 是否更新订阅记录里的最后阅读章节信息
                FavoriteBean favoriteBean = RealmHelper.queryFavorite(mContext, id);
                if (favoriteBean != null) {
                    RealmHelper.updateFavorite(mContext, id, data.getId(), data.getChapter_name(), new Date());
                }
                // 跳转View页面,传递漫画id和章节id
                Intent intent = new Intent();
                intent.setClass(mContext, DMZJViewActivity.class);
                intent.putExtra(AppConstants.COMIC_ID, data.getComic_id());
                intent.putExtra(AppConstants.CHAPTER_ID, data.getId());
                mContext.startActivity(intent);
            }
        });

        holder.getView(R.id.chapter_order_reverse).setEnabled(false);
        holder.getView(R.id.chapter_order_positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DMZJInfoBean.Data> data = mAdapter.getData();
                Collections.reverse(data);
                mAdapter.setList(data);
                holder.getView(R.id.chapter_order_positive).setEnabled(false);
                ((TextView)holder.getView(R.id.chapter_order_positive)).setTextColor(Color.parseColor("#39adff"));
                holder.getView(R.id.chapter_order_reverse).setEnabled(true);
                ((TextView)holder.getView(R.id.chapter_order_reverse)).setTextColor(Color.parseColor("#cccccc"));
            }
        });

        holder.getView(R.id.chapter_order_reverse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DMZJInfoBean.Data> data = mAdapter.getData();
                Collections.reverse(data);
                mAdapter.setList(data);
                holder.getView(R.id.chapter_order_positive).setEnabled(true);
                ((TextView)holder.getView(R.id.chapter_order_positive)).setTextColor(Color.parseColor("#cccccc"));
                holder.getView(R.id.chapter_order_reverse).setEnabled(false);
                ((TextView)holder.getView(R.id.chapter_order_reverse)).setTextColor(Color.parseColor("#39adff"));
            }
        });

    }

    // activity里io线程设置，事件发送前，不然数据会被设置为空
    public void setOtherData(String coverUrl, String author, String title) {
        this.mCoverUrl = coverUrl;
        this.mAuthor = author;
        this.mTitle = title;
    }
}
