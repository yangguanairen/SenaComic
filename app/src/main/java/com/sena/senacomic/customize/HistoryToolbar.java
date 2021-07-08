package com.sena.senacomic.customize;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.sena.senacomic.R;

import org.jetbrains.annotations.NotNull;

public class HistoryToolbar extends Toolbar {

    private Context mContext;
    private LayoutInflater inflater;

    private View view;
    private EditText searchET;
    private TextView editTV;



    public HistoryToolbar(@NonNull @NotNull Context context) {
        this(context, null);
    }

    public HistoryToolbar(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistoryToolbar(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        initView();

        setContentInsetsRelative(20, 20);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(R.styleable.MyToolbar);
            String hint = array.getString(R.styleable.MyToolbar_searchViewHint);
            if (hint != null) {
                searchET.setHint(hint);

            }
            array.recycle();
        }

    }


    private void initView() {

        if (view == null) {
            inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.toolbar_history, null);

            searchET = view.findViewById(R.id.et_search);
            editTV = view.findViewById(R.id.tv_edit);

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL);
            addView(view, params);
        }
    }

    public void searchETSetOnClickListener(View.OnClickListener listener) {
        searchET.setOnClickListener(listener);
    }

    public void editTVSetOnClickListener(View.OnClickListener listener) {
        editTV.setOnClickListener(listener);
    }

    public void setEditTVText(String s) {
        editTV.setText(s);
    }

//    public void setEditTVContextDescription(String s) {
//        editTV.setContentDescription(s);
//    }
//
//    public String getEditTVContentDescription() {
//        return editTV.getContentDescription().toString();
//    }
}
