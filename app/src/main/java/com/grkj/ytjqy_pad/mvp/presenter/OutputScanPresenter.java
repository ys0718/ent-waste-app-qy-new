package com.grkj.ytjqy_pad.mvp.presenter;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 *  Created by yusam on 2022/3/20.
 * 扫码出库
 */

public class OutputScanPresenter extends RxPresenter<OutputScanPresenter.View> {

    /**
     * 根据入库批次号获取入库信息
     */
    public void getOutputInfo(String rkpch) {
        Map<String, String> params = new HashMap<>();
        params.put("rkQrcodeValue", rkpch);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl. URL_PRODUCT_INPUT_INFO), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.getOutputInfoSuccess(GsonUtil.GsonToString(result));
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }

    public interface View extends BaseView {
        void getOutputInfoSuccess(String bean);
    }

}
