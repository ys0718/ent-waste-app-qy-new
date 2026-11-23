package com.grkj.ytjqy_pad.mvp.presenter;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.EnterpriseInfoBean;
import com.grkj.ytjqy_pad.mvp.model.StatisticsInfoListBean;
import com.grkj.ytjqy_pad.mvp.view.activity.hn.HnUtils;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.view.RefreshUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by yusam on 2022/3/20.
 * 入库列表
 */

public class ProductInputListPresenter extends RxPresenter<ProductInputListPresenter.View> {

    private int pageNumber = 1;
    private int totalPage;

    /**
     * 请求列表
     */
    public void getList(final Map<String, String> params, final String url) {
        params.put("page", pageNumber + "");
        params.remove("tzType");
        params.put("page", pageNumber + "");
        Observable<Object> observable = apiServer.CommonGet(token, url, addDwid(params)).map(new HttpResultFunc<Object>());
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
     * 获取总量
     */
    public void getTotalWeight(Map<String,String> parentParam) {
        Map<String,String> params = parentParam;
        params.put("tzType", "rk");
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl.URL_PRODUCT_INPUT_TOTAL), addDwid(params)).map(new HttpResultFunc<Object>());
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

    /**
     * 获取总量
     */
    public void getTotalWeight(Map<String,String> parentParam,String url) {
        Map<String,String> params = parentParam;
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(url), addDwid(params)).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (!TextUtils.isEmpty(result)) {
                    StatisticsInfoListBean model = new StatisticsInfoListBean();
                    model.rkl = result;
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


    public interface View extends BaseView {
        void getListSuccess(PageBean pageBean, String url);

        void getMoreListSuccess(PageBean pageBean, String url);

        void getTotalWeightSuccess(StatisticsInfoListBean total);

        void getEnterpriseInfoSuccess(EnterpriseInfoBean bean);
    }


}
