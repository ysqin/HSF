package com.eric.hsf.transport;

import com.eric.hsf.common.HostAndPort;
import com.eric.hsf.protocol.MethodInvokeMetaWrap;
import com.eric.hsf.protocol.ResultWrap;

/**
 * Created by Administrator on 2017/11/29.
 */
public interface RpcClient {
    public void init();
    public ResultWrap invoke(MethodInvokeMetaWrap mimw, HostAndPort hostAndPort);
    public void close();
}
