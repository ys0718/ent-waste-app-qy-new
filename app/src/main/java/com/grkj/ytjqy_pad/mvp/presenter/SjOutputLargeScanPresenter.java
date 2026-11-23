package com.grkj.ytjqy_pad.mvp.presenter;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.utils.common.DES;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by yusam on 2022/3/20.
 * 收集扫码出库
 */

public class SjOutputLargeScanPresenter extends RxPresenter<SjOutputLargeScanPresenter.View> {

    /**
     * 扫码获取废物信息
     */
    public void getScanInfo(String rkpch) {
        Map<String, String> params = new HashMap<>();
        try {
            String nameDes = DES.encryptDES(rkpch, App.DESC_KEY);
            params.put("rkQrcodeValue", nameDes);
            params.put("isEncrypt", "1");
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(mContext,"扫描失败，请重试！");
            return;
        }
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl. URL_SJ_INPUT_INFO), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.getScanInfoSuccess(GsonUtil.GsonToString(result));
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    /**
     * 获取待入库列表
     */
    public void getTodoList(Map<String, String> params) {
        params.put(Constants.ROWS_KEY, "999");
        params.put("page", "1");
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl. URL_WASTE_TYPE_LIST_BY_ID_CKXZ), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
//                List<CommonProductInfoBean> list = GsonUtil.JsonToList(GsonUtil.GsonToString(result), CommonProductInfoBean.class);
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);

                String json = GsonUtil.GsonToString(model.getRows());
                List<CommonProductInfoBean> list = GsonUtil.JsonToList(json, CommonProductInfoBean.class);
                mView.getTodoListSuccess(list);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }



    //保存弹窗
    public void showSaveDialog(List params) {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确定保存？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                saveData(params);
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
    public void saveData(List params) {
        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParamObj(params), ApiUrl.getHnUrl(ApiUrl. URL_SJ_OUTPUT_SAVE_MUL)).map(new HttpResultFunc<Object>());
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
        void getScanInfoSuccess(String bean);

        void getTodoListSuccess(List<CommonProductInfoBean> bean);

        void saveDataSuccess(Object bean);
    }

}
