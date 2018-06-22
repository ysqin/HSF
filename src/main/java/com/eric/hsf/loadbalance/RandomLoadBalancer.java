package com.eric.hsf.loadbalance;

import com.eric.hsf.common.HostAndPort;
import com.eric.hsf.protocol.MethodInvokeMetaWrap;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/12/1.
 */
public class RandomLoadBalancer implements LoadBalancer {
    @Override
    public HostAndPort select(List<HostAndPort> hostAndPorts, MethodInvokeMetaWrap mimw) {
        int i = new Random().nextInt(hostAndPorts.size());
        return hostAndPorts.get(i);
    }
}
