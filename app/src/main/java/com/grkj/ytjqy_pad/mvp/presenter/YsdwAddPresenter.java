package com.grkj.ytjqy_pad.mvp.presenter;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.zyld.GfWxfwZyldYsxx;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by yusam on 2026/9/14.
 * 添加运输单位信息
 */

public class YsdwAddPresenter extends RxPresenter<YsdwAddPresenter.View> {


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
        }, mContext));
    }


    /**
     * 根据运输单位名称查询运输单位信息
     */
    public void getYsqyInfo(String ysdwmc) {
        Map<String, String> params = new HashMap<>();
        params.put("ysdwmc", ysdwmc);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_ZYLD_YSQY_INFO, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                GfWxfwZyldYsxx model = GsonUtil.objectToBean(result, GfWxfwZyldYsxx.class);
                mView.getYsqyInfoSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    //保存弹窗
    public void showSaveDialog(final GfWxfwZyldYsxx data) {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确定保存？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                saveData(data);
                roundCornerDialog.dismiss();
            }

            @Override
            public void onCancelClick() {

            }
        });
        //显示对话框
        roundCornerDialog.show();
    }


    /**
     * 保存数据
     */
    public void saveData(GfWxfwZyldYsxx data) {
        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParamObj(data), ApiUrl.URL_ZYLD_YSXX_SAVE).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.saveDataSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));

    }


    public interface View extends BaseView {

        void getParamListSuccess(List<ParamListBean> bean);

        void getYsqyInfoSuccess(GfWxfwZyldYsxx bean);

        void saveDataSuccess(Object bean);

    }

}
