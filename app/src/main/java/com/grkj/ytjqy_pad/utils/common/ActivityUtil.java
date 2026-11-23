package com.grkj.ytjqy_pad.utils.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * 活动工具
 */

public class ActivityUtil {



    /**
     * 跳转到另一个activity
     */
    public static Intent jumpToActivity(Activity current, Class mClass) {
        Intent intent = new Intent(current, mClass);
        current.startActivity(intent);
        return intent;
    }

    /**
     * 跳转到另一个activity(有返回值)
     */
    public static Intent jumpToActivity(Activity current, Class mClass, int requestCode) {
        Intent intent = new Intent(current, mClass);
        current.startActivityForResult(intent, requestCode);
        return intent;
    }

    /**
     * 携带String跳转到另一个activity(有返回值)
     */
    public static Intent jumpToActivityWithString(Activity current, Class mClass, int requestCode, String extrasName, String object) {
        Intent intent = new Intent(current, mClass);
        intent.putExtra(extrasName, object);
        current.startActivityForResult(intent, requestCode);
        return intent;
    }

    /**
     * 携带int跳转到另一个activity(有返回值)
     */
    public static Intent jumpToActivityWithInt(Activity current, Class mClass, int requestCode, String extrasName, int object) {
        Intent intent = new Intent(current, mClass);
        intent.putExtra(extrasName, object);
        current.startActivityForResult(intent, requestCode);
        return intent;
    }

    /**
     * 携带Parcelable跳转到另一个activity(有返回值)
     */
    public static Intent jumpToActivityWithParcelable(Activity current, Class mClass, int requestCode, String extrasName, Parcelable object) {
        Intent intent = new Intent(current, mClass);
        intent.putExtra(extrasName, object);
        current.startActivityForResult(intent, requestCode);
        return intent;
    }

    /**
     * 携带Serializable跳转到另一个activity(有返回值)
     */
    public static Intent jumpToActivityWithSerializable(Activity current, Class mClass, int requestCode, String extrasName, Serializable object) {
        Intent intent = new Intent(current, mClass);
        intent.putExtra(extrasName, object);
        current.startActivityForResult(intent, requestCode);
        return intent;
    }

    public static boolean isExsitMianActivity(Context context, Class<?> cls){
        Intent intent = new Intent(context, cls);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

}
