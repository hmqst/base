package com.example.base.api;

import com.example.base.constant.RetrofitConstant;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.HttpHeaders;

import javax.annotation.Nonnull;
import java.io.IOException;

/** 上传拦截器 */
public class SignInterceptor implements Interceptor {

    @Nonnull
    @Override
    public Response intercept(@Nonnull Chain chain) throws IOException {
        Request request = chain.request();
        if (request.header(HttpHeaders.AUTHORIZATION) == null){
            // 添加token
            Request authorization = request.newBuilder().addHeader("Authorization", "Bearer" + RetrofitConstant.token).build();
            return chain.proceed(authorization);
        }
        return chain.proceed(request);
    }
}
