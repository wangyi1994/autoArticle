package com.example.autoarticle.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.autoarticle.fragment.BaseMainFragment;
import com.example.autoarticle.fragment.ConversationFragment;
import com.example.autoarticle.fragment.MineFragment;
import com.example.autoarticle.model.page;

import java.util.List;

public class MainFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<page> pages;

    public MainFragmentPagerAdapter(FragmentManager fm, List<page> pages) {
        super(fm);
        this.pages = pages;
    }


    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        BaseMainFragment f;
        if (pages.get(position).getName().equals("mine")) {
            f = new MineFragment();
        } else if (pages.get(position).getName().equals("conversation")) {
            f = new ConversationFragment();
        }
        else {
            return null;
        }
        Bundle bundle1 = new Bundle();
        f.setArguments(bundle1);
        return f;
    }
}