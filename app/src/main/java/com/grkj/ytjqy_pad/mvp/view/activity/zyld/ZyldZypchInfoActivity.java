package com.grkj.ytjqy_pad.mvp.view.activity.zyld;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.zyld.GfWxfwZyldFwxxPch;
import com.grkj.ytjqy_pad.mvp.presenter.ZyldZypchInfoPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.view.MyLinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2026/9/15.
 * 转移批次号信息编辑
 */
public class ZyldZypchInfoActivity extends BaseActivity<ZyldZypchInfoPresenter> implements ZyldZypchInfoPresenter.View {

    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;


    //转移数量
    @BindView(R.id.et_item_zysl)
    EditText et_item_zysl;

    //计量单位
    @BindView(R.id.et_item_jldw)
    EditText et_item_jldw;

    //数字识别码
    @BindView(R.id.et_item_szsbm)
    EditText et_item_szsbm;

    //接受数量
    @BindView(R.id.et_item_jssl)
    EditText et_item_jssl;

    //签收标识
    @BindView(R.id.et_item_qsbs)
    EditText et_item_qsbs;

    //是否存在重大差异
    @BindView(R.id.et_item_sfczzdcy)
    EditText et_item_sfczzdcy;


    //保存
    @BindView(R.id.ll_save)
    LinearLayout ll_save;


    //是否存在重大差异
    @BindView(R.id.ll_sfczzdcy)
    LinearLayout ll_sfczzdcy;


    private GfWxfwZyldFwxxPch mData = null;

    private boolean isXnbq = false;

    private boolean editable = true;//是否可编辑
    private boolean isDbld = true;//是否待办联单

    @Override
    protected int getLayout() {
        return R.layout.activity_zyld_zypch_info;
    }


    @Override
    protected void initEventAndData() {

        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);
        editable = getIntent().getBooleanExtra("editable", true);
        isDbld = getIntent().getBooleanExtra("isDbld", true);

        GfWxfwZyldFwxxPch parentData = GsonUtil.GsonToBean(dataStr, GfWxfwZyldFwxxPch.class);
        mData = parentData;
        if (TextUtils.isEmpty(mData.jssl)) {
            mData.jssl = mData.zysl;
        }
        initView();

        Map<String, String> map = new HashMap<>();
        map.put("types", "联单废物批次号签收标识,联单废物是否存在重大差异");
        mPresenter.getParamList(map);

    }

    //视图回显
    private void initView() {

        if (editable) {
            ll_save.setVisibility(View.VISIBLE);
        } else {
            if (!isDbld) {
                ll_save.setVisibility(View.GONE);
            }
        }

        //转移数量
        et_item_zysl.setText(mData.zysl);
        //计量单位
        et_item_jldw.setText(mData.jldw);
        //数字识别码
        et_item_szsbm.setText(mData.szsbm);
        //接受数量
        et_item_jssl.setText(mData.jssl);

    }

    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("转移此批号信息");
    }


    @OnClick({R.id.ll_save, R.id.et_item_qsbs, R.id.et_item_sfczzdcy})
    public void onViewClicked(View view) {
        switch (view.getId()) {


            // 签收标识
            case R.id.et_item_qsbs:
                showPopWindow("联单废物批次号签收标识", et_item_qsbs);
                break;

            // 是否存在重大差异
            case R.id.et_item_sfczzdcy:
                showPopWindow("联单废物是否存在重大差异", et_item_sfczzdcy);
                break;


            // 保存
            case R.id.ll_save:
                if (TextUtils.isEmpty(checkEmpty())) {
                    mData.jssl = et_item_jssl.getText().toString().trim();
                    mPresenter.showSaveDialog(buildSaveParams());
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
        if (TextUtils.equals(mData.qsbs, "0") && TextUtils.isEmpty(et_item_sfczzdcy.getText().toString().trim())) {//拒收
            return "请选择是否存在重大差异！";
        }

//
//        if (isXnbq && TextUtils.isEmpty(et_item_zcsstybh.getText().toString().trim())){
//            return "请选择贮存设施统一编号！";
//        }
//
//        if (TextUtils.isEmpty(et_item_bzfs.getText().toString().trim())){
//            return "请选择包装方式！";
//        }

        return "";
    }


    /*
     * 组装保存参数（废物信息+转移批次号列表）
     * */
    private Map<String, Object> buildSaveParams() {
        Map<String, Object> params = GsonUtil.GsonToMaps(GsonUtil.GsonToString(mData));
        return params;
    }


    @Override
    protected ZyldZypchInfoPresenter createPresenter() {
        return new ZyldZypchInfoPresenter();
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
            //签收标识
            et_item_qsbs.setText(commonTransfer(mData.qsbs, "联单废物批次号签收标识"));
            //是否重大差异
            et_item_sfczzdcy.setText(commonTransfer(mData.sfczzdcy, "联单废物是否存在重大差异"));

            if (TextUtils.equals(mData.qsbs, "0")) {
                ll_sfczzdcy.setVisibility(View.VISIBLE);
            } else {
                ll_sfczzdcy.setVisibility(View.GONE);
            }
        }

    }


    @Override
    public void saveDataSuccess(Object bean) {
        ToastUtil.showToast(mContext, "保存成功！");
        setResult(9);
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
                //包装方式
                switch (mType) {
                    case "联单废物批次号签收标识":
                        mData.qsbs = model.ID;
                        if (TextUtils.equals(mData.qsbs, "0")) {//拒收显示
                            ll_sfczzdcy.setVisibility(View.VISIBLE);
                            et_item_jssl.setText("0");
                        } else {
                            mData.sfczzdcy = "";
                            et_item_sfczzdcy.setText("");
                            ll_sfczzdcy.setVisibility(View.GONE);
                        }

                        break;
                    case "联单废物是否存在重大差异":
                        mData.sfczzdcy = model.ID;
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
