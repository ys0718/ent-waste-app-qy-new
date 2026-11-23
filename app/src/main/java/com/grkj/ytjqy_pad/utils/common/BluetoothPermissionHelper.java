package com.grkj.ytjqy_pad.utils.common;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙权限请求辅助类
 * 根据 Android 版本动态请求必要的蓝牙权限，并通过回调返回结果。
 * 需要在 AndroidManifest.xml 中声明所有可能用到的权限（见下文）。
 */
public class BluetoothPermissionHelper {

    private final Activity activity;
    private final Callback callback;

    /**
     * 构造方法
     * @param activity 当前 Activity，用于发起权限请求
     * @param callback 权限请求结果的回调接口
     */
    public BluetoothPermissionHelper(Activity activity, Callback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    /**
     * 发起蓝牙权限请求
     * @param requestCode 请求码，用于在 onRequestPermissionsResult 中区分请求
     */
    public void requestPermissions(int requestCode) {
        List<String> permissionsToRequest = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12 (API 31) 及以上：使用细粒度蓝牙权限
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN);
            }
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT);
            }
            // 如果需要蓝牙广播功能，可以取消注释下一行
            // if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_ADVERTISE)
            //         != PackageManager.PERMISSION_GRANTED) {
            //     permissionsToRequest.add(Manifest.permission.BLUETOOTH_ADVERTISE);
            // }

            // 蓝牙扫描通常仍需要位置权限（用于获取扫描结果中的位置信息）
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        } else {
            // Android 11 (API 30) 及以下：使用传统蓝牙权限
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH);
            }
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_ADMIN)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_ADMIN);
            }

            // Android 10+ 蓝牙扫描必须拥有位置权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            } else {
                // 低于 Android 10 也可能需要位置权限（根据具体功能决定）
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    permissionsToRequest.toArray(new String[0]),
                    requestCode);
        } else {
            // 所有权限已授予，直接回调成功
            if (callback != null) {
                callback.onPermissionsGranted(requestCode);
            }
        }
    }

    /**
     * 在 Activity 的 onRequestPermissionsResult 中调用此方法，以处理权限请求结果。
     * @param requestCode  请求码
     * @param permissions  请求的权限数组
     * @param grantResults 授权结果数组
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }
        if (deniedPermissions.isEmpty()) {
            if (callback != null) {
                callback.onPermissionsGranted(requestCode);
            }
        } else {
            if (callback != null) {
                callback.onPermissionsDenied(requestCode, deniedPermissions);
            }
        }
    }

    /**
     * 权限请求回调接口
     */
    public interface Callback {
        /**
         * 所有请求的权限都被用户授予
         * @param requestCode 请求码
         */
        void onPermissionsGranted(int requestCode);

        /**
         * 有权限被用户拒绝
         * @param requestCode       请求码
         * @param deniedPermissions 被拒绝的权限列表
         */
        void onPermissionsDenied(int requestCode, List<String> deniedPermissions);
    }
}