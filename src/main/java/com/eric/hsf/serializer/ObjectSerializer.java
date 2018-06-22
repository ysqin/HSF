package com.eric.hsf.serializer;

/**
 * Created by Administrator on 2017/12/1.
 */
public interface ObjectSerializer {
    public byte[] serializer(Object value);
    public Object deserializer(byte[] bytes);
}
