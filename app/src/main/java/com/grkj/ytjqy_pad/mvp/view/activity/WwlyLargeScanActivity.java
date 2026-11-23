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

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.WwlyListBean;
import com.grkj.ytjqy_pad.mvp.presenter.WwlyLargeScanPresenter;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2023/2/10.
 * 委外利用/处置批量扫码登记
 */
public class WwlyLargeScanActivity extends ScanBaseActivity<WwlyLargeScanPresenter> implements WwlyLargeScanPresenter.View{

    //利用处置方式小类
    @BindView(R.id.et_item19)
    EditText et_item19;

    //接收单位类型
    @BindView(R.id.et_item27)
    EditText et_item27;

    //许可证编号
    @BindView(R.id.et_item21)
    EditText et_item21;

    //单位名称
    @BindView(R.id.et_item26)
    EditText et_item26;

    //总重量
    @BindView(R.id.et_item8)
    TextView et_item8;

    //总条数
    @BindView(R.id.tv_num)
    TextView tv_num;

    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //全选
    @BindView(R.id.iv_select_all)
    ImageView iv_select_all;

    private String typeName = "";
    private String typeValue = "";

    @Override
    protected int getLayout(){
        return R.layout.activity_large_wwly;
    }


    @Override
    protected void initEventAndData(){
        typeName = getIntent().getStringExtra("typeName");
        typeValue = getIntent().getStringExtra("typeValue");
        Map<String, String> map = new HashMap<>();
        map.put("types","危险废物利用处置方式代码,接收单位类型");
        initDifView();
        mPresenter.getParamList(map);

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
        initTopToolbar("批量"+typeName);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        initRecycleView();
    }


    private ScanDialog roundCornerDialog;

    @OnClick({R.id.ll_back,R.id.et_item19,R.id.et_item27,R.id.ll_save,R.id.ll_scan,R.id.ll_select_all,R.id.ll_clear_all})
    public void onViewClicked(View view){
        switch (view.getId()) {
            //全选
            case R.id.ll_select_all:
                isSelectAll = !isSelectAll;
                iv_select_all.setImageResource(isSelectAll?R.mipmap.ic_selected:R.mipmap.ic_unselected);
                String totalWeight = "0.0";
                if (mList.size() != 0) {
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).SELECT = isSelectAll;
                        if (isSelectAll) {
                            totalWeight = new BigDecimal(totalWeight).add(new BigDecimal(Constants.kgToTon(mList.get(i).jldw,mList.get(i).wwlyczl))).toString();
                        } else {
                            totalWeight = "0.0";
                        }
                    }
                }
                updateTodoListNum();
                et_item8.setText(totalWeight);
                adapter.notifyDataSetChanged();
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

            //利用处置方式小类
            case R.id.et_item19:
                showPopWindow("危险废物利用处置方式代码",et_item19);
                break;

            //开始扫描
            case R.id.ll_scan:
                showScanDialog(0);
                break;

            //保存
            case R.id.ll_save:
                mData.xkzbm = et_item21.getText().toString();
                mData.jsdwmc = et_item26.getText().toString();
                if (TextUtils.isEmpty(checkEmpty())) {
                    if (mList.size() == 0) {
                        ToastUtil.showToast(mContext,"请扫描正确的二维码后保存！");
                    } else {
                        List<WwlyListBean> commitList = new ArrayList<>();
                        for (int i = 0; i < mList.size(); i++) {
                            WwlyListBean newData = mList.get(i);
                            if (mList.get(i).SELECT) {
                                newData.lyczfsxlbh = mData.lyczfsxlbh;
                                newData.lyczfsxlmc = mData.lyczfsxlmc;
                                newData.jsdwlx = mData.jsdwlx;
                                newData.jsdwmc = mData.jsdwmc;
                                newData.xkzbm = mData.xkzbm;
                                newData.lyczfsdlbh = typeValue;
                                newData.lyczfsdlmc = typeName;
                                newData.id = "";
                                newData.ccsj = DateTimeUtil.getCurrentDate();
                                commitList.add(newData);
                            }
                        }

                        if (commitList.size() == 0) {
                            ToastUtil.showToast(mContext,"请至少选择一条产废/出库信息！");
                            return;
                        }

                        mPresenter.showSaveDialog(commitList);
                    }

                } else {
                    ToastUtil.showToast(mContext,checkEmpty());
                }

                break;

            //接收单位类型
            case R.id.et_item27:
                showPopWindow("接收单位类型",et_item27);
                break;

        }
    }

    //处理不同类型的显示
    private void initDifView(){
    }


    @Override
    public void showScanDialog(long delayTime) {
        if (roundCornerDialog == null) {
            roundCornerDialog = new ScanDialog(mContext);
            roundCornerDialog.setOnScanResultCallBack(new ScanDialog.OnScanResultCallBack(){
                @Override
                public void OnScanSuccess(String result){
                    KLog.e("扫码结果："+result);
                    roundCornerDialog.dismiss();
                    mPresenter.getScanInfo(result);
                }

                @Override
                public void OnScanFail(String errorMsg){

                }
            });
        }

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                //显示对话框
                roundCornerDialog.show();
            }
        },delayTime);

    }


    /*
     * 判空处理
     * */
    private String checkEmpty(){
        if (TextUtils.isEmpty(mData.lyczfsxlbh)) {
            return "请选择利用处置方式小类!";
        } else if (TextUtils.isEmpty(mData.jsdwlx)) {
            return "请选择接收单位类型！";
        } else if (TextUtils.isEmpty(mData.jsdwmc)) {
            return "请输入单位名称！";
        } else if (TextUtils.isEmpty(mData.xkzbm)) {
            return "请输入许可证编号！";
        } else {
            return "";
        }
    }


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private List<WwlyListBean> mList = new ArrayList<>();
    ;//数据列表
    private CommonAdapter<WwlyListBean> adapter;//适配器

    /**
     * 企业列表初始化
     */
    private void initRecycleView(){
        adapter = new CommonAdapter<WwlyListBean>(mContext,R.layout.item_common_waste_info,mList){
            @Override
            protected void convert(ViewHolder holder,final WwlyListBean bean,final int position){
                if (bean != null) {
                    holder.setText(R.id.tv_item1,TextUtils.isEmpty(bean.fwxxmc)?getString(R.string.common_empty):bean.fwxxmc);

                    String groupFwlb = getString(R.string.str_record_fwlb2,bean.fwdlbh,bean.fwdlmc);
                    setText(holder.getView(R.id.tv_item1),bean.fwxxmc,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2),groupFwlb,R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3),bean.fwxlbh,R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item4),bean.ccsj,R.string.str_record_list11);
                    setText(holder.getView(R.id.tv_item6),bean.wwlyczl,R.string.empty_replace);
                    holder.setText(R.id.tv_item7,getString(R.string.str_jldw_zxww,typeName.substring(typeName.length()-2),bean.jldw));


                    ImageView cb = holder.getView(R.id.btn_select);
                    LinearLayout item_layout = holder.getView(R.id.item_layout);
                    cb.setVisibility(View.VISIBLE);
                    cb.setImageResource(bean.SELECT?R.drawable.checkbox_theme:R.mipmap.ic_unselected);
                    item_layout.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            updateSelectTotal(bean);
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
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view,RecyclerView.ViewHolder viewHolder,int i){
                mList.get(i).SELECT = !mList.get(i).SELECT;
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view,RecyclerView.ViewHolder viewHolder,int i){
                return false;
            }
        });

        mRecyclerView.setAdapter(adapter);
    }


    //更新选择后的总量
    private void updateSelectTotal(WwlyListBean bean){
        bean.SELECT = !bean.SELECT;
        String totalWeight = et_item8.getText().toString().trim();

        //千克转吨
        String wwlyczl_t = Constants.kgToTon(bean.jldw,bean.wwlyczl);

        if (TextUtils.isEmpty(totalWeight)) {
            totalWeight = "0";
        }
        if (bean.SELECT) {
            totalWeight = new BigDecimal(totalWeight).add(new BigDecimal(wwlyczl_t)).toString();
        } else {
            totalWeight = new BigDecimal(totalWeight).subtract(new BigDecimal(wwlyczl_t)).toString();
        }
        et_item8.setText(totalWeight);
        updateTodoListNum();
        adapter.notifyDataSetChanged();
    }

    //更新删除后的总量
    private void updateDelTotal(WwlyListBean bean){
        String totalWeight = et_item8.getText().toString().trim();
        //千克转吨
        String wwlyczl_t = Constants.kgToTon(bean.jldw,bean.wwlyczl);
        if (TextUtils.isEmpty(totalWeight)) {
            totalWeight = "0";
        }
        if (bean.SELECT) {
            String newWeight = new BigDecimal(totalWeight).subtract(new BigDecimal(wwlyczl_t)).toString();
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
        if (mList != null && mList.size() != 0) {
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).SELECT) {
                    selectNum++;
                }
            }
            checkIsAll(adapter.getDatas());
        }
        tv_num.setText("待处理列表(共"+mList.size()+"条,已选中"+selectNum+"条)");

    }


    private void setText(View tv,String value,int strRes){
        tv.setVisibility(View.VISIBLE);

        ((TextView) tv).setText(Html.fromHtml(TextUtils.isEmpty(value)?getString(strRes,getString(R.string.common_empty_text)):getString(strRes,value)));
    }


    @Override
    protected WwlyLargeScanPresenter createPresenter(){
        return new WwlyLargeScanPresenter();
    }

    @Override
    public void showError(String msg){
        toastShow(msg);
    }

//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == 520 || keyCode == 521) {
//            showScanDialog();
////            et_item18.setText("");
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void getScanInfoSuccess(String bean){
        roundCornerDialog.show();
        if (!TextUtils.isEmpty(bean)) {
            WwlyListBean WwlyListBean = GsonUtil.GsonToBean(bean,WwlyListBean.class);
            String result = "";
            if (mList.size() != 0) {
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).id.equals(WwlyListBean.id)) {
                        result = "请勿重复扫描!";
                    }
//                    else if (!(mList.get(i).fwxxmc.equals(WwlyListBean.fwxxmc))) {
//                        result = "请扫描相同类别的废物!";
//                    }
                }
            }

            if (TextUtils.isEmpty(result)) {
                mList.add(WwlyListBean);
                updateSelectTotal(WwlyListBean);
                ToastUtil.showToast(mContext,"扫描成功，已添加到待处理列表！");
            } else {
                ToastUtil.showToast(mContext,result);
            }

        } else {
            ToastUtil.showToast(mContext,"请扫描正确的二维码！");
        }

        showScanDialog(300);
    }


    @Override
    public void saveDataSuccess(Object bean){
        ToastUtil.showToast(mContext,"保存成功！");
        setResult(9);
        finish();
    }

    @Override
    public void getParamListSuccess(List<ParamListBean> bean){
        String json = GsonUtil.GsonToString(bean);
        allParamList = GsonUtil.JsonToList(json,ParamListBean.class);
    }


    private SelectPopupWindow selectPopupWindow;
    protected List<ParamListBean> allParamList = new ArrayList<>();
    protected List<UserBean> receiverList = new ArrayList<>();
    private WwlyListBean mData = new WwlyListBean();


    //初始化选择地址下拉弹窗
    private void showPopWindow(String type,EditText item){
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < allParamList.size(); i++) {
            List<ParamListBean> childList = allParamList.get(i).childList;

            if (childList != null && childList.size() != 0 && type.equals(allParamList.get(i).type)) {
                for (int j = 0; j < childList.size(); j++) {
                    if ("危险废物利用处置方式代码".equals(allParamList.get(i).type)) {
                        ParamListBean bean = childList.get(j);
                        if (typeValue.equals(bean.code)) {
                            for (int k = 0; k < bean.childList.size(); k++) {
                                ParamListBean childBean = bean.childList.get(k);
                                String groupName = getString(R.string.str_record_fwlb2,childBean.code,childBean.value);
                                selectPopupWindowList.add(new SelectPopupWindowBean(childBean.code,groupName,childBean.value));
                            }
                        }
                    } else {
                        selectPopupWindowList.add(new SelectPopupWindowBean(childList.get(j).code,childList.get(j).value));
                    }
                }
            }
        }

        selectPopupWindow = new SelectPopupWindow(mContext,selectPopupWindowList,new SelectPopupWindow.OnClickItemListener(){
            @Override
            public void onDo(SelectPopupWindowBean model,String mType){
                item.setText(model.NAME);
                //废物特性,物理特性,包装方式,计量单位,处理方式及去向,包装材质,包装规模
                switch (mType) {
                    case "危险废物利用处置方式代码":
                        mData.lyczfsxlbh = model.ID;
                        mData.lyczfsxlmc = model.GROUP_NAME;
                        et_item19.setText(model.NAME);
                        break;
                    case "接收单位类型":
                        mData.jsdwlx = model.ID;
                        break;
                }
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size()+1;
        }

        selectPopupWindow.show(mLayout,type,(View) item.getParent(),sort);

    }


    /*
     * 通用转义
     * */
    protected String commonTransfer(String code,String type){
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
