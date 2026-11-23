package com.grkj.ytjqy_pad.mvp.view.activity.zyld;

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
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.zyld.GfWxfwZyldFwxx;
import com.grkj.ytjqy_pad.mvp.model.zyld.GfWxfwZyldFwxxPch;
import com.grkj.ytjqy_pad.mvp.presenter.SelectZypchListPresenter;
import com.grkj.ytjqy_pad.mvp.presenter.SelectZypchListPresenter;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
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
 * 转移批次号多选列表
 */

public class SelectMultiZypchListActivity extends BaseActivity<SelectZypchListPresenter> implements SelectZypchListPresenter.View {

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

    //废物名称-布局
    @BindView(R.id.ll_search2)
    LinearLayout ll_search2;

    //主布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;


    private int totalPage;
    private int pageNumber = 1;
    private List<GfWxfwZyldFwxxPch> mList;//产废列表
    private CommonAdapter<GfWxfwZyldFwxxPch> adapter;//产废列表适配器
    private Map<String, String> params = new HashMap<>();


    private boolean isSingleSelected = false;

    //已选中的废物列表
    private List<GfWxfwZyldFwxxPch> wasteSelectedList = new ArrayList<>();
    private GfWxfwZyldFwxx parentData = new GfWxfwZyldFwxx();

    @Override
    protected int getLayout() {
        return R.layout.activity_select_waste_list;
    }

    @Override
    protected void initEventAndData() {
        //废物信息
        String wasteSelectBeanStr = getIntent().getStringExtra(Constants.JUMP_KEY);
        if (!TextUtils.isEmpty(wasteSelectBeanStr)) {
            parentData = GsonUtil.GsonToBean(wasteSelectBeanStr, GfWxfwZyldFwxx.class);
        }


//
//        //产废0 出库1
//        String source = getIntent().getStringExtra("source");
//        params.put("source",source);
//
//        //选中废物信息
//        String wasteSelectBeanStr = getIntent().getStringExtra("wasteSelectBean");
//        if (!TextUtils.isEmpty(wasteSelectBeanStr)) {
//            wasteSelectBean = GsonUtil.GsonToBean(wasteSelectBeanStr, GfWxfwZyldFwxxPch.class);
//        } else {
//            wasteSelectBean = new GfWxfwZyldFwxxPch();
//        }
//
//        //已选中的废物列表
//        String wasteSelectedListStr = getIntent().getStringExtra("wasteSelectedList");
//        if (!TextUtils.isEmpty(wasteSelectedListStr)) {
//            wasteSelectedList = GsonUtil.JsonToList(wasteSelectedListStr, GfWxfwZyldFwxxPch.class);
//
//            //选中处理
//            for (int i = 0; i < wasteSelectedList.size(); i++) {
//                wasteSelectedList.get(i).SELECT = true;
//
//                if (!TextUtils.isEmpty(wasteSelectedList.get(i).wwlyczl)) {
//                    wasteSelectedList.get(i).zxlyczl = wasteSelectedList.get(i).wwlyczl;
//                }
//
//            }
//        } else {
//            wasteSelectedList = new ArrayList<>();
//        }


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
        initTopToolbar("选择批次号信息");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }


    @OnClick({R.id.ll_save, R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            // 确定
            case R.id.ll_save:

                List<GfWxfwZyldFwxxPch> commitList = new ArrayList<>();
                for (int i = 0; i < mList.size(); i++) {
                    GfWxfwZyldFwxxPch newData = mList.get(i);
                    if (mList.get(i).SELECT) {
                        newData.id = "";
                        newData.mainid = parentData.mainid;
                        newData.childid = parentData.id;
                        commitList.add(newData);
                    }
                }

                if (commitList.size() == 0) {
                    ToastUtil.showToast(mContext, "请至少选择一条产生批次号！");
                    return;
                }

                mPresenter.showSaveDialog(commitList);
                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;

        }
    }


    @Override
    protected SelectZypchListPresenter createPresenter() {
        return new SelectZypchListPresenter();
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

        if (!TextUtils.isEmpty(parentData.fwdm)) {
            params.put("fwdm", parentData.fwdm);
        }

        if (!TextUtils.isEmpty(parentData.fwmc)) {
            params.put("fwmc", parentData.fwmc);
        }

        if (!TextUtils.isEmpty(parentData.zylx)) {
            params.put("zylx", parentData.zylx);
        }

        pageNumber = mPresenter.initRefresh(refreshLayout, mContext, ApiUrl.URL_SELECT_MULTI_ZYPCH_LIST, params);
        adapter = new CommonAdapter<GfWxfwZyldFwxxPch>(mContext, R.layout.item_common_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final GfWxfwZyldFwxxPch bean, final int position) {
                if (bean != null) {
                    ImageView cb = holder.getView(R.id.btn_select);
                    LinearLayout item_layout = holder.getView(R.id.item_layout);
                    cb.setVisibility(View.VISIBLE);
                    cb.setImageResource(bean.SELECT ? R.drawable.checkbox_theme : R.mipmap.ic_unselected);
                    item_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bean.SELECT = !bean.SELECT;
                            adapter.notifyDataSetChanged();
                        }
                    });

                    setText(holder.getView(R.id.tv_item1), bean.tzpch, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2), bean.szsbm, R.string.str_zyld_item31);
                    setText(holder.getView(R.id.tv_item3), TextUtils.equals("1", bean.dataSource) ? "产生台账" : "出库台账", R.string.str_zyld_item32);
                    setText(holder.getView(R.id.tv_item6), bean.zysl, R.string.empty_replace);
                    holder.setText(R.id.tv_item7, "转移数量(" + bean.jldw + ")");
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

    private List<GfWxfwZyldFwxxPch> temList = new ArrayList<>();

    /**
     * 向列表视图填充数据
     */
    private void fillRecycleView(PageBean<GfWxfwZyldFwxxPch> model) {
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        temList = GsonUtil.JsonToList(json, GfWxfwZyldFwxxPch.class);

        List<GfWxfwZyldFwxxPch> commonList = new ArrayList<>();

        //剔除重复
        if (temList.size() != 0 && temList != null && wasteSelectedList.size() != 0 && wasteSelectedList != null) {
            for (int i = 0; i < temList.size(); i++) {
                for (int j = 0; j < wasteSelectedList.size(); j++) {
                    if (wasteSelectedList.get(j).id.equals(temList.get(i).id)) {
                        commonList.add(temList.get(i));
                    }
                }
            }

//            Iterator<GfWxfwZyldFwxxPch> iterator = temList.iterator();
//
//            for (int i = 0; i < wasteSelectedList.size(); i++) {
//                while (iterator.hasNext()) {
//                    if (wasteSelectedList.get(i).csfwid.equals(iterator.next().csfwid)) {
//                        iterator.remove();
//                    }
//                }
//            }

        }

        temList.removeAll(commonList);
        mList.addAll(wasteSelectedList);
        mList.addAll(temList);
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
    public void saveDataSuccess(Object bean) {
        ToastUtil.showToast(mContext, "保存成功！");
        setResult(9);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }
    }


}
