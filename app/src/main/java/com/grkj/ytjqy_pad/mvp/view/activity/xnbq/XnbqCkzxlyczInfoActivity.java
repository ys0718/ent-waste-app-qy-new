package com.grkj.ytjqy_pad.mvp.view.activity.xnbq;

import android.text.TextUtils;
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
import com.grkj.ytjqy_pad.mvp.model.xnbq.GfYbqyXxbaZxlyczss;
import com.grkj.ytjqy_pad.mvp.presenter.XnbqCkzxlyczInfoPresenter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2026/10/28.
 * 虚拟标签-立产立清-出库自行利用处置信息
 */

public class XnbqCkzxlyczInfoActivity extends WeightBaseActivity<XnbqCkzxlyczInfoPresenter> implements XnbqCkzxlyczInfoPresenter.View {

    private GfYbqyTzCkZxlyczXnbq mData = new GfYbqyTzCkZxlyczXnbq();

    //自行利用/处置时间
    @BindView(R.id.et_item2)
    EditText et_item2;

    //计量单位
    @BindView(R.id.et_item17)
    EditText et_item17;

    //废物大类编号
    @BindView(R.id.et_item3)
    EditText et_item3;

    //废物小类编号
    @BindView(R.id.et_item4)
    EditText et_item4;

    //废物小类名称
    @BindView(R.id.et_item5)
    EditText et_item5;

    //废物大类名称
    @BindView(R.id.et_item6)
    EditText et_item6;

    //废物详细名称
    @BindView(R.id.et_item7)
    EditText et_item7;

    //利用处置设施编码
    @BindView(R.id.et_item13)
    EditText et_item13;

    //利用处置方式
    @BindView(R.id.et_item14)
    EditText et_item14;

    //利用处置设施类型
    @BindView(R.id.et_item15)
    EditText et_item15;

    //利用处置设施名称
    @BindView(R.id.et_item16)
    EditText et_item16;

    //利用处置方式小类
    @BindView(R.id.et_item25)
    EditText et_item25;

    //自行利用处置类型
    @BindView(R.id.et_item23)
    EditText et_item23;

    //运送部门经办人
    @BindView(R.id.et_item18)
    HistorySearchSpinner et_item18;

    //出库部门经办人
    @BindView(R.id.editSpinner_14)
    SearchSpinner editSpinner_14;

    //自行利用/处置部门经办人
    @BindView(R.id.editSpinner_19)
    SearchSpinner editSpinner_19;

    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //删除按钮
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;

    //保存按钮
    @BindView(R.id.ll_save)
    LinearLayout ll_save;

    private boolean enableSave = true;

    private List<GfYbqyXxbaZxlyczss> zxlyssList = new ArrayList<>();
    private List<UserBean> temUserList = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_xnbq_ckzxlycz_info;
    }

    @Override
    protected void initEventAndData() {
        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);

        initWeight();

        if (TextUtils.isEmpty(dataStr)) {//新增
            isNew = true;
            isGetWeight = true;
            mData = new GfYbqyTzCkZxlyczXnbq();
            mData.type = "1";//1-出库 2-立产立清-立产立处置
            et_item2.setText(DateTimeUtil.getCurrentDate());
            et_item23.setText("出库自行利用处置");
            mData.lyczlx = "2";
            mData.zxlyczsj = et_item2.getText().toString();
            editSpinner_19.setText(SPUtils.get(mContext, Constants.USER_NAME, "").toString());
            editSpinner_14.setText(SPUtils.get(mContext, Constants.USER_NAME, "").toString());
            mData.jldw = getJldw();
            et_item17.setText(mData.jldw);
            handleReadOnly(false);
        } else {//编辑
            et_item2.setEnabled(false);
            et_item2.setClickable(false);
            handleReadOnly(true);
            mData = GsonUtil.GsonToBean(dataStr, GfYbqyTzCkZxlyczXnbq.class);
            ll_delete.setVisibility(View.VISIBLE);
            ll_save.setVisibility(View.GONE);
        }

        mPresenter.getZxlyssList();
        Map<String, String> map = new HashMap<>();
        map.put("types", "计量单位,自行利用处置类型,危险废物利用处置方式代码");
        mPresenter.getParamList(map);
        mPresenter.getUserList("");

    }

    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("出库自行利用处置");
    }

    private void initView() {
        et_item2.setText(mData.zxlyczsj);
        et_item13.setText(mData.ssbh);
        et_item16.setText(mData.ssmc);
        et_item15.setText(mData.sslx);
        et_item14.setText(mData.lyczfs);
        et_item25.setText(mData.lyczfsxl);
        et_item3.setText(mData.fwdlbh);
        et_item6.setText(mData.fwdlmc);
        et_item4.setText(mData.fwxlbh);
        et_item5.setText(mData.fwxlmc);
        et_item7.setText(mData.fwxxmc);
        et_item8.setText(mData.zxlyczl);
        et_item17.setText(TextUtils.isEmpty(mData.jldw) ? getJldw() : mData.jldw);
        et_item23.setText(getString(R.string.str_record_fwlb2, mData.lyczlx, commonTransfer(mData.lyczlx, "自行利用处置类型")));
        et_item18.setText(mData.ysjbr);
        editSpinner_19.setText(mData.zxlyczjbr);
    }


    @OnClick({R.id.et_item13, R.id.et_item17, R.id.et_item23,
            R.id.ll_back, R.id.ll_save, R.id.ll_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //利用处置设施编号
            case R.id.et_item13:
                showSsPopWindow();
                break;


            //计量单位
            case R.id.et_item17:
                showPopWindow("计量单位", et_item17);
                break;

            //自行利用处置类型
            case R.id.et_item23:
                showPopWindow("自行利用处置类型", et_item23);
                break;

            // 返回
            case R.id.ll_back:
                finish();
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
        if (!enableSave) {
            ToastUtil.showToast(mContext, "数据正在保存，请勿频繁操作");
            return;
        }

        et_item18.clearFocus();
        mData.zxlyczsj = et_item2.getText().toString();
        mData.zxlyczl = et_item8.getText().toString();
        mData.ckjbr = editSpinner_14.getText().toString();
        mData.zxlyczjbr = editSpinner_19.getText().toString();
        mData.ysjbr = et_item18.getText();
        mData.jldw = et_item17.getText().toString();

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

        if (TextUtils.isEmpty(mData.zxlyczsj)) {
            return "请选择自行利用/处置时间！";
        } else if (TextUtils.isEmpty(mData.zxlyczl)) {
            return "请输入/地磅获取自行利用预估量！";
        } else if (Double.valueOf(mData.zxlyczl) <= 0) {
            return "请输入/地磅获取正确的自行利用预估量！";
        } else if (TextUtils.isEmpty(mData.ssbh)) {
            return "请选择利用处置设施编码！";
        } else if (TextUtils.isEmpty(mData.lyczlx)) {
            return "请选择自行利用处置类型！";
        } else if (TextUtils.isEmpty(mData.ckjbr)) {
            return "请输入出库部门经办人！";
        } else if (TextUtils.isEmpty(mData.zxlyczjbr)) {
            return "请输入自行利用/处置部门经办人！";
        } else if (TextUtils.isEmpty(mData.ysjbr)) {
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
                mPresenter.saveData(map, ApiUrl.getHnUrl(ApiUrl.URL_XNBQ_CKZXLICZ_SAVE));
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
    protected XnbqCkzxlyczInfoPresenter createPresenter() {
        return new XnbqCkzxlyczInfoPresenter();
    }


    @Override
    public void showError(String msg) {
        enableSave = true;
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
            initView();
        }
    }

    @Override
    public void saveDataSuccess(Object bean) {
        enableSave = true;
        setResult(9);
        ToastUtil.showToast(mContext, "保存成功！");
        finish();
    }

    @Override
    public void getCshjInfoSuccess(Object bean) {

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
        if (!TextUtils.isEmpty(mData.ckjbr)) {
            editSpinner_14.setText(mData.ckjbr);
        }
        if (!TextUtils.isEmpty(mData.zxlyczjbr)) {
            editSpinner_19.setText(mData.zxlyczjbr);
        }
        editSpinner_14.setOnInsertListener(new SearchSpinner.onInsertListener() {
            @Override
            public void insertChange(String name, String value, boolean isFromSelect) {
                mData.ckjbr = name;
            }
        });
        editSpinner_19.setOnInsertListener(new SearchSpinner.onInsertListener() {
            @Override
            public void insertChange(String name, String value, boolean isFromSelect) {
                mData.zxlyczjbr = name;
            }
        });

    }

    @Override
    public void getProductDeviceListSuccess(List<ProductDeviceBean> list) {

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
                    ParamListBean bean = childList.get(j);
                    if ("自行利用处置类型".equals(allParamList.get(i).type)) {
//                        String groupName = getString(R.string.str_record_fwlb2, bean.code, bean.value);
                        SelectPopupWindowBean popupWindowBean = new SelectPopupWindowBean(bean.code, bean.value);
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
                //计量单位,自行利用处置类型
                switch (mType) {
                    case "自行利用处置类型":
                        mData.lyczlx = model.ID;
                        break;
                    case "计量单位":
                        mData.jldw = model.ID;
                        changeUnit(mData.jldw);
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


                mData.ssbh = bean.ssbh;
                mData.ssmc = bean.ssmc;
                mData.sslx = bean.czsslx;
                mData.lyczfs = bean.lyczfsdl;
                mData.lyczfsxl = bean.lyczfsxl;
                mData.fwdlbh = bean.fwdlbh;
                mData.fwdlmc = bean.fwdlmc;
                mData.fwxlbh = bean.fwxlbh;
                mData.fwxlmc = bean.fwxlmc;
                mData.fwxxmc = bean.fwxxmc;

                et_item13.setText(mData.ssbh);
                et_item16.setText(mData.ssmc);
                et_item15.setText(commonTransfer(mData.lyczfs, "危险废物利用处置方式代码"));
                mData.sslx = et_item15.getText().toString();
                et_item14.setText(getString(R.string.str_record_fwlb2, mData.lyczfs, commonTransfer(mData.lyczfs, "危险废物利用处置方式代码")));
                et_item25.setText(mData.lyczfsxl);
                et_item3.setText(mData.fwdlbh);
                et_item6.setText(mData.fwdlmc);
                et_item4.setText(mData.fwxlbh);
                et_item5.setText(mData.fwxlmc);
                et_item7.setText(mData.fwxxmc);

                //计量单位处理
                if (!TextUtils.isEmpty(bean.jldw)) {
                    mData.jldw = bean.jldw;
                    et_item17.setText(mData.jldw);
                }

                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }
        selectPopupWindow.show(mLayout, "", (View) et_item13.getParent(), sort);
    }


}
