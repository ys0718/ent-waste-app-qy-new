package com.grkj.ytjqy_pad.mvp.presenter;

import android.content.Context;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
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

public class HistoryPresenter extends RxPresenter<HistoryPresenter.View>{


    /**
     * 请求历史列表
     */
    public void getHistoryList(String key,String token,Context mContext){
        Map<String, String> params = new HashMap<>();
        params.put("key",key);
        setContext(mContext);
        setToken(token);
        Observable<Object> observable = apiServer.CommonGet(token,ApiUrl.URL_HISTORY_LIST,params).map(new HttpResultFunc<Object>());
        addSubscription(observable,new SubscriberCallBack(new ApiCallback<List<HistoryInfoBean>>(){
            @Override
            public void onSuccess(List<HistoryInfoBean> result){
                mView.getHistoryListSuccess(result);
            }

            @Override
            public void onFailure(String msg){
//                mView.showError(msg);
            }
        },mContext,true));
    }


    /**
     * 保存历史
     */
    public void saveHistory(String key,String name,String token,Context mContext){
        Map<String, String> params = new HashMap<>();
        params.put("key",key);
        params.put("name",name);
        setContext(mContext);
        setToken(token);
        Observable<Object> observable = apiServer.commonRequest(token,SubscriberCallBack.requestBodyParam(params),
                ApiUrl.URL_HISTORY_SAVE).map(new HttpResultFunc<Object>());
        addSubscription(observable,new SubscriberCallBack(new ApiCallback<Object>(){
            @Override
            public void onSuccess(Object result){
                mView.saveHistorySuccess(result);
            }

            @Override
            public void onFailure(String msg){
                mView.showError(msg);
            }
        },mContext,false));
    }

    /**
     * 删除历史
     */
    public void deleteHistory(String key,String name,String token,Context mContext){
        Map<String, String> params = new HashMap<>();
        params.put("key",key);
        params.put("name",name);
        Observable<Object> observable = apiServer.CommonGet(token,ApiUrl.URL_HISTORY_DELETE,params
        ).map(new HttpResultFunc<Object>());
        addSubscription(observable,new SubscriberCallBack(new ApiCallback<Object>(){
            @Override
            public void onSuccess(Object result){
                mView.deleteHistorySuccess(name);
            }

            @Override
            public void onFailure(String msg){
                mView.showError(msg);
            }
        },mContext,false));
    }


    public interface View extends BaseView{

        void getHistoryListSuccess(List<HistoryInfoBean> bean);

        void saveHistorySuccess(Object result);

        void deleteHistorySuccess(String name);

    }


}
