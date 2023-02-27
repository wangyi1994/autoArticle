package com.example.autoarticle.NetWork;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface requests {
    @GET("/oral_init")
    Call<ResponseBody> init();
    @POST("/oral_chat")
    Call<ResponseBody> getResult( @Body RequestBody body);

    @POST("/create_conversation")
    Call<ResponseBody> create_conversation( @Body RequestBody body);

}