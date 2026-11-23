package com.grkj.ytjqy_pad.mvp.presenter;

import com.grkj.ytjqy_pad.BuildConfig;
import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.socks.library.KLog;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by yusam on 2022-04-02
 * 关于
 */

public class SettingAboutPresenter extends RxPresenter<SettingAboutPresenter.View> {


    /**
     * 检查更新
     *
     * @param isLandscape
     */
    public void checkVersion(boolean isLandscape) {

//        String isDebug = Constants.IS_DEBUG ? "&isDebug=1" : "";
        String isDebug = BuildConfig.LOG_DEBUG? "&isDebug=1" : "";
        String dwid = SPUtils.get(mContext, Constants.DWID, "").toString();
        String deviceCode = SPUtils.get(mContext,Constants.DEVICE_CODE,"").toString();
        String url = ApiUrl.URL_GET_NEW_VERSION + "?dwid=" + dwid + "&deviceCode=" + deviceCode;
        String type = isLandscape ? "" : "&type=3";//一体机传空，移动端传3
//        KLog.e("dwid:" + dwid);
//        KLog.e("deviceCode:" + SerialNumber);
        Observable<Object> observable = apiServer.CommonGet(token, url + type + isDebug, new HashMap<>()).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object model) {
                mView.checkVersionSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    public interface View extends BaseView {

        void checkVersionSuccess(Object model);

    }


}


