package com.grkj.ytjqy_pad.mvp.presenter;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.DataUploadBean;
import com.grkj.ytjqy_pad.mvp.model.DataUploadDetailBean;
import com.grkj.ytjqy_pad.mvp.model.StatisticsInfoListBean;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.view.RefreshUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;
import java.util.Map;

import rx.Observable;

public class DataUploadInfoPresenter extends RxPresenter<DataUploadInfoPresenter.View> {

    private int pageNumber = 1;
    private int totalPage;

    /**
     * 请求列表
     */
    public void getList(final Map<String, String> params, final String url) {
        Observable<Object> observable = apiServer.CommonGet(token, url, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {

                String dataStr = GsonUtil.GsonToString(result);
                List<DataUploadDetailBean> dataUploadList = GsonUtil.JsonToList(dataStr, DataUploadDetailBean.class);
                mView.getListSuccess(dataUploadList, url);
                getTotalWeight(params);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }

    /**
     * 刷新与加载设置
     */
    public int initRefresh(final SmartRefreshLayout refreshLayout, final Activity activity, final String url, final Map<String, String> params) {
        params.put(Constants.ROWS_KEY, "999");
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
                }).setEnableLoadmore(false);
//                .setOnLoadmoreListener(new OnLoadmoreListener () {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//                if (totalPage > pageNumber) {
//                    pageNumber++;
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            getList(params, url);
//                        }
//                    }, 200);
//                } else {
//                    refreshLayout.finishLoadmore();
//                    refreshLayout.setLoadmoreFinished(true);
//                }
//            }
//        });

        return pageNumber;
    }


    /**
     * 获取总量
     */
    public void getTotalWeight(Map<String, String> params) {
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl.URL_PRODUCT_STATISTICS_TOTAL), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                if (!TextUtils.isEmpty(result.toString())) {
                    StatisticsInfoListBean model = GsonUtil.objectToBean(result, StatisticsInfoListBean.class);
                    mView.getTotalWeightSuccess(model);
                } else {
                    mView.getTotalWeightSuccess(new StatisticsInfoListBean());
                }
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }


    public interface View extends BaseView {
        void getListSuccess(List<DataUploadDetailBean> dataList, String url);

        void getTotalWeightSuccess(StatisticsInfoListBean total);
    }


}
