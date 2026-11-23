package com.grkj.ytjqy_pad.mvp.presenter;


import android.text.TextUtils;

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
import com.grkj.ytjqy_pad.mvp.model.HtxxBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.RfidCardInfoBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.model.ZyldBean;
import com.grkj.ytjqy_pad.utils.common.DES;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by yusam on 2022/3/20.
 * 出库信息
 */

public class ProductOutputInfoPresenter extends RxPresenter<ProductOutputInfoPresenter.View> {


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
    public void getZcssList(boolean isGetWasteList) {
        String dwid = SPUtils.get(mContext, Constants.DWID, "").toString();
        Map<String, String> params = new HashMap<>();
        params.put("dwid", dwid);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_ZCSS_LIST, addDwid(params)).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<List<ZcssBean>>() {
            @Override
            public void onSuccess(List<ZcssBean> result) {
                mView.getZcssListSuccess(result, isGetWasteList);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    /**
     * 请求处置单位列表
     */
    public void getReceiverList(String entername) {
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("rows", "999");
        params.put("jsqymc", entername);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_ENTER_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                List<UserBean> list = model.getRows();
                mView.getReceiverListSuccess(list);
            }

            @Override
            public void onFailure(String msg) {
//                mView.showError(msg);
                mView.getReceiverListSuccess(new ArrayList<>());
            }
        }, mContext, false));
    }


    /**
     * 请求出库子列表
     */
    public void getChildIdFromList(String mainid) {
        Map<String, String> params = new HashMap<>();
        params.put("mainid", mainid);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl.URL_OUTPUT_CHILD_LIST), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                List<CommonProductInfoBean.GfYbqyTzCkZyFwxx> list = model.getRows();

                String json = GsonUtil.GsonToString(list);
                List<CommonProductInfoBean.GfYbqyTzCkZyFwxx> childList = GsonUtil.JsonToList(json, CommonProductInfoBean.GfYbqyTzCkZyFwxx.class);

                if (childList != null && childList.size() != 0) {
                    CommonProductInfoBean.GfYbqyTzCkZyFwxx bean = childList.get(0);
                    mView.getChildIdFromListSuccess(bean);
                }
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }


//    /**
//     * 获取标签信息
//     */
//    public void getLabelInfo(String qrcodeValue) {
//        Map<String, String> params = new HashMap<>();
//        params.put("type", "ck");//cf-产废，rk-入库，ck-出库
//        params.put("qrCodeValues", qrcodeValue);
//        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_LABEL_INFO_LIST, params).map(new HttpResultFunc<Object>());
//        addSubscription(observable, new SubscriberCallBack(new ApiCallback<List<LabelInfoBean>>() {
//            @Override
//            public void onSuccess(List<LabelInfoBean> result) {
//                if (result!=null && result.size()!=0){
//                    mView.getLabelInfoSuccess(result.get(0));
//                }
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                mView.showError(msg);
//            }
//        }, mContext));
//    }


    /**
     *  获取Rfid卡司机信息
     */
    public void  getRfCardInfo(String id) {
        Map<String, String> params = new HashMap<>();
        String url = ApiUrl.getHnUrl(ApiUrl.URL_RFID_CARD_INFO) + id;
        Observable<Object> observable = apiServer.CommonGet(token, url, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.getRfCardInfoSuccess(GsonUtil.GsonToBean(result,RfidCardInfoBean.class));
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));

    }

    /**
     * 请求按钮列表
     */
    public void getButtonList() {
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_BUTTONS_LIST, new HashMap<>()).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                boolean isGetRfid = false;
                List<String> parenList = GsonUtil.getListByKey(result,"ydd_ck");
                if (parenList.size()!=0){
                    for (int i = 0; i < parenList.size(); i++) {
                        if (parenList.get(i).contains("ydd_rfid")){
                            isGetRfid = true;
                        }
                    }
                }
                mView.isGetRfidSuccess(isGetRfid);
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
        String url = ApiUrl.getHnUrl(ApiUrl.URL_CK_INFO) + id;
        Observable<Object> observable = apiServer.CommonGet(token, url, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                CommonProductInfoBean newData = GsonUtil.GsonToBean(result, CommonProductInfoBean.class);
                getLabelInfoByNewQrCode(newData.qrcodeValue);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));

    }


    private void getLabelInfoByNewQrCode(String qrcodeValue){
        Map<String, String> params = new HashMap<>();
//        params.put("rkpch", rkpch);
        params.put("qrcodeValue", qrcodeValue);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl.URL_LABEL_INFO_CK), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.getLabelInfoSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }




//    /**
//     * 获取批次编码
//     */
//    public void getPcbm() {
//        String dwid = SPUtils.get(mContext, Constants.DWID, "").toString();
//        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_GET_PCBM, new HashMap<>()).map(new HttpResultFunc<Object>());
//        addSubscription(observable, new SubscriberCallBack(new ApiCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                mView.getPcbmSuccess(result);
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                mView.showError(msg);
//            }
//        }, mContext));
//    }

    /**
     * 请求废物类别列表
     */
    public void getTypeList(String zcssbm, String id) {
        Map<String, String> params = new HashMap<>();
        params.put("zcssbm", zcssbm);//贮存设施编码
        params.put("page", "1");//库ID
        params.put("rows", "999");//库ID
        String url = ApiUrl.getHnUrl(ApiUrl.URL_WASTE_TYPE_LIST_BY_ID_CKBJ);//编辑
        if (TextUtils.isEmpty(id)) {
            url = ApiUrl.getHnUrl(ApiUrl.URL_WASTE_TYPE_LIST_BY_ID_CKXZ);
        } else {
            params.put("id", id);//库ID
        }
        Observable<Object> observable = apiServer.CommonGet(token, url, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {

                if (result != null) {
                    String json = GsonUtil.GsonToString(result);
                    PageBean pageBean = GsonUtil.GsonToBean(json, PageBean.class);
                    mView.getTypeListSuccess(pageBean.getRows());
                }

            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


//    /**
//     * 保存数据
//     */
//    public void saveData(List params, final String url) {
//        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParamObj(params), ApiUrl.URL_PRODUCT_OUTPUT_SAVE_LIST).map(new HttpResultFunc<Object>());
//        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
//            @Override
//            public void onSuccess(Object result) {
//                mView.saveDataSuccess(result);
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                mView.showError(msg);
//            }
//        }, mContext));
//    }


    /**
     * 保存数据
     */
    public void saveData(final Map<String, String> params, final String url) {
        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParam(addDwid(params)), ApiUrl.getHnUrl(url)).map(new HttpResultFunc<Object>());
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


    /*
     * 确认删除
     * */
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
        String url = ApiUrl.getHnUrl(ApiUrl.URL_PRODUCT_OUTPUT_DELETE);
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

    /**
     * 请求转移联单列表
     */
    public void getZyldList(String number1) {
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("rows", "99");
        params.put("number1", number1);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_ZYLD_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                String json = GsonUtil.GsonToString(model.getRows());
                List<ZyldBean> list = GsonUtil.JsonToList(json, ZyldBean.class);
                mView.getZyldListSuccess(list);
            }

            @Override
            public void onFailure(String msg) {
//                mView.showError(msg);
                mView.getZyldListSuccess(new ArrayList<>());
            }
        }, mContext, false));
    }

    /**
     * 请求转移联单列表
     */
    public void getZyldList(CommonProductInfoBean mData) {
        String dwid = SPUtils.get(mContext, Constants.SELECT_DWID, "").toString();
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("rows", "999");
        params.put("dwid", dwid);
        params.put("actids", "1,2,3,4,5,5_5");//字典参数
        params.put("fwdlbh", mData.fwdlbh);
        params.put("fwxlbh", mData.fwxlbh);
        params.put("fwxxmc", mData.fwxxmc);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_ZYLD_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {

                KLog.e();

                if (result != null) {
                    PageBean model = GsonUtil.objectToBean(result, PageBean.class);

                    if (model.getRows() != null) {
                        String json = GsonUtil.GsonToString(model.getRows());
                        List<ZyldBean> list = GsonUtil.JsonToList(json, ZyldBean.class);
                        mView.getZyldListSuccess(list);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }


    /**
     * 同步联单
     */
    public void syncZyldList(CommonProductInfoBean mData, String startTime, String endTime) {
        String dwid = SPUtils.get(mContext, Constants.SELECT_DWID, "").toString();
        Map<String, String> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("dwid", dwid);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_SYNC_ZYLD_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ToastUtil.showToast(mContext, "同步成功！");
                getZyldList(mData);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }



    /**
     * 请求合同列表
     */
    public void getHtList(String htbh) {
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("rows", "99");
        params.put("htbh", htbh);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_HTXX_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                String json = GsonUtil.GsonToString(model.getRows());
                List<HtxxBean> list = GsonUtil.JsonToList(json, HtxxBean.class);
                mView.getHtListSuccess(list);
            }

            @Override
            public void onFailure(String msg) {
//                mView.showError(msg);
                mView.getHtListSuccess(new ArrayList<>());
            }
        }, mContext, false));
    }

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
            ToastUtil.showToast(mContext, "扫描失败，请重试！");
            return;
        }
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl.URL_PRODUCT_INPUT_INFO), params).map(new HttpResultFunc<Object>());
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
     * 获取产生环节废物id
     */
    public void getCshjFwid(Map<String, String> params) {
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.getHnUrl(ApiUrl.URL_GET_CSHJ_FWID), params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.getCshjFwidSuccess(GsonUtil.GsonToString(result));
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    /**
     * 请求用户列表
     */
    public void getUserList(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_USER_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                mView.getUserListSuccess(model.getRows());
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }


    public interface View extends BaseView {

        void getTypeListSuccess(List<CommonProductInfoBean> bean);

        void getZcssListSuccess(List<ZcssBean> bean, boolean isGetWasteList);

        void getParamListSuccess(List<ParamListBean> bean);

        void saveDataSuccess(Object bean);

        void getUserListSuccess(List<UserBean> list);

        void getReceiverListSuccess(List<UserBean> list);

        void getChildIdFromListSuccess(CommonProductInfoBean.GfYbqyTzCkZyFwxx bean);

        void deleteDataSuccess(String bean);

        void getLabelInfoSuccess(Object bean);

        void getRfCardInfoSuccess(RfidCardInfoBean bean);

        void isGetRfidSuccess(boolean bean);

        void getZyldListSuccess(List<ZyldBean> list);

        void getHtListSuccess(List<HtxxBean> list);

        void getScanInfoSuccess(String bean);

        void getCshjFwidSuccess(String bean);

//        void getPcbmSuccess(String bean);

    }


}
