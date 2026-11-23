package com.grkj.ytjqy_pad.mvp.presenter;

import android.app.Activity;
import android.os.Handler;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.view.RefreshUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * * Created by yusam on 2023/2/15.
 * 打印列表
 */

public class PrintListPresenter extends RxPresenter<PrintListPresenter.View> {

    private int pageNumber = 1;
    private int totalPage;


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
        }, mContext,false));
    }

    /**
     * 请求企业类型列表
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
        }, mContext,false));
    }


    /**
     * 请求列表
     */
    public void getList(final Map<String, String> params, final String url) {
        params.put("page", pageNumber + "");
        Observable<Object> observable = apiServer.CommonGet(token, url, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                totalPage = model.getTotal();
                int page = model.getPage();
                if (page > 1) {
                    mView.getMoreListSuccess(model, url);
                } else {
                    mView.getListSuccess(model, url);
                }
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        },mContext,false));
    }



    /**
     * 刷新与加载设置
     */
    public int initRefresh(final SmartRefreshLayout refreshLayout, final Activity activity, final String url, final Map<String, String> params) {
        params.put(Constants.ROWS_KEY, Constants.ROWS_VALUE);
        RefreshUtils.setRefreshConfig(activity, refreshLayout)
                .setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshlayout) {
                        pageNumber = 1;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getList(params, url);
                            }
                        }, 200);
                        refreshLayout.setLoadmoreFinished(false);
                    }
                }).setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (totalPage > pageNumber) {
                    pageNumber++;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getList(params, url);
                        }
                    }, 200);
                } else {
                    refreshLayout.finishLoadmore();
                    refreshLayout.setLoadmoreFinished(true);
                }
            }
        });

        return pageNumber;
    }

    /**
     * 获取标签信息
     */
    public void getLabelInfo( String id, String url) {
        Map<String, String> params = new HashMap<>();
//        params.put("rkpch", rkpch);
        params.put("id", id);
        Observable<Object> observable = apiServer.CommonGet(token, url, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.getLabelInfoSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
//                mView.showError(msg);
                ToastUtil.showToast(mContext,msg);
            }
        }, mContext));
    }



    public interface View extends BaseView {

        void getTypeListSuccess(List<CommonProductInfoBean> bean);

        void getParamListSuccess(List<ParamListBean> bean);

        void getListSuccess(PageBean pageBean, String url);

        void getMoreListSuccess(PageBean pageBean, String url);

        void getLabelInfoSuccess(Object result);

    }


}
