package com.grkj.ytjqy_pad.utils.common;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.utils.scancode.CommonScanActivity;
import com.grkj.ytjqy_pad.utils.scancode.utils.Constant;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * 权限申请工具类
 * Created by yusam on 2018/7/23.
 */

public class PermissionUtils {

    /**
     * 请求定位权限
     */
    public static void requestLocationPermission(final Context context, OnBooleanListener listener) {
        onBooleanListener = listener;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            RxPermissions.getInstance(context).request(Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (!aBoolean) {
                                showSettingDialog(context, "定位与存储");
                            } else {
                                onBooleanListener.onSuccess();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                        }
                    });
        } else {
            RxPermissions.getInstance(context).request(Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (!aBoolean) {
                                showSettingDialog(context, "定位与存储");
                            } else {
                                onBooleanListener.onSuccess();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                        }
                    });
        }
    }

    /**
     * 请求蓝牙权限
     */
    public static void requestBluetoothPermission(final Context context, OnBooleanListener listener) {
        onBooleanListener = listener;
        RxPermissions.getInstance(context).request(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (!aBoolean) {
                            showSettingDialog(context, "蓝牙权限");
                        } else {
                            onBooleanListener.onSuccess();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    /**
     * 请求相机权限跳转到扫码页
     */
    public static void requestCameraAndScan(final Activity context, OnBooleanListener listener) {
        onBooleanListener = listener;
        RxPermissions.getInstance(context).request(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (!aBoolean) {
                            showSettingDialog(context, "相机与存储");
                        } else {
                            ActivityUtil.jumpToActivityWithInt(context, CommonScanActivity.class, Constants.SCAN_REQUEST_CODE, Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_QRCODE_MODE);
                            onBooleanListener.onSuccess();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }


    /**
     * 请求相机权限跳转到扫码页
     */
    public static void requestCamera(final Context context, OnBooleanListener listener) {
        onBooleanListener = listener;
        RxPermissions.getInstance(context).request(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (!aBoolean) {
                            showSettingDialog(context, "相机与存储");
                        } else {
                            onBooleanListener.onSuccess();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    /**
     * 请求读写权限
     */
    public static void requestStoragePermission(final Activity context, OnBooleanListener listener) {
        onBooleanListener = listener;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            onBooleanListener.onSuccess();
            return;
        }

        RxPermissions.getInstance(context).request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (!aBoolean) {
                            showSettingDialog(context, "读写");
                        } else {
                            onBooleanListener.onSuccess();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    /**
     * 弹出需要权限窗口
     *
     * @param mContext
     * @param name     权限名称
     */
    public static void showSettingDialog(final Context mContext, String name) {
        new AlertDialog.Builder(mContext)
                .setMessage("app需要开启" + name + "权限才能使用此功能")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                        mContext.startActivity(intent);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (onBooleanListener != null) {
                            onBooleanListener.onCancel();
                        }
                    }
                })
                .create()
                .show();
    }

    public static final int REQUEST_BLUETOOTH_PERMISSIONS = 100;

    /**
     * 请求蓝牙操作所需的运行时权限。
     * 适配 Android 6.0 (API 23) 及以上版本，并针对 Android 12 (API 31) 引入了新的蓝牙权限模型。
     *
     * 注意：使用前需在 AndroidManifest.xml 中声明所有可能用到的权限。
     * 例如：
     * <uses-permission android:name="android.permission.BLUETOOTH" />
     * <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
     * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
     * <!-- 针对 Android 12+ -->
     * <uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
     * <uses-permission android:name="android.permission.BLUETOOTH_ADVERTISE" />
     * <uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
     *
     * 此外，如果应用目标是 Android 10+ 并进行蓝牙扫描，还需在 manifest 中请求位置权限
     * （通常为 ACCESS_FINE_LOCATION 或 ACCESS_COARSE_LOCATION）。
     *
     * @param activity    当前 Activity，用于发起权限请求
     * @param requestCode 请求码，用于在 onRequestPermissionsResult 中识别该请求
     */
    public static void requestBluetoothPermissions(Activity activity, int requestCode) {
        List<String> permissionsToRequest = new ArrayList<>();

        // 根据 Android 版本确定需要请求的权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12 (API 31) 及以上：使用细粒度蓝牙权限
            // 如果需要执行蓝牙扫描
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN);
            }
            // 如果需要发起蓝牙连接
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT);
            }
            // 如果需要蓝牙广播（可选，根据实际需求添加）
            // if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_ADVERTISE)
            //         != PackageManager.PERMISSION_GRANTED) {
            //     permissionsToRequest.add(Manifest.permission.BLUETOOTH_ADVERTISE);
            // }

            // 对于蓝牙扫描，Android 12+ 仍可能需要位置权限（取决于是否要获取扫描结果中的位置信息）
            // 通常如果只需要设备发现，可以只请求 ACCESS_FINE_LOCATION 或 ACCESS_COARSE_LOCATION
            // 这里根据需求添加（示例中假设需要精确定位）
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
            // 对于 Android 10+，蓝牙扫描需要位置权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            } else {
                // 低于 Android 10 也可能需要位置权限（取决于具体功能）
                // 这里作为示例仍然添加
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }
        }

        // 如果有权限需要请求，则发起请求
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    permissionsToRequest.toArray(new String[0]),
                    requestCode);
        } else {
            if(onBooleanListener!=null){
                onBooleanListener.onSuccess();
            }
        }
    }



    public static OnBooleanListener onBooleanListener;

    public static OnBooleanListener getOnBooleanListener() {
        return onBooleanListener;
    }

    public static void setOnBooleanListener(OnBooleanListener onBooleanListener) {
        PermissionUtils.onBooleanListener = onBooleanListener;
    }

    public interface OnBooleanListener {
        void onSuccess();

        void onCancel();
    }

}
