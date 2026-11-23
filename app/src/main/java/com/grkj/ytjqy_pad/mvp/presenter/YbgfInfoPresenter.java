package com.grkj.ytjqy_pad.mvp.presenter;

import com.google.gson.Gson;
import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ProductDeviceBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.ybgf.GfMlsGygfScheduleGroup;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by yusam on 2026/5/23.
 * 一般固废信息(美的)
 */

public class YbgfInfoPresenter extends RxPresenter<YbgfInfoPresenter.View> {



    /**
     * 请求产生设施列表
     */
    public void getProductDeviceList() {
        Map<String, String> params = new HashMap<>();
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl. URL_CAR_ROOM_LIST), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                KLog.e(GsonUtil.GsonToString(result));
//                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                mView.getProductDeviceListSuccess(GsonUtil.JsonToList(result, GfMlsGygfScheduleGroup.class));
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }




    /**
     * 保存数据
     */
    public void saveData(final Map<String, String> params, final String url) {
        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParam(params), url).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.saveDataSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }

    /**
     * 删除数据
     */
    public void deleteData(String id) {
        String url = ApiUrl.URL_YBGF_DELETE + id;
        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParam(new HashMap<>()), url).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.deleteDataSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }




    public interface View extends BaseView {
        void getProductDeviceListSuccess(List<GfMlsGygfScheduleGroup> list);

        void saveDataSuccess(Object bean);

        void deleteDataSuccess(Object bean);


    }


}
