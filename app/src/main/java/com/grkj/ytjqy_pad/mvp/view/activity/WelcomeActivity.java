package com.grkj.ytjqy_pad.mvp.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.utils.common.ActivityUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;

/**
 * 启动页
 */
public class WelcomeActivity extends Activity {

    // TODO: 2022/4/6
    private int[] themes = {R.style.WelcomeStyle1, R.style.WelcomeStyle1, R.style.WelcomeStyle1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //随机设置启动图
//        int arrIndex = new Random().nextInt(themes.length - 1);
//        setTheme(themes[arrIndex]);

//        switch (Constants.VERSION_CODE){
//            case Constants.VERSION_CODE_SH:
//                setTheme(R.style.WelcomeStyle2);
//                break;
//            default:
//                setTheme(R.style.WelcomeStyle1);
//                break;
//        }


        setTheme(R.style.WelcomeStyle1);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String ip = (String) SPUtils.get(WelcomeActivity.this, "IP", "");
                if (TextUtils.isEmpty(ip)){
                    //跳转到服务器配置页
                    ActivityUtil.jumpToActivity(WelcomeActivity.this, SettingServerActivity.class);
                }else {
                    //跳转到登录页
                    ActivityUtil.jumpToActivity(WelcomeActivity.this, LoginActivity.class);
//                    ActivityUtil.jumpToActivity(WelcomeActivity.this, MainActivity.class);
                }
                finish();
            }
        }, 1000);
    }

    /**
     * 屏蔽物理返回按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
