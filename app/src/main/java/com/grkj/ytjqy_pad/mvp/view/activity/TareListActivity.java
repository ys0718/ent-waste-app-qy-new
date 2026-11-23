package com.grkj.ytjqy_pad.mvp.view.activity;

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
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.TareListBean;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.presenter.TareListPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
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
 * 去皮选择列表
 */

public class TareListActivity extends BaseActivity<TareListPresenter> implements TareListPresenter.View {

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
    private List<TareListBean> mList;//企业列表
    private CommonAdapter<TareListBean> adapter;//企业列表适配器
    private String fwxxmcKeyWord;//废物下拉搜索
    private Map<String, String> params = new HashMap<>();
    private TareListBean selectBean = new TareListBean();

    private ZcssBean zcssSelectBean = null;
    private CommonProductInfoBean wasteSelectBean = null;

    @Override
    protected int getLayout() {
        return R.layout.activity_tare_list;
    }

    @Override
    protected void initEventAndData() {
        tv_left_bottom2.setText("库存总量(吨)");
        //参数列表
        String allParamStr = getIntent().getStringExtra("allParamList");
        allParamList = GsonUtil.JsonToList(allParamStr, ParamListBean.class);

//        //选中贮存设施
//        String zcssSelectBeanStr = getIntent().getStringExtra("zcssSelectBean");
//        if (!TextUtils.isEmpty(zcssSelectBeanStr)) {
//            zcssSelectBean = GsonUtil.GsonToBean(zcssSelectBeanStr, ZcssBean.class);
//            et_item1.setText(zcssSelectBean.ssmc);
//            params.put("zcssmcId", zcssSelectBean.id);
//        }
//
//
//        //选中废物信息
//        String wasteSelectBeanStr = getIntent().getStringExtra("wasteSelectBean");
//        if (!TextUtils.isEmpty(wasteSelectBeanStr)) {
//            wasteSelectBean = GsonUtil.GsonToBean(wasteSelectBeanStr, CommonProductInfoBean.class);
//            et_item2.setText(wasteSelectBean.fwdlmc);
//            params.put("fwId", wasteSelectBean.id);
//        }
//        //贮存设施列表
//        String zcssListStr = getIntent().getStringExtra("zcssList");
//
//        if(!TextUtils.isEmpty(zcssListStr)){
//            zcssList = GsonUtil.JsonToList(zcssListStr, ZcssBean.class);
//        }else {
//            ll_search.setVisibility(View.GONE);
//        }
//
//
//        //废物信息列表
//        String wasteTypeListStr = getIntent().getStringExtra("wasteTypeList");
//
//        if (!TextUtils.isEmpty(wasteTypeListStr)) {
//            wasteTypeList = GsonUtil.JsonToList(wasteTypeListStr, CommonProductInfoBean.class);
//        }

        refreshLayout.autoRefresh();
        mPresenter.getTypeList();
    }

    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
        initGridView();
    }


    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initRecycleView();
        initTopToolbar("选择皮重");
    }

    @OnClick({R.id.ll_back, R.id.ll_search, R.id.ll_search2, R.id.ll_add2, R.id.ll_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 返回
            case R.id.ll_back:
                finish();
                break;

            // 确定
            case R.id.ll_save:
                if (selectBean.SELECT) {
                    Intent result = new Intent();
                    result.putExtra("selectBean", GsonUtil.GsonToString(selectBean));
                    result.putExtra("zcssSelectBean", GsonUtil.GsonToString(zcssSelectBean));
                    result.putExtra("wasteSelectBean", GsonUtil.GsonToString(wasteSelectBean));
                    result.putExtra("wasteTypeList", GsonUtil.GsonToString(wasteTypeList));
                    setResult(9, result);
                    finish();
                } else {
                    ToastUtil.showToast(mContext, "请选择皮重！");
                }

                break;

            // 新增
//            case R.id.ll_add:
            case R.id.ll_add2:
                startActivityForResult(new Intent(mContext, TareInfoActivity.class), 9);
                break;

            // 贮存设备
            case R.id.ll_search:
                showZcssPopWindow();
                break;

            // 废物名称
            case R.id.ll_search2:
//                if (TextUtils.isEmpty(et_item1.getText().toString())) {
//                    ToastUtil.showToast(mContext, "请先选择贮存设备！");
//                } else {
//                    showFwlbPopWindow();
//                }
                showFwlbPopWindow();
                break;

        }
    }


    @Override
    protected TareListPresenter createPresenter() {
        return new TareListPresenter();
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mList = new ArrayList<>();
        pageNumber = mPresenter.initRefresh(refreshLayout, mContext, ApiUrl.URL_TARE_LIST, params);
        adapter = new CommonAdapter<TareListBean>(mContext, R.layout.item_common_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final TareListBean bean, final int position) {
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

                    item_layout.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            startActivityForResult(new Intent(mContext, TareInfoActivity.class)
                                    .putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(mList.get(position))), 9);

                            return false;
                        }
                    });

                    setText(holder.getView(R.id.tv_item1), bean.fwxxmc, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2), commonTransfer(bean.rqbzlx, "包装方式"), R.string.str_record_list7);
                    setText(holder.getView(R.id.tv_item3), commonTransfer(bean.rqbzcz, "包装材质"), R.string.str_record_list8);
                    setText(holder.getView(R.id.tv_item4), commonTransfer(bean.rqbzgg, "包装规模"), R.string.str_record_list9);
                    setText(holder.getView(R.id.tv_item6), bean.pzl, R.string.empty_replace);
                    holder.setText(R.id.tv_item7, "皮重(kg)");
                }
            }
        };


        mRecyclerView.setAdapter(adapter);
    }


    /**
     * 企业列表初始化
     */
    private void initGridView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mList = new ArrayList<>();
        pageNumber = mPresenter.initRefresh(refreshLayout, mContext, ApiUrl.URL_TARE_LIST, params);
        adapter = new CommonAdapter<TareListBean>(mContext, R.layout.item_common_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final TareListBean bean, final int position) {
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

                    item_layout.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            startActivityForResult(new Intent(mContext, TareInfoActivity.class)
                                    .putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(mList.get(position))), 9);

                            return false;
                        }
                    });

                    setText(holder.getView(R.id.tv_item1), bean.fwxxmc, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2), commonTransfer(bean.rqbzlx, "包装方式"), R.string.str_record_list7);
                    setText(holder.getView(R.id.tv_item3), commonTransfer(bean.rqbzcz, "包装材质"), R.string.str_record_list8);
                    setText(holder.getView(R.id.tv_item4), commonTransfer(bean.rqbzgg, "包装规模"), R.string.str_record_list9);
                    setText(holder.getView(R.id.tv_item6), bean.pzl, R.string.empty_replace);
                    holder.setText(R.id.tv_item7, "皮重(kg)");
                }
            }
        };


        mRecyclerView.setAdapter(adapter);
    }


    private void setText(View tv, String value, int strRes) {
        tv.setVisibility(View.VISIBLE);
        ((TextView) tv).setText(TextUtils.isEmpty(value) ? getString(strRes, getString(R.string.common_empty_text)) : getString(strRes, value));
    }


    private List<ParamListBean> allParamList = new ArrayList<>();

    /*
     * 通用转义
     * */
    private String commonTransfer(String code, String type) {
        String result = "";
        for (int i = 0; i < allParamList.size(); i++) {
            ArrayList<ParamListBean> childList = allParamList.get(i).childList;
            if (childList != null && childList.size() != 0 && type.equals(allParamList.get(i).type)) {
                for (int j = 0; j < childList.size(); j++) {
                    if (!TextUtils.isEmpty(code) && code.equals(childList.get(j).code)) {
                        result = childList.get(j).value;
                    }
                }
            }
        }
        return result;
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
    private void fillRecycleView(PageBean<TareListBean> model) {
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        List<TareListBean> LabelInfoBeans = GsonUtil.JsonToList(json, TareListBean.class);
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
    public void getTotalWeightSuccess(String total) {
        if (!TextUtils.isEmpty(total)) {
            tv_left_bottom.setText(total);
        } else {
            tv_left_bottom.setText(getResources().getString(R.string.common_zero));
        }
    }


    @Override
    public void getTypeListSuccess(List<CommonProductInfoBean> bean) {
        wasteSelectBean = new CommonProductInfoBean();
        et_item2.setText("");
        String json = GsonUtil.GsonToString(bean);
        wasteTypeList = GsonUtil.JsonToList(json, CommonProductInfoBean.class);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }
    }

    private SelectPopupWindow selectPopupWindow;

    private List<ZcssBean> zcssList = new ArrayList<>();
    private List<CommonProductInfoBean> wasteTypeList = new ArrayList<>();

    //初始贮存设施下拉弹窗
    private void showZcssPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();
        selectPopupWindowList.add(new SelectPopupWindowBean("", "全部"));
        for (int i = 0; i < zcssList.size(); i++) {
            ZcssBean mData = zcssList.get(i);
            String name = getString(R.string.str_record_fwlb2, mData.no, mData.ssmc);
            selectPopupWindowList.add(new SelectPopupWindowBean(zcssList.get(i).id, name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                if (TextUtils.isEmpty(model.ID)) {//如果是全部
                    zcssSelectBean = new ZcssBean();
                    wasteSelectBean = new CommonProductInfoBean();
                    wasteTypeList = new ArrayList<>();
                    et_item2.setText("");
                    params.put("zcssmcId", model.ID);
                    params.put("fwId", model.ID);
                } else {
                    for (int i = 0; i < zcssList.size(); i++) {
                        if (zcssList.get(i).id.equals(model.ID)) {
                            zcssSelectBean = zcssList.get(i);
                        }
                    }
                    et_item1.setText(zcssSelectBean.ssmc);
                    params.put("zcssmcId", zcssSelectBean.id);
                    mPresenter.getTypeList();
                }


                refreshLayout.autoRefresh();
                selectPopupWindow.dismiss();
            }
        });

        selectPopupWindow.show(mLayout, "", ll_search, selectPopupWindowList.size() + 1);
    }


    //初始废物类别下拉弹窗
    private void showFwlbPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();
        selectPopupWindowList.add(new SelectPopupWindowBean("", "全部"));

        for (int i = 0; i < wasteTypeList.size(); i++) {
            CommonProductInfoBean mData = wasteTypeList.get(i);
            String name = getString(R.string.str_record_fwlb, mData.fwxxmc, mData.fwdlbh, mData.fwdlmc, mData.fwxlbh);
            selectPopupWindowList.add(new SelectPopupWindowBean(wasteTypeList.get(i).id, name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {

                if (TextUtils.isEmpty(model.ID)) {//如果是全部
                    wasteSelectBean = new CommonProductInfoBean();
                    et_item2.setText(model.NAME);
                    params.put("fwId", model.ID);
                } else {
                    for (int i = 0; i < wasteTypeList.size(); i++) {
                        if (wasteTypeList.get(i).id.equals(model.ID)) {
                            wasteSelectBean = wasteTypeList.get(i);
                        }
                    }
                    et_item2.setText(wasteSelectBean.fwxxmc);
//                    params.put("fwId", wasteSelectBean.id);
                    params.put("fwxxmcKeyWord",wasteSelectBean.fwxxmc);
                }

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
