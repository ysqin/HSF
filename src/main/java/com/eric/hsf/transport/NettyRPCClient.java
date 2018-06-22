package com.eric.hsf.transport;

import com.eric.hsf.common.HostAndPort;
import com.eric.hsf.protocol.MethodInvokeMetaWrap;
import com.eric.hsf.protocol.ResultWrap;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Created by Administrator on 2017/11/29.
 */
public class NettyRPCClient implements RpcClient {
    private Bootstrap bt;
    private EventLoopGroup worker;

    @Override
    public void init(){
        bt=new Bootstrap();
        worker=new NioEventLoopGroup();

        bt.group(worker);
        bt.channel(NioSocketChannel.class);
    }



    @Override
    public ResultWrap invoke(final MethodInvokeMetaWrap mimw, HostAndPort hostAndPort) {

        bt.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                //数据帧解码
                pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
                //解码对象
                pipeline.addLast(new ObjectDecoder());
                //数据帧编码
                pipeline.addLast(new LengthFieldPrepender(2));
                //编码对象
                pipeline.addLast(new ObjectEncoder());
                //添加最终处理类
                pipeline.addLast(new ChannelHandlerAdapter(){
                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        System.err.println("错误："+cause);
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        ChannelFuture channelFuture = ctx.writeAndFlush(mimw);
                        channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                        channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        ResultWrap resultWrap= (ResultWrap) msg;
                        mimw.setResultWrap(resultWrap);
                    }
                });
            }
        });
        try {
            ChannelFuture future = bt.connect(hostAndPort.getHost(), hostAndPort.getPort()).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mimw.getResultWrap();
    }
    @Override
    public void close(){
        worker.shutdownGracefully();
    }
}
