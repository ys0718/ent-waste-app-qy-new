package com.grkj.ytjqy_pad.utils.common;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hjq.toast.Toaster;
import com.hjq.toast.style.WhiteToastStyle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chenerte on 2017/8/6.
 */

public class ToastUtil {
    private static String oldMsg = "-1";
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;
    private static int DURATION = 5000;

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

    public static void showToast(Context context, int resId, int gravity) {
        showToast(context, context.getString(resId), gravity, 0, 0);
    }

    public static void showToast(Context context, String s, int gravity) {
        showToast(context, s, gravity, 0, 0);
    }

    public static void showToast(Context context, int resId, int gravity, int offX, int offY) {
        showToast(context, context.getString(resId), gravity, offX, offY);
    }

//    public static void showToast(Context context, String s) {
//        if (toast == null) {
//            toast = Toast.makeText(context, s, Toast.LENGTH_LONG);
//            controlToastTime(toast,DURATION);
//            LinearLayout linearLayout = (LinearLayout) toast.getView();
//            TextView messageTextView = (TextView) linearLayout.getChildAt(0);
//            messageTextView.setTextSize(15);
//            messageTextView.setPadding(7, 5, 7, 5);
//            toast.show();
//            oneTime = System.currentTimeMillis();
//        } else {
//            twoTime = System.currentTimeMillis();
//            if (s.equals(oldMsg)) {
//                if (twoTime - oneTime > DURATION) {
//                    toast.setText(s);
//                    toast.show();
//                    oneTime = twoTime;
//                }
//            } else {
//                oneTime = System.currentTimeMillis();
//                oldMsg = s;
//                toast.setText(s);
//                toast.show();
//            }
//
//        }
//
//    }

    public static void showToast(Context context,String s){
//        if (toast == null) {
//            toast = new Toast(context);
//            toast = Toast.makeText(context,s,Toast.LENGTH_LONG);
//            LinearLayout linearLayout = (LinearLayout) toast.getView();
//            TextView messageTextView = (TextView) linearLayout.getChildAt(0);
//            messageTextView.setTextSize(15);
//            messageTextView.setPadding(7,5,7,5);
//        } else {
//            toast.cancel();//关闭当前的toast
//            toast = new Toast(context);
//            toast = Toast.makeText(context,s,Toast.LENGTH_LONG);
//            LinearLayout linearLayout = (LinearLayout) toast.getView();
//            TextView messageTextView = (TextView) linearLayout.getChildAt(0);
//            messageTextView.setTextSize(15);
//            messageTextView.setPadding(7,5,7,5);
//        }
//        toast.show();

        Toaster.setStyle(new WhiteToastStyle());
        Toaster.showLong(s);
    }


    public static void showTestToast(Context context,String s){
        if (toast == null) {
            toast = new Toast(context);
            toast = Toast.makeText(context,s,Toast.LENGTH_LONG);
            LinearLayout linearLayout = (LinearLayout) toast.getView();
            TextView messageTextView = (TextView) linearLayout.getChildAt(0);
            messageTextView.setTextSize(15);
            messageTextView.setPadding(7,5,7,5);
        } else {
            toast.cancel();//关闭当前的toast
            toast = new Toast(context);
            toast = Toast.makeText(context,s,Toast.LENGTH_LONG);
            LinearLayout linearLayout = (LinearLayout) toast.getView();
            TextView messageTextView = (TextView) linearLayout.getChildAt(0);
            messageTextView.setTextSize(15);
            messageTextView.setPadding(7,5,7,5);
        }
        controlToastTime(toast,100000);
        toast.show();
    }


    /**
     * 定时器
     * @param toast
     * @param duration
     */
    public static void controlToastTime(final Toast toast, int duration) {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, duration);
    }


    public static void showToast(Context context, String s, int gravity, int offX, int offY) {
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_LONG);
            controlToastTime(toast,DURATION);
            toast.setGravity(gravity, offX, offY);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > DURATION) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
            oneTime = twoTime;
        }
    }


}
