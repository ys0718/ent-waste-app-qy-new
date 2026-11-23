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
 * Created by yusam on 2022/3/20.
 * 打印标签
 */

public class PrintLabelPresenter extends RxPresenter<PrintLabelPresenter.View> {


    /**
     * 根据入库批次号获取入库信息
     */
    public void printLabel(Map<String,String> params) {
        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParam(params),ApiUrl. URL_PRINT_SK_LABEL).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.printLabelSuccess(GsonUtil.GsonToString(result));
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }



    /**
     * 请求按钮列表
     */
    public void getButtonList() {
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_BUTTONS_LIST, new HashMap<>()).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                boolean isShowTsc = false;
                if (GsonUtil.getListByKey(result,"ydd_print").size()!=0){
                    isShowTsc = true;
                }
                mView.isShowTsc(isShowTsc);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    public interface View extends BaseView {

        void printLabelSuccess(String bean);

        void isShowTsc(boolean isShowTsc);
    }


}
