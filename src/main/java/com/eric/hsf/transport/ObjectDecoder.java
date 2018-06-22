package com.eric.hsf.transport;

import com.eric.hsf.serializer.KryoSerializer;
import com.eric.hsf.serializer.ObjectSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */
public class ObjectDecoder extends MessageToMessageDecoder<ByteBuf> {
    private ObjectSerializer serializer=new KryoSerializer();

    public void setSerializer(ObjectSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        byte[] values=new byte[msg.readableBytes()];
        msg.readBytes(values);//将缓冲中的数据对到values中
        Object obj =serializer.deserializer(values);
        out.add(obj);
    }
}
