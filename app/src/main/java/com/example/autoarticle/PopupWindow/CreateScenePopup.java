package com.example.autoarticle.PopupWindow;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autoarticle.NetWork.RetrofitManager;
import com.example.autoarticle.R;
import com.example.autoarticle.activity.MainActivity;
import com.example.autoarticle.adapter.sceneAdapter;
import com.example.autoarticle.model.DataCenter;
import com.example.autoarticle.model.initBean;

import java.util.List;

import retrofit2.Retrofit;

public class CreateScenePopup  extends PopupWindow {

    private MainActivity activity;
    private View contentView;

    private RecyclerView make_scene_list;

    private LinearLayoutManager mLinearLayoutManager;
    private sceneAdapter sceneAdapter;
    private List<String> scene_list;
    private initBean initBean;

    private Button make_scene;

    private String scene_select;

    private RelativeLayout container;
    private Retrofit retrofit;

    public CreateScenePopup(Context context) {
        super(context);
        this.activity=(MainActivity) context;
        retrofit = RetrofitManager.getInstance().getRetrofit();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.main_add_talk_scene, null);
        make_scene_list=contentView.findViewById(R.id.make_scene_list);
        make_scene=contentView.findViewById(R.id.make_scene);
        container=contentView.findViewById(R.id.make_scene_container);
        try{
            scene_list= DataCenter.getInstance().getInitBean().getDefault_scenarios().get(0);
            initBean=DataCenter.getInstance().getInitBean();
        }
        catch (Exception exp){
            Log.e("CreateScenePopup","create scene exp:"+exp.getMessage());
        }
       // btnToPay = contentView.findViewById(R.id.game_detail_score_error_btn_to_pay);
        //获取屏幕的宽高
        this.setContentView(contentView);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);

        this.setFocusable(true);

        initView();
    }

    private void initView(){
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateScenePopup.this.dismiss();
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(activity);
        make_scene_list.setLayoutManager(mLinearLayoutManager);
        sceneAdapter = new sceneAdapter(activity,scene_list);

        sceneAdapter.setOnMakeItemEvent(new sceneAdapter.OnMakeItemEvent() {
            @Override
            public void onItemClick(int position) {
                scene_select=scene_list.get(position);
                sceneAdapter.makeSceneClick(position);
            }
        });
        make_scene_list.setAdapter(sceneAdapter);

        make_scene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               activity.createScene(initBean,scene_select);
                CreateScenePopup.this.dismiss();
            }
        });
    }

    public void showPopupWindow(View parent) {

        if (!this.isShowing()) {

// 以下拉方式显示popupwindow

            this.showAtLocation(parent, Gravity.LEFT, 0, 0);

        } else {
            this.dismiss();
        }

    }

}
