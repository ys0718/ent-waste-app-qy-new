package com.grkj.ytjqy_pad.mvp.presenter;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.CsdwBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.YjsjBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author JueHongP
 */
public class SjInputInfoPresenter extends RxPresenter<SjInputInfoPresenter.View> {

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
    public void getTypeList(String mainid) {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PAGE_KEY,"1");
        params.put(Constants.ROWS_KEY,"999");
        params.put("mainid",mainid);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl. URL_SJ_INPUT_FWLB_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean pageBean = GsonUtil.GsonToBean(result,PageBean.class);
                List<CommonProductInfoBean> list = GsonUtil.JsonToList(GsonUtil.GsonToString(pageBean.getRows()), CommonProductInfoBean.class);
                mView.getTypeListSuccess(list);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }

    /**
     * 请求产生单位列表
     */
    public void getCsdwList() {
        Map<String, String> params = new HashMap<>();
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl. URL_SJ_INPUT_CSDW_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                List<CsdwBean> list = GsonUtil.JsonToList(GsonUtil.GsonToString(result), CsdwBean.class);
                mView.getCsdwListSuccess(list);
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
        params.put(Constants.PAGE_KEY,"1");
        params.put(Constants.ROWS_KEY,"9999");
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_SJ_INPUT_ZCSS_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean pageBean = GsonUtil.GsonToBean(result,PageBean.class);
                List<ZcssBean> list = GsonUtil.JsonToList(GsonUtil.GsonToString(pageBean.getRows()), ZcssBean.class);
                mView.getZcssListSuccess(list);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }

    /**
     * 请求贮存区域列表
     */
    public void getZcqyList(Map<String, String> params) {
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_SJ_INPUT_ZCQY_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean pageBean = GsonUtil.GsonToBean(result,PageBean.class);
                List<ZcssBean> list = GsonUtil.JsonToList(GsonUtil.GsonToString(pageBean.getRows()), ZcssBean.class);
                mView.getZcQyListSuccess(list);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    /**
     * 请求应急事件列表
     */
    public void getYjsjList() {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PAGE_KEY,"1");
        params.put(Constants.ROWS_KEY,"9999");
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_SJ_INPUT_YJSJ_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean pageBean = GsonUtil.GsonToBean(result,PageBean.class);
                List<YjsjBean> list = GsonUtil.JsonToList(GsonUtil.GsonToString(pageBean.getRows()), YjsjBean.class);
                mView.getYjsjListSuccess(list);
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
        //rk-入库，ck-出库
        params.put("type", "rk");
        params.put("id", id);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl.URL_SJ_INPUT_LABEL), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                LabelInfoBean labelInfoBean = GsonUtil.objectToBean(result, LabelInfoBean.class);
                mView.getLabelInfoSuccess(labelInfoBean);
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
     * 根据二维码获取入库信息
     */
    public void getInputInfo(String qrcodeValue) {
        Map<String, String> params = new HashMap<>();
        params.put("ewmwb", qrcodeValue);

        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl.URL_GET_INPUT_INFO_BY_QRCODE), addDwid(params)).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                CommonProductInfoBean bean = GsonUtil.objectToBean(result, CommonProductInfoBean.class);
                mView.getInputInfoSuccess(bean);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
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
        String url = ApiUrl.getHnUrl(ApiUrl.URL_SJ_INPUT_DELETE) + id;
        HashMap<String, String> params = new HashMap<>();
        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParam(params), url).map(new HttpResultFunc<Object>());
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

    public interface View extends BaseView {

        void getYjsjListSuccess(List<YjsjBean> bean);

        void getZcssListSuccess(List<ZcssBean> bean);

        void getZcQyListSuccess(List<ZcssBean> bean);

        void getParamListSuccess(List<ParamListBean> bean);

        void saveDataSuccess(Object bean);

        void getLabelInfoSuccess(LabelInfoBean labelInfoBean);

        void deleteDataSuccess(String bean);

        void getInputInfoSuccess(CommonProductInfoBean bean);

        void getTypeListSuccess(List<CommonProductInfoBean> list);

        void getCsdwListSuccess(List<CsdwBean> list);
    }


}
