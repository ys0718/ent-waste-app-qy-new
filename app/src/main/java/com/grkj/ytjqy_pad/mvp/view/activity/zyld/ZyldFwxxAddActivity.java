package com.grkj.ytjqy_pad.mvp.view.activity.zyld;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.model.zyld.GfWxfwZyldFwxx;
import com.grkj.ytjqy_pad.mvp.model.zyld.GfWxfwZyldFwxxPch;
import com.grkj.ytjqy_pad.mvp.model.zyld.JsdwxxBean;
import com.grkj.ytjqy_pad.mvp.presenter.ZyldFwxxAddPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.view.MyLinearLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2026/9/15.
 * 添加转移联单废物信息
 */
public class ZyldFwxxAddActivity extends BaseActivity<ZyldFwxxAddPresenter> implements ZyldFwxxAddPresenter.View {

    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //标题
    @BindView(R.id.tv_title)
    TextView tv_title;

    //主页
    @BindView(R.id.ll_home)
    LinearLayout ll_home;

    //废物信息
    @BindView(R.id.et_item_fwxx)
    EditText et_item_fwxx;

    //废物代码
    @BindView(R.id.et_item_fwdm)
    EditText et_item_fwdm;

    //废物名称
    @BindView(R.id.et_item_fwmc)
    EditText et_item_fwmc;

    //利用处置方式
    @BindView(R.id.et_item_lyczfs)
    EditText et_item_lyczfs;

    //产废信息统一编号
    @BindView(R.id.et_item_cfxxtybh)
    EditText et_item_cfxxtybh;

    //转移量(吨)
    @BindView(R.id.et_item_zyl)
    EditText et_item_zyl;

    //贮存设施统一编号
    @BindView(R.id.et_item_zcsstybh)
    EditText et_item_zcsstybh;

    //包装方式
    @BindView(R.id.et_item_bzfs)
    EditText et_item_bzfs;

    //处理意见
    @BindView(R.id.et_item_clyj)
    EditText et_item_clyj;

    //是否重大差异
    @BindView(R.id.et_item_sfczzdcy)
    EditText et_item_sfczzdcy;

    //包装方式简述
    @BindView(R.id.et_item_bzfsjs)
    EditText et_item_bzfsjs;

    //转移批次号列表
    @BindView(R.id.ll_pch_list)
    LinearLayout ll_pch_list;

    //添加批次号
    @BindView(R.id.ll_add_pch)
    LinearLayout ll_add_pch;

    //取消
    @BindView(R.id.ll_back)
    LinearLayout ll_back;

    //保存
    @BindView(R.id.ll_save)
    LinearLayout ll_save;

    //删除
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;

    //代办联单展示区域
    @BindView(R.id.ll_dbld)
    LinearLayout ll_dbld;

    //虚拟标签出库转移展示区域
    @BindView(R.id.ll_xnbq)
    LinearLayout ll_xnbq;

    //非虚拟标签出库转移展示区域
    @BindView(R.id.ll_not_xnbq)
    LinearLayout ll_not_xnbq;

    //废物信息区域
    @BindView(R.id.ll_fwxx)
    MyLinearLayout ll_fwxx;

    private GfWxfwZyldFwxx mData = null;

    //转移批次号列表
    private List<GfWxfwZyldFwxxPch> mPchList = new ArrayList<>();
    private boolean isXnbq = false;

    private boolean editable = true;//是否可编辑
    private boolean isDbld = true;//是否待办联单

    @Override
    protected int getLayout() {
        return R.layout.activity_zyld_fwxx_add;
    }


    @Override
    protected void initEventAndData() {

        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);
        editable = getIntent().getBooleanExtra("editable",true);
        isDbld = getIntent().getBooleanExtra("isDbld",true);
        String zylx = getIntent().getStringExtra("zylx");

        fwxxList = GsonUtil.JsonToList(getIntent().getStringExtra("fwList"), JsdwxxBean.Wtlysz.class);

        if (TextUtils.isEmpty(dataStr)) {//新增
            mData = new GfWxfwZyldFwxx();
            String mainId = getIntent().getStringExtra(Constants.MAINID);
            mData.mainid = mainId;
            ll_dbld.setVisibility(View.GONE);

        } else {
            GfWxfwZyldFwxx parentData = GsonUtil.GsonToBean(dataStr, GfWxfwZyldFwxx.class);
            mData = parentData;
            mPresenter.getCphList(parentData.id);
            ll_delete.setVisibility(View.VISIBLE);
            ll_fwxx.setOnInterceptTouchEvent(true, getResources().getColor(R.color.gray));
            initView();

        }
        isXnbq = TextUtils.equals("XNBQCKZY",zylx);
        if (isXnbq){//虚拟标签出库转移
            ll_xnbq.setVisibility(View.VISIBLE);
            ll_not_xnbq.setVisibility(View.GONE);
        }else {
            ll_xnbq.setVisibility(View.GONE);
            ll_not_xnbq.setVisibility(View.VISIBLE);
        }

        Map<String, String> map = new HashMap<>();
        map.put("types", "联单包装方式,联单废物处理意见,联单废物是否存在重大差异,");
        mPresenter.getParamList(map);
        mPresenter.getZcssList();

    }

    //视图回显
    private void initView() {

        if (editable){
            ll_save.setVisibility(View.VISIBLE);
            ll_delete.setVisibility(View.VISIBLE);
            ll_add_pch.setVisibility(View.VISIBLE);
        }else {
            if (!isDbld){
                ll_save.setVisibility(View.GONE);
            }

            ll_delete.setVisibility(View.GONE);
            ll_add_pch.setVisibility(View.GONE);
        }

        if (isDbld){
            ll_dbld.setVisibility(View.VISIBLE);
        }

        //废物信息
        et_item_fwxx.setText(mData.fwmc);
        //废物代码
        et_item_fwdm.setText(mData.fwdm);
        //废物名称
        et_item_fwmc.setText(mData.fwmc);
        //利用处置方式
        et_item_lyczfs.setText(mData.lyczfs);
        //产废信息统一编号
        et_item_cfxxtybh.setText(mData.cfxxtybh);
        //转移量(吨)
        et_item_zyl.setText(mData.zyl == null ? "0" : mData.zyl.toString());
        //贮存设施统一编号
        et_item_zcsstybh.setText(mData.zcsstybh);
        //包装方式简述
        et_item_bzfsjs.setText(mData.bzfsjs);


        //回显转移批次号列表
        fillPchView();

    }

    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("废物信息");
    }


    @OnClick({R.id.ll_back, R.id.ll_save, R.id.ll_add_pch, R.id.et_item_fwxx, R.id.et_item_zcsstybh,
            R.id.et_item_clyj, R.id.et_item_sfczzdcy, R.id.et_item_bzfs,R.id.ll_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            // 取消
            case R.id.ll_back:
                finish();
                break;

            // 废物信息
            case R.id.et_item_fwxx:
                showFwxxPopWindow();

                break;

            // 贮存设施统一编号
            case R.id.et_item_zcsstybh:
                showZcssPopWindow();
                break;

            // 处理意见
            case R.id.et_item_clyj:
                showPopWindow("联单废物处理意见", et_item_clyj);
                break;

            // 联单废物是否存在重大差异
            case R.id.et_item_sfczzdcy:
                showPopWindow("联单废物是否存在重大差异", et_item_sfczzdcy);
                break;

            // 包装方式
            case R.id.et_item_bzfs:
                showPopWindow("联单包装方式", et_item_bzfs);
                break;

            // 添加批次号
            case R.id.ll_add_pch:
                if (TextUtils.isEmpty(mData.id)){
                    ToastUtil.showToast(mContext, "请先保存数据！");
                    return;
                }

                goToActivity(new Intent(mContext, SelectMultiZypchListActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(mData)),9);
                fillPchView();
                break;

            // 保存
            case R.id.ll_save:
                if (TextUtils.isEmpty(checkEmpty())) {
                    fillData();
                    mPresenter.showSaveDialog(buildSaveParams());
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
        if (TextUtils.isEmpty(et_item_fwxx.getText().toString().trim())) {
            return "请选择废物信息！";
        }

        if (isXnbq && TextUtils.isEmpty(et_item_zcsstybh.getText().toString().trim())){
            return "请选择贮存设施统一编号！";
        }

        if (TextUtils.isEmpty(et_item_bzfs.getText().toString().trim())){
            return "请选择包装方式！";
        }

        return "";
    }


    /*
     * 采集表单数据
     * */
    private void fillData() {
        if (isXnbq){
            mData.zyl = et_item_zyl.getText().toString().trim();
        }

        //包装方式简述
        mData.bzfsjs = et_item_bzfsjs.getText().toString().trim();
    }


    /*
     * 组装保存参数（废物信息+转移批次号列表）
     * */
    private Map<String, Object> buildSaveParams() {
        Map<String, Object> params = GsonUtil.GsonToMaps(GsonUtil.GsonToString(mData));
        params.put("pchList", mPchList);
        return params;
    }


    /*
     * 填充转移批次号列表
     * */
    private void fillPchView() {
        ll_pch_list.removeAllViews();

        for (int i = 0; i < mPchList.size(); i++) {
            final GfWxfwZyldFwxxPch bean = mPchList.get(i);

            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_zyld_fwxx_pch, null);

            LinearLayout item_layout = itemView.findViewById(R.id.item_layout);

            //转移批次号
            TextView tv_item_jssl = itemView.findViewById(R.id.tv_item_jssl);
            tv_item_jssl.setText(TextUtils.isEmpty(bean.jssl) ? "-" : bean.jssl);
            //转移数量
            TextView tv_item_zysl = itemView.findViewById(R.id.tv_item_zysl);

            if (isLandscape){
                tv_item_zysl.setText(TextUtils.isEmpty(bean.zysl) ? "-" : bean.zysl.toString());
                tv_item_jssl.setText(TextUtils.isEmpty(bean.jssl) ? "-" : bean.jssl.toString());
            }else {
                tv_item_zysl.setText(TextUtils.isEmpty(bean.zysl) ? "-" : bean.zysl.toString() + " " + bean.jldw);
                tv_item_jssl.setText(TextUtils.isEmpty(bean.jssl) ? "-" : bean.jssl.toString() + " " + bean.jldw);
            }

            //计量单位
            TextView tv_item_jldw = itemView.findViewById(R.id.tv_item_jldw);
            tv_item_jldw.setText(TextUtils.isEmpty(bean.jldw) ? "-" : bean.jldw);
            //数字识别码
            TextView tv_item_szsbm = itemView.findViewById(R.id.tv_item_szsbm);
            tv_item_szsbm.setText(TextUtils.isEmpty(bean.szsbm) ? "-" : bean.szsbm);
            //数据来源（1-产生台账；2-出库台账）
            TextView tv_item_ly = itemView.findViewById(R.id.tv_item_ly);
            tv_item_ly.setText(transferDataSource(bean.dataSource));

            //删除
            LinearLayout ll_edit = itemView.findViewById(R.id.ll_edit);
            LinearLayout ll_delete = itemView.findViewById(R.id.ll_delete);

            if (!editable){
                ll_edit.setVisibility(View.INVISIBLE);
                ll_delete.setVisibility(View.INVISIBLE);
            }

            if (isDbld) {
                ll_edit.setVisibility(View.VISIBLE);

                item_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToActivity(new Intent(mContext,ZyldZypchInfoActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(bean)), 9);
                    }
                });

            }


            ll_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.deletePphData(bean.id);
                }
            });

            ll_pch_list.addView(itemView);
        }
    }


    /*
     * 数据来源转义（1-产生台账；2-出库台账）
     * */
    private String transferDataSource(String dataSource) {
        if (TextUtils.isEmpty(dataSource)) {
            return "-";
        } else if ("1".equals(dataSource)) {
            return "产生台账";
        } else if ("2".equals(dataSource)) {
            return "出库台账";
        }
        return dataSource;
    }


    @Override
    protected ZyldFwxxAddPresenter createPresenter() {
        return new ZyldFwxxAddPresenter();
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
            //包装方式
            et_item_bzfs.setText(commonTransfer(mData.bzfs, "联单包装方式"));
            //处理意见
            et_item_clyj.setText(commonTransfer(mData.clyj, "联单废物处理意见"));
            //是否重大差异
            et_item_sfczzdcy.setText(commonTransfer(mData.sfczzdcy, "联单废物是否存在重大差异"));
        }

    }


    @Override
    public void saveDataSuccess(Object bean) {
        ToastUtil.showToast(mContext, "保存成功！");
        setResult(9);
        finish();
    }

    @Override
    public void deleteDataSuccess(String msg) {
        ToastUtil.showToast(mContext, "删除成功！");
        setResult(9);
        finish();
    }

    @Override
    public void deletePchDataSuccess(String msg) {
        ToastUtil.showToast(mContext, "删除成功！");
        mPresenter.getCphList(mData.id);
    }


    private SelectPopupWindow selectPopupWindow;
    private List<ParamListBean> allParamList = new ArrayList<>();

    //贮存设施列表
    private List<ZcssBean> zcssList = new ArrayList<>();
    //废物信息列表
    private List<JsdwxxBean.Wtlysz> fwxxList = new ArrayList<>();


    @Override
    public void getZcssListSuccess(List<ZcssBean> bean) {
        String json = GsonUtil.GsonToString(bean);
        zcssList = GsonUtil.JsonToList(json, ZcssBean.class);

        //编辑时回显：根据贮存设施统一编号匹配并加载废物信息列表
//        if (mData != null && !TextUtils.isEmpty(mData.zcssid)) {
//            for (int i = 0; i < zcssList.size(); i++) {
//                if (mData.zcssid.equals(zcssList.get(i).id)) {
//                    mPresenter.getFwxxList(zcssList.get(i).id);
//                }
//            }
//        }
    }

    @Override
    public void getCphListSuccess(List<GfWxfwZyldFwxxPch> list) {
        mPchList.clear();
        mPchList.addAll(list);
        fillPchView();
    }


    @Override
    public void getFwxxListSuccess(List<JsdwxxBean.Wtlysz> bean) {
//        String json = GsonUtil.GsonToString(bean);
//        fwxxList = GsonUtil.JsonToList(json, JsdwxxBean.Wtlysz.class);
    }


    //初始化贮存设施下拉弹窗
    private void showZcssPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < zcssList.size(); i++) {
            ZcssBean bean = zcssList.get(i);
            selectPopupWindowList.add(new SelectPopupWindowBean(bean.id, bean.no));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                for (int i = 0; i < zcssList.size(); i++) {
                    if (zcssList.get(i).id.equals(model.ID)) {
                        //贮存设施统一编号
                        mData.zcsstybh = zcssList.get(i).no;
                        //贮存设施名称
                        mData.zcssmc = zcssList.get(i).ssmc;
                        //贮存设施id
                        mData.zcssid = zcssList.get(i).id;
                        et_item_zcsstybh.setText(zcssList.get(i).no);

                    }
                }
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }

        selectPopupWindow.show(mLayout, "", (View) et_item_zcsstybh.getParent(), sort);

    }


    //置空废物信息
    private void clearWasteInfo() {
        //废物信息
        mData.fwmc = "";
        //废物代码
        mData.fwdm = "";
        //利用处置方式
        mData.lyczfs = "";

        et_item_fwxx.setText("");
        et_item_fwdm.setText("");
        et_item_fwmc.setText("");
        et_item_lyczfs.setText("");
        fwxxList.clear();
    }


    //初始化废物信息下拉弹窗
    private void showFwxxPopWindow() {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < fwxxList.size(); i++) {
            JsdwxxBean.Wtlysz bean = fwxxList.get(i);
            selectPopupWindowList.add(new SelectPopupWindowBean(i + "", bean.fwmc));
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                for (int i = 0; i < fwxxList.size(); i++) {
                    JsdwxxBean.Wtlysz bean = fwxxList.get(i);
                    if (model.ID.equals(i+"")) {
                        //废物信息
                        mData.fwmc = bean.fwmc;
                        //废物代码
                        mData.fwdm = bean.fwdm;
                        //利用处置方式
                        mData.lyczfs = bean.lyczfs;

                        mData.cfxxtybh = bean.cfxxtybh;

                        et_item_fwxx.setText(bean.fwmc);
                        et_item_fwdm.setText(bean.fwdm);
                        et_item_fwmc.setText(bean.fwmc);
                        et_item_lyczfs.setText(bean.lyczfs);
                        et_item_cfxxtybh.setText(bean.cfxxtybh);
                    }
                }
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }

        selectPopupWindow.show(mLayout, "", (View) et_item_fwxx.getParent(), sort);

    }


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
                    case "联单包装方式":
                        mData.bzfs = model.ID;
                        break;
                    case "联单废物处理意见":
                        mData.clyj = model.ID;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 9://刷新产生批次号列表
                mPresenter.getCphList(mData.id);
                break;
        }
    }



}
