package com.grkj.ytjqy_pad.mvp.view.activity.jydw;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.presenter.WasteInputInfoPresenter;
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
 * 危废入库登记信息
 */

public class WasteInputInfoActivity extends BaseActivity<WasteInputInfoPresenter> implements WasteInputInfoPresenter.View {

    private CommonProductInfoBean mData = new CommonProductInfoBean();

    //批次号
    @BindView(R.id.et_item1)
    EditText et_item1;

    //入库量
    @BindView(R.id.et_item2)
    EditText et_item2;

    //计量单位
    @BindView(R.id.et_jldw)
    EditText et_jldw;

    //入库时间
    @BindView(R.id.et_item3)
    EditText et_item3;

    //贮存设施编码
    @BindView(R.id.et_item4)
    EditText et_item4;

    //贮存设施类型
    @BindView(R.id.et_item5)
    EditText et_item5;

    //废物类别
    @BindView(R.id.et_item6)
    EditText et_item6;

    //废物类别名称
    @BindView(R.id.et_item7)
    EditText et_item7;

    //废物代码
    @BindView(R.id.et_item8)
    EditText et_item8;

    //废物名称
    @BindView(R.id.et_item9)
    EditText et_item9;

    //危险特性
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
    EditText et_item14;

    //运送部门经办人
    @BindView(R.id.et_item15)
    EditText et_item15;

    //同步数据
    @BindView(R.id.ll_tbsj)
    LinearLayout ll_tbsj;

    //扫码同步
    @BindView(R.id.ll_smtb)
    LinearLayout ll_smtb;

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
        return R.layout.activity_jy_input_info;
    }

    @Override
    protected void initEventAndData() {

        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);
        //新增
        if (TextUtils.isEmpty(dataStr)) {
            mData = new CommonProductInfoBean();
            mData.jldw = getJldw();
            ll_print.setVisibility(View.GONE);
            ll_delete.setVisibility(View.GONE);
        } else { //编辑
            mData = GsonUtil.GsonToBean(dataStr, CommonProductInfoBean.class);
            ll_tbsj.setClickable(false);
            ll_smtb.setClickable(false);
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
        }

        Map<String, String> map = new HashMap<>();
        map.put("types", "废物特性,物理特性,包装方式,计量单位,处理方式及去向,包装材质,包装规模,废物形态");
        mPresenter.getParamList(map);


        setTimeDialogClickListener(et_item3);
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
    }

    private void initView(CommonProductInfoBean data) {

        et_item1.setText(data.rkpch);
        et_item2.setText(TextUtils.isEmpty(data.rkl) ? data.jsl : data.rkl);
        et_item3.setText(TextUtils.isEmpty(data.rksj) ? DateTimeUtil.getCurrentDate() : data.rksj);
        et_item4.setText(data.zcssbm);
        et_item5.setText(data.zcsslx);
        et_item6.setText(data.fwdlbh);
        et_item7.setText(data.fwdlmc);
        et_item8.setText(data.fwxlbh);
        et_item9.setText(data.fwxxmc);
        et_item10.setText(data.fwtx);
        et_item11.setText(commonTransfer(data.rqbzlx, "包装方式"));
        et_item12.setText(data.rqbzsl);
        et_item13.setText(data.ysjbr);

        et_item14.setText(data.rqbzsl);
        et_item15.setText(TextUtils.isEmpty(data.ysjbr) ? SPUtils.get(mContext, Constants.USER_NAME, "").toString() : data.ysjbr);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //同步数据返回
        if (resultCode == 10) {
            mData = GsonUtil.GsonToBean(data.getStringExtra("selectBean"), CommonProductInfoBean.class);
            mData.lrfs = "";
            initView(mData);
        } else if (data != null && requestCode == scanCodeUtil.scanRequestCode) {
            String scanCode = data.getStringExtra("ScanCodeResult");
            mPresenter.getInputInfo(scanCode);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.et_item4, R.id.ll_tbsj, R.id.ll_smtb, R.id.ll_back, R.id.ll_save, R.id.ll_print, R.id.ll_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //同步数据
            case R.id.ll_tbsj:
                goToActivity(new Intent(mContext, WasteInputSyncActivity.class), 10);
                break;

            //扫码同步
            case R.id.ll_smtb:
                showScanDialog();
                break;

            /*//计量单位
            case R.id.et_jldw:
                showPopWindow("计量单位", et_jldw);
                break;*/

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
                copyData.gfYbqyTzRkFwxx = new CommonProductInfoBean.GfYbqyTzRkFwxx();

                String copyStr = GsonUtil.GsonToString(copyData);
                startActivityForResult(new Intent(mContext, WasteInputInfoActivity.class)
                        .putExtra(Constants.IS_COPY, true)
                        .putExtra(Constants.JUMP_KEY, copyStr), 9);

                break;

            // 删除
            case R.id.ll_delete:
                mPresenter.showDeleteDialog(mData.id);
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

        mData.rkl = et_item2.getText().toString();
        mData.rksj = et_item3.getText().toString();

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

        if (TextUtils.isEmpty(et_item2.getText().toString())) {
            return "请输入入库量！";
        } else if (TextUtils.isEmpty(et_item4.getText().toString())) {
            return "请选择贮存设施编码！";
        } else if (TextUtils.isEmpty(et_item6.getText().toString())) {
            return "请先同步数据！";
        } else {
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
                mPresenter.saveData(map, ApiUrl.getHnUrl(ApiUrl.URL_WASTE_INPUT_SAVE));
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
    protected WasteInputInfoPresenter createPresenter() {
        return new WasteInputInfoPresenter();
    }

    private ScanCodeUtil scanCodeUtil;

    /**
     * 扫码弹窗
     */
    private void showScanDialog() {
        scanCodeUtil = new ScanCodeUtil(WasteInputInfoActivity.this, isLandscape, 0, Constant.REQUEST_CODE, true);
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
        String json = GsonUtil.GsonToString(list);
        zcssList = GsonUtil.JsonToList(json, ZcssBean.class);

        if (!TextUtils.isEmpty(mData.zcssbm)) {
            for (int i = 0; i < zcssList.size(); i++) {
                if (mData.zcssbm.equals(zcssList.get(i).no)) {
                    zcssSelectBean = zcssList.get(i);
                }
            }
        }
        initView(mData);

    }


    private List<ZcssBean> zcssList = new ArrayList<>();

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
        if (!TextUtils.isEmpty(mData.rkpch)) {
            ToastUtil.showToast(mContext, "保存成功！");
            et_item1.setText(mData.rkpch);
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
            if ("计量单位".equals(mType)) {
                mData.jldw = model.ID;
                setJldw(mData.jldw);
            }
            selectPopupWindow.dismiss();
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }

        selectPopupWindow.show(mLayout, type, (View) item.getParent(), sort);

    }

    /**
     * 初始贮存设施下拉弹窗
     */
    private void showZcssPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < zcssList.size(); i++) {
            ZcssBean mData = zcssList.get(i);
            String name = getString(R.string.str_input_zcbm, mData.no, mData.sslx, mData.ssmc);
            selectPopupWindowList.add(new SelectPopupWindowBean(zcssList.get(i).id, name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, (SelectPopupWindow.OnClickItemListener) (model, mType) -> {
            for (int i = 0; i < zcssList.size(); i++) {
                if (zcssList.get(i).id.equals(model.ID)) {
                    zcssSelectBean = zcssList.get(i);
                }
            }
            et_item4.setText(zcssSelectBean.no);
            et_item5.setText(String.format("%s%s", zcssSelectBean.sslx, zcssSelectBean.ssmc));

            mData.zcssId = zcssSelectBean.id;
            mData.zcssbm = zcssSelectBean.no;
            mData.zcsslx = zcssSelectBean.sslx;

            selectPopupWindow.dismiss();
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item4.getParent(), sort);
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
