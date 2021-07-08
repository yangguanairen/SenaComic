package com.sena.senacomic.kuaikanmanhua.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.sena.senacomic.R;
import com.sena.senacomic.RetrofitService;
import com.sena.senacomic.customize.HomeToolbar;
import com.sena.senacomic.kuaikanmanhua.adapter.KuaiKanSearchRecyclerAdapter;
import com.sena.senacomic.kuaikanmanhua.bean.KuaiKanSearchBean;
import com.sena.senacomic.databinding.ActivityKuaiKanSearchBinding;
import com.sena.senacomic.util.LogUtil;
import com.sena.senacomic.util.RetrofitHelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class KuaiKanSearchActivity extends AppCompatActivity {


    private RetrofitService retrofitService;
    private ActivityKuaiKanSearchBinding binding;
    private KuaiKanSearchRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKuaiKanSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        adapter = new KuaiKanSearchRecyclerAdapter(this);
        binding.recyclerViewSearch.setAdapter(adapter);

        retrofitService = RetrofitHelper.getKuaiKanInstance(this).getServer();

        initToolbar();

    }

    private void initToolbar() {
        // 获取属性组，即布局下所写的属性标签
        @SuppressLint("ResourceType") XmlPullParser parser = getResources().getXml(R.layout.toolbar_home);
        AttributeSet attrs = Xml.asAttributeSet(parser);
        // 遍历Xml根节点，否则返回的属性数量为-1
        int type;
        while (true) {
            try {
                if (!((type=parser.next()) != XmlPullParser.START_TAG && type!=XmlPullParser.END_DOCUMENT))
                    break;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

        }
        // 初始化自定义Toolbar
        HomeToolbar toolbar = new HomeToolbar(this, attrs);
        // 设置toolbar
        setSupportActionBar(binding.customizeToolbar);
        getWindow().setStatusBarColor(Color.parseColor("#39adff"));

        binding.customizeToolbar.setSearchViewOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    LogUtil.e(v.getText().toString());
                    retrofitService.refreshKuaiKanSearch(v.getText().toString(), 20)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<KuaiKanSearchBean>() {
                                @Override
                                public void accept(KuaiKanSearchBean bean) throws Throwable {
                                    adapter.setList(bean.getHits());
                                }
                            });
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                // true键盘不会收缩，false键盘收缩
                return false;
            }
        });
    }

}