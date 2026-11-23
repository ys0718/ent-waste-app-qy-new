package com.grkj.ytjqy_pad.mvp.view.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.CheckInfoBean;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.presenter.CheckInfoPresenter;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.spinner.HistorySearchSpinner;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2022/3/21.
 * 校准信息
 */

public class CheckInfoActivity extends BaseActivity<CheckInfoPresenter> implements CheckInfoPresenter.View{

    private CheckInfoBean mData = new CheckInfoBean();
    //废物类别
    @BindView(R.id.et_item1)
    EditText et_item1;

    //废物类别名称
    @BindView(R.id.et_item2)
    EditText et_item2;

    //废物代码
    @BindView(R.id.et_item3)
    EditText et_item3;

    //废物名称
    @BindView(R.id.et_item4)
    EditText et_item4;

    //产废量
    @BindView(R.id.et_item5)
    EditText et_item5;

    //当前重量
    @BindView(R.id.et_item6)
    EditText et_item6;

    //校正时间
    @BindView(R.id.et_item7)
    EditText et_item7;

    //校正人员
    @BindView(R.id.et_item8)
    EditText et_item8;

    //情况说明
    @BindView(R.id.et_item9)
    HistorySearchSpinner et_item9;

    //原单位
    @BindView(R.id.tv_unit1)
    TextView tv_unit1;

    //新单位
    @BindView(R.id.tv_unit2)
    TextView tv_unit2;

    //产生量/入库量
    @BindView(R.id.tv_old_name)
    TextView tv_old_name;

    //当前重量布局
    @BindView(R.id.rl_item6)
    RelativeLayout rl_item6;


    private boolean isCk;//是否为出库跳转

    private boolean isWeight;//是否称重


    @Override
    protected int getLayout(){
        return R.layout.activity_check_info;
    }

    @Override
    protected void initEventAndData(){
        initTop();

        et_item9.setKey(Constants.KEY_JZ);

        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);
        isCk = getIntent().getBooleanExtra("IS_CK",false);
        isWeight = getIntent().getBooleanExtra("IS_WEIGHT",false);

        CommonProductInfoBean mainData = GsonUtil.GsonToBean(dataStr,CommonProductInfoBean.class);



        mData = GsonUtil.GsonToBean(dataStr,CheckInfoBean.class);
        mData.id = "";
        mData.mainid = mainData.id;
        mData.cslJldw = mainData.jldw;
        mData.rklJldw = mainData.jldw;
        mData.cklJldw = mainData.jldw;

        if (isCk){
            mData.rkl = mainData.gfYbqyTzCkZyFwxx.ckl;
//            mData.rkl = new BigDecimal(mData.rkl).divide(new BigDecimal(1000)).setScale(4,BigDecimal.ROUND_HALF_UP).toString();

            tv_old_name.setText(getString(R.string.str_check_item5_2));
        }else {
            mData.csl = mainData.gfYbqyTzRkFwxx.rkl;
//            mData.csl = new BigDecimal(mData.csl).divide(new BigDecimal(1000)).setScale(4,BigDecimal.ROUND_HALF_UP).toString();
            tv_old_name.setText(getString(R.string.str_check_item5));
        }




        String userId = SPUtils.get(mContext,Constants.USER_ID,"").toString();
        mData.jzryId = userId;

        initView();

    }


    @Override
    protected void initLandscapeView(){
        super.initLandscapeView();
        if (isWeight){
            rl_item6.setBackgroundResource(R.mipmap.bg_edittext_disable);
            et_item6.setEnabled(false);
        }
    }

    @Override
    protected void initPortraitView(){
        super.initPortraitView();
        if (isWeight){
            rl_item6.setBackgroundResource(R.drawable.bg_edittext_disable);
            et_item6.setEnabled(false);
        }
    }

    private void initView(){
        et_item1.setText(mData.fwdlbh);
        et_item2.setText(mData.fwdlmc);
        et_item3.setText(mData.fwxlbh);
        et_item4.setText(mData.fwxxmc);
        et_item5.setText(isCk?mData.rkl:mData.csl);
        et_item6.setText(getIntent().getStringExtra("CURRENT_WEIGHT"));
//        tv_unit1.setText(isCk?mData.cslJldw:mData.rklJldw);
//        tv_unit2.setText(isCk?mData.rklJldw:mData.cklJldw);
        tv_unit1.setText(mData.jldw);
        tv_unit2.setText(mData.jldw);
        et_item7.setText(DateTimeUtil.getCurrentDate());
        et_item8.setText(SPUtils.get(mContext,Constants.USER_NAME,"").toString());


    }


    @OnClick({R.id.et_item7,R.id.ll_back,R.id.ll_save})
    public void onViewClicked(View view){
        switch (view.getId()) {


            // 返回
            case R.id.ll_back:
                finish();
                break;


            //校准时间
            case R.id.et_item7:
                // TODO: 2023/6/26  
                break;

            // 保存
            case R.id.ll_save:

                et_item9.clearFocus();

                if (isCk) {
                    mData.rkl = et_item5.getText().toString();
                    mData.ckl = et_item6.getText().toString();
                } else {
                    mData.csl = et_item5.getText().toString();
                    mData.rkl = et_item6.getText().toString();
                }

                mData.jzsj = et_item7.getText().toString();
                mData.jzry = et_item8.getText().toString();
                mData.qksm = et_item9.getText();

                if (TextUtils.isEmpty(checkEmpty())) {
                    Map<String, String> map = GsonUtil.GsonToMaps(GsonUtil.GsonToString(mData));

                    String url = isCk?ApiUrl. URL_CHECK_SAVE_CK:ApiUrl. URL_CHECK_SAVE_RK;

                    mPresenter.showSaveDialog(map,url);
                } else {
                    ToastUtil.showToast(mContext,checkEmpty());
                }

                break;

        }
    }


    /*
     * 判空处理
     * */
    private String checkEmpty(){
        if (TextUtils.isEmpty(et_item6.getText().toString())) {
            return "请输入当前重量！";
        }  else if (Double.valueOf(et_item6.getText().toString()) <= 0) {
            return "请输入正确的当前重量！";
        }else if (TextUtils.isEmpty(et_item7.getText().toString())) {
            return "请选择校正时间！";
        } else if (TextUtils.isEmpty(et_item8.getText().toString())) {
            return "请输入校正人员！";
        } else if (TextUtils.isEmpty(mData.qksm)) {
            return "请输入情况说明！";
        } else {
            return "";
        }
    }


    @Override
    protected CheckInfoPresenter createPresenter(){
        return new CheckInfoPresenter();
    }


    @Override
    public void showError(String msg){
        ToastUtil.showToast(mContext,msg);
    }


    @Override
    public void saveDataSuccess(Object bean){
        ToastUtil.showToast(mContext,"校准成功！");
        setResult(13,getIntent().putExtra("CURRENT_WEIGHT",et_item6.getText().toString()));
        finish();
    }


}
