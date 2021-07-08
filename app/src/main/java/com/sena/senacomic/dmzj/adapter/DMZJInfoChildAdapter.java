package com.sena.senacomic.dmzj.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.senacomic.R;
import com.sena.senacomic.dmzj.bean.DMZJInfoBean;

import org.jetbrains.annotations.NotNull;

public class DMZJInfoChildAdapter extends BaseQuickAdapter<DMZJInfoBean.Data, BaseViewHolder> {

    private Context mContext;

    public DMZJInfoChildAdapter(Context context) {
        super(R.layout.item_dmzj_list_child);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, DMZJInfoBean.Data data) {

        holder.setText(R.id.continue_read, data.getChapter_name());

    }
}
