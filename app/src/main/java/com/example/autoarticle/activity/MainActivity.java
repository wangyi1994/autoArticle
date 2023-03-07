package com.example.autoarticle.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.autoarticle.NetWork.ServerManager;
import com.example.autoarticle.command.C;
import com.example.autoarticle.model.scenario;
import com.example.autoarticle.NetWork.RetrofitManager;
import com.example.autoarticle.NetWork.requests;
import com.example.autoarticle.PopupWindow.MainToolPopup;
import com.example.autoarticle.R;
import com.example.autoarticle.adapter.talkListAdapter;
import com.example.autoarticle.listener.MainToolClick;
import com.example.autoarticle.model.ChatMessage;
import com.example.autoarticle.model.CreateResult;
import com.example.autoarticle.model.DataCenter;
import com.example.autoarticle.model.OptionEntity;
import com.example.autoarticle.model.OralChatBean;
import com.example.autoarticle.model.character;
import com.example.autoarticle.model.initBean;
import com.example.autoarticle.model.conversation;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.autoarticle.activity.TalkActivity.JSON;

public class MainActivity extends AppCompatActivity {


    private RecyclerView talkList;
    private LinearLayoutManager mLinearLayoutManager;
    private talkListAdapter talkListAdapter;
    private List<conversation> conversations;

    /**
     * 添加场景按钮
     */
    private Button main_add_conversations;

    private View.OnClickListener add_click;

    /**
     * 功能弹窗点击事件
     */
    private MainToolClick mainToolClick;


    private MainToolPopup mPopupWindow;



    private  List<OptionEntity> optionEntities;


    private Retrofit retrofit;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initView();
        initData();
    }
    private void init(){
        gson =new Gson();
        conversations =new ArrayList<>();
        retrofit = RetrofitManager.getInstance().getRetrofit();
        add_click=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float endX =   view.getX()+20;
                float endY =  view.getY()+100;
                initPopWindow(view,endX,endY);
            }
        };
        mainToolClick=new MainToolClick() {
            @Override
            public void onToolClick(int position) {
                mPopupWindow.dismiss();
                OptionEntity optionEntity= optionEntities.get(position);
                if(optionEntity.getOptionName().equals("增加场景")){
                    Intent intent=new Intent(MainActivity.this,CreateSceneActivity.class);
                    startActivity(intent);
                }

            }
        };
        optionEntities =new ArrayList<>();
    }

    private void initData(){
        if(DataCenter.getInstance().getConversations()==null||DataCenter.getInstance().getConversations().size()==0){
            if(DataCenter.getInstance().getInitBean()!=null){
                createScene(DataCenter.getInstance().getInitBean());
            }
        }
        else{
            conversations.addAll(DataCenter.getInstance().getConversations());
            talkListAdapter.notifyDataSetChanged();
        }
    }

   private void initView(){
       talkList=findViewById(R.id.talk_list);
       main_add_conversations=findViewById(R.id.main_add_conversations);

       main_add_conversations.setOnClickListener(add_click);

       mLinearLayoutManager = new LinearLayoutManager(MainActivity.this);
       talkList.setLayoutManager(mLinearLayoutManager);
       talkListAdapter=new talkListAdapter(this, conversations);
       talkList.setAdapter(talkListAdapter);
   }


    private void initPopWindow(final View selectedView,float mRawX,float mRawY) {
        if(mPopupWindow==null){
            optionEntities.add(new OptionEntity(0, null, "增加场景"));
            optionEntities.add(new OptionEntity(0, null, "情景模拟"));
            optionEntities.add(new OptionEntity(0, null, "角色扮演"));
            optionEntities.add(new OptionEntity(0, null, "自定义场景"));
            mPopupWindow=new MainToolPopup(MainActivity.this,optionEntities,mRawX,mRawY);
            mPopupWindow.setMainToolClick(mainToolClick);
        }

        mPopupWindow.showAtLocation();

    }

    public void createScene(initBean initBean){
        OralChatBean bean=new OralChatBean();
        character default_character=initBean.getDefault_characters().get(0);
        scenario default_scenario=initBean.getDefault_scenarios().get(0);
        bean.setScenario(default_scenario);
       // character character1=new Gson().fromJson(default_characters, character.class);
        bean.setCharacter(default_character);
        bean.setAi_level("TBD");
        bean.setAi_speed("TBD");
        bean.setUser(DataCenter.getInstance().getUser());

        ServerManager.CreateScene(bean,new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    if(response==null||response.body()==null){
                        return;
                    }
                    String result=response.body().string();
                    CreateResult createResult=new Gson().fromJson(result,CreateResult.class);
                    conversation bean=new conversation();
                    bean.setConversation_id(createResult.getConversation_id());
                    bean.setCharacter(createResult.getCharacter());
                    bean.setScenario(createResult.getScenario());
                    bean.setMessages(createResult.getGreetings());
                    conversations.add(bean);
                    talkListAdapter.notifyDataSetChanged();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("test api","t.body:"+t.getMessage());
            }
        });


    }

}