package com.example.base.netty;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "netty")
@Data
public class NettyProperties {
    private String host;
    private int port;
    private int bossThread;
    private int workerThread;
    private boolean keepalive;
    private int backlog;
}
