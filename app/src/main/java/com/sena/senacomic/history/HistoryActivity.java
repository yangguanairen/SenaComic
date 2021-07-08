package com.sena.senacomic.history;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.sena.senacomic.R;
import com.sena.senacomic.customize.HistoryToolbar;
import com.sena.senacomic.databinding.ActivityHistoryBinding;
import com.sena.senacomic.dmzj.view.DMZJInfoActivity;
import com.sena.senacomic.kuaikanmanhua.view.KuaiKanAllChapterActivity;
import com.sena.senacomic.mangabz.view.MangabzInfoActivity;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.RealmHelper;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding;
    private HistoryAdapter adapter;

    private HashMap<String, String> itemMap = new HashMap<>();

    private List<String> positionList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initNormalAdapter();

        initToolbar();

        initEditLayout();

    }


    private void initToolbar() {

        getWindow().setStatusBarColor(Color.parseColor("#39adff"));

        @SuppressLint("ResourceType") XmlPullParser parser = getResources().getXml(R.layout.toolbar_history);
        AttributeSet attrs = Xml.asAttributeSet(parser);
        int type = 0;
        while (true) {
            try {
                if (!((type=parser.next()) != XmlPullParser.START_TAG) && type != XmlPullParser.END_TAG) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        HistoryToolbar toolbar = new HistoryToolbar(this, attrs);
        setSupportActionBar(binding.toolbarSearch);

        // 利用textChangeListener发送事件改变item视图格式
        binding.toolbarSearch.editTVSetOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(binding.toolbarSearch.getContentDescription().toString())) {
                    binding.toolbarSearch.setEditTVText("取消");
                    adapter.setFlag("0");
                    binding.flagTv.setText("0");
                    binding.editLayout.setVisibility(View.VISIBLE);
                    binding.toolbarSearch.setContentDescription("1");
                } else {
                    binding.toolbarSearch.setEditTVText("编辑");
                    adapter.setFlag("1");
                    binding.flagTv.setText("1");
                    binding.editLayout.setVisibility(View.GONE);
                    binding.toolbarSearch.setContentDescription("0");
                }
            }
        });

    }

    private void initNormalAdapter() {
        binding.recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(this);
        // 很重要, 所有的编辑操作都是通过它完成
        adapter.setOtherObject(binding.flagTv);

        binding.recyclerViewHistory.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                HistoryBean historyBean = (HistoryBean) adapter.getData().get(position);
                if ("0".equals(binding.toolbarSearch.getContentDescription().toString())) {
                    Intent intent = new Intent();
                    int origin = historyBean.getOrigin();
                    if (origin == AppConstants.KUAI_KAN_ORIGIN) {
                        intent.setClass(HistoryActivity.this, KuaiKanAllChapterActivity.class);
                    } else if (origin == AppConstants.DMZJ_ORIGIN) {
                        intent.setClass(HistoryActivity.this, DMZJInfoActivity.class);
                    } else if (origin == AppConstants.MANGABZ_ORIGIN) {
                        intent.setClass(HistoryActivity.this, MangabzInfoActivity.class);
                    }
                    intent.putExtra(AppConstants.COMIC_ID, historyBean.getComicId());
                    startActivity(intent);
                } else {
                    CheckBox checkBox = (CheckBox) adapter.getViewByPosition(position, R.id.edit_check);
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        itemMap.remove(position+"");
                        positionList.remove(position+"");
                    } else {
                        checkBox.setChecked(true);
                        itemMap.put(position+"", historyBean.getId());
                        positionList.add(position+"");
                    }
                }
            }
        });


        setAdapterData();
    }

    private void setAdapterData() {
        List<HistoryBean> list = RealmHelper.getAllHistory(this);
        if (list.size() == 0) {
            // 设置空页面
            binding.tvNoData.setVisibility(View.VISIBLE);
            binding.recyclerViewHistory.setVisibility(View.GONE);
        } else {
            adapter.setList(list);
        }
    }


    private void initEditLayout() {
        binding.editDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击全选后点击删除，清空HistoryBean表，页面置空
                // 这么操作是由于recyclerview不会一次性加载全部item
                // 当用户为滑动，点击全选删除，如果根据holder进行删除，导致后面未加载数据不能被正确删除
                // none是因为点击全选后contentDescription被指为”none“
                if ("none".equals(binding.editAllSelect.getContentDescription().toString())) {
                    RealmHelper.deleteAllHistory(getApplicationContext());
                    binding.tvNoData.setVisibility(View.VISIBLE);
                    binding.recyclerViewHistory.setVisibility(View.GONE);
                } else {
                    for (String key: positionList) {
                        RealmHelper.deleteHistory(getApplicationContext(), itemMap.get(key));
                        adapter.removeAt(Integer.valueOf(key));
                        // 有时删除后数据不能正确加载，比如删了还会显示。
                        // 重新设置list数据，虽然会回到顶部，但目前只能这样
                        adapter.setList(adapter.getData());
                    }
                }
                // 清空待删除列表
                positionList.clear();
                itemMap.clear();

            }
        });

        binding.editAllSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 利用textChangeListener发送事件改变选中状态
                if ("all".equals(v.getContentDescription().toString())) {
                    adapter.setSelectFlag("all");
                    binding.flagTv.setText("all");
                    binding.editAllSelect.setText("取消全选");
                    binding.editAllSelect.setContentDescription("none");
                } else {
                    adapter.setSelectFlag("none");
                    binding.flagTv.setText("none");
                    binding.editAllSelect.setText("全选");
                    binding.editAllSelect.setContentDescription("all");
                }
            }
        });
    }

    // 从History前往Info/View页面后点击返回，用户可能进行会改变HistoryBean表数据的操作
    // 重新从数据库获取数据，并设置给adapter
    @Override
    protected void onRestart() {
        super.onRestart();
        setAdapterData();
    }
}