package com.example.base.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * @author benben
 * @program base
 * @Description Retrofit 基础请求封装
 * @date 2021-08-04 15:13
 */
public interface RetrofitBaseApi {

    @GET
    Call<String> get(@Url String url,
                     @QueryMap Map<String, String> queryMaps);

    @POST
    Call<String> postJson(@Url String url,
                          @Body Map<String, Object> body);

    @POST
    @FormUrlEncoded
    Call<String> postForm(@Url String url,
                          @FieldMap Map<String, Object> fieldMaps,
                          @QueryMap Map<String, String> queryMaps);

    @POST
    @FormUrlEncoded
    Call<String> postForm(@Url String url,
                          @FieldMap Map<String, Object> fieldMaps);

    @POST
    @Streaming
    Call<ResponseBody> postAndResultStreaming(@Url String url,
                                              @FieldMap Map<String, Object> params,
                                              @QueryMap Map<String, String> queryMaps);
}
