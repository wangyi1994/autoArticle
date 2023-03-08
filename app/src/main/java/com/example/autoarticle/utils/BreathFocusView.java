package com.example.autoarticle.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.autoarticle.R;

import androidx.annotation.Nullable;


/**
 * @author LJ
 * @data 2020/7/6.
 */

public class BreathFocusView extends View {
    private Context mContext;
    public Drawable[] bgs;
    private MyRunable runable;

    public BreathFocusView(Context context) {
        this(context, null);
    }

    public BreathFocusView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BreathFocusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setVisibility(GONE);
        initBreath();
    }

    /**
     * 初始化焦点展示方式为呼吸灯
     */
    private void initBreath() {
        bgs = new Drawable[22];
        bgs[0] = getResources().getDrawable(R.drawable.bg_breath_1);
        bgs[1] = getResources().getDrawable(R.drawable.bg_breath_2);
        bgs[2] = getResources().getDrawable(R.drawable.bg_breath_3);
        bgs[3] = getResources().getDrawable(R.drawable.bg_breath_4);
        bgs[4] = getResources().getDrawable(R.drawable.bg_breath_5);
        bgs[5] = getResources().getDrawable(R.drawable.bg_breath_6);
        bgs[6] = getResources().getDrawable(R.drawable.bg_breath_7);
        bgs[7] = getResources().getDrawable(R.drawable.bg_breath_8);
        bgs[8] = getResources().getDrawable(R.drawable.bg_breath_9);
        bgs[9] = getResources().getDrawable(R.drawable.bg_breath_10);
        bgs[10] = getResources().getDrawable(R.drawable.bg_breath_11);
        bgs[11] = bgs[9];
        bgs[12] = bgs[8];
        bgs[13] = bgs[7];
        bgs[14] = bgs[6];
        bgs[15] = bgs[5];
        bgs[16] = bgs[4];
        bgs[17] = bgs[3];
        bgs[18] = bgs[2];
        bgs[19] = bgs[1];
        runable = new MyRunable();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            post(runable);
        } else {
            removeCallbacks(runable);
        }
    }

    /**
     * 隐藏
     */
    public void gone() {
        clearAnimation();
        setVisibility(GONE);
    }

    class MyRunable implements Runnable {
        public void setX() {
            x = 0;
        }

        int x = 0;

        @Override
        public void run() {
            setBackground(bgs[x % 20]);
            x++;
            postDelayed(this, 90);
        }
    }
}
