package com.sena.senacomic.favorite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.sena.senacomic.R;
import com.sena.senacomic.databinding.ActivityFavoriteBinding;
import com.sena.senacomic.dmzj.view.DMZJInfoActivity;
import com.sena.senacomic.kuaikanmanhua.view.KuaiKanAllChapterActivity;
import com.sena.senacomic.mangabz.view.MangabzInfoActivity;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.RealmHelper;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityFavoriteBinding binding;
    private FavoriteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(Color.parseColor("#39adff"));

        binding.recyclerVIewFavorite.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new FavoriteAdapter(this);
        binding.recyclerVIewFavorite.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                FavoriteBean favoriteBean = (FavoriteBean) adapter.getData().get(position);
                Intent intent = new Intent();
                int origin = favoriteBean.getOrigin();
                if (origin == AppConstants.KUAI_KAN_ORIGIN) {
                    intent.setClass(FavoriteActivity.this, KuaiKanAllChapterActivity.class);
                } else if (origin == AppConstants.DMZJ_ORIGIN) {
                    intent.setClass(FavoriteActivity.this, DMZJInfoActivity.class);
                } else if (origin == AppConstants.MANGABZ_ORIGIN) {
                    intent.setClass(FavoriteActivity.this, MangabzInfoActivity.class);
                }
                intent.putExtra(AppConstants.COMIC_ID, favoriteBean.getComicId());
                startActivity(intent);
            }
        });

        setAdapterData();

        binding.resourcesAll.setOnClickListener(this::onClick);
        binding.resourcesKuaiKan.setOnClickListener(this::onClick);
        binding.resourcesDmzj.setOnClickListener(this::onClick);
        binding.resourcesMangabz.setOnClickListener(this::onClick);


    }

    private void setAdapterData() {
        List<FavoriteBean> list = RealmHelper.getAllFavorite(this);
        if (list == null) {
            binding.tvNoData.setVisibility(View.VISIBLE);
            binding.tvNoData.invalidate();
            binding.recyclerVIewFavorite.setVisibility(View.GONE);
        } else {
            adapter.setList(list);
        }
    }


    private void invalidateSort() {
        List<TextView> list = Arrays.asList(binding.resourcesAll, binding.resourcesDmzj, binding.resourcesKuaiKan, binding.resourcesMangabz);
        for (TextView v : list) {
            if (v.isPressed()) {
                v.setTextColor(Color.WHITE);
            } else {
                v.setTextColor(Color.parseColor("#cccccc"));
            }
            v.invalidate();
        }
    }

    @Override
    public void onClick(View v) {

        invalidateSort();

        switch (v.getId()) {
            case R.id.resources_all:
                adapter.setList(RealmHelper.getAllFavorite(this));
                break;
            case R.id.resources_kuai_kan:
                adapter.setList(RealmHelper.queryFavoriteByOrigin(this, AppConstants.KUAI_KAN_ORIGIN));
                break;
            case R.id.resources_dmzj:
                adapter.setList(RealmHelper.queryFavoriteByOrigin(this, AppConstants.DMZJ_ORIGIN));
                break;
            case R.id.resources_mangabz:
                adapter.setList(RealmHelper.queryFavoriteByOrigin(this, AppConstants.MANGABZ_ORIGIN));
                break;
            default:
                break;
        }
    }

    // 从Favorite前往Info页面后点击返回，用户可能进行会改变FavoriteBean表数据的操作
    // 重新从数据库获取数据，并设置给adapter
    @Override
    protected void onRestart() {
        super.onRestart();
        setAdapterData();
    }


}