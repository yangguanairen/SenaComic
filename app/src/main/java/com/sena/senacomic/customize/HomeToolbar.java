package com.sena.senacomic.customize;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.sena.senacomic.R;
import com.sena.senacomic.util.LogUtil;


public class HomeToolbar extends Toolbar {

    private LayoutInflater inflater;

    private View view;
    private EditText searchView;
    private ImageView iconIV;

    private Context mContext;

    public HomeToolbar(Context context) {
        this(context, null);
    }

    public HomeToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();


        setContentInsetsRelative(20, 20);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyToolbar);
            // 获得搜索的图标
            Drawable searchIcon = array.getDrawable(R.styleable.MyToolbar_editTextSearchIcon);
            if (searchIcon != null) {
                searchIcon.setBounds(0, 0, searchIcon.getMinimumWidth(), searchIcon.getMinimumHeight());
                searchView.setCompoundDrawables(searchIcon, null, null, null);
            }
            // 获得搜索的hint
            String hint = array.getString(R.styleable.MyToolbar_searchViewHint);
            if (hint != null) {
                searchView.setHint(hint);
            }
            Drawable resourcesIcon = array.getDrawable(R.styleable.MyToolbar_resourcesIcon);
            if (resourcesIcon != null) {
                iconIV.setImageDrawable(resourcesIcon);
            }

            boolean isShowSearchView = array.getBoolean(R.styleable.MyToolbar_isShowSearchView, false);
            if (isShowSearchView) {
                showSearchView();
            } else {
                hideSearchView();
            }
            array.recycle();

        }



    }


    private void initView() {
        if (view == null) {
            inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.toolbar_home, null);

            searchView = view.findViewById(R.id.toolbar_search);
            iconIV = view.findViewById(R.id.icon);

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL);
            addView(view, layoutParams);
        }
    }

    private void showSearchView() {
        if (searchView != null) {
            searchView.setVisibility(View.VISIBLE);
        }
    }

    private void hideSearchView() {
        if (searchView != null) {
            searchView.setVisibility(View.GONE);
        }
    }

    public void setSearchViewOnEditorActionListener(TextView.OnEditorActionListener listener) {
        searchView.setOnEditorActionListener(listener);
    }

}
