package com.eric.hsf.failover;

import com.eric.hsf.common.HostAndPort;
import com.eric.hsf.protocol.MethodInvokeMetaWrap;
import com.eric.hsf.protocol.Result;

import java.util.List;

/**
 * Created by Administrator on 2017/12/1.
 */
public interface Cluster {
    public Result invoke(List<HostAndPort> hostAndPorts, MethodInvokeMetaWrap mimw);
}
