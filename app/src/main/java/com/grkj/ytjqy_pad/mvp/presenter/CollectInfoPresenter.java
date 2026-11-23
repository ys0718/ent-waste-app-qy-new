package com.grkj.ytjqy_pad.mvp.presenter;

import android.content.Context;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.GfYbqyXxbaXkzxxZcssBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.view.activity.hn.AreaBean;
import com.grkj.ytjqy_pad.mvp.view.activity.hn.HnUtils;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by yusam on 2022/3/20.
 * 社会源收集信息
 */

public class CollectInfoPresenter extends RxPresenter<CollectInfoPresenter.View> {


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



    //将json文件读取为String类型的数据
    private String readJsonFile(File file) {
        String json = "";
        try {
            InputStream inputStream = new FileInputStream(file);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    //将接口获取到的json数据存到本地文件
    private void WriteJsonFile(List<AreaBean> result) {
        try {
            File file = new File(mContext.getExternalCacheDir(),"city.json");
            FileWriter fileWriter = new FileWriter(file,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(GsonUtil.GsonToString(result));
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 请求行政区域树
     */
    public void getXzqyTree() {
        String jsonName = mContext.getExternalCacheDir() + "/city.json";
        File file = new File(jsonName);
        if (file.exists()){
            List<AreaBean> resultList = GsonUtil.JsonToList(readJsonFile(file), AreaBean.class);
            mView.getXzqyTreeSuccess(resultList);
        }else {
            Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_XZQY_TREE, new HashMap<>()).map(new HttpResultFunc<Object>());
            addSubscription(observable, new SubscriberCallBack(new ApiCallback<List<AreaBean>>() {
                @Override
                public void onSuccess(List<AreaBean> result) {
                    mView.getXzqyTreeSuccess(result);
                    WriteJsonFile(result);
                }

                @Override
                public void onFailure(String msg) {
                    mView.showError(msg);
                }
            }, mContext));
        }

    }



    /**
     * 请求单位列表
     */
    public void getDwList() {
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put(Constants.ROWS_KEY, "999");
        Observable<Object> observable = apiServer.CommonGet(token, ApiUrl.URL_DW_LIST, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                mView.getDwListSuccess(model.getRows());
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
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
     * 删除数据
     */
    public void deleteData(String url ) {
        Observable<Object> observable = apiServer.commonRequest(token, SubscriberCallBack.requestBodyParam(new HashMap<>()), url).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                mView.deleteDataSuccess(result);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }



    /**
     * 请求贮存设施列表
     * @param xkzId
     */
    public void getZcssListByXkz(String xkzId) {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.MAINID, xkzId);
        params.put("page", "1");
        params.put("rows", "999");
        String url = ApiUrl.URL_ZCSS_LIST_BY_XKZID;
        Observable<Object> observable = apiServer.CommonGet(token, url, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                if (result!=null){
                    PageBean pageBean = GsonUtil.GsonToBean(result, PageBean.class);
                    List<GfYbqyXxbaXkzxxZcssBean> list = GsonUtil.JsonToList(GsonUtil.GsonToString(pageBean.getRows()), GfYbqyXxbaXkzxxZcssBean.class);
                    mView.getZcssListByXkzSuccess(list);
                }

            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext));
    }


    /**
     * 请求列表
     */
    public void getChildList(final Map<String, String> params) {
        params.put("page", "1");
        params.put("rows", "99");
        String url = ApiUrl.URL_COLLECT_CHILD_LIST;

        Observable<Object> observable = apiServer.CommonGet(token, url, addDwid(params)).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                mView.getChildListSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }

    /**
     * 获取标签信息
     */
    public void getLabelInfo(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        Observable<Object> observable = apiServer.CommonGet(token,  ApiUrl.URL_LABEL_INFO_BY_CHILD_ID, params).map(new HttpResultFunc<Object>());
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
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                mView.getXkzListSuccess(model.getRows());
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }



    public interface View extends BaseView {

        void getParamListSuccess(List<ParamListBean> bean);

        void getXzqyTreeSuccess(List<AreaBean> list);

        void saveDataSuccess(Object bean);

        void deleteDataSuccess(Object bean);

        void getDwListSuccess(Object rows);

        void getZcssListByXkzSuccess(List<GfYbqyXxbaXkzxxZcssBean> list);

        void getChildListSuccess(PageBean pageBean);

        void getLabelInfoSuccess(Object bean);

        void getXkzListSuccess(Object bean);
    }


}
