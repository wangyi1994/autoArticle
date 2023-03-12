package com.example.autoarticle.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.autoarticle.activity.MainActivity;
import com.google.gson.Gson;

/**
 * Created by che on 2018/11/19.
 * 仅能用于首页碎片否则会报错
 */
public abstract class BaseMainFragment extends Fragment {
    public final String TAG = getClass().getSimpleName();
    protected MainActivity mainActivity;
    /**
     * 碎片在主页的坐标位置
     */
    public int pageIndex;
    protected Gson gson;

    private int layoutId;
    public View view;




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        gson = new Gson();
        if (view == null) {
            layoutId = getContentViewID();
            view = LayoutInflater.from(mainActivity).inflate(layoutId, container, false);
        }
        init();
        initView();
        initData();
        return view;
    }


    /**
     * 获取布局资源id
     */
    public abstract int getContentViewID();


    public abstract void init();
    public abstract void initView();
    public abstract void initData();




    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
