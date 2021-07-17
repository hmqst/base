package com.example.base.netty.client.old;

import com.example.base.netty.NettyProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NettyClient {

    @Autowired
    private NettyProperties nettyProperties;

    @Autowired
    private ClientChannelInitializer clientChannelInitializer;

    @Async
    public void start() throws Exception{
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(clientChannelInitializer);

            // Start the baseconfig.
            ChannelFuture channelFuture = b.connect(nettyProperties.getHost(), nettyProperties.getPort()).sync(); // (5)
            // Wait until the connection is closed.
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
