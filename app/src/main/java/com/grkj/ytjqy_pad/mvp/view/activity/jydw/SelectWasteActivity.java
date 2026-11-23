package com.grkj.ytjqy_pad.mvp.view.activity.jydw;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.Constants;
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
 * 选择废物信息
 *
 * @author JueHongP
 */
public class SelectWasteActivity extends BaseActivity<SelectWasteListPresenter> implements SelectWasteListPresenter.View {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    /**
     * 当前页
     */
    @BindView(R.id.tv_right_bottom1)
    TextView tv_right_bottom1;

    /**
     * 总页数总条数
     */
    @BindView(R.id.tv_right_bottom2)
    TextView tv_right_bottom2;

    /**
     * 标题
     */
    @BindView(R.id.tv_title)
    TextView tv_title;

    /**
     * 主布局
     */
    @BindView(R.id.mLayout)
    LinearLayout mLayout;


    private int totalPage;
    private int pageNumber = 1;
    /**
     * 废物信息列表
     */
    private List<CommonProductInfoBean> mList;
    /**
     * 废物信息列表适配器
     */
    private CommonAdapter<CommonProductInfoBean> adapter;
    private final Map<String, String> params = new HashMap<>();
    private CommonProductInfoBean selectBean = new CommonProductInfoBean();

    private String zcssId = "";
    private String url = "";

    @Override
    protected int getLayout() {
        return R.layout.activity_select_waste_list;
    }

    @Override
    protected void initEventAndData() {

        zcssId = getIntent().getStringExtra("zcssId");
        url = getIntent().getStringExtra("url");

        if (!TextUtils.isEmpty(zcssId)) {
            params.put("zcssId", zcssId);
        }

        findViewById(R.id.ll_home).setVisibility(View.GONE);

        initRecycleView();
        refreshLayout.autoRefresh();
    }


    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop("选择废物信息");
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));

    }


    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("选择废物信息");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }


    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.ll_save, R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            // 确定
            case R.id.ll_save:
                if (selectBean.SELECT) {
                    selectBean.rkId = selectBean.id;
                    selectBean.id = "";
                    Intent result = new Intent();
                    result.putExtra("selectBean", GsonUtil.GsonToString(selectBean));
                    setResult(10, result);
                    finish();
                } else {
                    ToastUtil.showToast(mContext, "请选择产废信息！");
                }

                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;

            default:
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
        params.put(Constants.ROWS_KEY, Constants.ROWS_VALUE);
        pageNumber = mPresenter.initRefresh(refreshLayout, mContext, url, params);
        adapter = new CommonAdapter<CommonProductInfoBean>(mContext, R.layout.item_common_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final CommonProductInfoBean bean, final int position) {
                if (bean != null) {
                    ImageView cb = holder.getView(R.id.btn_select);
                    LinearLayout itemLayout = holder.getView(R.id.item_layout);
                    cb.setVisibility(View.VISIBLE);
                    cb.setImageResource(bean.SELECT ? R.drawable.checkbox_theme : R.mipmap.ic_unselected);
                    itemLayout.setOnClickListener(v -> {
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).SELECT = false;
                        }
                        bean.SELECT = true;
                        selectBean = bean;
                        adapter.notifyDataSetChanged();
                    });

                    setText(holder.getView(R.id.tv_item1), bean.fwxxmc, R.string.empty_replace);

                    setText(holder.getView(R.id.tv_item2), bean.fwdlbh, R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3), bean.fwdlmc, R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item4), bean.fwxlbh, R.string.str_record_list3);
                    setText(holder.getView(R.id.tv_item5), bean.rksj, R.string.str_record_list3);

                    setText(holder.getView(R.id.tv_item6), bean.rkl, R.string.empty_replace);
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

}
