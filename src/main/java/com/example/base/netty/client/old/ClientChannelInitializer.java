package com.example.base.netty.client.old;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.springframework.stereotype.Component;

@Component
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new LineBasedFrameDecoder(1204, true, true));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new ClientHandler());
    }
}
