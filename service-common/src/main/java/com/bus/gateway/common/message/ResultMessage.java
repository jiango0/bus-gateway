package com.bus.gateway.common.message;

public class ResultMessage<T> {

    private boolean status = false;

    private String code;

    private String msg;

    private T data;

    public static ResultMessage returnStatus(boolean status) {
        return new ResultMessage(status, null, null, null);
    }

    public ResultMessage() {}

    public ResultMessage(boolean status, String code, String msg, T data) {
        this.status = status;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
