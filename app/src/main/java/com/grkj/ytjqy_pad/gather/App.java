package com.grkj.ytjqy_pad.gather;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.clj.fastble.BleManager;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.grkj.ytjqy_pad.mvp.view.activity.HomeActivity;
import com.hjq.toast.Toaster;
import com.jxprint.BTPrinterManager;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashSet;
import java.util.Set;



/**
 * Created by yusam on 2022/3/19
 * Application组件
 */

public class App extends Application {

    private static App instance;
    private Set<Activity> allActivities;
    public final static String DESC_KEY = "gdsgf001";

    public static synchronized App getInstance() {
        return instance;
    }

    public static Context getContext() {
        return getInstance().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initBlt();
        // 初始化 Toast 框架
        Toaster.init(this);
        BTPrinterManager.getInstance().init(this);
        BTPrinterManager.getInstance().setEffectOnReceive(true);
        BTPrinterManager.getInstance().addBtToAvailableList("JXPRINTER", 203);

//        initLeakCanary();
    }




//    private void initLeakCanary() {
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        sRefWatcher = LeakCanary.install(this);
//    }
//
//    private static RefWatcher sRefWatcher;
//
//    public static RefWatcher getRefWatcher() {
//        return sRefWatcher;
//    }

    /**
     * 初始化蓝牙
     * */
    private void initBlt() {
        //蓝牙设置
        BleManager.getInstance().init(this);
        BleScanRuleConfig config = new BleScanRuleConfig.Builder()
                //            .setServiceUuids(serviceUuids) // 只扫描指定的服务的设备，可选
                .setDeviceName(true, "DP","CC4","DL","XS-BLE") //true 模糊搜索； 只扫描指定广播名的设备，可选
                //            .setDeviceMac(mac) // 只扫描指定mac的设备，可选
                //            .setAutoConnect(isAutoConnect) // 连接时的是否自动链接参数，可选，默认false
                .setScanTimeOut(3000) // 扫描持续时间，可选，默认10秒 设置0持续扫描
                .build();

        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(5, 2000)
                .setConnectOverTime(5000)
                .setOperateTimeout(2000)
                .initScanRule(config);
    }

    /**
     * 这是activity的添加，删除和退出app
     */
    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    public void exitLiveApp() {
//        WeightUtils.getInstance().stopWeight();

        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
    }


    public void goToHome() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    if (!act.getClass().getName().equals(HomeActivity.class.getName())){
                        act.finish();
                    }
                }
            }
        }
    }


    public int getActivitiesSize(){
        return allActivities.size();
    }

    //64k
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);

    }


}
