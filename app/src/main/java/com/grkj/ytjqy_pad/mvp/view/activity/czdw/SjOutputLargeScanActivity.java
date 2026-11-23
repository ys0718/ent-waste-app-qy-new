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
import com.grkj.ytjqy_pad.mvp.presenter.SjOutputLargeScanPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.ScanBaseActivity;
import com.grkj.ytjqy_pad.utils.common.AppUtils;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
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
 * 收集批量扫码出库
 */
public class SjOutputLargeScanActivity extends ScanBaseActivity<SjOutputLargeScanPresenter> implements SjOutputLargeScanPresenter.View {

    //运输经办人
    @BindView(R.id.et_item15)
    EditText et_item15;

    //出库经办人
    @BindView(R.id.et_item16)
    EditText et_item16;

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
        return R.layout.activity_sj_large_output;
    }

    private boolean isPlck;//是否批量出库

    private CommonProductInfoBean mData = new CommonProductInfoBean();

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

        String defaultUserName = SPUtils.get(mContext, Constants.USER_NAME, "").toString();
        et_item16.setText(defaultUserName);
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
                ll_top.setVisibility(isOpen ? View.VISIBLE : View.GONE);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        initRecycleView();
    }


    @OnClick({R.id.ll_back, R.id.ll_save, R.id.ll_scan, R.id.ll_select_all, R.id.ll_clear_all, R.id.ll_search, R.id.et_scss})
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


            //开始扫描
            case R.id.ll_scan:
                showScanDialog(0);
                break;

            //保存
            case R.id.ll_save:
                mData.ysjbr = et_item15.getText().toString();
                mData.ckjbr = et_item16.getText().toString();
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
                                newData.ckjbr = mData.ckjbr;
                                newData.ckl = newData.rkl;
                                newData.dataFrom = dataFrom;
                                newData.versionFrom = mData.versionFrom;
                                newData.lrfs = "2";
                                newData.deviceCode = deviceCode;

                                newData.cksj = DateTimeUtil.getCurrentDate();
                                newData.rkId = newData.id;
                                newData.id = "";
                                commitList.add(newData);
                                //                                newData.zyldbh = mData.zyldbh;
//                                newData.zyldid = mData.zyldid;
//                                newData.jsdwmc = mData.jsdwmc;
//                                newData.jsdwid = mData.jsdwid;
//                                newData.qx = mData.qx;
//                                if (!TextUtils.isEmpty(dwid)) {
//                                    newData.xwqyDwid = dwid;
//                                }
//                                newData.fwId = newData.id;
//                                newData.rkQrcodeValue = newData.qrcodeValue;
//                                newData.gfYbqyTzCkZyFwxx = new CommonProductInfoBean.GfYbqyTzCkZyFwxx();
//                                newData.gfYbqyTzCkZyFwxx.rkid = newData.id;
//                                newData.gfYbqyTzCkZyFwxx.mlnf = newData.mlnf;
//                                newData.gfYbqyTzCkZyFwxx.fwdlbh = newData.fwdlbh;
//                                newData.gfYbqyTzCkZyFwxx.fwdlmc = newData.fwdlmc;
//                                newData.gfYbqyTzCkZyFwxx.fwxlbh = newData.fwxlbh;
//                                newData.gfYbqyTzCkZyFwxx.fwxlmc = newData.fwxlmc;
//                                newData.gfYbqyTzCkZyFwxx.fwxxmc = newData.fwxxmc;
//                                newData.gfYbqyTzCkZyFwxx.rkpch = newData.rkpch;
//                                newData.gfYbqyTzCkZyFwxx.jldw = newData.jldw;
//                                newData.gfYbqyTzCkZyFwxx.rqbzbm = newData.rqbzbm;
//                                newData.gfYbqyTzCkZyFwxx.rqbzlx = newData.rqbzlx;
//                                newData.gfYbqyTzCkZyFwxx.rqbzsl = newData.rqbzsl;
//                                newData.gfYbqyTzCkZyFwxx.rqbzgg = newData.rqbzgg;
//                                newData.gfYbqyTzCkZyFwxx.rqbzcz = newData.rqbzcz;
//                                newData.gfYbqyTzCkZyFwxx.wlxz = newData.wlxz;
//                                newData.gfYbqyTzCkZyFwxx.fwtx = newData.fwtx;
//                                newData.gfYbqyTzCkZyFwxx.zcssbm = newData.zcssbm;
//                                newData.gfYbqyTzCkZyFwxx.zcsslx = newData.zcsslx;
//                                newData.gfYbqyTzCkZyFwxx.ckl = newData.ckl;
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
                params.put("startTime", sv_rksj.getStartTime());
                params.put("endTime", sv_rksj.getEndTime());
                params.put("fwxxmc", et_fwmc.getText().toString());
                mPresenter.getTodoList(params);
                break;

        }
    }

    private ScanCodeUtil scanCodeUtil;

    @Override
    public void showScanDialog(long delayTime) {

        scanCodeUtil = new ScanCodeUtil(SjOutputLargeScanActivity.this, isLandscape, delayTime, Constant.REQUEST_CODE);
        scanCodeUtil.scanCode();
        scanCodeUtil.getResultListener(result -> {
            KLog.e("扫码结果为：" + result);
            mPresenter.getScanInfo(result);

        });
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
        if (TextUtils.isEmpty(mData.ysjbr)) {
            return "请输入运输经办人！";
        } else if (TextUtils.isEmpty(mData.ckjbr)) {
            return "请输入出库经办人！";
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
    protected SjOutputLargeScanPresenter createPresenter() {
        return new SjOutputLargeScanPresenter();
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




}
