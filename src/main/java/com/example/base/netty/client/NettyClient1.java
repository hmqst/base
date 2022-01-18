package com.example.base.netty.client;

import com.example.base.netty.NettyProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * Netty客户端
 * @author benben
 * @date 2021-03-24 11:29
 */
@Component
@Slf4j
public class NettyClient1 {

    @Resource
    private NettyProperties nettyProperties;

    private Channel channel,clientChannel;
    private OnResultListener onResultListener;
    private EventLoopGroup workerGroup;
    private boolean isStart = false;

    /**
     * 内部断开重连使用
     */
    private void reConnect(){
        log.info("【Netty客户端】reConnect");
        workerGroup = new NioEventLoopGroup(nettyProperties.getWorkerThread());
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new LineBasedFrameDecoder(1204));
                    pipeline.addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));
                    pipeline.addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));
                    pipeline.addLast(new SimpleChannelInboundHandler<String>(){
                        @Override
                        public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                            //super.channelRead(ctx, msg);
                            if (onResultListener != null){
                                onResultListener.onResult(msg);
                            }
                            log.info("【Netty客户端】接收到数据：" + msg);
                        }

                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            //super.channelActive(ctx);
                            sendMessage("客服端在链接成功时发送的数据");
                            log.info("【Netty客户端】channelActive");
                        }

                        @Override
                        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                            channel = ctx.channel();
                            log.info("【Netty客户端】服务器连接成功，连接的channel的短ID是：" + ctx.channel());
                        }

                        @Override
                        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                            channel = null;
                            log.info("【Netty客户端】服务器断开，当前被移除的channel的短ID是：" + ctx.channel());
                        }

                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                            //super.exceptionCaught(ctx, cause);
                            ctx.close();
                        }


                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            final EventLoop loop = ctx.channel().eventLoop();
                            loop.schedule(NettyClient1.this::reConnect, 1L, TimeUnit.SECONDS);
                            //super.channelInactive(ctx);
                        }
                    });
                }
            });

            // Start the baseconfig.
            ChannelFuture channelFuture = b.connect(nettyProperties.getHost(), nettyProperties.getPort());
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (!channelFuture.isSuccess()) {
                    final EventLoop loop = channelFuture.channel().eventLoop();
                    loop.schedule(this::reConnect, 1L, TimeUnit.SECONDS);
                }
            });
            clientChannel = channelFuture.channel();
        } catch (Exception e){
            if (onResultListener != null){
                onResultListener.onError(e);
            }
        }
    }

    /**
     * 外部调用
     */
    public void connect(){
        if (isStart){
            return;
        }
        isStart = true;
        reConnect();
    }

    /**
     * 断开连接
     */
    public void disConnect(){
        log.info("【Netty客户端】disConnect");
        isStart = false;
        if (channel != null){
            channel.close();
        }
        if (clientChannel != null){
            clientChannel.closeFuture();
            clientChannel.close();
        }
        if (workerGroup != null){
            workerGroup.shutdownGracefully();
        }
    }

    public boolean sendMessage(String msg){
        if (channel != null) {
            channel.writeAndFlush(Unpooled.copiedBuffer((msg + System.getProperty("line.separator")).getBytes()));
            return true;
        }else {
            return false;
        }
    }

    /**
     * 数据回调接口
     */
    public interface OnResultListener {
        void onError(Exception exception);
        void onResult(String json);
    }

    public OnResultListener getOnResultListener() {
        return onResultListener;
    }

    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }
}

