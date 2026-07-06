package com.example.base.netty.client.old;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private Channel channel;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        log.info("接收到了客户端的消息是:" + msg);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
        log.info("服务器连接，连接的channel的短ID是：" +ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channel = null;
        log.info("服务器断开，当前被移除的channel的短ID是：" +ctx.channel());
    }

    public void sendMsg(String msg){
        if (channel != null) {
            channel.writeAndFlush(Unpooled.copiedBuffer((msg + System.getProperty("line.separator")).getBytes()));
        }
    }
}
