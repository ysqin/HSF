package com.eric.hsf.transport;

import com.eric.hsf.serializer.KryoSerializer;
import com.eric.hsf.serializer.ObjectSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */
public class ObjectEncoder extends MessageToMessageEncoder<Object> {
    private ObjectSerializer serializer=new KryoSerializer();

    public void setSerializer(ObjectSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        byte[] bytes =serializer.serializer(msg);
        ByteBuf buffer= Unpooled.buffer();
        buffer.writeBytes(bytes);
        out.add(buffer);

    }
}
