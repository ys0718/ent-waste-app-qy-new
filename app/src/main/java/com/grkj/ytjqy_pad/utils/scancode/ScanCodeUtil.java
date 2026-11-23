package com.grkj.ytjqy_pad.utils.scancode;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.utils.common.AppUtils;
import com.grkj.ytjqy_pad.utils.common.PermissionUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.scancode.utils.Constant;
import com.grkj.ytjqy_pad.utils.view.ScanDialog;

/**
 * @author JueHongP
 */
public class ScanCodeUtil {

    private final Activity mActivity;
    /**
     * 是否横屏
     */
    private final boolean isLandscape;
    /**
     * 延迟
     */
    private final long delayTime;
    /**
     * 扫描识别码
     */
    public int scanRequestCode;
    /**
     * 扫描结果监听
     */
    private ResultListener mListener;
    /**
     * 扫码弹窗
     */
    private ScanDialog roundCornerDialog;

    /**
     * 是否从经营台账扫码
     */
    boolean isJyTz = false;

    public ScanCodeUtil(Activity activity, boolean isLandscape, long delayTime, int requestCode) {
        this.mActivity = activity;
        this.isLandscape = isLandscape;
        this.scanRequestCode = requestCode;
        this.delayTime = delayTime;
    }

    public ScanCodeUtil(Activity activity, boolean isLandscape, long delayTime, int requestCode, boolean isJyTz) {
        this.mActivity = activity;
        this.isLandscape = isLandscape;
        this.scanRequestCode = requestCode;
        this.delayTime = delayTime;
        this.isJyTz = isJyTz;
    }

    /**
     * 定义回调接口
     */
    public interface ResultListener {
        /**
         * 设置扫描结果监听
         */
        void getResult(String result);
    }

    /**
     * 实现回调接口
     */
    public void getResultListener(ResultListener resultListener) {
        mListener = resultListener;
    }



    public void scanCode() {
        scanCode(ScanDialog.TYPE_SCAN);
    }

    public void scanCode(int type) {
        // 横屏或者手持终端设备
        if (isLandscape || Constants.judgeDevice()) {
            showScanDialog(type);
        } else {
            PermissionUtils.requestCamera(mActivity, new PermissionUtils.OnBooleanListener() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(mActivity, CommonScanActivity.class);
                    intent.putExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_QRCODE_MODE);
                    mActivity.startActivityForResult(intent, scanRequestCode);
                }

                @Override
                public void onCancel() {
                    ToastUtil.showToast(mActivity, "扫描二维码需要开启相机权限");
                }
            });
        }
    }


    /**
     * 扫码枪弹窗
     */
    private void showScanDialog(int type) {
        if (roundCornerDialog == null) {
            if (isJyTz) {
                roundCornerDialog = new ScanDialog(mActivity, true);
            } else {
                roundCornerDialog = new ScanDialog(mActivity);
            }
            roundCornerDialog.setDialogType(type);
            roundCornerDialog.setOnScanResultCallBack(new ScanDialog.OnScanResultCallBack() {
                @Override
                public void OnScanSuccess(String result) {
                    if (mListener != null) {
                        mListener.getResult(result);
                        roundCornerDialog.dismiss();
                    }
                }

                @Override
                public void OnScanFail(String errorMsg) {

                }
            });
        }

        new Handler().postDelayed(() -> {
            //显示对话框
            roundCornerDialog.show();
        }, delayTime);
    }

}
