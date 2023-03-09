package com.example.autoarticle.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.example.autoarticle.R;


/**
 * 自定义进度条
 *
 * @author liubeifu
 */
public class LoadingView extends View {
    private RotateAnimation animation;
    private LinearInterpolator linearInterpolator;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linearInterpolator = new LinearInterpolator();
        this.setBackgroundResource(R.drawable.loading);
    }


    /**
     * 手动控制动画开始
     */
    public void startAnimator() {
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(800);
        animation.setRepeatCount(-1);//动画的反复次数
        animation.setInterpolator(linearInterpolator);
//        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
        startAnimation(animation);//開始动画
    }

    /**
     * 手动控制动画停止
     */
    public void stopAnimator() {
        clearAnimation();
    }


}