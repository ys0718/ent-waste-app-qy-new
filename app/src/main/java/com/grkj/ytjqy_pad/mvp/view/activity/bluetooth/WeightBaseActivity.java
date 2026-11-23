package com.grkj.ytjqy_pad.mvp.view.activity.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.grkj.module_hardware.weight.UsbWeighService;
import com.grkj.module_hardware.weight.WeightUtils2;
import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.base.BasePresenter;
import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.PrinterTypeBean;
import com.grkj.ytjqy_pad.utils.common.BluetoothPermissionHelper;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.PermissionUtils;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.view.BltSelectDialog;
import com.grkj.ytjqy_pad.utils.view.BoldTextView;
import com.grkj.ytjqy_pad.utils.view.MyLinearLayout;
import com.socks.library.KLog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by yusam on 2022/3/21.
 * 出库信息
 */

public abstract class WeightBaseActivity<T extends BasePresenter> extends BaseActivity implements BaseView {
    protected List<ParamListBean> allParamList = new ArrayList<>();

    //称重量
    @BindView(R.id.et_item8)
    protected EditText et_item8;

    //产废/入库/出库时间
    @BindView(R.id.et_item2)
    EditText et_item2;

    //称重模块
    @BindView(R.id.ll_weight)
    protected LinearLayout ll_weight;

    //扫码
    @BindView(R.id.ll_scan)
    protected LinearLayout ll_scan;

    //计量单位
    @BindView(R.id.et_item17)
    EditText et_item17;

    //计量单位
    @BindView(R.id.iv_jldw)
    ImageView iv_jldw;


    //修改时不可编辑布局
    @BindView(R.id.ll_clickable)
    protected MyLinearLayout ll_clickable;


    private BluetoothDevice _device = null;
    private boolean isFirstGetWeight = true;

    protected T mPresenter;

    private int compareSize = 1000;
    private int compareFloatSize = 4;
    private static final int COMPARE_SIZE_DUN = 1000;
    private static final int COMPARE_SIZE_KG = 1;


    protected abstract T createPresenter();

    protected UsbWeighService usbService;
//    protected WeightUtils.UsbHandler mHandler;

    //是否为称重入库
    protected boolean isWeight = false;

    //是否获取重量并更新到入库量中
    protected boolean isGetWeight = false;

    //是否点击清除
    protected boolean isClear;

    //是否为新增
    protected boolean isNew = false;

    //是否为复制
    protected boolean isCopy = false;


    @BindView(R.id.tv_weight1)
    protected BoldTextView tv_weight1;

    @BindView(R.id.tv_weight2)
    protected BoldTextView tv_weight2;

    @BindView(R.id.tv_weight3)
    protected EditText tv_weight3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        super.onCreate(savedInstanceState);

//        setJldwInfo(getJldw());
//        startWeight();

        //选择蓝牙称
        findViewById(R.id.ll_select_blt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    btnDevice_Click();
            }
        });
        //清零
        findViewById(R.id.ll_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usbService != null) {
                    tv_weight3.setText("0.000");
                    isClear = true;
                    usbService.write(orderZero());
                }
            }
        });

        //重新称重
        findViewById(R.id.ll_get_weight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUnit(getJldw());
            }
        });

        setTimeDialogClickListener(et_item2);
    }




    protected void handleReadOnly(boolean isIntercept) {
        et_item17.setEnabled(!isIntercept);
        et_item17.setClickable(!isIntercept);
        ll_clickable.setOnInterceptTouchEvent(isIntercept, getResources().getColor(R.color.gray));
    }


    protected void initWeight() {
        isWeight = getIntent().getBooleanExtra("isWeight", false);
        if (isWeight) {//称重
            ll_weight.setVisibility(View.VISIBLE);
            ll_scan.setVisibility(View.VISIBLE);
            et_item8.setEnabled(false);
            et_item8.setFocusable(false);
            et_item8.clearFocus();
            findViewById(R.id.ll_get_weight).setVisibility(View.VISIBLE);
        } else {
            ll_weight.setVisibility(View.GONE);
            ll_scan.setVisibility(View.GONE);
            iv_jldw.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 获取标签类型
     *
     * @return
     */
    protected String getBqlxFromDevice() {
        String printerStr = SPUtils.get(mContext, "PRINTER", "").toString();
        PrinterTypeBean printerTypeBean = GsonUtil.GsonToBean(printerStr, PrinterTypeBean.class);
        return printerTypeBean.bqlx;
    }

    /*
     * 通用转义
     * */
    protected String commonTransfer(String code, String type) {
        String result = "";
        for (int i = 0; i < allParamList.size(); i++) {
            List<ParamListBean> childList = allParamList.get(i).childList;
            if (childList != null && childList.size() != 0 && type.equals(allParamList.get(i).type)) {
                for (int j = 0; j < childList.size(); j++) {
                    if (!TextUtils.isEmpty(code) && code.equals(childList.get(j).code)) {
                        result = childList.get(j).value;
                    }
                }
            }
        }
        return result;
    }


    //选择蓝牙秤设备
    protected void btnDevice_Click() {

        final BltSelectDialog _deviceDialog = new BltSelectDialog(mContext);
        _deviceDialog.onConfirmListener = new BltSelectDialog.OnConfirmListener() {
            @Override
            public void confirm(BluetoothDevice device) {

                if(!mWeightUtils.isConnect){
//                    mWeightUtils.stopWeight();
                    _device = device;
                    SPUtils.put(mContext, Constants.BLT_CONNECTED_ADDRESS, device.getAddress());
                    mWeightUtils.btnConnect_Click(_device);
                }


//                PermissionUtils.requestBluetoothPermission(mContext, new PermissionUtils.OnBooleanListener() {
//                    @Override
//                    public void onSuccess() {
//                        mWeightUtils.btnConnect_Click(_device);
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });
                _deviceDialog.dismiss();

            }
        };
        _deviceDialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        startWeight();
    }

    /**
     * 选择计量单位
     */
    protected void changeUnit(String jldw) {
        isGetWeight = true;
        SPUtils.put(mContext, "JLDW", jldw);
        startWeight();
        KLog.e("jldw:" + jldw);
    }


    @Override
    public void onPause() {


        super.onPause();
        stopWeight();
    }

    private WeightUtils2 mWeightUtils = null;

    protected void startWeight() {
        mWeightUtils = WeightUtils2.getInstance();
        if (!isWeight) {
            return;
        }
        KLog.e("进来了");
        mWeightUtils.initView(getApplicationContext(), tv_weight1, tv_weight2, tv_weight3, et_item8, getJldw());
        mWeightUtils.setJldwInfo(getJldw());
        mWeightUtils.setGetWeight(isGetWeight);
        mWeightUtils.setWeight(isWeight);
        mWeightUtils.setNew(isNew);
        mWeightUtils.setClear(isClear);


        if (isLandscape) {
            if (usbhandler == null) {
                usbhandler = new UsbHandler(this);
            }
            mWeightUtils.initUsbWeight(usbhandler);
        }

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {

            bluetoothPermissionHelper = new BluetoothPermissionHelper(this, new BluetoothPermissionHelper.Callback() {
                @Override
                public void onPermissionsGranted(int requestCode) {
                    // 权限已获取，可以执行蓝牙操作
                    mWeightUtils.initBltWeight();
                    // 例如：开始扫描、连接等
                }

                @Override
                public void onPermissionsDenied(int requestCode, List<String> deniedPermissions) {
                    // 权限被拒绝，可根据需求提示用户或降级处理
                    PermissionUtils.showSettingDialog(mContext,"蓝牙");
                    // 可以检查是否勾选“不再询问”，并引导用户去设置页面
                }
            });
            bluetoothPermissionHelper.requestPermissions(REQUEST_BLUETOOTH_PERMS);

//            PermissionUtils.requestBluetoothPermission(mContext, new PermissionUtils.OnBooleanListener() {
//                @Override
//                public void onSuccess() {
//                    mWeightUtils.initBltWeight();
//                }
//
//                @Override
//                public void onCancel() {
//
//                }
//            });
        }

    }

    protected void stopWeight() {
        if (isWeight) {
            mWeightUtils.stopWeight();
        }
    }

    private UsbHandler usbhandler;

    private class UsbHandler extends Handler {
        private final WeakReference<WeightBaseActivity> mWeakReference;

        public UsbHandler(WeightBaseActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            WeightBaseActivity mActivity = mWeakReference.get();
            if (mActivity == null || mActivity.isFinishing()) return;
            switch (msg.what) {
                case UsbWeighService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    KLog.e("NotReverseData " + data);
                    KLog.e("devicePID " + mWeightUtils.devicePID);
                    KLog.e("NEW_USB_PID " + UsbWeighService.NEW_USB_PID);
                    if (data.contains("WS")) {
                        mWeightUtils.setUsbWeightView(data);
                    } else {
//                        String reverseData = mWeightUtils.reverseString(data);
//                        mWeightUtils.setBltWeightView(reverseData);
                        mWeightUtils.setBltWeightView(data);
                    }

                    break;
                case UsbWeighService.CTS_CHANGE:
                    Toast.makeText(mActivity, "CTS_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                case UsbWeighService.DSR_CHANGE:
                    Toast.makeText(mActivity, "DSR_CHANGE", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        tv_weight2 = null;
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mWeightUtils.onDestroy();
        stopWeight();
        mWeightUtils = null;
    }

    /**
     * 将秤盘上的重量置为零重基准
     */
    protected byte[] orderZero() {
        return mWeightUtils.orderZero();
    }

    protected void hideSaveButtonByEv(LinearLayout ll_save) {
//        //湖南，山东，内蒙环境入库和出库数据不允许编辑
//        String environment = (String) SPUtils.get(mContext, Constants.ENVIRONMENT, "");
//        String[] dis_save_arr = new String[]{Constants.EV_HUNAN, Constants.EV_SHANDONG, Constants.EV_NEIMENG};
//        for (int i = 0; i < dis_save_arr.length; i++) {
//            if (environment.equals(dis_save_arr[i])) {
//                ll_save.setVisibility(View.GONE);
//            }
//        }
    }

    private static final int REQUEST_BLUETOOTH_PERMS = 100;
    private BluetoothPermissionHelper bluetoothPermissionHelper;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果交给 Helper 处理
        bluetoothPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
