package com.grkj.ytjqy_pad.mvp.view.activity.hn;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.CollectInfoBean;
import com.grkj.ytjqy_pad.mvp.model.CollectInfoChildBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.presenter.CollectInfoChildPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.printer2.NewPrintLabelActivity;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.view.MyLinearLayout;
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
 * 社会收集源信息-子表
 */

public class CollectInfoChildActivity extends BaseActivity<CollectInfoChildPresenter> implements CollectInfoChildPresenter.View {

    private CollectInfoChildBean mData = new CollectInfoChildBean();

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

    //计量单位
    @BindView(R.id.et_item17)
    EditText et_item17;

    //删除
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;

    //保存
    @BindView(R.id.ll_save)
    LinearLayout ll_save;

    //打印标签
    @BindView(R.id.ll_print)
    LinearLayout ll_print;

    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //表单布局
    @BindView(R.id.ll_clickable)
    MyLinearLayout ll_clickable;

    private String dataStr;
    private String mainId;
    private boolean isNew;
    private CollectInfoBean parentBean = new CollectInfoBean();

    @Override
    protected int getLayout() {
        return R.layout.activity_collect_info_child;
    }

    @Override
    protected void initEventAndData() {
        dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);

        String parentStr = getIntent().getStringExtra(Constants.DATA_PARENT);
        parentBean = GsonUtil.GsonToBean(parentStr, CollectInfoBean.class);
        mainId = parentBean.id;


        if (TextUtils.isEmpty(dataStr)) {
            mData = new CollectInfoChildBean();
            isNew = true;
            ll_delete.setVisibility(View.GONE);
            ll_print.setVisibility(View.GONE);
        } else {
            mData = GsonUtil.GsonToBean(dataStr, CollectInfoChildBean.class);
            showHideBtn();
            ll_clickable.setOnInterceptTouchEvent(true, getResources().getColor(R.color.gray));
            isNew = false;
        }

        Map<String, String> map = new HashMap<>();
        map.put("types", "危险废物利用处置方式代码,包装方式,包装材质,包装规模,计量单位,物理特性");
        mPresenter.getParamList(map);
        mPresenter.getFwlbList(parentBean.xkzId);
    }


    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();

    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("入库信息");
    }

    private void initView(boolean isSelectWaste) {
        et_item2.setText(mData.fwdlbh);
        et_item3.setText(mData.fwxlbh);
        et_item4.setText(mData.fwxxmc);

        et_item5.setText(commonTransfer(mData.rqbzlx, KEY_BZLX));
        et_item6.setText(commonTransfer(mData.rqbzcz, KEY_BZCZ));
        et_item7.setText(commonTransfer(mData.rqbzgg, KEY_BZGM));
        et_item1.setText(mData.lyczfs);

        if (!isSelectWaste) {
            et_item8.setText(mData.rkl);
            et_item9.setText(mData.remark);
            et_item17.setText(TextUtils.isEmpty(mData.jldw) ? getJldw() : mData.jldw);
        }
    }


    /*
     * 显示/隐藏按钮
     * */
    private void showHideBtn() {
        if (TextUtils.equals("1", parentBean.commitStatus)) {
            ll_delete.setVisibility(View.GONE);
            ll_save.setVisibility(View.GONE);
            ll_print.setVisibility(View.GONE);
            ll_clickable.setOnInterceptTouchEvent(true, getResources().getColor(R.color.gray));
        } else {
            ll_delete.setVisibility(View.VISIBLE);
            ll_save.setVisibility(View.VISIBLE);
            ll_print.setVisibility(View.VISIBLE);
            ll_clickable.setOnInterceptTouchEvent(false, getResources().getColor(R.color.gray));
        }
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


    @OnClick({R.id.ll_back, R.id.ll_save, R.id.ll_delete, R.id.et_item1, R.id.et_item2, R.id.et_item4, R.id.et_item17, R.id.ll_print})
    public void onViewClicked(View view) {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();
        switch (view.getId()) {
//            case R.id.et_item1://危险废物利用处置方式代码
//                showPopWindow(KEY_LYZCFS, et_item1, groupParamList(KEY_LYZCFS));
//                break;
            case R.id.et_item17://计量单位
                showPopWindow(KEY_JLDW, et_item17, groupParamList(KEY_JLDW));
                break;

            case R.id.et_item2://废物类别
                for (int i = 0; i < typeList.size(); i++) {
                    CollectInfoChildBean mData = typeList.get(i);
                    String name = getString(R.string.str_record_fwlb, mData.fwxxmc, mData.fwdlbh, mData.fwdlmc, mData.fwxlbh);
                    selectPopupWindowList.add(new SelectPopupWindowBean(typeList.get(i).id, name));
                }

                showPopWindow(KEY_FWLB, et_item2, selectPopupWindowList);

                break;


            case R.id.ll_save://保存

                mData.rkl = et_item8.getText().toString();
                mData.remark = et_item9.getText().toString();
                mData.jldw = et_item17.getText().toString();
                mData.mainid = mainId;
                if (isNew) {
                    mData.id = "";
                    mData.rksj = parentBean.sjrq;
                }

                if (TextUtils.isEmpty(checkEmpty())) {
                    if (TextUtils.isEmpty(mainId)) {
                        Intent intent = getIntent();
                        intent.putExtra("selectBean", GsonUtil.GsonToString(mData));
                        setResult(10, intent);
                        finish();
                    } else {
                        showSaveDialog();
                    }


                } else {
                    ToastUtil.showToast(mContext, checkEmpty());
                }

                break;

            case R.id.ll_print://打印标签
                mPresenter.getLabelInfo(mData.id);
                break;

            // 删除
            case R.id.ll_delete:
                showDeleteDialog();
                break;

        }
    }

    /*
     * 判空处理
     * */
    private String checkEmpty() {
         if (TextUtils.isEmpty(mData.fwdlbh)) {
            return "请选择危废类别！";
        } else if (TextUtils.isEmpty(mData.fwdlbh)) {
            return "请输入入库量！";
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
                Map<String, String> map = GsonUtil.GsonToMaps(GsonUtil.GsonToString(mData));
                mPresenter.saveData(map, ApiUrl.URL_COLLECT_SAVE_CHILD);
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
    protected CollectInfoChildPresenter createPresenter() {
        return new CollectInfoChildPresenter();
    }


    @Override
    public void showError(String msg) {
        ToastUtil.showToast(mContext, msg);
    }

    private List<ParamListBean> allParamList = new ArrayList<>();

    @Override
    public void getParamListSuccess(List<ParamListBean> list) {
        String json = GsonUtil.GsonToString(list);
        KLog.e(list.size());
        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);
        initView(false);
    }

    @Override
    public void saveDataSuccess(Object bean) {
        ToastUtil.showToast(mContext, "保存成功！");
        mData = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), CollectInfoChildBean.class);
        setResult(9);
        finish();
    }


    @Override
    public void deleteDataSuccess(Object bean) {
        ToastUtil.showToast(mContext, "删除成功！");
        setResult(9);
        finish();
    }

    private List<CollectInfoChildBean> typeList = new ArrayList<>();

    @Override
    public void getFwlbListSuccess(List<CollectInfoChildBean> list) {
        String json = GsonUtil.GsonToString(list);
        typeList = GsonUtil.JsonToList(json, CollectInfoChildBean.class);
    }

    @Override
    public void getLabelInfoSuccess(Object bean) {
        LabelInfoBean data = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), LabelInfoBean.class);
        data.WLXZ = commonTransfer(data.WLXZ, "物理特性");
        startActivityForResult(new Intent(mContext, NewPrintLabelActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(data)), 10);
    }


    private SelectPopupWindow selectPopupWindow;

    private static final String KEY_LYZCFS = "危险废物利用处置方式代码";
    private static final String KEY_FWLB = "废物类别";
    private static final String KEY_BZLX = "包装方式";
    private static final String KEY_BZCZ = "包装材质";
    private static final String KEY_BZGM = "包装规模";
    private static final String KEY_JLDW = "计量单位";


    private List<SelectPopupWindowBean> groupParamList(String type) {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();
        for (int i = 0; i < allParamList.size(); i++) {
            List<ParamListBean> childList = allParamList.get(i).childList;

            if (childList != null && childList.size() != 0 && type.equals(allParamList.get(i).type)) {
                for (int j = 0; j < childList.size(); j++) {
                    selectPopupWindowList.add(new SelectPopupWindowBean(childList.get(j).code, childList.get(j).value));
                }
            }
        }
        return selectPopupWindowList;
    }


    private CollectInfoChildBean fwlbBean = new CollectInfoChildBean();

    //初始化选择地址下拉弹窗
    private void showPopWindow(String type, EditText item, List<SelectPopupWindowBean> selectPopupWindowList) {
        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                switch (mType) {

//                    case KEY_LYZCFS://危险废物利用处置方式代码
//                        mData.lyczfs = model.ID;
//                        item.setText(model.NAME);
//                        break;

                    case KEY_JLDW://计量单位
                        setJldw(model.NAME);
                        item.setText(model.NAME);
                        break;
                    case KEY_FWLB://废物类别

                        for (int i = 0; i < typeList.size(); i++) {
                            if (TextUtils.equals(typeList.get(i).id, model.ID)) {
                                fwlbBean = typeList.get(i);
                            }
                        }
                        fwlbBean.rqbzlx = fwlbBean.bzfs;
                        //将小类赋值到处置/利用方式中
                        fwlbBean.lyczfs = fwlbBean.lyczfsxl;
                        mData = fwlbBean;
                        initView(true);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 9) {
            if (requestCode == 9) {//复制
                setResult(9);
                finish();
            }

        }
    }


}
