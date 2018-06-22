package com.eric.hsf.registry;

import com.eric.hsf.common.HostAndPort;

import java.util.List;

/**
 * Created by Administrator on 2017/11/29.
 */
public interface Registry {
    public String PREFIX="/RPC";
    public String SUFFIX="/providers";

    public void register(Class targetService, HostAndPort hostAndPort);

    public void subscrible(Class targetService, List<HostAndPort> hostAndPorts);

    public List<HostAndPort> retriveService(Class targetService);

    public void close();
}
