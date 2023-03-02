package com.example.autoarticle.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int left;
    private int top;
    private int right;
    private int bottom;

    public SpacesItemDecoration(int left,int top,int right,int bottom) {

        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override

    public void getItemOffsets(Rect outRect, View view,

                               RecyclerView parent, RecyclerView.State state) {

        outRect.left = left;

        outRect.right = right;

        outRect.bottom = bottom;

// Add top margin only for the first item to avoid double space between items

        if (parent.getChildPosition(view) == 0)

            outRect.top = top;

    }

}
