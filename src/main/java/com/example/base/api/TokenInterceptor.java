package com.example.base.api;

import com.alibaba.fastjson.JSON;
import com.example.base.constant.RetrofitConstant;
import okhttp3.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;

/** 回传拦截器 */
public class TokenInterceptor implements Interceptor {

    @Nonnull
    @Override
    public Response intercept(@Nonnull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        int code = response.code();
        // 未登录则登录后重新发起请求
        if (code == HttpStatus.UNAUTHORIZED.value()){
            response.close();
            FormBody formBody = new FormBody.Builder()
                    .add("username", RetrofitConstant.USER_NAME)
                    .add("password", RetrofitConstant.PASS_WORD)
                    .add("grant_type", RetrofitConstant.GRANT_TYPE)
                    .build();
            Request request = new Request.Builder()
                    .url(RetrofitConstant.BASE_URL + RetrofitConstant.TOKEN_API_URL)
                    .addHeader(HttpHeaders.AUTHORIZATION, RetrofitConstant.CREDENTIAL)
                    .post(formBody)
                    .build();
            Response response1 = chain.proceed(request);
            ResponseBody body = response1.body();
            if (response1.code() == HttpStatus.OK.value()){
                if (body != null){
                    HashMap map = JSON.parseObject(body.string(), HashMap.class);
                    if (map.containsKey("access_token")) {
                        // 登录成功
                        RetrofitConstant.token = map.get("access_token").toString();
                        Request authorization = chain.request().newBuilder()
                                .header(HttpHeaders.AUTHORIZATION, RetrofitConstant.HEADER_PREFIX + RetrofitConstant.token).build();
                        return chain.proceed(authorization);
                    }
                }
                // 登录错误
                return chain.proceed(response1.request());
            }else {
                // 登录异常
                return chain.proceed(chain.request());
            }


        }
        return response;
    }
}
