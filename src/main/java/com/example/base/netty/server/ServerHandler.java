package com.example.base.netty.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 用于记录和管理所有客户端的channel
     */
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        StringBuilder content = new StringBuilder(msg.toString());
        log.info("【Netty服务端】接收到客户端 " + ctx.channel() + " 消息:" + content);
        content.append("--服务器回复");
        content.append(System.getProperty("line.separator"));

        //将客户端发送过来的消息刷到所有的channel中
        for (Channel channel : clients) {
            channel.writeAndFlush(Unpooled.copiedBuffer(content.toString().getBytes()));
        }
    }

    /**
     * 客户端创建的时候触发，当客户端连接上服务端之后，就可以获取该channel，然后放到channelGroup中进行统一管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
        log.info("【Netty服务端】客户端连接，连接的channel是：" + ctx.channel());
    }

    /**
     * 客户端销毁的时候触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //当handlerRemoved 被触发时候，channelGroup会自动移除对应的channel
        //clients.remove(ctx.channel());
        log.info("【Netty服务端】客户端断开，当前被移除的channel是：" + ctx.channel());
    }
}
