package com.grkj.ytjqy_pad.mvp.view.activity.jytz;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.event.ChangeEvent;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.RxBus;
import com.grkj.ytjqy_pad.mvp.model.CollectInfoChildBean;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.JytzLyczBean;
import com.grkj.ytjqy_pad.mvp.model.JytzLyczBean.ZxlyFwListBean;
import com.grkj.ytjqy_pad.mvp.model.GfYbqyXxbaXkzxxBean;
import com.grkj.ytjqy_pad.mvp.model.JytzLyczBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.JytzLyczBean;
import com.grkj.ytjqy_pad.mvp.model.ZxlyssBean;
import com.grkj.ytjqy_pad.mvp.presenter.JytzLyczInfoPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.SelectMultiWasteListActivity;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.view.AddNumberView;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;
import com.socks.library.KLog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2026/6/24.
 * 经营台账-利用处置信息
 */

public class JytzLyczInfoActivity extends BaseActivity<JytzLyczInfoPresenter> implements JytzLyczInfoPresenter.View {

    private JytzLyczBean mData = new JytzLyczBean();

    //批次号
    @BindView(R.id.et_item1)
    EditText et_item1;

    //入库时间
    @BindView(R.id.et_item2)
    EditText et_item2;

    //许可证废物类别
    @BindView(R.id.et_xkzfwlb)
    EditText et_xkzfwlb;


    //废物类别
    @BindView(R.id.et_item3)
    EditText et_item3;

    //废物类别名称
    @BindView(R.id.et_item4)
    EditText et_item4;

    //自行处置完毕时间
    @BindView(R.id.et_item5)
    EditText et_item5;


    //关联许可证
    @BindView(R.id.et_glxkz)
    EditText et_glxkz;

    //废物代码
    @BindView(R.id.et_item6)
    EditText et_item6;

    //废物名称
    @BindView(R.id.et_item7)
    EditText et_item7;

    //自行利用量
    @BindView(R.id.et_item8)
    EditText et_item8;

    //容器/包装编码
    @BindView(R.id.et_item9)
    EditText et_item9;

    //产生批次号
    @BindView(R.id.et_item10)
    EditText et_item10;

    //容器/包装类型
    @BindView(R.id.et_item11)
    EditText et_item11;

    //容器/包装数量
    @BindView(R.id.add_view)
    AddNumberView et_item12;

    //处置设施编码
    @BindView(R.id.et_item13)
    EditText et_item13;

    //处置设施类型
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

    //利用处置方式大类
    @BindView(R.id.et_item18)
    EditText et_item18;

    //利用处置方式小类
    @BindView(R.id.et_item19)
    EditText et_item19;

    //出库部门经办人
    @BindView(R.id.et_item20)
    EditText et_item20;

    //自行利用部门经办人
    @BindView(R.id.et_item21)
    EditText et_item21;

    //运输部门经办人
    @BindView(R.id.et_item22)
    EditText et_item22;

    //自行利用处置类型
    @BindView(R.id.et_item23)
    EditText et_item23;

    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //删除按钮
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;

    //关联产废/出库信息
    @BindView(R.id.ll_get_cfxx)
    LinearLayout ll_get_cfxx;

    //保存
    @BindView(R.id.ll_save)
    LinearLayout ll_save;

    //是否为扫码自行利用
    private boolean isScan;
    private String rkid;

    private String typeName = "利用处置";
//    private String typeValue = "R";

    private JytzLyczBean.ZxlyFwListBean wasteSelectBean = new JytzLyczBean.ZxlyFwListBean();
    private TimePickerView timePicker;


    @Override
    protected int getLayout() {
        return R.layout.activity_jytz_czly_info;
    }

    @Override
    protected void initEventAndData() {

//        typeName = getIntent().getStringExtra("typeName");
//        typeValue = getIntent().getStringExtra("typeValue");

        initDifView();

        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);
        //产生登记跳转
        String cfdjStr = getIntent().getStringExtra("cfdjStr");

        //出库登记跳转
        String ckdjStr = getIntent().getStringExtra("ckdjStr");

        isScan = getIntent().getBooleanExtra("IS_SCAN", false);

        if (TextUtils.isEmpty(dataStr)) {
            ll_save.setVisibility(View.VISIBLE);
            //组装mData
            if (!TextUtils.isEmpty(cfdjStr)) {
                groupMainData(cfdjStr, false);
            } else if (!TextUtils.isEmpty(ckdjStr)) {
                groupMainData(ckdjStr, true);
            } else {
                mData = new JytzLyczBean();
            }

            et_item1.setHint(getString(R.string.str_auto_get));
            et_item2.setText(DateTimeUtil.getCurrentDate());
            et_item5.setText(DateTimeUtil.getCurrentDate());
            et_item21.setText(SPUtils.get(mContext, Constants.USER_NAME, "").toString());
            mData.lyczsj = et_item2.getText().toString();
            mData.lyczwbsj = et_item5.getText().toString();

        } else {
            ll_save.setVisibility(View.GONE);
            ll_delete.setVisibility(View.VISIBLE);
            mData = GsonUtil.GsonToBean(dataStr, JytzLyczBean.class);
            rkid = mData.id;
            et_item8.setText(mData.lyczl);
            mPresenter.getFwxxList(mData.id);
            mPresenter.getFwlbList(mData.xkzid);
        }


        mPresenter.getZxlyssList();
        Map<String, String> map = new HashMap<>();
        map.put("types", "废物特性,物理特性,计量单位,包装方式,包装材质,包装规模,危险废物利用处置方式代码,自行利用处置类型");
        mPresenter.getParamList(map);
        mPresenter.getTypeList();
        mPresenter.getXkzList();

    }


    //组装mData
    private void groupMainData(String dataStr, boolean isCk) {
//        JytzLyczBean temData = GsonUtil.GsonToBean(dataStr, JytzLyczBean.class);
//        JytzLyczBean.ZxlyFwListBean childData = GsonUtil.GsonToBean(dataStr, JytzLyczBean.ZxlyFwListBean.class);
//
//        String czl = temData.csl;
//        String pch = temData.cspch;
//        if (isCk) {
//            czl = temData.ckl;
//            pch = temData.ckpch;
//            childData.ckid = childData.id;
//        }
//        et_item10.setText(pch);
//        //将产生量/出库量赋值为利用量
//        temData.zxlyczl = czl;
//        temData.wwlyczl = czl;
//        //主键赋空
//        temData.id = "";//主键赋空
//
//        //将产生量/出库量赋值为利用量
//        childData.zxlyczl = czl;
//        childData.wwlyczl = czl;
//        //将主键赋值
//        childData.csfwid = childData.id;
//
//        //主键赋空
//        childData.id = "";
//
//        List<JytzLyczBean.ZxlyFwListBean> childList = new ArrayList<>();
//        childList.add(childData);
//        temData.zxlyczFwxxes = childList;
//        isScan = true;
//        mData = temData;
//
//        //自行利用量
//        et_item8.setText(mData.zxlyczl);
//
//        //隐藏关联按钮
//        ll_get_cfxx.setVisibility(View.GONE);
    }

    //处理不同类型的显示
    private void initDifView() {
        ((TextView) findViewById(R.id.tv_title)).setText(typeName + "登记");

        ((TextView) findViewById(R.id.tv_title_item1)).setText(getString(R.string.str_zxly_item1, typeName));
        ((TextView) findViewById(R.id.tv_title_item2)).setText(getString(R.string.str_zxly_item2, typeName));
        ((TextView) findViewById(R.id.tv_title_item14_2)).setText(getString(R.string.str_zxly_item29, typeName));
        ((TextView) findViewById(R.id.tv_title_item8)).setText(Html.fromHtml("<font color=\"#e51e5f\">" + "* " + "</font>" +
                getString(R.string.str_zxly_item8, typeName)));
        ((TextView) findViewById(R.id.tv_title_item13)).setText(Html.fromHtml("<font color=\"#e51e5f\">" + "* " + "</font>" +
                getString(R.string.str_zxly_item13, typeName)));
        ((TextView) findViewById(R.id.tv_title_item14)).setText(Html.fromHtml("<font color=\"#e51e5f\">" + "* " + "</font>" +
                getString(R.string.str_zxly_item14, typeName)));
        ((TextView) findViewById(R.id.tv_title_item20)).setText(Html.fromHtml("<font color=\"#e51e5f\">" + "* " + "</font>" +
                getString(R.string.str_zxly_item20, typeName)));
        ((TextView) findViewById(R.id.tv_title_item_wbsj)).setText(Html.fromHtml("<font color=\"#e51e5f\">" + "* " + "</font>" +
                getString(R.string.str_zxly_item21, typeName)));
    }


    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
//        initTopToolbar(typeName + "登记");
    }

    private void initView() {
        et_glxkz.setText(mData.xkzbh);
        et_item1.setText(mData.lyczpcbm);
        et_item2.setText(mData.lyczsj);
        et_item3.setText(mData.fwdlbh);
        et_item4.setText(mData.fwdlmc);
        et_item6.setText(mData.fwxlbh);
        et_item7.setText(mData.fwxxmc);
        et_item9.setText(mData.rqbzbm);
        et_item11.setText(commonTransfer(mData.rqbzlx, "包装方式"));
        et_item12.setResult(mData.rqbzsl);
        et_item13.setText(mData.ssbm);
        et_item14.setText(mData.ssmc);
        et_item15.setText(commonTransfer(mData.rqbzcz, "包装材质"));
        et_item16.setText(commonTransfer(mData.rqbzgg, "包装规模"));

        et_item17.setText(TextUtils.isEmpty(mData.jldw) ? getJldw() : mData.jldw);
        et_item5.setText(mData.lyczwbsj);

        et_item20.setText(mData.ckjbr);
        et_item22.setText(mData.ysjbr);
        et_item23.setText(getString(R.string.str_record_fwlb2, mData.lyczlx, commonTransfer(mData.lyczlx, "自行利用处置类型")));

        if (!isScan) {
            et_item18.setText(getString(R.string.str_record_fwlb2, mData.lyczfsdlbh, mData.lyczfsdlmc));
            et_item19.setText(getString(R.string.str_record_fwlb2, mData.lyczfsxlbh, mData.lyczfsxlmc));
        } else {
//            setDlmc();
        }

        if (TextUtils.isEmpty(mData.lyczjbr)) {
            et_item21.setText(SPUtils.get(mContext, Constants.USER_NAME, "").toString());
        } else {
            et_item21.setText(mData.lyczjbr);
        }

    }

    private List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();
    @OnClick({R.id.et_item3, R.id.et_item5, R.id.et_item6, R.id.et_item7, R.id.et_item13, R.id.et_item18,R.id.et_glxkz,R.id.et_item2,
            R.id.et_item15, R.id.et_item16, R.id.et_item17, R.id.et_item19, R.id.ll_back, R.id.et_item23,R.id.et_xkzfwlb,
            R.id.ll_save, R.id.ll_delete, R.id.ll_get_cfxx})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //废物类别
            case R.id.et_item3:
                showFwlbPopWindow();
                break;

            //关联许可证
            case R.id.et_glxkz:
                selectPopupWindowList = new ArrayList<>();
                for (int i = 0; i < xkzList.size(); i++) {
                    SelectPopupWindowBean bean = new SelectPopupWindowBean(xkzList.get(i).id, xkzList.get(i).xkzbh);
                    selectPopupWindowList.add(bean);
                }
                showPopWindow("许可证", et_glxkz, selectPopupWindowList);
                break;



            //许可证废物类别
            case R.id.et_xkzfwlb:
                selectPopupWindowList = new ArrayList<>();
                for (int i = 0; i < typeList.size(); i++) {
                    CollectInfoChildBean mData = typeList.get(i);
                    String name = getString(R.string.str_record_fwlb2, mData.fwxlbh,mData.fwxlmc);
                    selectPopupWindowList.add(new SelectPopupWindowBean(typeList.get(i).id, name));
                }

                showPopWindow("废物类别", et_xkzfwlb, selectPopupWindowList);

                break;



            //自行处置完毕时间
            case R.id.et_item5:

                timePicker = DateTimeUtil.showTimePicker(mContext, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        et_item5.setText(DateTimeUtil.formatDateTime(date, DateTimeUtil.DF_YYYY_MM_DD_HH_MM_SS));
                    }
                });

                break;

            //自行处置时间
            case R.id.et_item2:

                timePicker = DateTimeUtil.showTimePicker(mContext, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        et_item2.setText(DateTimeUtil.formatDateTime(date, DateTimeUtil.DF_YYYY_MM_DD_HH_MM_SS));
                    }
                });

                break;

            //物理特性
            case R.id.et_item17:
                showPopWindow("计量单位", et_item17);
                break;

            // 关联出库信息
            case R.id.ll_get_cfxx:

                if (TextUtils.isEmpty(mData.id)) {
                    ToastUtil.showToast(mContext, "请先保存后再关联！");
                    return;
                }

                startActivityForResult(new Intent(mContext, SelectMultiLyczfwListActivity.class)
                                .putExtra("mainid",mData.id)
                                .putExtra("wasteSelectedList", mData.lyczFwxxes.size() != 0 ? GsonUtil.GsonToString(mData.lyczFwxxes) : "")
                                .putExtra("wasteSelectBean", wasteBean != null ? GsonUtil.GsonToString(wasteBean) : "")
                        , 10);

                break;


            //包装材质
            case R.id.et_item15:
                showPopWindow("包装材质", et_item15);
                break;

            //包装规模
            case R.id.et_item16:
                showPopWindow("包装规模", et_item16);
                break;

            //自行利用处置类型
            case R.id.et_item23:
                showPopWindow("自行利用处置类型", et_item23);
                break;

            //危险废物利用处置方式大类
            case R.id.et_item18:
                showPopWindow("危险废物利用处置方式代码", et_item18);
                break;

            //危险废物利用处置方式小类
            case R.id.et_item19:
                showZxlyczxlPopWindow();
                break;

            //处置设施编号
            case R.id.et_item13:
                showSsPopWindow();
                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;

            // 保存
            case R.id.ll_save:
                mData.lyczsj = et_item2.getText().toString();
                mData.lyczl = et_item8.getText().toString();
                mData.rqbzbm = et_item9.getText().toString();
                mData.rqbzsl = et_item12.getResultString();
                mData.ssbm = et_item13.getText().toString();
                mData.ssmc = et_item14.getText().toString();
                mData.jldw = et_item17.getText().toString();
                mData.lyczwbsj = et_item5.getText().toString();
                mData.lyczjbr = et_item21.getText().toString();
                mData.ckjbr = et_item20.getText().toString();
                mData.ysjbr = et_item22.getText().toString();



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
        }
    }


    /*
     * 判空处理
     * */
    private String checkEmpty() {
        if (TextUtils.isEmpty(mData.ssbm)) {
            return "请选择利用处置设施！";
        } else if (TextUtils.isEmpty(et_glxkz.getText().toString())) {
            return "请选择关联许可证！";
        } else if (TextUtils.isEmpty(et_xkzfwlb.getText().toString())) {
            return "请选择许可证废物类别！";
        }  else if (TextUtils.isEmpty(mData.lyczjbr)) {
            return "请输入自行利用经办人！";
        }  else if (TextUtils.isEmpty(mData.ckjbr)) {
            return "请输入出库部门经办人！";
        }  else if (TextUtils.isEmpty(mData.ysjbr)) {
            return "请输入运送部门经办人！";
        } else {
            return "";
        }
    }


    private void showSaveDialog() {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        String addMessage = "";
//        if (TextUtils.equals(mData.lyczlx, "1") && mData.lyczFwxxes.size() > 1) {
//            addMessage = "立产立清自行利用处置只能关联一个废物,已选择第一条数据进行关联。";
//            JytzLyczBean.ZxlyFwListBean firstBean = mData.lyczFwxxes.get(0);
//            mData.lyczFwxxes.clear();
//            mData.lyczFwxxes.add(firstBean);
//        }

        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage(addMessage + "是否确定保存？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                Map<String, String> map = GsonUtil.GsonToMaps(GsonUtil.GsonToString(mData));
                mPresenter.saveData(map, ApiUrl.URL_JYTZ_LYCZ_SAVE);
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
    protected JytzLyczInfoPresenter createPresenter() {
        return new JytzLyczInfoPresenter();
    }


    @Override
    public void showError(String msg) {
        ToastUtil.showToast(mContext, msg);
    }


    @Override
    public void getTypeListSuccess(List<CommonProductInfoBean> list) {
        String json = GsonUtil.GsonToString(list);
        wasteTypeList = GsonUtil.JsonToList(json, JytzLyczBean.ZxlyFwListBean.class);

        if (!TextUtils.isEmpty(mData.fwId)) {
            for (int i = 0; i < wasteTypeList.size(); i++) {
                if (mData.fwId.equals(wasteTypeList.get(i).id)) {
                    wasteSelectBean = wasteTypeList.get(i);
                }
            }

            if (!TextUtils.isEmpty(wasteSelectBean.id)) {
                initWasteInfo();
            }

        }
    }

    @Override
    public void getFwxxListSuccess(List<JytzLyczBean.ZxlyFwListBean> list) {
        mData.lyczFwxxes = new ArrayList<>();
        mData.lyczFwxxes.addAll(list);
        initPchmStr(false);
    }

    @Override
    public void getZxlyssListSuccess(List<ZxlyssBean> list) {
        String json = GsonUtil.GsonToString(list);
        zcssList = GsonUtil.JsonToList(json, ZxlyssBean.class);
    }


    private List<ParamListBean> allParamList = new ArrayList<>();
    private List<JytzLyczBean.ZxlyFwListBean> wasteTypeList = new ArrayList<>();
    private List<ZxlyssBean> zcssList = new ArrayList<>();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timePicker != null) {
            timePicker = null;
        }
    }

    @Override
    public void getParamListSuccess(List<ParamListBean> list) {
        String json = GsonUtil.GsonToString(list);
        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);

        if (!TextUtils.isEmpty(mData.id) || isScan) {
            initView();
            getXlList();
        } else {
//            setDlmc();
        }

    }

    //获取小类列表
    private void getXlList() {
        selectZxlyczxlPopupWindowList = new ArrayList<>();
        for (int i = 0; i < allParamList.size(); i++) {
            List<ParamListBean> childList = allParamList.get(i).childList;
            for (int j = 0; j < childList.size(); j++) {
                if ("危险废物利用处置方式代码".equals(allParamList.get(i).type)) {
                    ParamListBean bean = childList.get(j);
                    if (mData.lyczfsdlbh.equals(bean.code)) {
                        for (int k = 0; k < bean.childList.size(); k++) {
                            ParamListBean childBean = bean.childList.get(k);
                            String groupName2 = getString(R.string.str_record_fwlb2, childBean.code, childBean.value);
                            selectZxlyczxlPopupWindowList.add(new SelectPopupWindowBean(childBean.code, groupName2, childBean.value));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void saveDataSuccess(Object bean) {
        RxBus.getDefault().post(new ChangeEvent());
        if (isScan) {
            RxBus.getDefault().post(new ChangeEvent());
        }
        setResult(9);
        ToastUtil.showToast(mContext, "保存成功！");
        finish();
    }


    @Override
    public void deleteDataSuccess(String bean) {
        ToastUtil.showToast(mContext, "删除成功！");
        setResult(9);
        finish();
    }


    private List<CollectInfoChildBean> typeList = new ArrayList<>();

    private CollectInfoChildBean wasteBean = new CollectInfoChildBean();

    @Override
    public void getFwlbListSuccess(List<CollectInfoChildBean> list) {
        String json = GsonUtil.GsonToString(list);
        typeList = GsonUtil.JsonToList(json, CollectInfoChildBean.class);

        if(!TextUtils.isEmpty(mData.id)){ //匹配废物
            for (int i = 0; i < typeList.size(); i++) {
                if (typeList.get(i).id.equals(mData.xkzFwxxid)){
                    String name = getString(R.string.str_record_fwlb2, typeList.get(i).fwxlbh,typeList.get(i).fwxlmc);
                    et_xkzfwlb.setText(name);
                    wasteBean = typeList.get(i);
                }
            }
        }
    }


    private SelectPopupWindow selectPopupWindow;

    private List<SelectPopupWindowBean> zxlyxlList = new ArrayList<>();


    //初始化选择地址下拉弹窗
    private void showPopWindow(String type, EditText item) {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < allParamList.size(); i++) {
            List<ParamListBean> childList = allParamList.get(i).childList;

            if (childList != null && childList.size() != 0 && type.equals(allParamList.get(i).type)) {
                for (int j = 0; j < childList.size(); j++) {
                    ParamListBean bean = childList.get(j);
                    if ("危险废物利用处置方式代码".equals(allParamList.get(i).type)) {
                        if (j < 3) {
                            String groupName = getString(R.string.str_record_fwlb2, bean.code, bean.value);
                            SelectPopupWindowBean popupWindowBean = new SelectPopupWindowBean(childList.get(j).code, groupName, childList.get(j).value);
                            selectPopupWindowList.add(popupWindowBean);
                        }
                    } else if ("自行利用处置类型".equals(allParamList.get(i).type)) {
                        String groupName = getString(R.string.str_record_fwlb2, bean.code, bean.value);
                        SelectPopupWindowBean popupWindowBean = new SelectPopupWindowBean(childList.get(j).code, groupName, childList.get(j).value);
                        selectPopupWindowList.add(popupWindowBean);
                    } else {
                        selectPopupWindowList.add(new SelectPopupWindowBean(bean.code, bean.value));
                    }
                }
            }
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                item.setText(model.NAME);
                //废物特性,物理特性,包装方式,计量单位,处理方式及去向,包装材质,包装规模
                switch (mType) {
                    case "危险废物利用处置方式代码":
                        et_item18.setText(model.NAME);
                        mData.lyczfsdlbh = model.ID;
                        mData.lyczfsdlmc = model.GROUP_NAME;
                        mData.lyczfsxlbh = "";
                        mData.lyczfsxlmc = "";
                        et_item19.setText("");
                        getXlList();
                        break;
                    case "自行利用处置类型":
                        et_item23.setText(model.NAME);
                        mData.lyczlx = model.ID;
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
                        SPUtils.put(mContext, "JLDW", mData.jldw);
                        break;
                }
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }

        selectPopupWindow.show(mLayout, type, (View) item.getParent(), sort);

    }
    private CollectInfoChildBean fwlbBean = new CollectInfoChildBean();
    //初始化选择地址下拉弹窗
    private void showPopWindow(String type, EditText item, List<SelectPopupWindowBean> selectPopupWindowList) {
        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                item.setText(model.NAME);
                switch (mType) {
                    case "许可证"://许可证
                        mData.xkzid = model.ID;
                        mData.xkzbh = model.NAME;
                        et_glxkz.setText(mData.xkzbh);
                        mPresenter.getFwlbList(model.ID);
                        break;

                    case "废物类别"://废物类别

                        for (int i = 0; i < typeList.size(); i++) {
                            if (TextUtils.equals(typeList.get(i).id, model.ID)) {
                                fwlbBean = typeList.get(i);
                            }
                        }
                        mData.xkzFwxxid = model.ID;

                        mData.lyczfsdlbh = fwlbBean.lyczfsdl;
                        mData.lyczfsxlbh = fwlbBean.lyczfsxl;
                        et_xkzfwlb.setText(model.NAME);
                        getTypeName(mData.lyczfsdlbh,mData.lyczfsxlbh);


//                        fwlbBean.rqbzlx = fwlbBean.bzfs;
//                        //将小类赋值到处置/利用方式中
//                        fwlbBean.lyczfs = fwlbBean.lyczfsxl;
                        break;

                }

                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }

        selectPopupWindow.show(mLayout, type, (View) item.getParent(), sort);

    }

    /**
     * 根据大类、小类编码匹配中文
     * */
    private void getTypeName(String code,String childCode){

        for (int i = 0; i < allParamList.size(); i++) {
            List<ParamListBean> childList = allParamList.get(i).childList;
            if (childList != null && childList.size() != 0 && "危险废物利用处置方式代码".equals(allParamList.get(i).type)) {
                for (int j = 0; j < childList.size(); j++) {
                    ParamListBean bean = childList.get(j);
                    if ("危险废物利用处置方式代码".equals(allParamList.get(i).type)) {
                        if (bean.code.equals(code)) { //根据大类编码匹配
                            String groupName = getString(R.string.str_record_fwlb2, bean.code, bean.value);
                            mData.lyczfsdlmc =bean.value;
                            et_item18.setText(groupName);
                            for (int k = 0; k < bean.childList.size(); k++) {
                                ParamListBean sonBean = bean.childList.get(k);
                                if(sonBean.code.equals(childCode)){//根据小类编码匹配
                                    String sonGroupName = getString(R.string.str_record_fwlb2, sonBean.code, sonBean.value);
                                    mData.lyczfsxlmc =sonBean.value;
                                    et_item19.setText(sonGroupName);
                                }
                            }
                        }
                    }
                }
            }
        }
    }




    private List<SelectPopupWindowBean> selectZxlyczxlPopupWindowList = new ArrayList<>();

    //自行利用小类下拉弹窗
    private void showZxlyczxlPopWindow() {

        selectPopupWindow = new SelectPopupWindow(mContext, selectZxlyczxlPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                et_item19.setText(model.NAME);
                mData.lyczfsxlbh = model.ID;
                mData.lyczfsxlmc = model.GROUP_NAME;
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectZxlyczxlPopupWindowList.size() < 4) {
            sort = selectZxlyczxlPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item19.getParent(), sort);

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
            JytzLyczBean.ZxlyFwListBean mData = wasteTypeList.get(i);
            String name = getString(R.string.str_record_fwlb, mData.fwxxmc, mData.fwdlbh, mData.fwdlmc, mData.fwxlbh);
            selectPopupWindowList.add(new SelectPopupWindowBean(wasteTypeList.get(i).id, name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                JytzLyczBean.ZxlyFwListBean bean = new JytzLyczBean.ZxlyFwListBean();
                for (int i = 0; i < wasteTypeList.size(); i++) {
                    if (wasteTypeList.get(i).id.equals(model.ID)) {
                        bean = wasteTypeList.get(i);
                    }
                }

                wasteSelectBean = bean;
                initWasteInfo();

                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item3.getParent(), sort);
    }

    private void initWasteInfo() {
        et_item3.setText(wasteSelectBean.fwdlbh);
        et_item4.setText(wasteSelectBean.fwdlmc);
        et_item6.setText(wasteSelectBean.fwxlbh);
        et_item7.setText(wasteSelectBean.fwxxmc);

        et_item3.setText(wasteSelectBean.fwdlbh);
        et_item4.setText(wasteSelectBean.fwdlmc);
        et_item6.setText(wasteSelectBean.fwxlbh);
        et_item7.setText(wasteSelectBean.fwxxmc);

        mData.fwdlbh = wasteSelectBean.fwdlbh;
        mData.fwxxmc = wasteSelectBean.fwxxmc;
        mData.fwtx = wasteSelectBean.fwtx;
        mData.wlxz = wasteSelectBean.wlxz;
//        mData.rqbzlx = wasteSelectBean.bzfs;
        mData.fwxlbh = wasteSelectBean.fwxlbh;
        mData.fwxlmc = wasteSelectBean.fwxlmc;
        mData.fwdlmc = wasteSelectBean.fwdlmc;
        mData.fwId = wasteSelectBean.id;

        mData.rqbzbm = wasteSelectBean.rqbzbm;
        mData.rqbzcz = wasteSelectBean.rqbzcz;
        mData.rqbzgg = wasteSelectBean.rqbzgg;

        et_item15.setText(commonTransfer(mData.rqbzcz, "包装材质"));
        et_item16.setText(commonTransfer(mData.rqbzgg, "包装规模"));
        et_item11.setText(commonTransfer(mData.rqbzlx, "包装方式"));

        groupBzbm();
    }


    //初始自行利用设施下拉弹窗
    private void showSsPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < zcssList.size(); i++) {
            ZxlyssBean mData = zcssList.get(i);
            String name = getString(R.string.str_record_fwlb2, mData.ssbh, mData.ssmc);
            selectPopupWindowList.add(new SelectPopupWindowBean(zcssList.get(i).id, name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                ZxlyssBean bean = new ZxlyssBean();
                for (int i = 0; i < zcssList.size(); i++) {
                    if (zcssList.get(i).id.equals(model.ID)) {
                        bean = zcssList.get(i);
                    }
                }
                et_item13.setText(bean.ssbh);
                et_item14.setText(bean.ssmc);
                mData.ssbm = et_item13.getText().toString();
                mData.ssmc = et_item14.getText().toString();
                mData.ssid = bean.id;
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item13.getParent(), sort);
    }


    /*
     * 通用转义
     * */
    private String commonTransfer(String code, String type) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 10) {
            String selectListStr = data.getStringExtra("selectList");
            KLog.e("selectListStr ====" + selectListStr);
            List<JytzLyczBean.ZxlyFwListBean> selectList = GsonUtil.JsonToList(selectListStr, JytzLyczBean.ZxlyFwListBean.class);
            mData.lyczFwxxes = new ArrayList<>();
            mData.lyczFwxxes.addAll(selectList);
            setResult(9);

            if (mData.lyczFwxxes.size() == 1) {
                mData.jldw = mData.lyczFwxxes.get(0).jldw;
                et_item17.setText(mData.jldw);
            }


            initPchmStr(true);
        }
    }

    private List<JytzLyczBean.ZxlyFwListBean> selectList = new ArrayList<>();

    //处理批次号、废物重量
    private void initPchmStr(boolean isNew) {
        selectList = mData.lyczFwxxes;
        String pcbmStr = "";
//        String totalWeight = "0";
        String bzsl = "0";
        for (int i = 0; i < selectList.size(); i++) {
            if (!TextUtils.isEmpty(selectList.get(i).ckpch)) {
                pcbmStr += selectList.get(i).ckpch + ",";
            }

            if (TextUtils.isEmpty(selectList.get(i).lyczl)) {
                selectList.get(i).lyczl = "0";
            }
//            //废物重量叠加
//            totalWeight = new BigDecimal(selectList.get(i).lyczl).add(new BigDecimal(totalWeight)).toString();
        }
        pcbmStr = pcbmStr.substring(0, pcbmStr.length() - 1);
        et_item10.setText(pcbmStr);

//        if (isNew) {
//            et_item8.setText(totalWeight);
//        }

    }


    private List<GfYbqyXxbaXkzxxBean> xkzList = new ArrayList<>();

    @Override
    public void getXkzListSuccess(Object bean) {
        xkzList = GsonUtil.JsonToList(GsonUtil.GsonToString(bean), GfYbqyXxbaXkzxxBean.class);
    }



}
