package com.grkj.ytjqy_pad.base;

import android.content.Context;
import android.text.TextUtils;

import com.grkj.ytjqy_pad.gather.ApiServer;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.http.HttpMethods;
import com.grkj.ytjqy_pad.utils.common.SPUtils;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yusam on 2018/7/19.
 * 基于Rx的Presenter封装,控制订阅的生命周期
 */
public abstract class RxPresenter<T extends BaseView> implements BasePresenter<T> {
    protected ApiServer apiServer = HttpMethods.getInstance().create(ApiServer.class);

    protected T mView;
    protected Context mContext;
    protected String token;

    protected CompositeSubscription mCompositeSubscription;

    /**
     *  取消订阅
     * */
    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = null;
        }
    }

    protected void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    /**
     *  RxJava绑定
     */
    public void addSubscription(Observable observable, Subscriber<?> subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));

    }

    @Override
    public void attachView(T view) {
        this.mView = view;
        if (mView instanceof SimpleActivity) {
            mContext = (SimpleActivity) mView;
            token = SPUtils.get(mContext,"token","").toString();
        } else if (mView instanceof SimpleFragment) {
            mContext = ((SimpleFragment) mView).getActivity();
            token = SPUtils.get(mContext,"token","").toString();
        }else {
            mContext = getContext();
            token = getToken();
        }
    }

    public String getToken(){
        return token;
    }

    public Context getContext(){
        return mContext;
    }

    public void setToken(String token){
        this.token = token;
    }

    public void setContext(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }


    protected Map<String,String> addDwid(Map<String, String> params){
        String select_dwid = SPUtils.get(mContext, Constants.SELECT_DWID, "").toString();
        if (!TextUtils.isEmpty(select_dwid)){
            params.put(Constants.SELECT_DWID,select_dwid);
        }
        return params;
    }


}
