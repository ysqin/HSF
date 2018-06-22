package com.eric.hsf.failover;

import com.eric.hsf.common.HostAndPort;
import com.eric.hsf.loadbalance.LoadBalancer;
import com.eric.hsf.protocol.MethodInvokeMetaWrap;
import com.eric.hsf.protocol.Result;
import com.eric.hsf.protocol.ResultWrap;
import com.eric.hsf.transport.RpcClient;

import java.util.List;

/**
 * Created by Administrator on 2017/12/1.
 */
public class FailFastCluster implements Cluster {
    private RpcClient client;
    private LoadBalancer loadBalancer;

    public FailFastCluster(RpcClient client, LoadBalancer loadBalancer) {
        this.client = client;
        this.loadBalancer = loadBalancer;
    }

    @Override
    public Result invoke(List<HostAndPort> hostAndPorts, MethodInvokeMetaWrap mimw) {
        HostAndPort hostAndPort= loadBalancer.select(hostAndPorts,mimw);
        ResultWrap resultWrap = client.invoke(mimw, hostAndPort);
        return resultWrap.getResult();
    }
}
