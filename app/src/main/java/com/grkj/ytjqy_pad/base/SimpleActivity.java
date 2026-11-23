package com.grkj.ytjqy_pad.base;

import static com.grkj.ytjqy_pad.gather.Constants.SCAN_REQUEST_CODE;
import static com.grkj.ytjqy_pad.gather.Constants.USER_NAME;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.SystemInfoBean;
import com.grkj.ytjqy_pad.mvp.view.activity.LoginActivity;
import com.grkj.ytjqy_pad.utils.common.ActivityUtil;
import com.grkj.ytjqy_pad.utils.common.AppUtils;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.DialogUtils;
import com.grkj.ytjqy_pad.utils.common.GestureHandler;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;
import com.grkj.ytjqy_pad.utils.view.SSZHTextView;
import com.socks.library.KLog;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;


/**
 * Created by yusam on 2018/7/19.
 * 无MVP的activity基类
 */

public abstract class SimpleActivity extends SupportActivity {
    protected Activity mContext;
    private Unbinder mUnBinder;

    private Dialog dialog;
    protected String token;

    /** 触摸时按下的点 **/
    PointF downP = new PointF();
    /** 触摸时当前的点 **/
    PointF curP = new PointF();

    /** 手势监听 */
    GestureHandler mGestureHandler;
    /** 是否需要监听手势关闭功能 */
    protected boolean mNeedBackGesture = true;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
//        App.getRefWatcher().watch(this);
        mGestureHandler = new GestureHandler();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getLayout());

//        getWindow().setEnterTransition(new Fade().setDuration(2000));
//        getWindow().setExitTransition(new Fade().setDuration(2000));
////        getWindow().setEnterTransition(new Explode().setDuration(2000));
////        getWindow().setExitTransition(new Explode().setDuration(2000));

        token = (String) SPUtils.get(mContext, Constants.TOKEN, "");
        mUnBinder = ButterKnife.bind(this);
        App.getInstance().addActivity(this);

        initEventAndData();
        KLog.e("token:" + token);


        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            isLandscape = true;
            mNeedBackGesture = false;
            initLandscapeView();
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            isLandscape = false;
            initPortraitView();
        }

    }


    protected boolean isLandscape;

    protected void initLandscapeView() {
    }

    protected void initPortraitView() {
    }


    protected void initTop() {
        RelativeLayout toolbarLayout = findViewById(R.id.rl_top);
        TextView tv_top1 = toolbarLayout.findViewById(R.id.tv_top1);
        SSZHTextView tv_top2 = toolbarLayout.findViewById(R.id.tv_top2);
        tv_top1.setText(DateTimeUtil.getYMDW());
        String username = SPUtils.get(mContext, USER_NAME, "").toString();
        tv_top2.setText(getString(R.string.common_welcome, username));

        LinearLayout ll_home = findViewById(R.id.ll_home);
        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.getInstance().getActivitiesSize() == 2) {
                    finish();
                } else {
                    App.getInstance().goToHome();
                    finish();
                }
            }
        });

        initBottom();
    }


    protected void initTop(String title) {
        RelativeLayout toolbarLayout = findViewById(R.id.rl_top);
        TextView tv_top1 = toolbarLayout.findViewById(R.id.tv_top1);
        SSZHTextView tv_top2 = toolbarLayout.findViewById(R.id.tv_top2);
        tv_top1.setText(DateTimeUtil.getYMDW());
        String username = SPUtils.get(mContext, USER_NAME, "").toString();
        tv_top2.setText(getString(R.string.common_welcome, username));
        TextView toolbar_title = findViewById(R.id.tv_title);
        if (toolbar_title!=null){
            toolbar_title.setText(title);
        }
        LinearLayout ll_home = findViewById(R.id.ll_home);
        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.getInstance().getActivitiesSize() == 2) {
                    finish();
                } else {
                    App.getInstance().goToHome();
                    finish();
                }
            }
        });

        initBottom();
    }


    protected void initBottom() {
        TextView mVersion = findViewById(R.id.mVersion);
        mVersion.setText("当前版本号:V" + AppUtils.getVersionName(mContext));
        TextView tv_company = findViewById(R.id.tv_company);
        ImageView login_logo_iv = findViewById(R.id.login_logo_iv);

        String system_info_str = SPUtils.get(mContext,"SYSTEM_INFO","").toString();
        SystemInfoBean bean = GsonUtil.GsonToBean(system_info_str,SystemInfoBean.class);
        tv_company.setText(getString(R.string.str_support));
        login_logo_iv.setImageResource(R.mipmap.ic_login_top_logo2);

        initSystemInfo(bean,tv_company,login_logo_iv);

//        switch (Constants.VERSION_CODE) {
//            case Constants.VERSION_CODE_SH:
//                login_logo_iv.setImageResource(R.mipmap.ic_login_top_logo2_sh);
//                tv_company.setText(getString(R.string.str_support_sh));
//                tv_company.setVisibility(View.GONE);
//                break;
//            default:
//                tv_company.setText(getString(R.string.str_support));
//                login_logo_iv.setImageResource(R.mipmap.ic_login_top_logo2);
//                break;
//        }
    }


    protected void initSystemInfo(SystemInfoBean bean,TextView tv_company,ImageView loginLogoIv){
        if (bean != null) {
            tv_company.setText(getString(R.string.str_support3,bean.tecSupport));
            if (!TextUtils.isEmpty(bean.baseImg)) {
                String base64Str = bean.baseImg;
                base64Str = base64Str.replace("data:image/jpeg;base64,","");
                base64Str = base64Str.replace("data:image/png;base64,","");
                byte[] decodedString = Base64.decode(base64Str,Base64.DEFAULT);
                //系统api加载，byte转bitmap
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
                loginLogoIv.setImageBitmap(decodedByte);
            } else {

                if (isLandscape){
                    loginLogoIv.setImageResource(R.mipmap.ic_login_top_logo);
                }else {
                    loginLogoIv.setImageResource(R.mipmap.ic_logo);
                }

            }
        }
    }


    //    protected void initTopToolbar(String title, String item1, boolean isItem2Visible) {
//        RelativeLayout toolbarLayout = findViewById(R.id.toolbar);
//        TextView toolbar_title = toolbarLayout.findViewById(R.id.toolbar_title);
//        ImageView iv_back = toolbarLayout.findViewById(R.id.iv_back);
//        TextView tv_item1 = toolbarLayout.findViewById(R.id.tv_left_bottom2);
//        TextView tv_item2 = toolbarLayout.findViewById(R.id.tv_title2);
//        toolbar_title.setText(title);
//        tv_item1.setText(item1);
//        tv_item2.setVisibility(isItem2Visible ? View.VISIBLE : View.GONE);
//
//        iv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }
//
    protected void initTopToolbar(String title) {
        RelativeLayout toolbarLayout = findViewById(R.id.toolbar);
        TextView toolbar_title = toolbarLayout.findViewById(R.id.tv_title);
        ImageView iv_back = toolbarLayout.findViewById(R.id.iv_back);
        toolbar_title.setText(title);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    protected void showLoading() {
        if (dialog == null) {
            dialog = DialogUtils.getInstance().createLoadingDialog(mContext);
        }
    }

    protected void hideLoading() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 退出登录
     */
    protected void backLoginActivity() {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否返回登录？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                roundCornerDialog.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        App.getInstance().exitLiveApp();
                        ActivityUtil.jumpToActivity(mContext, LoginActivity.class);
                        finish();
                    }
                }, 100);
            }

            @Override
            public void onCancelClick() {

            }
        });
        //显示对话框
        roundCornerDialog.show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getInstance().removeActivity(mContext);
        hideLoading();
        mUnBinder.unbind();
        mGestureHandler = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();//返回
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected abstract int getLayout();

    protected abstract void initEventAndData();

    public void toastShow(String text) {
        if ("HTTP 504 Unsatisfiable Request (only-if-cached)".equals(text)) {
            showMessageDialog("网络连接超时，请稍后再试");
        } else if ("未先登录，或token已过期，拒绝访问！".equals(text)) {
            showLogoutDialog(text);
        } else {
            showMessageDialog(text);
        }
    }

    /**
     * 提示窗口
     */
    protected void showMessageDialog(String mMessage) {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage(mMessage);
        //设置为只显示内容
        roundCornerDialog.setDialogType(RoundCornerDialog.TYPE_TIPS);
        //显示对话框
        roundCornerDialog.show();
    }

    /**
     * 提示窗口
     */
    private void showLogoutDialog(String mMessage) {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage(mMessage);
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                roundCornerDialog.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        App.getInstance().exitLiveApp();
                        ActivityUtil.jumpToActivity(mContext, LoginActivity.class);
                        finish();
                    }
                }, 100);
            }

            @Override
            public void onCancelClick() {

            }
        });
        //显示对话框
        roundCornerDialog.show();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mNeedBackGesture){
            return mGestureHandler.doEventF(ev,this) || super.dispatchTouchEvent(ev);
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyBoard(this, v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    protected boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 隐藏软键盘
     */
    public static void hideKeyBoard(Context context, IBinder token) {

        if (token != null) {

            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);

        }

    }


    protected interface ScanCodeReusultCallBack {
        void onResult(String result);
    }

    private ScanCodeReusultCallBack scanCodeReusultCallBack;

    protected void setScanCodeReusultCallBack(ScanCodeReusultCallBack scanCodeReusultCallBack) {
        this.scanCodeReusultCallBack = scanCodeReusultCallBack;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCAN_REQUEST_CODE:
                if (data != null) {
                    if (scanCodeReusultCallBack != null) {
                        String scanResult = data.getStringExtra("ScanCodeResult");
                        scanCodeReusultCallBack.onResult(scanResult);
                    }
                }
                break;
        }
    }

    /* *
     * @Description //检测Map空的key删除
     * @Date 17:31 2022/8/17
     * @Param [param]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    protected Map<String, Object> removeNullString(Map<String, Object> param) {
        try {
            Iterator<String> it = param.keySet().iterator();
            while (it.hasNext()) {
                Object ele = it.next();
                if (param.get(ele).toString().trim().equals("") || param.get(ele) == null || param.get(ele) instanceof Boolean) {
                    //hashMap.remove(ele);    //出错 修改了映射结构 影响了迭代器遍历
                    it.remove();              //用迭代器删除 则不会出错
                }
            }
            return param;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    protected String getJldw(){
        return SPUtils.get(mContext,"JLDW","吨").toString();
    }


    //保存最新一次计量单位选择
    protected void setJldw(String jldw){
        SPUtils.put(mContext,"JLDW",jldw);
    }


    //是否可点击
    private boolean clickable = true;

    protected void goToActivity(Intent temIntent,int requestCode){
        if (clickable){
            startActivityForResult(temIntent,requestCode);
        }else{
//            ToastUtil.showToast(mContext,"请勿频繁点击！");
        }
        banMulClick();
    }

    protected void goToActivity(Intent temIntent){
        if (clickable){
            startActivity(temIntent);
        }else{
//            ToastUtil.showToast(mContext,"请勿频繁点击！");
        }
        banMulClick();
    }

    //禁止多次点击
    private void banMulClick(){
        clickable = false;
//        if (clickable){
//            clickable = false;
//        }
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                clickable = true;
            }
        },2000);


    }

    protected void setTimeDialogClickListener(TextView textView){
        //防止控件设置不可点击
        textView.setEnabled(true);
        //防止弹出键盘
        textView.setFocusable(false);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimeUtil.showTimePicker(mContext, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        textView.setText(DateTimeUtil.formatDateTime(date, DateTimeUtil.DF_YYYY_MM_DD_HH_MM_SS));
                    }
                });
            }
        });
    }

    /**
     * 根据录入方式判断是否为称重
     * */
    protected boolean isWeightByLrfs(String lrfs){
        if (TextUtils.equals(lrfs,"1")){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 判断出库是否大于入库
     */
    protected boolean isExceed(String ckl, String ckDw, String rkl, String rkDw) {
        boolean result = false;
        double ckWeight = Double.parseDouble(ckl);
        double rkWeight = Double.parseDouble(rkl);
        switch (rkDw) {
            case "吨":
                if (!"吨".equals(ckDw)) {
                    ckWeight = ckWeight * 0.001;
                }
                result = ckWeight > rkWeight;
                break;
            case "千克":
                if (!"千克".equals(ckDw)) {
                    ckWeight = ckWeight * 1000;
                }
                result = ckWeight > rkWeight;
                break;
            default:
                break;
        }
        return result;
    }

}
