package com.example.base.api.data;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

import javax.annotation.Nonnull;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

/**
 * 異步請求返回值封裝
 * @param <T>
 */
public abstract class ICallBack<T> implements Callback<T> {

    @Override
    public void onResponse(@Nonnull Call<T> call, @Nonnull Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(call, response);
        } else {
            onError("請求失敗", call, new HttpException(response));
        }
    }

    @Override
    public void onFailure(@Nonnull Call<T> call, @Nonnull Throwable t) {
        if (t instanceof UnknownHostException) {
            onError("网络连接失败" + t.getMessage(), call, t);
        } else if (t instanceof InterruptedIOException && t.getMessage().equals("timeout")) {
            onError("网络连接超时", call, t);
        } else {
            onError("网络异常" + t.getMessage(), call, t);
        }
    }

    public abstract void onError(String error, @Nonnull Call<T> call, @Nonnull Throwable t);

    public abstract void onSuccess(@Nonnull Call<T> call, @Nonnull Response<T> response);
}
