package com.grkj.ytjqy_pad.mvp.view.activity;//package com.grkj.ytjqy_pad.mvp.view.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.mvp.model.DataUploadBean;
import com.grkj.ytjqy_pad.mvp.model.TypeListBean;
import com.grkj.ytjqy_pad.mvp.presenter.DataUploadPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * 数据上报列表
 */

public class DataUploadListActivity extends BaseActivity<DataUploadPresenter> implements DataUploadPresenter.View {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    //当前页
    @BindView(R.id.tv_right_bottom1)
    TextView tv_right_bottom1;

    //总页数总条数
    @BindView(R.id.tv_right_bottom2)
    TextView tv_right_bottom2;

    //主布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //状态选择布局
    @BindView(R.id.ll_search)
    LinearLayout ll_search;

    //状态选择文字
    @BindView(R.id.et_item1)
    TextView et_item1;

    //类型选择布局
    @BindView(R.id.ll_search2)
    LinearLayout ll_search2;

    //类型选择文字
    @BindView(R.id.et_item2)
    TextView et_item2;


    private int totalPage;
    private int pageNumber = 1;
    private List<DataUploadBean> mList = new ArrayList<>();//数据列表
    private CommonAdapter<DataUploadBean> adapter;//数据列表适配器
    private Map<String, String> params = new HashMap<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_upload_data_list;
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getStatusList();
        mPresenter.getTypeList();
    }


    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));

    }


    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }


    @OnClick({R.id.ll_search, R.id.ll_search2, R.id.ll_add, R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //状态
            case R.id.ll_search:
                showStatusPopWindow();
                break;
            //类型
            case R.id.ll_search2:
                showTypePopWindow();
                break;
            //主动上报
            case R.id.ll_add:
                mPresenter.showUploadDialog();
                break;
            // 返回
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    protected DataUploadPresenter createPresenter() {
        return new DataUploadPresenter();
    }

    @Override
    public void showError(String msg) {
        toastShow(msg);
        hideLoading();
        mList.clear();
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }

        refreshLayout.finishRefresh();
    }

    /**
     * 企业列表初始化
     */
    private void initRecycleView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mList = new ArrayList<>();
        pageNumber = mPresenter.initRefresh(refreshLayout, mContext, ApiUrl.URL_DATA_UPLOAD_LIST, params);
        adapter = new CommonAdapter<DataUploadBean>(mContext, R.layout.item_common_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final DataUploadBean bean, final int position) {
                if (bean != null) {

                    setText(holder.getView(R.id.tv_item1), bean.reportTime, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2), getTypeName(bean.reportType), R.string.str_data_upload_item1);
                    setText(holder.getView(R.id.tv_item3), getStatusName(bean.reportStatus), R.string.str_data_upload_item2);
                    setText(holder.getView(R.id.tv_item4), bean.dataTime, R.string.str_data_upload_item3);
                    setText(holder.getView(R.id.tv_item6), bean.dataCount + "", R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item7), "数据量(条)", R.string.empty_replace);

                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                startActivityForResult(new Intent(mContext, DataUploadInfoActivity.class)
                        .putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(mList.get(i)))
                        .putExtra("STATUS_LIST",GsonUtil.GsonToString(statusList))
                        , 9);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                return false;
            }
        });

        mRecyclerView.setAdapter(adapter);
    }



    /**
     * 获取类型名称
     * */
    private String getTypeName(int type){
        String result = "";
        for (int i = 0; i < typeList.size(); i++) {
            if (type == typeList.get(i).type){
                result = typeList.get(i).msg;
            }
        }
        return result;
    }



    /**
     * 获取状态名称
     * */
    private String getStatusName(int status){
        String result = "";
        for (int i = 0; i < statusList.size(); i++) {
            if (status == statusList.get(i).type){
                result = statusList.get(i).msg;
            }
        }
        return result;

    }


    private void setText(View tv, String value, int strRes) {
        tv.setVisibility(View.VISIBLE);
        ((TextView) tv).setText(Html.fromHtml(TextUtils.isEmpty(value) ? getString(strRes, getString(R.string.common_empty_text)) : getString(strRes, value)));
    }

    @Override
    public void getListSuccess(PageBean model, String url) {
        totalPage = model.getTotal();
        refreshLayout.finishRefresh();
        tv_right_bottom2.setText(getString(R.string.str_page_total, model.getTotal(), model.getRecords()));


        if (model != null) {
            fillRecycleView(model);
        }
    }

    /**
     * 向列表视图填充数据
     */
    private void fillRecycleView(PageBean<DataUploadBean> model) {
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        List<DataUploadBean> LabelInfoBeans = GsonUtil.JsonToList(json, DataUploadBean.class);
        mList.addAll(LabelInfoBeans);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getMoreListSuccess(PageBean model, String url) {
        refreshLayout.finishLoadmore();
        if (totalPage == pageNumber) {
            refreshLayout.finishLoadmore();
        }
        if (model != null) {
            fillRecycleView(model);
        }
    }

    @Override
    public void commitDataSuccess(String msg) {
        toastShow(msg);
    }

    @Override
    public void getStatusListSuccess(List<TypeListBean> result) {
        String json = GsonUtil.GsonToString(result);
        statusList = GsonUtil.JsonToList(json, TypeListBean.class);

        initRecycleView();
        refreshLayout.autoRefresh();
    }

    @Override
    public void getTypeListSuccess(List<TypeListBean> result) {
        String json = GsonUtil.GsonToString(result);
        typeList = GsonUtil.JsonToList(json, TypeListBean.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }
    }


    private SelectPopupWindow selectPopupWindow;
    private List<TypeListBean> statusList = new ArrayList<>();
    private List<TypeListBean> typeList = new ArrayList<>();

    //上报状态下拉弹窗
    private void showStatusPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();
        selectPopupWindowList.add(new SelectPopupWindowBean("", "全部"));
        for (int i = 0; i < statusList.size(); i++) {
            TypeListBean mData = statusList.get(i);
            selectPopupWindowList.add(new SelectPopupWindowBean(mData.type + "", mData.msg));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                params.put("reportStatus", model.ID);
                et_item1.setText(model.NAME);
                refreshLayout.autoRefresh();
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", ll_search, sort);

    }

    //上报类型下拉弹窗
    private void showTypePopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();
        selectPopupWindowList.add(new SelectPopupWindowBean("", "全部"));
        for (int i = 0; i < typeList.size(); i++) {
            TypeListBean mData = typeList.get(i);
            selectPopupWindowList.add(new SelectPopupWindowBean(mData.type + "", mData.msg));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                params.put("reportType", model.ID);
                et_item2.setText(model.NAME);
                refreshLayout.autoRefresh();
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", ll_search2, sort);

    }

}


