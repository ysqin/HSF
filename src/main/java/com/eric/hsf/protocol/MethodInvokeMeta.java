package com.eric.hsf.protocol;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/29.
 */
public class MethodInvokeMeta implements Serializable{
    private Class targetClass;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] args;

    public MethodInvokeMeta() {
    }

    public MethodInvokeMeta(Class targetClass, String methodName, Class<?>[] parameterTypes, Object[] args) {
        this.targetClass = targetClass;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
