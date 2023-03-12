package com.example.autoarticle.fragment;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.autoarticle.NetWork.ServerManager;
import com.example.autoarticle.activity.CreateSceneActivity;
import com.example.autoarticle.model.scenario;
import com.example.autoarticle.PopupWindow.MainToolPopup;
import com.example.autoarticle.R;
import com.example.autoarticle.adapter.talkListAdapter;
import com.example.autoarticle.listener.MainToolClick;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationFragment extends BaseMainFragment {


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



    @Override
    public int getContentViewID() {
        return R.layout.fragment_conversation;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public  void init(){
        gson =new Gson();
        conversations =new ArrayList<>();
        add_click=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                float endX =   view.getX()+20;
//                float endY =  view.getY()+100;
//                initPopWindow(view,endX,endY);
                Intent intent=new Intent(mainActivity, CreateSceneActivity.class);
                startActivity(intent);
            }
        };
        mainToolClick=new MainToolClick() {
            @Override
            public void onToolClick(int position) {
                mPopupWindow.dismiss();
                OptionEntity optionEntity= optionEntities.get(position);
                if(optionEntity.getOptionName().equals("增加场景")){
                    Intent intent=new Intent(mainActivity,CreateSceneActivity.class);
                    startActivity(intent);
                }

            }
        };
        optionEntities =new ArrayList<>();
    }

    @Override
    public  void initData(){
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

    @Override
     public void initView(){
       talkList=view.findViewById(R.id.talk_list);
       main_add_conversations=view.findViewById(R.id.main_add_conversations);

       main_add_conversations.setOnClickListener(add_click);

       mLinearLayoutManager = new LinearLayoutManager(mainActivity);
       talkList.setLayoutManager(mLinearLayoutManager);
       talkListAdapter=new talkListAdapter(mainActivity, conversations);
       talkList.setAdapter(talkListAdapter);
   }



    private void initPopWindow(final View selectedView,float mRawX,float mRawY) {
        if(mPopupWindow==null){
            optionEntities.add(new OptionEntity(0, null, "增加场景"));
            optionEntities.add(new OptionEntity(0, null, "情景模拟"));
            optionEntities.add(new OptionEntity(0, null, "角色扮演"));
            optionEntities.add(new OptionEntity(0, null, "自定义场景"));
            mPopupWindow=new MainToolPopup(mainActivity,optionEntities,mRawX,mRawY);
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