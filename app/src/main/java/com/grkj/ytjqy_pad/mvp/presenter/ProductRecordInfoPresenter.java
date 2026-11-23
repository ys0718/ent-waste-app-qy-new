package com.grkj.ytjqy_pad.mvp.presenter;

import android.text.TextUtils;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ProductDeviceBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by yusam on 2022/3/20.
 * 产废登记列表
 */

public class ProductRecordInfoPresenter extends RxPresenter<ProductRecordInfoPresenter.View> {


    /**
     * 请求参数列表
     */
    public void getParamList(final Map<String, String> params) {
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_PARAM_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<List<ParamListBean>>() {
            @Override
            public void onSuccess(List<ParamListBean> result) {
                mView.getParamListSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    /**
     * 请求废物类型列表
     */
    public void getTypeList() {
        String dwid = SPUtils.get(mContext, Constants.DWID, "").toString();
        Map<String, String> params = new HashMap<>();
        params.put("dwid", dwid);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl. URL_WASTE_TYPE_LIST), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<List<CommonProductInfoBean>>() {
            @Override
            public void onSuccess(List<CommonProductInfoBean> result) {
                mView.getTypeListSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    /**
     * 请求产生设施列表
     */
    public void getProductDeviceList(String fwxxId) {
        Map<String, String> params = new HashMap<>();
        params.put("fwxxId", fwxxId);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl. URL_PRODUCT_DEVICE_LIST), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                mView.getProductDeviceListSuccess(model.getRows());
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }


    /**
     * 请求用户列表
     */
    public void getUserList(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name",name);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_USER_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                mView.getUserListSuccess(model.getRows());
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }


//    /**
//     * 获取标签信息
//     */
//    private void getLabelInfo(String qrcodeValue) {
//        Map<String, String> params = new HashMap<>();
////        params.put("rkpch", rkpch);
//        params.put("qrcodeValue", qrcodeValue);
//        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_LABEL_INFO_CS, params).map(new HttpResultFunc<Object>());
//        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
//            @Override
//            public void onSuccess(Object result) {
//
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                mView.showError(msg);
//            }
//        }, mContext));
//    }


    /**
     * 获取标签信息
     */
    private void getLabelInfo(String qrcodeValue) {
        Map<String, String> params = new HashMap<>();
        params.put("type", "cf");//cf-产废，rk-入库，ck-出库
        params.put("qrCodeValues", qrcodeValue);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl. URL_LABEL_INFO_LIST), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<List<LabelInfoBean>>() {
            @Override
            public void onSuccess(List<LabelInfoBean> result) {
                if (result!=null && result.size()!=0){
                    mView.getLabelInfoSuccess(result.get(0));
                }
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
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
        String url = ApiUrl.URL_PRODUCT_RECORD_DELETE + id;
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


    /**
     * 检测是否上报成功，是否可打印
     */
    public void checkPrintable(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_CHECK_PRINTABLE_CFDJ, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<String>() {
            @Override
            public void onSuccess(String result) {
                getLabelInfo(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }




    public interface View extends BaseView {

        void getTypeListSuccess(List<CommonProductInfoBean> bean);

        void getProductDeviceListSuccess(List<ProductDeviceBean> bean);

        void getUserListSuccess(List<UserBean> list);

        void getParamListSuccess(List<ParamListBean> bean);

        void saveDataSuccess(Object bean);

        void deleteDataSuccess(Object bean);

        void getLabelInfoSuccess(Object bean);

    }


}
