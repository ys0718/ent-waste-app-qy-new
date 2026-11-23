package com.grkj.ytjqy_pad.mvp.view.activity.hn;

import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.CshjBean;
import com.grkj.ytjqy_pad.mvp.model.FwlbBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ProductDeviceBean;
import com.grkj.ytjqy_pad.mvp.model.TareListBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.model.ZyldBean;
import com.grkj.ytjqy_pad.mvp.presenter.CRCInfoPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.MulPrintListActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.SelectWasteByZcssListActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.TareListActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.printer2.NewPrintLabelActivity;
import com.grkj.ytjqy_pad.utils.common.AppUtils;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.model.Select;
import com.grkj.ytjqy_pad.utils.view.GrAddNumberView;
import com.grkj.ytjqy_pad.utils.view.GrFormParentView;
import com.grkj.ytjqy_pad.utils.view.GrInputView;
import com.grkj.ytjqy_pad.utils.view.MyLinearLayout;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2023/12/08.
 * 小微企业产入出登记信息
 */

public class NewCRCInfoActivity extends BaseActivity<CRCInfoPresenter> implements CRCInfoPresenter.View {

    private CommonProductInfoBean mData = new CommonProductInfoBean();

    //生成标签
    @BindView(R.id.ll_print)
    LinearLayout ll_print;

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


    @BindView(R.id.ll_left)
    MyLinearLayout ll_left;

    @BindView(R.id.ll_right)
    MyLinearLayout ll_right;


//    GrInputView et_item1;
//    GrInputView et_item2;
//    GrInputView et_item3;
//    GrInputView et_item4;
//    GrInputView et_item5;
//    GrInputView et_item6;
//    GrInputView et_item7;
//    GrInputView et_item8;
//    GrInputView et_item9;
//    @BindView(R.id.et_item10)
//    GrInputView et_item10;
//    @BindView(R.id.et_jldw)
//    GrInputView et_jldw;
//    GrInputView et_item11;
//    GrInputView et_item12;
//    GrInputView et_item13;
//    GrInputView et_item14;
//    GrInputView et_item15;
//    GrInputView et_item16;
//    GrInputView et_item17;
//    GrInputView et_item18;
//    GrInputView et_item19;
//    GrAddNumberView et_item20;
//    GrInputView et_item21;
//    GrInputView et_item22;
//    GrInputView et_item23;
//    GrInputView et_item24;
//    GrInputView et_item25;

    @BindView(R.id.et_item1)
    GrInputView et_item1;
    @BindView(R.id.et_item2)
    GrInputView et_item2;
    @BindView(R.id.et_item3)
    GrInputView et_item3;
    @BindView(R.id.et_item4)
    GrInputView et_item4;
    @BindView(R.id.et_item5)
    GrInputView et_item5;
    @BindView(R.id.et_item6)
    GrInputView et_item6;
    @BindView(R.id.et_item7)
    GrInputView et_item7;
    @BindView(R.id.et_item8)
    GrInputView et_item8;
    @BindView(R.id.et_item9)
    GrInputView et_item9;
    @BindView(R.id.et_item10)
    GrInputView et_item10;
    @BindView(R.id.et_jldw)
    GrInputView et_jldw;
    @BindView(R.id.et_item11)
    GrInputView et_item11;
    @BindView(R.id.et_item12)
    GrInputView et_item12;
    @BindView(R.id.et_item13)
    GrInputView et_item13;
    @BindView(R.id.et_item14)
    GrInputView et_item14;
    @BindView(R.id.et_item15)
    GrInputView et_item15;
    @BindView(R.id.et_item16)
    GrInputView et_item16;
    @BindView(R.id.et_item17)
    GrInputView et_item17;
    @BindView(R.id.et_item18)
    GrInputView et_item18;
    @BindView(R.id.et_item19)
    GrInputView et_item19;
    @BindView(R.id.et_item20)
    GrAddNumberView et_item20;
    @BindView(R.id.et_item21)
    GrInputView et_item21;
    @BindView(R.id.et_item22)
    GrInputView et_item22;
    @BindView(R.id.et_item23)
    GrInputView et_item23;
    @BindView(R.id.et_item24)
    GrInputView et_item24;
    @BindView(R.id.et_item25)
    GrInputView et_item25;


    private boolean enableSave = true;
    private ZcssBean zcssSelectBean = null;
    private FwlbBean wasteSelectBean = null;
    private CommonProductInfoBean.GfYbqyTzRkFwxx childListBean = null;
    private boolean isClickLabel = false;//点击生成标签

    private Map<String, String> zcssParams = new HashMap<>();

    private List<GrFormParentView> viewList;


    @Override
    protected int getLayout() {
        return R.layout.activity_new_crc_info;
    }

    @Override
    protected void initEventAndData() {
        mData = new CommonProductInfoBean();
        mData.ckjbr = SPUtils.get(mContext, Constants.USER_NAME, "").toString();
        mData.jldw = getJldw();
        Map<String, String> map = new HashMap<>();
        map.put("types", "废物特性,物理特性,计量单位,包装方式,出库去向,包装材质,包装规模,注意事项");
        mPresenter.getParamList(map);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("产入出登记");
    }


    private void initView() {
        et_item1 = createInputView(et_item1).newInstance("操作时间", "rksj", TextUtils.isEmpty(mData.rksj) ? DateTimeUtil.getCurrentDate() : mData.rksj);
        et_item2 = createInputView(et_item2).newInstance("贮存设施编码", "zcssbm", mData.zcssbm, true, zcssSelectList);
        et_item3 = createInputView(et_item3).newInstance("贮存设施类型", "zcsslx", mData.zcsslx);
        et_item4 = createInputView(et_item4).newInstance("废物类别", "fwdlbh", mData.fwdlbh, true, false, true);
        et_item5 = createInputView(et_item5).newInstance("废物类别名称", "fwdlmc", mData.fwdlmc);
        et_item6 = createInputView(et_item6).newInstance("废物代码", "fwxlbh", mData.fwxlbh);
        et_item7 = createInputView(et_item7).newInstance("废物描述", "fwxlmc", mData.fwxlmc);
        et_item8 = createInputView(et_item8).newInstance("废物名称", "fwxxmc", mData.fwxxmc);
        et_item9 = createInputView(et_item9).newInstance("废物形态", "wlxz", mData.wlxz, commonTransfer(mData.wlxz, "物理特性"), true, groupParamList("物理特性"));
        et_item10 = et_item10.newInstance("废物量", "rkl", mData.rkl, true, true, false);
        et_jldw = et_jldw.newInstance("计量单位", "jldw", mData.jldw, commonTransfer(mData.jldw, "计量单位"), false, groupParamList("计量单位"));
        et_jldw.hideItem();
        et_jldw.hideClearIcon();
        et_item11 = createInputView(et_item11).newInstance("危险特性", "fwtx", mData.fwtx, true, groupParamList("废物特性"));
        et_item12 = createInputView(et_item12).newInstance("主要成分", "zywrcf", mData.zywrcf, true, true, false);
        et_item13 = createInputView(et_item13).newInstance("危害成分", "wxcf", mData.wxcf, true, true, false);
        et_item14 = createInputView(et_item14).newInstance("产生危险废物设施编码", "scssbh", mData.scssbh,getString(R.string.str_record_fwlb2, mData.scssbh, mData.scssmc), true, scssSelect);
        et_item15 = createInputView(et_item15).newInstance("注意事项", "zysx", mData.zysx, false, groupParamList("注意事项"));
        et_item16 = createInputView(et_item16).newInstance("出库去向", "qx", commonTransfer(mData.qx, "出库去向"), false, groupParamList("出库去向"));
        et_item17 = createInputView(et_item17).newInstance("容器/包装类型", "rqbzlx", mData.rqbzlx, commonTransfer(mData.rqbzlx, "包装方式"), true, groupParamList("包装方式"));

        et_item18 = createInputView(et_item18).newInstance("容器/包装材质", "rqbzcz", mData.rqbzcz, commonTransfer(mData.rqbzcz, "包装材质"), true, groupParamList("包装材质"));
        et_item19 = createInputView(et_item19).newInstance("容器/包装规格", "rqbzgg", mData.rqbzgg, commonTransfer(mData.rqbzgg, "包装规模"), true, groupParamList("包装规模"));
        et_item21 = createInputView(et_item21).newInstance("贮存部门经办人", "zcjbr", mData.zcjbr, true, true, false);
        et_item22 = createInputView(et_item22).newInstance("出库部门经办人", "ckjbr", mData.ckjbr, false, userSelectList);
        et_item23 = createInputView(et_item23).newInstance("运送部门经办人", "ysjbr", mData.ysjbr, true, true, false);
        et_item24 = createInputView(et_item24).newInstance("接收单位名称", "jsdwmc", TextUtils.isEmpty(mData.jsdwmc) ? "/" : mData.jsdwmc, true, jsdwSelectList, true, true);
        et_item25 = createInputView(et_item25).newInstance("备注", "remark", mData.remark, false, true, false);
        et_item20 = createAddView(et_item20).newInstance("容器/包装数量", "rqbzsl", mData.rqbzsl, false);

        //时间选择监听
        setTimeDialogClickListener(et_item1.getTextView());

        //废物量设置只能输入带小数点数字类型
        et_item10.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        //贮存设施
        et_item2.setOnItemChangedListener(new GrInputView.OnItemChangedListener() {
            @Override
            public void itemChange(Select selectBean) {
                for (int i = 0; i < zcssList.size(); i++) {
                    if (zcssList.get(i).id.equals(selectBean.getValue())) {
                        zcssSelectBean = zcssList.get(i);
                    }
                }
                et_item2.setText(zcssSelectBean.no);
                et_item3.setText(zcssSelectBean.sslx);
                mData.zcssbm = et_item2.getKeyValue();
                mData.zcsslx = et_item3.getKeyValue();

                mData.scssbh = "";
                wasteSelectBean = new FwlbBean();
                initWasteInfo();
            }
        });

        //废物类别
        et_item4.setOnClickListener(new GrInputView.OnclickListener() {
            @Override
            public void onclick() {
                if (TextUtils.isEmpty(et_item2.getKeyValue())) {
                    ToastUtil.showToast(mContext, "请先选择贮存设备编码！");
                } else {
                    goToActivity(new Intent(mContext, SelectWasteByZcssListActivity.class)
                                    .putExtra("zcssId", zcssSelectBean.id)
                                    .putExtra("title", "选择产废信息")
                                    .putExtra("url", ApiUrl.URL_WASTE_TYPE_LIST_BY_ID)
                            , 11);
                }

            }
        });

        //产生设施点击
        et_item14.setOnItemChangedListener(new GrInputView.OnItemChangedListener() {
            @Override
            public void itemChange(Select selectBean) {
                ProductDeviceBean bean = new ProductDeviceBean();
                for (int i = 0; i < productDeviceList.size(); i++) {
                    if (productDeviceList.get(i).scssid.equals(selectBean.getValue())) {
                        bean = productDeviceList.get(i);
                    }
                }
                mData.scssbh = bean.scssbh;
                mData.scssid = bean.scssid;
                mData.scssmc = bean.scssmc;
                mData.scsslx = bean.scsslx;

                et_item14.setText(getString(R.string.str_record_fwlb2, mData.scssbh, mData.scssmc));
            }
        });

        GrFormParentView[] arr = new GrFormParentView[]{et_item1, et_item2, et_item3, et_item4, et_item5, et_item6, et_item7, et_item8, et_item9,
                et_item11, et_item12, et_item13, et_item14, et_item15, et_item16, et_item17, et_item18, et_item19, et_item20,
                et_item21, et_item22, et_item23, et_item24, et_item25};
        viewList = new ArrayList<>(Arrays.asList(arr));

        if (ll_left.getChildCount() == 0) {
            for (int i = 0; i < viewList.size(); i++) {
                if (isLandscape) {//横屏
                    if (i % 2 == 0) {
                        ll_left.addView(viewList.get(i));
                    } else {
                        ll_right.addView(viewList.get(i));
                    }
                } else {
                    ll_left.addView(viewList.get(i));
                }
            }
        }

        viewList.add(et_item10);
        hideLoading();

    }

    private void initData() {

        GrInputView[] commonSettingArr = new GrInputView[]{et_item1, et_item2, et_item3, et_item4, et_item5, et_item6, et_item7, et_item8, et_item10,
                et_item11, et_item12, et_item13, et_item14, et_item15, et_item21, et_item22, et_item23, et_item24, et_item25 };

        for (int i = 0; i < commonSettingArr.length; i++) {
            commonSettingArr[i].setTextByObj(mData);
        }

        et_item20.setResult(mData.rqbzsl);
        et_item9.setTextAndValue(commonTransfer(mData.wlxz, "物理特性"), mData.wlxz);
        et_item16.setTextAndValue(commonTransfer(mData.qx, "出库去向"), mData.qx);
        et_item17.setTextAndValue(commonTransfer(mData.rqbzlx, "包装方式"), mData.rqbzlx);
        et_item18.setTextAndValue(commonTransfer(mData.rqbzcz, "包装材质"), mData.rqbzcz);
        et_item19.setTextAndValue(commonTransfer(mData.rqbzgg, "包装规模"), mData.rqbzgg);
    }


    GrInputView createInputView(GrInputView arr) {
//        if (arr == null) {
//            arr = new GrInputView(mContext);
//        }
        return arr;
    }

    private GrAddNumberView createAddView(GrAddNumberView arr) {
        if (arr == null) {
            arr = new GrAddNumberView(mContext);
        }
        return arr;
    }

//    @BindView(R.id.scroll_view)
//    ScrollView scroll_view;


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
            }
        } else if (resultCode == 11) { //选择废物类别
            String selectBeanStr = data.getStringExtra("selectBean");
            wasteSelectBean = GsonUtil.GsonToBean(selectBeanStr, FwlbBean.class);
            initWasteInfo();
        }

    }

    //公用废物类别赋值
    private void initBaseWasteInfo() {
        mData.rqbzlx = wasteSelectBean.bzfs;
        mData.fwId = wasteSelectBean.id;
        mData.zcfwxxId = wasteSelectBean.cshjId;
        mData.fwxxId = wasteSelectBean.cshjId;
        wasteSelectBean.id = "";
        wasteSelectBean.mainid = "";
        mData = (CommonProductInfoBean) GsonUtil.fillSameFieldNameValue(wasteSelectBean, mData);
    }


    //选择/清空废物类别信息
    private void initWasteInfo() {
        initBaseWasteInfo();
        getInsertValue();
        if (TextUtils.isEmpty(wasteSelectBean.cshjId)) {
            initData();
        } else {
            mPresenter.getCshjInfo(wasteSelectBean.cshjId);
        }

    }


    private void getInsertValue() {
        mData.rksj = et_item1.getTextView().getText().toString();
        mData.qx = et_item16.getKeyValue();
        mData.jldw = et_jldw.getKeyValue();
        mData.rkl = et_item10.getTextView().getText().toString();
        mData.rqbzsl = et_item20.getNumView().getText().toString();
        mData.zcjbr = et_item21.getTextView().getText().toString();
        mData.ckjbr = et_item22.getTextView().getText().toString();
        mData.ysjbr = et_item23.getTextView().getText().toString();
        mData.jsdwmc = et_item24.getTextView().getText().toString();
    }

    @OnClick({
            R.id.ll_back, R.id.ll_save, R.id.ll_print})
    public void onViewClicked(View view) {
        switch (view.getId()) {

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


            // 保存
            case R.id.ll_save:
                onSaveClick();
                break;

            // 删除
            case R.id.ll_delete:
                mPresenter.showDeleteDialog(mData.id);
                break;


        }
    }

    private void onSaveClick() {
        getInsertValue();
        Map<String, String> params = new HashMap<>();
        int requireNum = 0;
        int passNum = 0;

        for (int i = 0; i < viewList.size(); i++) {
            if (viewList.get(i).isRequired()) {
                requireNum++;
                if (viewList.get(i).checkRequired()) {
                    passNum++;
                }
            }
            params.putAll(viewList.get(i).getModelMap());
        }

        if (requireNum != passNum) {
            ToastUtil.showToast(mContext, "请检查参数！");
        } else {
            CommonProductInfoBean newData = GsonUtil.GsonToBean(GsonUtil.GsonToString(params), CommonProductInfoBean.class);
            mData = (CommonProductInfoBean) GsonUtil.fillSameFieldNameNotEmpty(newData, mData);
            mData.dataFrom = isLandscape ? Constants.TYPE_YTJ : Constants.TYPE_SCZD;
            mData.versionFrom = AppUtils.getVersionName(mContext);
            String deviceCode = SPUtils.get(mContext, "DEVICE_CODE", "").toString();
            mData.deviceCode = deviceCode;

            KLog.e(GsonUtil.GsonToString(mData));
            showSaveDialog();
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
                CommonProductInfoBean temData = mData;
                temData.qrcodeUrl = null;
                temData.qrcodeValue = null;
                Map<String, String> map = GsonUtil.GsonToMaps(GsonUtil.GsonToString(temData));
                mPresenter.saveData(map, ApiUrl.getHnUrl(ApiUrl.URL_CRC_SAVE));
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
    protected CRCInfoPresenter createPresenter() {
        return new CRCInfoPresenter();
    }


    @Override
    public void showError(String msg) {
        enableSave = true;
        hideLoading();
        ToastUtil.showToast(mContext, msg);
    }


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
                    mPresenter.getTypeList(zcssList.get(i).id);
                    zcssSelectBean = zcssList.get(i);
                }
            }
        }

        zcssSelectList = new ArrayList<>();
        if (zcssList != null && zcssList.size() != 0) {
            for (int i = 0; i < zcssList.size(); i++) {
                ZcssBean zcssBean = zcssList.get(i);
                String name = getString(R.string.str_input_zcbm, zcssBean.no, zcssBean.sslx, zcssBean.ssmc);
                zcssSelectList.add(new Select(name, zcssBean.id));
            }
            et_item2.setDatas(zcssSelectList);
        }

        mPresenter.getUserList("");

    }


    private List<CommonProductInfoBean> wasteTypeList = new ArrayList<>();
    private List<ZcssBean> zcssList = new ArrayList<>();
    private List<Select> zcssSelectList;
    private List<UserBean> receiverList = new ArrayList<>();

    @Override
    public void getParamListSuccess(List<ParamListBean> list) {
        String json = GsonUtil.GsonToString(list);
        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);
        initView();
        mPresenter.getZcssList(zcssParams);
    }


    private List<Select> groupParamList(String type) {
        List<Select> selectPopupWindowList = new ArrayList<>();
        for (int i = 0; i < allParamList.size(); i++) {
            List<ParamListBean> childList = allParamList.get(i).childList;

            if (childList != null && childList.size() != 0 && type.equals(allParamList.get(i).type)) {
                for (int j = 0; j < childList.size(); j++) {
                    Select bean = new Select(childList.get(j).value, childList.get(j).code);
                    selectPopupWindowList.add(bean);
                }
            }
        }
        return selectPopupWindowList;
    }


    @Override
    public void saveDataSuccess(Object bean) {
//        enableSave = true;
//        mData = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), CommonProductInfoBean.class);
////        scrollToBottom(scroll_view, View.FOCUS_UP);
//
//        ToastUtil.showToast(mContext, "保存成功！");
////        et_item1.setText(mData.rkpch);
//        ll_print.setVisibility(View.VISIBLE);
//        ll_delete.setVisibility(View.VISIBLE);
//        ll_copy.setVisibility(View.VISIBLE);
        setJldw(mData.jldw);
        setResult(9);
        finish();
        goToActivity(new Intent(mContext, ProductOutputListActivity.class));
    }


    @Override
    public void getCshjInfoSuccess(Object bean) {
        CshjBean cshjBean = GsonUtil.GsonToBean(bean, CshjBean.class);
        cshjBean.rqbzlx = cshjBean.bzfs;
        cshjBean.id = "";
        mData = (CommonProductInfoBean) GsonUtil.fillSameFieldNameValue(cshjBean, mData);
        initData();
        mPresenter.getProductDeviceList(wasteSelectBean.cshjId);
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
        }
    }


    @Override
    public void getLabelInfoSuccess(Object bean) {
        LabelInfoBean data = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), LabelInfoBean.class);
        data.WLXZ = commonTransfer(data.WLXZ, "物理特性");
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
    private List<Select> jsdwSelectList = new ArrayList<>();

    @Override
    public void getReceiverListSuccess(List<UserBean> list) {
        String json = GsonUtil.GsonToString(list);
        receiverList = GsonUtil.JsonToList(json, UserBean.class);
        temFromList = receiverList;

        if (temFromList != null && temFromList.size() != 0) {
            for (int i = 0; i < temFromList.size(); i++) {
                Select bean = new Select(temFromList.get(i).jsqymc, temFromList.get(i).jsqymc);
                jsdwSelectList.add(bean);
            }
            et_item24.setDatas(jsdwSelectList);
        }
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
    }


    private List<UserBean> temUserList = new ArrayList<>();
    private List<UserBean> userList = new ArrayList<>();
    private List<Select> userSelectList = new ArrayList<>();

    @Override
    public void getUserListSuccess(List<UserBean> list) {
        String json = GsonUtil.GsonToString(list);
        userList = GsonUtil.JsonToList(json, UserBean.class);
        temUserList = userList;

        if (temUserList != null && temFromList.size() != 0) {
            for (int i = 0; i < temUserList.size(); i++) {
                Select bean = new Select(temUserList.get(i).name, temUserList.get(i).name);
                userSelectList.add(bean);
            }
            et_item22.setDatas(userSelectList);
        }

        mPresenter.getReceiverList("");

    }


    private List<ProductDeviceBean> productDeviceList = new ArrayList<>();
    private List<Select> scssSelect = new ArrayList<>();

    @Override
    public void getProductDeviceListSuccess(List<ProductDeviceBean> list) {
        String json = GsonUtil.GsonToString(list);
        productDeviceList = GsonUtil.JsonToList(json, ProductDeviceBean.class);

        scssSelect = new ArrayList<>();
        for (int i = 0; i < productDeviceList.size(); i++) {
            ProductDeviceBean scssBean = productDeviceList.get(i);
            String name = getString(R.string.str_record_fwlb2, scssBean.scssbh, scssBean.scssmc);
            scssSelect.add(new Select(name, scssBean.scssid));
        }

        if (productDeviceList != null && productDeviceList.size() != 0) {
            mData.scssbh = productDeviceList.get(0).scssbh;
            mData.scsslx = productDeviceList.get(0).scsslx;
            mData.scssid = productDeviceList.get(0).scssid;
            mData.scssmc = productDeviceList.get(0).scssmc;
        } else {
            mData.scssbh = "";
            mData.scssid = "";
            mData.scssmc = "";
            mData.scsslx = "";
        }
        et_item14.setText(getString(R.string.str_record_fwlb2, mData.scssbh, mData.scssmc));
        et_item14.setDatas(scssSelect);
    }

    @Override
    public void checkPrintableSuccess(String result) {
        goToActivity(new Intent(mContext, MulPrintListActivity.class).putExtra("TYPCH", mData.typch).putExtra("RKPCH", mData.rkpch), 19);
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


}
