package com.example.base.api.data;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.HashMap;

/**
 * Retrofit自定义API
 */
public interface RetrofitApi {

    /**
     * 查询设备绑定状态
     */
    @POST("device/bindingAdmin")
    Call<Data<String>> checkBindState(@Body HashMap<String, Object> json);
}
