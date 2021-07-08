package com.sena.senacomic.kuaikanmanhua.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.senacomic.R;
import com.sena.senacomic.kuaikanmanhua.bean.KuaiKanComicListBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KuaiKanComicListRecyclerAdapter extends BaseQuickAdapter<KuaiKanComicListBean.TopicMessageList, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;
    private List<KuaiKanComicListBean.TopicMessageList> mComicList;

    public KuaiKanComicListRecyclerAdapter(Context context) {
        super(R.layout.item_kuai_kan_comic_list);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, KuaiKanComicListBean.TopicMessageList comicMessage) {
        Glide.with(mContext).load(comicMessage.getVertical_image_url()).into((ImageView) holder.getView(R.id.comic_cover_image));
        holder.setText(R.id.comic_title, comicMessage.getTitle());
        holder.setText(R.id.comic_counts, mContext.getString(R.string.kuai_kan_chapter_counts, comicMessage.getComics_count()));
    }

}
