package com.grkj.ytjqy_pad.http;


import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.socks.library.KLog;

import rx.functions.Func1;


/**
 * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
 *
 * @param <> Subscriber真正需要的数据类型，也就是Data部分的数据类型
 */

public class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

    @Override
    public T call(HttpResult<T> httpResult) {
        KLog.e("请求结果为：" + GsonUtil.GsonToString(httpResult));
        //这里封装了除去成功信息之外，然后统一抛出错误信息
        if (!"1".equals(httpResult.getStatus())) {
            throw new ApiException(httpResult.getMessage());
        }
        return httpResult.getData();
    }
}