package com.grkj.ytjqy_pad.http;

/**
 * 这是封装了最外层的数据结构
 *
 * @anthor yusam
 * created on 2018/7/19
 */

public class HttpResult<T> {
    private String status;
    private T data;
    private String message;
    private Object e;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getE() {
        return e;
    }

    public void setE(Object e) {
        this.e = e;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (null != data) {
            sb.append(data.toString());
        }
        return sb.toString();
    }
}
