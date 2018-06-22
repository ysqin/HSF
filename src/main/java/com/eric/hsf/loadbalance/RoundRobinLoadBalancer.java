package com.eric.hsf.loadbalance;

import com.eric.hsf.common.HostAndPort;
import com.eric.hsf.protocol.MethodInvokeMetaWrap;

import java.util.List;

/**
 * Created by Administrator on 2017/12/1.
 */
public class RoundRobinLoadBalancer implements LoadBalancer {
    private Integer round=0;
    public HostAndPort select(List<HostAndPort> hostAndPorts, MethodInvokeMetaWrap mimw) {
        int i = round % hostAndPorts.size();
        round++;
        if(round<0){
            round=0;
        }
        return hostAndPorts.get(i);
    }
}
