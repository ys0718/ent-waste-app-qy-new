package com.grkj.ytjqy_pad.mvp.presenter;

import android.text.TextUtils;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.CollectInfoChildBean;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.GfYbqyXxbaXkzxxBean;
import com.grkj.ytjqy_pad.mvp.model.GfYbqyXxbaXkzxxZcssBean;
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

/**
 * @author JueHongP
 */
public class HnWasteInputInfoPresenter extends RxPresenter<HnWasteInputInfoPresenter.View> {

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
     * 请求许可证列表
     */
    public void getXkzList() {
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put(Constants.ROWS_KEY, "999");
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_XKZ_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object object) {
                PageBean bean = GsonUtil.GsonToBean(object, PageBean.class);
                String json = GsonUtil.GsonToString(bean.getRows());
                List<GfYbqyXxbaXkzxxBean> result = GsonUtil.JsonToList(json, GfYbqyXxbaXkzxxBean.class);
                mView.getLicenceListSuccess(result);

            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }


    /**
     * 请求贮存设施列表
     */
    public void getZcssList(Map<String, String> params) {
        params.put(Constants.PAGE_KEY, "1");
        params.put(Constants.ROWS_KEY, "999");
        String url = ApiUrl.URL_ZCSS_LIST_BY_XKZID;
        Observable<Object> observable = apiServer.CommonGet(token, url, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object object) {
                PageBean bean = GsonUtil.GsonToBean(object, PageBean.class);
                String json = GsonUtil.GsonToString(bean.getRows());
                List<GfYbqyXxbaXkzxxZcssBean> result = GsonUtil.JsonToList(json, GfYbqyXxbaXkzxxZcssBean.class);
                mView.getZcssListSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }

    /**
     * 根据贮存设施获取废物类别列表
     */
    public void getFwlbList(String mainid) {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.MAINID, mainid);
        params.put(Constants.PAGE_KEY, "1");
        params.put(Constants.ROWS_KEY, "999");
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_WASTE_TYPE_LIST_BY_XKZID, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                if (result != null) {
                    PageBean pageBean = GsonUtil.GsonToBean(result, PageBean.class);
                    List<CommonProductInfoBean> list = GsonUtil.JsonToList(GsonUtil.GsonToString(pageBean.getRows()), CommonProductInfoBean.class);
                    mView.getFwlbListSuccess(list);
                }
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }

    /**
     * 请求出库详细信息
     */
    public void getInputInfoById(String mainId) {
        Map<String, String> params = new HashMap<>();
        params.put("id", mainId);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_HN_WASTE_INPUT_INFO_BY_ID, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                CommonProductInfoBean bean = GsonUtil.objectToBean(result, CommonProductInfoBean.class);
                mView.getInputInfoByIdSuccess(bean);
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
        params.put("type", "rk");
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
     * 根据二维码获取小微企业出库废物信息
     */
    public void getInputInfo(Map<String, String> params ) {
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl.URL_HN_SCAN_XWQY_WASTE_INPUT), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                CommonProductInfoBean.GFHnJyqyTzRkFwxxes bean = GsonUtil.objectToBean(result, CommonProductInfoBean.GFHnJyqyTzRkFwxxes.class);
                mView.getInputInfoSuccess(bean);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }

    /**保存出库信息*/
    public void saveCkFwxx(final List params, final String url){
        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParamObj(params), url).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.saveCkFwxxSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    /** @param type  0-入库数据 1-小微企业废物信息
     * 确认删除
     * */
    public void showDeleteDialog(String id,int type) {
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
                if(type == 0){
                    deleteData(id);
                }else if(type == 1){
                    deleteFwxx(id);
                }

            }

            @Override
            public void onCancelClick() {

            }
        });
        //显示对话框
        roundCornerDialog.show();
    }

    /**
     * 删除废物数据
     */
    private void deleteFwxx(String id) {
        String url = ApiUrl.getHnUrl(ApiUrl.URL_DELETE_HN_XWQY_WASTE_OUTPUT);
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        Observable<Object> observable = apiServer.CommonGet(token, url, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mView.deleteFwxxSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    /**
     * 删除数据
     */
    private void deleteData(String id) {
        String url = ApiUrl.getHnUrl(ApiUrl.URL_DELETE_HN_WASTE_INPUT);
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        Observable<Object> observable = apiServer.CommonGet(token, url, params).map(new HttpResultFunc<Object>());
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

    public interface View extends BaseView {

        void getZcssListSuccess(List<GfYbqyXxbaXkzxxZcssBean> bean);

        void getParamListSuccess(List<ParamListBean> bean);

        void saveDataSuccess(Object bean);

        void getLabelInfoSuccess(LabelInfoBean labelInfoBean);

        void deleteDataSuccess(String bean);

        void getInputInfoSuccess(CommonProductInfoBean.GFHnJyqyTzRkFwxxes bean);

        void getInputInfoByIdSuccess(CommonProductInfoBean bean);

        void getLicenceListSuccess(List<GfYbqyXxbaXkzxxBean> result);

        void getFwlbListSuccess(List<CommonProductInfoBean> bean);

        void deleteFwxxSuccess(String result);

        void saveCkFwxxSuccess(Object result);
    }

}
