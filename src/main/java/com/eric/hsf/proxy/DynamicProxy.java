package com.eric.hsf.proxy;

import com.eric.hsf.common.HostAndPort;
import com.eric.hsf.failover.Cluster;
import com.eric.hsf.protocol.MethodInvokeMeta;
import com.eric.hsf.protocol.MethodInvokeMetaWrap;
import com.eric.hsf.protocol.Result;
import com.eric.hsf.registry.Registry;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by Administrator on 2017/12/1.
 */
public class DynamicProxy implements InvocationHandler,FactoryBean{
    private Class targetInterface;

    private List<HostAndPort> hostAndPorts;//当前服务列表
    private Registry registry;//获取服务列表实现服务列表的监测
    private Cluster cluster;

    public DynamicProxy(Class targetInterface, Registry registry, Cluster cluster) {
        this.targetInterface = targetInterface;
        this.registry = registry;
        this.cluster = cluster;
        //查询服务列表
        hostAndPorts = registry.retriveService(targetInterface);
        //订阅服务变更
        registry.subscrible(targetInterface,hostAndPorts);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        MethodInvokeMeta invokeMeta=new MethodInvokeMeta(targetInterface,
                method.getName(),method.getParameterTypes(),args);
        MethodInvokeMetaWrap mimw=new MethodInvokeMetaWrap(invokeMeta);
        //负责发送网络请求
        Result result = cluster.invoke(hostAndPorts, mimw);
        if(result.getException()!=null) {
            throw result.getException();
        }
        return result.getResult();
    }

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(DynamicProxy.class.getClassLoader(),
                new Class[]{targetInterface},this);
    }

    @Override
    public Class<?> getObjectType() {
        return targetInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
