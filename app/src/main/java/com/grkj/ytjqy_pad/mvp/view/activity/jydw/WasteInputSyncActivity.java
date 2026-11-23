package com.grkj.ytjqy_pad.mvp.view.activity.jydw;

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
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.WasteTypeListBean;
import com.grkj.ytjqy_pad.mvp.presenter.SyncWasteInputPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.popwindow.model.Select;
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

/**
 * 危废入库登记同步数据页面
 *
 * @author JueHongP
 */
public class WasteInputSyncActivity extends BaseActivity<SyncWasteInputPresenter> implements SyncWasteInputPresenter.View {

    /**
     * 父布局
     */
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    /**
     * 联单编号
     */
    @BindView(R.id.et_ldbh)
    EditText et_ldbh;

    /**
     * 废物类别
     */
    @BindView(R.id.et_fwlb)
    EditText et_fwlb;

    /**
     * 废物代码
     */
    @BindView(R.id.et_fwxl)
    EditText et_fwxl;

    /**
     * 开始结束时间
     */
    @BindView(R.id.v_select_time)
    SelectTimeView v_select_time;

    /**
     * 搜索
     */
    @BindView(R.id.ll_search)
    LinearLayout ll_search;

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
    private List<CommonProductInfoBean> mList;
    /**
     * 产废列表适配器
     */
    private CommonAdapter<CommonProductInfoBean> adapter;
    private Map<String, String> params = new HashMap<>();
    private CommonProductInfoBean selectBean = new CommonProductInfoBean();


    @Override
    protected int getLayout() {
        return R.layout.activity_synchrony;
    }

    @Override
    protected SyncWasteInputPresenter createPresenter() {
        return new SyncWasteInputPresenter();
    }

    @Override
    protected void initEventAndData() {
        initSelectCondition();
        initRecycleView();
        refreshLayout.autoRefresh();
    }

    /**
     * 初始化搜索条件
     */
    private void initSelectCondition() {
        et_fwlb.setHint("请选择废物类别");
        et_fwxl.setHint("请选择废物代码");
        mPresenter.getWasteTypeList();
        v_select_time.setOnTimeChangedListener(new SelectTimeView.OnTimeChangedListener() {
            @Override
            public void startTimeChanged(String startTime, String endTime) {
                params.put("startTime", startTime);
                params.put("endTime", endTime);
            }
        });
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
        initTopToolbar("同步数据");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.et_fwlb, R.id.et_fwxl, R.id.ll_search, R.id.ll_save, R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //废物大别下拉框
            case R.id.et_fwlb:
                initSelectPopupWindow("废物大类", et_fwlb);
                break;
            //废物小类下拉框
            case R.id.et_fwxl:
                initSelectPopupWindow("废物小类", et_fwxl);
                break;
            //搜索
            case R.id.ll_search:
                params.put("number1", et_ldbh.getText().toString());
                params.put("fwdlbh", et_fwlb.getText().toString());
                params.put("fwxlbh", et_fwxl.getText().toString());
                refreshLayout.autoRefresh();
                break;
            // 确定
            case R.id.ll_save:
                if (selectBean.SELECT) {
                    selectBean.sgfId = selectBean.id;
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

    /**
     * 同步数据列表初始化
     */
    private void initRecycleView() {
        mList = new ArrayList<>();
        params.put("fwzt", "1");   //1-待入库 2-已入库
        pageNumber = mPresenter.initRefresh(refreshLayout, mContext, ApiUrl.getHnUrl(ApiUrl.URL_WAIT_INPUT_LIST), params);
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
                    setText(holder.getView(R.id.tv_item2), bean.ldbh, R.string.str_ldbh);
                    setText(holder.getView(R.id.tv_item3), bean.fwdlbh, R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item4), bean.fwxlbh, R.string.str_record_list2);

                    setText(holder.getView(R.id.tv_item6), bean.jsl, R.string.empty_replace);
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
    private void fillRecycleView(PageBean<CommonProductInfoBean> model) {
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        List<CommonProductInfoBean> listBeans = GsonUtil.JsonToList(json, CommonProductInfoBean.class);
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

    private List<WasteTypeListBean> allWasteTypeList = new ArrayList<>();

    @Override
    public void getWasteTypeListSuccess(List<WasteTypeListBean> result) {
        String json = GsonUtil.GsonToString(result);
        allWasteTypeList = GsonUtil.JsonToList(json, WasteTypeListBean.class);
    }

    private SelectPopupWindow selectPopupWindow;

    /**
     * 搜索下拉框
     */
    private void showSelectPopupWindow(String type, EditText editText) {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();
        WasteTypeListBean data = new WasteTypeListBean();
        List<WasteTypeListBean.ChildList> childList = new ArrayList<>();
        WasteTypeListBean.ChildList childListData = new WasteTypeListBean.ChildList();

        for (int i = 0; i < allWasteTypeList.size(); i++) {
            data = allWasteTypeList.get(i);
            childList = data.childList;
            if (type.equals("废物大类")) {
                selectPopupWindowList.add(new SelectPopupWindowBean(data.fwlb, getString(R.string.str_input_fwlb, data.fwlb, data.fwmc)));
            } else if (type.equals("废物小类")) {
                for (int j = 0; j < childList.size(); j++) {
                    childListData = childList.get(j);
                    if (et_fwlb.getText().toString().equals(childListData.pcode)) {
                        selectPopupWindowList.add(new SelectPopupWindowBean(childListData.fwlb, getString(R.string.str_input_fwlb, childListData.fwlb, data.fwmc)));
                    }
                }
            }
        }

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                editText.setText(model.ID);
                selectPopupWindow.dismiss();
            }
        });

        selectPopupWindow.show(mLayout, type, (View) editText.getParent(), sort);
    }

    /**
     * 初始化搜索下拉框
     */
    private void initSelectPopupWindow(String type, EditText editText) {
        switch (type) {
            case "废物大类":
                showSelectPopupWindow(type, editText);
                break;
            case "废物小类":
                if (TextUtils.isEmpty(et_fwlb.getText().toString())) {
                    toastShow("请先选择废物类别");
                } else {
                    showSelectPopupWindow(type, editText);
                }
                break;
            default:
                break;
        }
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
