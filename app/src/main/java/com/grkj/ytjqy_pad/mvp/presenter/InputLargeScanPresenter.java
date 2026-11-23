package com.grkj.ytjqy_pad.mvp.presenter;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.HtxxBean;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.model.ZyldBean;
import com.grkj.ytjqy_pad.mvp.view.activity.hn.HnUtils;
import com.grkj.ytjqy_pad.utils.common.DES;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by yusam on 2022/3/20.
 * 扫码出库
 */

public class InputLargeScanPresenter extends RxPresenter<InputLargeScanPresenter.View> {

    /**
     * 扫码获取废物信息
     */
    public void getScanInfo(String rkpch,String zcssId) {
        Map<String, String> params = new HashMap<>();
        params.put("csQrcodeValue", rkpch);
        params.put("zcssId", zcssId);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl. URL_PRODUCT_RECORD_INFO), params).map(new HttpResultFunc<Object>());
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
     * 扫码获取废物信息列表
     */
    public void getScanInfoList(String qrcodeValue,String zcssId) {
        Map<String, String> params = new HashMap<>();

        try {
            String qrcodeDes = DES.encryptDES(qrcodeValue, App.DESC_KEY);
            params.put("qrcodeValue", qrcodeDes);
            params.put("isEncrypt", "1");
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(mContext,"扫描失败，请重试！");
            return;
        }

        params.put("zcssId", zcssId);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl. URL_SCAN_INPUT_INFO_LIST), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                List<CommonProductInfoBean> list = GsonUtil.JsonToList(GsonUtil.GsonToString(result), CommonProductInfoBean.class);
                mView.getScanInfoListSuccess(list);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }



    /**
     * 请求贮存设施列表
     */
    public void getZcssList() {
        String dwid = SPUtils.get(mContext, Constants.DWID, "").toString();
        Map<String,String> params = new HashMap<>();
        params.put("dwid", dwid);
        String url = ApiUrl.URL_ZCSS_LIST;
        Observable<Object> observable = apiServer.CommonGet(token, url, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<List<ZcssBean>>() {
            @Override
            public void onSuccess(List<ZcssBean> result) {
                mView.getZcssListSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
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
        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParamObj(params), ApiUrl.getHnUrl(ApiUrl.URL_PRODUCT_INPUT_SAVE_LIST)).map(new HttpResultFunc<Object>());
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
        params.put("page","1");
        params.put("rows","99");
        params.put("number1",number1);
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
                mView.showError(msg);
            }
        }, mContext, false));
    }


    /**
     * 请求来源单位列表
     */
    public void getFromList(String entername) {
        Map<String, String> params = new HashMap<>();
        params.put("page","1");
        params.put("rows","99");
        params.put("entername",entername);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_FROM_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                String json = GsonUtil.GsonToString(model.getRows());
                List<UserBean> list = GsonUtil.JsonToList(json, UserBean.class);
                mView.getFromListSuccess(list);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }

    /**
     * 请求合同列表
     */
    public void getHtList(String htbh) {
        Map<String, String> params = new HashMap<>();
        params.put("page","1");
        params.put("rows","99");
        params.put("htbh",htbh);
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
                mView.showError(msg);
            }
        }, mContext, false));
    }


    public interface View extends BaseView {
        void getScanInfoSuccess(String bean);

        void getScanInfoListSuccess(List<CommonProductInfoBean> bean);

        void getZcssListSuccess(List<ZcssBean> bean);

        void saveDataSuccess(Object bean);

        void getFromListSuccess(List<UserBean> list);

        void getZyldListSuccess(List<ZyldBean> list);

        void getHtListSuccess(List<HtxxBean> list);

    }

}
