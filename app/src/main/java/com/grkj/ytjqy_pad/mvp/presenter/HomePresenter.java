package com.grkj.ytjqy_pad.mvp.presenter;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.NoticeBean;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by yusam on 2022/3/20.
 * 菜单页
 */

public class HomePresenter extends RxPresenter<HomePresenter.View> {


    /**
     * 总数统计(产生单位)
     */
    public void getTotalInfo() {
        String url =  ApiUrl.URL_GET_TOTAL;
        Observable<Object> observable = apiServer.CommonGet(token, url, new HashMap<>()).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.getTotalInfoSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }


    /**
     * 总数统计(收集单位)
     */
    public void getTotalInfoSjdw() {
        String url =  ApiUrl.URL_GET_TOTAL_SJDW;
        Observable<Object> observable = apiServer.CommonGet(token, url, new HashMap<>()).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.getTotalInfoSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }

    /**
     * 请求单位列表
     */
    public void getDwList(final Map<String, String> params) {
        params.put("page", "1");
        params.put(Constants.ROWS_KEY, "999");
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_DW_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                mView.getDwListSuccess(model.getRows());
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }

    /**
     * 请求最新公告
     */
    public void getLastedNotice() {
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_LASTED_NOTICE, new HashMap<>()).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                KLog.e(result);
                NoticeBean noticeBean = GsonUtil.GsonToBean(result, NoticeBean.class);
                mView.getLastedNoticeSuccess(noticeBean);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }




    public interface View extends BaseView {
        void getTotalInfoSuccess(Object bean);

        void getDwListSuccess(Object rows);

        void getLastedNoticeSuccess(NoticeBean noticeBean);
    }


}
