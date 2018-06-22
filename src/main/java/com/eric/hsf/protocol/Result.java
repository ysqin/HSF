package com.eric.hsf.protocol;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/29.
 */
public class Result implements Serializable {
    private Object result;
    private Exception exception;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
