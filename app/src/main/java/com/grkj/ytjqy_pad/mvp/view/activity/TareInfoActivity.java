package com.grkj.ytjqy_pad.mvp.view.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.TareListBean;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.presenter.TareInfoPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.view.AddNumberView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2022/3/21.
 * 去皮信息
 */

public class TareInfoActivity extends BaseActivity<TareInfoPresenter> implements TareInfoPresenter.View{

    private TareListBean mData = new TareListBean();

    //贮存设备名称
    @BindView(R.id.et_item1)
    EditText et_item1;

    //废物类别
    @BindView(R.id.et_item2)
    EditText et_item2;


    //容器/包装类型
    @BindView(R.id.et_item3)
    EditText et_item3;


    //包装材质
    @BindView(R.id.et_item4)
    EditText et_item4;

    //包装规格
    @BindView(R.id.et_item5)
    EditText et_item5;


    //容器/包装数量
    @BindView(R.id.add_view)
    AddNumberView et_item6;


    //容器/包装数量
    @BindView(R.id.add_view2)
    AddNumberView et_item7;


    //包装规格
    @BindView(R.id.et_item8)
    EditText et_item8;


    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //删除按钮
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;

    private boolean isAdd = true;//是否为新增


    @Override
    protected int getLayout(){
        return R.layout.activity_tare_info;
    }

    @Override
    protected void initEventAndData(){
        initTop();

        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);

        if (TextUtils.isEmpty(dataStr)) {
            mData = new TareListBean();
            isAdd = true;
        } else {
            isAdd = false;
            ll_delete.setVisibility(View.VISIBLE);
            mData = GsonUtil.GsonToBean(dataStr,TareListBean.class);

        }

        mPresenter.getTypeList();
        Map<String, String> map = new HashMap<>();
        map.put("types","废物特性,物理特性,包装方式,计量单位,处理方式及去向,包装材质,包装规模");
        mPresenter.getParamList(map);


        initListener();

    }

    private void initListener(){

        et_item6.setEnable(true);
        et_item6.setInputType();
        et_item6.setOnTextChangedListener(new AddNumberView.OnTextChangedListener(){
            @Override
            public void afterTextChanged(double result){
                double otherWeight = et_item7.getResult();
                String pzl = new BigDecimal(otherWeight).add(new BigDecimal(result)).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
                et_item8.setText(pzl);
            }
        });


        et_item7.setEnable(true);
        et_item7.setInputType();
        et_item7.setOnTextChangedListener(new AddNumberView.OnTextChangedListener(){
            @Override
            public void afterTextChanged(double result){
                double otherWeight = et_item6.getResult();
                String pzl = new BigDecimal(otherWeight).add(new BigDecimal(result)).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
                et_item8.setText(pzl);
            }
        });


    }

    private void initView(){
        et_item1.setText(mData.zcssbm);
        et_item2.setText(mData.fwdlbh);
        et_item3.setText(commonTransfer(mData.rqbzlx,"包装方式"));
        et_item4.setText(commonTransfer(mData.rqbzcz,"包装材质"));
        et_item5.setText(commonTransfer(mData.rqbzgg,"包装规模"));
        et_item6.setResult(mData.rqbzzl);
        et_item7.setResult(mData.qtzl);
        et_item8.setText(mData.pzl);
    }

    /*
     * 通用转义
     * */
    private String commonTransfer(String code,String type){
        String result = "";
        for (int i = 0; i < allParamList.size(); i++) {
            ArrayList<ParamListBean> childList = allParamList.get(i).childList;
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


    @OnClick({R.id.et_item1,R.id.et_item2,R.id.et_item4,R.id.et_item5,R.id.ll_back,R.id.ll_save,R.id.ll_delete})
    public void onViewClicked(View view){
        switch (view.getId()) {

            //贮存设备编号
            case R.id.et_item1:
                showZcssPopWindow();
                break;

            //废物类别
            case R.id.et_item2:
                showFwlbPopWindow();
                break;


            //包装材质
            case R.id.et_item4:
                showPopWindow("包装材质",et_item4,false);
                break;

            //包装规模
            case R.id.et_item5:
                showPopWindow("包装规模",et_item5,false);
                break;


            // 返回
            case R.id.ll_back:
                finish();
                break;

            // 保存
            case R.id.ll_save:
                mData.rqbzzl = et_item6.getResultString();
                mData.qtzl = et_item7.getResultString();
                mData.pzl = et_item8.getText().toString();

                if (TextUtils.isEmpty(checkEmpty())) {
                    Map<String, String> map = GsonUtil.GsonToMaps(GsonUtil.GsonToString(mData));
                    mPresenter.showSaveDialog(map);
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
//        if (TextUtils.isEmpty(mData.zcssbm)) {
//            return "请选择贮存设施！";
//        } else
//
        if (TextUtils.isEmpty(mData.fwdlbh)) {
            return "请选择废物类别！";
        } else if (TextUtils.isEmpty(mData.rqbzcz)) {
            return "请选择容器/包装材质！";
        } else if (TextUtils.isEmpty(mData.rqbzgg)) {
            return "请选择容器/包装规格！";
        } else if (TextUtils.isEmpty(mData.rqbzzl)) {
            return "请输入容器/包装重量！";
        } else if (TextUtils.isEmpty(mData.qtzl)) {
            return "请输入其他重量！";
        } else {
            return "";
        }
    }


    @Override
    protected TareInfoPresenter createPresenter(){
        return new TareInfoPresenter();
    }


    @Override
    public void showError(String msg){
        ToastUtil.showToast(mContext,msg);
    }


    @Override
    public void getTypeListSuccess(List<CommonProductInfoBean> list){
        String json = GsonUtil.GsonToString(list);
        wasteTypeList = GsonUtil.JsonToList(json,CommonProductInfoBean.class);
    }

    @Override
    public void getZcssListSuccess(List<ZcssBean> list){
        String json = GsonUtil.GsonToString(list);
        zcssList = GsonUtil.JsonToList(json,ZcssBean.class);

        if (!TextUtils.isEmpty(mData.zcssbm)) {
            for (int i = 0; i < zcssList.size(); i++) {
                if (mData.zcssbm.equals(zcssList.get(i).no)) {
//                    mPresenter.getTypeList(zcssList.get(i).id);
                }
            }
        }

    }


    private List<ParamListBean> allParamList = new ArrayList<>();
    private List<CommonProductInfoBean> wasteTypeList = new ArrayList<>();
    private List<ZcssBean> zcssList = new ArrayList<>();


    @Override
    public void getParamListSuccess(List<ParamListBean> list){
        String json = GsonUtil.GsonToString(list);
        allParamList = GsonUtil.JsonToList(json,ParamListBean.class);
        if (!isAdd) {
            initView();
        }
    }

    @Override
    public void saveDataSuccess(Object bean){
        setResult(9);
        ToastUtil.showToast(mContext,"保存成功！");
        finish();
    }


    @Override
    public void deleteDataSuccess(Object bean){
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
                    selectPopupWindowList.add(new SelectPopupWindowBean(childList.get(j).code,childList.get(j).value));
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
                        case "包装材质":
                            mData.rqbzcz = model.ID;
                            groupBzbm();
                            break;
                        case "包装规模":
                            mData.rqbzgg = model.ID;
                            groupBzbm();
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
            mData.rqbzbm = mData.rqbzlx+mData.rqbzcz+mData.rqbzgg;
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

                et_item2.setText(bean.fwdlbh);

                mData.fwdlbh = bean.fwdlbh;
                mData.fwxxmc = bean.fwxxmc;
                mData.rqbzlx = bean.bzfs;
                mData.fwxlbh = bean.fwxlbh;
                mData.fwxlmc = bean.fwxlmc;
                mData.fwdlmc = bean.fwdlmc;
                mData.fwId = bean.id;
                mData.cshjId = bean.cshjId;


                mData.rqbzbm = bean.rqbzbm;
                mData.rqbzcz = bean.rqbzcz;
                mData.rqbzgg = bean.rqbzgg;
                et_item4.setText(commonTransfer(mData.rqbzcz,"包装材质"));
                et_item5.setText(commonTransfer(mData.rqbzgg,"包装规模"));
                et_item3.setText(commonTransfer(mData.rqbzlx,"包装方式"));

                groupBzbm();
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size()+1;
        }
        selectPopupWindow.show(mLayout,"",(View) et_item2.getParent(),sort);
    }


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
                ZcssBean bean = new ZcssBean();
                for (int i = 0; i < zcssList.size(); i++) {
                    if (zcssList.get(i).id.equals(model.ID)) {
                        bean = zcssList.get(i);
                    }
                }
                et_item1.setText(bean.no);
//                mPresenter.getTypeList(bean.id);
                mData.zcssmc = bean.ssmc;
                mData.zcssbm = bean.no;
                mData.zcssId = bean.id;
                mData.zcsslx = bean.sslx;

                //重置废物信息
                et_item2.setText("");

                mData.fwdlbh = "";
                mData.fwxxmc = "";
                mData.rqbzlx = "";
                mData.fwxlbh = "";
                mData.fwxlmc = "";
                mData.fwdlmc = "";
                mData.fwId = "";
                mData.cshjId = "";

                mData.rqbzbm = "";
                mData.rqbzcz = "";
                mData.rqbzgg = "";
                et_item4.setText("");
                et_item5.setText("");
                et_item3.setText("");


                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size()+1;
        }
        selectPopupWindow.show(mLayout,"",(View) et_item1.getParent(),sort);
    }


}
