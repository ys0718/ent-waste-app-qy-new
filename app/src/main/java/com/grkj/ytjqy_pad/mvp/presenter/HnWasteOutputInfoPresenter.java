package com.grkj.ytjqy_pad.mvp.presenter;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.GfYbqyXxbaXkzxxBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/** 湖南-危废出库登记信息
 * @author JueHongP
 */
public class HnWasteOutputInfoPresenter extends RxPresenter<HnWasteOutputInfoPresenter.View> {

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
     * 请求贮存设施列表
     */
    public void getZcssList(Map<String, String> params) {
        String dwid = SPUtils.get(mContext, Constants.DWID, "").toString();
        params.put("dwid", dwid);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_ZCSS_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<List<ZcssBean>>() {
            @Override
            public void onSuccess(List<ZcssBean> result) {
                mView.getZcssListSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    /**根据id获取出库信息*/
    public void getOutputInfoById(String id){
        String url = ApiUrl.URL_HN_WASTE_OUTPUT_INFO_BY_ID + id;
        Map<String,String> params = new HashMap<>();
        Observable<Object> observable = apiServer.CommonGet(token, url, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object object) {
                mView.getOutputInfoByIdSuccess(GsonUtil.objectToBean(object,CommonProductInfoBean.class));
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }

    /**
     * 保存数据
     */
    public void saveData(final Map<String, String> params, final String url) {
        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParam(params), url).map(new HttpResultFunc<Object>());
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


    /**
     * 确认删除
     */
    public void showDeleteDialog(String id) {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确认删除？");
        //设置弹窗类型
        roundCornerDialog.setDialogType(RoundCornerDialog.TYPE_DELETE);
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                roundCornerDialog.dismiss();
                deleteData(id);
            }

            @Override
            public void onCancelClick() {

            }
        });
        //显示对话框
        roundCornerDialog.show();
    }


    /**
     * 删除数据
     */
    private void deleteData(String id) {
        String url = ApiUrl.getHnUrl(ApiUrl.URL_DELETE_HN_WASTE_OUTPUT);
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        Observable<Object> observable = apiServer.CommonGet(token, url, addDwid(params)).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mView.deleteDataSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }

    /**
     * 获取标签信息
     */
    public void getLabelInfo(String id) {
        Map<String, String> params = new HashMap<>();
        //rk-入库，ck-出库
        params.put("type", "ck");
        params.put("id", id);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl.URL_GET_HN_LABEL_INFO), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                LabelInfoBean labelInfoBean = GsonUtil.objectToBean(result, LabelInfoBean.class);
                mView.getLabelInfoSuccess(labelInfoBean);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }

    /**
     * 根据二维码获取出库信息
     */
    public void getOutputInfo(String qrcodeValue) {
        Map<String, String> params = new HashMap<>();
        params.put("qrcodeValue", qrcodeValue);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl.URL_GET_HN_OUTPUT_INFO_BY_QRCODE), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                CommonProductInfoBean bean = GsonUtil.objectToBean(result, CommonProductInfoBean.class);
                mView.getOutputInfoSuccess(bean);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }


    public interface View extends BaseView {

        void getParamListSuccess(List<ParamListBean> bean);

        void getZcssListSuccess(List<ZcssBean> bean);

        void getOutputInfoByIdSuccess(CommonProductInfoBean bean);

        void saveDataSuccess(Object bean);

        void deleteDataSuccess(String bean);

        void getLabelInfoSuccess(LabelInfoBean labelInfoBean);

        void getOutputInfoSuccess(CommonProductInfoBean bean);
    }


}
