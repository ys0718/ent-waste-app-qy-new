package com.grkj.ytjqy_pad.http;

/**
 * 这是请求参数后的结果回调
 *
 * @anthor yusam
 * created on 2018/7/19
 */
public interface ApiCallback<T> {

    void onSuccess(T model);

    void onFailure(String msg);
}
