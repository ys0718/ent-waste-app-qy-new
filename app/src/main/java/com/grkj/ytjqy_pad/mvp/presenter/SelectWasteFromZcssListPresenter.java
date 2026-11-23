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
import com.grkj.ytjqy_pad.mvp.view.activity.hn.HnUtils;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
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
 * * Created by yusam on 2022/3/20.
 * 选择产生信息列表
 */

public class SelectWasteFromZcssListPresenter extends RxPresenter<SelectWasteFromZcssListPresenter.View> {

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
                if (result != null) {
                    if(url.equals(ApiUrl.getHnUrl(ApiUrl. URL_WASTE_TYPE_LIST_BY_ID_CKBJ)) || url.equals(ApiUrl.getHnUrl(ApiUrl. URL_WASTE_TYPE_LIST_BY_ID_CKXZ)) ||url.equals(ApiUrl.getHnUrl(ApiUrl. URL_JYTZ_FWLB_LIST))  ){
                        String json = GsonUtil.GsonToString(result);
                        PageBean pageBean = GsonUtil.GsonToBean(json, PageBean.class);
                        mView.getListSuccess(pageBean.getRows());
                    }else if(url.equals(ApiUrl.URL_WASTE_TYPE_LIST_BY_ID)){
                        String json = GsonUtil.GsonToString(result);
                        List<CommonProductInfoBean> list = GsonUtil.JsonToList(json, CommonProductInfoBean.class);
                        mView.getListSuccess(list);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext,false));
    }

    /**
     * 刷新与加载设置
     */
    public int initRefresh(final SmartRefreshLayout refreshLayout, final Activity activity, final String url, final Map<String, String> params) {
        params.put(Constants.ROWS_KEY, "99999");
        params.put(Constants.PAGE_KEY, "1");
        RefreshUtils.setRefreshConfig(activity, refreshLayout)
                .setEnableLoadmore(false)
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




    public interface View extends BaseView {
        void getListSuccess(List<CommonProductInfoBean> bean);

    }


}
