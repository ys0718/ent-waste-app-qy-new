package com.grkj.ytjqy_pad.mvp.view.activity.hn.jydw;

import android.annotation.SuppressLint;
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
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.WasteTypeListBean;
import com.grkj.ytjqy_pad.mvp.presenter.SelectXwqyWastePresenter;
import com.grkj.ytjqy_pad.mvp.presenter.SyncWasteInputPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.view.SelectTimeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectXwqyWasteAcitivity extends BaseActivity<SelectXwqyWastePresenter> implements SelectXwqyWastePresenter.View {

    /**
     * 父布局
     */
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    /*    *//**
     * 联单编号
     *//*
    @BindView(R.id.et_ldbh)
    EditText et_ldbh;

    *//**
     * 废物类别
     *//*
    @BindView(R.id.et_fwlb)
    EditText et_fwlb;

    *//**
     * 废物代码
     *//*
    @BindView(R.id.et_fwxl)
    EditText et_fwxl;

    *//**
     * 开始结束时间
     *//*
    @BindView(R.id.v_select_time)
    SelectTimeView v_select_time;

    */
    /**
     * 搜索
     *//*
    @BindView(R.id.ll_search)
    LinearLayout ll_search;*/

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    /**
     * 返回
     */
    @BindView(R.id.ll_back)
    LinearLayout ll_back;

    /**
     * 保存
     */
    @BindView(R.id.ll_save)
    LinearLayout ll_save;

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


    private int totalPage;
    private int pageNumber = 1;
    /**
     * 同步数据列表
     */
    private List<CommonProductInfoBean.GFHnJyqyTzRkFwxxes> mList;
    /**
     * 产废列表适配器
     */
    private CommonAdapter<CommonProductInfoBean.GFHnJyqyTzRkFwxxes> adapter;
    private Map<String, String> params = new HashMap<>();

    private List<CommonProductInfoBean.GFHnJyqyTzRkFwxxes> selectList = new ArrayList<>();

    /**主表id*/
    private String mainId;


    @Override
    protected int getLayout() {
        return R.layout.activity_synchrony;
    }

    @Override
    protected SelectXwqyWastePresenter createPresenter() {
        return new SelectXwqyWastePresenter();
    }

    @Override
    protected void initEventAndData() {

        mainId = getIntent().getStringExtra(Constants.MAINID);
        params.put("fwdlbh",getIntent().getStringExtra("fwdlbh"));
        params.put("fwxlbh",getIntent().getStringExtra("fwxlbh"));

        initRecycleView();
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop("关联数据");
        findViewById(R.id.ll_condition).setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));

    }


    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("关联数据");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.ll_save, R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            // 确定
            case R.id.ll_save:
                if (selectList.size() != 0) {
                    mPresenter.saveCkFwxx(selectList,ApiUrl.URL_SAVE_HN_XWQY_WASTE_OUTPUT);
                } else {
                    ToastUtil.showToast(mContext, "请至少选择一条数据！");
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

    /**
     * 关联数据列表初始化
     */
    private void initRecycleView() {
        mList = new ArrayList<>();
        pageNumber = mPresenter.initRefresh(refreshLayout, mContext, ApiUrl.getHnUrl(ApiUrl.URL_HN_XWQY_WASTE_INPUT_LIST), params);
        adapter = new CommonAdapter<CommonProductInfoBean.GFHnJyqyTzRkFwxxes>(mContext, R.layout.item_common_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final CommonProductInfoBean.GFHnJyqyTzRkFwxxes bean, final int position) {
                if (bean != null) {
                    ImageView cb = holder.getView(R.id.btn_select);
                    LinearLayout item_layout = holder.getView(R.id.item_layout);
                    cb.setVisibility(View.VISIBLE);
                    cb.setImageResource(bean.SELECT ? R.drawable.checkbox_theme : R.mipmap.ic_unselected);
                    item_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bean.SELECT = !bean.SELECT;
                            bean.ckId = bean.id;
                            bean.id = "";
                            bean.sl = bean.ckl;
                            bean.mainid = mainId;
                            bean.csdwid = bean.dwid;
                            bean.csdwmc = bean.entername;
                            if (bean.SELECT) {
                                selectList.add(bean);
                            } else {
                                selectList.remove(bean);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });

                    setText(holder.getView(R.id.tv_item1), bean.fwxxmc, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2), bean.fwdlbh, R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3), bean.fwxlbh, R.string.str_record_list2);
                    holder.getView(R.id.tv_item4).setVisibility(View.GONE);
                    setText(holder.getView(R.id.tv_item6), bean.ckl, R.string.empty_replace);
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
    private void fillRecycleView(PageBean<CommonProductInfoBean.GFHnJyqyTzRkFwxxes> model) {
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        List<CommonProductInfoBean.GFHnJyqyTzRkFwxxes> listBeans = GsonUtil.JsonToList(json, CommonProductInfoBean.GFHnJyqyTzRkFwxxes.class);
        for (int i = 0; i < listBeans.size(); i++) {
            listBeans.get(i).SELECT = false;
        }
        mList.addAll(listBeans);
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
    public void saveCkFwxxSuccess(Object result) {
        Intent intent = new Intent();
        intent.putExtra("selectBean",GsonUtil.GsonToString(result));
        setResult(10,intent);
        finish();
    }

    @Override
    public void showError(String msg) {
        toastShow(msg);
        hideLoading();
        mList.clear();
        adapter.notifyDataSetChanged();
        refreshLayout.finishRefresh();
    }
}
