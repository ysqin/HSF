package com.eric.hsf.loadbalance;

import com.eric.hsf.common.HostAndPort;
import com.eric.hsf.protocol.MethodInvokeMetaWrap;

import java.util.List;

/**
 * Created by Administrator on 2017/12/1.
 */
public interface LoadBalancer {
    HostAndPort select(List<HostAndPort> hostAndPorts, MethodInvokeMetaWrap mimw);
}
