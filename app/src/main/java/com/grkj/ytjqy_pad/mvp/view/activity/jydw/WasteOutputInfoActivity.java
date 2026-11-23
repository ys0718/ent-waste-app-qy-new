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
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.GfYbqyXxbaXkzxxBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.model.ZxlyssBean;
import com.grkj.ytjqy_pad.mvp.presenter.WasteOutputInfoPresenter;
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
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 危废出库信息
 *
 * @author JueHongP
 */
public class WasteOutputInfoActivity extends BaseActivity<WasteOutputInfoPresenter> implements WasteOutputInfoPresenter.View {

    /**
     * 表单数据
     */
    private CommonProductInfoBean mData = new CommonProductInfoBean();

    /**
     * 暂存表单数据(除废物信息外)
     */
    private CommonProductInfoBean stagingData = new CommonProductInfoBean();

    /**
     * 批次号
     */
    @BindView(R.id.et_item1)
    EditText et_item1;

    /**
     * 许可证编号
     */
    @BindView(R.id.et_item2)
    EditText et_item2;

    /**
     * 利用处置设施编码
     */
    @BindView(R.id.et_item3)
    EditText et_item3;

    /**
     * 贮存设施编码
     */
    @BindView(R.id.et_item4)
    EditText et_item4;

    /**
     * 贮存设施类型
     */
    @BindView(R.id.et_item5)
    EditText et_item5;

    /**
     * 出库量
     */
    @BindView(R.id.et_item6)
    EditText et_item6;

    /**
     * 计量单位
     */
    @BindView(R.id.et_jldw)
    EditText et_jldw;

    /**
     * 出库时间
     */
    @BindView(R.id.et_item7)
    EditText et_item7;

    /**
     * 废物类别
     */
    @BindView(R.id.et_item8)
    EditText et_item8;

    /**
     * 选择废物
     */
    @BindView(R.id.ll_xzfw)
    LinearLayout ll_xzfw;

    /**
     * 扫码选择废物
     */
    @BindView(R.id.ll_smtb)
    LinearLayout ll_smtb;

    /**
     * 废物类别名称
     */
    @BindView(R.id.et_item9)
    EditText et_item9;

    /**
     * 废物名称
     */
    @BindView(R.id.et_item10)
    EditText et_item10;

    /**
     * 废物代码
     */
    @BindView(R.id.et_item11)
    EditText et_item11;

    /**
     * 危险特性
     */
    @BindView(R.id.et_item12)
    EditText et_item12;

    /**
     * 废物形态
     */
    @BindView(R.id.et_item13)
    EditText et_item13;

    /**
     * 容器/包装类型
     */
    @BindView(R.id.et_item14)
    EditText et_item14;

    /**
     * 容器/包装数量
     */
    @BindView(R.id.et_item15)
    EditText et_item15;

    /**
     * 去向
     */
    @BindView(R.id.et_item16)
    EditText et_item16;

    /**
     * 运送部门经办人
     */
    @BindView(R.id.hsSpinner_1)
    HistorySearchSpinner hsSpinner_1;

    /**
     * 出库部门经办人
     */
    @BindView(R.id.sSpinner_1)
    SearchSpinner sSpinner_1;

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


    private boolean enableSave = true;
    /**
     * 选中的贮存设施
     */
    private ZcssBean zcssSelectBean = null;
    /**
     * 选中的许可证编号
     */
    private GfYbqyXxbaXkzxxBean licenceSelectBean = null;
    /**
     * 选中的利用处置设施编码
     */
    private ZxlyssBean zxlyssBean = null;

    @Override
    protected WasteOutputInfoPresenter createPresenter() {
        return new WasteOutputInfoPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_jy_output_info;
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

    @Override
    protected void initEventAndData() {
        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);

        Map<String, String> map = new HashMap<>();
        map.put("types", "废物特性,物理特性,包装方式,计量单位,处理方式及去向,包装材质,包装规模,出库去向");
        mPresenter.getParamList(map);

        //新增
        if (TextUtils.isEmpty(dataStr)) {
            mData = new CommonProductInfoBean();
            stagingData = new CommonProductInfoBean();

            mData.jldw = getJldw();
            ll_print.setVisibility(View.GONE);
            ll_delete.setVisibility(View.GONE);
        } else { //编辑
            mData = GsonUtil.GsonToBean(dataStr, CommonProductInfoBean.class);
            //已保存无法再选择废物信息
            ll_xzfw.setClickable(false);
            ll_smtb.setClickable(false);
            //已被处理时不可保存、删除
            if ("1".equals(mData.isly)) {
                ll_save.setVisibility(View.GONE);
                ll_delete.setVisibility(View.GONE);
                ll_print.setVisibility(View.VISIBLE);
            } else {
                ll_save.setVisibility(View.VISIBLE);
                ll_print.setVisibility(View.VISIBLE);
                ll_delete.setVisibility(View.VISIBLE);
            }
        }

        setTimeDialogClickListener(et_item7);
    }

    private void initData() {

        et_item1.setText(mData.ckpch);
        et_item2.setText(mData.xkzbh);
        et_item3.setText(mData.ssbm);
        et_item4.setText(mData.zcssbm);
        et_item5.setText(mData.zcsslx);
        et_item6.setText(mData.ckl);
        et_item7.setText(TextUtils.isEmpty(mData.cksj) ? DateTimeUtil.getCurrentDate() : mData.cksj);
        et_item8.setText(mData.fwdlbh);
        et_item9.setText(mData.fwdlmc);
        et_item10.setText(mData.fwxxmc);
        et_item11.setText(mData.fwxlbh);
        et_item12.setText(mData.fwtx);
        et_item13.setText(mData.wlxz);
        et_item14.setText(commonTransfer(mData.rqbzlx, "包装方式"));
        et_item15.setText(mData.rqbzsl);
        et_item16.setText(commonTransfer(mData.qx, "出库去向"));
        hsSpinner_1.setText(mData.ysjbr);
        sSpinner_1.setText(TextUtils.isEmpty(mData.ckjbr) ? SPUtils.get(mContext, Constants.USER_NAME, "").toString() : mData.ckjbr);

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.et_item2, R.id.et_item3, R.id.et_item4, R.id.et_item16, R.id.et_jldw, R.id.ll_xzfw, R.id.ll_smtb, R.id.ll_delete, R.id.ll_save, R.id.ll_print, R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //选择许可证编号
            case R.id.et_item2:
                showLicencePopWindow();
                break;

            //选择利用处置设施编码
            case R.id.et_item3:
                showLyczssPopWindow();
                break;

            //选择贮存设施编码
            case R.id.et_item4:
                showZcssPopWindow();
                break;

            //选择计量单位
            case R.id.et_jldw:
                showPopWindow("计量单位", et_jldw);
                break;

            //手动选择废物
            case R.id.ll_xzfw:
                if (TextUtils.isEmpty(et_item4.getText().toString())) {
                    ToastUtil.showToast(mContext, "请先选择贮存设施!");
                } else {
                    Intent intent = new Intent(mContext, SelectWasteActivity.class);
                    intent.putExtra("url",ApiUrl.getHnUrl(ApiUrl.URL_WAIT_OUTPUT_LIST));
                    goToActivity(intent, 10);
                }
                break;

            //扫码选择废物
            case R.id.ll_smtb:
                if (TextUtils.isEmpty(et_item4.getText().toString())) {
                    ToastUtil.showToast(mContext, "请先选择贮存设施!");
                } else {
                    showScanDialog();
                }
                break;

            //选择去向
            case R.id.et_item16:
                showPopWindow("出库去向", et_item16);
                break;

            //删除
            case R.id.ll_delete:
                mPresenter.showDeleteDialog(mData.id);
                break;

            //返回
            case R.id.ll_back:
                finish();
                break;

            //保存
            case R.id.ll_save:
                onSaveClick();
                break;

            //打印
            case R.id.ll_print:
                mPresenter.getLabelInfo(mData.id);
                break;

            default:
                break;
        }
    }

    private ScanCodeUtil scanCodeUtil;

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

    private void onSaveClick() {
        if (!enableSave) {
            ToastUtil.showToast(mContext, "数据正在保存，请勿频繁操作");
            return;
        }

        //待出库量
        double ckl = Double.parseDouble(et_item6.getText().toString());
        //入库量
        double rkl;
        if (mData.rkl != null) { //新增
            rkl = Double.parseDouble(mData.rkl);
        } else { //编辑
            rkl = Double.parseDouble(mData.ckl);
        }
        /*更改计量单位*/
        if (stagingData.jldw != null && stagingData.jldw.equals("千克")) {
            ckl = Double.parseDouble(et_item6.getText().toString()) * 0.001;
        }

        if (ckl > rkl) {
            toastShow("出库量不能大于入库量！");
            return;
        }

        mData.sslx = stagingData.sslx;
        mData.xkzbh = stagingData.xkzbh;
        mData.xkzbid = stagingData.xkzbid;
        mData.ssbh = stagingData.ssbh;
        mData.ssbm = stagingData.ssbm;
        mData.lyczfs = stagingData.lyczfs;
        mData.lyczfsxl = stagingData.lyczfsxl;
        mData.zcssId = stagingData.zcssId;
        mData.zcssbm = stagingData.zcssbm;
        mData.zcsslx = stagingData.zcsslx;
        mData.ckl = et_item6.getText().toString();
        mData.cksj = et_item7.getText().toString();
        mData.qx = stagingData.qx;
        mData.ysjbr = hsSpinner_1.getText();
        mData.ckjbr = sSpinner_1.getText();

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

        if (TextUtils.isEmpty(et_item6.getText().toString())) {
            return "请输入出库量！";
        } else if (Double.parseDouble(et_item6.getText().toString()) <= 0) {
            return "请输入正确的出库量！";
        } else if (TextUtils.isEmpty(et_item2.getText().toString())) {
            return "请选择许可证编号！";
        } else if (TextUtils.isEmpty(et_item3.getText().toString())) {
            return "请选择利用处置设施编码！";
        } else if (TextUtils.isEmpty(et_item4.getText().toString())) {
            return "请选择贮存设施编码！";
        } else if (TextUtils.isEmpty(et_item8.getText().toString())) {
            return "请选择废物信息！";
        } else if (TextUtils.isEmpty(et_item16.getText().toString())) {
            return "请选择去向！";
        } else if (TextUtils.isEmpty(hsSpinner_1.getText())) {
            return "请输入运送部门经办人！";
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
                Map<String, String> map = GsonUtil.GsonToMaps(GsonUtil.GsonToString(mData));
                mPresenter.saveData(map, ApiUrl.getHnUrl(ApiUrl.URL_WASTE_OUTPUT_SAVE));
                roundCornerDialog.dismiss();
            }

            @Override
            public void onCancelClick() {

            }
        });
        //显示对话框
        roundCornerDialog.show();
    }

    /**
     * 参数请求成功处理
     */
    @Override
    public void getParamListSuccess(List<ParamListBean> bean) {
        String json = GsonUtil.GsonToString(bean);
        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);

        mPresenter.getLicenceList();
        mPresenter.getZxlyDeviceList(zxlyDeviceParams);
        mPresenter.getZcssList(zcssParams);

    }

    private List<GfYbqyXxbaXkzxxBean> licenceList = new ArrayList<>();

    /**
     * 许可证请求成功处理
     */
    @Override
    public void getLicenceListSuccess(List<GfYbqyXxbaXkzxxBean> result) {
        String json = GsonUtil.GsonToString(result);
        licenceList = GsonUtil.JsonToList(json, GfYbqyXxbaXkzxxBean.class);
    }

    private final Map<String, String> zxlyDeviceParams = new HashMap<>();
    private List<ZxlyssBean> zxlyDeviceList = new ArrayList<>();

    /**
     * 利用处置设施请求成功处理
     */
    @Override
    public void getZxlyDeviceListSuccess(PageBean model) {
        String json = GsonUtil.GsonToString(model.getRows());
        zxlyDeviceList = GsonUtil.JsonToList(json, ZxlyssBean.class);
    }

    private final Map<String, String> zcssParams = new HashMap<>();
    private List<ZcssBean> zcssList = new ArrayList<>();

    /**
     * 贮存设施请求成功处理
     */
    @Override
    public void getZcssListSuccess(List<ZcssBean> bean) {
        String json = GsonUtil.GsonToString(bean);
        zcssList = GsonUtil.JsonToList(json, ZcssBean.class);

        if (!TextUtils.isEmpty(mData.zcssbm)) {
            for (int i = 0; i < zcssList.size(); i++) {
                if (mData.zcssbm.equals(zcssList.get(i).no)) {
                    zcssSelectBean = zcssList.get(i);
                }
            }
        }

        initData();
    }

    /**
     * 获取标签信息请求成功处理
     */
    @Override
    public void getLabelInfoSuccess(LabelInfoBean labelInfoBean) {
        startActivityForResult(new Intent(mContext, NewPrintLabelActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(labelInfoBean)), 10);
    }

    /**
     * 出库数据保存成功处理
     */
    @Override
    public void saveDataSuccess(Object bean) {
        mData = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), CommonProductInfoBean.class);
        setJldw(mData.jldw);
        ToastUtil.showToast(mContext, "保存成功！");
        et_item1.setText(mData.ckpch);
        ll_print.setVisibility(View.VISIBLE);
        ll_delete.setVisibility(View.VISIBLE);

        setResult(9);
    }

    /**
     * 出库数据删除成功处理
     */
    @Override
    public void deleteDataSuccess(String bean) {
        ToastUtil.showToast(mContext, "删除成功！");
        setResult(9);
        finish();
    }

    @Override
    public void getOutputInfoSuccess(CommonProductInfoBean bean) {
        if (bean != null) {
            mData = bean;
            mData.rkId = bean.id;
            mData.id = "";
            mData.lrfs = "2";
            initWasteData(mData);
        } else {
            toastShow("请扫描正确的二维码！");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //同步数据返回
        if (resultCode == 10) {
            mData = GsonUtil.GsonToBean(data.getStringExtra("selectBean"), CommonProductInfoBean.class);
            mData.lrfs = "";
            initWasteData(mData);
        } else if (data != null && requestCode == scanCodeUtil.scanRequestCode) {
            String scanCode = data.getStringExtra("ScanCodeResult");
            mPresenter.getOutputInfo(scanCode);
        }
    }

    private void initWasteData(CommonProductInfoBean data) {

        et_item6.setText(data.rkl);
        et_item8.setText(data.fwdlbh);
        et_item9.setText(data.fwdlmc);
        et_item10.setText(data.fwxxmc);
        et_item11.setText(data.fwxlbh);
        et_item12.setText(data.fwtx);
        et_item13.setText(data.wlxz);
        et_item14.setText(commonTransfer(data.rqbzlx, "包装方式"));
        et_item15.setText(data.rqbzsl);

    }

    @Override
    public void showError(String msg) {
        enableSave = true;
        toastShow(msg);
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
                stagingData.jldw = model.ID;
                setJldw(stagingData.jldw);
            } else if ("出库去向".equals(mType)) {
                stagingData.qx = model.ID;
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
     * 初始许可证编号下拉弹窗
     */
    private void showLicencePopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < licenceList.size(); i++) {
            selectPopupWindowList.add(new SelectPopupWindowBean(licenceList.get(i).xkzbid, licenceList.get(i).xkzbh));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, (SelectPopupWindow.OnClickItemListener) (model, mType) -> {
            for (int i = 0; i < licenceList.size(); i++) {
                if (licenceList.get(i).xkzbid.equals(model.ID)) {
                    licenceSelectBean = licenceList.get(i);
                }
            }
            et_item2.setText(licenceSelectBean.xkzbh);

            stagingData.xkzbh = licenceSelectBean.xkzbh;
            stagingData.xkzbid = licenceSelectBean.xkzbid;

            selectPopupWindow.dismiss();
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item2.getParent(), sort);
    }

    /**
     * 利用处置设施下拉弹窗
     */
    private void showLyczssPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < zxlyDeviceList.size(); i++) {
            ZxlyssBean zxlyssData = zxlyDeviceList.get(i);
            selectPopupWindowList.add(new SelectPopupWindowBean(zxlyssData.ssbh, getString(R.string.str_input_czssbm, zxlyssData.ssbh, zxlyssData.ssmc, zxlyssData.lyczfsdl, zxlyssData.lyczfsxl)));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, (SelectPopupWindow.OnClickItemListener) (model, mType) -> {
            for (int i = 0; i < zxlyDeviceList.size(); i++) {
                if (zxlyDeviceList.get(i).ssbh.equals(model.ID)) {
                    zxlyssBean = zxlyDeviceList.get(i);
                }
            }
            et_item3.setText(getString(R.string.str_input_czssbm, zxlyssBean.ssbh, zxlyssBean.ssmc, zxlyssBean.lyczfsdl, zxlyssBean.lyczfsxl));

            stagingData.sslx = zxlyssBean.czsslx;
            stagingData.ssbh = zxlyssBean.ssbh;
            stagingData.ssbm = zxlyssBean.ssmc;
            stagingData.lyczfs = zxlyssBean.lyczfsdl;
            stagingData.lyczfsxl = zxlyssBean.lyczfsxl;
            selectPopupWindow.dismiss();
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item3.getParent(), sort);
    }

    /**
     * 初始贮存设施下拉弹窗
     */
    private void showZcssPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < zcssList.size(); i++) {
            ZcssBean mData = zcssList.get(i);
            String name = getString(R.string.str_input_zcbm, mData.no, mData.sslx, mData.ssmc);
            selectPopupWindowList.add(new SelectPopupWindowBean(mData.id, name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, (SelectPopupWindow.OnClickItemListener) (model, mType) -> {
            for (int i = 0; i < zcssList.size(); i++) {
                if (zcssList.get(i).id.equals(model.ID)) {
                    zcssSelectBean = zcssList.get(i);
                }
            }
            et_item4.setText(zcssSelectBean.no);
            et_item5.setText(String.format("%s%s", zcssSelectBean.sslx, zcssSelectBean.ssmc));

            stagingData.zcssId = zcssSelectBean.id;
            stagingData.zcssbm = zcssSelectBean.no;
            stagingData.zcsslx = zcssSelectBean.sslx;

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
