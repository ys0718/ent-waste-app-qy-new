package com.grkj.ytjqy_pad.mvp.view.activity.hn.jydw;

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
import com.grkj.ytjqy_pad.mvp.model.GfYbqyXxbaXkzxxZcssBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.presenter.HnWasteOutputInfoPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.jydw.SelectWasteActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.printer2.NewPrintLabelActivity;
import com.grkj.ytjqy_pad.utils.common.AppUtils;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.popwindow.spinner.HistorySearchSpinner;
import com.grkj.ytjqy_pad.utils.popwindow.spinner.SearchSpinner;
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
 * 湖南---危废出库登记信息
 *
 * @author JueHongP
 */
public class WasteOutputInfoActivity extends BaseActivity<HnWasteOutputInfoPresenter> implements HnWasteOutputInfoPresenter.View {

    /**
     * 出库批次号
     */
    @BindView(R.id.et_item1)
    EditText et_item1;

    /**
     * 贮存设施编码
     */
    @BindView(R.id.et_item2)
    EditText et_item2;
    /**
     * 扫码获取
     */
    @BindView(R.id.ll_scan)
    LinearLayout ll_scan;
    /**
     * 贮存设施类型
     */
    @BindView(R.id.et_item3)
    EditText et_item3;
    /**
     * 出库量
     */
    @BindView(R.id.et_item4)
    EditText et_item4;
    /**
     * 计量单位
     */
    @BindView(R.id.et_jldw)
    EditText et_jldw;
    /**
     * 出库时间
     */
    @BindView(R.id.et_item5)
    EditText et_item5;
    /**
     * 废物类别
     */
    @BindView(R.id.et_item6)
    EditText et_item6;
    /**
     * 废物类别名称
     */
    @BindView(R.id.et_item7)
    EditText et_item7;
    /**
     * 废物代码
     */
    @BindView(R.id.et_item8)
    EditText et_item8;
    /**
     * 废物名称
     */
    @BindView(R.id.et_item9)
    EditText et_item9;
    /**
     * 危险特性
     */
    @BindView(R.id.et_item10)
    EditText et_item10;
    /**
     * 容器/包装类型
     */
    @BindView(R.id.et_item11)
    EditText et_item11;
    /**
     * 容器/包装材质
     */
    @BindView(R.id.et_item12)
    EditText et_item12;
    /**
     * 容器/包装规格
     */
    @BindView(R.id.et_item13)
    EditText et_item13;
    /**
     * 容器/包装数量
     */
    @BindView(R.id.add_view)
    AddNumberView add_view;
    /**
     * 去向
     */
    @BindView(R.id.et_item14)
    EditText et_item14;
    /**
     * 运送部门经办人
     */
    @BindView(R.id.et_item15)
    HistorySearchSpinner et_item15;
    /**
     * 贮存部门经办人
     */
    @BindView(R.id.et_item16)
    SearchSpinner et_item16;

    /**
     * 父布局
     */
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    /**
     * 生成标签
     */
    @BindView(R.id.ll_print)
    LinearLayout ll_print;
    /**
     * 删除按钮
     */
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;
    /**
     * 保存按钮
     */
    @BindView(R.id.ll_save)
    LinearLayout ll_save;
    /**
     * 复制按钮
     */
    @BindView(R.id.ll_copy)
    LinearLayout ll_copy;

    private CommonProductInfoBean mData = new CommonProductInfoBean();

    private boolean enableSave = true;

    /**
     * 选中贮存设施
     */
    private GfYbqyXxbaXkzxxZcssBean zcssSelectBean = null;
    /**
     * 选中废物类别
     */
    private CommonProductInfoBean fwxxSelectBean = null;


    @Override
    protected HnWasteOutputInfoPresenter createPresenter() {
        return new HnWasteOutputInfoPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_hnjy_output_info;
    }

    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("出库登记");
    }

    @Override
    protected void initEventAndData() {

        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);
        CommonProductInfoBean data = GsonUtil.GsonToBean(dataStr, CommonProductInfoBean.class);

        //新增
        if (TextUtils.isEmpty(dataStr)) {
            mData = new CommonProductInfoBean();
            mData.jldw = getJldw();
            ll_print.setVisibility(View.GONE);
            ll_delete.setVisibility(View.GONE);
        } else { //编辑
            mPresenter.getOutputInfoById(data.id);
            enHandle();

            //已被处理时不可保存、删除
            if ("1".equals(data.isly)) {
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
        map.put("types", "废物特性,物理特性,包装方式,计量单位,处理方式及去向,包装材质,包装规模,出库去向");
        mPresenter.getParamList(map);

        setTimeDialogClickListener(et_item5);
    }

    private void initData() {
        et_item1.setText(mData.ckpch);
        et_item2.setText(mData.zcssbm);
        et_item3.setText(mData.zcsslx);
        et_item4.setText(mData.ckl);
        et_jldw.setText(TextUtils.isEmpty(mData.jldw) ? getJldw() : mData.jldw);
        et_item5.setText(TextUtils.isEmpty(mData.cksj) ? DateTimeUtil.getCurrentDate() : mData.cksj);
        et_item6.setText(mData.fwdlbh);
        et_item7.setText(mData.fwdlmc);
        et_item8.setText(mData.fwxlbh);
        et_item9.setText(mData.fwxxmc);
        et_item10.setText(mData.fwtx);
        et_item11.setText(commonTransfer(mData.rqbzlx, "包装方式"));
        et_item12.setText(commonTransfer(mData.rqbzcz, "包装材质"));
        et_item13.setText(commonTransfer(mData.rqbzgg, "包装规模"));
        add_view.setResult(mData.rqbzsl);
        et_item14.setText(commonTransfer(mData.qx, "出库去向"));
        et_item15.setText(mData.ysjbr);
        et_item16.setText(TextUtils.isEmpty(mData.ckjbr) ? SPUtils.get(mContext, Constants.USER_NAME, "").toString() : mData.ckjbr);
    }

    /**
     * 禁止操作
     */
    private void enHandle() {
        et_item2.setEnabled(false);
        ll_scan.setEnabled(false);
        et_item6.setEnabled(false);
        et_item12.setEnabled(false);
        et_item13.setEnabled(false);
        add_view.setEnable(false);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.et_item2, R.id.ll_scan, R.id.et_item6, R.id.et_item12, R.id.et_jldw, R.id.et_item14, R.id.et_item13, R.id.ll_back, R.id.ll_save, R.id.ll_print, R.id.ll_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //贮存设施编码
            case R.id.et_item2:
                showZcssPopWindow();
                break;

            //扫码获取
            case R.id.ll_scan:
                showScanDialog();
                break;

            //计量单位
            case R.id.et_jldw:
                showPopWindow("计量单位", et_jldw);
                break;

            //废物类别
            case R.id.et_item6:
                if (!TextUtils.isEmpty(et_item2.getText().toString())) {
                    Intent intent = new Intent(mContext, SelectWasteActivity.class);
                    intent.putExtra("zcssId", stagingData.zcssId);
                    intent.putExtra("url", ApiUrl.URL_HN_WASTE_INFO_BY_ZCSSID);
                    goToActivity(intent, 10);
                } else {
                    ToastUtil.showToast(mContext, "请先选择贮存设施编码！");
                }
                break;

            //包装材质
            case R.id.et_item12:
                showPopWindow("包装材质", et_item12);
                break;

            //包装规模
            case R.id.et_item13:
                showPopWindow("包装规模", et_item13);
                break;

            //包装规模
            case R.id.et_item14:
                showPopWindow("出库去向", et_item14);
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

        mData.sslx = stagingData.sslx;
        mData.xkzbh = stagingData.xkzbh;
        mData.xkzbid = stagingData.xkzbid;
        mData.rqbzsl = add_view.getResultString();
        mData.ysjbr = et_item15.getText();
        mData.ckjbr = et_item16.getText();
        mData.deviceCode = SPUtils.get(mContext, "DEVICE_CODE", "").toString();

        if (!"2".equals(mData.lrfs)) {
            mData.lrfs = "";
        }

        //如果数据来源为空，则根据横竖屏设置
        if (TextUtils.isEmpty(mData.dataFrom) || "pc".equals(mData.dataFrom)) {
            mData.dataFrom = isLandscape ? Constants.TYPE_YTJ : Constants.TYPE_SCZD;
        }
        //如果版本来源为空，则获取版本信息
        if (TextUtils.isEmpty(mData.versionFrom)) {
            mData.versionFrom = AppUtils.getVersionName(mContext);
        }

        if (TextUtils.isEmpty(mData.xwqyDwid)) {
            mData.xwqyDwid = SPUtils.get(mContext, Constants.SELECT_DWID, "").toString();
        }

        if (TextUtils.isEmpty(checkEmpty())) {
            if (!isExceed(et_item4.getText().toString(), et_jldw.getText().toString(), mData.rkl, mData.jldw)) {
                mData.ckl = et_item4.getText().toString();
                mData.cksj = et_item5.getText().toString();
                mData.jldw = et_jldw.getText().toString();
                setJldw(mData.jldw);
                showSaveDialog("是否确定保存？");
            } else {
                toastShow("出库量不能超出入库量" + mData.rkl + mData.jldw + "！");
            }
        } else {
            ToastUtil.showToast(mContext, checkEmpty());
        }
    }

    /**
     * 判空处理
     */
    private String checkEmpty() {

        if (TextUtils.isEmpty(et_item4.getText().toString())) {
            return "请输入出库量！";
        } else if (TextUtils.isEmpty(et_item6.getText().toString())) {
            return "请选择废物类别！";
        } else if (TextUtils.isEmpty(et_item14.getText().toString())) {
            return "请选择去向！";
        } else if (TextUtils.isEmpty(et_item15.getText())) {
            return "请输入运送部门经办人！";
        } else {
            return "";
        }

    }

    private void showSaveDialog(String msg) {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage(msg);
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                enableSave = false;
                Map<String, String> map = GsonUtil.GsonToMaps(GsonUtil.GsonToString(mData));
                mPresenter.saveData(map, ApiUrl.getHnUrl(ApiUrl.URL_SAVE_HN_WASTE_OUTPUT));
                roundCornerDialog.dismiss();
            }

            @Override
            public void onCancelClick() {

            }
        });
        //显示对话框
        roundCornerDialog.show();
    }

    private ScanCodeUtil scanCodeUtil;

    /**
     * 扫码弹窗
     */
    private void showScanDialog() {

        scanCodeUtil = new ScanCodeUtil(WasteOutputInfoActivity.this, isLandscape, 0, Constant.REQUEST_CODE, true);
        scanCodeUtil.scanCode();
        scanCodeUtil.getResultListener(result -> {
            if (!TextUtils.isEmpty(result) && !result.contains("null")) {
                mPresenter.getOutputInfo(result);
            } else {
                toastShow("请扫描正确的二维码！");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10) {
            mData = GsonUtil.GsonToBean(data.getStringExtra("selectBean"), CommonProductInfoBean.class);
            mData.ckl = mData.rkl;
            mData.ysjbr = "";
            initData();
        } else if (data != null && requestCode == scanCodeUtil.scanRequestCode) {
            String scanCode = data.getStringExtra("ScanCodeResult");
            mPresenter.getOutputInfo(scanCode);
        }
    }

    private SelectPopupWindow selectPopupWindow;

    @Override
    public void getParamListSuccess(List<ParamListBean> bean) {
        String json = GsonUtil.GsonToString(bean);
        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);

        initData();
        mPresenter.getZcssList(zcssParams);
    }

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
                /*case "计量单位":
                    mData.jldw = model.ID;
                    setJldw(mData.jldw);*/
                case "包装材质":
                    mData.rqbzcz = model.ID;
                    break;
                case "包装规模":
                    mData.rqbzgg = model.ID;
                    break;
                case "出库去向":
                    mData.qx = model.ID;
                    break;
                default:
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

    /**
     * 贮存设施请求参数
     */
    private final Map<String, String> zcssParams = new HashMap<>();
    private List<GfYbqyXxbaXkzxxZcssBean> zcssList = new ArrayList<>();

    @Override
    public void getZcssListSuccess(List<ZcssBean> bean) {
        String json = GsonUtil.GsonToString(bean);
        zcssList = GsonUtil.JsonToList(json, GfYbqyXxbaXkzxxZcssBean.class);

        if (!TextUtils.isEmpty(mData.zcssbm)) {
            for (int i = 0; i < zcssList.size(); i++) {
                if (mData.zcssbm.equals(zcssList.get(i).no)) {
                    zcssSelectBean = zcssList.get(i);
                }
            }
        }
    }

    /**
     * 暂存数据
     */
    private CommonProductInfoBean stagingData = new CommonProductInfoBean();

    /**
     * 初始贮存设施下拉弹窗
     */
    private void showZcssPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < zcssList.size(); i++) {
            GfYbqyXxbaXkzxxZcssBean mData = zcssList.get(i);
            String name = getString(R.string.str_input_zcbm, mData.no, mData.sslx, mData.ssmc);
            selectPopupWindowList.add(new SelectPopupWindowBean(zcssList.get(i).id, name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, (SelectPopupWindow.OnClickItemListener) (model, mType) -> {

            for (int i = 0; i < zcssList.size(); i++) {
                if (zcssList.get(i).id.equals(model.ID)) {
                    zcssSelectBean = zcssList.get(i);
                }
            }

            stagingData.zcssId = TextUtils.isEmpty(zcssSelectBean.zcssId) ? zcssSelectBean.id : zcssSelectBean.zcssId;
            stagingData.zcssbm = zcssSelectBean.no;
            stagingData.zcsslx = zcssSelectBean.sslx;

            mData = stagingData;

            initData();

            selectPopupWindow.dismiss();
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item2.getParent(), sort);
    }

    @Override
    public void getOutputInfoByIdSuccess(CommonProductInfoBean bean) {
        mData = bean;
    }

    @Override
    public void saveDataSuccess(Object bean) {
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

    @Override
    public void deleteDataSuccess(String bean) {
        ToastUtil.showToast(mContext, "删除成功！");
        setResult(9);
        finish();
    }

    @Override
    public void getLabelInfoSuccess(LabelInfoBean labelInfoBean) {
        startActivityForResult(new Intent(mContext, NewPrintLabelActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(labelInfoBean)), 10);
    }

    @Override
    public void getOutputInfoSuccess(CommonProductInfoBean bean) {

        mData = bean;
        mData.lrfs = "2";
        mData.rkId = mData.id;
        mData.id = "";
        mData.ckl = mData.rkl;
        mData.ysjbr = "";

        enHandle();
        initData();
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

    @Override
    public void showError(String msg) {
        enableSave = true;
        toastShow(msg);
    }
}
