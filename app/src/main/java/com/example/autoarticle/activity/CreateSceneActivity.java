package com.example.autoarticle.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autoarticle.NetWork.ServerManager;
import com.example.autoarticle.R;
import com.example.autoarticle.adapter.sceneAdapter;
import com.example.autoarticle.adapter.voiceAdapter;
import com.example.autoarticle.model.CreateResult;
import com.example.autoarticle.model.DataCenter;
import com.example.autoarticle.model.OralChatBean;
import com.example.autoarticle.model.character;
import com.example.autoarticle.model.conversation;
import com.example.autoarticle.model.initBean;
import com.example.autoarticle.model.scenario;
import com.example.autoarticle.utils.L;
import com.example.autoarticle.utils.SpacesItemDecoration;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.autoarticle.config.config.TALK_ITEM;

public class CreateSceneActivity extends AppCompatActivity {

    private String TAG = CreateSceneActivity.class.getSimpleName();
    /**
     * 选择场景列表
     */
    private RecyclerView create_scene_choose_scene;

    /**
     * 场景列表布局管理器
     */
    private GridLayoutManager sceneGridLayoutManager;
    private List<scenario> sceneList;
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
    private List<character> voiceList;
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

    private TextView create_scene_title;

    private scenario scene;

    private character character;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_scene);
        initObject();
        initData();
        initView();

    }

    private void initData() {
        initBean bean = DataCenter.getInstance().getInitBean();
        if (bean == null) {
            L.i(TAG, "initData initBean is null");
            return;
        }
        voiceList = new ArrayList<>();
        voiceList.addAll(bean.getDefault_characters());
        sceneList = new ArrayList<>();
        sceneList.addAll(bean.getDefault_scenarios());
    }

    private void initView() {
        create_scene_choose_scene = findViewById(R.id.create_scene_choose_scene);
        sceneGridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        create_scene_choose_scene.addItemDecoration(new SpacesItemDecoration(10, 10, 10, 20));
        create_scene_choose_scene.setLayoutManager(sceneGridLayoutManager);
        sceneAdapter = new sceneAdapter(this, sceneList);
        sceneAdapter.setOnMakeItemEvent(new sceneAdapter.OnMakeItemEvent() {
            @Override
            public void onItemClick(int position) {
                scene = sceneList.get(position);
            }
        });
        create_scene_choose_scene.setAdapter(sceneAdapter);
        create_scene_button = findViewById(R.id.create_scene_button);
        create_scene_choose_character = findViewById(R.id.create_scene_choose_character);
        choose_character_voice_list = findViewById(R.id.choose_character_voice_list);
        voiceGridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        choose_character_voice_list.addItemDecoration(new SpacesItemDecoration(10, 10, 30, 20));
        choose_character_voice_list.setLayoutManager(voiceGridLayoutManager);
        voiceAdapter = new voiceAdapter(this, voiceList);
        voiceAdapter.setOnMakeItemEvent(new voiceAdapter.OnMakeItemEvent() {
            @Override
            public void onItemClick(int position) {
                character = voiceList.get(position);
            }
        });
        choose_character_voice_list.setAdapter(voiceAdapter);
        create_scene_title = findViewById(R.id.create_scene_title);
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
                        if (scene == null) {
                            Toast.makeText(CreateSceneActivity.this, R.string.choose_scene_first, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (create_scene_choose_character.getVisibility() == View.GONE) {
                            create_scene_choose_character.setVisibility(View.VISIBLE);
                            create_scene_choose_scene.setVisibility(View.GONE);
                            create_scene_button.setText(R.string.create_scene);
                            create_scene_title.setText("角色");
                            return;
                        }
                        if (character == null) {
                            Toast.makeText(CreateSceneActivity.this, R.string.choose_character_first, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        createScene();
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
                choose_character_speed3.setTextColor(getResources().getColor(R.color.white));
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

    public void createScene() {
        OralChatBean bean = new OralChatBean();
        bean.setScenario(scene);
        // character character1=new Gson().fromJson(default_characters, character.class);
        bean.setCharacter(character);
        bean.setAi_level("TBD");
        bean.setAi_speed("TBD");
        bean.setUser(DataCenter.getInstance().getUser());
        ServerManager.CreateScene(bean, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response == null || response.body() == null) {
                        return;
                    }
                    String result = response.body().string();
                    CreateResult createResult = new Gson().fromJson(result, CreateResult.class);
                    conversation bean = new conversation();
                    bean.setConversation_id(createResult.getConversation_id());
                    bean.setCharacter(createResult.getCharacter());
                    bean.setScenario(createResult.getScenario());
                    bean.setMessages(createResult.getGreetings());

                    Intent intent=new Intent(CreateSceneActivity.this, TalkActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(TALK_ITEM,bean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    CreateSceneActivity.this.finish();
                } catch (Exception ex) {
                    ex.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("test api", "t.body:" + t.getMessage());
            }
        });


    }

}