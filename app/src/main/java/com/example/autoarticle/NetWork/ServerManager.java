package com.example.autoarticle.NetWork;

import com.example.autoarticle.model.DataCenter;
import com.example.autoarticle.model.User;
import com.example.autoarticle.model.UserInfo;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;

/**
 * Created by LBF on 2018/10/17.
 */
public class ServerManager {

    public static final String TAG = "ServerManager";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void getChatRecords(Callback<ResponseBody> callback) {
        User user = DataCenter.getInstance().getUser();
        if (user == null) {
            return ;
        }
        UserInfo userInfo=new UserInfo();
        userInfo.setUser(user);
        String userStr = new Gson().toJson(userInfo);
        RequestBody requestBody = MultipartBody.create(JSON, userStr);
        requests request = RetrofitManager.getInstance().getRetrofit().create(requests.class);
        retrofit2.Call<ResponseBody> responseBodyCall = request.get_chat(requestBody);
        responseBodyCall.enqueue(callback);
    }


}
