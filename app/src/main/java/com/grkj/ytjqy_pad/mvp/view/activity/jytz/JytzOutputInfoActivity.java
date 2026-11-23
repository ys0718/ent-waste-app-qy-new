package com.grkj.ytjqy_pad.mvp.view.activity.jytz;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.HtxxBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.RfidCardInfoBean;
import com.grkj.ytjqy_pad.mvp.model.TareListBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.model.ZyldBean;
import com.grkj.ytjqy_pad.mvp.presenter.JytzOutputInfoPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.CheckInfoActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.ProductWwlyInfoActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.ProductZxlyInfoActivity;
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
import com.grkj.ytjqy_pad.utils.scancode.ScanCodeUtil;
import com.grkj.ytjqy_pad.utils.scancode.utils.Constant;
import com.grkj.ytjqy_pad.utils.view.AddNumberView;
import com.grkj.ytjqy_pad.utils.view.MyLinearLayout;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;
import com.grkj.ytjqy_pad.utils.view.ScanDialog;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2026/6/24.
 * 经营台账-出库信息
 */

public class JytzOutputInfoActivity extends WeightBaseActivity<JytzOutputInfoPresenter> implements JytzOutputInfoPresenter.View {

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

    //废物类别名称
    @BindView(R.id.et_item4)
    EditText et_item4;

    //去向
    @BindView(R.id.et_item5)
    EditText et_item5;

    //废物代码
    @BindView(R.id.et_item6)
    EditText et_item6;

    //废物名称
    @BindView(R.id.et_item7)
    EditText et_item7;

    //容器/包装编码
    @BindView(R.id.et_item9)
    EditText et_item9;

    //联单编号
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

    //出库部门经办人
    @BindView(R.id.editSpinner_19)
    SearchSpinner editSpinner_19;

    //合同编号
    @BindView(R.id.editSpinner_20)
    SearchSpinner editSpinner_20;

    //转移联单编号
    @BindView(R.id.editSpinner_21)
    SearchSpinner editSpinner_21;


    //司机名称
    @BindView(R.id.et_item24)
    EditText et_item24;

    //司机联系方式
    @BindView(R.id.et_item25)
    EditText et_item25;


    //获取Rfid司机信息
    @BindView(R.id.ll_get_card_info)
    LinearLayout ll_get_card_info;

    //司机信息
    @BindView(R.id.ll_driver_info)
    LinearLayout ll_driver_info;

    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //合同编号
    @BindView(R.id.ll_item20)
    LinearLayout ll_item20;

    //保存按钮
    @BindView(R.id.ll_save)
    LinearLayout ll_save;

    //自行/委外
    @BindView(R.id.ll_handle)
    LinearLayout ll_handle;

    //删除按钮
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;


    //自行/委外文字
    @BindView(R.id.tv_handle)
    TextView tv_handle;

    //生成标签
    @BindView(R.id.ll_print)
    LinearLayout ll_print;

    //重新称重
    @BindView(R.id.ll_get_weight)
    LinearLayout ll_get_weight;

    //转移联单编、接收单位名称布局
    @BindView(R.id.ll_item20_21)
    LinearLayout ll_item20_21;

    //扫码按钮
    @BindView(R.id.ll_scan)
    LinearLayout ll_scan;

    //修改时不可编辑布局
    @BindView(R.id.ll_clickable)
    MyLinearLayout ll_clickable;


    private boolean isCzdw;//是否为处置单位
    private boolean isZx;//是否为自行利用处置
    private String typeName = "自行利用";
    private String typeValue = "R";
    private String childId = "";//子表主键

    @Override
    protected int getLayout() {
        return R.layout.activity_jytz_output_info;
    }

    @Override
    protected void initEventAndData() {

        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);
        isCzdw = getIntent().getBooleanExtra("isCzdw", false);
        initWeight();


//        if (isWeight) {//称重
//            ll_weight.setVisibility(View.VISIBLE);
//            ll_scan.setVisibility(View.VISIBLE);
//        } else {
//            ll_weight.setVisibility(View.GONE);
//            ll_scan.setVisibility(View.GONE);
//        }

        if (TextUtils.isEmpty(dataStr)) {//新增
            isNew = true;
            isGetWeight = true;
            mData = new CommonProductInfoBean();
            et_item1.setHint(getString(R.string.str_auto_get));
            et_item2.setText(DateTimeUtil.getCurrentDate());
            mData.jldw = getJldw();
            et_item17.setText(mData.jldw);
//            mData.jldw = "吨";
//            if (!isWeight) {
//                mData.jldw = getJldw();
//            }
//            et_item17.setText(mData.jldw);

            editSpinner_19.setText(SPUtils.get(mContext, Constants.USER_NAME, "").toString());
            mData.cksj = et_item2.getText().toString();
            handleReadOnly(false);

        } else {//编辑
            mData = GsonUtil.GsonToBean(dataStr, CommonProductInfoBean.class);
            handleReadOnly(true);
            ll_save.setVisibility(View.GONE);
            if ("1".equals(mData.isly)) {//已被处理时不可保存、删除
                ll_delete.setVisibility(View.GONE);
                ll_handle.setVisibility(View.GONE);
            } else {
//                isWeight = true;
                ll_delete.setVisibility(View.VISIBLE);
                ll_handle.setVisibility(View.VISIBLE);
            }

            et_item8.setText(mData.ckl);
            ll_print.setVisibility(View.VISIBLE);

            hideSaveButtonByEv(ll_save);

        }

        if (isWeight) {
            ll_scan.setVisibility(View.VISIBLE);
        } else {
            ll_scan.setVisibility(View.GONE);
        }


        et_item18.setKey(Constants.KEY_CK);

        mPresenter.getZcssList(true);
        Map<String, String> map = new HashMap<>();
        map.put("types", "废物特性,物理特性,包装方式,计量单位,处理方式及去向,包装材质,包装规模,经营出库去向");
        mPresenter.getParamList(map);
        mPresenter.getUserList("");

        if (isCzdw) {
            ll_item20.setVisibility(View.VISIBLE);
        } else {
            ll_item20.setVisibility(View.GONE);
        }


    }

    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("出库信息");
    }

    private void initView() {
//        showHideByQx();
        et_item1.setText(mData.ckpch);
        et_item2.setText(mData.cksj);
        et_item3.setText(mData.fwdlbh);
        et_item4.setText(mData.fwdlmc);
        et_item5.setText(commonTransfer(mData.qx, "经营出库去向"));
        et_item6.setText(mData.fwxlbh);
        et_item7.setText(mData.fwxxmc);
        et_item9.setText(mData.rqbzbm);
        et_item10.setText(mData.zyldNumber1);
        et_item11.setText(commonTransfer(mData.rqbzlx, "包装方式"));
        et_item12.setResult(mData.rqbzsl);
        et_item13.setText(mData.zcssbm);
        et_item14.setText(mData.zcsslx);
        et_item15.setText(commonTransfer(mData.rqbzcz, "包装材质"));
        et_item16.setText(commonTransfer(mData.rqbzgg, "包装规模"));
        et_item17.setText(mData.jldw);
        et_item18.setText(mData.ysjbr);
        tv_handle.setText(commonTransfer(mData.qx, "经营出库去向"));
        if (!TextUtils.isEmpty(mData.ckjbr)) {
            editSpinner_19.setText(mData.ckjbr);
        }
        editSpinner_21.setText(mData.zyldbh);
        et_item24.setText(mData.sjmc);
        et_item25.setText(mData.sjdh);

    }

    private TareListBean tareSelectBean = new TareListBean();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 9) {
            if (requestCode == 11) {//跳转自行/委外利用处置回调
                ll_save.setVisibility(View.GONE);
                ll_handle.setVisibility(View.GONE);
                ll_delete.setVisibility(View.GONE);
            } else {//选择皮重
                String selectBeanStr = data.getStringExtra("selectBean");
                isGetWeight = true;
                tareSelectBean = GsonUtil.GsonToBean(selectBeanStr, TareListBean.class);
                tv_weight3.setText(tareSelectBean.pzl);
//                wasteSelectBean = new CommonProductInfoBean();
//                initWasteInfo();
            }
        } else if (resultCode == 11) { //选择废物类别
            String selectBeanStr = data.getStringExtra("selectBean");
            wasteSelectBean = GsonUtil.GsonToBean(selectBeanStr, CommonProductInfoBean.class);
            initWasteInfo();
        } else if (resultCode == 13 && !isWeight) { //校准
            setResult(9);
            String current_weight = data.getStringExtra("CURRENT_WEIGHT");
            et_item8.setText(current_weight);
        }else if (data != null && scanCodeUtil!=null && requestCode == scanCodeUtil.scanRequestCode) { // TODO: 2023/11/20
            String scanCode = data.getStringExtra("ScanCodeResult");
            mPresenter.getScanInfo(scanCode);
        }
    }


    private void initWasteInfo() {
        et_item3.setText(wasteSelectBean.fwdlbh);
        et_item4.setText(wasteSelectBean.fwdlmc);
        et_item6.setText(wasteSelectBean.fwxlbh);
        et_item7.setText(wasteSelectBean.fwxxmc);

        mData.wxfwbqewm = wasteSelectBean.wxfwbqewm;
        mData.qrcodeUrl = wasteSelectBean.qrcodeUrl;
        mData.fwdlbh = wasteSelectBean.fwdlbh;
        mData.fwxxmc = wasteSelectBean.fwxxmc;
        mData.fwtx = wasteSelectBean.fwtx;
        mData.hftx = wasteSelectBean.hftx;
        mData.wlxz = wasteSelectBean.wlxz;
        mData.rqbzlx = wasteSelectBean.rqbzlx;
        mData.fwxlbh = wasteSelectBean.fwxlbh;
        mData.fwxlmc = wasteSelectBean.fwxlmc;
        mData.mlnf = wasteSelectBean.mlnf;
        mData.wxcf = wasteSelectBean.wxcf;
        mData.fwdlmc = wasteSelectBean.fwdlmc;
        mData.fwId = wasteSelectBean.id;
        mData.rqbzbm = wasteSelectBean.rqbzbm;
        mData.rqbzcz = wasteSelectBean.rqbzcz;
        mData.rqbzgg = wasteSelectBean.rqbzgg;
        mData.zysx = wasteSelectBean.zysx;
        mData.remark = wasteSelectBean.remark;
        mData.rkQrcodeValue = wasteSelectBean.qrcodeValue;
        mData.qrcodeValue = wasteSelectBean.qrcodeValue;
        mData.zywrcf = wasteSelectBean.zywrcf;
        mData.bqlx = wasteSelectBean.bqlx;
        mData.rkId = wasteSelectBean.id;
        mData.zyldNumber1 = wasteSelectBean.zyldNumber1;
        mData.csdwmc = wasteSelectBean.csdwmc;
        mData.fwxt = wasteSelectBean.fwxt;
        mData.rkpch = wasteSelectBean.rkpch;
        mData.zcssid = wasteSelectBean.zcssid;


        //计量单位处理
        if (!TextUtils.isEmpty(wasteSelectBean.jldw) && !isWeight) {
            mData.jldw = wasteSelectBean.jldw;
            et_item17.setText(mData.jldw);
        }

//        mData.gfYbqyTzCkZyFwxx = new CommonProductInfoBean.GfYbqyTzCkZyFwxx();
//        mData.gfYbqyTzCkZyFwxx.rkid = wasteSelectBean.id;
//        mData.gfYbqyTzCkZyFwxx.id = childId;
//        mData.gfYbqyTzCkZyFwxx.mlnf = wasteSelectBean.mlnf;
//        mData.gfYbqyTzCkZyFwxx.fwdlbh = wasteSelectBean.fwdlbh;
//        mData.gfYbqyTzCkZyFwxx.fwdlmc = wasteSelectBean.fwdlmc;
//        mData.gfYbqyTzCkZyFwxx.fwxlbh = wasteSelectBean.fwxlbh;
//        mData.gfYbqyTzCkZyFwxx.fwxlmc = wasteSelectBean.fwxlmc;
//        mData.gfYbqyTzCkZyFwxx.fwxxmc = wasteSelectBean.fwxxmc;
//        mData.gfYbqyTzCkZyFwxx.rkpch = wasteSelectBean.rkpch;
//        mData.gfYbqyTzCkZyFwxx.jldw = wasteSelectBean.jldw;
//        mData.gfYbqyTzCkZyFwxx.rqbzbm = wasteSelectBean.rqbzbm;
//        mData.gfYbqyTzCkZyFwxx.rqbzlx = wasteSelectBean.rqbzlx;
//        mData.gfYbqyTzCkZyFwxx.rqbzsl = wasteSelectBean.rqbzsl;
//        mData.gfYbqyTzCkZyFwxx.rqbzgg = wasteSelectBean.rqbzgg;
//        mData.gfYbqyTzCkZyFwxx.rqbzcz = wasteSelectBean.rqbzcz;
//        mData.gfYbqyTzCkZyFwxx.wlxz = wasteSelectBean.wlxz;
//        mData.gfYbqyTzCkZyFwxx.fwtx = wasteSelectBean.fwtx;
//        mData.gfYbqyTzCkZyFwxx.zcssbm = wasteSelectBean.zcssbm;
//        mData.gfYbqyTzCkZyFwxx.zcsslx = wasteSelectBean.zcsslx;
//        mData.gfYbqyTzCkZyFwxx.ckl = wasteSelectBean.rkl;
//        mData.gfYbqyTzCkZyFwxx.rqbzsl = wasteSelectBean.rqbzsl;

        et_item12.setResult(!TextUtils.isEmpty(wasteSelectBean.rqbzsl) ? wasteSelectBean.rqbzsl : "1");

        if (!isWeight) {
            et_item8.setText(wasteSelectBean.rkl);
        }

        et_item10.setText(wasteSelectBean.zyldNumber1);
        et_item15.setText(commonTransfer(mData.rqbzcz, "包装材质"));
        et_item16.setText(commonTransfer(mData.rqbzgg, "包装规模"));
        et_item11.setText(commonTransfer(mData.rqbzlx, "包装方式"));

        groupBzbm();

    }


    @OnClick({R.id.et_item3, R.id.et_item5, R.id.et_item6, R.id.et_item7, R.id.et_item13, R.id.et_item15, R.id.et_item16, R.id.et_item17, R.id.ll_handle,
             R.id.ll_back, R.id.ll_save, R.id.ll_delete,  R.id.ll_print, R.id.ll_clear, R.id.ll_qp, R.id.ll_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            //选择蓝牙称
//            case R.id.ll_select_blt:
//                btnDevice_Click();
//                break;


            //废物类别
            case R.id.et_item3:
                if (TextUtils.isEmpty(mData.zcssbm)) {
                    ToastUtil.showToast(mContext, "请先选择贮存设备编码！");
                } else {

                    String url = ApiUrl.getHnUrl(ApiUrl. URL_JYTZ_FWLB_LIST);//编辑

                    goToActivity(new Intent(mContext, SelectWasteByZcssListActivity.class)
                                    .putExtra("zcssbm", mData.zcssbm)
                                    .putExtra("id", mData.id)
                                    .putExtra("title", "选择入库信息")
                                    .putExtra("url", url)
                            , 11);

                }
                break;

            //经营出库去向
            case R.id.et_item5:
                showPopWindow("经营出库去向", et_item5, false);
                break;

            //计量单位
            case R.id.et_item17:
                showPopWindow("计量单位", et_item17, false);
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
                mPresenter.getLabelInfo(mData.id);
                break;


            //去皮
            case R.id.ll_qp:
                goToActivity(new Intent(mContext, TareListActivity.class)
                                .putExtra("allParamList", GsonUtil.GsonToString(allParamList))
                        , 10);

                break;

            // 保存
            case R.id.ll_save:
                et_item18.clearFocus();
                mData.cksj = et_item2.getText().toString();
                mData.ckl = et_item8.getText().toString();
                mData.rqbzbm = et_item9.getText().toString();
                mData.zyldNumber1 = et_item10.getText().toString();
                mData.rqbzsl = et_item12.getResultString();
                mData.jldw = et_item17.getText().toString();
                mData.ysjbr = et_item18.getText().toString();
                mData.ckjbr = editSpinner_19.getText().toString();
                mData.zyldbh = editSpinner_21.getText().toString();
                mData.lrfs = isWeight ? "1" : "";

                String deviceCode = SPUtils.get(mContext, "DEVICE_CODE", "").toString();
                mData.deviceCode = deviceCode;

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

                break;

            // 删除
            case R.id.ll_delete:
                mPresenter.showDeleteDialog(mData.id);
                break;


            //利用/处置
            case R.id.ll_handle:
                getCshjFwid();
                break;
            //开始扫描
            case R.id.ll_scan:
                showScanDialog(0);
                break;

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
                .putExtra("IS_CK", true)
                .putExtra("IS_WEIGHT", isWeight)
                .putExtra("CURRENT_WEIGHT", et_item8.getText().toString()), 13);
    }


    //跳转至委外/自行利用处置
    private void gotoZxww() {
        Class temActivity = ProductZxlyInfoActivity.class;
        if (typeName.contains("委外")) {
            temActivity = ProductWwlyInfoActivity.class;
        }

        goToActivity(new Intent(mContext, temActivity)
                .putExtra("typeValue", typeValue)
                .putExtra("typeName", typeName)
                .putExtra("ckdjStr", GsonUtil.GsonToString(mData)), 11);


    }

    private ScanCodeUtil scanCodeUtil;

    private void showScanDialog(long delayTime) {

        scanCodeUtil = new ScanCodeUtil(JytzOutputInfoActivity.this,isLandscape,delayTime, Constant.REQUEST_CODE);
        scanCodeUtil.scanCode();
        scanCodeUtil.getResultListener(result -> {
            if (!TextUtils.isEmpty(result)) {
                mPresenter.getScanInfo(result);
            }
        });

    }


    //获取产生环节信息
    private void getCshjFwid() {
        Map<String, String> param = new HashMap<>();
        param.put("fwdlbh", mData.fwdlbh);
        param.put("fwxxmc", mData.fwxxmc);
        param.put("fwdlmc", mData.fwdlmc);
        param.put("fwxlbh", mData.fwxlbh);
        param.put("fwxlmc", mData.fwxlmc);
        mPresenter.getCshjFwid(param);
    }


    /*
     * 判空处理
     * */
    private String checkEmpty() {

        if (TextUtils.isEmpty(mData.ckl)) {
            return "请输入/地磅获取出库量！";
        } else if (Double.valueOf(mData.ckl) <= 0) {
            return "请输入/地磅获取正确的出库量！";
        } else if (TextUtils.isEmpty(mData.zcssbm)) {
            return "请选择贮存设施编码！";
        } else if (TextUtils.isEmpty(mData.fwdlbh)) {
            return "请选择废物类别！";
        } else if (TextUtils.isEmpty(mData.qx)) {
            return "请选择去向！";
        }

//        else if (TextUtils.isEmpty(mData.rqbzcz)) {
//            return "请选择容器/包装材质！";
//        } else if (TextUtils.isEmpty(mData.rqbzgg)) {
//            return "请选择容器/包装规格！";
//        } else if (TextUtils.isEmpty(mData.rqbzsl) || Double.valueOf(mData.rqbzsl) <= 0) {
//            return "请输入容器/包装数量！";
//        }
//        else if (TextUtils.isEmpty(mData.zyldbh) && !isZx) {
//            return "请输入转移联单编号！";
//        }
        else if (TextUtils.isEmpty(mData.ysjbr)) {
            return "请输入运送部门经办人！";
        }

        else {
            return "";
        }
    }


    private void showSaveDialog() {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确定保存？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                Map<String, String> map = GsonUtil.GsonToMaps(GsonUtil.GsonToString(mData));
                mPresenter.saveData(map, ApiUrl.URL_JYTZ_OUTPUT_SAVE);
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
    protected JytzOutputInfoPresenter createPresenter() {
        return new JytzOutputInfoPresenter();
    }


    @Override
    public void showError(String msg) {
        ToastUtil.showToast(mContext, msg);
    }


    @Override
    public void getZcssListSuccess(List<ZcssBean> list, boolean isGetWasteList) {
        String json = GsonUtil.GsonToString(list);
        zcssList = GsonUtil.JsonToList(json, ZcssBean.class);

        if (!TextUtils.isEmpty(mData.zcssbm)) {
            for (int i = 0; i < zcssList.size(); i++) {
                if (mData.zcssbm.equals(zcssList.get(i).no)) {
                    zcssSelectBean = zcssList.get(i);
                    mData.zcsslx = zcssSelectBean.sslx;

                    et_item13.setText(zcssSelectBean.no);
                    et_item14.setText(zcssSelectBean.sslx + zcssSelectBean.ssmc);
                }
            }
        }

    }


    private List<CommonProductInfoBean> wasteTypeList = new ArrayList<>();
    private List<ZcssBean> zcssList = new ArrayList<>();
    private List<UserBean> userList = new ArrayList<>();
    private List<UserBean> receiverList = new ArrayList<>();


    @Override
    public void getParamListSuccess(List<ParamListBean> list) {
        String json = GsonUtil.GsonToString(list);
        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);

        initTypeByQx();

        if (!TextUtils.isEmpty(mData.id)) {
            initView();
        }

    }

    @Override
    public void saveDataSuccess(Object bean) {
        mData = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), CommonProductInfoBean.class);
        setJldw(mData.jldw);
        handleReadOnly(true);
        ToastUtil.showToast(mContext, "保存成功！");
        et_item1.setText(mData.ckpch);
        ll_print.setVisibility(View.VISIBLE);
        ll_delete.setVisibility(View.VISIBLE);
        ll_save.setVisibility(View.GONE);
        setResult(9);
        initTypeByQx();
    }

    //根据去向初始化自行/委外类型
    private void initTypeByQx() {
        String qxName = commonTransfer(mData.qx, "经营出库去向");
        if (qxName.contains("自行") || qxName.contains("委外")) {
            tv_handle.setText(qxName);
            typeName = qxName;
            typeValue = qxName.contains("利用") ? "R" : "D";
            ll_handle.setVisibility("1".equals(mData.isly) ? View.GONE : View.VISIBLE);
        } else {
            ll_handle.setVisibility(View.GONE);
        }
    }


    private List<UserBean> temUserList = new ArrayList<>();

    @Override
    public void getUserListSuccess(List<UserBean> list) {
        String json = GsonUtil.GsonToString(list);
        userList = GsonUtil.JsonToList(json, UserBean.class);
        temUserList = userList;

        if (temUserList.size() == 0) {
            editSpinner_19.setHint("请输入");
            editSpinner_19.setText(SPUtils.get(mContext, Constants.USER_NAME, "").toString());
            return;
        }

        List<Select> selectList = new ArrayList<>();
        for (int i = 0; i < temUserList.size(); i++) {
            Select bean = new Select(temUserList.get(i).name, temUserList.get(i).name);
            selectList.add(bean);
        }
        editSpinner_19.setItemData(selectList);
        editSpinner_19.setDisabled(false);
        if (!TextUtils.isEmpty(mData.ckjbr)) {
            editSpinner_19.setText(mData.ckjbr);
        }
        editSpinner_19.setOnInsertListener(new SearchSpinner.onInsertListener() {
            @Override
            public void insertChange(String name, String value, boolean isFromSelect) {
                mData.ckjbr = name;
//                if (!isFromSelect) {//如果是输入或者是为空的时候筛选
//                    mPresenter.getUserList(name);
//                }
            }
        });

    }


    @Override
    public void deleteDataSuccess(String bean) {
        ToastUtil.showToast(mContext, "删除成功！");
        setResult(9);
        finish();
    }

    @Override
    public void getLabelInfoSuccess(Object bean) {

        LabelInfoBean data = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), LabelInfoBean.class);
//        data.WLXZ = commonTransfer(data.WLXZ, "物理特性");

        goToActivity(new Intent(mContext, NewPrintLabelActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(data)), 10);

    }


    @Override
    public void getScanInfoSuccess(String bean) {
        wasteSelectBean = GsonUtil.GsonToBean(bean, CommonProductInfoBean.class);
        initWasteInfo();
        mData.zcssbm = wasteSelectBean.zcssbm;
        mData.zcsslx = wasteSelectBean.zcsslx;
        mPresenter.getZcssList(false);
    }

    @Override
    public void getCshjFwidSuccess(String bean) {
        CommonProductInfoBean cshjfwbean = GsonUtil.GsonToBean(bean, CommonProductInfoBean.class);
        mData.fwId = cshjfwbean.id;
        mData.ckfwid = cshjfwbean.id;
        gotoZxww();

    }

    private SelectPopupWindow selectPopupWindow;

    //初始化选择地址下拉弹窗
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

            String textStr = item.getText().toString();
            if (!TextUtils.isEmpty(textStr)) {//回显处理
                String[] split = textStr.split(",");
                for (int i = 0; i < selectPopupWindowList.size(); i++) {
                    for (int k = 0; k < split.length; k++) {
                        if (split[k].equals(selectPopupWindowList.get(i).NAME)) {
                            selectPopupWindowList.get(i).SELECT = true;
                        }
                    }
                }
            }

            selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnCheckClickItemListener() {
                @Override
                public void onDo(String mType, List<SelectPopupWindowBean> datas) {
                    String resultName = "";
                    for (int i = 0; i < datas.size(); i++) {
                        if (i == datas.size() - 1) {
                            resultName += datas.get(i).NAME;
                        } else {
                            resultName += datas.get(i).NAME + ",";
                        }
                    }
                    item.setText(resultName);
                }
            });
        } else {
            selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
                @Override
                public void onDo(SelectPopupWindowBean model, String mType) {
                    item.setText(model.NAME);
                    //废物特性,物理特性,包装方式,计量单位,处理方式及去向,包装材质,包装规模
                    switch (mType) {
                        case "经营出库去向":
                            mData.qx = model.ID;
//                            showHideByQx();
                            break;
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

    //如果为自行利用/处置时，隐藏转移联单编号、接收单位名称
    private void showHideByQx() {
        if (commonTransfer(mData.qx, "经营出库去向").contains("自行")) {
            ll_item20_21.setVisibility(View.GONE);
            isZx = true;
            //自控
            editSpinner_21.setText("/");
            mData.jsdwmc = "";
            mData.jsdwid = "";
        } else {
            ll_item20_21.setVisibility(View.VISIBLE);
            isZx = false;
        }
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


    private ZcssBean zcssSelectBean = null;
    private CommonProductInfoBean wasteSelectBean = null;

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
                mData.zcssbm = zcssSelectBean.no;
                mData.zcsslx = zcssSelectBean.sslx;

                wasteSelectBean = new CommonProductInfoBean();
                mData.gfYbqyTzCkZyFwxx = new CommonProductInfoBean.GfYbqyTzCkZyFwxx();

                //处理选择后出库量被置空
                wasteSelectBean.rkl = et_item8.getText().toString();
                initWasteInfo();
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item13.getParent(), sort);
    }


}
