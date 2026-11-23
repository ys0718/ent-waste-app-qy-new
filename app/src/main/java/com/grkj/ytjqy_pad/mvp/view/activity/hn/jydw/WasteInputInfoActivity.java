package com.grkj.ytjqy_pad.mvp.view.activity.hn.jydw;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.GfYbqyXxbaXkzxxBean;
import com.grkj.ytjqy_pad.mvp.model.GfYbqyXxbaXkzxxZcssBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.presenter.HnWasteInputInfoPresenter;
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
import com.grkj.ytjqy_pad.utils.view.GrRecyclerView;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 湖南---危废入库登记信息
 *
 * @author JueHongP
 */
public class WasteInputInfoActivity extends BaseActivity<HnWasteInputInfoPresenter> implements HnWasteInputInfoPresenter.View {

    /**
     * 入库批次号
     */
    @BindView(R.id.et_item1)
    EditText et_item1;
    /**
     * 许可证编号
     */
    @BindView(R.id.et_item2)
    EditText et_item2;
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
     * 入库量
     */
    @BindView(R.id.et_item6)
    EditText et_item6;
    /**
     * 计量单位
     */
    @BindView(R.id.et_jldw)
    EditText et_jldw;
    /**
     * 入库时间
     */
    @BindView(R.id.et_item7)
    EditText et_item7;
    /**
     * 废物类别
     */
    @BindView(R.id.et_item8)
    EditText et_item8;
    /**
     * 废物类别名称
     */
    @BindView(R.id.et_item9)
    EditText et_item9;
    /**
     * 废物代码
     */
    @BindView(R.id.et_item10)
    EditText et_item10;
    /**
     * 废物名称
     */
    @BindView(R.id.et_item11)
    EditText et_item11;
    /**
     * 危险特性
     */
    @BindView(R.id.et_item12)
    EditText et_item12;
    /**
     * 容器/包装类型
     */
    @BindView(R.id.et_item13)
    EditText et_item13;
    /**
     * 容器/包装材质
     */
    @BindView(R.id.et_item14)
    EditText et_item14;
    /**
     * 容器/包装规格
     */
    @BindView(R.id.et_item15)
    EditText et_item15;
    /**
     * 容器/包装数量
     */
    @BindView(R.id.add_view)
    AddNumberView add_view;
    /**
     * 运送部门经办人
     */
    @BindView(R.id.et_item17)
    HistorySearchSpinner et_item17;
    /**
     * 贮存部门经办人
     */
    @BindView(R.id.editSpinner_19)
    SearchSpinner editSpinner_19;

    /**
     * 父布局
     */
    @BindView(R.id.mLayout)
    LinearLayout mLayout;
    /**
     * 新增
     */
    @BindView(R.id.ll_addWaste)
    LinearLayout ll_addWaste;
    /**
     * 扫码
     */
    @BindView(R.id.ll_scanWaste)
    LinearLayout ll_scanWaste;
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
    /**
     * 自动计算按钮
     */
    @BindView(R.id.ll_zdjs)
    LinearLayout ll_zdjs;

    /**
     * 已添加废物信息列表
     */
    @BindView(R.id.mRecyclerView)
    GrRecyclerView mRecyclerView;

    private CommonProductInfoBean mData = new CommonProductInfoBean();

    private boolean enableSave = true;
    /**
     * 选中许可证编号
     */
    private GfYbqyXxbaXkzxxBean licenceSelectBean = null;
    /**
     * 选中贮存设施
     */
    private GfYbqyXxbaXkzxxZcssBean zcssSelectBean = null;
    /**
     * 选中废物类别
     */
    private CommonProductInfoBean fwxxSelectBean = null;


    @Override
    protected HnWasteInputInfoPresenter createPresenter() {
        return new HnWasteInputInfoPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_hnjy_input_info;
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
        initTopToolbar("入库登记");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        initRecycleView();
    }

    @Override
    protected void initEventAndData() {

        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);
        CommonProductInfoBean data = GsonUtil.GsonToBean(dataStr, CommonProductInfoBean.class);

        //新增
        if (TextUtils.isEmpty(dataStr)) {
            mData = new CommonProductInfoBean();
            mData.jldw = getJldw();
            mData.rksj = DateTimeUtil.getCurrentDate();
            ll_print.setVisibility(View.GONE);
            ll_delete.setVisibility(View.GONE);
        } else { //编辑
            mPresenter.getInputInfoById(data.id);
            enHandle();
            //已出库时不可保存、删除
            if ("1".equals(data.isck)) {
                ll_addWaste.setEnabled(false);
                ll_scanWaste.setEnabled(false);
                ll_save.setVisibility(View.GONE);
                ll_delete.setVisibility(View.GONE);
                ll_print.setVisibility(View.VISIBLE);
            } else {
                ll_save.setVisibility(View.VISIBLE);
                ll_print.setVisibility(View.VISIBLE);
                ll_delete.setVisibility(View.VISIBLE);
            }

        }
        et_jldw.setEnabled(false);

        Map<String, String> map = new HashMap<>();
        map.put("types", "废物特性,物理特性,包装方式,计量单位,处理方式及去向,包装材质,包装规模,出库去向");
        mPresenter.getParamList(map);

        setTimeDialogClickListener(et_item7);
    }

    /**
     * 禁止操作
     */
    private void enHandle() {
        et_item2.setEnabled(true);
        et_item4.setEnabled(false);
        et_item8.setEnabled(false);
        et_item14.setEnabled(false);
        et_item15.setEnabled(false);
        add_view.setEnable(false);
    }


    private void initData() {

        et_item1.setText(mData.rkpch);
        et_item2.setText(mData.xkzbh);
        et_item4.setText(mData.zcssbm);
        et_item5.setText(mData.zcsslx);
        et_item6.setText(TextUtils.isEmpty(mData.rkl) ? "0" : mData.rkl);
        et_jldw.setText(mData.jldw);
        et_item7.setText(mData.rksj);
        et_item8.setText(mData.fwdlbh);
        et_item9.setText(mData.fwdlmc);
        et_item10.setText(mData.fwxlbh);
        et_item11.setText(mData.fwxxmc);
        et_item12.setText(mData.fwtx);
        et_item13.setText(commonTransfer(mData.rqbzlx, "包装方式"));
        et_item14.setText(commonTransfer(mData.rqbzcz, "包装材质"));
        et_item15.setText(commonTransfer(mData.rqbzgg, "包装规模"));
        add_view.setResult(mData.rqbzsl);
        et_item17.setText(mData.ysjbr);
        editSpinner_19.setText(TextUtils.isEmpty(mData.zcjbr) ? SPUtils.get(mContext, Constants.USER_NAME, "").toString() : mData.zcjbr);

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.et_item2, R.id.et_item4, R.id.et_item8, R.id.et_jldw, R.id.et_item14, R.id.et_item15, R.id.ll_scanWaste,
            R.id.ll_zdjs,R.id.ll_addWaste, R.id.ll_back, R.id.ll_save, R.id.ll_print, R.id.ll_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //许可证编号
            case R.id.et_item2:
                showLicencePopWindow();
                break;

            //贮存设施编码
            case R.id.et_item4:
                if (!TextUtils.isEmpty(et_item2.getText().toString())) {
                    showZcssPopWindow();
                } else {
                    ToastUtil.showToast(mContext, "请先选择许可证编号！");
                }
                break;

            //计量单位
            case R.id.et_jldw:
                showPopWindow("计量单位", et_jldw);
                break;

            //废物类别
            case R.id.et_item8:
                if (!TextUtils.isEmpty(et_item4.getText().toString())) {
                    showFwlbPopWindow();
                } else {
                    ToastUtil.showToast(mContext, "请先选择贮存设施编码！");
                }
                break;

            //包装材质
            case R.id.et_item14:
                showPopWindow("包装材质", et_item14);
                break;

            //包装规模
            case R.id.et_item15:
                showPopWindow("包装规模", et_item15);
                break;

            //添加废物信息
            case R.id.ll_addWaste:
                if (!TextUtils.isEmpty(mData.id)) {
                    Intent intent = new Intent(mContext, SelectXwqyWasteAcitivity.class);
                    intent.putExtra(Constants.MAINID, mData.id);
                    intent.putExtra("fwdlbh", mData.fwdlbh);
                    intent.putExtra("fwxlbh", mData.fwxlbh);
                    goToActivity(intent, 9);
                } else {
                    ToastUtil.showToast(mContext, "请先保存基本信息！");
                }
                break;
            //扫码添加废物信息
            case R.id.ll_scanWaste:
                if (!TextUtils.isEmpty(mData.id)) {
                    showScanDialog();
                } else {
                    ToastUtil.showToast(mContext, "请先保存基本信息！");
                }
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
                mPresenter.showDeleteDialog(mData.id, 0);
                break;

            //自动计算
            case R.id.ll_zdjs:
                if (mList.size() == 0){
                    ToastUtil.showToast(mContext,"请关联小微企业出库废物信息后重试！");
                    return;
                }

                updateTotalWeight();
                break;

            default:
                break;
        }
    }

    /**
     * 出库废物信息数据列表
     */
    private final List<CommonProductInfoBean.GFHnJyqyTzRkFwxxes> mList = new ArrayList<>();
    /**
     * 适配器
     */
    private CommonAdapter<CommonProductInfoBean.GFHnJyqyTzRkFwxxes> adapter;

    private void initRecycleView() {
        adapter = new CommonAdapter<CommonProductInfoBean.GFHnJyqyTzRkFwxxes>(mContext, R.layout.item_common_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final CommonProductInfoBean.GFHnJyqyTzRkFwxxes bean, final int position) {
                if (bean != null) {
                    holder.setText(R.id.tv_item1, TextUtils.isEmpty(bean.fwxxmc) ? getString(R.string.common_empty) : bean.fwxxmc);

                    String groupFwlb = getString(R.string.str_record_fwlb2, bean.fwdlbh, bean.fwdlmc);
                    setText(holder.getView(R.id.tv_item1), bean.fwxxmc, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2), groupFwlb, R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3), bean.fwxlbh, R.string.str_record_list2);
                    /*setText(holder.getView(R.id.tv_item4), bean.cssj, R.string.str_record_list11);*/
                    setText(holder.getView(R.id.tv_item6), bean.sl, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item7), bean.jldw, R.string.str_jldw_output);

                    ImageView iv_delete = holder.getView(R.id.iv_delete);

                    iv_delete.setVisibility("1".equals(mData.isck) ? View.GONE : View.VISIBLE);

                    iv_delete.setOnClickListener(v -> {
                        deleteBean = bean;
                        mList.remove(bean);
                        mPresenter.showDeleteDialog(bean.id, 1);
                    });

                }
            }
        };

        mRecyclerView.setAdapter(adapter);
    }

    private void setText(View tv, String value, int strRes) {
        tv.setVisibility(View.VISIBLE);

        ((TextView) tv).setText(Html.fromHtml(TextUtils.isEmpty(value) ? getString(strRes, getString(R.string.common_empty_text)) : getString(strRes, value)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //同步数据返回
        if (resultCode == 10) {
            String json = data.getStringExtra("selectBean");
            List<CommonProductInfoBean.GFHnJyqyTzRkFwxxes> selectList = GsonUtil.JsonToList(json, CommonProductInfoBean.GFHnJyqyTzRkFwxxes.class);
            mData.lrfs = "";
            mList.addAll(selectList);
            adapter.notifyDataSetChanged();
            ToastUtil.showToast(mContext, "成功关联数据！");
        }
    }

    private void onSaveClick() {
        if (!enableSave) {
            ToastUtil.showToast(mContext, "数据正在保存，请勿频繁操作");
            return;
        }

        mData.rkl = et_item6.getText().toString();
        mData.deviceCode = SPUtils.get(mContext, "DEVICE_CODE", "").toString();

        mData.rqbzsl = add_view.getResultString();
        mData.ysjbr = et_item17.getText();
        mData.zcjbr = editSpinner_19.getText();
        mData.jldw = et_jldw.getText().toString();

        if (TextUtils.isEmpty(mData.lrfs)) {
            mData.lrfs = "";
        }

        //如果数据来源为空，则根据横竖屏设置
        if (TextUtils.isEmpty(mData.dataFrom)) {
            mData.dataFrom = isLandscape ? Constants.TYPE_YTJ : Constants.TYPE_SCZD;
        }
        //如果版本来源为空，则获取版本信息
        if (TextUtils.isEmpty(mData.versionFrom)) {
            mData.versionFrom = AppUtils.getVersionName(mContext);
        }

        if (TextUtils.isEmpty(mData.xwqyDwid)) {
            mData.xwqyDwid = SPUtils.get(mContext, Constants.SELECT_DWID, "").toString();
        }

        if (mList.size() != 0) {
            mData.gfHnJyqyTzRkFwxxes = mList;
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
            return "请选择废物类别！";
        } else if (TextUtils.isEmpty(et_item17.getText())) {
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
                mPresenter.saveData(map, ApiUrl.getHnUrl(ApiUrl.URL_SAVE_HN_WASTE_INPUT));
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

        scanCodeUtil = new ScanCodeUtil(WasteInputInfoActivity.this, isLandscape, 0, Constant.REQUEST_CODE, true);
        scanCodeUtil.scanCode();
        scanCodeUtil.getResultListener(result -> {
            if (!TextUtils.isEmpty(result) && !result.contains("null")) {
                Map<String, String> params = new HashMap<>();
                params.put("fwdlbh", mData.fwdlbh);
                params.put("fwxlbh", mData.fwxlbh);
                params.put("qrcodeValue", result);
                mPresenter.getInputInfo(params);
            } else {
                toastShow("请扫描正确的二维码！");
            }
        });
    }

    private SelectPopupWindow selectPopupWindow;

    @Override
    public void getParamListSuccess(List<ParamListBean> bean) {
        String json = GsonUtil.GsonToString(bean);
        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);
        initData();
        mPresenter.getXkzList();
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
                case "计量单位":
                    mData.jldw = model.ID;
                    setJldw(mData.jldw);
                case "包装材质":
                    mData.rqbzcz = model.ID;
                    break;
                case "包装规模":
                    mData.rqbzgg = model.ID;
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

    private List<GfYbqyXxbaXkzxxBean> licenceList = new ArrayList<>();

    /**
     * 许可证请求成功处理
     */
    @Override
    public void getLicenceListSuccess(List<GfYbqyXxbaXkzxxBean> result) {
        String json = GsonUtil.GsonToString(result);
        licenceList = GsonUtil.JsonToList(json, GfYbqyXxbaXkzxxBean.class);
    }

    /**
     * 初始许可证编号下拉弹窗
     */
    private void showLicencePopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < licenceList.size(); i++) {
            selectPopupWindowList.add(new SelectPopupWindowBean(licenceList.get(i).id, licenceList.get(i).xkzbh));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, (SelectPopupWindow.OnClickItemListener) (model, mType) -> {

            zcssParams.put(Constants.MAINID, model.ID);
            mPresenter.getZcssList(zcssParams);
            mPresenter.getFwlbList(model.ID);

            for (int i = 0; i < licenceList.size(); i++) {
                if (licenceList.get(i).id.equals(model.ID)) {
                    licenceSelectBean = licenceList.get(i);
                }
            }
            et_item2.setText(licenceSelectBean.xkzbh);

            mData.xkzbh = licenceSelectBean.xkzbh;
            mData.xkzid = licenceSelectBean.id;

            refreshZcssData();

            selectPopupWindow.dismiss();
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item2.getParent(), sort);
    }

    /**
     * 更新贮存设施信息和废物信息
     */
    private void refreshZcssData() {
        et_item4.setText("");
        et_item5.setText("");
        refreshWasteData(new CommonProductInfoBean());
    }

    /**
     * 贮存设施请求参数
     */
    private final Map<String, String> zcssParams = new HashMap<>();
    private List<GfYbqyXxbaXkzxxZcssBean> zcssList = new ArrayList<>();

    @Override
    public void getZcssListSuccess(List<GfYbqyXxbaXkzxxZcssBean> bean) {
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
            et_item4.setText(zcssSelectBean.no);
            et_item5.setText(zcssSelectBean.sslx);

            mData.zcssId = TextUtils.isEmpty(zcssSelectBean.zcssId) ? zcssSelectBean.id : zcssSelectBean.zcssId;
            mData.zcssbm = zcssSelectBean.no;
            mData.zcsslx = zcssSelectBean.sslx;

            refreshWasteData(new CommonProductInfoBean());

            selectPopupWindow.dismiss();
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item4.getParent(), sort);
    }

    private List<CommonProductInfoBean> fwxxList = new ArrayList<>();

    @Override
    public void getFwlbListSuccess(List<CommonProductInfoBean> bean) {
        fwxxList = bean;
    }

    /**
     * 初始废物类别下拉弹窗
     */
    private void showFwlbPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < fwxxList.size(); i++) {
            CommonProductInfoBean fwxxData = fwxxList.get(i);
            String name = getString(R.string.str_input_fwlb, fwxxData.fwdlbh, fwxxData.fwdlmc);
            selectPopupWindowList.add(new SelectPopupWindowBean(fwxxData.fwdlbh, name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, (SelectPopupWindow.OnClickItemListener) (model, mType) -> {
            for (int i = 0; i < fwxxList.size(); i++) {
                if (fwxxList.get(i).fwdlbh.equals(model.ID)) {
                    fwxxSelectBean = fwxxList.get(i);
                }
            }
            refreshWasteData(fwxxSelectBean);

            selectPopupWindow.dismiss();
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item8.getParent(), sort);
    }

    /**
     * 更新废物信息
     */
    private void refreshWasteData(CommonProductInfoBean data) {

        et_item8.setText(data.fwdlbh);
        et_item9.setText(data.fwdlmc);
        et_item10.setText(data.fwxlbh);
        et_item11.setText(data.fwxxmc);
        et_item12.setText(data.fwtx);
        et_item13.setText(commonTransfer(TextUtils.isEmpty(data.rqbzlx) ? data.bzfs : data.rqbzlx, "包装方式"));
        et_item14.setText(commonTransfer(data.rqbzcz, "包装材质"));
        et_item15.setText(commonTransfer(data.rqbzgg, "包装规模"));

        //组装上传数据
        mData.fwId = data.id;
        mData.fwdlbh = data.fwdlbh;
        mData.fwdlmc = data.fwdlmc;
        mData.fwxlbh = data.fwxlbh;
        mData.fwxlmc = data.fwxlmc;
        mData.fwxxmc = data.fwxxmc;
        mData.fwtx = data.fwtx;
        mData.wlxz = data.wlxz;
        mData.zysx = data.zysx;
        mData.zywrcf = data.zywrcf;
        mData.wxcf = data.wxcf;
        mData.clfs = data.clfs;
        mData.bzfs = data.bzfs;
        mData.rqbzbm = data.rqbzbm;
        mData.rqbzcz = data.rqbzcz;
        mData.rqbzgg = data.rqbzgg;
        mData.rqbzlx = TextUtils.isEmpty(data.rqbzlx) ? data.bzfs : data.rqbzlx;
        mData.rqbzsl = data.rqbzsl;
        mData.lyczfsdl = data.lyczfsdl;
        mData.lyczfsxl = data.lyczfsxl;
        mData.remark = data.remark;
    }

    @Override
    public void saveDataSuccess(Object bean) {
        enableSave = true;
        CommonProductInfoBean result = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), CommonProductInfoBean.class);
        setJldw(result.jldw);
        if (!TextUtils.isEmpty(result.id)) {
            ToastUtil.showToast(mContext, "保存成功!");
            et_item1.setText(result.rkpch);
            ll_print.setVisibility(View.VISIBLE);
            ll_delete.setVisibility(View.VISIBLE);
            setResult(9);
        }
        mPresenter.getInputInfoById(result.id);
    }

    @Override
    public void getLabelInfoSuccess(LabelInfoBean labelInfoBean) {
        labelInfoBean.WLXZ = commonTransfer(labelInfoBean.WLXZ, "物理特性");
        startActivityForResult(new Intent(mContext, NewPrintLabelActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(labelInfoBean)), 10);
    }

    @Override
    public void deleteDataSuccess(String bean) {
        ToastUtil.showToast(mContext, "删除成功！");
        setResult(9);
        finish();
    }

    @Override
    public void getInputInfoSuccess(CommonProductInfoBean.GFHnJyqyTzRkFwxxes bean) {

        bean.ckId = bean.id;
        bean.id = "";
        bean.sl = bean.ckl;
        bean.mainid = mData.id;
        bean.csdwid = bean.dwid;
        bean.csdwmc = bean.entername;

        List<CommonProductInfoBean.GFHnJyqyTzRkFwxxes> paramList = new ArrayList<>();
        paramList.add(bean);
        mPresenter.saveCkFwxx(paramList, ApiUrl.URL_SAVE_HN_XWQY_WASTE_OUTPUT);
    }

    @Override
    public void getInputInfoByIdSuccess(CommonProductInfoBean bean) {
        mData = bean;
        mData.lrfs = "2";

        initData();

        mList.clear();
        mList.addAll(mData.gfHnJyqyTzRkFwxxes);
        adapter.notifyDataSetChanged();
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

    CommonProductInfoBean.GFHnJyqyTzRkFwxxes deleteBean = new CommonProductInfoBean.GFHnJyqyTzRkFwxxes();

    @Override
    public void deleteFwxxSuccess(String result) {
        updateTotalWeight(deleteBean);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void saveCkFwxxSuccess(Object result) {
        String json = GsonUtil.GsonToString(result);
        List<CommonProductInfoBean.GFHnJyqyTzRkFwxxes> data = GsonUtil.JsonToList(json, CommonProductInfoBean.GFHnJyqyTzRkFwxxes.class);
        if (data.size() != 0) {
            mList.addAll(data);
            adapter.notifyDataSetChanged();
            scanCodeUtil.scanCode();
        } else {
            toastShow("关联失败！");
        }
    }

    /**
     * 更新关联后的入库量
     */
    private void updateTotalWeight() {
        et_jldw.setText("吨");
        setJldw(et_jldw.getText().toString());
//        String totalWeight = et_item6.getText().toString();
//
//        if (TextUtils.isEmpty(totalWeight)) {
//            totalWeight = "0";
//        }
//
        String decimalWeight = "0";
        for (int i = 0; i < mList.size(); i++) {
            //千克转吨
            String ckl = Constants.kgToTon(mList.get(i).jldw, mList.get(i).sl);
            decimalWeight = new BigDecimal(decimalWeight).add(new BigDecimal(ckl)).toString();
        }

        et_item6.setText(decimalWeight);

    }

    /**
     * 更新删除后的入库量
     */
    private void updateTotalWeight(CommonProductInfoBean.GFHnJyqyTzRkFwxxes bean) {
        String totalWeight = et_item6.getText().toString();

        String decimalWeight;
        String ckl = Constants.kgToTon(bean.jldw, bean.sl);
        decimalWeight = new BigDecimal(totalWeight).subtract(new BigDecimal(ckl)).toString();
        if (Double.parseDouble(decimalWeight) < 0) {
            decimalWeight = "0";
        }
        et_item6.setText(decimalWeight);
    }

    @Override
    public void showError(String msg) {
        enableSave = true;
        toastShow(msg);
    }
}
