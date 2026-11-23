package com.grkj.ytjqy_pad.mvp.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
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
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.model.JytzZyldBean;
import com.grkj.ytjqy_pad.utils.common.DES;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by yusam on 2026/6/24.
 * 经营台账-入库信息
 */

public class JytzInputInfoPresenter extends RxPresenter<JytzInputInfoPresenter.View> {


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
     * 请求贮存设施列表
     */
    public void getZcssList(Map<String, String> params) {
        String dwid = SPUtils.get(mContext, Constants.DWID, "").toString();
        params.put("dwid", dwid);
        String url = ApiUrl.URL_ZCSS_LIST;
        if (!TextUtils.isEmpty(params.get("fwdlbh"))) {
            url = ApiUrl.URL_ZCSS_LIST_BY_PARAM;
        }
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

    /**
     * 请求转移联单信息
     */
    public void getZyldInfo(String ldbh) {
        Map<String,String> params = new HashMap<>();
        params.put("ldbh", ldbh);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_JYTZ_ZYLD_INFO, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                JytzZyldBean bean = GsonUtil.GsonToBean(result,JytzZyldBean.class);
                mView.geZyldInfoSuccess(bean);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext,false));
    }




    /**
     * 检测是否上报成功，是否可打印
     */
    public void checkPrintable(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_CHECK_PRINTABLE, params).map(new HttpResultFunc<Object>());
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



    /**
     * 获取标签信息
     */
    public void getLabelInfo(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("type", "rk");//cf-产废，rk-入库，ck-出库
        params.put("id", id);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl.URL_JYTZ_RK_LABEL_INFO), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object data) {
                String json = GsonUtil.GsonToString(data);
                LabelInfoBean result = GsonUtil.GsonToBean(json,LabelInfoBean.class);
                mView.getLabelInfoSuccess(result);
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


    /*
     * 确认删除
     * */
    public void showDeleteDialog(String id) {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确认删除？");
        //设置弹窗类型
        roundCornerDialog.setDialogType(RoundCornerDialog.TYPE_DELETE);
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                roundCornerDialog.dismiss();
                deleteData(id);
            }

            @Override
            public void onCancelClick() {

            }
        });
        //显示对话框
        roundCornerDialog.show();
    }

    /**
     * 删除数据
     */
    private void deleteData(String id) {
        String url = ApiUrl.getHnUrl(ApiUrl. URL_JYTZ_INPUT_DELETE) + id;
        HashMap<String, String> params = new HashMap<>();
//        params.put("id", id);
        Observable<Object> observable = apiServer.commonRequest(token,SubscriberCallBack.requestBodyParam(params), url ).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mView.deleteDataSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    /**
     * 扫码获取废物信息
     */
    public void getScanInfo(String rkpch, String zcssId) {
        Map<String, String> params = new HashMap<>();
//        params.put("csQrcodeValue", rkpch);
        params.put("zcssId", zcssId);

        try {
            String nameDes = DES.encryptDES(rkpch, App.DESC_KEY);
            params.put("csQrcodeValue", nameDes);
            params.put("isEncrypt", "1");
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(mContext,"扫描失败，请重试！");
            return;
        }

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



    public interface View extends BaseView {

        void getZcssListSuccess(List<ZcssBean> bean);

        void geZyldInfoSuccess(JytzZyldBean bean);

        void getParamListSuccess(List<ParamListBean> bean);

        void saveDataSuccess(Object bean);

        void getLabelInfoSuccess(Object bean);

        void deleteDataSuccess(String bean);

        void getScanInfoSuccess(String bean);


    }


}
