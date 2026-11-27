package com.grkj.ytjqy_pad.mvp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.clj.fastble.data.BleDevice;
import com.grkj.module_hardware.printer.PrinterUtils;
import com.grkj.module_hardware.utils.DeviceUtils;
import com.grkj.ytjqy_pad.BuildConfig;
import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.PrinterTypeBean;
import com.grkj.ytjqy_pad.mvp.model.SystemInfoBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.VersionInfoBean;
import com.grkj.ytjqy_pad.mvp.presenter.LoginPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.zyld.TodoZyldListActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.zyld.ZyldListActivity;
import com.grkj.ytjqy_pad.utils.common.AppUtils;
import com.grkj.ytjqy_pad.utils.common.CrcUtils;
import com.grkj.ytjqy_pad.utils.common.DES;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.update.UpdateApkUtil;
import com.grkj.ytjqy_pad.utils.view.BoldTextView;
import com.grkj.ytjqy_pad.utils.view.GetWeightDialog;
import com.socks.library.KLog;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2022/3/20.
 * 登录
 */

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginPresenter.View {

    //用户名
    @BindView(R.id.mUserName)
    EditText mUserName;

    //密码
    @BindView(R.id.mPassWord)
    EditText mPassWord;

    //是否显示密码
    @BindView(R.id.mIsShowPassWord)
    ImageView mIsShowPassWord;

    //是否记住密码
    @BindView(R.id.mIsRemember)
    CheckBox mIsRemember;

    //版本
    @BindView(R.id.mVersion)
    TextView mVersion;

    //登录图标
    @BindView(R.id.login_logo_iv)
    ImageView loginLogoIv;

    //动态系统名
    @BindView(R.id.app_name_tv)
    TextView appNameTv;

    //选择密码登录
    @BindView(R.id.ll_login_psw_select)
    RelativeLayout ll_login_psw_select;

    //选择刷卡登录
    @BindView(R.id.ll_login_card_select)
    RelativeLayout ll_login_card_select;

    //左侧底部布局
    @BindView(R.id.ll_left_bottom)
    LinearLayout ll_left_bottom;

    //正在识别卡片布局
    @BindView(R.id.cv_login_card)
    FrameLayout cv_login_card;

    //密码登录布局
    @BindView(R.id.cv_login_psw)
    FrameLayout cv_login_psw;

    //左侧底部图片
    @BindView(R.id.iv_left_bottom)
    ImageView iv_left_bottom;

    //左侧底部图片
    @BindView(R.id.tv_left_bottom)
    TextView tv_left_bottom;

    //左侧底部图片
    @BindView(R.id.iv_right_bottom)
    ImageView iv_right_bottom;

    //左侧底部图片
    @BindView(R.id.tv_right_bottom)
    TextView tv_right_bottom;

    //右侧底部布局
    @BindView(R.id.ll_right_bottom)
    LinearLayout ll_right_bottom;

    //当前日期
    @BindView(R.id.tv_right)
    TextView tv_right;

    //网络检测loading
    @BindView(R.id.iv_net_loading)
    ImageView iv_net_loading;

    //网络检测loading
    @BindView(R.id.tv_net_info)
    BoldTextView tv_net_info;

    //网络检测结果
    @BindView(R.id.ll_net_result)
    LinearLayout ll_net_result;


    //网络检测开始/暂停/重新检测
    @BindView(R.id.btn_net)
    Button btn_net;

    //网络检测网络是否可用
    @BindView(R.id.tv_net1)
    TextView tv_net1;

    //网络检测网络是否可用
    @BindView(R.id.iv_net1)
    ImageView iv_net1;

    //网络检测服务器是否可用
    @BindView(R.id.tv_net2)
    TextView tv_net2;

    //技术支持
    @BindView(R.id.tv_company)
    TextView tv_company;

    //当前设备号
    @BindView(R.id.tv_device)
    TextView tv_device;

    //网络检测服务器是否可用
    @BindView(R.id.iv_net2)
    ImageView iv_net2;


    @BindView(R.id.et_card_info)
    EditText et_card_info;

    private Boolean isIsShowPassWord; // 是否显示密码
    private Boolean isRemember; // 是否记住密码
    private String userName;//用户名
    private String password;//密码
    private String mHash;
    private Animation netAni;
    private boolean requestAble = true;
    private int currentPage = PAGE_SELECT_PSW;
    private String deviceCode;
    //    private static final int PAGE_SELECT_WAY = 0;
//    private static final int PAGE_SELECT_CARD = 1;
    private static final int PAGE_SELECT_PSW = 2;
    private static final int PAGE_SELECT_NET = 3;

    //具体界面对应的presenter由具体页面决定
    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    //设置窗体全屏
    @Override
    protected int getLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_login;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initEventAndData() {
        mNeedBackGesture = false;
//        switch (Constants.VERSION_CODE) {
//            case Constants.VERSION_CODE_SH:
//                loginLogoIv.setImageResource(R.mipmap.ic_login_top_logo_sh);
//                tv_company.setText(getString(R.string.str_support_sh));
//                tv_company.setVisibility(View.GONE);
//                break;
//            default:
//                tv_company.setText(getString(R.string.str_support));
//                loginLogoIv.setImageResource(R.mipmap.ic_login_top_logo);
//                break;
//        }

        DisplayMetrics dm = getResources().getDisplayMetrics();
        KLog.e("高度", "initEventAndData: " + dm.heightPixels);
        KLog.e("宽度", "initEventAndData: " + dm.widthPixels);
        KLog.e("密度", "initEventAndData: " + dm.densityDpi);
        KLog.e("密度", "initEventAndData: " + DeviceUtils.isShykDevice(mContext));


        //默认新标签
        PrinterTypeBean printerTypeBean = new PrinterTypeBean();
        printerTypeBean.bqlx = "n";
        printerTypeBean.dyjgg = "1";
        SPUtils.put(mContext, "PRINTER", GsonUtil.GsonToString(printerTypeBean));


        deviceCode = Build.SERIAL;

        if (deviceCode.equals("unknown")) {
            deviceCode = getIMEI(mContext);
        }

        SPUtils.put(mContext, Constants.DEVICE_CODE, deviceCode);


//        initBugly();
//        tv_device.setText(getString(R.string.str_device, deviceCode)+(Constants.IS_DEBUG?"(调试模式)":""));
        tv_device.setText(getString(R.string.str_device, deviceCode) + (BuildConfig.DEBUG ? "(调试模式)" : ""));
//        tv_device.setText(dm.heightPixels + "x" + dm.widthPixels + "x" + dm.densityDpi);

        tv_right.setText(DateTimeUtil.getYMDW());
        rememberPassword();
        mVersion.setText("当前版本号:V" + AppUtils.getVersionName(mContext));

        //记住上次登录模式
        Integer login_type = (Integer) SPUtils.get(mContext, "LOGIN_TYPE", currentPage);
        currentPage = login_type;
        showPage();
        netAni = AnimationUtils.loadAnimation(this, R.anim.net_loading);
        LinearInterpolator lin = new LinearInterpolator();
        netAni.setInterpolator(lin);

        et_card_info.setShowSoftInputOnFocus(false);
    }


    private String getIMEI(Context context) {
        String imei = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return imei;
    }

    private void initBugly() {
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setDeviceID(deviceCode);   // 建议设置，提升统计准确性
        strategy.setAppChannel("myChannel");             // 自定义渠道
        strategy.setAppVersion(AppUtils.getVersionName(mContext));                 // 覆盖 Manifest 中的版本号
//        strategy.setAppReportDelay(1000);               // 延迟上报，默认 10s
        CrashReport.initCrashReport(getApplicationContext(), "f18a47e9f1", true, strategy);
    }


    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();

    }


    @Override
    protected void initPortraitView() {
        super.initPortraitView();

        PrinterUtils.getInstance().closeBltPrinter();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getSystemInfo();


    }


    private boolean isChecking = true;

    private void checkNet() {
        tv_net_info.setText("网络/服务器检测中...");
        ll_net_result.setVisibility(View.INVISIBLE);

        if (netAni != null) {
            iv_net_loading.startAnimation(netAni);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (iv_net_loading == null) {
                    return;
                }

                isChecking = false;
                iv_net_loading.clearAnimation();
                boolean networkAvailable = AppUtils.isNetworkAvailable(mContext);
                ll_net_result.setVisibility(View.VISIBLE);
                tv_net_info.setText("网络/服务器检测完成");
                btn_net.setText("重新测试");
                if (networkAvailable) {//可用
                    tv_net1.setText("正常");
                    tv_net2.setText("正常");
                    tv_net1.setTextColor(getResources().getColor(R.color.color_success_green));
                    tv_net2.setTextColor(getResources().getColor(R.color.color_success_green));
                    iv_net1.setImageResource(R.mipmap.ic_selected);
                    iv_net2.setImageResource(R.mipmap.ic_selected);
                } else {
                    tv_net1.setText("不正常");
                    tv_net2.setText("不正常");
                    tv_net1.setTextColor(getResources().getColor(R.color.color_yellow));
                    tv_net2.setTextColor(getResources().getColor(R.color.color_yellow));
                    iv_net1.setImageResource(R.mipmap.ic_error);
                    iv_net2.setImageResource(R.mipmap.ic_error);
                }
            }
        }, 2000);
    }

    /*** 记住密码*/
    private void rememberPassword() {
        // 初始化是否隐藏密码
        isIsShowPassWord = false;
        // 初始化是否记住密码
        isRemember = (Boolean) SPUtils.get(mContext, Constants.IS_REMEMBER, false);
        mIsRemember.setChecked(isRemember);
        if (isRemember) {
            userName = (String) SPUtils.get(mContext, Constants.LOGIN_NAME, "");
            password = (String) SPUtils.get(mContext, Constants.PASSWORD, "");
            mUserName.setText(userName);
            mPassWord.setText(password);
        }
    }

    /*** 认证随机码*/
    private void verify_random() {
        if (!BuildConfig.LOG_DEBUG) {
            //APP完整性校验-Hash值
            mHash = CrcUtils.getHash(mContext);
            if (!TextUtils.isEmpty(mHash)) {
                Map<String, String> params = new HashMap<>();
                params.put("RANDOM", mHash);
                mPresenter.verifyRandom(SubscriberCallBack.requestBodyParam(params));
            } else {
                toastShow("HASH值获取失败");
            }
        } else {
            mPresenter.checkVersion(isLandscape);
        }
    }

    @Override
    public void verifyRandomSuccess(String string) {
    }

    @Override
    public void checkVersionSuccess(Object model) {
        if (model != null) {
            VersionInfoBean bean = GsonUtil.GsonToBean(model, VersionInfoBean.class);
            if (!TextUtils.isEmpty(bean.code)) {
                double netVersion = Double.valueOf(bean.version.replace(".", ""));
                double localVersion = Double.valueOf(AppUtils.getAppVersionName(mContext, getPackageName()).replace(".", ""));
                if (AppUtils.getAppVersionCode(mContext, getPackageName()) < Double.valueOf(bean.code) || localVersion < netVersion) {
                    UpdateApkUtil.showUpdateDialog(LoginActivity.this, bean, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull @androidx.annotation.NonNull MaterialDialog dialog, @NonNull @androidx.annotation.NonNull DialogAction which) {
                            dialog.dismiss();
                            if (which == DialogAction.POSITIVE) {
                                UpdateApkUtil.requestPermissionAndInstall(bean.attachmentId);
                            } else {
                                if (!TextUtils.isEmpty(bean.forceUpdateStatus) && "1".equals(bean.forceUpdateStatus)) {
                                    ToastUtil.showToast(mContext, "请更新至最新版本！");
//                                    System.exit(0);
                                } else {
                                    gotoHome();
                                }
                            }
                        }
                    });
                } else {
                    gotoHome();
                }
            } else {
                gotoHome();
            }
        } else {
            gotoHome();
        }
    }

    private void gotoHome() {
        ToastUtil.showToast(mContext, "登录成功！");
        boolean isMenuV1 = (boolean) SPUtils.get(mContext, Constants.IS_MENU_V1, true);

        if (isMenuV1){
            startActivity(new Intent(mContext, OldHomeActivity.class).putExtra(Constants.JUMP_KEY, menuBeanListStr));
        }else {
            startActivity(new Intent(mContext, HomeActivity.class).putExtra(Constants.JUMP_KEY, menuBeanListStr));
        }
        finish();
    }


    @Override
    public void getSystemInfoSuccess(Object model) {
        if (model != null) {
            SystemInfoBean bean = GsonUtil.GsonToBean(model, SystemInfoBean.class);
            SPUtils.put(mContext, "SYSTEM_INFO", GsonUtil.GsonToString(bean));
            initSystemInfo(bean, tv_company, loginLogoIv);

            ImageView iv_logo = findViewById(R.id.iv_logo);
            initSystemInfo(bean, tv_company, iv_logo);
        }
    }

    @Override
    public void testLoginSuccess(UserBean model) {
        SPUtils.put(mContext, Constants.IS_REMEMBER, mIsRemember.isChecked());
        SPUtils.put(mContext, Constants.OFFICE_ID, model.officeId);
        SPUtils.put(mContext, Constants.DWID, model.dwid);
        SPUtils.put(mContext, Constants.USER_NAME, model.name);
        SPUtils.put(mContext, Constants.USER_ID, model.id);

        //是否记住密码
        if (mIsRemember.isChecked()) {
            SPUtils.put(mContext, Constants.LOGIN_NAME, mUserName.getText().toString().trim());
            SPUtils.put(mContext, Constants.PASSWORD, mPassWord.getText().toString().trim());
        } else {
            SPUtils.put(mContext, Constants.LOGIN_NAME, "");
            SPUtils.put(mContext, Constants.PASSWORD, "");
        }
        token = (String) SPUtils.get(mContext, Constants.TOKEN, "");
        SPUtils.put(mContext, Constants.USER_INFO, GsonUtil.GsonToString(model));
        SPUtils.put(mContext, "isCzdw", model.isCzdw);


        startActivity(new Intent(mContext, JydwPrintListActivity.class));
//        startActivity(new Intent(mContext, ZyldListActivity.class));
    }


    @Override
    public void showError(String msg) {
        hideLoading();
        toastShow(msg);
        et_card_info.setText("");
        requestAble = true;
    }

    private String menuBeanListStr;

    @Override
    public void LoginSuccess(UserBean model, String menuBeanListStr) {
        hideLoading();
        SPUtils.put(mContext, Constants.IS_REMEMBER, mIsRemember.isChecked());
        SPUtils.put(mContext, Constants.OFFICE_ID, model.officeId);
        SPUtils.put(mContext, Constants.DWID, model.dwid);
        SPUtils.put(mContext, Constants.USER_NAME, model.name);
        SPUtils.put(mContext, Constants.USER_ID, model.id);


        //是否记住密码
        if (mIsRemember.isChecked()) {
            SPUtils.put(mContext, Constants.LOGIN_NAME, mUserName.getText().toString().trim());
            SPUtils.put(mContext, Constants.PASSWORD, mPassWord.getText().toString().trim());
        } else {
            SPUtils.put(mContext, Constants.LOGIN_NAME, "");
            SPUtils.put(mContext, Constants.PASSWORD, "");
        }
        token = (String) SPUtils.get(mContext, Constants.TOKEN, "");
        SPUtils.put(mContext, Constants.USER_INFO, GsonUtil.GsonToString(model));
        SPUtils.put(mContext, "isCzdw", model.isCzdw);


        mPresenter.checkVersion(isLandscape);
        this.menuBeanListStr = menuBeanListStr;
//        startActivity(new Intent(mContext, HomeActivity.class).putExtra(Constants.JUMP_KEY, menuBeanListStr));
//        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserName = null;
        mPassWord = null;
    }

    private BleDevice weightBle;

    //记录用户首次点击返回键的时间
    private long mFirstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - mFirstTime > 2000) {
                    ToastUtil.showToast(mContext, "再按一次退出程序");
                    mFirstTime = secondTime;
                    return true;
                } else {
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @OnClick({R.id.mIsShowPassWord, R.id.mLogin, R.id.mVersion, R.id.ll_login_psw_select, R.id.ll_login_card_select,
            R.id.ll_left_bottom, R.id.ll_right_bottom, R.id.btn_net, R.id.ll_server_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 显示和隐藏密码
            case R.id.mIsShowPassWord:
                isIsShowPassWord = !isIsShowPassWord;
                if (isIsShowPassWord) {
                    mIsShowPassWord.setImageDrawable(getResources().getDrawable(R.mipmap.ic_eye_open));
                    mPassWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mIsShowPassWord.setImageDrawable(getResources().getDrawable(R.mipmap.ic_eye_close));
                    mPassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            //登录
            case R.id.mLogin:
//                initWeightDialog();

                // TODO: 2024/6/24
                userName = mUserName.getText().toString().trim();
                password = mPassWord.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    ToastUtil.showToast(mContext, "请输入用户名");
                } else if (TextUtils.isEmpty(password)) {
                    ToastUtil.showToast(mContext, "请输入密码");
                } else {
                    // 加密
                    descAndLogin();
                }
                break;
//            //选择刷卡登录
//            case R.id.ll_login_card_select:
//                currentPage = PAGE_SELECT_CARD;
//                showPage();
//                break;
//            //选择密码登录
//            case R.id.ll_login_psw_select:
//                currentPage = PAGE_SELECT_PSW;
//                showPage();
//                break;
            //左下角按钮
            case R.id.ll_left_bottom:
//                switch (currentPage) {
//                    //选择登录时，退出应用
//                    case PAGE_SELECT_WAY:
//                        finish();
//                        break;
//                    //刷卡登录时，切换密码登录
//                    case PAGE_SELECT_CARD:
//                        currentPage = PAGE_SELECT_PSW;
//                        showPage();
//                        break;
//                    //密码登录时，切换刷卡登录
//                    case PAGE_SELECT_PSW:
//                        et_card_info.requestFocus();
//                        currentPage = PAGE_SELECT_CARD;
//                        showPage();
//                        break;
//                    //选择网络时，返回到选择登录
//                    case PAGE_SELECT_NET:
//                        currentPage = PAGE_SELECT_WAY;
//                        showPage();
//                        break;
//                }
                switch (currentPage) {
                    //密码登录时，切换刷卡登录
                    case PAGE_SELECT_PSW:
                        finish();
                        showPage();
                        break;
                    //选择网络时，返回到选择登录
                    case PAGE_SELECT_NET:
                        currentPage = PAGE_SELECT_PSW;
                        showPage();
                        break;
                }
                break;
            //网络检测
            case R.id.ll_right_bottom:
                currentPage = PAGE_SELECT_NET;
                showPage();
                break;

            //开始/暂停/重新检测
            case R.id.btn_net:
                checkNet();
                break;

            //服务器配置
            case R.id.ll_server_info:
                startActivity(new Intent(mContext, SettingServerActivity.class));
                break;
        }
    }

    private GetWeightDialog weightDialog;

    //称重弹窗
    private void initWeightDialog() {
        weightDialog = new GetWeightDialog(mContext);
        weightDialog.setConfirmListener(new GetWeightDialog.ConfirmListener() {
            @Override
            public void onConfirmClick(String weight) {
                KLog.e("GetWeightDialog：" + weight);
                ToastUtil.showToast(mContext, "扫描成功，已添加到待出库列表！");
            }

            @Override
            public void onCancelClick() {
                weightDialog.dismiss();
            }
        });
        weightDialog.show();
    }


    //选择方式页
    @BindView(R.id.ll_page_way)
    LinearLayout ll_page_way;
    //刷卡登录页
    @BindView(R.id.ll_page_card)
    LinearLayout ll_page_card;
    //密码登录页
    @BindView(R.id.ll_page_psw)
    LinearLayout ll_page_psw;
    //选择方式页
    @BindView(R.id.ll_page_net)
    LinearLayout ll_page_net;

    private void showPage() {
        switch (currentPage) {
            //密码登录
            case PAGE_SELECT_PSW:
                tv_left_bottom.setText("退出应用");
                iv_left_bottom.setImageResource(R.mipmap.ic_exit_app);
                tv_right_bottom.setText("网络测试");
                iv_right_bottom.setImageResource(R.mipmap.ic_network_check2);
                tv_right_bottom.setVisibility(View.VISIBLE);
                iv_right_bottom.setVisibility(View.VISIBLE);

                ll_page_way.setVisibility(View.GONE);
                ll_page_card.setVisibility(View.GONE);
                ll_page_psw.setVisibility(View.VISIBLE);
                ll_page_net.setVisibility(View.GONE);
                break;
            //网络测试
            case PAGE_SELECT_NET:
                tv_left_bottom.setText("返回");
                iv_left_bottom.setImageResource(R.mipmap.ic_back_login);
                tv_right_bottom.setVisibility(View.INVISIBLE);
                iv_right_bottom.setVisibility(View.INVISIBLE);

                ll_page_way.setVisibility(View.GONE);
                ll_page_card.setVisibility(View.GONE);
                ll_page_psw.setVisibility(View.GONE);
                ll_page_net.setVisibility(View.VISIBLE);
                break;
        }

    }

    /**
     * 加密并登录
     */
    private void descAndLogin() {
        showLoading();
        Map<String, String> params = new HashMap<>();
//        params.put("USERNAME",userName);
//        params.put("MESSAGE",password);
        params.put("CARDID", "");
        params.put("NOWVERSION", AppUtils.getVersionName(mContext));
//        params.put("ISDEBUG",Constants.IS_DEBUG?"1":"");
        params.put("ISDEBUG", BuildConfig.LOG_DEBUG ? "1" : "");
        SPUtils.put(mContext, "DEVICE_CODE", deviceCode);
        params.put("DEVICECODE", deviceCode);
        SPUtils.put(mContext, "LOGIN_TYPE", currentPage);
        KLog.e("DEVICECODE", deviceCode);

        try {
            String nameDes = DES.encryptDES(userName, App.DESC_KEY);
            String pswDes = DES.encryptDES(password, App.DESC_KEY);
            params.put("USERNAME", nameDes);
            params.put("MESSAGE", pswDes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPresenter.loginDes(SubscriberCallBack.requestBodyParam(params));
    }


}
