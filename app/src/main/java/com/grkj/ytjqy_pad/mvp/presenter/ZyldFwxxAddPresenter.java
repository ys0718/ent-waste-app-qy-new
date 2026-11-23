package com.grkj.ytjqy_pad.mvp.presenter;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.model.zyld.GfWxfwZyldFwxx;
import com.grkj.ytjqy_pad.mvp.model.zyld.GfWxfwZyldFwxxPch;
import com.grkj.ytjqy_pad.mvp.model.zyld.JsdwxxBean;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by yusam on 2026/9/15.
 * 添加转移联单废物信息
 */

public class ZyldFwxxAddPresenter extends RxPresenter<ZyldFwxxAddPresenter.View> {


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
    public void getZcssList() {
        Map<String, String> params = new HashMap<>();
        String dwid = SPUtils.get(mContext, Constants.DWID, "").toString();
        params.put("dwid", dwid);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_ZCSS_LIST, params).map(new HttpResultFunc<Object>());
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
     * 根据贮存设施获取废物信息列表
     */
    public void getFwxxList(String mainid) {
        Map<String, String> params = new HashMap<>();
        params.put("mainid", mainid);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_WASTE_TYPE_LIST_BY_ID, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<List<JsdwxxBean.Wtlysz>>() {
            @Override
            public void onSuccess(List<JsdwxxBean.Wtlysz> result) {
                mView.getFwxxListSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    //保存弹窗
    public void showSaveDialog(final Map<String, Object> params) {
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
    public void saveData(Map<String, Object> params) {
        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParamObj(params), ApiUrl.URL_ZYLD_FWXX_SAVE).map(new HttpResultFunc<Object>());
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
     * 删除废物信息数据
     */
    private void deleteData(String id) {
        String url =  ApiUrl.getHnUrl(ApiUrl. URL_ZYLD_FWXX_DELETE) + id;
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParam(params), url ).map(new HttpResultFunc<Object>());
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
     * 删除批次号信息数据
     */
    public void deletePphData(String id) {
        String url =  ApiUrl.getHnUrl(ApiUrl. URL_ZYLD_FWXX_PCH_DELETE) + id;
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParam(params), url ).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mView.deletePchDataSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }

    public void getCphList(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("childid", id);
        params.put(Constants.PAGE_KEY, "1");
        params.put(Constants.ROWS_KEY, "999");
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_ZYPCH_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {

                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                String json = GsonUtil.GsonToString(model.getRows());
                List<GfWxfwZyldFwxxPch> list = GsonUtil.JsonToList(json,GfWxfwZyldFwxxPch.class);
                mView.getCphListSuccess(list);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    public interface View extends BaseView {

        void getParamListSuccess(List<ParamListBean> bean);

        void getZcssListSuccess(List<ZcssBean> bean);

        void getCphListSuccess(List<GfWxfwZyldFwxxPch> list);

        void getFwxxListSuccess(List<JsdwxxBean.Wtlysz> bean);

        void saveDataSuccess(Object bean);

        void deleteDataSuccess(String msg);

        void deletePchDataSuccess(String msg);

    }

}
