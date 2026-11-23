package com.grkj.ytjqy_pad.mvp.presenter;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.CollectInfoChildBean;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.GfYbqyXxbaXkzxxZcssBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.view.activity.hn.HnUtils;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by yusam on 2022/3/20.
 * 社会源收集信息-子表
 */

public class CollectInfoChildPresenter extends RxPresenter<CollectInfoChildPresenter.View> {


    /**
     * 请求参数列表
     */
    public void getParamList(final Map<String, String> params) {
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_PARAM_LIST,params).map(new HttpResultFunc<Object>());
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
        String url = ApiUrl.URL_COLLECT_DELETE_CHILD + id;
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
     * 获取标签信息
     */
    public void getLabelInfo(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        Observable<Object> observable = apiServer.CommonGet(token,  ApiUrl.URL_LABEL_INFO_BY_CHILD_ID,params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.getLabelInfoSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


//    /**
//     * 请求废物类别列表
//     */
//    public void getFwlbList(final Map<String, String> params) {
//        params.put("page", "1");
//        params.put("rows", "99");
//        String url = ApiUrl.URL_WASTE_TYPE_LIST_BY_ID;
//        Observable<Object> observable = apiServer.CommonGet(token, url, params).map(new HttpResultFunc<Object>());
//        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
//            @Override
//            public void onSuccess(Object result) {
//                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
//                mView.getFwlbListSuccess(model);
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                mView.showError(msg);
//            }
//        }, mContext, false));
//    }


    /**
     * 根据贮存设施获取废物类别列表
     */
    public void getFwlbList(String mainid) {
        Map<String, String> params = new HashMap<>();
        params.put("mainid", mainid);
        params.put("page", "1");
        params.put("rows", "999");
        Observable<Object> observable = apiServer.CommonGet(token,  ApiUrl.URL_WASTE_TYPE_LIST_BY_XKZID,params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                if (result != null) {
                    PageBean pageBean = GsonUtil.GsonToBean(result, PageBean.class);
                    List<CollectInfoChildBean> list = GsonUtil.JsonToList(GsonUtil.GsonToString(pageBean.getRows()), CollectInfoChildBean.class);
                    mView.getFwlbListSuccess(list);
                }
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

        void deleteDataSuccess(Object bean);

        void getFwlbListSuccess(List<CollectInfoChildBean> list);

        void getLabelInfoSuccess(Object bean);
    }


}
