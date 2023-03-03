package com.example.autoarticle.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.autoarticle.R;
import com.example.autoarticle.adapter.sceneAdapter;
import com.example.autoarticle.adapter.voiceAdapter;
import com.example.autoarticle.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CreateSceneActivity extends AppCompatActivity {

    /**
     * 选择场景列表
     */
    private RecyclerView create_scene_choose_scene;

    /**
     * 场景列表布局管理器
     */
    private GridLayoutManager sceneGridLayoutManager;
    private List<String> sceneList;
    private sceneAdapter sceneAdapter;

    /**
     * 下一步按钮
     */
    private Button create_scene_button;

    /**
     * 创建角色界面容器
     */
    private LinearLayout create_scene_choose_character;

    /**
     * 选择音色列表
     */
    private RecyclerView choose_character_voice_list;
    private GridLayoutManager voiceGridLayoutManager;
    private List<String> voiceList;
    private voiceAdapter voiceAdapter;
    /**
     * 语速1
     */
    private TextView choose_character_speed1;
    /**
     * 语速2
     */
    private TextView choose_character_speed2;
    /**
     * 语速3
     */
    private TextView choose_character_speed3;
    /**
     * 难度1
     */
    private TextView choose_character_level1;
    /**
     * 难度1
     */
    private TextView choose_character_level2;
    /**
     * 难度1
     */
    private TextView choose_character_level3;

    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_scene);
        initObject();
        initData();
        initView();

    }

    private void initData() {
        voiceList = new ArrayList<>();
        voiceList.add("Ethan");
        voiceList.add("Jack");
        voiceList.add("Prebhat");
        voiceList.add("Ethan");
        voiceList.add("Jack");
        voiceList.add("Prebhat");
        sceneList = new ArrayList<>();
        sceneList.add("Ethan");
        sceneList.add("Jack");
        sceneList.add("Prebhat");
        sceneList.add("Ethan");
        sceneList.add("Jack");
        sceneList.add("Prebhat");
    }

    private void initView() {
        create_scene_choose_scene = findViewById(R.id.create_scene_choose_scene);
        sceneGridLayoutManager = new GridLayoutManager(this,2,RecyclerView.VERTICAL,false);
        create_scene_choose_scene.addItemDecoration(new SpacesItemDecoration(10,10,10,20));
        create_scene_choose_scene.setLayoutManager(sceneGridLayoutManager);
        sceneAdapter = new sceneAdapter(this,sceneList);
        create_scene_choose_scene.setAdapter(sceneAdapter);
        create_scene_button = findViewById(R.id.create_scene_button);
        create_scene_choose_character = findViewById(R.id.create_scene_choose_character);
        choose_character_voice_list = findViewById(R.id.choose_character_voice_list);
        voiceGridLayoutManager = new GridLayoutManager(this,2,RecyclerView.VERTICAL,false);
        choose_character_voice_list.addItemDecoration(new SpacesItemDecoration(10,10,10,20));
        choose_character_voice_list.setLayoutManager(voiceGridLayoutManager);
        voiceAdapter = new voiceAdapter(this,voiceList);
        choose_character_voice_list.setAdapter(voiceAdapter);
        choose_character_speed1 = findViewById(R.id.choose_character_speed1);
        choose_character_speed2 = findViewById(R.id.choose_character_speed2);
        choose_character_speed3 = findViewById(R.id.choose_character_speed3);
        choose_character_level1 = findViewById(R.id.choose_character_level1);
        choose_character_level2 = findViewById(R.id.choose_character_level2);
        choose_character_level3 = findViewById(R.id.choose_character_level3);
        create_scene_button.setOnClickListener(onClickListener);
        choose_character_speed1.setOnClickListener(onClickListener);
        choose_character_speed2.setOnClickListener(onClickListener);
        choose_character_speed3.setOnClickListener(onClickListener);
        choose_character_level1.setOnClickListener(onClickListener);
        choose_character_level2.setOnClickListener(onClickListener);
        choose_character_level3.setOnClickListener(onClickListener);
    }

    private void initObject() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.create_scene_button:
                        if(create_scene_choose_character.getVisibility()==View.GONE){
                            create_scene_choose_character.setVisibility(View.VISIBLE);
                        }
                        else{

                        }
                        break;
                    case R.id.choose_character_speed1:
                        resetSpeed(1);
                        break;
                    case R.id.choose_character_speed2:
                        resetSpeed(2);
                        break;
                    case R.id.choose_character_speed3:
                        resetSpeed(3);
                        break;
                    case R.id.choose_character_level1:
                        resetLevel(1);
                        break;
                    case R.id.choose_character_level2:
                        resetLevel(2);
                        break;
                    case R.id.choose_character_level3:
                        resetLevel(3);
                        break;
                }
            }
        };

    }

    public void resetSpeed(int type) {
        choose_character_speed1.setBackground(null);
        choose_character_speed2.setBackground(null);
        choose_character_speed3.setBackground(null);
        choose_character_speed1.setTextColor(getResources().getColor(R.color.black));
        choose_character_speed2.setTextColor(getResources().getColor(R.color.black));
        choose_character_speed3.setTextColor(getResources().getColor(R.color.black));
        switch (type) {
            case 1:
                choose_character_speed1.setBackground(getResources().getDrawable(R.drawable.create_scene_speed_select));
                choose_character_speed1.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                choose_character_speed2.setBackground(getResources().getDrawable(R.drawable.create_scene_speed_select));
                choose_character_speed2.setTextColor(getResources().getColor(R.color.white));
                break;
            case 3:
                choose_character_speed3.setBackground(getResources().getDrawable(R.drawable.create_scene_speed_select));
                choose_character_speed2.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    public void resetLevel(int type) {
        choose_character_level1.setBackground(null);
        choose_character_level2.setBackground(null);
        choose_character_level3.setBackground(null);
        choose_character_level1.setTextColor(getResources().getColor(R.color.black));
        choose_character_level2.setTextColor(getResources().getColor(R.color.black));
        choose_character_level3.setTextColor(getResources().getColor(R.color.black));
        switch (type) {
            case 1:
                choose_character_level1.setBackground(getResources().getDrawable(R.drawable.create_scene_speed_select));
                choose_character_level1.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                choose_character_level2.setBackground(getResources().getDrawable(R.drawable.create_scene_speed_select));
                choose_character_level2.setTextColor(getResources().getColor(R.color.white));
                break;
            case 3:
                choose_character_level3.setBackground(getResources().getDrawable(R.drawable.create_scene_speed_select));
                choose_character_level3.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }
}