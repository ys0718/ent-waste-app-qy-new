package com.grkj.ytjqy_pad.mvp.view.activity.czdw;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.HtxxBean;
import com.grkj.ytjqy_pad.mvp.model.PrinterTypeBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.model.ZyldBean;
import com.grkj.ytjqy_pad.mvp.presenter.InputLargeScanPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.ScanBaseActivity;
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
import com.grkj.ytjqy_pad.utils.scancode.ScanCodeUtil;
import com.grkj.ytjqy_pad.utils.scancode.utils.Constant;
import com.grkj.ytjqy_pad.utils.view.ScanDialog;
import com.socks.library.KLog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2022/3/20.
 * 扫码出库
 */
public class InputLargeScanActivity extends ScanBaseActivity<InputLargeScanPresenter> implements InputLargeScanPresenter.View{


    //运送部门经办人
    @BindView(R.id.et_item18)
    HistorySearchSpinner et_item18;

    //是否为处置单位
    private boolean isCzdw = false;

    //合同编号、转移联单
    @BindView(R.id.ll_htbh_zyld)
    LinearLayout ll_item20_21;

    //来源单位
    @BindView(R.id.ll_item22)
    LinearLayout ll_item22;


    //数据列表
    private List<CommonProductInfoBean> mList = new ArrayList<>();

    private CommonAdapter<CommonProductInfoBean> adapter;//适配器


    @Override
    protected int getLayout(){
        return R.layout.activity_large_input;
    }

    @Override
    protected void initEventAndData(){
        isCzdw = getIntent().getBooleanExtra("isCzdw",false);

        //获取贮存设施列表
        mPresenter.getZcssList();

        if (isCzdw) {//处置单位则显示合同编号、转移联单
            ll_item20_21.setVisibility(View.VISIBLE);
            ll_item22.setVisibility(View.VISIBLE);
            mPresenter.getHtList("");
        } else {
            ll_item20_21.setVisibility(View.GONE);
            ll_item22.setVisibility(View.GONE);
        }

        findViewById(R.id.et_scan).requestFocus();

    }

    @Override
    protected void initLandscapeView(){
        super.initLandscapeView();
        initTop();
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));
        initRecycleView();
    }

    @Override
    protected void initPortraitView(){
        super.initPortraitView();
        initTopToolbar("批量入库");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        initRecycleView();
    }


    @OnClick({R.id.ll_back,R.id.et_item13,R.id.ll_save,R.id.ll_scan,R.id.ll_select_all,R.id.ll_clear_all})
    public void onViewClicked(View view){
        switch (view.getId()) {
            //全选
            case R.id.ll_select_all:
                isSelectAll = !isSelectAll;
                iv_select_all.setImageResource(isSelectAll?R.mipmap.ic_selected:R.mipmap.ic_unselected);
                String totalWeight = "0.0";
                //千克转吨
                if (mList.size() != 0) {
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).SELECT = isSelectAll;
                        if (isSelectAll) {
                            totalWeight = new BigDecimal(totalWeight).add(new BigDecimal(Constants.kgToTon(mList.get(i).jldw,mList.get(i).csl))).toString();
                        } else {
                            totalWeight = "0.0";
                        }
                    }
                }
                updateTodoListNum();
                et_item8.setText(totalWeight);
                adapter.notifyDataSetChanged();
                break;

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

            //贮存设备编号
            case R.id.et_item13:
                showZcssPopWindow();
                break;

            //开始扫描
            case R.id.ll_scan:
                et_item18.clearFocus();
                showScanDialog(0);

                break;

            //保存
            case R.id.ll_save:
                et_item18.clearFocus();
                mData.ysjbr = et_item18.getText().toString();
                mData.zcjbr = SPUtils.get(mContext,Constants.USER_NAME,"").toString();
                String dataFrom = isLandscape?Constants.TYPE_YTJ:Constants.TYPE_SCZD;
                mData.versionFrom = AppUtils.getVersionName(mContext);
                String deviceCode = SPUtils.get(mContext,"DEVICE_CODE","").toString();

                if (TextUtils.isEmpty(checkEmpty())) {

                    if (mList.size() == 0) {
                        ToastUtil.showToast(mContext,"请扫描正确的二维码后保存！");
                    } else {
                        List<CommonProductInfoBean> commitList = new ArrayList<>();
                        for (int i = 0; i < mList.size(); i++) {
                            CommonProductInfoBean newData = mList.get(i);

                            if (mList.get(i).SELECT) {
                                //主表组装
                                newData.ysjbr = mData.ysjbr;
                                newData.zcssbm = mData.zcssbm;
                                newData.zcjbr = mData.zcjbr;
                                newData.zcsslx = mData.zcsslx;
                                newData.htbh = mData.htbh;
                                newData.htid = mData.htid;
                                newData.rkl = newData.csl;
                                newData.dataFrom = dataFrom;
                                newData.versionFrom = mData.versionFrom;
                                newData.deviceCode = deviceCode;
                                newData.lrfs = "2";
                                newData.rksj = DateTimeUtil.getCurrentDate();
                                newData.bqlx = getBqlxFromDevice();

                                if (!isCzdw) {//处置单位时，不用组装子表
                                    //子表组装
                                    newData.gfYbqyTzRkFwxx = new CommonProductInfoBean.GfYbqyTzRkFwxx();
                                    newData.gfYbqyTzRkFwxx.csfwid = newData.id;
                                    newData.gfYbqyTzRkFwxx.qrcodeValue = newData.qrcodeValue;
                                    newData.gfYbqyTzRkFwxx.cspch = newData.cspch;
                                    newData.gfYbqyTzRkFwxx.fwdlbh = newData.fwdlbh;
                                    newData.gfYbqyTzRkFwxx.fwdlmc = newData.fwdlmc;
                                    newData.gfYbqyTzRkFwxx.fwxlbh = newData.fwxlbh;
                                    newData.gfYbqyTzRkFwxx.fwxlmc = newData.fwxlmc;
                                    newData.gfYbqyTzRkFwxx.fwxxmc = newData.fwxxmc;
                                    newData.gfYbqyTzRkFwxx.wlxz = newData.wlxz;
                                    newData.gfYbqyTzRkFwxx.rkl = newData.rkl;
                                    newData.gfYbqyTzRkFwxx.jldw = newData.jldw;
                                    newData.gfYbqyTzRkFwxx.rqbzbm = newData.rqbzbm;
                                    newData.gfYbqyTzRkFwxx.rqbzlx = newData.rqbzlx;
                                    newData.gfYbqyTzRkFwxx.rqbzsl = newData.rqbzsl;
                                    newData.gfYbqyTzRkFwxx.mlnf = newData.mlnf;
                                    newData.gfYbqyTzRkFwxx.id = "";
                                    newData.gfYbqyTzRkFwxx.isck = "0";
                                    newData.gfYbqyTzRkFwxx.fwtx = newData.fwtx;
                                    newData.gfYbqyTzRkFwxx.rqbzcz = newData.rqbzcz;
                                    newData.gfYbqyTzRkFwxx.rqbzgg = newData.rqbzgg;
                                    newData.gfYbqyTzRkFwxx.qx = newData.qx;
                                }

                                //组件赋空处理
                                newData.id = "";
                                commitList.add(newData);
                            }
                        }

                        if (commitList.size() == 0) {
                            ToastUtil.showToast(mContext,"请至少选择一条入库信息！");
                            return;
                        }

                        mPresenter.showSaveDialog(commitList);
                    }

                } else {
                    ToastUtil.showToast(mContext,checkEmpty());
                }

                break;

        }
    }


    private ScanCodeUtil scanCodeUtil;

    @Override
    public void showScanDialog(long delayTime) {

        if (TextUtils.isEmpty(mData.zcssbm)) {
            ToastUtil.showToast(mContext,"请先选择贮存设施编码！");
            return;
        }

        scanCodeUtil = new ScanCodeUtil(InputLargeScanActivity.this, isLandscape, delayTime, Constant.REQUEST_CODE);
        scanCodeUtil.scanCode();
        scanCodeUtil.getResultListener(result -> {
            if (!TextUtils.isEmpty(result)) {
                mPresenter.getScanInfoList(result, zcssSelectBean.id);
            }
        });


        // TODO: 2023/11/20
        /*if (isLandscape) {
            if (roundCornerDialog == null) {
                roundCornerDialog = new ScanDialog(mContext);
                roundCornerDialog.setOnScanResultCallBack(new ScanDialog.OnScanResultCallBack() {
                    @Override
                    public void OnScanSuccess(String result) {
                        roundCornerDialog.dismiss();
//                    mPresenter.getScanInfoList(result,zcssSelectBean.id);
                        mPresenter.getScanInfoList(result, zcssSelectBean.id);
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
        }*/


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO: 2023/11/20
        if (data != null && requestCode == scanCodeUtil.scanRequestCode) {
            String scanCode = data.getStringExtra("ScanCodeResult");
            mPresenter.getScanInfoList(scanCode, zcssSelectBean.id);
        }

    }


    /**
     * 获取标签类型
     *
     * @return
     */
    private String getBqlxFromDevice(){
        String printerStr = SPUtils.get(mContext,"PRINTER","").toString();
        PrinterTypeBean printerTypeBean = GsonUtil.GsonToBean(printerStr,PrinterTypeBean.class);
        return printerTypeBean.bqlx;
    }


//    private void checkIsAll(List<CommonProductInfoBean> newList){
//        isSelectAll = true;
//        if (newList!=null && newList.size()!=0){
//            for (int j = 0; j < newList.size(); j++) {
//                if (!newList.get(j).SELECT){
//                    isSelectAll = false;
//                }
//            }
//        }
//        iv_select_all.setImageResource(isSelectAll?R.mipmap.ic_selected:R.mipmap.ic_unselected);
//    }


    /*
     * 判空处理
     * */
    private String checkEmpty(){
        if (TextUtils.isEmpty(mData.zcssbm)) {
            return "请选择贮存设施编码！";
        } else if (TextUtils.isEmpty(et_item18.getText().toString())) {
            return "请输入运送部门经办人！";
        } else if (isCzdw && TextUtils.isEmpty(editSpinner_20.getText())) {
            return "请输入/选择合同编号！";
        } else {
            return "";
        }
    }


    /**
     * 企业列表初始化
     */
    private void initRecycleView(){
        adapter = new CommonAdapter<CommonProductInfoBean>(mContext,R.layout.item_common_waste_info,mList){
            @Override
            protected void convert(ViewHolder holder,final CommonProductInfoBean bean,final int position){
                if (bean != null) {
                    holder.setText(R.id.tv_item1,TextUtils.isEmpty(bean.fwxxmc)?getString(R.string.common_empty):bean.fwxxmc);

                    String groupFwlb = getString(R.string.str_record_fwlb2,bean.fwdlbh,bean.fwdlmc);
                    setText(holder.getView(R.id.tv_item1),bean.fwxxmc,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2),groupFwlb,R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3),bean.fwxlbh,R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item4),bean.cssj,R.string.str_record_list11);
                    setText(holder.getView(R.id.tv_item5), bean.cspch, R.string.str_record_list14);
                    setText(holder.getView(R.id.tv_item6),bean.csl,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item7),bean.jldw,R.string.str_jldw_input);


                    ImageView cb = holder.getView(R.id.btn_select);
                    LinearLayout item_layout = holder.getView(R.id.item_layout);
                    cb.setVisibility(View.VISIBLE);
                    cb.setImageResource(bean.SELECT?R.drawable.checkbox_theme:R.mipmap.ic_unselected);
                    item_layout.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            updateSelectTotal(bean);
                            List<CommonProductInfoBean> newList = adapter.getDatas();
                            checkIsAll(newList);
                        }
                    });

                    ImageView iv_delete = holder.getView(R.id.iv_delete);
                    iv_delete.setVisibility(View.VISIBLE);

                    iv_delete.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            updateDelTotal(bean);
                        }
                    });


                }
            }
        };

        mRecyclerView.setAdapter(adapter);
    }




    //更新选择后的总量
    private void updateSelectTotal(CommonProductInfoBean bean){
        bean.SELECT = !bean.SELECT;
        String totalWeight = et_item8.getText().toString().trim();
        //千克转吨
        String csl_t = Constants.kgToTon(bean.jldw,bean.csl);

        if (TextUtils.isEmpty(totalWeight)) {
            totalWeight = "0";
        }
        if (bean.SELECT) {
            totalWeight = new BigDecimal(totalWeight).add(new BigDecimal(csl_t)).toString();
        } else {
            totalWeight = new BigDecimal(totalWeight).subtract(new BigDecimal(csl_t)).toString();
        }
        et_item8.setText(totalWeight);
        updateTodoListNum();
        adapter.notifyDataSetChanged();
    }

    //更新删除后的总量
    private void updateDelTotal(CommonProductInfoBean bean){
        String totalWeight = et_item8.getText().toString().trim();
        //千克转吨
        String csl_t = Constants.kgToTon(bean.jldw,bean.csl);

        if (TextUtils.isEmpty(totalWeight)) {
            totalWeight = "0";
        }
        if (bean.SELECT) {
            String newWeight = new BigDecimal(totalWeight).subtract(new BigDecimal(csl_t)).toString();
            et_item8.setText(newWeight);
        }
        mList.remove(bean);
        adapter.getDatas().remove(bean);
        updateTodoListNum();
        adapter.notifyDataSetChanged();
    }

    //更新待处理条数
    private void updateTodoListNum(){
        int selectNum = 0;
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).SELECT) {
                selectNum++;
            }
        }
        tv_num.setText("(共"+mList.size()+"条,已选中"+selectNum+"条)");
    }


    private void setText(View tv,String value,int strRes){
        tv.setVisibility(View.VISIBLE);

        ((TextView) tv).setText(Html.fromHtml(TextUtils.isEmpty(value)?getString(strRes,getString(R.string.common_empty_text)):getString(strRes,value)));
    }


    @Override
    protected InputLargeScanPresenter createPresenter(){
        return new InputLargeScanPresenter();
    }

    @Override
    public void showError(String msg){
        toastShow(msg);
    }


    @Override
    public void getScanInfoSuccess(String bean){
        scanCodeUtil.scanCode();
        if (!TextUtils.isEmpty(bean)) {
            CommonProductInfoBean CommonProductInfoBean = GsonUtil.GsonToBean(bean,CommonProductInfoBean.class);
            String result = "";
            if (mList.size() != 0) {
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).id.equals(CommonProductInfoBean.id)) {
                        result = "请勿重复扫描!";
                    }
                }
            }

            if (TextUtils.isEmpty(result)) {
                mList.add(CommonProductInfoBean);
                updateSelectTotal(CommonProductInfoBean);
                ToastUtil.showToast(mContext,"扫描成功，已添加到待入库列表！");
            } else {
                ToastUtil.showToast(mContext,result);
            }

        } else {
            ToastUtil.showToast(mContext,"请扫描正确的二维码！");
        }

        showScanDialog(300);
    }

    @Override
    public void getScanInfoListSuccess(List<CommonProductInfoBean> resultList){
        if (resultList != null && resultList.size() != 0) {

            mList.addAll(resultList);
            //去重处理
            Set<CommonProductInfoBean> setByName = new TreeSet<CommonProductInfoBean>((o1,o2) ->
                    o1.qrcodeValue.compareTo(o2.qrcodeValue));
            setByName.addAll(mList);
            List<CommonProductInfoBean> listByName = new ArrayList<>(setByName);

            if (mList.size() != listByName.size()) {
                ToastUtil.showToast(mContext,"已为您剔除重复数据");
            }

            mList = new ArrayList<>();
            mList.addAll(listByName);

            String totalWeight = "0";
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).SELECT = true;
                totalWeight = new BigDecimal(totalWeight).add(new BigDecimal(Constants.kgToTon(mList.get(i).jldw,mList.get(i).csl))).toString();
            }

            updateTodoListNum();
            et_item8.setText(totalWeight);

            adapter.getDatas().clear();
            adapter.getDatas().addAll(mList);
            adapter.notifyDataSetChanged();

        } else {
            ToastUtil.showToast(mContext,"请扫描正确的二维码！");
        }

        showScanDialog(300);
    }

    @Override
    public void getZcssListSuccess(List<ZcssBean> list){
        String json = GsonUtil.GsonToString(list);
        zcssList = GsonUtil.JsonToList(json,ZcssBean.class);
    }


    @Override
    public void saveDataSuccess(Object bean){
        ToastUtil.showToast(mContext,"保存成功！");
        setResult(9);
        finish();
    }

    private List<UserBean> temFromList = new ArrayList<>();

    @Override
    public void getFromListSuccess(List<UserBean> list){
        temFromList = list;
        List<Select> selectList = new ArrayList<>();
        for (int i = 0; i < temFromList.size(); i++) {
            Select bean = new Select(temFromList.get(i).entername,temFromList.get(i).entername);
            selectList.add(bean);
        }
        editSpinner_22.setItemData(selectList);
        editSpinner_22.setOnInsertListener(new SearchSpinner.onInsertListener(){
            @Override
            public void insertChange(String name,String value,boolean isFromSelect){
                mData.lydwmc = name;
                KLog.e("editSpinner",name);
                if (TextUtils.isEmpty(name) || !isFromSelect) {//如果是输入或者是为空的时候筛选
                    mPresenter.getFromList(name);
                }
            }
        });

    }

    private List<ZyldBean> temZyldList = new ArrayList<>();

    @BindView(R.id.editSpinner_21)
    SearchSpinner editSpinner_21;

    @BindView(R.id.editSpinner_22)
    SearchSpinner editSpinner_22;


    //贮存设备编码
    @BindView(R.id.et_item13)
    EditText et_item13;

    //贮存设备类型
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

    @Override
    public void getZyldListSuccess(List<ZyldBean> list){
        temZyldList.clear();
        temZyldList = list;

        List<Select> selectList = new ArrayList<>();
        for (int i = 0; i < temZyldList.size(); i++) {
            Select bean = new Select(temZyldList.get(i).number1,temZyldList.get(i).id);
            selectList.add(bean);
        }
        editSpinner_21.setItemData(selectList);
        editSpinner_21.setOnInsertListener(new SearchSpinner.onInsertListener(){
            @Override
            public void insertChange(String name,String value,boolean isFromSelect){
                KLog.e("editSpinner",name);
                mData.zyldbh = name;
                mData.zyldid = value;

                for (int i = 0; i < temZyldList.size(); i++) {
                    if (temZyldList.get(i).id.equals(value)) {
                        mData.lydwmc = temZyldList.get(i).jsdwmc;
                        editSpinner_22.setText(mData.lydwmc);
                    }
                }

                if (TextUtils.isEmpty(name) || !isFromSelect) {
                    mPresenter.getZyldList(name);
                }
            }
        });

    }

    private List<HtxxBean> temHtxxList = new ArrayList<>();


    @BindView(R.id.editSpinner_20)
    SearchSpinner editSpinner_20;

    @Override
    public void getHtListSuccess(List<HtxxBean> list){
        temHtxxList.clear();
        temHtxxList = list;

        List<Select> selectList = new ArrayList<>();
        for (int i = 0; i < temHtxxList.size(); i++) {
            Select bean = new Select(temHtxxList.get(i).htbh,temHtxxList.get(i).id);
            selectList.add(bean);
        }
        editSpinner_20.setItemData(selectList);
        editSpinner_20.setOnInsertListener(new SearchSpinner.onInsertListener(){
            @Override
            public void insertChange(String name,String value,boolean isFromSelect){
                KLog.e("editSpinner",name);
                mData.htbh = name;
                mData.htid = value;
                if (TextUtils.isEmpty(name) || !isFromSelect) {
                    mPresenter.getHtList(name);
                }

            }
        });

    }


    private SelectPopupWindow selectPopupWindow;
    private List<ZcssBean> zcssList = new ArrayList<>();
    private ZcssBean zcssSelectBean = null;

    private CommonProductInfoBean mData = new CommonProductInfoBean();

    //初始贮存设施下拉弹窗
    private void showZcssPopWindow(){
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < zcssList.size(); i++) {
            ZcssBean mData = zcssList.get(i);
            String name = getString(R.string.str_input_zcbm,mData.no,mData.sslx,mData.ssmc);
            selectPopupWindowList.add(new SelectPopupWindowBean(zcssList.get(i).id,name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext,selectPopupWindowList,new SelectPopupWindow.OnClickItemListener(){
            @Override
            public void onDo(SelectPopupWindowBean model,String mType){
                for (int i = 0; i < zcssList.size(); i++) {
                    if (zcssList.get(i).id.equals(model.ID)) {
                        zcssSelectBean = zcssList.get(i);
                    }
                }
                et_item13.setText(zcssSelectBean.no);
                et_item14.setText(zcssSelectBean.sslx+zcssSelectBean.ssmc);
                mData.zcssbm = zcssSelectBean.no;
                mData.zcsslx = zcssSelectBean.sslx;
                //选择完成清空列表
                mList.clear();
                adapter.getDatas().clear();
                et_item8.setText("0.0");
                adapter.notifyDataSetChanged();
                updateTodoListNum();
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size()+1;
        }
        selectPopupWindow.show(mLayout,"",(View) et_item13.getParent(),sort);
    }


}
