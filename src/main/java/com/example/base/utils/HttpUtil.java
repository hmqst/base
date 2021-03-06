package com.example.base.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtil {
    private static CloseableHttpClient httpClient = null;

    public static synchronized CloseableHttpClient getHttpClient() {
        if (null == httpClient) {
            //注册访问协议相关的Socket工厂
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", SSLConnectionSocketFactory.getSystemSocketFactory()).build();

            //HttpConnection工厂：皮遏制写请求/解析响应处理器
            HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connectionFactory = new
                    ManagedHttpClientConnectionFactory(DefaultHttpRequestWriterFactory.INSTANCE,
                    DefaultHttpResponseParserFactory.INSTANCE);
            //DNS解析器
            DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;

            //创建池化连接管理器
            PoolingHttpClientConnectionManager poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connectionFactory, dnsResolver);
            //默认为Socket配置
            SocketConfig defaultSocketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
            poolConnManager.setDefaultSocketConfig(defaultSocketConfig);

            // 设置整个连接池的最大连接数
            poolConnManager.setMaxTotal(1000);
            // 每个路由的默认最大连接，每个路由实际最大连接默认为DefaultMaxPerRoute控制，maxTotal是整个池子最大数
            // DefaultMaxPerRoute设置过小无法支持大并发（ConnectPoolTimeoutException: Timeout waiting for connect from pool) 路由是maxTotal的细分
            //每个路由最大连接数
            poolConnManager.setDefaultMaxPerRoute(1000);
            //在从连接池获取连接时，连接不活跃多长时间后需要一次验证，默认2S
            poolConnManager.setValidateAfterInactivity(5 * 1000);

            //默认请求配置
            RequestConfig requestConfig = RequestConfig.custom()
                    //设置连接超时时间
                    .setConnectTimeout(2 * 1000)
                    //设置等待数据超时时间
                    .setSocketTimeout(5 * 1000)
                    //设置从连接池获取连接的等待超时时间
                    .setConnectionRequestTimeout(2000)
                    .build();

            HttpClientBuilder httpClientBuilder = HttpClients.custom();
            httpClientBuilder.setConnectionManager(poolConnManager)
                    //设置连接池不是共享模式
                    .setConnectionManagerShared(false)
                    //定期回调空闲连接
                    .evictIdleConnections(60L, TimeUnit.SECONDS)
                    //定期回收过期
                    .evictExpiredConnections()
                    //连接存活时间，如果不设置，根据长连接信息决定
                    .setConnectionTimeToLive(60, TimeUnit.SECONDS)
                    //设置默认请求配置
                    .setDefaultRequestConfig(requestConfig)
                    // 连接重试策略，是否能keepalive
                    .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
                    //长连接配置，即获取长连接生产多少时间
                    .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                    //设置重试次数，默认是3次；当前是禁用
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false));

            httpClient = httpClientBuilder.build();

            //JVM停止或重启时，关闭连接池释放连接
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }));

        }
        return httpClient;
    }


    /**
     * 发送 GET 请求（HTTP），不带输入数据
     *
     * @param url
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, new HashMap<>());
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     *
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, Object> params) {
        long a = System.currentTimeMillis();
        StringBuilder param = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (i == 0) {
                param.append("?");
            } else {
                param.append("&");
            }
            param.append(entry.getKey()).append("=").append(entry.getValue());
            i++;
        }
        String result = null;
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        HttpGet httpPost;
        try {
            httpPost = new HttpGet(url + param);
            response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();

            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(response.getEntity(), "UTF-8");
                }
            } else {
                //不推荐使用CloseableHttpResponse.close关闭连接，他将直接关闭Socket，导致长连接不能复用
                EntityUtils.consume(response.getEntity());
            }

            String logsb = "##################\n" +
                    "# 发送报文：" + getPlanText(params) + "\n" +
                    "# 响应代码：" + status + "\n" +
                    "# 响应报文：" + result + "\n" +
                    "# 耗时：" + (System.currentTimeMillis() - a) + "\n" +
                    "########################################################################\n";
            System.out.println(logsb);

            return result;
        } catch (IOException e) {
            try {
                if (null != response) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (IOException e1) {
                System.out.println(e.getMessage());
            }
            System.out.println(e.getMessage());
        }
        return result;
    }


    /**
     * 发送 POST 请求（HTTP），K-V形式
     *
     * @param url  接口URL
     * @param json 参数 json
     * @return
     */
    public static String doPost(String url, String json) {
        long a = System.currentTimeMillis();
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        try {
            httpPost.addHeader("Content-Type", "application/json");
            StringEntity postingString = new StringEntity(json, "utf-8");
            httpPost.setEntity(postingString);
            response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(response.getEntity(), "UTF-8");
                }
            } else {
                //不推荐使用CloseableHttpResponse.close关闭连接，他将直接关闭Socket，导致长连接不能复用
                EntityUtils.consume(response.getEntity());
            }

            String logsb = "##################\n" +
                    "# 发送报文：" + json + "\n" +
                    "# 响应代码：" + status + "\n" +
                    "# 响应报文：" + result + "\n" +
                    "# 耗时：" + (System.currentTimeMillis() - a) + "\n" +
                    "########################################################################\n";
            System.out.println(logsb);

            return result;
        } catch (Exception e) {
            try {
                if (null != response) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (IOException e1) {
                System.out.println(e.getMessage());
            }
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     * 参数Map格式化
     *
     * @param map
     * @return
     */
    public static String getPlanText(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey()).
                    append("=").
                    append(entry.getValue())
                    .append("&");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
