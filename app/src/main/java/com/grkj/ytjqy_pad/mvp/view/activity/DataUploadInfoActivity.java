package com.grkj.ytjqy_pad.mvp.view.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
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
import com.grkj.ytjqy_pad.mvp.model.DataUploadDetailBean;
import com.grkj.ytjqy_pad.mvp.model.DataUploadDetailBean;
import com.grkj.ytjqy_pad.mvp.model.StatisticsInfoListBean;
import com.grkj.ytjqy_pad.mvp.model.TypeListBean;
import com.grkj.ytjqy_pad.mvp.presenter.DataUploadInfoPresenter;
import com.grkj.ytjqy_pad.utils.common.ActivityUtil;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 数据上报信息
 */

public class DataUploadInfoActivity extends BaseActivity<DataUploadInfoPresenter> implements DataUploadInfoPresenter.View {

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
    //标题
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_title2)
    TextView tv_title2;

    @BindView(R.id.ll_add)
    LinearLayout ll_add;

    @BindView(R.id.ll_left_bottom)
    LinearLayout ll_left_bottom;


    private int pageNumber = 1;
    private List<DataUploadDetailBean> mList;//数据列表
    private CommonAdapter<DataUploadDetailBean> adapter;//数据列表适配器
    private Map<String, String> params = new HashMap<>();

    private List<TypeListBean> statusList = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_common_list;
    }

    @Override
    protected void initEventAndData() {
        initTop();
        tv_title.setText("数据上报详情");
        ll_add.setVisibility(View.GONE);
        ll_left_bottom.setVisibility(View.GONE);
        tv_title2.setVisibility(View.INVISIBLE);

        initRecycleView();
        refreshLayout.autoRefresh();
    }

    @OnClick({R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 返回
            case R.id.ll_back:
                finish();
                break;
        }
    }

    /**
     * 企业列表初始化
     */
    private void initRecycleView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mList = new ArrayList<>();

        String parentData = getIntent().getStringExtra(Constants.JUMP_KEY);
        DataUploadBean parentBean = GsonUtil.GsonToBean(parentData, DataUploadBean.class);

        String statusListStr = getIntent().getStringExtra("STATUS_LIST");
        statusList = GsonUtil.JsonToList(statusListStr, TypeListBean.class);

        if (TextUtils.isEmpty(parentBean.id)) {
            params.put("dataTime", parentBean.dataTime);
        } else {
            params.put("dataReportId", parentBean.id);
        }

        params.put("reportType", parentBean.reportType + "");
        pageNumber = mPresenter.initRefresh(refreshLayout, mContext, ApiUrl.URL_DATA_UPLOAD_DETAIL_LIST, params);
        adapter = new CommonAdapter<DataUploadDetailBean>(mContext, R.layout.item_common_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final DataUploadDetailBean bean, final int position) {
                if (bean != null) {
                    setText(holder.getView(R.id.tv_item1), bean.fwxxmc, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2), bean.fwdlbh, R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3), bean.fwxlbh, R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item4), getStatusName(bean.reportStatus), R.string.str_data_upload_item2);
                    setText(holder.getView(R.id.tv_item5), bean.dataTime, R.string.str_data_upload_item3);
                    setText(holder.getView(R.id.tv_item6), bean.fwzl, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item7), "登记量", R.string.empty_replace);
                }
            }
        };
        mRecyclerView.setAdapter(adapter);
    }


    /**
     * 获取状态名称
     */
    private String getStatusName(int status) {
        String result = "";
        for (int i = 0; i < statusList.size(); i++) {
            if (status == statusList.get(i).type) {
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
    protected DataUploadInfoPresenter createPresenter() {
        return new DataUploadInfoPresenter();
    }

    @Override
    public void showError(String msg) {
        toastShow(msg);
        hideLoading();
        mList.clear();
        adapter.notifyDataSetChanged();
        refreshLayout.finishRefresh();
    }

    @Override
    public void getListSuccess(List<DataUploadDetailBean> model, String url) {
        refreshLayout.finishRefresh();

        if (model != null) {
            fillRecycleView(model);
        }
    }

    private void fillRecycleView(List<DataUploadDetailBean> model) {
        mList.clear();
        String json = GsonUtil.GsonToString(model);
        List<DataUploadDetailBean> LabelInfoBeans = GsonUtil.JsonToList(json, DataUploadDetailBean.class);
        mList.addAll(LabelInfoBeans);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void getTotalWeightSuccess(StatisticsInfoListBean total) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }
    }
}