package com.sena.senacomic.kuaikanmanhua.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.senacomic.R;

import org.jetbrains.annotations.NotNull;

public class KuaiKanBrowserRecyclerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    private Context mContext;

    public KuaiKanBrowserRecyclerAdapter(Context context) {
        super(R.layout.item_kuai_kan_browser);
        this.mContext = context;
    }


    @Override
    protected void convert(@NotNull BaseViewHolder holder, String s) {
        Glide.with(mContext).load(s).into((ImageView) holder.getView(R.id.comic_image));
    }

}
