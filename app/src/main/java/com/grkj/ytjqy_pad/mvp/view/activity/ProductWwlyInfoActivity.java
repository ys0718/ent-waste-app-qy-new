package com.grkj.ytjqy_pad.mvp.view.activity;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.view.TimePickerView;
import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.event.ChangeEvent;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.RxBus;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.WwlyListBean;
import com.grkj.ytjqy_pad.mvp.model.ZxlyssBean;
import com.grkj.ytjqy_pad.mvp.presenter.ProductWwlyInfoPresenter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2022/3/21.
 * 委外利用/处置信息
 */

public class ProductWwlyInfoActivity extends BaseActivity<ProductWwlyInfoPresenter> implements ProductWwlyInfoPresenter.View{

    private WwlyListBean mData = new WwlyListBean();

    //批次号
    @BindView(R.id.et_item1)
    EditText et_item1;

    //入库时间
    @BindView(R.id.et_item2)
    EditText et_item2;

    //废物类别
    @BindView(R.id.et_item3)
    EditText et_item3;

    //废物类别名称
    @BindView(R.id.et_item4)
    EditText et_item4;

    //自行处置完毕时间
    @BindView(R.id.et_item5)
    EditText et_item5;

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

    //转移联单编号
    @BindView(R.id.et_item20)
    EditText et_item20;

    //许可证编号
    @BindView(R.id.et_item21)
    EditText et_item21;

    //单位名称
    @BindView(R.id.et_item26)
    EditText et_item26;

    //接收单位类型
    @BindView(R.id.et_item27)
    EditText et_item27;

    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //删除按钮
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;

    //是否为扫码自行利用
    private boolean isScan;
    private String rkid;

    private String typeName = "自行利用";
    private String typeValue = "R";

    private CommonProductInfoBean wasteSelectBean = new CommonProductInfoBean();
    private TimePickerView timePicker;


    @Override
    protected int getLayout(){
        return R.layout.activity_wwly_info;
    }

    @Override
    protected void initEventAndData(){

        typeName = getIntent().getStringExtra("typeName");
        typeValue = getIntent().getStringExtra("typeValue");
        initDifView();

        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);
        isScan = getIntent().getBooleanExtra("IS_SCAN",false);

        //产生登记跳转
        String cfdjStr = getIntent().getStringExtra("cfdjStr");

        //出库登记跳转
        String ckdjStr = getIntent().getStringExtra("ckdjStr");

        if (TextUtils.isEmpty(dataStr)) {

            //组装mData
            if (!TextUtils.isEmpty(cfdjStr)) {
                groupMainData(cfdjStr,false);
            } else if (!TextUtils.isEmpty(ckdjStr)) {
                groupMainData(ckdjStr,true);
            } else {
                mData = new WwlyListBean();
            }

            et_item1.setHint(getString(R.string.str_auto_get));
            et_item2.setText(DateTimeUtil.getCurrentDate());

            mData.lyczfsdlbh = typeValue;
            mData.lyczfsdlmc = typeName;
            mData.ccsj = et_item2.getText().toString();

        } else {
            ll_delete.setVisibility(View.VISIBLE);
            mData = GsonUtil.GsonToBean(dataStr,WwlyListBean.class);
            rkid = mData.id;
            et_item8.setText(mData.wwlyczl);
            mPresenter.getFwxxList(mData.id);

        }


        mPresenter.getZxlyssList();
        Map<String, String> map = new HashMap<>();
        map.put("types","废物特性,物理特性,计量单位,包装方式,包装材质,包装规模,危险废物利用处置方式代码,接收单位类型");
        mPresenter.getParamList(map);
        mPresenter.getTypeList();

    }

    //组装mData
    private void groupMainData(String dataStr,boolean isCk){
        WwlyListBean temData = GsonUtil.GsonToBean(dataStr,WwlyListBean.class);
        WwlyListBean.WwlyFwListBean childData = GsonUtil.GsonToBean(dataStr,WwlyListBean.WwlyFwListBean.class);

        String czl = temData.csl;
        String pch = temData.cspch;
        if (isCk) {
            czl = temData.ckl;
            pch = temData.ckpch;
        }
        et_item10.setText(pch);
        //将产生量/出库量赋值为利用量
        temData.zxlyczl = czl;
        temData.wwlyczl = czl;
        //主键赋空
        temData.id = "";//主键赋空

        //将产生量/出库量赋值为利用量
        childData.zxlyczl = czl;
        childData.wwlyczl = czl;
        //产生登记、出库登记主键赋值到csfwid
        childData.csfwid = childData.id;
        childData.ckid = childData.id;

        //主键赋空
        childData.id = "";

        List<WwlyListBean.WwlyFwListBean> childList = new ArrayList<>();
        childList.add(childData);
        temData.wwlyczFwxxes = childList;
        isScan = true;
        mData = temData;

        //自行利用量
        et_item8.setText(mData.zxlyczl);

        //隐藏关联按钮
        findViewById(R.id.ll_get_cfxx).setVisibility(View.GONE);
    }


    @Override
    protected void initLandscapeView(){
        super.initLandscapeView();
        initTop();
    }

    @Override
    protected void initPortraitView(){
        super.initPortraitView();
    }

    //处理不同类型的显示
    private void initDifView(){
        ((TextView) findViewById(R.id.tv_title)).setText(typeName+"登记");
        ((TextView) findViewById(R.id.tv_title_item1)).setText(getString(R.string.str_wwly_item1,typeName));
        ((TextView) findViewById(R.id.tv_title_item8)).setText(Html.fromHtml("<font color=\"#e51e5f\">"+"* "+"</font>"+
                getString(R.string.str_zxly_item8,typeName)));
    }

    private void initView(){
        et_item1.setText(mData.wwlyczpcbm);
        et_item2.setText(mData.ccsj);
        et_item3.setText(mData.fwdlbh);
        et_item4.setText(mData.fwdlmc);
        et_item6.setText(mData.fwxlbh);
        et_item7.setText(mData.fwxxmc);
        et_item9.setText(mData.rqbzbm);
        et_item11.setText(commonTransfer(mData.rqbzlx,"包装方式"));
        et_item12.setResult(mData.rqbzsl);
        et_item15.setText(commonTransfer(mData.rqbzcz,"包装材质"));
        et_item16.setText(commonTransfer(mData.rqbzgg,"包装规模"));
        et_item17.setText(TextUtils.isEmpty(mData.jldw) ? getJldw() : mData.jldw);
        et_item5.setText(mData.fwxlmc);
        if (!isScan) {
            et_item18.setText(getString(R.string.str_record_fwlb2,mData.lyczfsdlbh,mData.lyczfsdlmc));
            et_item19.setText(getString(R.string.str_record_fwlb2,mData.lyczfsxlbh,mData.lyczfsxlmc));
        }else {
            setDlmc();
        }
        et_item21.setText(mData.xkzbm);
        et_item26.setText(mData.jsdwmc);
        et_item27.setText(commonTransfer(mData.jsdwlx,"接收单位类型"));

    }

    //赋值大利用处置方式大类名称
    private void setDlmc(){
        for (int i = 0; i < allParamList.size(); i++) {
            List<ParamListBean> childList = allParamList.get(i).childList;
            for (int j = 0; j < childList.size(); j++) {
                if ("危险废物利用处置方式代码".equals(allParamList.get(i).type)) {
                    ParamListBean bean = childList.get(j);
                    if (typeValue.equals(bean.code)) {
                        mData.lyczfsdlmc = bean.value;
                        et_item18.setText(getString(R.string.str_record_fwlb2,typeValue,mData.lyczfsdlmc));
                    }
                }
            }
        }
    }


    @OnClick({R.id.et_item3,R.id.et_item6,R.id.et_item7,R.id.et_item27,
            R.id.et_item15,R.id.et_item16,R.id.et_item17,R.id.et_item19,R.id.ll_back,
            R.id.ll_save,R.id.ll_delete,R.id.ll_get_cfxx})
    public void onViewClicked(View view){
        switch (view.getId()) {

            //废物类别
            case R.id.et_item3:
                showFwlbPopWindow();
                break;

            //物理特性
            case R.id.et_item17:
                showPopWindow("计量单位",et_item17,false);
                break;

            // 关联产废信息
            case R.id.ll_get_cfxx:

                if (TextUtils.isEmpty(mData.fwId)) {
                    ToastUtil.showToast(mContext,"请先选择废物类别！");
                    return;
                }

                startActivityForResult(new Intent(mContext,SelectMultiWasteListActivity.class)
                                .putExtra("wasteSelectedList",mData.wwlyczFwxxes.size() != 0?GsonUtil.GsonToString(mData.wwlyczFwxxes):"")
                                .putExtra("wasteSelectBean",wasteSelectBean != null?GsonUtil.GsonToString(wasteSelectBean):"")
                        ,10);

                break;


            //包装材质
            case R.id.et_item15:
                showPopWindow("包装材质",et_item15,false);
                break;

            //接收单位类型
            case R.id.et_item27:
                showPopWindow("接收单位类型",et_item27,false);
                break;

            //包装规模
            case R.id.et_item16:
                showPopWindow("包装规模",et_item16,false);
                break;

            //危险废物利用处置方式小类
            case R.id.et_item19:
                showPopWindow("危险废物利用处置方式代码",et_item19,false);
                break;


            // 返回
            case R.id.ll_back:
                finish();
                break;

            // 保存
            case R.id.ll_save:
                mData.ccsj = et_item2.getText().toString();
                mData.wwlyczl = et_item8.getText().toString();
                mData.rqbzbm = et_item9.getText().toString();
                mData.rqbzsl = et_item12.getResultString();
                mData.jldw = et_item17.getText().toString();
                mData.xkzbm = et_item21.getText().toString();
                mData.jsdwmc = et_item26.getText().toString();

                if (TextUtils.isEmpty(checkEmpty())) {
                    showSaveDialog();
                } else {
                    ToastUtil.showToast(mContext,checkEmpty());
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
    private String checkEmpty(){
        if (TextUtils.isEmpty(mData.wwlyczl) || Double.valueOf(mData.wwlyczl) == 0) {
            return "请输入委外处置量！";
        } else if (TextUtils.isEmpty(mData.fwdlbh)) {
            return "请选择废物类别！";
        } else if (TextUtils.isEmpty(mData.rqbzcz)) {
            return "请选择容器/包装材质！";
        } else if (TextUtils.isEmpty(mData.rqbzgg)) {
            return "请选择容器/包装规格！";
        } else if (TextUtils.isEmpty(mData.rqbzsl)) {
            return "请输入容器/包装数量！";
        } else if (TextUtils.isEmpty(mData.lyczfsxlbh)) {
            return "请选择利用处置方式小类！";
        } else if (TextUtils.isEmpty(mData.jsdwmc)) {
            return "请输入接收单位名称！";
        } else if (TextUtils.isEmpty(mData.jsdwlx)) {
            return "请选择接收单位类型！";
        } else if (TextUtils.isEmpty(mData.xkzbm)) {
            return "请输入许可证编号！";
        } else {
            return "";
        }
    }


    private void showSaveDialog(){
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确定保存？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener(){
            @Override
            public void onConfirmClick(){
                Map<String, String> map = GsonUtil.GsonToMaps(GsonUtil.GsonToString(mData));
                mPresenter.saveData(map,ApiUrl.URL_PRODUCT_WWLY_SAVE);
                roundCornerDialog.dismiss();
            }

            @Override
            public void onCancelClick(){

            }
        });
        //显示对话框
        roundCornerDialog.show();
    }


    @Override
    protected ProductWwlyInfoPresenter createPresenter(){
        return new ProductWwlyInfoPresenter();
    }


    @Override
    public void showError(String msg){
        ToastUtil.showToast(mContext,msg);
    }


    @Override
    public void getTypeListSuccess(List<CommonProductInfoBean> list){
        String json = GsonUtil.GsonToString(list);
        wasteTypeList = GsonUtil.JsonToList(json,CommonProductInfoBean.class);

        if (!TextUtils.isEmpty(mData.fwId)) {
            for (int i = 0; i < wasteTypeList.size(); i++) {
                if (mData.fwId.equals(wasteTypeList.get(i).id)) {
                    wasteSelectBean = wasteTypeList.get(i);
                }
            }
        }

    }

    @Override
    public void getFwxxListSuccess(List<WwlyListBean.WwlyFwListBean> list){
        mData.wwlyczFwxxes = new ArrayList<>();
        mData.wwlyczFwxxes.addAll(list);
        initPchmStr(false);
    }

    @Override
    public void getZxlyssListSuccess(List<ZxlyssBean> list){
        String json = GsonUtil.GsonToString(list);
        zcssList = GsonUtil.JsonToList(json,ZxlyssBean.class);

    }


    private List<ParamListBean> allParamList = new ArrayList<>();
    private List<CommonProductInfoBean> wasteTypeList = new ArrayList<>();
    private List<ZxlyssBean> zcssList = new ArrayList<>();


    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (timePicker != null) {
            timePicker = null;
        }
    }

    @Override
    public void getParamListSuccess(List<ParamListBean> list){
        String json = GsonUtil.GsonToString(list);
        allParamList = GsonUtil.JsonToList(json,ParamListBean.class);

        if (!TextUtils.isEmpty(mData.id) || isScan) {
            initView();
        } else {
            setDlmc();
        }

    }

    @Override
    public void saveDataSuccess(Object bean){
        if (isScan) {
            RxBus.getDefault().post(new ChangeEvent());
        }
        setResult(9);
        ToastUtil.showToast(mContext,"保存成功！");
        finish();
    }


    @Override
    public void deleteDataSuccess(String bean){
        ToastUtil.showToast(mContext,"删除成功！");
        setResult(9);
        finish();
    }


    private SelectPopupWindow selectPopupWindow;


    //初始化选择地址下拉弹窗
    private void showPopWindow(String type,EditText item,boolean isMulti){
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

        if (isMulti) {

            String textStr = item.getText().toString();
            if (!TextUtils.isEmpty(textStr)) {//回显处理
                String[] split = textStr.split(",");
                for (int i = 0; i < selectPopupWindowList.size(); i++) {
                    for (int k = 0; k < split.length; k++) {
                        if (split[k].equals(selectPopupWindowList.get(i).NAME)) {
                            selectPopupWindowList.get(i).SELECT = true;
                        }
                    }
                }
            }

            selectPopupWindow = new SelectPopupWindow(mContext,selectPopupWindowList,new SelectPopupWindow.OnCheckClickItemListener(){
                @Override
                public void onDo(String mType,List<SelectPopupWindowBean> datas){
                    String resultName = "";
                    for (int i = 0; i < datas.size(); i++) {
                        if (i == datas.size()-1) {
                            resultName += datas.get(i).NAME;
                        } else {
                            resultName += datas.get(i).NAME+",";
                        }
                    }
                    item.setText(resultName);
                }
            });
        } else {
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
                            SPUtils.put(mContext,"JLDW",mData.jldw);
                            break;
                        case "接收单位类型":
                            mData.jsdwlx = model.ID;
                            break;
                    }
                    selectPopupWindow.dismiss();
                }
            });
        }

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size()+1;
        }

        selectPopupWindow.show(mLayout,type,(View) item.getParent(),sort);

    }

    /*
     * 拼接包装编码
     * */
    private void groupBzbm(){

        if (!TextUtils.isEmpty(mData.rqbzlx) && !TextUtils.isEmpty(mData.rqbzcz) && !TextUtils.isEmpty(mData.rqbzgg)) {
            mData.rqbzbm = mData.rqbzgg+mData.rqbzcz+mData.rqbzlx;
            et_item9.setText(mData.rqbzbm);
        }
    }


    //初始废物类别下拉弹窗
    private void showFwlbPopWindow(){
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < wasteTypeList.size(); i++) {
            CommonProductInfoBean mData = wasteTypeList.get(i);
            String name = getString(R.string.str_record_fwlb,mData.fwxxmc,mData.fwdlbh,mData.fwdlmc,mData.fwxlbh);
            selectPopupWindowList.add(new SelectPopupWindowBean(wasteTypeList.get(i).id,name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext,selectPopupWindowList,new SelectPopupWindow.OnClickItemListener(){
            @Override
            public void onDo(SelectPopupWindowBean model,String mType){
                CommonProductInfoBean bean = new CommonProductInfoBean();
                for (int i = 0; i < wasteTypeList.size(); i++) {
                    if (wasteTypeList.get(i).id.equals(model.ID)) {
                        bean = wasteTypeList.get(i);
                    }
                }

                wasteSelectBean = bean;

                et_item3.setText(bean.fwdlbh);
                et_item4.setText(bean.fwdlmc);
                et_item5.setText(bean.fwxlmc);
                et_item6.setText(bean.fwxlbh);
                et_item7.setText(bean.fwxxmc);

                mData.fwdlbh = bean.fwdlbh;
                mData.fwxxmc = bean.fwxxmc;
                mData.fwtx = bean.fwtx;
                mData.wlxz = bean.wlxz;
                mData.rqbzlx = bean.bzfs;
                mData.fwxlbh = bean.fwxlbh;
                mData.fwxlmc = bean.fwxlmc;
                mData.fwdlmc = bean.fwdlmc;
                mData.fwId = bean.id;

                mData.rqbzbm = bean.rqbzbm;
                mData.rqbzcz = bean.rqbzcz;
                mData.rqbzgg = bean.rqbzgg;

                et_item15.setText(commonTransfer(mData.rqbzcz,"包装材质"));
                et_item16.setText(commonTransfer(mData.rqbzgg,"包装规模"));
                et_item11.setText(commonTransfer(mData.rqbzlx,"包装方式"));

                groupBzbm();
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size()+1;
        }
        selectPopupWindow.show(mLayout,"",(View) et_item3.getParent(),sort);
    }


    /*
     * 通用转义
     * */
    private String commonTransfer(String code,String type){
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
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == 10) {
            String selectListStr = data.getStringExtra("selectList");
            KLog.e("selectListStr ===="+selectListStr);
            List<WwlyListBean.WwlyFwListBean> selectList = GsonUtil.JsonToList(selectListStr,WwlyListBean.WwlyFwListBean.class);
            mData.wwlyczFwxxes = new ArrayList<>();
            mData.wwlyczFwxxes.addAll(selectList);

            if (mData.wwlyczFwxxes.size() == 1){
                mData.jldw = mData.wwlyczFwxxes.get(0).jldw;
                et_item17.setText(mData.jldw);
            }


            initPchmStr(true);
//            mData.wwlyczFwxxes = selectList;
        }
    }


    private List<WwlyListBean.WwlyFwListBean> selectList = new ArrayList<>();

    //处理批次号、废物重量
    private void initPchmStr(boolean isNew){
        selectList = mData.wwlyczFwxxes;
        String pcbmStr = "";
        String totalWeight = "0";
        String bzsl = "0";
        for (int i = 0; i < selectList.size(); i++) {
            if (!TextUtils.isEmpty(selectList.get(i).ckpch)) {
                pcbmStr += selectList.get(i).ckpch+",";
            } else if (!TextUtils.isEmpty(selectList.get(i).cspch)) {
                pcbmStr += selectList.get(i).cspch+",";
            }

            if (TextUtils.isEmpty(selectList.get(i).wwlyczl)) {
                selectList.get(i).wwlyczl = "0";
            }
            //废物重量叠加
            totalWeight = new BigDecimal(selectList.get(i).wwlyczl).add(new BigDecimal(totalWeight)).toString();
            //包装数量叠加
            bzsl = new BigDecimal(selectList.get(i).rqbzsl).add(new BigDecimal(bzsl)).toString();
        }
        pcbmStr = pcbmStr.substring(0,pcbmStr.length()-1);
        et_item10.setText(pcbmStr);
        et_item12.setResult(bzsl);

        if (isNew) {
            et_item8.setText(totalWeight);
        }

    }


}
