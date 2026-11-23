package com.grkj.ytjqy_pad.mvp.view.activity.jytz;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.JytzInfoBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.TareListBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.model.JytzZyldBean;
import com.grkj.ytjqy_pad.mvp.presenter.JytzInputInfoPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.CheckInfoActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.TareListActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.bluetooth.WeightBaseActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.printer2.NewPrintLabelActivity;
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
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;
import com.grkj.ytjqy_pad.utils.view.ScanDialog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2026/6/24.
 * 经营台账-入库信息
 */

public class JytzInputInfoActivity extends WeightBaseActivity<JytzInputInfoPresenter> implements JytzInputInfoPresenter.View {

    private CommonProductInfoBean mData = new CommonProductInfoBean();

    //批次号
    @BindView(R.id.et_item1)
    EditText et_item1;

    //入库时间
    @BindView(R.id.et_item2)
    EditText et_item2;

    //入库量
    @BindView(R.id.et_item8)
    EditText et_item8;

    //数字识别码
    @BindView(R.id.et_item10)
    EditText et_item10;


    //贮存设备编码
    @BindView(R.id.et_item13)
    EditText et_item13;

    //贮存设备类型
    @BindView(R.id.et_item14)
    EditText et_item14;

    //计量单位
    @BindView(R.id.et_item17)
    EditText et_item17;

    //运送部门经办人
    @BindView(R.id.et_item18)
    HistorySearchSpinner et_item18;

    //贮存部门经办人
    @BindView(R.id.editSpinner_19)
    SearchSpinner editSpinner_19;


    //转移联单
    @BindView(R.id.ll_item21)
    LinearLayout ll_item21;


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



    //转移联单
    @BindView(R.id.editSpinner_21)
    EditText et_zyld;

    //产生单位名称
    @BindView(R.id.et_csdw)
    EditText et_csdw;

    private boolean enableSave = true;


    private ZcssBean zcssSelectBean = null;
    private CommonProductInfoBean wasteSelectBean = null;
    private String csfwid;

    //是否为处置单位
    private boolean isCzdw;//是否为处置单位


    private Map<String, String> zcssParams = new HashMap<>();


    @Override
    protected int getLayout() {
        return R.layout.activity_jytz_input_info;
    }

    @Override
    protected void initEventAndData() {

        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);
//        isCzdw = getIntent().getBooleanExtra("isCzdw", false);
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
            mData.rksj = et_item2.getText().toString();
            ll_print.setVisibility(View.GONE);
            handleReadOnly(false);

        } else {//编辑
            mData = GsonUtil.GsonToBean(dataStr, CommonProductInfoBean.class);
            ll_save.setVisibility(View.GONE);
            if ("1".equals(mData.isck)) {//已出库时不可保存、删除

                ll_delete.setVisibility(View.GONE);
                ll_print.setVisibility(View.VISIBLE);
                ll_copy.setVisibility(View.VISIBLE);
            } else {
                if (isCopy) {//复制逻辑
                    ll_copy.setVisibility(View.GONE);
                    ll_print.setVisibility(View.GONE);
                    ll_delete.setVisibility(View.GONE);

                } else {
                    ll_copy.setVisibility(View.VISIBLE);
                    ll_print.setVisibility(View.VISIBLE);
                    ll_delete.setVisibility(View.VISIBLE);
                }
            }

            if (!isCopy) {
                handleReadOnly(true);
            }
        }


        Map<String, String> map = new HashMap<>();
        map.put("types", "废物特性,物理特性,包装方式,计量单位,处理方式及去向,包装材质,包装规模,废物形态");
        mPresenter.getParamList(map);

        et_zyld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //置空数字识别码
                szsbmList = new ArrayList<>();
                et_item10.setText("");
            }
        });
    }


    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("入库登记");

//        if(isCzdw){
//            findViewById(R.id.ll_glcf).setVisibility(View.GONE);
//        }

    }

    private void initView() {
        et_item1.setText(mData.rkpch);
        et_item2.setText(mData.rksj);
        et_zyld.setText(mData.zyldNumber1);
        et_item10.setText(mData.qrcodeValue);
        et_item13.setText(mData.zcssbm);
        et_item14.setText(mData.zcsslx);
        et_csdw.setText(mData.csdwmc);
        et_item8.setText(mData.rkl);
        et_item17.setText(TextUtils.isEmpty(mData.jldw) ? getJldw() : mData.jldw);
        et_item18.setText(mData.ysjbr);
        editSpinner_19.setText(mData.zcjbr);
    }


    /**
     * 从废物列表中提取所有废物名称，用逗号连接成字符串（传统循环方式，兼容 Java 7）
     * @param wasteList 废物列表（可为 null）
     * @return 逗号分隔的名称字符串，若列表为空或全为 null 则返回空字符串
     */
    public static String extractFwmc(List<JytzInfoBean.WasteItem> wasteList) {
        if (wasteList == null || wasteList.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (JytzInfoBean.WasteItem item : wasteList) {
            // 过滤 null 元素和 null 的 fwmc 字段（字段为 public，直接访问）
            if (item != null && item.fwmc != null) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(item.fwmc);
            }
        }
        return sb.toString();
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
        } else if (resultCode == 13 && !isWeight) { //校准
            setResult(9);
            String current_weight = data.getStringExtra("CURRENT_WEIGHT");
            et_item8.setText(current_weight);
        }  else if (resultCode == 11) { //转移联单
            String zyldStr = data.getStringExtra("zyldBean");
            //置空数字识别码
            szsbmList = new ArrayList<>();
            et_item10.setText("");

            JytzZyldBean zyldBean = GsonUtil.GsonToBean(zyldStr, JytzZyldBean.class);
            et_zyld.setText(zyldBean.number1);
            et_csdw.setText(zyldBean.csdwzym);
            mData.csdwmc = zyldBean.csdwzym;
            mData.zyldId = zyldBean.id;
        } else if (resultCode == 12) { //数字识别码列表
            String selectListStr = data.getStringExtra("selectList");
            List<JytzInfoBean.WasteItem> selectList = GsonUtil.JsonToList(selectListStr, JytzInfoBean.WasteItem.class);
            szsbmList = new ArrayList<>();
            szsbmList.addAll(selectList);

            String totalWeight = "0";
            if (szsbmList.size() != 0) {
                //赋值计量单位
                mData.jldw = szsbmList.get(0).jldw;
                et_item17.setText(mData.jldw);
                for (int i = 0; i < szsbmList.size(); i++) {//计算入库量
                   totalWeight = new BigDecimal(totalWeight).add(new BigDecimal(szsbmList.get(i).jsl)).setScale(4, RoundingMode.HALF_UP).toString();
                }
                et_item8.setText(totalWeight);
            }
            et_item10.setText(extractFwmc(szsbmList));
        } else if (data != null && requestCode == scanCodeUtil.scanRequestCode) { // TODO: 2023/11/20
            String scanCode = data.getStringExtra("ScanCodeResult");
            mPresenter.getScanInfo(scanCode, zcssSelectBean.id);
        }

    }


    @OnClick({R.id.ll_delete,  R.id.ll_select_zyld,R.id.ll_szsbm,R.id.et_item13,
            R.id.et_item17, R.id.ll_back, R.id.ll_save, R.id.ll_print, R.id.ll_clear, R.id.ll_qp,  R.id.ll_copy, R.id.ll_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {


            //选择转移联单
            case R.id.ll_select_zyld:
                startActivityForResult(new Intent(mContext, SelectZyldListActivity.class)
                        , 11);

                break;

            //数字识别码
            case R.id.ll_szsbm:

                if(TextUtils.isEmpty(et_zyld.getText().toString())){
                    ToastUtil.showToast(mContext, "请选择转移联单！");
                    return;
                }
                mPresenter.getZyldInfo(et_zyld.getText().toString());

                startActivityForResult(new Intent(mContext, SelectMultiNumListActivity.class)
                                .putExtra("wasteSelectedList", szsbmList.size() != 0 ? GsonUtil.GsonToString(szsbmList) : "")
                                .putExtra("number1",et_zyld.getText().toString())
                        , 12);
                break;

            //计量单位
            case R.id.et_item17:
//                showPopWindow("计量单位", et_item17, false);
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
//                mPresenter.checkPrintable(mData.id);
                mPresenter.getLabelInfo(mData.id);
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
                startActivityForResult(new Intent(mContext, TareListActivity.class)
                                .putExtra("allParamList", GsonUtil.GsonToString(allParamList))
                        , 10);
                break;

            // 保存
            case R.id.ll_save:
                onSaveClick();
                break;

            //复制
            case R.id.ll_copy:
//                //赋空处理
//                String mDataStr = GsonUtil.GsonToString(mData);
//                CommonProductInfoBean copyData = GsonUtil.GsonToBean(mDataStr, CommonProductInfoBean.class);
//                copyData.id = "";
//                copyData.isck = "";
//                copyData.rkpch = "";
//                copyData.rkl = "";
//                copyData.cspch = "";
//                copyData.qrcodeValue = "";
//                copyData.rksj = DateTimeUtil.getCurrentDate();
//                copyData.WasteItem = new CommonProductInfoBean.WasteItem();
//                copyData.bqlx = getBqlxFromDevice();
//
//                String copyStr = GsonUtil.GsonToString(copyData);
//                startActivityForResult(new Intent(mContext, JytzLyczInfoActivity.class)
//                        .putExtra(Constants.IS_COPY, true)
//                        .putExtra("isWeight", isWeight)
//                        .putExtra(Constants.JUMP_KEY, copyStr), 9);

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

    private List<JytzInfoBean.WasteItem> szsbmList = new ArrayList<>();

    private void onSaveClick() {
        if (!enableSave) {
            ToastUtil.showToast(mContext, "数据正在保存，请勿频繁操作");
            return;
        }


        JytzInfoBean mData = new JytzInfoBean();
        et_item18.clearFocus();
        mData.rksj = et_item2.getText().toString();
//        mData.rkl = et_item8.getText().toString();
//        mData.cspch = et_item10.getText().toString();
//        mData.jldw = et_item17.getText().toString();
        mData.ysjbr = et_item18.getText();
        mData.zcjbr = editSpinner_19.getText().toString();
        mData.zcssbm = this.mData.zcssbm;
        mData.zcsslx = this.mData.zcsslx;
        mData.zcssid = this.mData.zcssid;
        mData.zyldId = this.mData.zyldId;
        mData.csdwmc = this.mData.csdwmc;
        mData.jldw = this.mData.jldw;
        mData.zyldNumber1 = et_zyld.getText().toString();
        mData.fwsz = szsbmList;

        if (TextUtils.isEmpty(mData.dataFrom)) {//如果数据来源为空，则根据横竖屏设置
            mData.dataFrom = isLandscape ? Constants.TYPE_YTJ : Constants.TYPE_SCZD;
        }

        if (TextUtils.isEmpty(checkEmpty())) {
            showSaveDialog(mData);
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
        startActivityForResult(new Intent(mContext, CheckInfoActivity.class)
                .putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(mData))
                .putExtra("IS_CK", false)
                .putExtra("IS_WEIGHT", isWeight)
                .putExtra("CURRENT_WEIGHT", et_item8.getText().toString()), 13);
    }


    private ScanDialog roundCornerDialog;

    private ScanCodeUtil scanCodeUtil;

    private void showScanDialog(long delayTime) {

        if (TextUtils.isEmpty(mData.zcssbm)) {
            ToastUtil.showToast(mContext, "请先选择贮存设施编码！");
            return;
        }

        scanCodeUtil = new ScanCodeUtil(JytzInputInfoActivity.this, isLandscape, delayTime, Constant.REQUEST_CODE);
        scanCodeUtil.scanCode();
        scanCodeUtil.getResultListener(result -> {
            if (!TextUtils.isEmpty(result)) {
                mPresenter.getScanInfo(result, zcssSelectBean.id);
            }
        });

        // TODO: 2023/11/20
        /*if (isLandscape) {

            if (roundCornerDialog == null) {
                roundCornerDialog = new ScanDialog(mContext);
                roundCornerDialog.setOnScanResultCallBack(new ScanDialog.OnScanResultCallBack() {
                    @Override
                    public void OnScanSuccess(String result) {
                        roundCornerDialog.dismiss();
                        mPresenter.getScanInfo(result, zcssSelectBean.id);
//                    mPresenter.getScanInfo(result, "");
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

        } */

    }


    /*
     * 判空处理
     * */
    private String checkEmpty() {
        if (TextUtils.isEmpty(et_item13.getText().toString())) {
            return "请选择贮存设施编码！";
        } else if (TextUtils.isEmpty(et_item18.getText())) {
            return "请输入运送部门经办人！";
        } else if (TextUtils.isEmpty(et_zyld.getText())) {
            return "请选择转移联单！";
        }  else if (szsbmList.size() == 0) {
            return "请选择数字识别码！";
        }else {
            return "";
        }
    }

    private void showSaveDialog(JytzInfoBean mData) {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确定保存？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                enableSave = false;
                Map<String, String> map = GsonUtil.GsonToMaps(GsonUtil.GsonToString(mData));
                mPresenter.saveData(map, ApiUrl.getHnUrl(ApiUrl.URL_JYTZ_INPUT_SAVE));
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
    protected JytzInputInfoPresenter createPresenter() {
        return new JytzInputInfoPresenter();
    }


    @Override
    public void showError(String msg) {
        enableSave = true;
        ToastUtil.showToast(mContext, msg);
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
            if (zcssList != null && zcssList.size()!=0) {
                zcssSelectBean = zcssList.get(0);
            }
        }

        if (zcssSelectBean != null) {
            mData.zcssbm = zcssSelectBean.no;
            mData.zcsslx = zcssSelectBean.sslx;
            mData.zcssid = zcssSelectBean.id;
            et_item13.setText(zcssSelectBean.no);
            et_item14.setText(zcssSelectBean.sslx + zcssSelectBean.ssmc);
        }

    }

    @Override
    public void geZyldInfoSuccess(JytzZyldBean zyldBean) {
        et_csdw.setText(zyldBean.csdwzym);
        mData.csdwmc = zyldBean.csdwzym;
        mData.zyldId = zyldBean.id;
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
    }

    @Override
    public void saveDataSuccess(Object bean) {
        enableSave = true;
        mData = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), CommonProductInfoBean.class);
        handleReadOnly(true);
        ToastUtil.showToast(mContext, "保存成功！");
        et_item1.setText(mData.rkpch);
        ll_print.setVisibility(View.VISIBLE);
        ll_delete.setVisibility(View.VISIBLE);
        ll_copy.setVisibility(View.VISIBLE);
        setResult(9);
        finish();

    }


    @Override
    public void getLabelInfoSuccess(Object bean) {

        LabelInfoBean data = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), LabelInfoBean.class);
        data.WLXZ = commonTransfer(data.WLXZ, "物理特性");
        startActivityForResult(new Intent(mContext, NewPrintLabelActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(data)), 10);

    }

    @Override
    public void deleteDataSuccess(String bean) {
        ToastUtil.showToast(mContext, "删除成功！");
        setResult(9);
        finish();
    }


    @Override
    public void getScanInfoSuccess(String bean) {
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
                        case "计量单位":
//                            mData.jldw = model.ID;
//                            changeUnit(mData.jldw);
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
                mData.zcssbm = zcssSelectBean.no;
                mData.zcssid = zcssSelectBean.id;
                mData.zcsslx = zcssSelectBean.sslx;

                //置空废物信息
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
