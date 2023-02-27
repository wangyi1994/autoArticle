package com.example.autoarticle.PopupWindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autoarticle.R;
import com.example.autoarticle.adapter.OptionsAdapter;
import com.example.autoarticle.listener.MainToolClick;
import com.example.autoarticle.listener.OnRecyclerViewItemClick;
import com.example.autoarticle.model.OptionEntity;
import com.example.autoarticle.utils.DensityUtil;

import java.util.List;

public class MainToolPopup extends PopupWindow {

    private Activity activity;
    private List<OptionEntity> optionEntities;

    private float rawX ;
    private float rawY ;

    private MainToolClick mainToolClick;

    public void setMainToolClick(MainToolClick mainToolClick){
        this.mainToolClick=mainToolClick;
    }

    public MainToolPopup(Context context,List<OptionEntity> optionEntities,float mRawX,float mRawY) {
        super(context);
        this.activity = (Activity) context;
        this.optionEntities=optionEntities;

        View   mPopContentView = View.inflate(context, R.layout.item_list_option_pop, null);
        RecyclerView rvOptions = (RecyclerView) mPopContentView.findViewById(R.id.recyclerview_options);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvOptions.setLayoutManager(linearLayoutManager);
        OptionsAdapter optionsAdapter = new OptionsAdapter(context);
        optionsAdapter.setOptionEntities(optionEntities);
        optionsAdapter.setOnRecyclerViewItemClick(new OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                mainToolClick.onToolClick(position);
            }
        });
        rvOptions.setAdapter(optionsAdapter);
//        LinearLayout layoutDelete = (LinearLayout) mPopContentView.findViewById(R.id.layout_delete);
        // 在popupWindow还没有弹出显示之前就测量获取其宽高（单位是px像素）
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mPopContentView.measure(w, h);
        int viewWidth = mPopContentView.getMeasuredWidth();//获取测量宽度px
        int viewHeight = mPopContentView.getMeasuredHeight();//获取测量高度px
        final int screenWidth = DensityUtil.getScreenWidth(activity.getWindow().getDecorView().getContext());
        final int screenHeight = DensityUtil.getScreenHeight(activity.getWindow().getDecorView().getContext());

        this.setContentView(mPopContentView);
        this.setWidth(viewWidth);

        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setOutsideTouchable(true);
//        mPopupWindow.setBackgroundDrawable(drawable);
        this.setBackgroundDrawable(new BitmapDrawable());
        int offX = 20; // 可以自己调整偏移
        int offY = 20; // 可以自己调整偏移
         rawX = mRawX;
         rawY = mRawY;
        if (mRawX <= screenWidth / 2) {
            rawX = mRawX + offX;
            if (mRawY < screenHeight / 3) {
                rawY = mRawY;
                this.setAnimationStyle(R.style.pop_anim_left_top); //设置动画
            } else {
                rawY = mRawY - viewHeight - offY;
                this.setAnimationStyle(R.style.pop_anim_left_bottom); //设置动画
            }
        } else {
            rawX = mRawX - viewWidth - offX;
            if (mRawY < screenHeight / 3) {
                rawY = mRawY;
                this.setAnimationStyle(R.style.pop_anim_right_top); //设置动画
            } else {
                rawY = mRawY - viewHeight;
                this.setAnimationStyle(R.style.pop_anim_right_bottom); //设置动画
            }
        }
    }

    public  void showAtLocation(){
        this.showAtLocation(activity.getWindow().getDecorView(), Gravity.NO_GRAVITY, (int) rawX, (int) rawY);
    }

}
