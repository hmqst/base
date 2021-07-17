package com.example.base.netty.server;

import com.example.base.netty.NettyProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class NettyServer {

    @Resource
    private NettyProperties nettyProperties;

    @Resource
    private ServerChannelInitializer serverChannelInitializer;

    //@PostConstruct
    @Async
    public void start() throws Exception {
        log.info("【Netty服务端】Netty启动" + Thread.currentThread().getName());
        EventLoopGroup bossGroup = new NioEventLoopGroup(nettyProperties.getBossThread());
        EventLoopGroup workerGroup = new NioEventLoopGroup(nettyProperties.getWorkerThread());
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(serverChannelInitializer)
                    .option(ChannelOption.SO_BACKLOG, nettyProperties.getBacklog())
                    .childOption(ChannelOption.SO_KEEPALIVE, nettyProperties.isKeepalive());

            // Bind and start to accept incoming connections.
            ChannelFuture channelFuture = server.bind(nettyProperties.getHost(), nettyProperties.getPort()).sync();
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            log.info("【Netty服务端】Netty关闭");
        }
    }
}
