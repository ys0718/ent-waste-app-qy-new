package com.grkj.ytjqy_pad.mvp.presenter;

import android.app.Activity;
import android.os.Handler;

import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.WasteTypeListBean;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.view.RefreshUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/** 湖南-经营入库-选择小微企业出库废物信息
 * @author JueHongP
 */
public class SelectXwqyWastePresenter extends RxPresenter<SelectXwqyWastePresenter.View> {

    private int pageNumber = 1;
    private int totalPage;

    /**
     * 请求数据列表
     */
    public void getList(final Map<String, String> params, final String url) {
        params.put("page", pageNumber + "");
        Observable<Object> observable = apiServer.CommonGet(token, url, params).map(new HttpResultFunc<Object>());
        addSubscription(observable, new SubscriberCallBack(new ApiCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                PageBean model = GsonUtil.objectToBean(result, PageBean.class);
                totalPage = model.getTotal();
                int page = model.getPage();
                if (page > 1) {
                    mView.getMoreListSuccess(model, url);
                } else {
                    mView.getListSuccess(model, url);
                }
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, mContext, false));
    }


    /**
     * 刷新与加载设置
     */
    public int  initRefresh(final SmartRefreshLayout refreshLayout, final Activity activity, final String url, final Map<String, String> params) {
        params.put(Constants.ROWS_KEY, "99999");
        RefreshUtils.setRefreshConfig(activity, refreshLayout)
                .setEnableLoadmore(false)
                .setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshlayout) {
                        pageNumber = 1;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getList(params, url);
                            }
                        }, 200);
                        refreshLayout.setLoadmoreFinished(false);
                    }
                }).setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (totalPage > pageNumber) {
                    pageNumber++;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getList(params, url);
                        }
                    }, 200);
                } else {
                    refreshLayout.finishLoadmore();
                    refreshLayout.setLoadmoreFinished(true);
                }
            }
        });

        return pageNumber;
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


    public interface View extends BaseView {
        void getListSuccess(PageBean pageBean, String url);

        void getMoreListSuccess(PageBean pageBean, String url);

        void saveCkFwxxSuccess(Object result);
    }
}
