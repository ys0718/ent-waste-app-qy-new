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
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ZxlyListBean;
import com.grkj.ytjqy_pad.mvp.model.ZxlyssBean;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by yusam on 2022/3/20.
 * 出库信息
 */

public class ProductZxlyInfoPresenter extends RxPresenter<ProductZxlyInfoPresenter.View>{


    /**
     * 请求参数列表
     */
    public void getParamList(final Map<String, String> params){
        Observable<Object> observable = apiServer.CommonGet(token,ApiUrl.URL_PARAM_LIST,params).map(new HttpResultFunc<Object>());
        addSubscription(observable,new SubscriberCallBack(new ApiCallback<List<ParamListBean>>(){
            @Override
            public void onSuccess(List<ParamListBean> result){
                mView.getParamListSuccess(result);
            }

            @Override
            public void onFailure(String msg){
                mView.showError(msg);
            }
        },mContext));
    }


    /**
     * 请求贮存设施列表
     */
    public void getZxlyssList(){
        String dwid = SPUtils.get(mContext,Constants.DWID,"").toString();
        Map<String, String> params = new HashMap<>();
        params.put("dwid",dwid);
        Observable<Object> observable = apiServer.CommonGet(token,ApiUrl.URL_ZXLYSS_LIST,params).map(new HttpResultFunc<Object>());
        addSubscription(observable,new SubscriberCallBack(new ApiCallback<List<ZxlyssBean>>(){
            @Override
            public void onSuccess(List<ZxlyssBean> result){
                mView.getZxlyssListSuccess(result);
            }

            @Override
            public void onFailure(String msg){
                mView.showError(msg);
            }
        },mContext));
    }




    /**
     * 请求产生/出库废物信息列表
     */
    public void getFwxxList(String mainid){
        Map<String, String> params = new HashMap<>();
        params.put("mainid",mainid);
        params.put("page","1");
        params.put("rows","9999");
        Observable<Object> observable = apiServer.CommonGet(token,ApiUrl.URL_PRODUCT_ZXLY_FWXX_LIST,params).map(new HttpResultFunc<Object>());
        addSubscription(observable,new SubscriberCallBack(new ApiCallback<Object>(){
            @Override
            public void onSuccess(Object result){
                PageBean model = GsonUtil.objectToBean(result,PageBean.class);
                if (model.getRows() != null && model.getRows().size() != 0) {
                    String json = GsonUtil.GsonToString(model.getRows());
                    List<ZxlyListBean.ZxlyFwListBean> list = GsonUtil.JsonToList(json,ZxlyListBean.ZxlyFwListBean.class);
                    mView.getFwxxListSuccess(list);
                }
            }

            @Override
            public void onFailure(String msg){
                mView.showError(msg);
            }
        },mContext));
    }

    /**
     * 请求废物类别列表
     */
    public void getTypeList(){
        String dwid = SPUtils.get(mContext,Constants.DWID,"").toString();
        Map<String, String> params = new HashMap<>();
        params.put("dwid",dwid);
        Observable<Object> observable = apiServer.CommonGet(token,ApiUrl.getHnUrl(ApiUrl. URL_WASTE_TYPE_LIST),params).map(new HttpResultFunc<Object>());
        addSubscription(observable,new SubscriberCallBack(new ApiCallback<List<CommonProductInfoBean>>(){
            @Override
            public void onSuccess(List<CommonProductInfoBean> result){
                mView.getTypeListSuccess(result);
            }

            @Override
            public void onFailure(String msg){
                mView.showError(msg);
            }
        },mContext));
    }


    /**
     * 保存数据
     */
    public void saveData(final Map<String, String> params,final String url){
        Observable<Object> observable = apiServer.commonRequest(token,SubscriberCallBack.requestBodyParam(params),url).map(new HttpResultFunc<Object>());
        addSubscription(observable,new SubscriberCallBack(new ApiCallback<Object>(){
            @Override
            public void onSuccess(Object result){
                mView.saveDataSuccess(result);
            }

            @Override
            public void onFailure(String msg){
                mView.showError(msg);
            }
        },mContext));
    }


    /*
     * 确认删除
     * */
    public void showDeleteDialog(String id){
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确认删除？");
        //设置弹窗类型
        roundCornerDialog.setDialogType(RoundCornerDialog.TYPE_DELETE);
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener(){
            @Override
            public void onConfirmClick(){
                roundCornerDialog.dismiss();
                deleteData(id);
            }

            @Override
            public void onCancelClick(){

            }
        });
        //显示对话框
        roundCornerDialog.show();
    }


    /**
     * 删除数据
     */
    private void deleteData(String id){
        String url = ApiUrl.URL_PRODUCT_ZXLY_DELETE+id;
        HashMap<String, String> params = new HashMap<>();
        Observable<Object> observable = apiServer.CommonGet(token,url,params).map(new HttpResultFunc<Object>());
        addSubscription(observable,new SubscriberCallBack(new ApiCallback<String>(){
            @Override
            public void onSuccess(String result){
                mView.deleteDataSuccess(result);
            }

            @Override
            public void onFailure(String msg){
                mView.showError(msg);
            }
        },mContext));
    }


    public interface View extends BaseView{

        void getTypeListSuccess(List<CommonProductInfoBean> bean);

        void getFwxxListSuccess(List<ZxlyListBean.ZxlyFwListBean> bean);

        void getZxlyssListSuccess(List<ZxlyssBean> bean);

        void getParamListSuccess(List<ParamListBean> bean);

        void saveDataSuccess(Object bean);

        void deleteDataSuccess(String bean);

//        void getPcbmSuccess(String bean);

    }


}
