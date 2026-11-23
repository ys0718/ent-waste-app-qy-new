package com.grkj.ytjqy_pad.mvp.view.activity.jydw;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.YjsjBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.presenter.SjOutputInfoPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.SelectZyldListActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.bluetooth.WeightBaseActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.printer2.NewPrintLabelActivity;
import com.grkj.ytjqy_pad.utils.common.AppUtils;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.scancode.ScanCodeUtil;
import com.grkj.ytjqy_pad.utils.scancode.utils.Constant;
import com.grkj.ytjqy_pad.utils.view.AddNumberView;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author yusam
 * @date 2022/3/21
 * 收集出库登记信息
 */

public class SjOutputInfoActivity extends WeightBaseActivity<SjOutputInfoPresenter> implements SjOutputInfoPresenter.View {

    private CommonProductInfoBean mData = new CommonProductInfoBean();

    //批次号
    @BindView(R.id.et_item1)
    EditText et_item1;


    //计量单位
    @BindView(R.id.et_item17)
    EditText et_item17;

    //入库时间
    @BindView(R.id.et_item2)
    EditText et_item2;

    //贮存设施编码
    @BindView(R.id.et_item4)
    EditText et_item4;

    //贮存设施类型
    @BindView(R.id.et_item5)
    EditText et_item5;

    //贮存区域类型
    @BindView(R.id.et_item18)
    EditText et_item18;

    //废物类别
    @BindView(R.id.et_item6)
    EditText et_item6;

    //废物类别名称
    @BindView(R.id.et_item7)
    EditText et_item7;

    //废物代码
    @BindView(R.id.et_item3)
    EditText et_item3;

    //废物名称
    @BindView(R.id.et_item9)
    EditText et_item9;

    //废物描述
    @BindView(R.id.et_item10)
    EditText et_item10;

    //容器/包装类型
    @BindView(R.id.et_item11)
    EditText et_item11;

    //容器、包装材质
    @BindView(R.id.et_item12)
    EditText et_item12;

    //容器、包装规格
    @BindView(R.id.et_item13)
    EditText et_item13;

    //容器/包装数量
    @BindView(R.id.et_item14)
    AddNumberView et_item14;

    //运输经办人
    @BindView(R.id.et_item15)
    EditText et_item15;

    //出库经办人
    @BindView(R.id.et_item16)
    EditText et_item16;


    //转移联单编号
    @BindView(R.id.et_zyld)
    EditText et_zyld;

    //合同编号
    @BindView(R.id.et_item21)
    EditText et_item21;


    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //生成标签
    @BindView(R.id.ll_print)
    LinearLayout ll_print;


    //删除按钮
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;

    //保存按钮
    @BindView(R.id.ll_save)
    LinearLayout ll_save;

    //复制按钮
    @BindView(R.id.ll_copy)
    LinearLayout ll_copy;


    private boolean enableSave = true;
    /**
     * 选中贮存设施
     */
    private ZcssBean zcssSelectBean = null;
    /**
     * 贮存设施请求参数
     */
    private final Map<String, String> zcssParams = new HashMap<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_sj_output_info;
    }

    @Override
    protected void initEventAndData() {

        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);
        String defaultUserName = SPUtils.get(mContext, Constants.USER_NAME, "").toString();

        initWeight();

        if (isCopy){
            isGetWeight = true;
        }

        //新增
        if (TextUtils.isEmpty(dataStr)) {
            mData = new CommonProductInfoBean();
            isGetWeight = true;
            mData.jldw = getJldw();
            ll_print.setVisibility(View.GONE);
            ll_delete.setVisibility(View.GONE);
            et_item16.setText(defaultUserName);
            handleReadOnly(false);
            et_item17.setText(mData.jldw);
        } else { //编辑
            mData = GsonUtil.GsonToBean(dataStr, CommonProductInfoBean.class);
            //已出库时不可保存、删除
            if ("1".equals(mData.isck)) {
                ll_save.setVisibility(View.GONE);
                ll_delete.setVisibility(View.GONE);
                ll_print.setVisibility(View.VISIBLE);
            } else {
                ll_save.setVisibility(View.VISIBLE);
                ll_print.setVisibility(View.VISIBLE);
                ll_delete.setVisibility(View.VISIBLE);
            }

            if (!isCopy) {
                handleReadOnly(true);
            }

        }

        Map<String, String> map = new HashMap<>();
        map.put("types", "废物特性,物理特性,包装方式,计量单位,处理方式及去向,包装材质,包装规模,废物形态");
        mPresenter.getParamList(map);

        setTimeDialogClickListener(et_item2);
    }


    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("收集出库登记");
    }

    private void initView(CommonProductInfoBean data) {

        et_item1.setText(data.ckpch);
        et_item8.setText(TextUtils.isEmpty(data.ckl) ? data.jsl : data.ckl);
        et_item2.setText(TextUtils.isEmpty(data.cksj) ? DateTimeUtil.getCurrentDate() : data.cksj);
        et_item4.setText(data.zcssbm);
        et_item5.setText(data.zcsslx);
        et_item6.setText(data.fwdlbh);
        et_item7.setText(data.fwdlmc);
        et_item3.setText(data.fwxlbh);
        et_item9.setText(data.fwxxmc);
        et_item10.setText(data.fwxlmc);
        et_item11.setText(commonTransfer(data.rqbzlx, "包装方式"));
        et_item12.setText(commonTransfer(data.rqbzcz, "包装材质"));
        et_item13.setText(commonTransfer(data.rqbzgg, "包装规模"));
        et_item17.setText(TextUtils.isEmpty(mData.jldw) ? getJldw() : mData.jldw);

        et_item14.setResult(data.rqbzsl);
        et_item15.setText(data.ysjbr);
        et_item16.setText(data.ckjbr);
        et_item18.setText(data.zcssQyxxMc);

        et_zyld.setText(mData.zyldNumber1);
        et_item21.setText(data.csdwmc);

        showTipsCallback(et_zyld);
        showTipsCallback(et_item21);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //同步数据返回
        if (resultCode == 10) {
            mData = GsonUtil.GsonToBean(data.getStringExtra("selectBean"), CommonProductInfoBean.class);
            initView(mData);
        } else if (data != null && requestCode == scanCodeUtil.scanRequestCode) {
            String scanCode = data.getStringExtra("ScanCodeResult");
            mPresenter.getInputInfo(scanCode);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.et_item4, R.id.ll_back, R.id.ll_save, R.id.ll_print, R.id.ll_delete, R.id.et_item17, R.id.et_item6, R.id.et_item11,
            R.id.et_item12, R.id.et_item13, R.id.et_item18,R.id.ll_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //扫码选择废物
            case R.id.ll_scan:
                if (TextUtils.isEmpty(et_item4.getText().toString())) {
                    ToastUtil.showToast(mContext, "请先选择贮存设施!");
                } else if (TextUtils.isEmpty(et_item18.getText().toString())) {
                    ToastUtil.showToast(mContext, "请先选择贮存设施区域!");
                } else {
                    showScanDialog();
                }
                break;

            //贮存设施编码
            case R.id.et_item4:
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

            // 保存
            case R.id.ll_save:
                onSaveClick();
                break;

            //计量单位
            case R.id.et_item17:
                showPopWindow("计量单位", et_item17);
                break;

            //废物类别
            case R.id.et_item6:
                showFwlbPopWindow();
                break;

            //包装方式
            case R.id.et_item11:
                showPopWindow("包装方式", et_item11);
                break;


            //包装材质
            case R.id.et_item12:
                showPopWindow("包装材质", et_item12);
                break;

            //包装规模
            case R.id.et_item13:
                showPopWindow("包装规模", et_item13);
                break;


            //贮存设备区域编号
            case R.id.et_item18:
                showZcqyPopWindow();
                break;


            //复制
            case R.id.ll_copy:
                //赋空处理
                String mDataStr = GsonUtil.GsonToString(mData);
                CommonProductInfoBean copyData = GsonUtil.GsonToBean(mDataStr, CommonProductInfoBean.class);
                copyData.id = "";
                copyData.isck = "";
                copyData.ckpch = "";
                copyData.ckl = "";
                copyData.cspch = "";
                copyData.qrcodeValue = "";
                copyData.cksj = DateTimeUtil.getCurrentDate();
                copyData.gfYbqyTzCkZyFwxx = new CommonProductInfoBean.GfYbqyTzCkZyFwxx();

                String copyStr = GsonUtil.GsonToString(copyData);
                startActivityForResult(new Intent(mContext, SjOutputInfoActivity.class)
                        .putExtra(Constants.IS_COPY, true)
                        .putExtra(Constants.JUMP_KEY, copyStr), 9);

                break;

            // 删除
            case R.id.ll_delete:
                mPresenter.showDeleteDialog(mData.id);
                break;

            // 选择联单
            case R.id.ll_zyld:
                goToActivity(new Intent(mContext, SelectZyldListActivity.class),9);
                break;

            default:
                break;
        }
    }

    private void onSaveClick() {
        if (!enableSave) {
            ToastUtil.showToast(mContext, "数据正在保存，请勿频繁操作");
            return;
        }

        mData.ckl = et_item8.getText().toString();
        mData.cksj = et_item2.getText().toString();
        mData.rqbzsl = et_item14.getResultString();
        mData.ysjbr = et_item15.getText().toString();
        mData.ckjbr = et_item16.getText().toString();

        mData.lrfs = isWeight ? "1" : "";

        mData.deviceCode = SPUtils.get(mContext, "DEVICE_CODE", "").toString();

        //如果数据来源为空，则根据横竖屏设置
        if (TextUtils.isEmpty(mData.dataFrom)) {
            mData.dataFrom = isLandscape ? Constants.TYPE_YTJ : Constants.TYPE_SCZD;
        }
        //如果版本来源为空，则获取版本信息
        if (TextUtils.isEmpty(mData.versionFrom)) {
            mData.versionFrom = AppUtils.getVersionName(mContext);
        }

        if (TextUtils.isEmpty(checkEmpty())) {
            showSaveDialog();
        } else {
            ToastUtil.showToast(mContext, checkEmpty());
        }
    }


    /**
     * 判空处理
     */
    private String checkEmpty() {
         if (TextUtils.isEmpty(et_item8.getText().toString())) {
            return "请输入出库量！";
        } else if (TextUtils.isEmpty(et_item4.getText().toString())) {
            return "请选择贮存设施编码！";
        }  else if (TextUtils.isEmpty(et_item18.getText().toString())) {
            return "请选择贮存设施区域！";
        }else if (TextUtils.isEmpty(et_item6.getText().toString())) {
            return "请选择废物类别！";
        }else if (TextUtils.isEmpty(et_item11.getText().toString())) {
            return "请选择容器/包装类型！";
        }else if (TextUtils.isEmpty(et_item12.getText().toString())) {
            return "请选择容器/包装材质！";
        }else if (TextUtils.isEmpty(et_item13.getText().toString())) {
            return "请选择容器/包装规格！";
        }else if (TextUtils.isEmpty(et_item15.getText().toString())) {
            return "请输入运输经办人！";
        }else if (TextUtils.isEmpty(et_item16.getText().toString())) {
             return "请输入出库经办人！";
         }else {
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
                enableSave = false;
//                mData.gfYbqyTzCkZyFwxx = null;
                Map<String, String> map = GsonUtil.GsonToMaps(GsonUtil.GsonToString(mData));
                mPresenter.saveData(map, ApiUrl.URL_SJ_OUTPUT_SAVE);
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
    protected SjOutputInfoPresenter createPresenter() {
        return new SjOutputInfoPresenter();
    }

    private ScanCodeUtil scanCodeUtil;

    /**
     * 扫码弹窗
     */
    private void showScanDialog() {
        scanCodeUtil = new ScanCodeUtil(SjOutputInfoActivity.this, isLandscape, 0, Constant.REQUEST_CODE, true);
        scanCodeUtil.scanCode();
        scanCodeUtil.getResultListener(result -> {
            if (!TextUtils.isEmpty(result) && !result.contains("null")) {
                mPresenter.getInputInfo(result);
            } else {
                toastShow("请扫描正确的二维码！");
            }
        });
    }


    @Override
    public void showError(String msg) {
        enableSave = true;
        toastShow(msg);
    }


    /**
     * 获取贮存设施列表成功
     */
    @Override
    public void getZcssListSuccess(List<ZcssBean> list) {
        ZcssList = list;
        if (!TextUtils.isEmpty(mData.zcssbm)) {
            for (int i = 0; i < ZcssList.size(); i++) {
                if (mData.zcssbm.equals(ZcssList.get(i).ssbh)) {
                    zcssSelectBean = ZcssList.get(i);
                    Map<String, String> map = new HashMap<>();
                    map.put("mainid", zcssSelectBean.id);
                    map.put(Constants.PAGE_KEY, "1");
                    map.put(Constants.ROWS_KEY, "9999");
                    mPresenter.getZcqyList(map);
                }
            }
        }
        initView(mData);
    }

    @Override
    public void getZcQyListSuccess(List<ZcssBean> list) {
        ZcqyList = list;
    }


    private List<ZcssBean> ZcssList = new ArrayList<>();
    private List<ZcssBean> ZcqyList = new ArrayList<>();
    private List<YjsjBean> yjsjList = new ArrayList<>();

    /**
     * 获取参数列表成功
     */
    @Override
    public void getParamListSuccess(List<ParamListBean> list) {
        String json = GsonUtil.GsonToString(list);
        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);
        mPresenter.getZcssList(zcssParams);
    }

    /**
     * 保存数据成功
     */
    @Override
    public void saveDataSuccess(Object bean) {
        enableSave = true;
        mData = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), CommonProductInfoBean.class);
        setJldw(mData.jldw);
        if (!TextUtils.isEmpty(mData.ckpch)) {
            ToastUtil.showToast(mContext, "保存成功！");
            et_item1.setText(mData.ckpch);
            ll_print.setVisibility(View.VISIBLE);
            ll_delete.setVisibility(View.VISIBLE);
            setResult(9);
        }

    }

    /**
     * 获取标签信息成功
     */
    @Override
    public void getLabelInfoSuccess(LabelInfoBean labelInfoBean) {
//        LabelInfoBean data = GsonUtil.GsonToBean(GsonUtil.GsonToString(labelInfoBean), LabelInfoBean.class);
//        data.WLXZ = commonTransfer(data.WLXZ, "物理特性");
        startActivityForResult(new Intent(mContext, NewPrintLabelActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(labelInfoBean)), 10);
    }

    /**
     * 删除数据成功
     */
    @Override
    public void deleteDataSuccess(String bean) {
        ToastUtil.showToast(mContext, "删除成功！");
        setResult(9);
        finish();
    }

    /**
     * 根据二维码获取待入库数据成功
     */
    @Override
    public void getInputInfoSuccess(CommonProductInfoBean bean) {
        if (bean != null) {
            mData = bean;
            mData.sgfId = mData.id;
            mData.id = "";
            mData.lrfs = "2";
            initView(mData);
        } else {
            toastShow("请扫描正确的二维码！");
        }
    }

    @Override
    public void getTypeListSuccess(List<CommonProductInfoBean> list) {
        wasteTypeList = list;
    }

    private SelectPopupWindow selectPopupWindow;

    /**
     * 初始化下拉弹窗
     */
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

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, (SelectPopupWindow.OnClickItemListener) (model, mType) -> {
            item.setText(model.NAME);

            switch (mType) {
                case "计量单位":
                    mData.jldw = model.ID;
                    setJldw(mData.jldw);
                    break;

                case "包装材质":
                    mData.rqbzcz = model.ID;
                    break;

                case "包装规模":
                    mData.rqbzgg = model.ID;
                    break;

                case "包装方式":
                    mData.rqbzlx = model.ID;
                    break;
            }

            selectPopupWindow.dismiss();
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }

        selectPopupWindow.show(mLayout, type, (View) item.getParent(), sort);

    }


    private List<CommonProductInfoBean> wasteTypeList = new ArrayList<>();

    //初始废物类别下拉弹窗
    private void showFwlbPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < wasteTypeList.size(); i++) {
            CommonProductInfoBean mData = wasteTypeList.get(i);
            String name = getString(R.string.str_record_fwlb, mData.fwxxmc, mData.fwdlbh, mData.fwdlmc, mData.fwxlbh);
            selectPopupWindowList.add(new SelectPopupWindowBean(wasteTypeList.get(i).id, name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                CommonProductInfoBean bean = new CommonProductInfoBean();
                for (int i = 0; i < wasteTypeList.size(); i++) {
                    if (wasteTypeList.get(i).id.equals(model.ID)) {
                        bean = wasteTypeList.get(i);
                    }
                }

                initWasteInfo(bean);
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item6.getParent().getParent(), sort);
    }


    //选择/清空废物类别信息
    private void initWasteInfo(CommonProductInfoBean data) {
        mData.rkId = data.id;
        mData.rkpch = data.rkpch;

//        mData.fwdlbh = data.fwdlbh;
//        mData.fwdlmc = data.fwdlmc;
//        mData.fwxlbh = data.fwxlbh;
//        mData.fwxxmc = data.fwxxmc;
//        mData.fwxlmc = data.fwxlmc;
//        mData.rqbzlx = data.rqbzlx;
//        mData.rqbzcz = data.rqbzcz;
//        mData.rqbzgg = data.rqbzgg;
//        mData.rqbzsl = data.rqbzsl;
//        mData.zcjbr = data.zcjbr;

        mData.ckl = data.rkl;
        mData.qrcodeUrl = data.qrcodeUrl;
        mData.fwdlbh = data.fwdlbh;
        mData.fwxxmc = data.fwxxmc;
        mData.fwtx = data.fwtx;
        mData.hftx = data.hftx;
        mData.wlxz = data.wlxz;
        mData.fwxt = data.fwxt;
        mData.rqbzlx = data.rqbzlx;
        mData.fwxlbh = data.fwxlbh;
        mData.fwxlmc = data.fwxlmc;
        mData.mlnf = data.mlnf;
        mData.wxcf = data.wxcf;
        mData.fwdlmc = data.fwdlmc;
        mData.fwId = data.id;
        mData.rqbzbm = data.rqbzbm;
        mData.rqbzcz = data.rqbzcz;
        mData.rqbzgg = data.rqbzgg;
        mData.zysx = data.zysx;
        mData.remark = data.remark;
        mData.rkQrcodeValue = data.qrcodeValue;
        mData.qrcodeValue = data.qrcodeValue;
        mData.zywrcf = data.zywrcf;
        mData.bqlx = data.bqlx;


        et_item6.setText(data.fwdlbh);
        et_item7.setText(data.fwdlmc);
        et_item3.setText(data.fwxlbh);
        et_item8.setText(data.rkl);
        et_item9.setText(data.fwxxmc);
        et_item10.setText(data.fwxlmc);
        et_item11.setText(commonTransfer(data.rqbzlx, "包装方式"));
        et_item12.setText(commonTransfer(data.rqbzcz, "包装材质"));
        et_item13.setText(commonTransfer(data.rqbzgg, "包装规模"));

        et_zyld.setText(mData.zyldNumber1);
        et_item21.setText(data.csdwmc);

        et_item14.setResult(data.rqbzsl);
    }


    /**
     * 初始贮存设施下拉弹窗
     */
    private void showZcssPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < ZcssList.size(); i++) {
            ZcssBean mData = ZcssList.get(i);
            String name = getString(R.string.str_input_zcbm, mData.ssbh, mData.sslx, mData.ssmc);
            selectPopupWindowList.add(new SelectPopupWindowBean(ZcssList.get(i).id, name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, (SelectPopupWindow.OnClickItemListener) (model, mType) -> {
            for (int i = 0; i < ZcssList.size(); i++) {
                if (ZcssList.get(i).id.equals(model.ID)) {
                    zcssSelectBean = ZcssList.get(i);
                }
            }
            et_item4.setText(zcssSelectBean.ssbh);
            et_item5.setText(String.format("%s%s", zcssSelectBean.sslx, zcssSelectBean.ssmc));

            mData.zcssid = zcssSelectBean.id;
            mData.zcssbm = zcssSelectBean.ssbh;
            mData.zcsslx = zcssSelectBean.sslx;

            et_item18.setText("");
            mData.zcssQyxxMc = "";
            mData.zcssQyxxId = "";

            initWasteInfo(new CommonProductInfoBean());

            Map<String, String> map = new HashMap<>();
            map.put("mainid", zcssSelectBean.id);
            map.put(Constants.PAGE_KEY, "1");
            map.put(Constants.ROWS_KEY, "9999");
            mPresenter.getZcqyList(map);
            selectPopupWindow.dismiss();
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item4.getParent(), sort);
    }


    /**
     * 初始贮存区域下拉弹窗
     */
    private void showZcqyPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < ZcqyList.size(); i++) {
            selectPopupWindowList.add(new SelectPopupWindowBean(ZcqyList.get(i).id, ZcqyList.get(i).qymc));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, (SelectPopupWindow.OnClickItemListener) (model, mType) -> {
            et_item18.setText(model.NAME);
            mData.zcssQyxxId = model.ID;
            mData.zcssQyxxMc = model.NAME;
            initWasteInfo(new CommonProductInfoBean());
            mPresenter.getTypeList(mData.zcssQyxxId);
            selectPopupWindow.dismiss();
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item18.getParent(), sort);
    }



    /**
     * 通用转义
     */
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

}
