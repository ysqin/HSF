package com.eric.hsf.registry;

import com.eric.hsf.common.HostAndPort;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2017/11/29.
 */
public class ZookeeperRegistry implements  Registry{
    private ZkClient client;
    public ZookeeperRegistry(String serers){
        client=new ZkClient(serers);
    }
    /**
     * 注册服务
     * @param targetService
     * @param hostAndPort
     */
    @Override
    public void register(Class targetService, HostAndPort hostAndPort) {
        String node=PREFIX+"/"+targetService.getName()+SUFFIX;
        if(!client.exists(node)){
            client.createPersistent(node,true);
        }
        String tmpnode=node+"/"+hostAndPort.getHost()+":"+hostAndPort.getPort();
        client.createEphemeral(tmpnode);
    }

    /**
     * 更新 hostAndPorts
     * @param targetService
     * @param hostAndPorts
     */
    @Override
    public void subscrible(Class targetService, final List<HostAndPort> hostAndPorts) {
        String node=PREFIX+"/"+targetService.getName()+SUFFIX;
        client.subscribeChildChanges(node, new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("=====update nodes ======");
                hostAndPorts.clear();
                for (String nodeStr : currentChilds) {
                    HostAndPort hostAndPort=new HostAndPort();
                    hostAndPort.setHost(nodeStr.split(":")[0]);
                    hostAndPort.setPort(Integer.parseInt(nodeStr.split(":")[1]));
                    System.out.println("add nodes："+hostAndPort);
                    hostAndPorts.add(hostAndPort);
                }
            }
        });
    }

    @Override
    public List<HostAndPort> retriveService(Class targetService) {
        String node=PREFIX+"/"+targetService.getName()+SUFFIX;
        List<String> children = client.getChildren(node);
        List<HostAndPort> hostAndPorts=new Vector<HostAndPort>();
        for (String nodeStr : children) {
            HostAndPort hostAndPort=new HostAndPort();
            hostAndPort.setHost(nodeStr.split(":")[0]);
            hostAndPort.setPort(Integer.parseInt(nodeStr.split(":")[1]));
            hostAndPorts.add(hostAndPort);
        }
        return hostAndPorts;
    }

    @Override
    public void close() {
        client.close();
    }
}
