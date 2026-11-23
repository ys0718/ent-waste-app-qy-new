package com.grkj.ytjqy_pad.mvp.view.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.presenter.SelectWasteListPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
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
 * Created by yusam on 2022/3/20.
 * 产生信息选择列表
 */

public class SelectWasteListActivity extends BaseActivity<SelectWasteListPresenter> implements SelectWasteListPresenter.View {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    //废物总量
    @BindView(R.id.tv_left_bottom)
    TextView tv_left_bottom;

    //库存总量描述
    @BindView(R.id.tv_left_bottom2)
    TextView tv_left_bottom2;

    //当前页
    @BindView(R.id.tv_right_bottom1)
    TextView tv_right_bottom1;

    //总页数总条数
    @BindView(R.id.tv_right_bottom2)
    TextView tv_right_bottom2;

    //标题
    @BindView(R.id.tv_title)
    TextView tv_title;

    //贮存设备名称-文字
    @BindView(R.id.et_item1)
    TextView et_item1;

    //贮存设备名称-布局
    @BindView(R.id.ll_search)
    LinearLayout ll_search;

    //废物名称-文字
    @BindView(R.id.et_item2)
    TextView et_item2;

    //废物详细名称
    @BindView(R.id.et_search)
    EditText et_search;

    //废物名称-布局
    @BindView(R.id.ll_search2)
    LinearLayout ll_search2;

    //主布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //主布局
    @BindView(R.id.ll_input)
    LinearLayout ll_input;


    private int totalPage;
    private int pageNumber = 1;
    private List<CommonProductInfoBean> mList;//产废列表
    private CommonAdapter<CommonProductInfoBean> adapter;//产废列表适配器
    private Map<String, String> params = new HashMap<>();
    private CommonProductInfoBean selectBean = new CommonProductInfoBean();

    private CommonProductInfoBean wasteSelectBean = null;

    @Override
    protected int getLayout() {
        return R.layout.activity_select_waste_list;
    }

    @Override
    protected void initEventAndData() {

        //选中废物信息
        String wasteSelectBeanStr = getIntent().getStringExtra("wasteSelectBean");
        if (!TextUtils.isEmpty(wasteSelectBeanStr)) {
            wasteSelectBean = GsonUtil.GsonToBean(wasteSelectBeanStr, CommonProductInfoBean.class);
        } else {
            wasteSelectBean = new CommonProductInfoBean();
        }
        ll_input.setVisibility(View.VISIBLE);

        initRecycleView();
        refreshLayout.autoRefresh();
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
        initTopToolbar("选择产废信息");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }


    @OnClick({R.id.ll_save,R.id.ll_back,R.id.ll_search_click})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            // 确定
            case R.id.ll_save:
                if (selectBean.SELECT) {
                    Intent result = new Intent();
                    result.putExtra("selectBean", GsonUtil.GsonToString(selectBean));
                    setResult(10, result);
                    finish();
                } else {
                    ToastUtil.showToast(mContext, "请选择产废信息！");
                }

                break;

            // 搜索
            case R.id.ll_search_click:
                params.put("fwxxmc", et_search.getText().toString().trim());
                refreshLayout.autoRefresh();
                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;

        }
    }


    @Override
    protected SelectWasteListPresenter createPresenter() {
        return new SelectWasteListPresenter();
    }


    @Override
    public void showError(String msg) {
        toastShow(msg);
        hideLoading();
        mList.clear();
        adapter.notifyDataSetChanged();
        refreshLayout.finishRefresh();
    }


    /**
     * 企业列表初始化
     */
    private void initRecycleView() {
        mList = new ArrayList<>();
        if (!TextUtils.isEmpty(wasteSelectBean.fwdlbh)) {
            params.put("fwdlbh", wasteSelectBean.fwdlbh);
        }
        if (!TextUtils.isEmpty(wasteSelectBean.fwxlbh)) {
            params.put("fwxlbh", wasteSelectBean.fwxlbh);
        }
        if (!TextUtils.isEmpty(wasteSelectBean.fwxxmc)) {
            params.put("fwxxmc", wasteSelectBean.fwxxmc);
        }
//        if (!TextUtils.isEmpty(wasteSelectBean.jldw)) {
//            params.put("jldw", wasteSelectBean.jldw);
//        }
        if (!TextUtils.isEmpty(wasteSelectBean.fwtx)) {
            params.put("fwtx", wasteSelectBean.fwtx);
        }
        if (!TextUtils.isEmpty(wasteSelectBean.rqbzlx)) {
            params.put("rqbzlx", wasteSelectBean.rqbzlx);
        }
        if (!TextUtils.isEmpty(wasteSelectBean.wlxz)) {
            params.put("wlxz", wasteSelectBean.wlxz);
        }
//        if (!TextUtils.isEmpty(wasteSelectBean.dwid)) {
//            params.put("dwid", wasteSelectBean.dwid);
//        }
//        if (!TextUtils.isEmpty(wasteSelectBean.bqlx)) {
//            params.put("bqlx", wasteSelectBean.bqlx);
//        }

        params.put("zcssId", getIntent().getStringExtra("zcssId"));
        pageNumber = mPresenter.initRefresh(refreshLayout, mContext, ApiUrl.getHnUrl(ApiUrl.URL_SELECT_WASTE_LIST), params);
        adapter = new CommonAdapter<CommonProductInfoBean>(mContext, R.layout.item_common_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final CommonProductInfoBean bean, final int position) {
                if (bean != null) {
                    ImageView cb = holder.getView(R.id.btn_select);
                    LinearLayout item_layout = holder.getView(R.id.item_layout);
                    cb.setVisibility(View.VISIBLE);
                    cb.setImageResource(bean.SELECT ? R.drawable.checkbox_theme : R.mipmap.ic_unselected);
                    item_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < mList.size(); i++) {
                                mList.get(i).SELECT = false;
                            }
                            bean.SELECT = true;
                            selectBean = bean;
                            adapter.notifyDataSetChanged();
                        }
                    });

                    setText(holder.getView(R.id.tv_item1), bean.fwxxmc, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2), bean.fwdlbh, R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3), bean.fwxlbh, R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item4), bean.cssj, R.string.str_record_list3);
                    setText(holder.getView(R.id.tv_item6), bean.csl, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item7), bean.jldw, R.string.str_jldw_input);

                }
            }
        };


        mRecyclerView.setAdapter(adapter);
    }

    private void setText(View tv, String value, int strRes) {
        tv.setVisibility(View.VISIBLE);
        ((TextView) tv).setText(TextUtils.isEmpty(value) ? getString(strRes, getString(R.string.common_empty_text)) : getString(strRes, value));
    }


    /**
     * 获取列表数据成功
     */
    @Override
    public void getListSuccess(final PageBean model, String url) {
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
    private void fillRecycleView(PageBean<CommonProductInfoBean> model) {
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        List<CommonProductInfoBean> LabelInfoBeans = GsonUtil.JsonToList(json, CommonProductInfoBean.class);
        mList.addAll(LabelInfoBeans);
        adapter.notifyDataSetChanged();
    }

    /**
     * 加载更多列表成功
     */
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }
    }


}
