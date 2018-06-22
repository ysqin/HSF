package com.eric.hsf.providers;

import com.eric.hsf.common.HostAndPort;
import com.eric.hsf.protocol.MethodInvokeMeta;
import com.eric.hsf.protocol.MethodInvokeMetaWrap;
import com.eric.hsf.protocol.Result;
import com.eric.hsf.protocol.ResultWrap;
import com.eric.hsf.registry.Registry;
import com.eric.hsf.transport.ObjectDecoder;
import com.eric.hsf.transport.ObjectEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/29.
 */
public class ServiceProvider {
    private  Map<Class,Object> exposeBeanMap;

    /*netty服务器相关*/
    private ServerBootstrap sbt;
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private int port;

    /*注册中心*/
    private Registry registry;

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public ServiceProvider(int port) {
        this.port = port;
    }

    public void init() throws UnknownHostException, InterruptedException {
            sbt=new ServerBootstrap();
            boss=new NioEventLoopGroup();
            worker=new NioEventLoopGroup();
            sbt.group(boss,worker);
            sbt.channel(NioServerSocketChannel.class);

            start();
    }
    public void start() throws InterruptedException, UnknownHostException {
             //初始化通道
            sbt.childHandler(new ChannelInitializer<SocketChannel>() {

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
                    //最终处理者
                    pipeline.addLast(new ChannelHandlerAdapter(){
                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                            System.err.println("错误："+cause);
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                           MethodInvokeMetaWrap mimw= (MethodInvokeMetaWrap) msg;
                           //调用信息
                            MethodInvokeMeta invokeMeta = mimw.getInvokeMeta();
                            //客户端附件信息  -- 不做处理
                            Map<Object, Object> attchment = mimw.getAttchment();

                            //通过 invokeMeta 反射调用本地工厂bean  并且将结果封装ResultWrap
                            Object target = exposeBeanMap.get(invokeMeta.getTargetClass());
                            Method method = target.getClass().getDeclaredMethod(invokeMeta.getMethodName(),
                                    invokeMeta.getParameterTypes());
                            if(method.isAccessible()){
                                method.setAccessible(true);
                            }

                            Result result=new Result();
                            try {
                                Object res = method.invoke(target, invokeMeta.getArgs());
                                result.setResult(res);
                            } catch (Exception e) {
                                result.setException(e);
                            }

                            ResultWrap resultWrap=new ResultWrap();
                            resultWrap.setResult(result);
                            //省略附件参数
                            // ...
                            ChannelFuture channelFuture = ctx.writeAndFlush(resultWrap);
                            channelFuture.addListener(ChannelFutureListener.CLOSE);
                            channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                            channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                        }
                    });
                }
            });
            //注册服务
            String host= InetAddress.getLocalHost().getHostAddress();//自动解析本机ip
            for(Class targetClass : exposeBeanMap.keySet()){
                registry.register(targetClass,new HostAndPort(host,port));
            }

            new Thread(){
                @Override
                public void run() {
                    try {
                        //绑定端口并启动服务
                        System.out.println("我在@"+port+"监听...");
                        ChannelFuture channelFuture = sbt.bind(port).sync();
                        channelFuture.channel().closeFuture().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

    public void setExposeBeanMap(Map<Class, Object> exposeBeanMap) {
        this.exposeBeanMap = exposeBeanMap;
    }

    public void close(){
       boss.shutdownGracefully();
       worker.shutdownGracefully();
   }
}
