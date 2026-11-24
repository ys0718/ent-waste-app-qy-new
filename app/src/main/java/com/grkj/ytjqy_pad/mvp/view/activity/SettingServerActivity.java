package com.grkj.ytjqy_pad.mvp.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.SimpleActivity;
import com.grkj.ytjqy_pad.gather.ApiServer;
import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.http.HttpMethods;
import com.grkj.ytjqy_pad.utils.common.ActivityUtil;
import com.grkj.ytjqy_pad.utils.common.AppUtils;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.view.SSZHTextView;
import com.socks.library.KLog;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 服务器配置
 */

public class SettingServerActivity extends SimpleActivity {
    private static final String TAG = "ip";

    //登录时间
    @BindView(R.id.tv_top1)
    TextView tv_top1;
    //登录名
    @BindView(R.id.tv_top2)
    SSZHTextView tv_top2;
    @BindView(R.id.im_top2)
    ImageView im_top2;
    //ip地址输入
    @BindView(R.id.et_ip)
    EditText et_ip;
    //端口号输入
    @BindView(R.id.et_port)
    EditText et_port;
    //服务器连接结果
    @BindView(R.id.ll_server_result)
    LinearLayout ll_server_result;
    //保存配置
    @BindView(R.id.btn_save)
    Button btn_save;
    /*//取消
    @BindView(R.id.btn_cancel)
    Button btn_cancel;*/
    //连接测试
    @BindView(R.id.btn_test)
    Button btn_test;
    //测试信息
    @BindView(R.id.tv_server_info)
    TextView tv_server_info;
    //检测服务器是否连接成功
    @BindView(R.id.tv_ser)
    TextView tv_ser;
    //检测服务器是否连接成功
    @BindView(R.id.iv_ser)
    ImageView iv_ser;
    //服务器连接loading
    @BindView(R.id.iv_server_loading)
    ImageView iv_server_loading;

    //是否选中http
    @BindView(R.id.iv_http)
    ImageView iv_http;

    //是否选中https
    @BindView(R.id.iv_https)
    ImageView iv_https;

    //菜单1.0
    @BindView(R.id.iv_v1)
    ImageView iv_v1;

    //菜单2.0
    @BindView(R.id.iv_v2)
    ImageView iv_v2;

    private Animation netAni;
    private String successIp;
    private String successPort;
    private String contractName = "http://";
    private boolean isMenuV1 = true;

    @Override
    protected int getLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_setting_server;
    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("配置服务器信息");
    }

    @Override
    protected void initEventAndData() {
        ButterKnife.bind(this);
        netAni = AnimationUtils.loadAnimation(this, R.anim.net_loading);
        LinearInterpolator lin = new LinearInterpolator();
        netAni.setInterpolator(lin);
        btn_save.setVisibility(View.GONE);
        ll_server_result.setVisibility(View.GONE);
        tv_top2.setVisibility(View.GONE);
        im_top2.setVisibility(View.GONE);
        //再次进入取出上次保存的服务器信息
        String ip = (String) SPUtils.get(mContext, "IP", "");
        String port = (String) SPUtils.get(mContext, "Port", "");
        contractName = (String) SPUtils.get(mContext, "CONTRACT_NAME", "http://");

        isMenuV1 = (boolean) SPUtils.get(mContext,Constants.IS_MENU_V1,true);

        KLog.e("ip:" + ip);
        KLog.e("port:" + port);

        if (TextUtils.isEmpty(ip)){
            et_ip.setText(Constants.DEFAULT_IP);
        }else {
            et_ip.setText(ip);
        }


        if (TextUtils.isEmpty(port)){
            et_port.setText(Constants.DEFAULT_PORT);
        }else {
            et_port.setText(port);
        }

        if(contractName.equals("http://")){
            iv_http.setImageResource(R.mipmap.ic_selected);
            iv_https.setImageResource(R.mipmap.ic_unselected);
        }else {
            iv_http.setImageResource(R.mipmap.ic_unselected);
            iv_https.setImageResource(R.mipmap.ic_selected);
        }

        iv_v1.setImageResource(isMenuV1?R.mipmap.ic_selected:R.mipmap.ic_unselected);
        iv_v2.setImageResource(!isMenuV1?R.mipmap.ic_selected:R.mipmap.ic_unselected);

        initBottom();
//
//        //首次进入配置默认服务器
//        if (TextUtils.isEmpty(ip) && TextUtils.isEmpty(port)) {
//            et_ip.setText(Constants.DEFAULT_IP);
//            et_port.setText(Constants.DEFAULT_PORT);
//        }else {
//            et_ip.setText(ip);
//            et_port.setText(port);
//        }


    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int result = msg.arg1;

            //新开延时线程处理ui
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isChecking = false;
                    if (iv_server_loading!=null){
                        iv_server_loading.clearAnimation();
                        tv_ser.setVisibility(View.VISIBLE);
                        iv_ser.setVisibility(View.VISIBLE);

                        tv_server_info.setText("检测完成");
                        btn_test.setText("重新测试");
                        if (result == 1) {
                            tv_ser.setText("连接成功");
                            tv_ser.setTextColor(getResources().getColor(R.color.color_success_green));
                            iv_ser.setImageResource(R.mipmap.ic_selected);
                            btn_save.setVisibility(View.VISIBLE);
                            successIp = et_ip.getText().toString();
                            successPort = et_port.getText().toString();

                        } else {
                            tv_ser.setText("连接失败");
                            tv_ser.setTextColor(getResources().getColor(R.color.color_yellow));
                            iv_ser.setImageResource(R.mipmap.ic_error);
                            successIp = "";
                            successPort = "";
                        }
                    }


                }
            }, 3000);
        }
    };

    @OnClick({R.id.btn_save, R.id.btn_test,R.id.ll_back,R.id.ll_htttp,R.id.ll_htttps,R.id.ll_v1,R.id.ll_v2})
    public void onClick(View view) {
        switch (view.getId()) {
            // 保存配置
            case R.id.btn_save:
                if (TextUtils.isEmpty(et_ip.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "请输入IP地址!", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(et_port.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "请输入端口号!", Toast.LENGTH_LONG).show();
                } else {
                    save();
                }
                break;
            //  服务器连接
            case R.id.btn_test:
                if (TextUtils.isEmpty(et_ip.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "请输入IP地址", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(et_port.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "请输入端口号", Toast.LENGTH_LONG).show();
                } else {
                    testPing();
                }
                break;
            // 返回
            case R.id.ll_back:
                finish();
                break;
            // 菜单v1.0
            case R.id.ll_v1:
                isMenuV1 = true;
                iv_v1.setImageResource(R.mipmap.ic_selected);
                iv_v2.setImageResource(R.mipmap.ic_unselected);
                break;
            // 菜单v2.0
            case R.id.ll_v2:
                isMenuV1 = false;
                iv_v2.setImageResource(R.mipmap.ic_selected);
                iv_v1.setImageResource(R.mipmap.ic_unselected);
                break;
            case R.id.ll_htttp:
                iv_http.setImageResource(R.mipmap.ic_selected);
                iv_https.setImageResource(R.mipmap.ic_unselected);
                contractName = "http://";
                break;
            // https
            case R.id.ll_htttps:
                iv_http.setImageResource(R.mipmap.ic_unselected);
                iv_https.setImageResource(R.mipmap.ic_selected);
                contractName = "https://";
                break;
        }
    }

    private boolean isChecking = true;

    private void testPing() {
        //判断输入IP地址是否合法
        if (IpCheck(et_ip.getText().toString())) {

            tv_server_info.setText("正在检测中...");
            ll_server_result.setVisibility(View.VISIBLE);
            tv_ser.setVisibility(View.INVISIBLE);
            iv_ser.setVisibility(View.INVISIBLE);
            btn_save.setVisibility(View.GONE);

            if (netAni != null) {
                iv_server_loading.startAnimation(netAni);
            }


            //网络请求置于子线程中进行
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();
                    boolean serverAvailable = AppUtils.isServerAvailable(et_ip.getText().toString(), et_port.getText().toString());
                    if (serverAvailable) {//服务器可用
                        msg.arg1 = 1;
                        handler.sendMessage(msg);
                    } else {
                        msg.arg1 = 0;
                        handler.sendMessage(msg);
                    }
                }
            }).start();
        } else {
            ToastUtil.showToast(mContext, "请输入合法的ip地址！");
        }
    }

    private void save() {
        String ip = et_ip.getText().toString();
        String port = et_port.getText().toString();
        String url = contractName + et_ip.getText().toString() + ":" + et_port.getText().toString();
        //判断点击保存时的IP地址与测试时的IP地址是否一致
        if (ip.equals(successIp) && port.equals(successPort)) {

            SPUtils.put(mContext,Constants.IS_MENU_V1,isMenuV1);

            SPUtils.put(mContext, "IP", successIp);
            SPUtils.put(mContext, "Port", successPort);
            SPUtils.put(mContext, "CONTRACT_NAME", contractName);

            //保存输入数据
            SPUtils.put(mContext, "URL", url);
            ToastUtil.showToast(mContext, "保存成功,正在重启app...");
            //跳转到登录页面
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    HttpMethods.getInstance().create(ApiServer.class);
//                    ActivityUtil.jumpToActivity(mContext, LoginActivity.class);
//                    finish();

                    //重启APP
                    final Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
                    if (intent != null) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);

                }
            }, 1000);
        } else {
            ToastUtil.showToast(mContext, "请先重新测试！");
        }
    }

    //判断输入IP是否合法
    public static boolean IpCheck(String ip) {
//        boolean CheckResult = false;
//        if (ip != null || !ip.isEmpty() | ip.length() < 16) {
//            // 定义正则表达式
//            String regex = "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
//                    "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
//                    "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
//                    "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])";
//            CheckResult = Pattern.matches(regex, ip);
//        }
//        return CheckResult;
        return true;
    }

}