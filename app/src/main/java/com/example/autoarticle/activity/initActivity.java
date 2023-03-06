package com.example.autoarticle.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.autoarticle.NetWork.RetrofitManager;
import com.example.autoarticle.NetWork.ServerManager;
import com.example.autoarticle.NetWork.requests;
import com.example.autoarticle.R;
import com.example.autoarticle.command.C;
import com.example.autoarticle.model.ChatMessage;
import com.example.autoarticle.model.CreateResult;
import com.example.autoarticle.model.DataCenter;
import com.example.autoarticle.model.User;
import com.example.autoarticle.model.character;
import com.example.autoarticle.model.conversation;
import com.example.autoarticle.model.initBean;
import com.example.autoarticle.model.scenario;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class initActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private Gson gson;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        init();
        initData();
    }
    private void init(){
        gson =new Gson();
        retrofit = RetrofitManager.getInstance().getRetrofit();
        User user=new User();
        user.setId("test_userid1");
        DataCenter.getInstance().setUser(user);
    }
    private void initData(){
        requests request=retrofit.create(requests.class);
        Call<ResponseBody> init = request.init();
        init.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    if(response==null){
                        return;
                    }
                    String result=response.body().string();
                    initBean initBean=gson.fromJson(result,initBean.class);
                    DataCenter.getInstance().setInitBean(initBean);

                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
                finally {
                    getChatRecord();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("test api","t.body:"+t.getMessage());
                getChatRecord();
            }
        });
    }
    private void getChatRecord(){
        ServerManager.getChatRecords(new Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                    try{
                        if(response==null||response.body()==null){
                            return;
                        }
                        String result=response.body().string();
                        conversation[] array = new Gson().fromJson(result,conversation[].class);
                        List<conversation> list = Arrays.asList(array);
                        DataCenter.getInstance().setConversations(list);

                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                    goActivity();
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    Log.i("test api","t.body:"+t.getMessage());
                    goActivity();
                }
            }
        );
    }
    private void goActivity(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}