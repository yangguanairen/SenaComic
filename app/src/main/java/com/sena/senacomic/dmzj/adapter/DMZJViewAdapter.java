package com.sena.senacomic.dmzj.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.senacomic.R;
import com.sena.senacomic.util.GlideUtil;

import org.jetbrains.annotations.NotNull;

public class DMZJViewAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;

    public DMZJViewAdapter(Context context) {
        super(R.layout.item_dmzj_view);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, String s) {

        Glide.with(mContext).load(GlideUtil.getInstance(mContext).addDMZJCookie(s))
                .into((ImageView) holder.getView(R.id.comic_image));

    }
}
