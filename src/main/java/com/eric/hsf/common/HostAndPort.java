package com.eric.hsf.common;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/29.
 */
public class HostAndPort implements Serializable{
    private String host;
    private int port;

    public HostAndPort() {
    }

    public HostAndPort(String host, int port) {

        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "HostAndPort{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
