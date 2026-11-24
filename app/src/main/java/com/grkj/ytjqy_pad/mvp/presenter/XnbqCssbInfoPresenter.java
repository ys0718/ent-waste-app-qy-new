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
import com.grkj.ytjqy_pad.mvp.model.HtxxBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ProductDeviceBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.model.ZyldBean;
import com.grkj.ytjqy_pad.mvp.model.xnbq.GfYbqyTzNewCsRkXnbq;
import com.grkj.ytjqy_pad.mvp.model.xnbq.GfYbqyXxbaZxlyczss;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by yusam on 2026/10/28.
 * 虚拟标签-立产立清-产生申报信息
 */

public class XnbqCssbInfoPresenter extends RxPresenter<XnbqCssbInfoPresenter.View> {


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
        Observable<Object> observable = apiServer.CommonGet(token, url, addDwid(params)).map(new HttpResultFunc<Object>());
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

                KLog.e();

                if (result != null) {
                    PageBean model = GsonUtil.objectToBean(result, PageBean.class);

                    if (model.getRows() != null) {
                        String json = GsonUtil.GsonToString(model.getRows());
                        List<ZyldBean> list = GsonUtil.JsonToList(json, ZyldBean.class);
                        mView.getZyldListSuccess(list);
                    }
                }
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
        params.put("page", "1");
        params.put("rows", "99");
        params.put("entername", entername);
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
                mView.showError(msg);
            }
        }, mContext, false));
    }



    /**
     * 获取入库信息
     */
    public void getRkInfo(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl.URL_XNBQ_CSSB_INFO), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.getRkInfoSuccess(result);
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
    public void getLabelInfo(String qrcodeValue) {
        Map<String, String> params = new HashMap<>();
        params.put("type", "xnbq-csrk");//cf-产废，rk-入库，ck-出库 xnbq-csrk-虚拟标签产生入库
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
     * 获取标签列表信息
     */
    public void getLabelInfoList(String qrcodeValues) {
        Map<String, String> params = new HashMap<>();
        params.put("type", "rk");
        params.put("qrcodeValues", qrcodeValues);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl. URL_LABEL_INFO_LIST), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<List<LabelInfoBean>>() {
            @Override
            public void onSuccess(List<LabelInfoBean> result) {
                mView.getLabelInfoListSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    /**
     * 根据贮存设施获取废物类别列表
     */
    public void getTypeList(String mainid) {
        Map<String, String> params = new HashMap<>();
        params.put("mainid", mainid);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_WASTE_TYPE_LIST_BY_ID,params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<List<GfYbqyTzNewCsRkXnbq>>() {
            @Override
            public void onSuccess(List<GfYbqyTzNewCsRkXnbq> result) {
                mView.getTypeListSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }



    /**
     * 请求自行利用处置设施列表
     */
    public void getZxlyssList() {
        String dwid = SPUtils.get(mContext, Constants.DWID, "").toString();
        Map<String, String> params = new HashMap<>();
        params.put("dwid", dwid);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_ZXLYSS_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<List<GfYbqyXxbaZxlyczss>>() {
            @Override
            public void onSuccess(List<GfYbqyXxbaZxlyczss> result) {
                mView.getZxlyssListSuccess(result);
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
        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParam(addDwid(params)), url).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mView.saveDataSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }

    /**
     * 上报国家
     */
    public void sbgjRequest(String id, final String url) {
        Map<String, String> params = new HashMap<>();
        params.put("id",id);
        Observable<Object> observable = apiServer.CommonGet(token,  url,params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mView.sbgjRequestSuccess(result);
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
        String url =  ApiUrl.getHnUrl(ApiUrl.URL_XNBQ_CSSB_DELETE) + id;
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        Observable<Object> observable = apiServer.commonRequest(token,  SubscriberCallBack.requestBodyParam(params), url).map(new HttpResultFunc<Object>());
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


    /**
     * 请求产生设施列表
     */
    public void getProductDeviceList(String fwxxId) {
        Map<String, String> params = new HashMap<>();
        params.put("fwxxId", fwxxId);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl. URL_PRODUCT_DEVICE_LIST), addDwid(params)).map(new HttpResultFunc<Object>());
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
     * 检测是否上报成功，是否可打印
     */
    public void checkPrintable(String typch) {
        Map<String, String> params = new HashMap<>();
        params.put("typch", typch);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_CHECK_PRINTABLE, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mView.checkPrintableSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }





    public interface View extends BaseView {

        void getTypeListSuccess(List<GfYbqyTzNewCsRkXnbq> bean);

        void getZcssListSuccess(List<ZcssBean> bean);

        void getParamListSuccess(List<ParamListBean> bean);

        void saveDataSuccess(String bean);

        void sbgjRequestSuccess(String bean);


        void getRkInfoSuccess(Object bean);

        void getLabelInfoSuccess(Object bean);

        void getLabelInfoListSuccess(List<LabelInfoBean> list);

        void deleteDataSuccess(String bean);

        void getFromListSuccess(List<UserBean> list);

        void getZyldListSuccess(List<ZyldBean> list);

        void getHtListSuccess(List<HtxxBean> list);

        void getUserListSuccess(List<UserBean> list);

        void getZxlyssListSuccess(List<GfYbqyXxbaZxlyczss> bean);

//        void getChildIdFromListSuccess(GfYbqyTzNewCsRkXnbq.GfYbqyTzRkFwxx childBean);

        void getProductDeviceListSuccess(List<ProductDeviceBean> bean);

        void checkPrintableSuccess(String result);

    }


}
