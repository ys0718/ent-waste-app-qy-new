package com.grkj.ytjqy_pad.mvp.presenter;

import android.content.Context;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.HistoryInfoBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * * Created by yusam on 2023/2/15.
 * 缓存历史
 */

public class SearchEdittextPresenter extends RxPresenter<SearchEdittextPresenter.View> {


    /**
     * 请求列表
     */
    public void getList(String token, Context mContext, String url, HashMap<String, String> params) {
        setContext(mContext);
        setToken(token);
        Observable<Object> observable = apiServer.CommonGet(token, url, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.getListSuccess(result, url);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }


    public interface View extends BaseView {

        void getListSuccess(Object bean, String url);


    }


}
