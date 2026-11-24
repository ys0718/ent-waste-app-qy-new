package com.grkj.ytjqy_pad.mvp.view.activity.sc;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.HtxxBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ProductDeviceBean;
import com.grkj.ytjqy_pad.mvp.model.TareListBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.model.ZyldBean;
import com.grkj.ytjqy_pad.mvp.presenter.ProductAndInputInfoPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.CheckInfoActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.MulPrintListActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.SelectWasteByZcssListActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.TareListActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.bluetooth.WeightBaseActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.printer2.NewPrintLabelActivity;
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
import com.grkj.ytjqy_pad.utils.view.AddNumberView;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2022/3/21.
 * 产废并入库信息
 */

public class ProductAndInputInfoActivity extends WeightBaseActivity<ProductAndInputInfoPresenter> implements ProductAndInputInfoPresenter.View {

    private CommonProductInfoBean mData = new CommonProductInfoBean();

    //批次号
    @BindView(R.id.et_item1)
    EditText et_item1;

    //入库时间
    @BindView(R.id.et_item2)
    EditText et_item2;

    //废物类别
    @BindView(R.id.et_item3)
    EditText et_item3;

    //废物类别
    @BindView(R.id.ll_item3)
    RelativeLayout ll_item3;

    //废物代码
    @BindView(R.id.et_item4)
    EditText et_item4;

    //废物名称
    @BindView(R.id.et_item5)
    EditText et_item5;

    //废物类别名称
    @BindView(R.id.et_item6)
    EditText et_item6;

    //废物特性
    @BindView(R.id.et_item7)
    EditText et_item7;

    //入库量
    @BindView(R.id.et_item8)
    EditText et_item8;

    //容器/包装编码
    @BindView(R.id.et_item9)
    EditText et_item9;

    //产生批次号
    @BindView(R.id.et_item10)
    EditText et_item10;

    //容器/包装类型
    @BindView(R.id.et_item11)
    EditText et_item11;

    //容器/包装数量
    @BindView(R.id.add_view)
    AddNumberView et_item12;

    //贮存设备编码
    @BindView(R.id.et_item13)
    EditText et_item13;

    //贮存设备类型
    @BindView(R.id.et_item14)
    EditText et_item14;

    //包装类型
    @BindView(R.id.et_item15)
    EditText et_item15;

    //包装规模
    @BindView(R.id.et_item16)
    EditText et_item16;

    //计量单位
    @BindView(R.id.et_item17)
    EditText et_item17;

    //运送部门经办人
    @BindView(R.id.et_item18)
    HistorySearchSpinner et_item18;

    //贮存部门经办人
    @BindView(R.id.editSpinner_19)
    SearchSpinner editSpinner_19;

    //贮存部门经办人手机
    @BindView(R.id.editSpinner_15)
    HistorySearchSpinner editSpinner_15;


    //转移联单
    @BindView(R.id.ll_item21)
    LinearLayout ll_item21;

    //来源单位
    @BindView(R.id.ll_item22)
    LinearLayout ll_item22;

    //仅处置单位展示
    @BindView(R.id.ll_czdw)
    LinearLayout ll_czdw;


    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //生成标签
    @BindView(R.id.ll_print)
    LinearLayout ll_print;

    //称重模块
    @BindView(R.id.ll_weight)
    LinearLayout ll_weight;

    //重新称重
    @BindView(R.id.ll_get_weight)
    LinearLayout ll_get_weight;


    //删除按钮
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;

    //核准按钮
    @BindView(R.id.ll_check)
    LinearLayout ll_check;

    //保存按钮
    @BindView(R.id.ll_save)
    LinearLayout ll_save;

    //复制按钮
    @BindView(R.id.ll_copy)
    LinearLayout ll_copy;

    //扫码按钮
    @BindView(R.id.ll_scan)
    LinearLayout ll_scan;

    //来源单位
    @BindView(R.id.editSpinner_22)
    SearchSpinner editSpinner_22;

    //转移联单
    @BindView(R.id.editSpinner_21)
    SearchSpinner editSpinner_21;


    //产生危险废物设施编码
    @BindView(R.id.et_item23)
    EditText et_item23;


    //产生部门经办人
    @BindView(R.id.editSpinner_14)
    SearchSpinner editSpinner_14;


    //合同编号
    @BindView(R.id.editSpinner_20)
    SearchSpinner editSpinner_20;

    //是否批量入库
    @BindView(R.id.switch_plrk)
    Switch switch_plrk;

    //是否批量入库布局
    @BindView(R.id.ll_plrk)
    LinearLayout ll_plrk;


    //批量入库数量布局
    @BindView(R.id.ll_item25)
    LinearLayout ll_item25;

    //批量入库数量
    @BindView(R.id.add_plsl)
    AddNumberView add_plsl;

    //经办人手机号
    @BindView(R.id.ll_jbrsj)
    LinearLayout ll_jbrsj;


    private boolean enableSave = true;

    private ZcssBean zcssSelectBean = null;
    private CommonProductInfoBean wasteSelectBean = null;
    private String csfwid;
    private CommonProductInfoBean.GfYbqyTzRkFwxx childListBean = null;
    private boolean isClickLabel = false;//点击生成标签

    //是否为处置单位
    private boolean isCzdw;//是否为处置单位

    //是否批量入库
    private boolean isPlrk = false;

    private boolean isGzEv;//是否为广州环境

    private Map<String, String> zcssParams = new HashMap<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_product_and_input_info;
    }

    @Override
    protected void initEventAndData() {
        String environment = String.valueOf(SPUtils.get(mContext, Constants.ENVIRONMENT, ""));
        if ("gz".equals(environment) || "dev".equals(environment)) {
            isGzEv = true;
        }
        ll_jbrsj.setVisibility(View.VISIBLE);

        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);
        isCzdw = getIntent().getBooleanExtra("isCzdw", false);
        isCopy = getIntent().getBooleanExtra(Constants.IS_COPY, false);


        initWeight();
        if (isCopy) {
            isGetWeight = true;
        }

        if (TextUtils.isEmpty(dataStr)) {//新增
            isNew = true;
            isGetWeight = true;
            mData = new CommonProductInfoBean();
            et_item2.setText(DateTimeUtil.getCurrentDate());
            editSpinner_19.setText(SPUtils.get(mContext, Constants.USER_NAME, "").toString());
            editSpinner_14.setText(SPUtils.get(mContext, Constants.USER_NAME, "").toString());
            mData.rksj = et_item2.getText().toString();
            ll_print.setVisibility(View.GONE);
            mData.jldw = getJldw();
            et_item17.setText(mData.jldw);

            editSpinner_15.setDefaultText();

            add_plsl.setMaxNum(80);
            add_plsl.setMinNum(2);
            add_plsl.setEnable(true);
            switch_plrk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isLandscape){
                        ll_item25.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                    }else {
                        ll_item25.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    }

                    isPlrk = isChecked;
                    scrollToBottom(scroll_view, View.FOCUS_DOWN);
                }
            });
            handleReadOnly(false);

        } else {//编辑

            CommonProductInfoBean parentData = GsonUtil.GsonToBean(dataStr, CommonProductInfoBean.class);
            if (isCopy) {
                mData = parentData;
                handleReadOnly(false);
            } else {
                et_item2.setEnabled(false);
                et_item2.setClickable(false);
                handleReadOnly(true);
                mPresenter.getRkInfo(parentData.id);
            }

        }

        Map<String, String> map = new HashMap<>();
        map.put("types", "废物特性,物理特性,包装方式,计量单位,处理方式及去向,包装材质,包装规模,废物形态");
        mPresenter.getParamList(map);
        mPresenter.getUserList("");
        et_item18.setKey(Constants.KEY_YSBMJBR);
        editSpinner_15.setKey(Constants.KEY_ZCJBRSJ);

        if (isCzdw) {
            ll_czdw.setVisibility(View.VISIBLE);
            mPresenter.getFromList("");
            mPresenter.getHtList("");
            mPresenter.getZyldList("");
        } else {
            ll_czdw.setVisibility(View.GONE);
        }


    }


    @Override
    public void onResume() {
        super.onResume();

        ll_plrk.setVisibility(View.VISIBLE);

//        if (isDPUser){
//            ll_plrk.setVisibility(View.VISIBLE);
//        }else{
//            ll_plrk.setVisibility(View.GONE);
//        }

    }

//    private void handleReadOnly(boolean isIntercept) {
//        et_item17.setEnabled(!isIntercept);
//        et_item17.setClickable(!isIntercept);
//        ll_clickable.setOnInterceptTouchEvent(isIntercept, getResources().getColor(R.color.gray));
//    }



    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();

    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("产生并入库");
    }

    private void initView() {

        et_item1.setText(mData.rkpch);
        et_item2.setText(mData.rksj);
        et_item3.setText(mData.fwdlbh);
        et_item6.setText(mData.fwdlmc);
        et_item4.setText(mData.fwxlbh);
        et_item5.setText(mData.fwxxmc);
        et_item7.setText(mData.fwtx);
        et_item8.setText(mData.rkl);
        et_item9.setText(mData.rqbzbm);
        et_item10.setText(mData.cspch);
        et_item11.setText(mData.rqbzlx);
        et_item12.setResult(mData.rqbzsl);
        et_item13.setText(mData.zcssbm);
        et_item14.setText(mData.zcsslx);
        et_item15.setText(commonTransfer(mData.rqbzcz, "包装材质"));
        et_item16.setText(commonTransfer(mData.rqbzgg, "包装规模"));
        et_item17.setText(mData.jldw);
        et_item18.setText(mData.ysjbr);
        editSpinner_19.setText(mData.zcjbr);
        editSpinner_15.setText(mData.zcjbrsj);
        et_item23.setText(getString(R.string.str_record_fwlb2, mData.scssbh, mData.scssmc));
        editSpinner_14.setText(mData.csjbr);

        if (!TextUtils.isEmpty(mData.typch)) {
            isPlrk = true;
            switch_plrk.setChecked(true);
        }


    }

    @BindView(R.id.scroll_view)
    ScrollView scroll_view;


    private boolean isRunning = true;

    public void scrollToBottom(ScrollView scroll_view, int position) {
        isRunning = true;
        scroll_view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isRunning) {
                    scroll_view.post(new Runnable() {
                        public void run() {
                            scroll_view.fullScroll(position);
                            isRunning = false;
                        }
                    });
                }
            }
        });
    }


    private TareListBean tareSelectBean = new TareListBean();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 9) {
            if (requestCode == 9) {//复制回调
                setResult(9);
                finish();
            } else {//选择皮重
                String selectBeanStr = data.getStringExtra("selectBean");
                tareSelectBean = GsonUtil.GsonToBean(selectBeanStr, TareListBean.class);
                tv_weight3.setText(tareSelectBean.pzl);
            }
        } else if (resultCode == 11) { //选择废物类别
            String selectBeanStr = data.getStringExtra("selectBean");
            wasteSelectBean = GsonUtil.GsonToBean(selectBeanStr, CommonProductInfoBean.class);
            initWasteInfo();
            mPresenter.getProductDeviceList(wasteSelectBean.cshjId);
        } else if (resultCode == 13 && !isWeight) { //校准
            setResult(9);
            String current_weight = data.getStringExtra("CURRENT_WEIGHT");
            et_item8.setText(current_weight);
        }

    }

    //公用废物类别赋值
    private void initBaseWasteInfo() {
        et_item3.setText(wasteSelectBean.fwdlbh);
        et_item6.setText(wasteSelectBean.fwdlmc);
        et_item4.setText(wasteSelectBean.fwxlbh);
        et_item5.setText(wasteSelectBean.fwxxmc);
        mData.fwdlbh = wasteSelectBean.fwdlbh;
        mData.fwxxmc = wasteSelectBean.fwxxmc;
        mData.fwtx = wasteSelectBean.fwtx;
        mData.wlxz = wasteSelectBean.wlxz;
        mData.fwxlbh = wasteSelectBean.fwxlbh;
        mData.fwxlmc = wasteSelectBean.fwxlmc;
        mData.mlnf = wasteSelectBean.mlnf;
        mData.fwdlmc = wasteSelectBean.fwdlmc;
        mData.zcfwxxId = wasteSelectBean.cshjId;
        mData.fwxxId = wasteSelectBean.cshjId;
        mData.rqbzbm = wasteSelectBean.rqbzbm;
        mData.rqbzcz = wasteSelectBean.rqbzcz;
        mData.rqbzgg = wasteSelectBean.rqbzgg;
        mData.qx = wasteSelectBean.qx;
        mData.wxcf = wasteSelectBean.wxcf;
        mData.rqbzsl = wasteSelectBean.rqbzsl;

        mData.remark = wasteSelectBean.remark;
        mData.zysx = wasteSelectBean.zysx;
        mData.zywrcf = wasteSelectBean.zywrcf;
        mData.bqlx = wasteSelectBean.bqlx;

        //计量单位处理
        if (!TextUtils.isEmpty(wasteSelectBean.jldw) && !isWeight) {
            mData.jldw = wasteSelectBean.jldw;
        }
        et_item17.setText(mData.jldw);

        et_item23.setText(getString(R.string.str_record_fwlb2, mData.scssbh, mData.scssmc));
        et_item11.setText(commonTransfer(wasteSelectBean.bzfs, "包装方式"));
        et_item15.setText(commonTransfer(mData.rqbzcz, "包装材质"));
        et_item16.setText(commonTransfer(mData.rqbzgg, "包装规模"));
        et_item12.setResult(!TextUtils.isEmpty(wasteSelectBean.rqbzsl) ? wasteSelectBean.rqbzsl : "1");
        groupBzbm();
    }


    //选择/清空废物类别信息
    private void initWasteInfo() {
        mData.rqbzlx = wasteSelectBean.bzfs;
//        mData.fwId = wasteSelectBean.id;
        csfwid = wasteSelectBean.id;
        initBaseWasteInfo();
        mPresenter.getCshjInfo(wasteSelectBean.cshjId);
//        mData.cspch = "";
//        et_item10.setText("");
    }


    @OnClick({R.id.et_item3, R.id.et_item6, R.id.et_item7, R.id.et_item13, R.id.et_item15, R.id.ll_delete, R.id.et_item16, R.id.et_item23,
            R.id.et_item17, R.id.ll_back, R.id.ll_save, R.id.ll_print, R.id.ll_clear, R.id.ll_qp, R.id.ll_get_cfxx, R.id.ll_copy, R.id.ll_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //废物类别
            case R.id.et_item3:
                if (TextUtils.isEmpty(mData.zcssbm)) {
                    ToastUtil.showToast(mContext, "请先选择贮存设备编码！");
                } else {
                    goToActivity(new Intent(mContext, SelectWasteByZcssListActivity.class)
                                    .putExtra("zcssId", zcssSelectBean.id)
                                    .putExtra("title", "选择产废信息")
                                    .putExtra("url", ApiUrl.URL_WASTE_TYPE_LIST_BY_ID)
                            , 11);
                }
                break;


            //产生设施
            case R.id.et_item23:
                if (TextUtils.isEmpty(mData.fwxxmc)) {
                    ToastUtil.showToast(mContext, "请选择废物类别！");
                } else {
                    showCsssPopWindow();
                }
                break;

            //计量单位
            case R.id.et_item17:
                showPopWindow("计量单位", et_item17, false);
                break;

            //废物特性
            case R.id.et_item7:
                showPopWindow("废物特性", et_item7, false);
                break;


            //包装材质
            case R.id.et_item15:
                showPopWindow("包装材质", et_item15, false);
                break;

            //包装规模
            case R.id.et_item16:
                showPopWindow("包装规模", et_item16, false);
                break;

            //贮存设备编号
            case R.id.et_item13:
                showZcssPopWindow();
                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;


            // 生成标签
            case R.id.ll_print:

                if (TextUtils.isEmpty(mData.typch)) {//单个标签处理
                    isClickLabel = true;
                    mPresenter.getRkInfo(mData.id);
                } else {//有同一批次号处理
                    mPresenter.checkPrintable(mData.typch);
                }
                break;

            // 清零
            case R.id.ll_clear:
                if (usbService != null) {
                    tv_weight3.setText("0.000");
                    isClear = true;
                    usbService.write(orderZero());
                }
                break;

            //去皮
            case R.id.ll_qp:


                goToActivity(new Intent(mContext, TareListActivity.class)
                                .putExtra("allParamList", GsonUtil.GsonToString(allParamList))
                        , 10);

                break;

            // 保存
            case R.id.ll_save:
                onSaveClick();
                break;

            //复制
            case R.id.ll_copy:
                //赋空处理
                String mDataStr = GsonUtil.GsonToString(mData);
                CommonProductInfoBean copyData = GsonUtil.GsonToBean(mDataStr, CommonProductInfoBean.class);
                copyData.id = "";
                copyData.isck = "";
                copyData.rkpch = "";
                copyData.rkl = "";
                copyData.cspch = "";
                copyData.qrcodeValue = "";
                copyData.rksj = DateTimeUtil.getCurrentDate();
//                copyData.gfYbqyTzRkFwxx = new CommonProductInfoBean.GfYbqyTzRkFwxx();
                copyData.gfYbqyTzRkFwxx = null;
                copyData.bqlx = getBqlxFromDevice();

                String copyStr = GsonUtil.GsonToString(copyData);
                goToActivity(new Intent(mContext, ProductAndInputInfoActivity.class)
                        .putExtra(Constants.IS_COPY, true)
                        .putExtra("isWeight", isWeight)
                        .putExtra(Constants.JUMP_KEY, copyStr), 9);

                break;

            // 删除
            case R.id.ll_delete:
                mPresenter.showDeleteDialog(mData.id);
                break;


            //校准
            case R.id.ll_check:
                goToCheck();
                break;

        }
    }

    private void onSaveClick() {
        if (!enableSave) {
            ToastUtil.showToast(mContext, "数据正在保存，请勿频繁操作");
            return;
        }

        et_item18.clearFocus();
        mData.rksj = et_item2.getText().toString();
        mData.rkl = et_item8.getText().toString();
        mData.rqbzbm = et_item9.getText().toString();
//        mData.cspch = et_item10.getText().toString();
        mData.rqbzsl = et_item12.getResultString();
        mData.csjbr = editSpinner_14.getText().toString();
        mData.jldw = et_item17.getText().toString();
        mData.ysjbr = et_item18.getText();
        mData.zcjbr = editSpinner_19.getText().toString();
        mData.zcjbrsj = editSpinner_15.getText().toString();
        mData.lrfs = isWeight ? "1" : "";
        mData.gfYbqyTzRkFwxx = null;

        editSpinner_15.clearFocus();
        String deviceCode = SPUtils.get(mContext, "DEVICE_CODE", "").toString();
        mData.deviceCode = deviceCode;

        if (isNew) {
            mData.bqlx = getBqlxFromDevice();
        }


        if (TextUtils.isEmpty(mData.dataFrom)) {//如果数据来源为空，则根据横竖屏设置
            mData.dataFrom = isLandscape ? Constants.TYPE_YTJ : Constants.TYPE_SCZD;
        }

        if (TextUtils.isEmpty(mData.versionFrom)) {//如果版本来源为空，则获取版本信息
            mData.versionFrom = AppUtils.getVersionName(mContext);
        }

        if (TextUtils.isEmpty(checkEmpty())) {
            showSaveDialog();
        } else {
            ToastUtil.showToast(mContext, checkEmpty());
        }
    }


    /**
     * 校正提示弹窗
     */
    private void showCheckDialog() {

        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("该记录的废物易挥发，是否进行校准？");
        roundCornerDialog.setConfirmText("去校准");
        //设置弹窗类型
        roundCornerDialog.setDialogType(RoundCornerDialog.TYPE_SAVE);
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                roundCornerDialog.dismiss();
                goToCheck();

            }

            @Override
            public void onCancelClick() {

            }
        });
        //显示对话框
        roundCornerDialog.show();
    }

    //跳转至校准
    private void goToCheck() {
        goToActivity(new Intent(mContext, CheckInfoActivity.class)
                .putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(mData))
                .putExtra("IS_CK", false)
                .putExtra("IS_WEIGHT", isWeight)
                .putExtra("CURRENT_WEIGHT", et_item8.getText().toString()), 13);
    }


    /*
     * 判空处理
     * */
    private String checkEmpty() {

        if (TextUtils.isEmpty(mData.rkl)) {
            return "请输入/地磅获取入库量！";
        } else if (Double.valueOf(mData.rkl) <= 0) {
            return "请输入/地磅获取正确的入库量！";
        } else if (TextUtils.isEmpty(mData.zcssbm)) {
            return "请选择贮存设施编码！";
        } else if (TextUtils.isEmpty(mData.fwdlbh)) {
            return "请选择废物类别！";
        } else if (TextUtils.isEmpty(mData.rqbzcz)) {
            return "请选择容器/包装材质！";
        } else if (TextUtils.isEmpty(mData.rqbzgg)) {
            return "请选择容器/包装规格！";
        } else if (TextUtils.isEmpty(mData.rqbzsl) || Double.valueOf(mData.rqbzsl) <= 0) {
            return "请输入容器/包装数量！";
        } else if (TextUtils.isEmpty(mData.ysjbr)) {
            return "请输入运送部门经办人！";
        } else if (TextUtils.isEmpty(mData.zcjbr)) {
            return "请输入贮存部门经办人！";
        } else if (TextUtils.isEmpty(mData.zcjbrsj) && isGzEv) {
            return "请输入贮存部门经办人手机！";
        } else if (isCzdw && TextUtils.isEmpty(editSpinner_20.getText())) {
            return "请输入/选择合同编号！";
        } else if (isCzdw && TextUtils.isEmpty(editSpinner_21.getText())) {
            return "请输入/选择转移联单！";
        } else if (isCzdw && TextUtils.isEmpty(editSpinner_22.getText())) {
            return "请输入/选择来源单位！";
        } else {
            return "";
        }

    }

    private void showSaveDialog() {
        Double num = add_plsl.getResult();
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage(num > 1 && isNew && isPlrk ? "是否确定保存" + num.intValue() + "条相同重量数据？" : "是否确定保存？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                enableSave = false;
//                mData.gfYbqyTzCkZyFwxx = null;
                CommonProductInfoBean temData = mData;
                //qrcodeUrl qrcodeValue新增/编辑都赋空
                temData.qrcodeUrl = null;
                temData.qrcodeValue = null;
                Double dbnum = add_plsl.getResult();

                Map<String, String> map = GsonUtil.GsonToMaps(GsonUtil.GsonToString(temData));

                //新增时判断是否开启批量入库
                if (isNew && isPlrk) {
                    map.put("num", String.valueOf(dbnum.intValue()));
                }

                mPresenter.saveData(map, ApiUrl.getHnUrl(ApiUrl.URL_PRODUCT_AND_INPUT_SAVE));
                roundCornerDialog.dismiss();
            }

            @Override
            public void onCancelClick() {

            }
        });
        //显示对话框
        roundCornerDialog.show();
    }


    @Override
    protected ProductAndInputInfoPresenter createPresenter() {
        return new ProductAndInputInfoPresenter();
    }


    @Override
    public void showError(String msg) {
        enableSave = true;
        ToastUtil.showToast(mContext, msg);
    }

    private boolean isShowTypeList = false;

    @Override
    public void getTypeListSuccess(List<CommonProductInfoBean> list) {
        String json = GsonUtil.GsonToString(list);
        wasteTypeList = GsonUtil.JsonToList(json, CommonProductInfoBean.class);
    }

    @Override
    public void getZcssListSuccess(List<ZcssBean> list) {
        String json = GsonUtil.GsonToString(list);
        zcssList = GsonUtil.JsonToList(json, ZcssBean.class);

        if (!TextUtils.isEmpty(mData.zcssbm)) {
            for (int i = 0; i < zcssList.size(); i++) {
                if (mData.zcssbm.equals(zcssList.get(i).no)) {
                    zcssSelectBean = zcssList.get(i);
                }
            }
        } else {
            if (zcssList != null && zcssList.size() != 0) {
                zcssSelectBean = zcssList.get(0);
            }
        }

        if (zcssSelectBean != null) {
            mData.zcssbm = zcssSelectBean.no;
            mData.zcsslx = zcssSelectBean.sslx;
            et_item13.setText(zcssSelectBean.no);
            et_item14.setText(zcssSelectBean.sslx + zcssSelectBean.ssmc);
            mPresenter.getTypeList(zcssSelectBean.id);
        }

    }


    private List<CommonProductInfoBean> wasteTypeList = new ArrayList<>();
    private List<ZcssBean> zcssList = new ArrayList<>();


    @Override
    public void getParamListSuccess(List<ParamListBean> list) {
        String json = GsonUtil.GsonToString(list);
        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);
        if (!isNew) {
            initView();
        }

        mPresenter.getZcssList(zcssParams);

//        transferWltx(mData.wlxz);
//        transferBzfs(mData.rqbzlx);
    }

    @Override
    public void saveDataSuccess(Object bean) {
        enableSave = true;
        setResult(9);
        ToastUtil.showToast(mContext, "保存成功！");
        if(isPlrk){
            finish();
            return;
        }
        mData = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), CommonProductInfoBean.class);
        SPUtils.put(mContext, Constants.LAST_ZCSS, GsonUtil.GsonToString(zcssSelectBean));
        setJldw(mData.jldw);
        handleReadOnly(true);
        scrollToBottom(scroll_view, View.FOCUS_UP);

        et_item1.setText(mData.rkpch);
        ll_print.setVisibility(View.VISIBLE);
        ll_delete.setVisibility(View.VISIBLE);
        ll_copy.setVisibility(View.VISIBLE);
//        ll_save.setVisibility(View.GONE);
//        ll_check.setVisibility(View.GONE);


//        if (!TextUtils.isEmpty(mData.hftx) && (mData.hftx.equals("1"))) {
//            ll_check.setVisibility(View.VISIBLE);
//            showCheckDialog();
//        }

    }


    @Override
    public void getCshjInfoSuccess(Object bean) {

        CommonProductInfoBean CommonProductInfoBean = GsonUtil.GsonToBean(bean, CommonProductInfoBean.class);
        mData.wxcf = CommonProductInfoBean.wxcf;
        mData.remark = CommonProductInfoBean.remark;
        mData.zysx = CommonProductInfoBean.zysx;
        mData.wxtx = CommonProductInfoBean.wxtx;
        mData.zywrcf = CommonProductInfoBean.zywrcf;
        mData.hftx = CommonProductInfoBean.hftx;
        mData.fwtx = CommonProductInfoBean.fwtx;
        mData.fwId = CommonProductInfoBean.id;

        mData.wlxz = CommonProductInfoBean.wlxz;
        mData.rqbzlx = CommonProductInfoBean.bzfs;
        mData.rqbzcz = CommonProductInfoBean.rqbzcz;
        mData.rqbzgg = CommonProductInfoBean.rqbzgg;


        et_item15.setText(commonTransfer(mData.rqbzcz, "包装材质"));
        et_item16.setText(commonTransfer(mData.rqbzgg, "包装规模"));
        groupBzbm();

    }

    @Override
    public void getRkInfoSuccess(Object bean) {
        mData = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), CommonProductInfoBean.class);

        if (isClickLabel) {
            isClickLabel = false;
            mPresenter.getLabelInfo(mData.qrcodeValue);
        } else {
            initButton();
            mPresenter.getProductDeviceList(mData.fwxxId);
        }

    }

    private void initButton() {
        if ("1".equals(mData.isck)) {//已出库时不可保存、删除
            ll_save.setVisibility(View.GONE);
            ll_delete.setVisibility(View.GONE);
            ll_print.setVisibility(View.VISIBLE);
            ll_copy.setVisibility(View.VISIBLE);
        } else {
            ll_save.setVisibility(View.VISIBLE);
//            if (isCopy) {//复制逻辑
//                ll_copy.setVisibility(View.GONE);
//                ll_print.setVisibility(View.GONE);
//                ll_delete.setVisibility(View.GONE);
//
//            } else {
////                isWeight = true;
//                ll_copy.setVisibility(View.VISIBLE);
//                ll_print.setVisibility(View.VISIBLE);
//                ll_delete.setVisibility(View.VISIBLE);
//            }

            ll_copy.setVisibility(View.VISIBLE);
            ll_print.setVisibility(View.VISIBLE);
            ll_delete.setVisibility(View.VISIBLE);

            hideSaveButtonByEv(ll_save);
        }
    }


    @Override
    public void getLabelInfoSuccess(Object bean) {
        LabelInfoBean data = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), LabelInfoBean.class);
        data.WLXZ = commonTransfer(data.WLXZ, "物理特性");

//        if (isLandscape) {
//            //新旧打印机跳转
//            if (Constants.IS_NEW_PRINTER) {
//                goToActivity(new Intent(mContext, BtPrintLabelActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(data)), 10);
////            goToActivity(new Intent(mContext, PrintLabelActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(data)), 10);
//            } else {
////            goToActivity(new Intent(mContext, PrintLabelActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(data)), 10);
//                goToActivity(new Intent(mContext, OldPrintLabelActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(data)), 10);
//            }
//        } else {
//            goToActivity(new Intent(mContext, BtPrintLabelActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(data)), 10);
//        }

//        goToActivity(new Intent(mContext,BtPrintLabelActivity.class).putExtra(Constants.JUMP_KEY,GsonUtil.GsonToString(data)),10);
        goToActivity(new Intent(mContext, NewPrintLabelActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(data)), 10);

    }

    @Override
    public void getLabelInfoListSuccess(List<LabelInfoBean> list) {

    }

    @Override
    public void deleteDataSuccess(String bean) {
        ToastUtil.showToast(mContext, "删除成功！");
        setResult(9);
        finish();
    }

    private List<UserBean> temFromList = new ArrayList<>();

    @Override
    public void getFromListSuccess(List<UserBean> list) {
        temFromList = list;
        List<Select> selectList = new ArrayList<>();
        for (int i = 0; i < temFromList.size(); i++) {
            Select bean = new Select(temFromList.get(i).entername, temFromList.get(i).id);
            selectList.add(bean);
        }
        editSpinner_22.setDisabled(false);
        editSpinner_22.setItemData(selectList);
        if (!TextUtils.isEmpty(mData.lydwmc)) {
            editSpinner_22.setText(mData.lydwmc);
        }
        editSpinner_22.setOnInsertListener(new SearchSpinner.onInsertListener() {
            @Override
            public void insertChange(String name, String value, boolean isFromSelect) {
                mData.lydwmc = name;
                mData.lydwid = value;
                KLog.e("editSpinner", name);
                if (!isFromSelect) {//如果是输入或者是为空的时候筛选
                    mPresenter.getFromList(name);
                }
            }
        });

    }

    private List<ZyldBean> temZyldList = new ArrayList<>();

    @Override
    public void getZyldListSuccess(List<ZyldBean> list) {
        temZyldList.clear();
        temZyldList = list;

        List<Select> selectList = new ArrayList<>();
        for (int i = 0; i < temZyldList.size(); i++) {
            Select bean = new Select(temZyldList.get(i).number1, temZyldList.get(i).id);
            selectList.add(bean);
        }
        editSpinner_21.setDisabled(false);
        editSpinner_21.setItemData(selectList);
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
                        mData.lydwmc = temZyldList.get(i).jsdwmc;
                        editSpinner_22.setText(mData.lydwmc);
                    }
                }

                if (!isFromSelect) {
                    mPresenter.getZyldList(name);
                }
            }
        });

    }

    private List<HtxxBean> temHtxxList = new ArrayList<>();

    @Override
    public void getHtListSuccess(List<HtxxBean> list) {
        temHtxxList.clear();
        temHtxxList = list;

        List<Select> selectList = new ArrayList<>();
        for (int i = 0; i < temHtxxList.size(); i++) {
            Select bean = new Select(temHtxxList.get(i).htbh, temHtxxList.get(i).id);
            selectList.add(bean);
        }
        editSpinner_20.setDisabled(false);
        editSpinner_20.setItemData(selectList);
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

    private List<UserBean> temUserList = new ArrayList<>();
    private List<UserBean> userList = new ArrayList<>();

    @Override
    public void getUserListSuccess(List<UserBean> list) {
        String json = GsonUtil.GsonToString(list);
        userList = GsonUtil.JsonToList(json, UserBean.class);
        temUserList = userList;

        if (temUserList.size() == 0) {
            editSpinner_14.setHint("请输入");
            editSpinner_14.setText(SPUtils.get(mContext, Constants.USER_NAME, "").toString());
            editSpinner_19.setHint("请输入");
            editSpinner_19.setText(SPUtils.get(mContext, Constants.USER_NAME, "").toString());
            return;
        }

        List<Select> selectList = new ArrayList<>();
        for (int i = 0; i < temUserList.size(); i++) {
            Select bean = new Select(temUserList.get(i).name, temUserList.get(i).name);
            selectList.add(bean);
        }
        editSpinner_14.setItemData(selectList);
        editSpinner_14.setDisabled(false);
        editSpinner_19.setItemData(selectList);
        editSpinner_19.setDisabled(false);
        if (!TextUtils.isEmpty(mData.zcjbr)) {
            editSpinner_19.setText(mData.zcjbr);
        }
        if (!TextUtils.isEmpty(mData.csjbr)) {
            editSpinner_14.setText(mData.csjbr);
        }
        editSpinner_19.setOnInsertListener(new SearchSpinner.onInsertListener() {
            @Override
            public void insertChange(String name, String value, boolean isFromSelect) {
                mData.zcjbr = name;
            }
        });
        editSpinner_14.setOnInsertListener(new SearchSpinner.onInsertListener() {
            @Override
            public void insertChange(String name, String value, boolean isFromSelect) {
                mData.csjbr = name;
            }
        });

    }


    private List<ProductDeviceBean> productDeviceList = new ArrayList<>();

    @Override
    public void getProductDeviceListSuccess(List<ProductDeviceBean> list) {
        String json = GsonUtil.GsonToString(list);
        productDeviceList = GsonUtil.JsonToList(json, ProductDeviceBean.class);

        if (isNew) {
            if (productDeviceList != null && productDeviceList.size() != 0) {
                mData.scssbh = productDeviceList.get(0).scssbh;
                mData.scsslx = productDeviceList.get(0).scsslx;
                mData.scssid = productDeviceList.get(0).scssid;
                mData.scssmc = productDeviceList.get(0).scssmc;
                et_item23.setText(getString(R.string.str_record_fwlb2, mData.scssbh, mData.scssmc));
            } else {
                mData.scssbh = "";
                mData.scssid = "";
                mData.scssmc = "";
                mData.scsslx = "";
            }
        }

    }

    @Override
    public void checkPrintableSuccess(String result) {
        goToActivity(new Intent(mContext, MulPrintListActivity.class).putExtra("TYPCH", mData.typch).putExtra("RKPCH", mData.rkpch), 19);
    }


    private SelectPopupWindow selectPopupWindow;

    //初始化下拉弹窗
    private void showPopWindow(String type, EditText item, boolean isMulti) {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < allParamList.size(); i++) {
            List<ParamListBean> childList = allParamList.get(i).childList;

            if (childList != null && childList.size() != 0 && type.equals(allParamList.get(i).type)) {
                for (int j = 0; j < childList.size(); j++) {
                    selectPopupWindowList.add(new SelectPopupWindowBean(childList.get(j).code, childList.get(j).value));
                }
            }
        }

        if (isMulti) {
            selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnCheckClickItemListener() {
                @Override
                public void onDo(String mType, List<SelectPopupWindowBean> datas) {
                    String result = "";
                    for (int i = 0; i < datas.size(); i++) {
                        result += datas.get(i).NAME + ",";
                    }
                    item.setText(result);
                }
            });
        } else {
            selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
                @Override
                public void onDo(SelectPopupWindowBean model, String mType) {
                    item.setText(model.NAME);
                    //废物特性,物理特性,包装方式,计量单位,处理方式及去向,包装材质,包装规模
                    switch (mType) {
                        case "包装材质":
                            mData.rqbzcz = model.ID;
                            groupBzbm();
                            break;
                        case "包装规模":
                            mData.rqbzgg = model.ID;
                            groupBzbm();
                            break;
                        case "计量单位":
                            mData.jldw = model.ID;
                            changeUnit(mData.jldw);
                            break;
                    }
                    selectPopupWindow.dismiss();
                }
            });
        }

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }

        selectPopupWindow.show(mLayout, type, (View) item.getParent(), sort);

    }

    /*
     * 拼接包装编码
     * */
    private void groupBzbm() {

        if (!TextUtils.isEmpty(mData.rqbzlx) && !TextUtils.isEmpty(mData.rqbzcz) && !TextUtils.isEmpty(mData.rqbzgg)) {
            mData.rqbzbm = mData.rqbzgg + mData.rqbzcz + mData.rqbzlx;
            et_item9.setText(mData.rqbzbm);
        }

    }


    //初始贮存设施下拉弹窗
    private void showZcssPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < zcssList.size(); i++) {
            ZcssBean mData = zcssList.get(i);
            String name = getString(R.string.str_input_zcbm, mData.no, mData.sslx, mData.ssmc);
            selectPopupWindowList.add(new SelectPopupWindowBean(zcssList.get(i).id, name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                for (int i = 0; i < zcssList.size(); i++) {
                    if (zcssList.get(i).id.equals(model.ID)) {
                        zcssSelectBean = zcssList.get(i);
                    }
                }
                et_item13.setText(zcssSelectBean.no);
                et_item14.setText(zcssSelectBean.sslx + zcssSelectBean.ssmc);

                wasteSelectBean = new CommonProductInfoBean();
                mPresenter.getTypeList(zcssSelectBean.id);
                mData.zcssbm = zcssSelectBean.no;
                mData.zcsslx = zcssSelectBean.sslx;

                //置空废物信息
                initWasteInfo();
                et_item9.setText("");
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item13.getParent(), sort);
    }


    //初始产生设施下拉弹窗
    private void showCsssPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < productDeviceList.size(); i++) {
            ProductDeviceBean mData = productDeviceList.get(i);
            String name = getString(R.string.str_record_fwlb2, mData.scssbh, mData.scssmc);
            selectPopupWindowList.add(new SelectPopupWindowBean(productDeviceList.get(i).scssid, name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                ProductDeviceBean bean = new ProductDeviceBean();
                for (int i = 0; i < productDeviceList.size(); i++) {
                    if (productDeviceList.get(i).scssid.equals(model.ID)) {
                        bean = productDeviceList.get(i);
                    }
                }
                mData.scssbh = bean.scssbh;
                mData.scssid = bean.scssid;
                mData.scssmc = bean.scssmc;
                mData.scsslx = bean.scsslx;
                et_item23.setText(getString(R.string.str_record_fwlb2, mData.scssbh, mData.scssmc));
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item23.getParent(), sort);
    }


}
