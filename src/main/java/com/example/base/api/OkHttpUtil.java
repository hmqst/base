package com.example.base.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.example.base.api.SignInterceptor;
import com.example.base.api.TokenInterceptor;
import com.example.base.constant.RetrofitConstant;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/** 网络请求工具类 */
public class OkHttpUtil {
    static String token = "";
    static final String baseUrl = RetrofitConstant.BASE_URL;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final MediaType FILEFORM = MediaType.get("multipart/form-data; charset=utf-8");
    private static final MediaType JPG = MediaType.parse("image/jpg");
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .callTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(new SignInterceptor())
            .addInterceptor(new TokenInterceptor())
            .build();

    public static String get(String url) throws IOException {
        Request request = createGetRequest(url);
        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return null;
        } else {
            return responseBody.string();
        }
    }

    public static void getAsync(String url, Callback callback) {
        Request request = createGetRequest(url);
        client.newCall(request).enqueue(callback);

    }

    public static String postJSON(String url, String json) throws IOException {
        Request request = createPostJSONRequest(json, url);
        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return null;
        } else {
            return responseBody.string();
        }
    }

    public static void postJSONAsync(String url, String json, Callback callback) {
        client.newCall(createPostJSONRequest(json, url)).enqueue(callback);
    }

    public static String postFORM(String url, HashMap<String, String> map, String jpgName, File jpgFile) throws IOException {
        Response response = client.newCall(createPostFormRequest(url, map, jpgName, jpgFile)).execute();
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return null;
        } else {
            return responseBody.string();
        }
    }

    public static void postFORMAsync(String url, HashMap<String, String> map, String jpgName, File jpgFile, Callback callback) {
        client.newCall(createPostFormRequest(url, map, jpgName, jpgFile)).enqueue(callback);
    }

    public static String postFORM(String url, HashMap<String, String> map) throws IOException {
        Response response = client.newCall(createPostFormRequest(url, map)).execute();
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return null;
        } else {
            return responseBody.string();
        }
    }

    public static void postFORMAsync(String url, HashMap<String, String> map, Callback callback) {
        client.newCall(createPostFormRequest(url, map)).enqueue(callback);
    }

    private static Request createPostJSONRequest(String json, String url) {
        RequestBody requestBody = RequestBody.create(JSON, json);
        return new Request.Builder()
                .url(baseUrl + url)
                .post(requestBody)
                .build();
    }

    private static Request createPostFormRequest(String url, HashMap<String, String> map, String jpgName, File jpgFile) {
        RequestBody fileBody = RequestBody.create(JPG, jpgFile);
        MultipartBody.Builder requestBuilder = new MultipartBody.Builder();
        requestBuilder.setType(MultipartBody.FORM).addFormDataPart(jpgName, jpgFile.getName(), fileBody);
        for (String key : map.keySet()) {
            if (map.get(key) != null) {
                String data = map.get(key);
                if (data == null) {
                    data = "";
                }
                requestBuilder.addFormDataPart(key, data);
            }
        }
        return new Request.Builder()
                .url(baseUrl + url)
                .post(requestBuilder.build())
                .build();
    }

    private static Request createPostFormRequest(String url, HashMap<String, String> map) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : map.keySet()) {
            if (map.get(key) != null) {
                String data = map.get(key);
                if (data == null) {
                    data = "";
                }
                builder.add(key, data);
            }
        }
        return new Request.Builder()
                .url(baseUrl + url)
                .post(builder.build())
                .build();
    }

    private static Request createGetRequest(String url) {
        return new Request.Builder()
                .url(baseUrl + url)
                .build();
    }
}
