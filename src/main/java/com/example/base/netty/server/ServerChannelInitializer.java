package com.example.base.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        /*// websocket基于http协议，所以需要http编解码器
        pipeline.addLast(new HttpServerCodec());
        // 添加对于读写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        // 对httpMessage进行聚合
        pipeline.addLast(new HttpObjectAggregator(1024*64));

        // ================= 上述是用于支持http协议的 ==============

        // websocket 服务器处理的协议，用于给指定的客户端进行连接访问的路由地址
        // 比如处理一些握手动作(ping,pong)
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        // 自定义handler
        pipeline.addLast(new ServerHandler());*/

        // 用换行符\n或者\r\n作为依据，遇到\n或者\r\n都认为是一条完整的消息。
        // 1024报文最大长度 true超出最大长度立刻抛出异常 true解析到的报文不带换行符
        pipeline.addLast(new LineBasedFrameDecoder(1204, true, true));
        // 定长协议 使用时不需要指定编码器
        /// pipeline.addLast(new FixedLengthFrameDecoder(3));
        // 定义反序列化器 将报文ByteBuf解析为java对象
        pipeline.addLast(new StringEncoder(Charset.forName("UTF-8")));
        pipeline.addLast(new StringDecoder(Charset.forName("UTF-8")));
        pipeline.addLast(new ServerHandler());
    }
}
