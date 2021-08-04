package com.example.base.constant;

import okhttp3.Credentials;

/**
 * @author benben
 * @program base
 * @Description Retrofit用常量數據
 * @date 2021-08-04 14:43
 */
public class RetrofitConstant {

    /**
     * 请求基础地址（必须以 / 结尾）
     */
    public static final String BASE_URL = "http://qqbesh.natappfree.cc/";

    /**
     * 认证接口地址
     */
    public static final String TOKEN_API_URL = "oauth/token";

    /**
     * 认证方式
     */
    public static final String GRANT_TYPE = "password";

    /**
     * token前缀
     */
    public static final String HEADER_PREFIX = "Bearer";

    /**
     * basic认证 CLIENT_ID + CLIENT_SECRET
     */
    public static final String CREDENTIAL = Credentials.basic("admin", "123456");

    /**
     * 认证用户名
     */
    public static final String USER_NAME = "admin";

    /**
     * 认证密码
     */
    public static final String PASS_WORD = "123456";

    /**
     * 拦截器使用的token（用户登陆授权Oauth2等）
     */
    public static String token = "";
}
