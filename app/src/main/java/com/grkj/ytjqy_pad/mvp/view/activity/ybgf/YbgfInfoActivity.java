package com.grkj.ytjqy_pad.mvp.view.activity.ybgf;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.TareListBean;
import com.grkj.ytjqy_pad.mvp.model.ybgf.GfMlsGygfScheduleGroup;
import com.grkj.ytjqy_pad.mvp.model.ybgf.GfMlsGygfScheduleGroupFwxx;
import com.grkj.ytjqy_pad.mvp.model.ybgf.GfMlsGygfWeigh;
import com.grkj.ytjqy_pad.mvp.presenter.YbgfInfoPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.ProductWwlyInfoActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.ProductZxlyInfoActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.TareListActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.bluetooth.WeightBaseActivity;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2026/5/23.
 * 一般固废信息(美的)
 */

public class YbgfInfoActivity extends WeightBaseActivity<YbgfInfoPresenter> implements YbgfInfoPresenter.View {

    private GfMlsGygfWeigh mData = new GfMlsGygfWeigh();

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

    //车间
    @BindView(R.id.et_item7)
    EditText et_item7;


    //产生量
    @BindView(R.id.et_item8)
    EditText et_item8;

    //物料编码
    @BindView(R.id.et_item9)
    EditText et_item9;

    //物料描述
    @BindView(R.id.et_item10)
    EditText et_item10;


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



    private String typeName = "自行利用";
    private String typeValue = "R";
    private String dataStr;
    private boolean isGzEv;//是否为广州环境


    @Override
    protected int getLayout() {
        return R.layout.activity_ybgf_info;
    }


    @Override
    protected void initEventAndData() {
//        String environment = String.valueOf(SPUtils.get(mContext, Constants.ENVIRONMENT, ""));
//        if ("gz".equals(environment) || "dev".equals(environment)) {
//            ll_jbrsj.setVisibility(View.VISIBLE);
//            isGzEv = true;
//        }
//        dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);
//        isCopy = getIntent().getBooleanExtra(Constants.IS_COPY, false);
//        initWeight();
//        if (isCopy) {
//            isGetWeight = true;
//        }
//
//        editSpinner_15.setKey(Constants.KEY_JBRSJ);
//
        isCopy = getIntent().getBooleanExtra(Constants.IS_COPY, false);
        if (isCopy) {
            isGetWeight = true;
        }
        dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);
        initWeight();

        if (TextUtils.isEmpty(dataStr)) {
            isGetWeight = true;
            isNew = true;
            setJldw("千克");
            mData = new GfMlsGygfWeigh();
            et_item2.setText(DateTimeUtil.getCurrentDate());
            et_item17.setText("千克");
            handleReadOnly(false);
        } else {
            mData = GsonUtil.GsonToBean(dataStr, GfMlsGygfWeigh.class);

//            if ("1".equals(mData.iscl)) {//已被处理时不可保存、删除
//                ll_save.setVisibility(View.GONE);
//                ll_delete.setVisibility(View.GONE);
//                ll_handle.setVisibility(View.GONE);
//                ll_copy.setVisibility(View.VISIBLE);
//            } else {
//                ll_save.setVisibility(View.VISIBLE);
//                if (isCopy) {//复制逻辑
//                    ll_copy.setVisibility(View.GONE);
//                    ll_delete.setVisibility(View.GONE);
//                    ll_handle.setVisibility(View.GONE);
//                } else {
//                    ll_copy.setVisibility(View.VISIBLE);
////                    ll_delete.setVisibility(View.VISIBLE);
//
//                    if (DateTimeUtil.compare_date(mData.cssj, DateTimeUtil.getCurrentDate()) == 0) {//如果不是当天数据则不可删除
//                        ll_delete.setVisibility(View.VISIBLE);
//                    } else {
//                        ll_delete.setVisibility(View.GONE);
//                    }
////                    ll_handle.setVisibility(View.VISIBLE);
//                }
//            }


            if (isCopy) {//复制逻辑
                ll_copy.setVisibility(View.GONE);
                ll_delete.setVisibility(View.GONE);
                ll_handle.setVisibility(View.GONE);
            } else {
                ll_copy.setVisibility(View.VISIBLE);
                ll_delete.setVisibility(View.VISIBLE);
            }

            if (!isCopy) {
                handleReadOnly(true);
            }
            initView();
        }

        mPresenter.getProductDeviceList();

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
        et_item1.setText(mData.serialNum);
        et_item2.setText(mData.carWeightTime);
        et_item3.setText(mData.fwdlbh);
        et_item4.setText(mData.fwxlbh);
        et_item5.setText(mData.fwxxmc);
        et_item7.setText(mData.cjmc);
        et_item8.setText(mData.netWeight);
        et_item9.setText(mData.itemCode);
        et_item10.setText(mData.itemDesc);
        tv_weight1.setText(mData.netWeight);
        tv_weight2.setText(mData.containerWeight);
        tv_weight3.setText(mData.weighValue);
    }


    @OnClick({R.id.et_item3,   R.id.ll_handle, R.id.ll_copy, R.id.et_item7,
              R.id.ll_back, R.id.ll_save, R.id.ll_delete, R.id.ll_clear, R.id.ll_qp
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //废物类别
            case R.id.et_item3:
                showFwlbPopWindow();
                break;


//            //计量单位
//            case R.id.et_item17:
//                showPopWindow("计量单位", et_item17, false);
//                break;


            //选择车间
            case R.id.et_item7:
                showCsssPopWindow();
                break;


            // 返回
            case R.id.ll_back:
                finish();
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
                mData.netWeight = et_item8.getText().toString();
                mData.jldw = et_item17.getText().toString();
                mData.carWeightTime = et_item2.getText().toString();
                mData.containerWeight = tv_weight2.getText().toString();
                mData.weighValue = tv_weight3.getText().toString();

                if(!TextUtils.isEmpty(mData.weighValue) && Double.valueOf(mData.weighValue) > 0){ //判断皮重的值是否大于0
                    mData.weighType = "去皮重";
                }else {
                    mData.weighType = "无皮重";
                }

//                String deviceCode = SPUtils.get(mContext, "DEVICE_CODE", "").toString();
//                mData.deviceCode = deviceCode;
//
//                if (isNew) {
//                    mData.bqlx = getBqlxFromDevice();
//                }
//
                if (TextUtils.isEmpty(mData.dataFrom)) {//如果数据来源为空，则根据横竖屏设置
                    mData.dataFrom = isLandscape ? Constants.TYPE_YTJ : Constants.TYPE_SCZD;
                }
//
//                if (TextUtils.isEmpty(mData.versionFrom)) {//如果版本来源为空，则获取版本信息
//                    mData.versionFrom = AppUtils.getVersionName(mContext);
//                }


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
                GfMlsGygfWeigh copyData = GsonUtil.GsonToBean(mDataStr, GfMlsGygfWeigh.class);
                copyData.id = "";
                copyData.serialNum = "";
                copyData.weighValue = "";
                setResult(9);
                finish();
                String copyStr = GsonUtil.GsonToString(copyData);
                startActivityForResult(new Intent(mContext, YbgfInfoActivity.class)
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
        if (TextUtils.isEmpty(mData.netWeight)) {
            return "请输入/地磅获取产生量！";
        } else if (Double.valueOf(mData.netWeight) <= 0) {
            return "请输入/地磅获取正确的产生量！";
        }else if (TextUtils.isEmpty(mData.cjid)) {
            return "请选择车间！";
        } else if (TextUtils.isEmpty(mData.fwdlbh)) {
            return "请选择废物类别！";
        }else {
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
                KLog.e(GsonUtil.GsonToString(mData));
                mPresenter.saveData(map, ApiUrl.URL_YBGF_SAVE);
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
    protected YbgfInfoPresenter createPresenter() {
        return new YbgfInfoPresenter();
    }


    @Override
    public void showError(String msg) {
        ToastUtil.showToast(mContext, msg);
    }



    @Override
    public void getProductDeviceListSuccess(List<GfMlsGygfScheduleGroup> list) {

        productDeviceList = list;
        
//        String json = GsonUtil.GsonToString(list);
//        productDeviceList = GsonUtil.JsonToList(json, GfMlsGygfScheduleGroup.class);

//        if (isNew) {
//            if (productDeviceList != null && productDeviceList.size() != 0) {
//                mData.scssbh = productDeviceList.get(0).scssbh;
//                mData.scsslx = productDeviceList.get(0).scsslx;
//                mData.scssid = productDeviceList.get(0).scssid;
//                mData.scssmc = productDeviceList.get(0).scssmc;
//            } else {
//                mData.scssbh = "";
//                mData.scssid = "";
//                mData.scssmc = "";
//                mData.scsslx = "";
//            }
//
//            et_item7.setText(getString(R.string.str_record_fwlb2, mData.scssbh, mData.scssmc));
////            et_item7.setText(mData.scssbh);
//        }

    }




    private List<GfMlsGygfScheduleGroupFwxx> wasteTypeList = new ArrayList<>();
    private List<GfMlsGygfScheduleGroup> productDeviceList = new ArrayList<>();




    @Override
    public void saveDataSuccess(Object bean) {
//        ToastUtil.showToast(mContext, "保存成功！");
//        setResult(9);
//        finish();
        handleReadOnly(true);
        mData = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), GfMlsGygfWeigh.class);
        ToastUtil.showToast(mContext, "保存成功！");
        et_item1.setText(mData.serialNum);
        setResult(9);
        ll_save.setVisibility(View.VISIBLE);
        ll_copy.setVisibility(View.VISIBLE);
        ll_delete.setVisibility(View.VISIBLE);
    }

    @Override
    public void deleteDataSuccess(Object bean) {
        ToastUtil.showToast(mContext, "删除成功！");
        setResult(9);
        finish();
    }


    private SelectPopupWindow selectPopupWindow;

    //初始化选择地址下拉弹窗
    private void showPopWindow(String type, EditText item, boolean isMulti) {
//        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();
//
//        for (int i = 0; i < allParamList.size(); i++) {
//            List<ParamListBean> childList = allParamList.get(i).childList;
//
//            if (childList != null && childList.size() != 0 && type.equals(allParamList.get(i).type)) {
//                for (int j = 0; j < childList.size(); j++) {
//                    selectPopupWindowList.add(new SelectPopupWindowBean(childList.get(j).code, childList.get(j).value));
//                }
//            }
//        }
//
//            selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
//                @Override
//                public void onDo(SelectPopupWindowBean model, String mType) {
//                    item.setText(model.NAME);
//                    switch (mType) {
//
//                        case "产废去向":
//                            mData.qx = model.ID;
//                            break;
//
//                        case "包装材质":
//                            mData.rqbzcz = model.ID;
//                            groupBzbm();
//                            break;
//
//                        case "包装规模":
//                            mData.rqbzgg = model.ID;
//                            groupBzbm();
//                            break;
//                        case "计量单位":
//                            mData.jldw = model.ID;
//                            changeUnit(mData.jldw);
//                            break;
//                    }
//
//
//                    selectPopupWindow.dismiss();
//                }
//            });
//
//
//        int sort = 5;
//        if (selectPopupWindowList.size() < 4) {
//            sort = selectPopupWindowList.size() + 1;
//        }
//
//        selectPopupWindow.show(mLayout, type, (View) item.getParent(), sort);

    }



    //初始废物类别下拉弹窗
    private void showFwlbPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < wasteTypeList.size(); i++) {
            GfMlsGygfScheduleGroupFwxx mData = wasteTypeList.get(i);
//            String name = getString(R.string.str_record_fwlb2, mData.fwxxmc, mData.fwxlbh);
            String name = mData.fwxxmc;
            selectPopupWindowList.add(new SelectPopupWindowBean(wasteTypeList.get(i).id, name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                GfMlsGygfScheduleGroupFwxx bean = new GfMlsGygfScheduleGroupFwxx();
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
        selectPopupWindow.show(mLayout, "", (View) et_item3.getParent(), sort);
    }

    //初始产生设施下拉弹窗
    private void showCsssPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < productDeviceList.size(); i++) {
            GfMlsGygfScheduleGroup mData = productDeviceList.get(i);
            selectPopupWindowList.add(new SelectPopupWindowBean(mData.id, mData.cjmc));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                GfMlsGygfScheduleGroup bean = new GfMlsGygfScheduleGroup();
                for (int i = 0; i < productDeviceList.size(); i++) {
                    if (productDeviceList.get(i).id.equals(model.ID)) {
                        bean = productDeviceList.get(i);
                    }
                }
                wasteTypeList = bean.fwxxList;
                mData.cjmc = model.NAME;
                mData.cjid = model.ID;
                et_item7.setText(model.NAME);

                //重置废物类别
                initWasteInfo(new GfMlsGygfScheduleGroupFwxx());
                selectPopupWindow.dismiss();
            }
        });

        int sort = 3;
        selectPopupWindow.show(mLayout, "", (View) et_item7.getParent(), sort);
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
//                groupBzbm();
            }

        }

    }

    //选择/清空废物类别信息
    private void initWasteInfo(GfMlsGygfScheduleGroupFwxx bean) {
        et_item3.setText(bean.fwdlbh);
        et_item4.setText(bean.fwxlbh);
        et_item5.setText(bean.fwxxmc);
        et_item9.setText(bean.wlbm);
        et_item10.setText(bean.wlms);

        mData.fwdlbh = bean.fwdlbh;
        mData.fwxxmc = bean.fwxxmc;
        mData.fwxlbh = bean.fwxlbh;
        mData.fwxlmc = bean.fwxlmc;
        mData.fwdlmc = bean.fwdlmc;
        mData.itemCode = bean.wlbm;
        mData.itemDesc = bean.wlms;
        mData.fwid = bean.id;

    }


}
