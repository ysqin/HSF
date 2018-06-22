package com.eric.hsf.protocol;

import java.io.Serializable;

import java.util.Map;

/**
 * @author: Eric
 * @date: 18/06/22
 */
public class ResultWrap implements Serializable {

    /** Field description */
    private Result result;

    /** Field description */
    private Map<Object, Object> attchment;

    /**
     *
     */
    public ResultWrap() {}

    /**
     *
     *
     * @param result
     * @Author: Enter your name here...
     * @date: 18/06/22
     * @version:Enter version here...
     */
    public ResultWrap(Result result) {
        this.result = result;
    }

    /**
     *
     * @return
     */
    public Map<Object, Object> getAttchment() {
        return attchment;
    }

    /**
     *
     * @param attchment
     * @Author: Eric
     * @date: 18/06/22
     * @version:1.0.1
     */
    public void setAttchment(Map<Object, Object> attchment) {
        this.attchment = attchment;
    }

    /**
     *
     * @return
     */
    public Result getResult() {
        return result;
    }

    /**
     *
     * @param result
     * @Author: Eric
     * @date: 18/06/22
     * @version:1.0.1
     */
    public void setResult(Result result) {
        this.result = result;
    }
}


