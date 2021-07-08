package com.sena.senacomic.dmzj.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.senacomic.R;
import com.sena.senacomic.dmzj.bean.DMZJSearchBean;
import com.sena.senacomic.util.GlideUtil;

import org.jetbrains.annotations.NotNull;

public class DMZJSearchAdapter extends BaseQuickAdapter<DMZJSearchBean, BaseViewHolder> {

    private Context mContext;

    public DMZJSearchAdapter(Context context) {
        super(R.layout.item_dmzj_search);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, DMZJSearchBean bean) {
        Glide.with(mContext)
                .load(GlideUtil.getInstance(mContext).addDMZJCookie(mContext.getString(R.string.dmzj_comic_cover_url, bean.getCover())))
                .into((ImageView) holder.getView(R.id.comic_cover_image));
        holder.setText(R.id.comic_title, bean.getName().split(" ")[0]);
        holder.setText(R.id.comic_author, bean.getAuthors());
        holder.setText(R.id.comic_classify, bean.getTypes());
        holder.setText(R.id.comic_update_time, bean.getLast_updatetime());
        holder.setText(R.id.comic_status, bean.getStatus());
    }
}
