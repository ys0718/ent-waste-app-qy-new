package com.grkj.ytjqy_pad.mvp.view.activity.czdw;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.HistoryInfoBean;
import com.grkj.ytjqy_pad.mvp.model.HtxxBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ProductDeviceBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.ZyldBean;
import com.grkj.ytjqy_pad.mvp.presenter.OutputLargeScanPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.ScanBaseActivity;
import com.grkj.ytjqy_pad.utils.common.AppUtils;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.popwindow.model.Select;
import com.grkj.ytjqy_pad.utils.popwindow.spinner.HistorySearchSpinner;
import com.grkj.ytjqy_pad.utils.popwindow.spinner.SearchSpinner;
import com.grkj.ytjqy_pad.utils.scancode.ScanCodeUtil;
import com.grkj.ytjqy_pad.utils.scancode.utils.Constant;
import com.grkj.ytjqy_pad.utils.view.AdjustableView;
import com.grkj.ytjqy_pad.utils.view.SelectTimeView;
import com.socks.library.KLog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2022/3/20.
 * 批量扫码出库
 */
public class OutputLargeScanActivity extends ScanBaseActivity<OutputLargeScanPresenter> implements OutputLargeScanPresenter.View {
    //运送部门经办人
    @BindView(R.id.et_item18)
    HistorySearchSpinner et_item18;

    //去向
    @BindView(R.id.et_item5)
    EditText et_item5;


    //总重量
    @BindView(R.id.et_item8)
    TextView et_item8;

    //总条数
    @BindView(R.id.tv_num)
    TextView tv_num;

    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //全选
    @BindView(R.id.iv_select_all)
    ImageView iv_select_all;

    //转移联单编号、接收单位名称父布局
    @BindView(R.id.ll_item20_21)
    LinearLayout ll_item20_21;

    //合同编号
    @BindView(R.id.ll_item20)
    LinearLayout ll_item20;

    //合同编号
    @BindView(R.id.editSpinner_20)
    SearchSpinner editSpinner_20;

    //转移联单编号
    @BindView(R.id.editSpinner_21)
    SearchSpinner editSpinner_21;

    //接收单位名称
    @BindView(R.id.editSpinner_22)
    SearchSpinner editSpinner_22;

    //搜索-入库时间
    @BindView(R.id.sv_rksj)
    SelectTimeView sv_rksj;

    //搜索-废物名称
    @BindView(R.id.et_scss)
    EditText et_fwmc;

    //收缩/展开
    AdjustableView adjustableView;

    @Override
    protected int getLayout() {
        return R.layout.activity_large_output;
    }

    private boolean isCzdw;//是否为处置单位

    private boolean isPlck;//是否批量出库

    private Map<String, String> params = new HashMap<>();

    @Override
    protected void initEventAndData() {

        isPlck = getIntent().getBooleanExtra("is_plck", false);
        findViewById(R.id.ll_scan_hor).setVisibility(isPlck ? View.GONE : View.VISIBLE);
        if (isPlck) {
            isSelectAll = false;
            iv_select_all.setImageResource(R.mipmap.ic_unselected);
            mPresenter.getTodoList(params);
            findViewById(R.id.ll_clear_all).setVisibility(View.GONE);
            findViewById(R.id.cv_search).setVisibility(View.VISIBLE);
        }

        isCzdw = getIntent().getBooleanExtra("isCzdw", false);
        Map<String, String> map = new HashMap<>();
        map.put("types", "出库去向");
        mPresenter.getParamList(map);
        mPresenter.getProductDeviceList();

        if (isCzdw) {
            ll_item20.setVisibility(View.VISIBLE);
            mPresenter.getHtList("");
        } else {
            ll_item20.setVisibility(View.GONE);
        }

        et_item18.setKey(Constants.KEY_CK);

        mPresenter.getHistoryList(Constants.KEY_CK);

    }

    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        initRecycleView();
    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("批量出库");
        adjustableView = findViewById(R.id.adjustableView);
        LinearLayout ll_top = findViewById(R.id.ll_top);

        adjustableView.setOnClickLayoutListener(new AdjustableView.OnClickLayoutListener() {
            @Override
            public void onClick(boolean isOpen) {
                ll_top.setVisibility(isOpen? View.VISIBLE: View.GONE);
            }
        });
//        mRecyclerView.setLayoutManager();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        initRecycleView();
    }




    @OnClick({R.id.ll_back, R.id.et_item5, R.id.ll_save, R.id.ll_scan, R.id.ll_select_all, R.id.ll_clear_all, R.id.ll_search,R.id.et_scss})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //全选
            case R.id.ll_select_all:
                isSelectAll = !isSelectAll;
                iv_select_all.setImageResource(isSelectAll ? R.mipmap.ic_selected : R.mipmap.ic_unselected);
                String totalWeight = "0.0";
                if (mList.size() != 0) {
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).SELECT = isSelectAll;
                        if (isSelectAll) {
                            totalWeight = new BigDecimal(totalWeight).add(new BigDecimal(Constants.kgToTon(mList.get(i).jldw, mList.get(i).rkl))).toString();
                        } else {
                            totalWeight = "0.0";
                        }
                    }
                }
                updateTodoListNum();
                et_item8.setText(totalWeight);
                adapter.notifyDataSetChanged();
                break;

            case R.id.ll_clear_all:
                mList.clear();
                adapter.getDatas().clear();
                et_item8.setText("0.0");
                adapter.notifyDataSetChanged();
                updateTodoListNum();
                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;

            //去向
            case R.id.et_item5:
                showPopWindow("出库去向", et_item5);
                break;

//            //生产设施
//            case R.id.et_scss:
//                showCsssPopWindow();
//                break;

            //开始扫描
            case R.id.ll_scan:
                et_item18.clearFocus();
                showScanDialog(0);
                break;

            //保存
            case R.id.ll_save:
                et_item18.clearFocus();
                mData.ysjbr = et_item18.getText().toString();
                mData.zyldbh = editSpinner_21.getText().toString();
                mData.jsdwmc = editSpinner_22.getText().toString();

                String dataFrom = isLandscape ? Constants.TYPE_YTJ : Constants.TYPE_SCZD;
                mData.versionFrom = AppUtils.getVersionName(mContext);
                String deviceCode = SPUtils.get(mContext, "DEVICE_CODE", "").toString();
                String dwid = SPUtils.get(mContext, Constants.SELECT_DWID, "").toString();

                if (TextUtils.isEmpty(checkEmpty())) {
                    if (mList.size() == 0) {
                        ToastUtil.showToast(mContext, "请扫描正确的二维码后保存！");
                    } else {
                        List<CommonProductInfoBean> commitList = new ArrayList<>();
                        for (int i = 0; i < mList.size(); i++) {

                            CommonProductInfoBean newData = mList.get(i);

                            if (mList.get(i).SELECT) {
                                newData.ysjbr = mData.ysjbr;
                                newData.zyldbh = mData.zyldbh;
                                newData.zyldid = mData.zyldid;
                                newData.jsdwmc = mData.jsdwmc;
                                newData.jsdwid = mData.jsdwid;
                                newData.ckjbr = mData.ckjbr;
                                newData.qx = mData.qx;
                                newData.ckl = newData.rkl;
                                newData.dataFrom = dataFrom;
                                newData.versionFrom = mData.versionFrom;
                                newData.lrfs = "2";
                                newData.deviceCode = deviceCode;

                                if (!TextUtils.isEmpty(dwid)) {
                                    newData.xwqyDwid = dwid;
                                }
                                newData.cksj = DateTimeUtil.getCurrentDate();
                                newData.fwId = newData.id;

                                newData.rkQrcodeValue = newData.qrcodeValue;
                                newData.gfYbqyTzCkZyFwxx = new CommonProductInfoBean.GfYbqyTzCkZyFwxx();
                                newData.gfYbqyTzCkZyFwxx.rkid = newData.id;
                                newData.gfYbqyTzCkZyFwxx.mlnf = newData.mlnf;
                                newData.gfYbqyTzCkZyFwxx.fwdlbh = newData.fwdlbh;
                                newData.gfYbqyTzCkZyFwxx.fwdlmc = newData.fwdlmc;
                                newData.gfYbqyTzCkZyFwxx.fwxlbh = newData.fwxlbh;
                                newData.gfYbqyTzCkZyFwxx.fwxlmc = newData.fwxlmc;
                                newData.gfYbqyTzCkZyFwxx.fwxxmc = newData.fwxxmc;
                                newData.gfYbqyTzCkZyFwxx.rkpch = newData.rkpch;
                                newData.gfYbqyTzCkZyFwxx.jldw = newData.jldw;
                                newData.gfYbqyTzCkZyFwxx.rqbzbm = newData.rqbzbm;
                                newData.gfYbqyTzCkZyFwxx.rqbzlx = newData.rqbzlx;
                                newData.gfYbqyTzCkZyFwxx.rqbzsl = newData.rqbzsl;
                                newData.gfYbqyTzCkZyFwxx.rqbzgg = newData.rqbzgg;
                                newData.gfYbqyTzCkZyFwxx.rqbzcz = newData.rqbzcz;
                                newData.gfYbqyTzCkZyFwxx.wlxz = newData.wlxz;
                                newData.gfYbqyTzCkZyFwxx.fwtx = newData.fwtx;
                                newData.gfYbqyTzCkZyFwxx.zcssbm = newData.zcssbm;
                                newData.gfYbqyTzCkZyFwxx.zcsslx = newData.zcsslx;
                                newData.gfYbqyTzCkZyFwxx.ckl = newData.ckl;
                                newData.id = "";
                                commitList.add(newData);

                            }
                        }

                        if (commitList.size() == 0) {
                            ToastUtil.showToast(mContext, "请至少选择一条入库信息！");
                            return;
                        }

                        mPresenter.showSaveDialog(commitList);
                    }

                } else {
                    ToastUtil.showToast(mContext, checkEmpty());
                }

                break;

            //搜索
            case R.id.ll_search:
                params.put("startTime",sv_rksj.getStartTime());
                params.put("endTime",sv_rksj.getEndTime());
                params.put("fwxxmc",et_fwmc.getText().toString());
                mPresenter.getTodoList(params);
                break;

        }
    }

    private ScanCodeUtil scanCodeUtil;

    @Override
    public void showScanDialog(long delayTime) {

        scanCodeUtil = new ScanCodeUtil(OutputLargeScanActivity.this, isLandscape, delayTime, Constant.REQUEST_CODE);
        scanCodeUtil.scanCode();
        scanCodeUtil.getResultListener(result -> {
            KLog.e("扫码结果为："+result);
            mPresenter.getScanInfo(result);

        });

        // TODO: 2023/11/20
        /*if (isLandscape) {
            if (roundCornerDialog == null) {
                roundCornerDialog = new ScanDialog(mContext);
                roundCornerDialog.setOnScanResultCallBack(new ScanDialog.OnScanResultCallBack() {
                    @Override
                    public void OnScanSuccess(String result) {
                        KLog.e("扫码结果：" + result);
                        roundCornerDialog.dismiss();
//                    mPresenter.getScanInfo(result);
                        mPresenter.getScanInfo(result);
                    }

                    @Override
                    public void OnScanFail(String errorMsg) {

                    }
                });
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //显示对话框
                    roundCornerDialog.show();
                }
            }, delayTime);
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // TODO: 2023/11/20
        if (data != null && requestCode == scanCodeUtil.scanRequestCode) {
            String scanCode = data.getStringExtra("ScanCodeResult");
            mPresenter.getScanInfo(scanCode);
        }

    }


    /*
     * 判空处理
     * */
    private String checkEmpty() {
        if (TextUtils.isEmpty(mData.qx)) {
            return "请选择去向！";
        } else if (TextUtils.isEmpty(mData.ysjbr)) {
            return "请输入运送部门经办人！";
        } else if (TextUtils.isEmpty(mData.zyldbh) && !isZx) {
            return "请输入转移联单编号！";
        } else if (TextUtils.isEmpty(mData.jsdwmc) && !isZx) {
            return "请输入接收单位名称！";
        } else {
            return "";
        }
    }


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private List<CommonProductInfoBean> mList = new ArrayList<>();
    ;//数据列表
    private CommonAdapter<CommonProductInfoBean> adapter;//适配器

    /**
     * 企业列表初始化
     */
    private void initRecycleView() {
        adapter = new CommonAdapter<CommonProductInfoBean>(mContext, R.layout.item_common_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final CommonProductInfoBean bean, final int position) {
                if (bean != null) {
                    holder.setText(R.id.tv_item1, TextUtils.isEmpty(bean.fwxxmc) ? getString(R.string.common_empty) : bean.fwxxmc);

                    String groupFwlb = getString(R.string.str_record_fwlb2, bean.fwdlbh, bean.fwdlmc);
                    setText(holder.getView(R.id.tv_item1), bean.fwxxmc, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2), groupFwlb, R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3), bean.fwxlbh, R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item4), bean.rksj, R.string.str_record_list11);
                    setText(holder.getView(R.id.tv_item5), bean.rkpch, R.string.str_record_list15);
                    setText(holder.getView(R.id.tv_item6), bean.rkl, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item7), bean.jldw, R.string.str_jldw_output);


                    ImageView cb = holder.getView(R.id.btn_select);
                    LinearLayout item_layout = holder.getView(R.id.item_layout);
                    cb.setVisibility(View.VISIBLE);
                    cb.setImageResource(bean.SELECT ? R.drawable.checkbox_theme : R.mipmap.ic_unselected);
                    item_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateSelectTotal(bean);
                            List<CommonProductInfoBean> newList = adapter.getDatas();
                            checkIsAll(newList);
                        }
                    });

                    ImageView iv_delete = holder.getView(R.id.iv_delete);
                    iv_delete.setVisibility(isPlck ? View.GONE : View.VISIBLE);

                    iv_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateDelTotal(bean);
                        }
                    });


                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                mList.get(i).SELECT = !mList.get(i).SELECT;
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                return false;
            }
        });

        mRecyclerView.setAdapter(adapter);
    }


//    private void checkIsAll(List<CommonProductInfoBean> newList){
//        isSelectAll = true;
//        if (newList!=null && newList.size()!=0){
//            for (int j = 0; j < newList.size(); j++) {
//                if (!newList.get(j).SELECT){
//                    isSelectAll = false;
//                }
//            }
//        }
//        iv_select_all.setImageResource(isSelectAll?R.mipmap.ic_selected:R.mipmap.ic_unselected);
//    }


    //更新选择后的总量
    private void updateSelectTotal(CommonProductInfoBean bean) {
        bean.SELECT = !bean.SELECT;
        String totalWeight = et_item8.getText().toString().trim();

        //千克转吨
        String rkl_t = Constants.kgToTon(bean.jldw, bean.rkl);

        if (TextUtils.isEmpty(totalWeight)) {
            totalWeight = "0";
        }
        if (bean.SELECT) {
            totalWeight = new BigDecimal(totalWeight).add(new BigDecimal(rkl_t)).toString();
        } else {
            totalWeight = new BigDecimal(totalWeight).subtract(new BigDecimal(rkl_t)).toString();
        }
        et_item8.setText(totalWeight);
        updateTodoListNum();
        adapter.notifyDataSetChanged();
    }

    //更新删除后的总量
    private void updateDelTotal(CommonProductInfoBean bean) {
        String totalWeight = et_item8.getText().toString().trim();
        //千克转吨
        String rkl_t = Constants.kgToTon(bean.jldw, bean.rkl);

        if (TextUtils.isEmpty(totalWeight)) {
            totalWeight = "0";
        }
        if (bean.SELECT) {
            String newWeight = new BigDecimal(totalWeight).subtract(new BigDecimal(rkl_t)).toString();
            et_item8.setText(newWeight);
        }

        mList.remove(bean);
        adapter.getDatas().remove(bean);
        updateTodoListNum();
        adapter.notifyDataSetChanged();
    }

    //更新待处理条数
    private void updateTodoListNum() {
        int selectNum = 0;
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).SELECT) {
                selectNum++;
            }
        }
//        tv_num.setText("待出库列表(共" + mList.size() + "条,已选中" + selectNum + "条)");
        tv_num.setText("(共" + mList.size() + "条,已选中" + selectNum + "条)");
    }


    private void setText(View tv, String value, int strRes) {
        tv.setVisibility(View.VISIBLE);

        ((TextView) tv).setText(Html.fromHtml(TextUtils.isEmpty(value) ? getString(strRes, getString(R.string.common_empty_text)) : getString(strRes, value)));
    }


    @Override
    protected OutputLargeScanPresenter createPresenter() {
        return new OutputLargeScanPresenter();
    }

    @Override
    public void showError(String msg) {
        toastShow(msg);
    }


    @Override
    public void getScanInfoSuccess(String bean) {
        scanCodeUtil.scanCode();
        if (!TextUtils.isEmpty(bean)) {
            CommonProductInfoBean CommonProductInfoBean = GsonUtil.GsonToBean(bean, CommonProductInfoBean.class);
            String result = "";
            if (mList.size() != 0) {
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).id.equals(CommonProductInfoBean.id)) {
                        result = "请勿重复扫描!";
                    }
                }
            }

            if (TextUtils.isEmpty(result)) {
                mList.add(CommonProductInfoBean);
                updateSelectTotal(CommonProductInfoBean);
                ToastUtil.showToast(mContext, "扫描成功，已添加到待出库列表！");
            } else {
                ToastUtil.showToast(mContext, result);
            }

        } else {
            ToastUtil.showToast(mContext, "请扫描正确的二维码！");
        }

//        showScanDialog(300);
    }

    private List<UserBean> temFromList = new ArrayList<>();

    @Override
    public void getReceiverListSuccess(List<UserBean> list) {
        String json = GsonUtil.GsonToString(list);
        receiverList = GsonUtil.JsonToList(json, UserBean.class);
        temFromList = receiverList;

        if (temFromList.size() == 0) {
            editSpinner_22.setHint("请输入");
            editSpinner_22.setText("/");
            return;
        }

        List<Select> selectList = new ArrayList<>();
        for (int i = 0; i < temFromList.size(); i++) {
            Select bean = new Select(temFromList.get(i).jsqymc, temFromList.get(i).jsqymc);
            selectList.add(bean);
        }
        editSpinner_22.setItemData(selectList);
        editSpinner_22.setDisabled(false);
        if (!TextUtils.isEmpty(mData.jsdwmc)) {
            editSpinner_22.setText(mData.jsdwmc);
        }
        editSpinner_22.setOnInsertListener(new SearchSpinner.onInsertListener() {
            @Override
            public void insertChange(String name, String value, boolean isFromSelect) {
                mData.jsdwmc = name;
//                mData.jsdwid = value;
                if (!isFromSelect) {//如果是输入或者是为空的时候筛选
                    mPresenter.getReceiverList(name);
                }
            }
        });

    }


    @Override
    public void getTodoListSuccess(List<CommonProductInfoBean> list) {
        mList = new ArrayList<>();
        mList.addAll(list);
        adapter.getDatas().clear();
        adapter.getDatas().addAll(mList);
        updateTodoListNum();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void saveDataSuccess(Object bean) {
        ToastUtil.showToast(mContext, "保存成功！");
        setResult(9);
        finish();
    }

    @Override
    public void getParamListSuccess(List<ParamListBean> bean) {
        String json = GsonUtil.GsonToString(bean);
        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);
    }

    private List<HtxxBean> temHtxxList = new ArrayList<>();

    @Override
    public void getHtListSuccess(List<HtxxBean> list) {
        temHtxxList.clear();
        temHtxxList = list;

        if (temHtxxList.size() == 0) {
            return;
        }

        List<Select> selectList = new ArrayList<>();
        for (int i = 0; i < temHtxxList.size(); i++) {
            Select bean = new Select(temHtxxList.get(i).htbh, temHtxxList.get(i).id);
            selectList.add(bean);
        }
        editSpinner_20.setItemData(selectList);
        editSpinner_20.setDisabled(false);
        if (!TextUtils.isEmpty(mData.htbh)) {
            editSpinner_20.setText(mData.htbh);
        }
        editSpinner_20.setOnInsertListener(new SearchSpinner.onInsertListener() {
            @Override
            public void insertChange(String name, String value, boolean isFromSelect) {
                KLog.e("editSpinner", name);
                mData.htbh = name;
                mData.htid = value;
                if (!isFromSelect) {
                    mPresenter.getHtList(name);
                }

            }
        });

    }

    private List<ProductDeviceBean> productDeviceList = new ArrayList<>();

    @Override
    public void getProductDeviceListSuccess(List<ProductDeviceBean> list) {
        String json = GsonUtil.GsonToString(list);
        productDeviceList = GsonUtil.JsonToList(json, ProductDeviceBean.class);

    }

    @Override
    public void getHistoryListSuccess(List<HistoryInfoBean> list) {
        String json = GsonUtil.GsonToString(list);
        List<HistoryInfoBean> temList = GsonUtil.JsonToList(json, HistoryInfoBean.class);

        if (temList.size() != 0){
            mData.ckjbr = temList.get(0).name;
        }else {
            String defaultUserName = SPUtils.get(mContext, Constants.USER_NAME, "").toString();
            mData.ckjbr = defaultUserName;
        }
    }

    private List<ZyldBean> temZyldList = new ArrayList<>();

    @Override
    public void getZyldListSuccess(List<ZyldBean> list) {
        temZyldList.clear();
        temZyldList = list;

        if (temZyldList.size() == 0) {
            editSpinner_21.setHint("请输入");
            editSpinner_21.setText("/");
            return;
        }

        List<Select> selectList = new ArrayList<>();
        for (int i = 0; i < temZyldList.size(); i++) {
            Select bean = new Select(temZyldList.get(i).number1, temZyldList.get(i).id);
            selectList.add(bean);
        }
        editSpinner_21.setItemData(selectList);
        editSpinner_21.setDisabled(false);
        if (!TextUtils.isEmpty(mData.zyldbh)) {
            editSpinner_21.setText(mData.zyldbh);
        }
        editSpinner_21.setOnInsertListener(new SearchSpinner.onInsertListener() {
            @Override
            public void insertChange(String name, String value, boolean isFromSelect) {
                KLog.e("editSpinner", name);
                mData.zyldbh = name;
                mData.zyldid = value;

                for (int i = 0; i < temZyldList.size(); i++) {
                    if (temZyldList.get(i).id.equals(value)) {
                        mData.jsdwmc = temZyldList.get(i).jsdwmc;
                        editSpinner_22.setText(mData.jsdwmc);
                    }
                }
                if (!isFromSelect) {
                    mPresenter.getZyldList(name);
                }
            }
        });

    }


    private SelectPopupWindow selectPopupWindow;
    protected List<ParamListBean> allParamList = new ArrayList<>();
    protected List<UserBean> receiverList = new ArrayList<>();
    private CommonProductInfoBean mData = new CommonProductInfoBean();


    //初始化下拉弹窗
    private void showPopWindow(String type, EditText item) {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < allParamList.size(); i++) {
            List<ParamListBean> childList = allParamList.get(i).childList;

            if (childList != null && childList.size() != 0 && type.equals(allParamList.get(i).type)) {
                for (int j = 0; j < childList.size(); j++) {
                    selectPopupWindowList.add(new SelectPopupWindowBean(childList.get(j).code, childList.get(j).value));
                }
            }
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                item.setText(model.NAME);
                //废物特性,物理特性,包装方式,计量单位,处理方式及去向,包装材质,包装规模
                switch (mType) {
                    case "出库去向":
                        mData.qx = model.ID;
                        showHideByQx();
                        break;
                }
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }

        selectPopupWindow.show(mLayout, type, (View) item.getParent(), sort);

    }


    private boolean isZx = false;

    //如果为自行利用/处置时，隐藏转移联单编号、接收单位名称
    private void showHideByQx() {
        if (commonTransfer(mData.qx, "出库去向").contains("自行")) {
            ll_item20_21.setVisibility(View.GONE);
            isZx = true;
            //自控
            editSpinner_21.setText("/");
            editSpinner_22.setText("/");
            mData.jsdwmc = "";
            mData.jsdwid = "";
        } else {
            mPresenter.getZyldList("");
            mPresenter.getReceiverList("");
            ll_item20_21.setVisibility(View.VISIBLE);
            isZx = false;
            if (isCzdw) {
                mPresenter.getHtList("");
            }
        }
    }


    /*
     * 通用转义
     * */
    protected String commonTransfer(String code, String type) {
        String result = "";
        for (int i = 0; i < allParamList.size(); i++) {
            List<ParamListBean> childList = allParamList.get(i).childList;
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


    //初始产生设施下拉弹窗
    private void showCsssPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < productDeviceList.size(); i++) {
            ProductDeviceBean mData = productDeviceList.get(i);
            String name = getString(R.string.str_record_fwlb2, mData.scssbh, mData.scssmc);
            selectPopupWindowList.add(new SelectPopupWindowBean(productDeviceList.get(i).id, name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                ProductDeviceBean bean = new ProductDeviceBean();
                for (int i = 0; i < productDeviceList.size(); i++) {
                    if (productDeviceList.get(i).id.equals(model.ID)) {
                        bean = productDeviceList.get(i);
                    }
                }
                params.put("scssmc",bean.scssmc);
                et_fwmc.setText(model.NAME);
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_fwmc.getParent(), sort);
    }



}
