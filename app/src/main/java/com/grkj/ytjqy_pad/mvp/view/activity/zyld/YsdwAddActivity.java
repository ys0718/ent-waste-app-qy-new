package com.grkj.ytjqy_pad.mvp.view.activity.zyld;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.zyld.GfWxfwZyldYsxx;
import com.grkj.ytjqy_pad.mvp.presenter.YsdwAddPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2026/9/14.
 * 添加运输单位信息
 */
public class YsdwAddActivity extends BaseActivity<YsdwAddPresenter> implements YsdwAddPresenter.View {

    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //标题
    @BindView(R.id.tv_title)
    TextView tv_title;

    //主页
    @BindView(R.id.ll_home)
    LinearLayout ll_home;

    //运输单位名称
    @BindView(R.id.et_item_ysdwmc)
    EditText et_item_ysdwmc;

    //名称校验提示
    @BindView(R.id.tv_ysdwmc_error)
    TextView tv_ysdwmc_error;

    //查询按钮
    @BindView(R.id.ll_query)
    LinearLayout ll_query;

    //运输类型
    @BindView(R.id.et_item_yslx)
    EditText et_item_yslx;

    //运输方式
    @BindView(R.id.et_item_ysfs)
    EditText et_item_ysfs;

    //运输企业ID
    @BindView(R.id.et_item_ysqyid)
    EditText et_item_ysqyid;

    //运输单位联系人
    @BindView(R.id.et_item_ysdwlxr)
    EditText et_item_ysdwlxr;

    //运输单位联系人手机
    @BindView(R.id.et_item_ysdwlxrsj)
    EditText et_item_ysdwlxrsj;

    //道路运输经营许可证号
    @BindView(R.id.et_item_ysdwxkzh)
    EditText et_item_ysdwxkzh;

    //统一社会信用代码
    @BindView(R.id.et_item_ysdwxydm)
    EditText et_item_ysdwxydm;

    //运输单位地址
    @BindView(R.id.et_item_ysdwdz)
    EditText et_item_ysdwdz;

    //返回
    @BindView(R.id.ll_back)
    LinearLayout ll_back;

    //保存
    @BindView(R.id.ll_save)
    LinearLayout ll_save;

    private GfWxfwZyldYsxx mData = null;


    @Override
    protected int getLayout() {
        return R.layout.activity_ysdw_add;
    }


    @Override
    protected void initEventAndData() {

        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);
        if (TextUtils.isEmpty(dataStr)) {//新增
            mData = new GfWxfwZyldYsxx();
        } else {
            GfWxfwZyldYsxx parentData = GsonUtil.GsonToBean(dataStr, GfWxfwZyldYsxx.class);
            mData = parentData;
            initView();
        }

        Map<String, String> map = new HashMap<>();
        map.put("types", "联单运输类型,联单运输方式");
        mPresenter.getParamList(map);

    }

    //视图回显
    private void initView() {

        //运输单位名称
        et_item_ysdwmc.setText(mData.ysdwmc);
        //运输企业ID
        et_item_ysqyid.setText(mData.ysqyid);
        //运输单位联系人
        et_item_ysdwlxr.setText(mData.lxr);
        //运输单位联系人手机
        et_item_ysdwlxrsj.setText(mData.lxrsj);
        //道路运输经营许可证号
        et_item_ysdwxkzh.setText(mData.dlysjyxkzh);
        //统一社会信用代码
        et_item_ysdwxydm.setText(mData.tyshxydm);
        //运输单位地址
        et_item_ysdwdz.setText(mData.ysdwdz);

    }

    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("运输单位信息");
    }


    @OnClick({R.id.ll_back, R.id.ll_save, R.id.ll_query, R.id.et_item_yslx, R.id.et_item_ysfs})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            // 返回
            case R.id.ll_back:
                finish();
                break;

            // 运输类型
            case R.id.et_item_yslx:
                showPopWindow("联单运输类型", et_item_yslx);
                break;

            // 运输方式
            case R.id.et_item_ysfs:
                showPopWindow("联单运输方式", et_item_ysfs);
                break;

            // 查询运输单位
            case R.id.ll_query:
                if (TextUtils.isEmpty(et_item_ysdwmc.getText().toString().trim())) {
                    ToastUtil.showToast(mContext, "请输入运输单位名称！");
                } else {
                    mPresenter.getYsqyInfo(et_item_ysdwmc.getText().toString().trim());
                }
                break;

            // 保存
            case R.id.ll_save:
                if (TextUtils.isEmpty(checkEmpty())) {
                    fillData();
                    mPresenter.showSaveDialog(mData);
                } else {
                    ToastUtil.showToast(mContext, checkEmpty());
                }
                break;

        }
    }


    /*
     * 判空处理
     * */
    private String checkEmpty() {
        if (TextUtils.isEmpty(et_item_ysdwmc.getText().toString().trim())) {
            tv_ysdwmc_error.setVisibility(View.VISIBLE);
            return "请输入运输单位名称！";
        }
        tv_ysdwmc_error.setVisibility(View.GONE);

        if (TextUtils.isEmpty(et_item_yslx.getText().toString().trim())) {
            return "请选择运输类型！";
        } else if (TextUtils.isEmpty(et_item_ysfs.getText().toString().trim())) {
            return "请选择运输方式！";
        }
        return "";
    }


    /*
     * 采集表单数据
     * */
    private void fillData() {
        //运输单位名称
        mData.ysdwmc = et_item_ysdwmc.getText().toString().trim();
        //运输企业ID
        mData.ysqyid = et_item_ysqyid.getText().toString().trim();
        //运输单位联系人
        mData.ysdwlxr = et_item_ysdwlxr.getText().toString().trim();
        //运输单位联系人手机
        mData.ysdwlxrsj = et_item_ysdwlxrsj.getText().toString().trim();
        //道路运输经营许可证号
        mData.dlysjyxkzh = et_item_ysdwxkzh.getText().toString().trim();
        //统一社会信用代码
        mData.tyshxydm = et_item_ysdwxydm.getText().toString().trim();
        //运输单位地址
        mData.ysdwdz = et_item_ysdwdz.getText().toString().trim();
    }


    @Override
    protected YsdwAddPresenter createPresenter() {
        return new YsdwAddPresenter();
    }

    @Override
    public void showError(String msg) {
        toastShow(msg);
    }


    @Override
    public void getParamListSuccess(List<ParamListBean> bean) {
        String json = GsonUtil.GsonToString(bean);
        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);

        if (mData != null) {
            //运输类型
            et_item_yslx.setText(commonTransfer(mData.yslx, "联单运输类型"));
            //运输方式
            et_item_ysfs.setText(commonTransfer(mData.ysfs, "联单运输方式"));
        }

    }


    @Override
    public void getYsqyInfoSuccess(GfWxfwZyldYsxx bean) {
        if (bean == null) {
            ToastUtil.showToast(mContext, "未查询到该运输单位信息！");
            return;
        }

        //运输企业ID
        et_item_ysqyid.setText(bean.ysqyid);
        //运输单位联系人
        et_item_ysdwlxr.setText(bean.ysdwlxr);
        //运输单位联系人手机
        et_item_ysdwlxrsj.setText(bean.ysdwlxrsj);
        //道路运输经营许可证号
        et_item_ysdwxkzh.setText(bean.dlysjyxkzh);
        //统一社会信用代码
        et_item_ysdwxydm.setText(bean.tyshxydm);
        //运输单位地址
        et_item_ysdwdz.setText(bean.ysdwdz);
    }


    @Override
    public void saveDataSuccess(Object bean) {
        ToastUtil.showToast(mContext, "保存成功！");
        setResult(10);
        finish();
    }


    private SelectPopupWindow selectPopupWindow;
    private List<ParamListBean> allParamList = new ArrayList<>();


    //初始化下拉弹窗
    private void showPopWindow(String type, final EditText item) {
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
                //运输类型,运输方式
                switch (mType) {
                    case "联单运输类型":
                        mData.yslx = model.ID;
                        break;
                    case "联单运输方式":
                        mData.ysfs = model.ID;
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
