package com.sena.senacomic.mangabz.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.senacomic.R;
import com.sena.senacomic.mangabz.bean.MangabzInfoBean;

import org.jetbrains.annotations.NotNull;

public class MangabzInfoAdapter extends BaseQuickAdapter<MangabzInfoBean, BaseViewHolder> {

    private Context mContext;

    public MangabzInfoAdapter(Context context) {
        super(R.layout.item_mangabz_info);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, MangabzInfoBean bean) {
        holder.setText(R.id.continue_read, bean.getName());
    }
}
