package com.sena.senacomic.dmzj.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.senacomic.R;
import com.sena.senacomic.dmzj.bean.DMZJClassifyBean;
import com.sena.senacomic.util.GlideUtil;

import org.jetbrains.annotations.NotNull;

public class DMZJClassifyAdapter extends BaseQuickAdapter<DMZJClassifyBean, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    public DMZJClassifyAdapter(Context context) {
        super(R.layout.item_dmzj_classify);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, DMZJClassifyBean bean) {

        Glide.with(mContext)
                .load(GlideUtil.getInstance(mContext).addDMZJCookie(mContext.getString(R.string.dmzj_comic_cover_url, bean.getCover())))
                .into((ImageView) holder.getView(R.id.comic_cover_image));
        holder.setText(R.id.comic_title, bean.getName());
        holder.setText(R.id.comic_author, bean.getAuthors());

    }
}
