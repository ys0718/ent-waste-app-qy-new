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
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.view.RefreshUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * @author JueHongP
 */
public class WasteInputListPresenter extends RxPresenter<WasteInputListPresenter.View> {

    private String mArea;

    private int pageNumber = 1;
    private int totalPage;

    /**
     * 请求列表
     */
    public void getList(final Map<String, String> params, final String url) {
        params.put("page", pageNumber + "");
        params.remove("tzType");
        Observable<Object> observable = apiServer.CommonGet(token, url, addDwid(params)).map(new HttpResultFunc<>());
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
    public int initRefresh(final SmartRefreshLayout refreshLayout, final Activity activity, final String url, final Map<String, String> params, final String area) {
        this.mArea = area;
        params.put(Constants.ROWS_KEY, Constants.ROWS_VALUE);
        RefreshUtils.setRefreshConfig(activity, refreshLayout)
                .setOnRefreshListener(mRefreshLayout -> {
                    pageNumber = 1;
                    new Handler().postDelayed(() -> getList(params, url), 200);
                    refreshLayout.setLoadmoreFinished(false);
                }).setOnLoadmoreListener(mRefreshLayout -> {
                    if (totalPage > pageNumber) {
                        pageNumber++;
                        new Handler().postDelayed(() -> getList(params, url), 200);
                    } else {
                        refreshLayout.finishLoadmore();
                        refreshLayout.setLoadmoreFinished(true);
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
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(getTotalWeightUrl()), addDwid(params)).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                if (!TextUtils.isEmpty(result.toString())) {
                    mView.getTotalWeightSuccess(result.toString());
                } else {
                    mView.getTotalWeightSuccess("");
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

    /**根据地区更换获取总量请求地址*/
    private String getTotalWeightUrl() {
        String url = "";
        switch (mArea){
            case Constants.EV_GUANGDONG:
                url = ApiUrl.URL_WASTE_INPUT_TOTAL;
                break;
            case Constants.EV_HUNAN:
                url = ApiUrl.URL_HN_WASTE_INPUT_TOTAL;
                break;
            default:
                break;
        }

        return url;
    }


    public interface View extends BaseView {
        void getListSuccess(PageBean pageBean, String url);

        void getMoreListSuccess(PageBean pageBean, String url);

        void getTotalWeightSuccess(String total);

        void getEnterpriseInfoSuccess(EnterpriseInfoBean bean);
    }
}
