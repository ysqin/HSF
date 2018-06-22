package com.eric.hsf.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2017/12/1.
 */
public class KryoSerializer implements ObjectSerializer {
        Kryo kryo = new Kryo();
    @Override
    public byte[] serializer(Object value) {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        Output output=new Output(baos);
        kryo.writeClassAndObject(output, value);
        output.flush();
        return baos.toByteArray();
    }

    @Override
    public Object deserializer(byte[] bytes) {
        Input input=new Input(bytes);
        return kryo.readClassAndObject(input);
    }
}
