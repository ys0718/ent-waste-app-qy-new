package com.grkj.ytjqy_pad.mvp.view.activity;

import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.ZxlyListBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.ZxlyssBean;
import com.grkj.ytjqy_pad.mvp.presenter.ZxlyLargeScanPresenter;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.view.ScanDialog;
import com.socks.library.KLog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2023/2/10.
 * 自行利用/处置批量扫码登记
 */
public class ZxlyLargeScanActivity extends ScanBaseActivity<ZxlyLargeScanPresenter> implements ZxlyLargeScanPresenter.View {

    //利用处置方式大类
    @BindView(R.id.et_item18)
    EditText et_item18;

    //利用处置方式小类
    @BindView(R.id.et_item19)
    EditText et_item19;

    //完毕时间
    @BindView(R.id.tv_title_item21)
    TextView tv_title_item21;

    //完毕时间
    @BindView(R.id.et_item5)
    EditText et_item5;

    //处置设施编码
    @BindView(R.id.et_item13)
    EditText et_item13;

    //处置设施类型
    @BindView(R.id.et_item14)
    EditText et_item14;

    //总重量
    @BindView(R.id.et_item8)
    TextView et_item8;

    //总条数
    @BindView(R.id.tv_num)
    TextView tv_num;

    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;


    private String typeName = "";
    private String typeValue = "";
    private List<ZxlyssBean> zcssList = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_large_zxly;
    }


    @Override
    protected void initEventAndData() {
        typeName = getIntent().getStringExtra("typeName");
        typeValue = getIntent().getStringExtra("typeValue");
        Map<String, String> map = new HashMap<>();
        map.put("types", "危险废物利用处置方式代码");
        initDifView();
        mPresenter.getParamList(map);
        mPresenter.getZxlyssList();

    }

    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));
        initRecycleView();
    }

    @Override
    protected void initPortraitView(){
        super.initPortraitView();
        initTopToolbar("批量"+typeName);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        initRecycleView();
    }


    private ScanDialog roundCornerDialog;


    private TimePickerView timePicker;

    @OnClick({R.id.ll_back, R.id.et_item19, R.id.et_item5, R.id.ll_save, R.id.ll_scan, R.id.ll_select_all, R.id.ll_clear_all, R.id.et_item13, R.id.et_item18})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //全选
            case R.id.ll_select_all:
                isSelectAll = !isSelectAll;
                iv_select_all.setImageResource(isSelectAll ? R.mipmap.ic_selected : R.mipmap.ic_unselected);
                String totalWeight = "0.0";
                if (mList.size() != 0) {
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).SELECT = isSelectAll;
                        if (isSelectAll) {
                            totalWeight = new BigDecimal(totalWeight).add(new BigDecimal(Constants.kgToTon(mList.get(i).jldw,mList.get(i).zxlyczl))).toString();
                        } else {
                            totalWeight = "0.0";
                        }
                    }
                }
                updateTodoListNum();
                et_item8.setText(totalWeight);
                adapter.notifyDataSetChanged();
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


            //清空
            case R.id.ll_clear_all:
                mList.clear();
                adapter.getDatas().clear();
                et_item8.setText("0.0");
                adapter.notifyDataSetChanged();
                updateTodoListNum();
                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;

            //危险废物利用处置方式大类
            case R.id.et_item18:
                showPopWindow("危险废物利用处置方式代码", et_item18);
                break;

            //危险废物利用处置方式小类
            case R.id.et_item19:
                showZxlyczxlPopWindow();
                break;

            //开始扫描
            case R.id.ll_scan:
                showScanDialog(0);
                break;

            //保存
            case R.id.ll_save:
                mData.zxlyczwbsj = et_item5.getText().toString();
                String defaultUserName = SPUtils.get(mContext, Constants.USER_NAME, "").toString();
                String dataFrom = isLandscape ? Constants.TYPE_YTJ : Constants.TYPE_SCZD;
                if (TextUtils.isEmpty(checkEmpty())) {
                    if (mList.size() == 0) {
                        ToastUtil.showToast(mContext, "请扫描正确的二维码后保存！");
                    } else {
                        List<ZxlyListBean> commitList = new ArrayList<>();
                        for (int i = 0; i < mList.size(); i++) {
                            ZxlyListBean newData = mList.get(i);
                            if (mList.get(i).SELECT) {
                                newData.lyczfsxlbh = mData.lyczfsxlbh;
                                newData.lyczfsxlmc = mData.lyczfsxlmc;
                                newData.ssbm = mData.ssbm;
                                newData.ssmc = mData.ssmc;
                                newData.zxlyczwbsj = mData.zxlyczwbsj;
                                newData.lyczfsdlbh = typeValue;
                                newData.lyczfsdlmc = typeName;
                                newData.zxlyczsj = DateTimeUtil.getCurrentDate();
                                newData.zxlyczjbr = defaultUserName;
                                newData.id = "";
                                commitList.add(newData);
                            }
                        }

                        if (commitList.size() == 0) {
                            ToastUtil.showToast(mContext, "请至少选择一条产废/出库信息！");
                            return;
                        }

                        mPresenter.showSaveDialog(commitList);
                    }

                } else {
                    ToastUtil.showToast(mContext, checkEmpty());
                }

                break;

            //处置设施编码
            case R.id.et_item13:
                showSsPopWindow();
                break;

        }
    }

    //处理不同类型的显示
    private void initDifView() {
        ((TextView) findViewById(R.id.tv_title_item13)).setText(Html.fromHtml("<font color=\"#e51e5f\">" + "* " + "</font>" +
                getString(R.string.str_zxly_item13, typeName)));
        ((TextView) findViewById(R.id.tv_title_item14)).setText(Html.fromHtml("<font color=\"#e51e5f\">" + "* " + "</font>" +
                getString(R.string.str_zxly_item14, typeName)));
        ((TextView) findViewById(R.id.tv_title_item21)).setText(Html.fromHtml("<font color=\"#e51e5f\">" + "* " + "</font>" +
                getString(R.string.str_zxly_item21, typeName)));
    }


    @Override
    public void showScanDialog(long delayTime) {
        if (roundCornerDialog == null) {
            roundCornerDialog = new ScanDialog(mContext);
            roundCornerDialog.setOnScanResultCallBack(new ScanDialog.OnScanResultCallBack() {
                @Override
                public void OnScanSuccess(String result) {
                    KLog.e("扫码结果：" + result);
                    roundCornerDialog.dismiss();
                    mPresenter.getScanInfo(result);
                }

                @Override
                public void OnScanFail(String errorMsg) {

                }
            });
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //显示对话框
                roundCornerDialog.show();
            }
        }, delayTime);

    }


    /*
     * 判空处理
     * */
    private String checkEmpty() {
        if (TextUtils.isEmpty(mData.zxlyczwbsj)) {
            return "请选择" + typeName + "完毕时间！";
        } else if (TextUtils.isEmpty(mData.lyczfsxlbh)) {
            return "请选择利用处置方式小类！";
        } else {
            return "";
        }
    }


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private List<ZxlyListBean> mList = new ArrayList<>();
    ;//数据列表
    private CommonAdapter<ZxlyListBean> adapter;//适配器

    /**
     * 企业列表初始化
     */
    private void initRecycleView() {
        adapter = new CommonAdapter<ZxlyListBean>(mContext, R.layout.item_common_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final ZxlyListBean bean, final int position) {
                if (bean != null) {
                    holder.setText(R.id.tv_item1, TextUtils.isEmpty(bean.fwxxmc) ? getString(R.string.common_empty) : bean.fwxxmc);

                    String groupFwlb = getString(R.string.str_record_fwlb2, bean.fwdlbh, bean.fwdlmc);
                    setText(holder.getView(R.id.tv_item1), bean.fwxxmc, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2), groupFwlb, R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3), bean.fwxlbh, R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item4), bean.zxlyczsj, R.string.str_record_list11);
                    setText(holder.getView(R.id.tv_item6), bean.zxlyczl, R.string.empty_replace);
//                    setText(holder.getView(R.id.tv_item7), typeName + "量(吨)", R.string.empty_replace);
                    holder.setText(R.id.tv_item7,getString(R.string.str_jldw_zxww,typeName.substring(typeName.length()-2),bean.jldw));

                    ImageView cb = holder.getView(R.id.btn_select);
                    LinearLayout item_layout = holder.getView(R.id.item_layout);
                    cb.setVisibility(View.VISIBLE);
                    cb.setImageResource(bean.SELECT ? R.drawable.checkbox_theme : R.mipmap.ic_unselected);
                    item_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateSelectTotal(bean);
                            List<ZxlyListBean> newList = adapter.getDatas();
                            checkIsAll(newList);
                        }
                    });

                    ImageView iv_delete = holder.getView(R.id.iv_delete);
                    iv_delete.setVisibility(View.VISIBLE);

                    iv_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateDelTotal(bean);
                        }
                    });


                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                mList.get(i).SELECT = !mList.get(i).SELECT;
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                return false;
            }
        });

        mRecyclerView.setAdapter(adapter);
    }




    //更新选择后的总量
    private void updateSelectTotal(ZxlyListBean bean) {
        bean.SELECT = !bean.SELECT;
        String totalWeight = et_item8.getText().toString().trim();
        //千克转吨
        String zxlyczl_t = Constants.kgToTon(bean.jldw,bean.zxlyczl);
        if (TextUtils.isEmpty(totalWeight)) {
            totalWeight = "0";
        }
        if (bean.SELECT) {
            totalWeight = new BigDecimal(totalWeight).add(new BigDecimal(zxlyczl_t)).toString();
        } else {
            totalWeight = new BigDecimal(totalWeight).subtract(new BigDecimal(zxlyczl_t)).toString();
        }
        et_item8.setText(totalWeight);
        updateTodoListNum();
        adapter.notifyDataSetChanged();
    }

    //更新删除后的总量
    private void updateDelTotal(ZxlyListBean bean) {
        String totalWeight = et_item8.getText().toString().trim();
        //千克转吨
        String zxlyczl_t = Constants.kgToTon(bean.jldw,bean.zxlyczl);
        if (TextUtils.isEmpty(totalWeight)) {
            totalWeight = "0";
        }
        if (bean.SELECT) {
            String newWeight = new BigDecimal(totalWeight).subtract(new BigDecimal(zxlyczl_t)).toString();
            et_item8.setText(newWeight);
        }

        mList.remove(bean);
        adapter.getDatas().remove(bean);
        updateTodoListNum();
        adapter.notifyDataSetChanged();
    }

    //更新待处理条数
    private void updateTodoListNum() {
        int selectNum = 0;
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).SELECT) {
                selectNum++;
            }
        }
        tv_num.setText("待处理列表(共" + mList.size() + "条,已选中" + selectNum + "条)");
    }


    private void setText(View tv, String value, int strRes) {
        tv.setVisibility(View.VISIBLE);

        ((TextView) tv).setText(Html.fromHtml(TextUtils.isEmpty(value) ? getString(strRes, getString(R.string.common_empty_text)) : getString(strRes, value)));
    }


    @Override
    protected ZxlyLargeScanPresenter createPresenter() {
        return new ZxlyLargeScanPresenter();
    }

    @Override
    public void showError(String msg) {
        toastShow(msg);
    }


    @Override
    public void getScanInfoSuccess(String bean) {
        roundCornerDialog.show();
        if (!TextUtils.isEmpty(bean)) {
            ZxlyListBean ZxlyListBean = GsonUtil.GsonToBean(bean, ZxlyListBean.class);
            String result = "";
            if (mList.size() != 0) {
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).id.equals(ZxlyListBean.id)) {
                        result = "请勿重复扫描!";
                    }
                }
            }

            if (TextUtils.isEmpty(result)) {
                mList.add(ZxlyListBean);
                updateSelectTotal(ZxlyListBean);
                ToastUtil.showToast(mContext,"扫描成功，已添加到待处理列表！");
            } else {
                ToastUtil.showToast(mContext, result);
            }

        } else {
            ToastUtil.showToast(mContext, "请扫描正确的二维码！");
        }

        showScanDialog(300);
    }


    @Override
    public void saveDataSuccess(Object bean) {
        ToastUtil.showToast(mContext, "保存成功！");
        setResult(9);
        finish();
    }

    @Override
    public void getParamListSuccess(List<ParamListBean> bean) {
        String json = GsonUtil.GsonToString(bean);
        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);
    }

    @Override
    public void getZxlyssListSuccess(List<ZxlyssBean> list) {
        String json = GsonUtil.GsonToString(list);
        zcssList = GsonUtil.JsonToList(json, ZxlyssBean.class);
    }


    private SelectPopupWindow selectPopupWindow;
    protected List<ParamListBean> allParamList = new ArrayList<>();
    protected List<UserBean> receiverList = new ArrayList<>();
    private ZxlyListBean mData = new ZxlyListBean();


    //初始化下拉弹窗
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
