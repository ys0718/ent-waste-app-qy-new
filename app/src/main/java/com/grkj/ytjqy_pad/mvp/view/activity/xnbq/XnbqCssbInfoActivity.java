package com.grkj.ytjqy_pad.mvp.view.activity.xnbq;

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
import com.grkj.ytjqy_pad.mvp.model.HtxxBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ProductDeviceBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.model.ZyldBean;
import com.grkj.ytjqy_pad.mvp.model.xnbq.GfYbqyTzCkZxlyczXnbq;
import com.grkj.ytjqy_pad.mvp.model.xnbq.GfYbqyTzNewCsRkXnbq;
import com.grkj.ytjqy_pad.mvp.model.xnbq.GfYbqyTzNewCsXnbq;
import com.grkj.ytjqy_pad.mvp.model.xnbq.GfYbqyXxbaZxlyczss;
import com.grkj.ytjqy_pad.mvp.model.xnbq.XnbqCssbCommitBean;
import com.grkj.ytjqy_pad.mvp.presenter.XnbqCssbInfoPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.bluetooth.WeightBaseActivity;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.popwindow.model.Select;
import com.grkj.ytjqy_pad.utils.popwindow.spinner.HistorySearchSpinner;
import com.grkj.ytjqy_pad.utils.popwindow.spinner.SearchSpinner;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2026/10/28.
 * 虚拟标签-立产立清-产生申报信息
 * 产废单位-危险废物产生台账（GfYbqyTzNewCsXnbq）+ 立产立清-立产立置信息（GfYbqyTzCkZxlyczXnbq type=2）
 */

public class XnbqCssbInfoActivity extends WeightBaseActivity<XnbqCssbInfoPresenter> implements XnbqCssbInfoPresenter.View {

    //产生台账（散装/桶装基本信息）
    private GfYbqyTzNewCsXnbq mData = new GfYbqyTzNewCsXnbq();

    //立产立清-立产立置信息（type=2，通过ckZxlyczXnbqId与产生台账关联）
    private GfYbqyTzCkZxlyczXnbq mZlyczData = new GfYbqyTzCkZxlyczXnbq();

    //生产台账编号
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

    //废物类别名称
    @BindView(R.id.et_item6)
    EditText et_item6;

    //废物特性
    @BindView(R.id.et_item7)
    EditText et_item7;

    //废物描述
    @BindView(R.id.et_item9)
    EditText et_item9;

    //注意事项
    @BindView(R.id.et_item10)
    EditText et_item10;

    //去向
    @BindView(R.id.et_item11)
    EditText et_item11;

    //容器/包装规格
    @BindView(R.id.et_item12)
    EditText et_item12;

    //生产设施名称
    @BindView(R.id.et_item13)
    EditText et_item13;

    //容器/包装类型
    @BindView(R.id.et_item14)
    EditText et_item14;

    //废物形态
    @BindView(R.id.et_item15)
    EditText et_item15;

    //有害成分
    @BindView(R.id.et_item16)
    EditText et_item16;

    //计量单位
    @BindView(R.id.et_item17)
    EditText et_item17;

    //主要成分
    @BindView(R.id.et_item19)
    EditText et_item19;

    //运送部门经办人
    @BindView(R.id.et_item18)
    HistorySearchSpinner et_item18;

    //自行利用/处置时间
    @BindView(R.id.et_item20)
    EditText et_item20;

    //利用处置设施编码
    @BindView(R.id.et_item21)
    EditText et_item21;

    //利用处置设施名称
    @BindView(R.id.et_item22)
    EditText et_item22;

    //产生设施编码
    @BindView(R.id.et_item23)
    EditText et_item23;

    //利用处置方式
    @BindView(R.id.et_item24)
    EditText et_item24;

    //虚拟标签管类型
    @BindView(R.id.et_item25)
    EditText et_item25;

    //密度和流量
    @BindView(R.id.et_item26)
    EditText et_item26;

    //车辆描述
    @BindView(R.id.et_item27)
    EditText et_item27;

    //利用处置方式小类
    @BindView(R.id.et_item28)
    EditText et_item28;

    //废物大类编号
    @BindView(R.id.et_item29)
    EditText et_item29;

    //废物大类名称
    @BindView(R.id.et_item30)
    EditText et_item30;

    //废物小类编号
    @BindView(R.id.et_item31)
    EditText et_item31;

    //废物小类名称
    @BindView(R.id.et_item32)
    EditText et_item32;

    //自行利用预估量
    @BindView(R.id.et_item33)
    EditText et_item33;

    //自行利用处置类型
    @BindView(R.id.et_item35)
    EditText et_item35;

    //产生部门经办人
    @BindView(R.id.editSpinner_14)
    SearchSpinner editSpinner_14;

    //自行利用/处置部门经办人
    @BindView(R.id.editSpinner_19)
    SearchSpinner editSpinner_19;

    //出库部门经办人
    @BindView(R.id.editSpinner_20)
    SearchSpinner editSpinner_20;


    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //删除按钮
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;

    //保存按钮
    @BindView(R.id.ll_save)
    LinearLayout ll_save;

    //上报国家按钮
    @BindView(R.id.ll_sbgj)
    LinearLayout ll_sbgj;

    private boolean enableSave = true;

    //是否正在保存立产立置表（两步保存：先立产立置后产生台账）
    private boolean isSaveZlycz = false;

    private List<GfYbqyXxbaZxlyczss> zxlyssList = new ArrayList<>();
    private List<ProductDeviceBean> productDeviceList = new ArrayList<>();
    private List<UserBean> temUserList = new ArrayList<>();

    private GfYbqyTzNewCsXnbq wasteSelectBean = null;
    private String csfwid;

    @Override
    protected int getLayout() {
        return R.layout.activity_xnbq_cssb_info;
    }

    @Override
    protected void initEventAndData() {
        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);

        initWeight();

        if (TextUtils.isEmpty(dataStr)) {//新增
            isNew = true;
            isGetWeight = true;
            mData = new GfYbqyTzNewCsXnbq();
            mZlyczData = new GfYbqyTzCkZxlyczXnbq();
            mZlyczData.type = "2";//2-立产立清-立产立处置
            mZlyczData.lyczlx = "1";//1-立产立清利用处置
            et_item2.setText(DateTimeUtil.getCurrentDate());
            mData.cssj = et_item2.getText().toString();
            et_item20.setText(DateTimeUtil.getCurrentDate());
            editSpinner_14.setText(SPUtils.get(mContext, Constants.USER_NAME, "").toString());
            editSpinner_19.setText(SPUtils.get(mContext, Constants.USER_NAME, "").toString());
            editSpinner_20.setText(SPUtils.get(mContext, Constants.USER_NAME, "").toString());
            mData.jldw = getJldw();
            et_item17.setText(mData.jldw);
            et_item34Init();
            //去向默认立产立清自处置
            mData.qx = "3";
            et_item11.setText("立产立清自处置");
            handleReadOnly(false);
        } else {//编辑
            et_item2.setEnabled(false);
            et_item2.setClickable(false);

            mData = GsonUtil.GsonToBean(dataStr, GfYbqyTzNewCsXnbq.class);
            mZlyczData = GsonUtil.GsonToBean(dataStr, GfYbqyTzCkZxlyczXnbq.class);
            mZlyczData.type = "2";
            mZlyczData.lyczlx = TextUtils.isEmpty(mZlyczData.lyczlx) ? "1" : mZlyczData.lyczlx;
            mZlyczData.id = mData.ckZxlyczXnbqId;
            //上报之后不能保存和删除
            if (TextUtils.isEmpty(mData.szsbm) ){
                ll_delete.setVisibility(View.VISIBLE);
                ll_save.setVisibility(View.VISIBLE);
                ll_sbgj.setVisibility(View.VISIBLE);
                handleReadOnly(false);
            }else {
                ll_delete.setVisibility(View.GONE);
                ll_save.setVisibility(View.GONE);
                ll_sbgj.setVisibility(View.GONE);
                handleReadOnly(true);
            }

        }

        //自行利用/处置时间选择
        setTimeDialogClickListener(et_item20);

        mPresenter.getZxlyssList();
        Map<String, String> map = new HashMap<>();
        map.put("types", "计量单位,虚拟标签类型,物理特性,包装方式,包装规模,自行利用处置类型,危险废物利用处置方式代码");
        mPresenter.getParamList(map);
        mPresenter.getUserList("");
        et_item8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                et_item33.setText(s.toString());
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
        initTopToolbar("立产立清-产生申报");
    }

    //自行利用预估量单位回显
    private void et_item34Init() {
        String jldw = TextUtils.isEmpty(mZlyczData.jldw) ? (TextUtils.isEmpty(mData.jldw) ? getJldw() : mData.jldw) : mZlyczData.jldw;
        mZlyczData.jldw = jldw;
        EditText et_item34 = findViewById(R.id.et_item34);
        et_item34.setText(jldw);
    }

    private void initView() {

        //产废单位-危险废物产生台账-散装/桶装基本信息
        et_item1.setText(mData.cstzbm);
        et_item2.setText(mData.cssj);
        et_item3.setText(mData.fwdlbh);
        et_item6.setText(mData.fwdlmc);
        et_item4.setText(mData.fwxlbh);
        et_item9.setText(mData.fwxlmc);
        et_item5.setText(mData.fwxxmc);
        et_item7.setText(mData.fwtx);
        et_item16.setText(mData.wxcf);
        et_item10.setText(mData.zysx);
        et_item19.setText(mData.zycf);
        et_item8.setText(mData.csl);
        et_item17.setText(TextUtils.isEmpty(mData.jldw) ? getJldw() : mData.jldw);
        et_item13.setText(mData.scssmc);
        et_item23.setText(mData.csssbm);
        et_item26.setText(mData.mdhll);
        et_item27.setText(mData.clms);
        et_item12.setText(commonTransfer(mData.rqbzgg, "包装规模"));
        et_item14.setText( commonTransfer(mData.rqbzlx , "包装方式"));
        et_item15.setText(commonTransfer(mData.wlxz , "物理特性"));
        et_item25.setText(commonTransfer(mData.xnbqlx, "虚拟标签类型"));
        et_item11.setText(transferQx(mData.qx));
        editSpinner_14.setText(mData.csbmjbr);

        //立产立清-立产立置信息
        et_item20.setText(mZlyczData.zxlyczsj);
        et_item21.setText(mZlyczData.ssbh);
        et_item22.setText(mZlyczData.ssmc);
        et_item24.setText(TextUtils.isEmpty(mZlyczData.lyczfs) ? "" :
                getString(R.string.str_record_fwlb2, mZlyczData.lyczfs, commonTransfer(mZlyczData.lyczfs, "危险废物利用处置方式代码")));
//        et_item28.setText(mZlyczData.lyczfsxl);
        setXlmc();
        et_item29.setText(mZlyczData.fwdlbh);
        et_item30.setText(mZlyczData.fwdlmc);
        et_item31.setText(mZlyczData.fwxlbh);
        et_item32.setText(mZlyczData.fwxlmc);
        et_item33.setText(mZlyczData.zxlyczl);
        et_item34Init();
        et_item35.setText(getString(R.string.str_record_fwlb2, mZlyczData.lyczlx, commonTransfer(mZlyczData.lyczlx, "自行利用处置类型")));
        et_item18.setText(mZlyczData.ysjbr);

    }

    //赋值大利用处置方式大类名称
    private void setXlmc(){
        for (int i = 0; i < allParamList.size(); i++) {
            List<ParamListBean> childList = allParamList.get(i).childList;
            for (int j = 0; j < childList.size(); j++) {
                if ("危险废物利用处置方式代码".equals(allParamList.get(i).type)) {
                    ParamListBean bean = childList.get(j);
                    if (mZlyczData.lyczfs.equals(bean.code)) {

                        for (int k = 0; k < bean.childList.size(); k++) {
                            ParamListBean childBean = bean.childList.get(k);

                            if(TextUtils.equals(childBean.code, mZlyczData.lyczfsxl)){
                                et_item28.setText(getString(R.string.str_record_fwlb2,childBean.code,childBean.value));
                            }
                        }
                    }
                }
            }
        }
    }

    //去向转义（2-立产立清转移；3-立产立清自处置）
    private String transferQx(String qx) {
        if ("2".equals(qx)) {
            return "立产立清转移";
        } else if ("3".equals(qx)) {
            return "立产立清自处置";
        }
        return TextUtils.isEmpty(qx) ? "" : qx;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 11) { //选择废物类别
            String selectBeanStr = data.getStringExtra("selectBean");
            wasteSelectBean = GsonUtil.GsonToBean(selectBeanStr, GfYbqyTzNewCsXnbq.class);
            initWasteInfo();
            mPresenter.getProductDeviceList(csfwid);
        } else if (resultCode == 13 && !isWeight) { //校准
            setResult(9);
            String current_weight = data.getStringExtra("CURRENT_WEIGHT");
            et_item8.setText(current_weight);
        }

    }


    //选择/清空废物类别信息
    private void initWasteInfo() {
        csfwid = wasteSelectBean.id;
        initBaseWasteInfo();
    }

    //公用废物类别赋值
    private void initBaseWasteInfo() {
        et_item3.setText(wasteSelectBean.fwdlbh);
        et_item6.setText(wasteSelectBean.fwdlmc);
        et_item4.setText(wasteSelectBean.fwxlbh);
        et_item9.setText(wasteSelectBean.fwxlmc);
        et_item5.setText(wasteSelectBean.fwxxmc);
        et_item7.setText(wasteSelectBean.fwtx);
        et_item16.setText(wasteSelectBean.wxcf);
        et_item10.setText(wasteSelectBean.zysx);
        et_item19.setText(wasteSelectBean.zycf);



        mData.fwdlbh = wasteSelectBean.fwdlbh;
        mData.fwdlmc = wasteSelectBean.fwdlmc;
        mData.fwxlbh = wasteSelectBean.fwxlbh;
        mData.fwxlmc = wasteSelectBean.fwxlmc;
        mData.fwxxmc = wasteSelectBean.fwxxmc;
        mData.fwtx = wasteSelectBean.fwtx;
        mData.wlxz = wasteSelectBean.wlxz;
        mData.wxcf = wasteSelectBean.wxcf;
        mData.zycf = wasteSelectBean.zycf;
        mData.zysx = wasteSelectBean.zysx;
        mData.rqbzcz = wasteSelectBean.rqbzcz;
        mData.rqbzlx = wasteSelectBean.rqbzlx;
        mData.rqbzgg = wasteSelectBean.rqbzgg;

        //计量单位处理
        if (!TextUtils.isEmpty(wasteSelectBean.jldw) && !isWeight) {
            mData.jldw = wasteSelectBean.jldw;
        }
        et_item17.setText(mData.jldw);

        et_item12.setText(commonTransfer(mData.rqbzgg, "包装规模"));
        et_item14.setText( commonTransfer(mData.rqbzlx , "包装方式"));
        et_item15.setText(commonTransfer(mData.wlxz , "物理特性"));

        //同步立产立置废物字段
        mZlyczData.fwdlbh = mData.fwdlbh;
        mZlyczData.fwdlmc = mData.fwdlmc;
        mZlyczData.fwxlbh = mData.fwxlbh;
        mZlyczData.fwxlmc = mData.fwxlmc;
        mZlyczData.fwxxmc = mData.fwxxmc;
        et_item29.setText(mZlyczData.fwdlbh);
        et_item30.setText(mZlyczData.fwdlmc);
        et_item31.setText(mZlyczData.fwxlbh);
        et_item32.setText(mZlyczData.fwxlmc);
    }


    @OnClick({R.id.et_item3, R.id.et_item13, R.id.et_item17, R.id.et_item25, R.id.et_item15, R.id.et_item14,
            R.id.et_item12, R.id.et_item35, R.id.et_item11, R.id.et_item21,R.id.ll_sbgj,
            R.id.ll_back, R.id.ll_save, R.id.ll_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //废物类别
            case R.id.et_item3:
                goToActivity(new Intent(mContext, SelectCshjWasteListActivity.class)
                                .putExtra("title", "选择产废信息")
                                .putExtra("url", ApiUrl.URL_CSHJ_FWLX_LIST)
                        , 11);
                break;

            //生产设施名称
            case R.id.et_item13:
                if (TextUtils.isEmpty(mData.fwxxmc)) {
                    ToastUtil.showToast(mContext, "请选择废物类别！");
                } else {
                    showCsssPopWindow();
                }
                break;

            //计量单位
            case R.id.et_item17:
                showPopWindow("计量单位", et_item17);
                break;

            //虚拟标签管类型
            case R.id.et_item25:
                showPopWindow("虚拟标签类型", et_item25);
                break;

            //废物形态
            case R.id.et_item15:
                showPopWindow("物理特性", et_item15);
                break;

            //容器/包装类型
            case R.id.et_item14:
                showPopWindow("包装方式", et_item14);
                break;

            //容器/包装规格
            case R.id.et_item12:
                showPopWindow("包装规模", et_item12);
                break;

            //自行利用处置类型
            case R.id.et_item35:
                showPopWindow("自行利用处置类型", et_item35);
                break;

            //去向
            case R.id.et_item11:
                showQxPopWindow();
                break;

            //利用处置设施编码
            case R.id.et_item21:
                showSsPopWindow();
                break;


            // 返回
            case R.id.ll_back:
                finish();
                break;

            // 保存
            case R.id.ll_save:
                onSaveClick();
                break;

            // 保存
            case R.id.ll_sbgj:
                showSbgjDialog();
                break;

            // 删除
            case R.id.ll_delete:
                mPresenter.showDeleteDialog(mData.id);
                break;
        }
    }


    //去向下拉弹窗（2-立产立清转移；3-立产立清自处置）
    private void showQxPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();
        selectPopupWindowList.add(new SelectPopupWindowBean("2", "立产立清转移"));
        selectPopupWindowList.add(new SelectPopupWindowBean("3", "立产立清自处置"));

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                mData.qx = model.ID;
                et_item11.setText(model.NAME);
                selectPopupWindow.dismiss();
            }
        });

        int sort = 3;
        selectPopupWindow.show(mLayout, "去向", (View) et_item11.getParent(), sort);
    }


    private void onSaveClick() {
        if (!enableSave) {
            ToastUtil.showToast(mContext, "数据正在保存，请勿频繁操作");
            return;
        }

        et_item18.clearFocus();
        mData.cssj = et_item2.getText().toString();
        mData.csl = et_item8.getText().toString();
        mData.csbmjbr = editSpinner_14.getText().toString();
        mData.jldw = et_item17.getText().toString();
        mData.mdhll = et_item26.getText().toString();
        mData.clms = et_item27.getText().toString();
        mZlyczData.zxlyczsj = et_item20.getText().toString();
        mZlyczData.zxlyczl = et_item33.getText().toString();
        mZlyczData.zxlyczjbr = editSpinner_19.getText().toString();
        mZlyczData.ckjbr = editSpinner_20.getText().toString();
        mZlyczData.ysjbr = et_item18.getText();
        mZlyczData.jldw = ((EditText) findViewById(R.id.et_item34)).getText().toString();
        mZlyczData.sslx =  commonTransfer(mZlyczData.lyczfs, "危险废物利用处置方式代码");

        if (TextUtils.isEmpty(checkEmpty())) {
            showSaveDialog();
        } else {
            ToastUtil.showToast(mContext, checkEmpty());
        }
    }


    /*
     * 判空处理
     * */
    private String checkEmpty() {

        if (TextUtils.isEmpty(mData.cssj)) {
            return "请选择产生时间！";
        } else if (TextUtils.isEmpty(mData.csl)) {
            return "请输入/地磅获取产生量！";
        } else if (Double.valueOf(mData.csl) <= 0) {
            return "请输入/地磅获取正确的产生量！";
        } else if (TextUtils.isEmpty(mData.fwdlbh)) {
            return "请选择废物类别！";
        } else if (TextUtils.isEmpty(mData.scssmc)) {
            return "请选择生产设施名称！";
        } else if (TextUtils.isEmpty(mData.qx)) {
            return "请选择去向！";
        } else if (TextUtils.isEmpty(mData.csbmjbr)) {
            return "请输入产生部门经办人！";
        } else if (TextUtils.isEmpty(mZlyczData.zxlyczsj)) {
            return "请选择自行利用/处置时间！";
        } else if (TextUtils.isEmpty(mZlyczData.zxlyczl)) {
            return "请输入自行利用预估量！";
        } else if (Double.valueOf(mZlyczData.zxlyczl) <= 0) {
            return "请输入正确的自行利用预估量！";
        } else if (TextUtils.isEmpty(mZlyczData.ssbh)) {
            return "请选择利用处置设施编码！";
        } else if (TextUtils.isEmpty(mZlyczData.lyczlx)) {
            return "请选择自行利用处置类型！";
        } else if (TextUtils.isEmpty(mZlyczData.zxlyczjbr)) {
            return "请输入自行利用/处置部门经办人！";
        } else if (TextUtils.isEmpty(mZlyczData.ckjbr)) {
            return "请输入出库部门经办人！";
        } else if (TextUtils.isEmpty(mZlyczData.ysjbr)) {
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
                isSaveZlycz = true;
                XnbqCssbCommitBean commitBean = new XnbqCssbCommitBean(mData,mZlyczData);
                Map<String, String> map = GsonUtil.GsonToMaps(GsonUtil.GsonToString(commitBean));
                mPresenter.saveData(map, ApiUrl.getHnUrl(ApiUrl.URL_XNBQ_CSSB_SAVE));
                roundCornerDialog.dismiss();
            }

            @Override
            public void onCancelClick() {

            }
        });
        //显示对话框
        roundCornerDialog.show();
    }

    private void showSbgjDialog() {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确定上报国家？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                enableSave = false;
                mPresenter.sbgjRequest(mData.id, ApiUrl.getHnUrl(ApiUrl.URL_XNBQ_CSSB_SBGJ));
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
    protected XnbqCssbInfoPresenter createPresenter() {
        return new XnbqCssbInfoPresenter();
    }


    @Override
    public void showError(String msg) {
        enableSave = true;
        isSaveZlycz = false;
        ToastUtil.showToast(mContext, msg);
    }

    @Override
    public void getTypeListSuccess(List<GfYbqyTzNewCsRkXnbq> list) {

    }

    @Override
    public void getZcssListSuccess(List<ZcssBean> list) {

    }

    @Override
    public void getZxlyssListSuccess(List<GfYbqyXxbaZxlyczss> list) {
        String json = GsonUtil.GsonToString(list);
        zxlyssList = GsonUtil.JsonToList(json, GfYbqyXxbaZxlyczss.class);
    }

    @Override
    public void getParamListSuccess(List<ParamListBean> list) {
        String json = GsonUtil.GsonToString(list);
        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);
        if (!isNew) {
//            initView();
            mPresenter.getRkInfo(mData.ckZxlyczXnbqId);
        }
    }

    @Override
    public void saveDataSuccess(String bean) {
        setResult(9);
        ToastUtil.showToast(mContext, "保存成功！");
        finish();
    }

    @Override
    public void sbgjRequestSuccess(String bean) {
        setResult(9);
        ToastUtil.showToast(mContext, "上报成功！");
        finish();
    }

    @Override
    public void getCshjInfoSuccess(Object bean) {

    }

    @Override
    public void getRkInfoSuccess(Object bean) {
        KLog.e(bean);
        mZlyczData = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), GfYbqyTzCkZxlyczXnbq.class);
        initView();
    }

    @Override
    public void getLabelInfoSuccess(Object bean) {

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

    @Override
    public void getFromListSuccess(List<UserBean> list) {

    }

    @Override
    public void getZyldListSuccess(List<ZyldBean> list) {

    }

    @Override
    public void getHtListSuccess(List<HtxxBean> list) {

    }

    @Override
    public void getUserListSuccess(List<UserBean> list) {
        String json = GsonUtil.GsonToString(list);
        temUserList = GsonUtil.JsonToList(json, UserBean.class);

        if (temUserList.size() == 0) {
            editSpinner_14.setHint("请输入");
            editSpinner_19.setHint("请输入");
            editSpinner_20.setHint("请输入");
            return;
        }

        List<Select> selectList = new ArrayList<>();
        for (int i = 0; i < temUserList.size(); i++) {
            Select bean = new Select(temUserList.get(i).name, temUserList.get(i).name);
            selectList.add(bean);
        }
        editSpinner_14.setItemData(selectList);
        editSpinner_14.setDisabled(false);
        editSpinner_19.setItemData(selectList);
        editSpinner_19.setDisabled(false);
        editSpinner_20.setItemData(selectList);
        editSpinner_20.setDisabled(false);
        if (!TextUtils.isEmpty(mData.csbmjbr)) {
            editSpinner_14.setText(mData.csbmjbr);
        }
        if (!TextUtils.isEmpty(mZlyczData.zxlyczjbr)) {
            editSpinner_19.setText(mZlyczData.zxlyczjbr);
        }
        if (!TextUtils.isEmpty(mZlyczData.ckjbr)) {
            editSpinner_20.setText(mZlyczData.ckjbr);
        }
        editSpinner_14.setOnInsertListener(new SearchSpinner.onInsertListener() {
            @Override
            public void insertChange(String name, String value, boolean isFromSelect) {
                mData.csbmjbr = name;
            }
        });
        editSpinner_19.setOnInsertListener(new SearchSpinner.onInsertListener() {
            @Override
            public void insertChange(String name, String value, boolean isFromSelect) {
                mZlyczData.zxlyczjbr = name;
            }
        });
        editSpinner_20.setOnInsertListener(new SearchSpinner.onInsertListener() {
            @Override
            public void insertChange(String name, String value, boolean isFromSelect) {
                mZlyczData.ckjbr = name;
            }
        });

    }

    @Override
    public void getProductDeviceListSuccess(List<ProductDeviceBean> list) {
        String json = GsonUtil.GsonToString(list);
        productDeviceList = GsonUtil.JsonToList(json, ProductDeviceBean.class);

        if (isNew) {
            if (productDeviceList != null && productDeviceList.size() != 0) {
                mData.csssbm = productDeviceList.get(0).scssbh;
                mData.scssmc = productDeviceList.get(0).scssmc;
                et_item13.setText(mData.scssmc);
                et_item23.setText(mData.csssbm);
            } else {
                mData.csssbm = "";
                mData.scssmc = "";
            }
        }

    }

    @Override
    public void checkPrintableSuccess(String result) {

    }


    private SelectPopupWindow selectPopupWindow;

    //初始化下拉弹窗
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

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                item.setText(model.NAME);
                //计量单位,虚拟标签类型,物理特性,包装方式,包装规模,自行利用处置类型
                switch (mType) {
                    case "计量单位":
                        mData.jldw = model.ID;
                        changeUnit(mData.jldw);
                        break;
                    case "虚拟标签类型":
                        mData.xnbqlx = model.ID;
                        break;
                    case "物理特性":
                        mData.wlxz = model.ID;
                        break;
                    case "包装方式":
                        mData.rqbzlx = model.ID;
                        break;
                    case "包装规模":
                        mData.rqbzgg = model.ID;
                        break;
                    case "自行利用处置类型":
                        mZlyczData.lyczlx = model.ID;
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
                mData.csssbm = bean.scssbh;
                mData.scssmc = bean.scssmc;
                et_item13.setText(mData.scssmc);
                et_item23.setText(mData.csssbm);
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item13.getParent(), sort);
    }


    //初始利用处置设施下拉弹窗
    private void showSsPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < zxlyssList.size(); i++) {
            GfYbqyXxbaZxlyczss bean = zxlyssList.get(i);
            String name = getString(R.string.str_record_fwlb2, bean.ssbh, bean.ssmc);
            selectPopupWindowList.add(new SelectPopupWindowBean(bean.id, name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                GfYbqyXxbaZxlyczss bean = new GfYbqyXxbaZxlyczss();
                for (int i = 0; i < zxlyssList.size(); i++) {
                    if (zxlyssList.get(i).id.equals(model.ID)) {
                        bean = zxlyssList.get(i);
                    }
                }


                mZlyczData.ssbh = bean.ssbh;
                mZlyczData.ssmc = bean.ssmc;
                mZlyczData.sslx = bean.czsslx;
                mZlyczData.lyczfs = bean.lyczfsdl;
                mZlyczData.lyczfsxl = bean.lyczfsxl;
                mZlyczData.fwdlbh = bean.fwdlbh;
                mZlyczData.fwdlmc = bean.fwdlmc;
                mZlyczData.fwxlbh = bean.fwxlbh;
                mZlyczData.fwxlmc = bean.fwxlmc;
                mZlyczData.fwxxmc = bean.fwxxmc;

                et_item21.setText(mZlyczData.ssbh);
                et_item22.setText(mZlyczData.ssmc);
                et_item24.setText(getString(R.string.str_record_fwlb2, mZlyczData.lyczfs, commonTransfer(mZlyczData.lyczfs, "危险废物利用处置方式代码")));
                setXlmc();
//                et_item28.setText(mZlyczData.lyczfsxl);
                et_item29.setText(mZlyczData.fwdlbh);
                et_item30.setText(mZlyczData.fwdlmc);
                et_item31.setText(mZlyczData.fwxlbh);
                et_item32.setText(mZlyczData.fwxlmc);

                //计量单位处理
                if (!TextUtils.isEmpty(bean.jldw)) {
                    mZlyczData.jldw = bean.jldw;
                    et_item34Init();
                }

                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item21.getParent(), sort);
    }


}
