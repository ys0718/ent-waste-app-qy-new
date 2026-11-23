package com.grkj.module_hardware.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Create by seny on 2025/7/15
 * Description: 设备工具类
 **/
public class DeviceUtils {

//    private static DeviceUtils instance;
//
//    private DeviceUtils(){
//
//    }
//    public static DeviceUtils getInstance(){
//        if (instance == null) {
//            synchronized (DeviceUtils.class){
//                if (instance == null){
//                    instance = new DeviceUtils();
//                }
//            }
//        }
//        return instance;
//    }

    /**
     * 判断是否为L2s
     * */
    public static Boolean isL2sDevice(Context context){
        float dp = getDensity((Activity) context);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        boolean isL2sDevice = false;
        if (dp == 332 && dm.heightPixels == 1356 && dm.widthPixels == 720) {
            isL2sDevice = true;
        }
        return isL2sDevice;
    }

    /**
     * 判断是否为石化盈科设备
     * */
    public static Boolean isShykDevice(Context context){
        float dp = getDensity((Activity) context);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        boolean isShykDevice = false;
        if (dp == 480 && dm.heightPixels == 2016 && dm.widthPixels == 1080) {
            isShykDevice = true;
        }
        return isShykDevice;
    }


    private static float getDensity(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.densityDpi;
    }

}
