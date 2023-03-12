package com.example.autoarticle.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.autoarticle.R;
import com.example.autoarticle.adapter.MainFragmentPagerAdapter;
import com.example.autoarticle.adapter.mainButtonAdapter;
import com.example.autoarticle.model.page;
import com.example.autoarticle.utils.TvRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager main_viewpager;
    private TvRecyclerView main_tab;

    private List<page>pageList;
    /**
     * viewpager翻页
     */
    private ViewPager.OnPageChangeListener opcl;
    /**
     * 功能列表布局管理器
     */
    private GridLayoutManager botton_manager;

    private mainButtonAdapter buttonAdapter;
    private List<String> buttons;
    /**
     * 推荐页适配器
     */
    private MainFragmentPagerAdapter mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initObject();
        initView();
    }
    private void initObject(){
        buttons=new ArrayList<>();
        buttons.add("聊天");
        buttons.add("我的");
        pageList=new ArrayList<>();
        page page=new page();
        page.setName("conversation");
        page page1=new page();
        page1.setName("mine");
        pageList.add(page);
        pageList.add(page1);
        opcl = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                buttonAdapter.PageChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
    }
    private void initView(){
        main_viewpager=findViewById(R.id.main_viewpager);
        mainAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), pageList);
        main_viewpager.setAdapter(mainAdapter);
        main_viewpager.setOffscreenPageLimit(5);
        main_viewpager.addOnPageChangeListener(opcl);

        main_tab=findViewById(R.id.main_tab);
        botton_manager = new GridLayoutManager(this, 2);
        botton_manager.setOrientation(LinearLayoutManager.VERTICAL);
        botton_manager.supportsPredictiveItemAnimations();
        buttonAdapter=new mainButtonAdapter(this,buttons);
        main_tab.setLayoutManager(botton_manager);
        main_tab.setAdapter(buttonAdapter);
    }
    public void PageSelectd(int position){
        main_viewpager.setCurrentItem(position);
    }
}