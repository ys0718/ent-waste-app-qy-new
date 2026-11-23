package com.grkj.ytjqy_pad.mvp.presenter;

import android.content.Context;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.HistoryInfoBean;
import com.grkj.ytjqy_pad.mvp.model.HtxxBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ProductDeviceBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.ZyldBean;
import com.grkj.ytjqy_pad.utils.common.DES;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by yusam on 2022/3/20.
 * 扫码出库
 */

public class OutputLargeScanPresenter extends RxPresenter<OutputLargeScanPresenter.View> {

    /**
     * 扫码获取废物信息
     */
    public void getScanInfo(String rkpch) {
        Map<String, String> params = new HashMap<>();
        try {
            String nameDes = DES.encryptDES(rkpch, App.DESC_KEY);
            params.put("rkQrcodeValue", nameDes);
            params.put("isEncrypt", "1");
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(mContext, "扫描失败，请重试！");
            return;
        }
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl.URL_PRODUCT_INPUT_INFO), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.getScanInfoSuccess(GsonUtil.GsonToString(result));
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }

    /**
     * 请求历史列表
     */
    public void getHistoryList(String key){
        Map<String, String> params = new HashMap<>();
        params.put("key",key);
        Observable<Object> observable = apiServer.CommonGet(token,ApiUrl.URL_HISTORY_LIST,params).map(new HttpResultFunc<Object>());
        addSubscription(observable,new SubscriberCallBack(new ApiCallback<List<HistoryInfoBean>>(){
            @Override
            public void onSuccess(List<HistoryInfoBean> result){
                mView.getHistoryListSuccess(result);
            }

            @Override
            public void onFailure(String msg){
//                mView.showError(msg);
            }
        },mContext,true));
    }


    /**
     * 获取待入库列表
     */
    public void getTodoList(Map<String, String> params) {
        params.put(Constants.ROWS_KEY, "999");
        params.put("page", "1");
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl.URL_WASTE_TYPE_LIST_BY_ID_CKXZ), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
//                List<CommonProductInfoBean> list = GsonUtil.JsonToList(GsonUtil.GsonToString(result), CommonProductInfoBean.class);
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);

                String json = GsonUtil.GsonToString(model.getRows());
                List<CommonProductInfoBean> list = GsonUtil.JsonToList(json, CommonProductInfoBean.class);
                mView.getTodoListSuccess(list);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


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
     * 请求处置单位列表
     */
    public void getReceiverList(String entername) {
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("rows", "999");
        params.put("jsqymc", entername);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_ENTER_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                List<UserBean> list = model.getRows();
                mView.getReceiverListSuccess(list);
            }

            @Override
            public void onFailure(String msg) {
//                mView.showError(msg);
                mView.getReceiverListSuccess(new ArrayList<>());
            }
        }, mContext, false));
    }


    //保存弹窗
    public void showSaveDialog(List params) {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确定保存？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                saveData(params);
                roundCornerDialog.dismiss();
            }

            @Override
            public void onCancelClick() {

            }
        });
        //显示对话框
        roundCornerDialog.show();
    }


    /**
     * 保存数据
     */
    public void saveData(List params) {
//        List newList = new ArrayList();
//        if (params.size() > 80) {
//            newList = params.subList(0, 80);
//        } else {
//            newList = params;
//        }
//        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParamObj(newList), ApiUrl.getHnUrl(ApiUrl.URL_PRODUCT_OUTPUT_SAVE_LIST)).map(new HttpResultFunc<Object>());
//        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
//            @Override
//            public void onSuccess(Object result) {
//                if (params.size() > 80) {
//                    List nextList = params.subList(80, params.size());
//                    saveData(nextList);
//                }else {
//                    mView.saveDataSuccess(result);
//                }
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                mView.showError(msg);
//            }
//        }, mContext));

        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParamObj(params), ApiUrl.getHnUrl(ApiUrl.URL_PRODUCT_OUTPUT_SAVE_LIST)).map(new HttpResultFunc<Object>());
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
     * 请求转移联单列表
     */
    public void getZyldList(String number1) {
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("rows", "99");
        params.put("number1", number1);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_ZYLD_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                String json = GsonUtil.GsonToString(model.getRows());
                List<ZyldBean> list = GsonUtil.JsonToList(json, ZyldBean.class);
                mView.getZyldListSuccess(list);
            }

            @Override
            public void onFailure(String msg) {
//                mView.showError(msg);
                mView.getZyldListSuccess(new ArrayList<>());
            }
        }, mContext, false));
    }

    /**
     * 请求合同列表
     */
    public void getHtList(String htbh) {
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("rows", "99");
        params.put("htbh", htbh);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_HTXX_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                String json = GsonUtil.GsonToString(model.getRows());
                List<HtxxBean> list = GsonUtil.JsonToList(json, HtxxBean.class);
                mView.getHtListSuccess(list);
            }

            @Override
            public void onFailure(String msg) {
//                mView.showError(msg);
                mView.getHtListSuccess(new ArrayList<>());
            }
        }, mContext, false));
    }


    /**
     * 请求产生设施列表
     */
    public void getProductDeviceList() {
        Map<String, String> params = new HashMap<>();
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl.URL_ALL_PRODUCT_DEVICE_LIST), addDwid(params)).map(new HttpResultFunc<Object>());
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

    public interface View extends BaseView {
        void getScanInfoSuccess(String bean);

        void getReceiverListSuccess(List<UserBean> bean);

        void getTodoListSuccess(List<CommonProductInfoBean> bean);

        void saveDataSuccess(Object bean);

        void getParamListSuccess(List<ParamListBean> bean);

        void getZyldListSuccess(List<ZyldBean> list);

        void getHtListSuccess(List<HtxxBean> list);

        void getProductDeviceListSuccess(List<ProductDeviceBean> bean);

        void getHistoryListSuccess(List<HistoryInfoBean> bean);
    }

}
