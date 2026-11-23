package com.grkj.ytjqy_pad.mvp.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.EnterpriseInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.zyld.GfWxfwZyld;
import com.grkj.ytjqy_pad.mvp.model.zyld.GfWxfwZyldFwxx;
import com.grkj.ytjqy_pad.mvp.model.zyld.GfWxfwZyldYsxx;
import com.grkj.ytjqy_pad.mvp.model.zyld.JsdwxxBean;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by yusam on 2026/9/8.
 * 转移联单信息
 */

public class ZyldInfoPresenter extends RxPresenter<ZyldInfoPresenter.View> {


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
     * 请求接收单位列表
     */
    public void getJsdwList(String jsdwmc) {
        Map<String, String> params = new HashMap<>();
        params.put("jydwmc",jsdwmc);
        String url = ApiUrl.URL_ZYLD_JSDW_INFO_LIST;
        Observable<Object> observable = apiServer.CommonGet(token, url, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (TextUtils.isEmpty(result) || TextUtils.equals(result, "[]")){
                    mView.getJsdwListSuccess(new JsdwxxBean());
                    return;
                }
                Gson gson = new Gson();
                List<JsdwxxBean> list = gson.fromJson(
                        result,
                        new TypeToken<List<JsdwxxBean>>() {}.getType()
                );
                mView.getJsdwListSuccess(list.get(0));
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }


    /**
     * 根据运输单位名称查询运输单位信息
     */
    public void getYsqyInfo(String ysdwmc) {
        Map<String, String> params = new HashMap<>();
        params.put("ysdwmc", ysdwmc);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_ZYLD_YSQY_INFO, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
//                GfWxfwZyldYsxx model = GsonUtil.objectToBean(result, GfWxfwZyldYsxx.class);
                String json = GsonUtil.GsonToString(result);
                List<GfWxfwZyldYsxx> LabelInfoBeans = GsonUtil.JsonToList(json,GfWxfwZyldYsxx.class);
                mView.getYsqyInfoSuccess(LabelInfoBeans);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    /**
     * 根据主表ID查询运输单位信息
     */
    public void getYsqyInfoById(String mainid) {
        Map<String, String> params = new HashMap<>();
        params.put("mainid", mainid);
        params.put(Constants.PAGE_KEY, "1");
        params.put(Constants.ROWS_KEY, "999");
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_ZYLD_YSQY_INFO_BY_MAINID, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                String json = GsonUtil.GsonToString(model.getRows());
                List<GfWxfwZyldYsxx> LabelInfoBeans = GsonUtil.JsonToList(json,GfWxfwZyldYsxx.class);
                mView.getYsqyInfoByIdSuccess(LabelInfoBeans);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    /**
     * 请求废物列表
     */
    public void getFwList(String mainid) {
        final Map<String, String> params = new HashMap<>();
        String url = ApiUrl.URL_ZYLD_FWXX_LIST_BY_MAINID;
        params.put(Constants.PAGE_KEY, "1");
        params.put(Constants.ROWS_KEY, "999");
        params.put("mainid", mainid);
        Observable<Object> observable = apiServer.CommonGet(token, url, addDwid(params)).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                String json = GsonUtil.GsonToString(model.getRows());
                List<GfWxfwZyldFwxx> list = GsonUtil.JsonToList(json,GfWxfwZyldFwxx.class);
                if (list.size()!=0){
                    mView.getFwListSuccess(list);
                }else {
                    mView.getFwListSuccess(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }




    //保存弹窗
    public void showSaveDialog(Map<String,Object> params) {
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
        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParamObj(params), ApiUrl.getHnUrl(ApiUrl.URL_ZYLD_MAIN_SAVE)).map(new HttpResultFunc<Object>());
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

    //上报国家弹窗
    public void showUploadDialog(String id,String url) {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确定上报国家？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                uploadData(id,url);
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
     * 上报国家数据
     */
    public void  uploadData(String id,String url) {
        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParamObj(new HashMap<>()), url + id).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.uploadDataSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));

    }


    /**
     * 获取当前用户企业信息
     */
    public void getEnterpriseInfo() {
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_NOW_ENTERPRISE_INFO, new HashMap<>()).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                EnterpriseInfoBean model = GsonUtil.objectToBean(result, EnterpriseInfoBean.class);
                mView.getEnterpriseInfoSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }


    /**
     * 删除运输单位信息
     */
    public void deleteYsdwxx(String id) {
        String url =  ApiUrl.getHnUrl(ApiUrl. URL_ZYLD_YSQY_INFO_DELETE) + id;
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        Observable<Object> observable = apiServer.commonRequest(token,  SubscriberCallBack.requestBodyParam(params), url).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mView.deleteYsdwxxSuccess(result);
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
        String url =  ApiUrl.getHnUrl(ApiUrl. URL_ZYLD_DELETE) + id;
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





    public interface View extends BaseView {

        void getParamListSuccess(List<ParamListBean> bean);
        void saveDataSuccess(Object bean);
        void uploadDataSuccess(Object bean);

        void getEnterpriseInfoSuccess(EnterpriseInfoBean bean);

        void getYsqyInfoSuccess(List<GfWxfwZyldYsxx> bean);

        void getYsqyInfoByIdSuccess(List<GfWxfwZyldYsxx> bean);

        void deleteYsdwxxSuccess(String msg);

        void getJsdwListSuccess(JsdwxxBean list);

        void getFwListSuccess(List<GfWxfwZyldFwxx> list);

        void deleteDataSuccess(String msg);

    }

}
