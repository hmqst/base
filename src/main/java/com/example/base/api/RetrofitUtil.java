package com.example.base.api;

import com.example.base.api.data.RetrofitApi;
import com.example.base.constant.RetrofitConstant;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class RetrofitUtil {
    public static final RetrofitApi API;
    public static final RetrofitBaseApi BASE_API;

    static {
        Retrofit retrofit = new Retrofit.Builder()
                // Base请求地址
                .baseUrl(RetrofitConstant.BASE_URL)
                // json序列化
                .addConverterFactory(JacksonConverterFactory.create())
                // 配置OKHttp
                .client(
                        new OkHttpClient.Builder()
                                // 设置超时
                                .connectTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(10, TimeUnit.SECONDS)
                                .callTimeout(10, TimeUnit.SECONDS)
                                .writeTimeout(10, TimeUnit.SECONDS)
                                // 请求拦截提 添加header
                                .addInterceptor(new SignInterceptor())
                                // 回传拦截器 401自动登录
                                .addInterceptor(new TokenInterceptor())
                                .build()
                )
                .build();
        API = retrofit.create(RetrofitApi.class);
        BASE_API = retrofit.create(RetrofitBaseApi.class);
    }
    private static final Pattern PATTERN = Pattern.compile("(((https|http)?://)?([a-z0-9]+[.])|(www.))"
            + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)");

    public static boolean isHttpUrl(String url) {
        return PATTERN.matcher(url).matches();
    }

}
