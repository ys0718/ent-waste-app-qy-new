package com.grkj.ytjqy_pad.mvp.view.activity;

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
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ProductDeviceBean;
import com.grkj.ytjqy_pad.mvp.model.TareListBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.presenter.ProductRecordInfoPresenter;
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
 * 产废登记信息
 */

public class ProductRecordInfoActivity extends WeightBaseActivity<ProductRecordInfoPresenter> implements ProductRecordInfoPresenter.View {

    private CommonProductInfoBean mData = new CommonProductInfoBean();

    //批次号
    @BindView(R.id.et_item1)
    EditText et_item1;

    //产生时间
    @BindView(R.id.et_item2)
    EditText et_item2;

    //废物类别
    @BindView(R.id.et_item3)
    EditText et_item3;

    //废物代码
    @BindView(R.id.et_item4)
    EditText et_item4;

    //废物名称
    @BindView(R.id.et_item5)
    EditText et_item5;

    //物理特性
    @BindView(R.id.et_item6)
    EditText et_item6;

    //废物特性
    @BindView(R.id.et_item7)
    EditText et_item7;

    //产生量
    @BindView(R.id.et_item8)
    EditText et_item8;

    //容器/包装编码
    @BindView(R.id.et_item9)
    EditText et_item9;

    //产废去向
    @BindView(R.id.et_item10)
    EditText et_item10;

    //容器/包装类型
    @BindView(R.id.et_item11)
    EditText et_item11;

    //容器/包装数量
    @BindView(R.id.add_view)
    AddNumberView et_item12;

    //产生危险废物设施编码
    @BindView(R.id.et_item13)
    EditText et_item13;

    //产生部门经办人
    @BindView(R.id.editSpinner_14)
    SearchSpinner editSpinner_14;

    //产生部门经办人手机
    @BindView(R.id.editSpinner_15)
    HistorySearchSpinner editSpinner_15;

    //包装类型
    @BindView(R.id.et_item15)
    EditText et_item15;

    //包装规模
    @BindView(R.id.et_item16)
    EditText et_item16;

    //计量单位
    @BindView(R.id.et_item17)
    EditText et_item17;

    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //删除按钮
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;

    //自行/委外按钮
    @BindView(R.id.ll_handle)
    LinearLayout ll_handle;

    //自行/委外文字
    @BindView(R.id.tv_handle)
    TextView tv_handle;

    //保存按钮
    @BindView(R.id.ll_save)
    LinearLayout ll_save;

    //复制按钮
    @BindView(R.id.ll_copy)
    LinearLayout ll_copy;

    //生成标签
    @BindView(R.id.ll_print)
    LinearLayout ll_print;

    //经办人手机号
    @BindView(R.id.ll_jbrsj)
    LinearLayout ll_jbrsj;


    private String typeName = "自行利用";
    private String typeValue = "R";
    private String dataStr;
    private boolean isGzEv;//是否为广州环境


    @Override
    protected int getLayout() {
        return R.layout.activity_common_info;
    }


    @Override
    protected void initEventAndData() {
        String environment = String.valueOf(SPUtils.get(mContext, Constants.ENVIRONMENT, ""));
        if ("gz".equals(environment) || "dev".equals(environment)) {
            ll_jbrsj.setVisibility(View.VISIBLE);
            isGzEv = true;
        }
        dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);
        isCopy = getIntent().getBooleanExtra(Constants.IS_COPY, false);
        initWeight();
        if (isCopy) {
            isGetWeight = true;
        }



        if (TextUtils.isEmpty(dataStr)) {
            isGetWeight = true;
            isNew = true;
            mData = new CommonProductInfoBean();
            et_item2.setText(DateTimeUtil.getCurrentDate());
            editSpinner_14.setText(SPUtils.get(mContext, Constants.USER_NAME, "").toString());
            mData.jldw = getJldw();
            et_item17.setText(mData.jldw);
            handleReadOnly(false);
            editSpinner_15.setDefaultText();

        } else {
            mData = GsonUtil.GsonToBean(dataStr, CommonProductInfoBean.class);

            if ("1".equals(mData.iscl)) {//已被处理时不可保存、删除
                ll_save.setVisibility(View.GONE);
                ll_delete.setVisibility(View.GONE);
                ll_handle.setVisibility(View.GONE);
                ll_print.setVisibility(View.VISIBLE);
                ll_copy.setVisibility(View.VISIBLE);
            } else {
                ll_save.setVisibility(View.VISIBLE);
                if (isCopy) {//复制逻辑
                    ll_copy.setVisibility(View.GONE);
                    ll_print.setVisibility(View.GONE);
                    ll_delete.setVisibility(View.GONE);
                    ll_handle.setVisibility(View.GONE);
                } else {
                    ll_copy.setVisibility(View.VISIBLE);
                    ll_print.setVisibility(View.VISIBLE);
//                    ll_delete.setVisibility(View.VISIBLE);

                    if (DateTimeUtil.compare_date(mData.cssj, DateTimeUtil.getCurrentDate()) == 0) {//如果不是当天数据则不可删除
                        ll_delete.setVisibility(View.VISIBLE);
                    } else {
                        ll_delete.setVisibility(View.GONE);
                    }
//                    ll_handle.setVisibility(View.VISIBLE);
                }
            }

            if (!isCopy) {
                handleReadOnly(true);
            }

            mPresenter.getProductDeviceList(mData.fwId);

            initView();
        }

        mPresenter.getTypeList();
        mPresenter.getUserList("");
        editSpinner_15.setKey(Constants.KEY_ZCJBRSJ);

        Map<String, String> map = new HashMap<>();
        map.put("types", "废物特性,物理特性,包装方式,计量单位,处理方式及去向,包装材质,包装规模,产废去向");
        mPresenter.getParamList(map);

    }


    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();

    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("产废登记信息");
    }

    private void initView() {
        et_item1.setText(mData.cspch);
        et_item2.setText(mData.cssj);
        et_item3.setText(mData.fwdlbh);
        et_item4.setText(mData.fwxlbh);
        et_item5.setText(mData.fwxxmc);
        et_item7.setText(mData.fwtx);
        et_item8.setText(mData.csl);
        et_item9.setText(mData.rqbzbm);
        et_item10.setText(commonTransfer(mData.qx, "产废去向"));
        et_item12.setResult(mData.rqbzsl);
        et_item13.setText(getString(R.string.str_record_fwlb2, mData.scssbh, mData.scssmc));
        editSpinner_14.setText(mData.jbr);
        editSpinner_15.setText(mData.jbrsj);
        et_item15.setText(commonTransfer(mData.rqbzcz, "包装材质"));
        et_item16.setText(commonTransfer(mData.rqbzgg, "包装规模"));
        et_item11.setText(commonTransfer(mData.rqbzlx, "包装方式"));
        et_item6.setText(commonTransfer(mData.wlxz, "物理特性"));
        tv_handle.setText(commonTransfer(mData.qx, "产废去向"));
        et_item17.setText(TextUtils.isEmpty(mData.jldw) ? getJldw() : mData.jldw);
    }


    @OnClick({R.id.et_item3, R.id.et_item13, R.id.et_item6, R.id.et_item7, R.id.et_item10, R.id.ll_handle, R.id.ll_copy,
            R.id.et_item15, R.id.et_item16, R.id.et_item17, R.id.ll_back, R.id.ll_save, R.id.ll_delete, R.id.ll_print, R.id.ll_clear, R.id.ll_qp
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //废物类别
            case R.id.et_item3:
                showFwlbPopWindow();
                break;

            //产生设施
            case R.id.et_item13:
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

            //产废去向
            case R.id.et_item10:
                showPopWindow("产废去向", et_item10, false);
                break;

//            //产生部门经办人
//            case R.id.editSpinner_14:
//                showUserPopWindow();
//                break;

            //包装材质
            case R.id.et_item15:
                showPopWindow("包装材质", et_item15, false);
                break;

            //包装规模
            case R.id.et_item16:
                showPopWindow("包装规模", et_item16, false);
                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;

            // 生成标签
            case R.id.ll_print:
                mPresenter.checkPrintable(mData.id);
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
                mData.cssj = et_item2.getText().toString();
                mData.rqbzbm = et_item9.getText().toString();
                mData.csl = et_item8.getText().toString();
                mData.jbr = editSpinner_14.getText().toString();
                mData.jbrsj = editSpinner_15.getText().toString();
                mData.rqbzsl = et_item12.getResultString();
                mData.jldw = et_item17.getText().toString();
                mData.lrfs = isWeight ? "1" : "";
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

                break;

            // 删除
            case R.id.ll_delete:
                showDeleteDialog();
                break;

            //利用/处置
            case R.id.ll_handle:
                Class temActivity = ProductZxlyInfoActivity.class;
                if (typeName.contains("委外")) {
                    temActivity = ProductWwlyInfoActivity.class;
                }
                startActivityForResult(new Intent(mContext, temActivity)
                        .putExtra("typeValue", typeValue)
                        .putExtra("typeName", typeName)
                        .putExtra("cfdjStr", GsonUtil.GsonToString(mData)), 11);
                break;


            //复制
            case R.id.ll_copy:
                String mDataStr = GsonUtil.GsonToString(mData);
                CommonProductInfoBean copyData = GsonUtil.GsonToBean(mDataStr, CommonProductInfoBean.class);
                copyData.id = "";
                copyData.iscl = "";
                copyData.cspch = "";
                copyData.csl = "";
                copyData.cssj = DateTimeUtil.getCurrentDate();
                setResult(9);
                finish();
                String copyStr = GsonUtil.GsonToString(copyData);
                startActivityForResult(new Intent(mContext, ProductRecordInfoActivity.class)
                        .putExtra(Constants.IS_COPY, true)
                        .putExtra("isWeight", isWeight)
                        .putExtra(Constants.JUMP_KEY, copyStr), 9);

                break;


        }
    }

    /*
     * 判空处理
     * */
    private String checkEmpty() {
        if (TextUtils.isEmpty(mData.csl)) {
            return "请输入/地磅获取产生量！";
        } else if (Double.valueOf(mData.csl) <= 0) {
            return "请输入/地磅获取正确的产生量！";
        } else if (TextUtils.isEmpty(mData.fwdlbh)) {
            return "请选择废物类别！";
        } else if (TextUtils.isEmpty(mData.rqbzcz)) {
            return "请选择容器/包装材质！";
        } else if (TextUtils.isEmpty(mData.rqbzgg)) {
            return "请选择容器/包装规格！";
        } else if (TextUtils.isEmpty(mData.rqbzsl) || Double.valueOf(mData.rqbzsl) <= 0) {
            return "请输入容器/包装数量！";
        } else if (TextUtils.isEmpty(mData.qx)) {
            return "请选择去向！";
        } else if (TextUtils.isEmpty(mData.scssbh)) {
            return "请输入产生危险设施编码！";
        }else if (TextUtils.isEmpty(mData.jbr)) {
            return "请输入产生部门经办人！";
        }else if (TextUtils.isEmpty(mData.jbrsj) && isGzEv) {
            return "请输入产生部门经办人手机！";
        } else {
            return "";
        }
    }


    //确认保存
    private void showSaveDialog() {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确定保存？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
//                mData.gfYbqyTzCkZyFwxx=null;
//                mData.gfYbqyTzRkFwxx=null;
                Map<String, String> map = GsonUtil.GsonToMaps(GsonUtil.GsonToString(mData));
//                map = removeNullString(map);
                mPresenter.saveData(map, ApiUrl.URL_PRODUCT_RECORD_SAVE);
                roundCornerDialog.dismiss();
            }

            @Override
            public void onCancelClick() {
            }
        });
        //显示对话框
        roundCornerDialog.show();
    }


    /*
     * 确认删除
     * */
    private void showDeleteDialog() {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确定删除？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                mPresenter.deleteData(mData.id);
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
    protected ProductRecordInfoPresenter createPresenter() {
        return new ProductRecordInfoPresenter();
    }


    @Override
    public void showError(String msg) {
        ToastUtil.showToast(mContext, msg);
    }


    @Override
    public void getTypeListSuccess(List<CommonProductInfoBean> list) {
        String json = GsonUtil.GsonToString(list);
        wasteTypeList = GsonUtil.JsonToList(json, CommonProductInfoBean.class);

    }

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
            } else {
                mData.scssbh = "";
                mData.scssid = "";
                mData.scssmc = "";
                mData.scsslx = "";
            }

            et_item13.setText(getString(R.string.str_record_fwlb2, mData.scssbh, mData.scssmc));
//            et_item13.setText(mData.scssbh);
        }

    }


    private List<UserBean> temFromList = new ArrayList<>();

    @Override
    public void getUserListSuccess(List<UserBean> list) {
        String json = GsonUtil.GsonToString(list);
        userList = GsonUtil.JsonToList(json, UserBean.class);
        temFromList = userList;

        if (temFromList.size() == 0) {
            editSpinner_14.setHint("请输入");
            editSpinner_14.setText(SPUtils.get(mContext, Constants.USER_NAME, "").toString());
            return;
        }

        List<Select> selectList = new ArrayList<>();
        for (int i = 0; i < temFromList.size(); i++) {
            Select bean = new Select(temFromList.get(i).name, temFromList.get(i).name);
            selectList.add(bean);
        }
        editSpinner_14.setItemData(selectList);
        editSpinner_14.setDisabled(false);
        if (!TextUtils.isEmpty(mData.jbr)) {
            editSpinner_14.setText(mData.jbr);
        }
        editSpinner_14.setOnInsertListener(new SearchSpinner.onInsertListener() {
            @Override
            public void insertChange(String name, String value, boolean isFromSelect) {
                mData.jbr = name;
//                if (!isFromSelect) {//如果是输入或者是为空的时候筛选
//                    mPresenter.getUserList(name);
//                }
            }
        });

    }

    private List<CommonProductInfoBean> wasteTypeList = new ArrayList<>();
    private List<ProductDeviceBean> productDeviceList = new ArrayList<>();
    private List<UserBean> userList = new ArrayList<>();


    @Override
    public void getParamListSuccess(List<ParamListBean> list) {
        String json = GsonUtil.GsonToString(list);

        KLog.e(list.size());

        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);

        if (!isCopy) {
            initTypeByQx();
        }


        if (!TextUtils.isEmpty(dataStr)) {
            initView();
        } else {
            setDefaultQx();
        }
    }

    /**
     * 设置默认去向
     */
    private void setDefaultQx() {
        for (int i = 0; i < allParamList.size(); i++) {
            ParamListBean bean = allParamList.get(i);
            if (bean.type.equals("产废去向")) {
                for (int j = 0; j < bean.childList.size(); j++) {
                    ParamListBean childBean = bean.childList.get(j);
                    if ("贮存入库".equals(childBean.value)) {
                        mData.qx = childBean.code;
                        et_item10.setText(commonTransfer(mData.qx, "产废去向"));
                    }
                }
            }
        }
    }


    @Override
    public void saveDataSuccess(Object bean) {
//        ToastUtil.showToast(mContext, "保存成功！");
//        setResult(9);
//        finish();
        setJldw(mData.jldw);
        handleReadOnly(true);
        mData = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), CommonProductInfoBean.class);
        ToastUtil.showToast(mContext, "保存成功！");
        et_item1.setText(mData.cspch);
        setResult(9);
        ll_print.setVisibility(View.VISIBLE);
        ll_save.setVisibility(View.VISIBLE);
        ll_copy.setVisibility(View.VISIBLE);
        ll_delete.setVisibility(View.VISIBLE);
        initTypeByQx();
    }

    //根据去向初始化自行/委外类型
    private void initTypeByQx() {
        String qxName = commonTransfer(mData.qx, "产废去向");
        if (qxName.contains("自行") || qxName.contains("委外")) {
            tv_handle.setText(qxName);
            typeName = qxName;
            typeValue = qxName.contains("利用") ? "R" : "D";
//            ll_handle.setVisibility("1".equals(mData.iscl) ? View.GONE : View.VISIBLE);
        } else {
            ll_handle.setVisibility(View.GONE);
        }
    }

    @Override
    public void deleteDataSuccess(Object bean) {
        ToastUtil.showToast(mContext, "删除成功！");
        setResult(9);
        finish();
    }

    @Override
    public void getLabelInfoSuccess(Object bean) {
        LabelInfoBean data = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), LabelInfoBean.class);
        data.WLXZ = commonTransfer(data.WLXZ, "物理特性");
        startActivityForResult(new Intent(mContext, NewPrintLabelActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(data)), 10);
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
                    switch (mType) {

                        case "产废去向":
                            mData.qx = model.ID;
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

    /*
     * 拼接包装编码
     * */
    private void groupBzbm() {

        if (!TextUtils.isEmpty(mData.rqbzlx) && !TextUtils.isEmpty(mData.rqbzcz) && !TextUtils.isEmpty(mData.rqbzgg)) {
            mData.rqbzbm = mData.rqbzgg + mData.rqbzcz + mData.rqbzlx;
            et_item9.setText(mData.rqbzbm);
        }

    }


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
                mPresenter.getProductDeviceList(bean.id);
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item3.getParent(), sort);
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
                et_item13.setText(getString(R.string.str_record_fwlb2, mData.scssbh, mData.scssmc));
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item13.getParent(), sort);
    }


    private TareListBean tareSelectBean = new TareListBean();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 9) {
            if (requestCode == 9) {//复制
                setResult(9);
                finish();
            } else if (requestCode == 11) {//跳转自行/委外利用处置回调
                ll_save.setVisibility(View.GONE);
                ll_handle.setVisibility(View.GONE);
                ll_delete.setVisibility(View.GONE);
            } else {//选择皮重
                String selectBeanStr = data.getStringExtra("selectBean");
                tareSelectBean = GsonUtil.GsonToBean(selectBeanStr, TareListBean.class);
                tv_weight3.setText(tareSelectBean.pzl);
//                initWasteInfo(new CommonProductInfoBean());

//                editSpinner_14.setText(tareSelectBean.zcsslx);
//                et_item3.setText(tareSelectBean.fwdlbh);
//                et_item6.setText(tareSelectBean.fwdlmc);
//                et_item4.setText(tareSelectBean.fwxlbh);
//                et_item5.setText(tareSelectBean.fwxxmc);
//
//                mData.fwdlbh = tareSelectBean.fwdlbh;
//                mData.fwxxmc = tareSelectBean.fwxxmc;
//                mData.fwxlbh = tareSelectBean.fwxlbh;
//                mData.fwxlmc = tareSelectBean.fwxlmc;
//                mData.fwdlmc = tareSelectBean.fwdlmc;
//                mData.fwId = tareSelectBean.fwId;
//
//                mData.rqbzlx = tareSelectBean.rqbzlx;
//                mData.rqbzcz = tareSelectBean.rqbzcz;
//                mData.rqbzgg = tareSelectBean.rqbzgg;
//                et_item15.setText(commonTransfer(tareSelectBean.rqbzcz,"包装材质"));
//                et_item16.setText(commonTransfer(tareSelectBean.rqbzgg,"包装规模"));
//                et_item11.setText(commonTransfer(tareSelectBean.rqbzlx,"包装方式"));
//                groupBzbm();
            }

        }

    }

    //选择/清空废物类别信息
    private void initWasteInfo(CommonProductInfoBean bean) {
        et_item3.setText(bean.fwdlbh);
        et_item4.setText(bean.fwxlbh);
        et_item5.setText(bean.fwxxmc);
        et_item7.setText(bean.fwtx);

        mData.fwdlbh = bean.fwdlbh;
        mData.fwxxmc = bean.fwxxmc;
        mData.fwtx = bean.fwtx;
        mData.hftx = bean.hftx;
        mData.wlxz = bean.wlxz;
        mData.rqbzlx = bean.bzfs;
        mData.fwxlbh = bean.fwxlbh;
        mData.fwxlmc = bean.fwxlmc;
        mData.fwdlmc = bean.fwdlmc;
        mData.fwId = bean.id;

        mData.mlnf = bean.mlnf;
        mData.wxcf = bean.wxcf;
        mData.remark = bean.remark;
        mData.zysx = bean.zysx;
        mData.zywrcf = bean.zywrcf;


        mData.rqbzbm = bean.rqbzbm;
        mData.rqbzcz = bean.rqbzcz;
        mData.rqbzgg = bean.rqbzgg;

        et_item15.setText(commonTransfer(mData.rqbzcz, "包装材质"));
        et_item16.setText(commonTransfer(mData.rqbzgg, "包装规模"));
        et_item11.setText(commonTransfer(bean.bzfs, "包装方式"));
        et_item6.setText(commonTransfer(bean.wlxz, "物理特性"));
        groupBzbm();
    }


}
