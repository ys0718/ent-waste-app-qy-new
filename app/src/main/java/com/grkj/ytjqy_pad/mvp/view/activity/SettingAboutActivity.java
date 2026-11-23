package com.grkj.ytjqy_pad.mvp.view.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.SystemInfoBean;
import com.grkj.ytjqy_pad.mvp.model.VersionInfoBean;
import com.grkj.ytjqy_pad.mvp.presenter.SettingAboutPresenter;
import com.grkj.ytjqy_pad.utils.common.AppUtils;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.update.UpdateApkUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2022/3/20.
 * 系统设置-关于
 */
public class SettingAboutActivity extends BaseActivity<SettingAboutPresenter> implements SettingAboutPresenter.View{
    //版本号
    @BindView(R.id.tv_item1)
    TextView tv_item1;
    //设备号
    @BindView(R.id.tv_item2)
    TextView tv_item2;
    //技术支持
    @BindView(R.id.tv_item3)
    TextView tv_item3;
    //联系人
    @BindView(R.id.tv_item4)
    TextView tv_item4;
    //电话
    @BindView(R.id.tv_item5)
    TextView tv_item5;

    @Override
    protected int getLayout(){
        return R.layout.activity_about;
    }

    @Override
    protected void initEventAndData(){

        //隐藏技术支持
        tv_item2.setVisibility(View.GONE);

        String deviceCode = SPUtils.get(mContext,Constants.DEVICE_CODE,"").toString();
        tv_item1.setText("V"+AppUtils.getVersionName(mContext));
        tv_item2.setText(deviceCode);

        String system_info_str = SPUtils.get(mContext,"SYSTEM_INFO","").toString();

        if (!TextUtils.isEmpty(system_info_str)){
            SystemInfoBean systemInfoBean = GsonUtil.GsonToBean(system_info_str,SystemInfoBean.class);
            tv_item3.setText(systemInfoBean.tecSupport);
            tv_item4.setText(systemInfoBean.lxr);
            tv_item5.setText(systemInfoBean.lxPhone);
        }else {
            tv_item3.setText(getString(R.string.str_support2));
            tv_item4.setText(getString(R.string.str_about_contact));
            tv_item5.setText(getString(R.string.str_about_phone));
        }


//        switch (Constants.VERSION_CODE) {
//            case Constants.VERSION_CODE_SH:
//                tv_item3.setText(getString(R.string.str_support2_sh));
//                tv_item4.setText(getString(R.string.str_about_contact_sh));
//                tv_item5.setText(getString(R.string.str_about_phone_sh));
//                break;
//            default:
//                tv_item3.setText(getString(R.string.str_support2));
//                tv_item4.setText(getString(R.string.str_about_contact));
//                tv_item5.setText(getString(R.string.str_about_phone));
//                break;
//        }

    }

    @OnClick({R.id.ll_check_version,R.id.ll_back})
    public void onViewClicked(View view){
        switch (view.getId()) {
            case R.id.ll_check_version:
                mPresenter.checkVersion(isLandscape);
                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    protected void initLandscapeView(){
        super.initLandscapeView();
        initTop();
    }

    @Override
    protected void initPortraitView(){
        super.initPortraitView();
        initTopToolbar("关于");
    }

    @Override
    protected SettingAboutPresenter createPresenter(){
        return new SettingAboutPresenter();
    }

    @Override
    public void showError(String msg){
        toastShow(msg);
    }

    @Override
    public void checkVersionSuccess(Object model){
        if (model != null) {
            VersionInfoBean bean = GsonUtil.GsonToBean(model,VersionInfoBean.class);
            if (!TextUtils.isEmpty(bean.code)) {
                double netVersion = Double.valueOf(bean.version.replace(".",""));
                double localVersion = Double.valueOf(AppUtils.getAppVersionName(mContext,getPackageName()).replace(".",""));
                if (AppUtils.getAppVersionCode(mContext,getPackageName()) < Double.valueOf(bean.code) || localVersion < netVersion) {
                    UpdateApkUtil.showUpdateDialog(SettingAboutActivity.this,bean,null);
                } else {
                    ToastUtil.showToast(mContext,"当前已更新至最新版本！");
                }
            }
        }
    }
}
