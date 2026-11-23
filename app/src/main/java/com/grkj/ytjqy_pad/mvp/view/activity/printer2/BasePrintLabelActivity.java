package com.grkj.ytjqy_pad.mvp.view.activity.printer2;

import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_ATTACHED;
import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;
import static com.grkj.ytjqy_pad.mvp.view.activity.printer.Constant.ACTION_USB_PERMISSION;
import static com.grkj.ytjqy_pad.mvp.view.activity.printer.DeviceConnFactoryManager.ACTION_QUERY_PRINTER_STATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android_print_sdk.PrinterType;
import com.android_print_sdk.usb.USBPrinter;
import com.clj.fastble.BleManager;
import com.dascom.dc2300.DsCardPrintLib;
import com.dascom.print.PrintCommands.ESCPOS;
import com.dascom.print.PrintCommands.ZPL;
import com.dothantech.lpapi.LPAPI;
import com.dothantech.printer.IDzPrinter;
import com.gprinter.io.UsbPort;
import com.grkj.module_hardware.printer.LabelBitmapGenerator;
import com.grkj.module_hardware.printer.LabelInfoBeanV3;
import com.grkj.module_hardware.printer.NewPrintContentUtilsV3;
import com.grkj.module_hardware.printer.PrinterUtils;
import com.grkj.module_hardware.utils.UsbUtil;
import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.SimpleActivity;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.view.activity.printer.DeviceConnFactoryManager;
import com.grkj.ytjqy_pad.utils.common.BluetoothPermissionHelper;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.PermissionUtils;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.qrcode.QrCodeUtils;
import com.socks.library.KLog;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.TaskCallback;
import net.posprinter.service.PosprinterService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import zpCPCLSDK.zpCPCLSDK.zp_cpcl_BluetoothPrinter;

/*
 * 生成标签
 * */
public class BasePrintLabelActivity extends SimpleActivity {
    //废物名称
    @BindView(R.id.tv_item1)
    TextView tv_item1;

    //废物类别
    @BindView(R.id.tv_item2)
    TextView tv_item2;

    //废物代码
    @BindView(R.id.tv_item3)
    TextView tv_item3;

    //产生时间
    @BindView(R.id.tv_item4)
    TextView tv_item4;

    //入库量
    @BindView(R.id.tv_item5)
    TextView tv_item5;

    //废物形态
    @BindView(R.id.tv_item6)
    TextView tv_item6;

    //危险特性
    @BindView(R.id.tv_item7)
    TextView tv_item7;

    //主要成分
    @BindView(R.id.tv_item8)
    TextView tv_item8;

    //危害成分
    @BindView(R.id.tv_item9)
    TextView tv_item9;

    //注意事项
    @BindView(R.id.tv_item10)
    TextView tv_item10;

    //数字识别码pr
    @BindView(R.id.tv_item11)
    TextView tv_item11;

    //产生单位
    @BindView(R.id.tv_item12)
    TextView tv_item12;

    //地址
    @BindView(R.id.tv_item13)
    TextView tv_item13;

    //联系人
    @BindView(R.id.tv_item14)
    TextView tv_item14;

    //联系方式
    @BindView(R.id.tv_item15)
    TextView tv_item15;

    //备注
    @BindView(R.id.tv_item16)
    TextView tv_item16;


    //数字识别码二维码
    @BindView(R.id.iv_code)
    ImageView iv_code;


    //打印状态
    @BindView(R.id.tv_print_status)
    TextView tv_print_status;


    //打印
    @BindView(R.id.ll_print)
    LinearLayout ll_print;


    @BindView(R.id.ll_print_info)
    LinearLayout ll_print_info;

    //设备号
    @BindView(R.id.mVersion)
    TextView mVersion;


    private LabelInfoBeanV3 mData = new LabelInfoBeanV3();

    private zp_cpcl_BluetoothPrinter btPrinter;//蓝牙打印机

    private ZPL zplDkPrinter = null;
    private ESCPOS espDpPrinter = null;

    //得实打印机
    protected DsCardPrintLib mPrintLib = null;


    protected String SelectedMac = "";
    protected String SelectedName = "";
    private int allNum = 0;

    private DsPrinterConnAsyncTask mDsPrinterConnAsyncTask;
    private Dl2800PrinterConnAsyncTask mDl2800PrinterConnAsyncTask;
    private DsDk320PrintAsyncTask dk320PrintTask;
    private Dp12370PrintAsyncTask dp12370PrintTask;


    @Override
    protected int getLayout() {
        String str = getIntent().getStringExtra(Constants.JUMP_KEY);
        printDataList = GsonUtil.JsonToList(str, LabelInfoBeanV3.class);
        allNum = printDataList.size();
        return R.layout.activity_mul_print_info;
    }

    @Override
    protected void initEventAndData() {
        initCode();
        labelType = (int) SPUtils.get(mContext, "labelType", Constants.PRINT_TYPE_10);
        changeLabelType();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    /**
     * 连接蓝牙打印机
     */
    @SuppressLint("MissingPermission")
    protected void doConnectBt() {
        BluetoothManager bluetoothmanger = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothadapter = bluetoothmanger.getAdapter();
        KLog.e("bluetoothadapter" + GsonUtil.GsonToString(bluetoothadapter.getBondedDevices()));
        SelectedMac = "";
        SelectedName = "";

        Set<BluetoothDevice> bondedDevices = bluetoothadapter.getBondedDevices();
        final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (BluetoothDevice device : bondedDevices) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("DeviceName", device.getName());
            map.put("BDAddress", device.getAddress());
            map.put("BondState", device.getBondState() + "");
            list.add(map);
        }

        //检测蓝牙是否有已连接dp385记录
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                String deviceName = list.get(i).get("DeviceName");
                if (deviceName.contains(Constants.BLUETOOTH_DP385) || deviceName.contains(Constants.BLUETOOTH_DL)) {
                    SelectedMac = list.get(i).get("BDAddress");
                    SelectedName = list.get(i).get("DeviceName");
                } else if (deviceName.contains(Constants.BLUETOOTH_DP12370)) {
                    SelectedMac = list.get(i).get("BDAddress");
                    SelectedName = list.get(i).get("DeviceName");
                    connectDp12370();
                }
            }
        }

    }


    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        checkPermission();
        requestPermission();
        initBroadcast();
        checkUsb();
    }

    private static final int REQUEST_BLUETOOTH_PERMS = 100;
    private BluetoothPermissionHelper bluetoothPermissionHelper;
    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("打印页");

        bluetoothPermissionHelper = new BluetoothPermissionHelper(this, new BluetoothPermissionHelper.Callback() {
            @Override
            public void onPermissionsGranted(int requestCode) {
                // 权限已获取，可以执行蓝牙操作
                matchDevice();
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

    }

    private void initCode() {
        Bitmap qrCodeBitmap = QrCodeUtils.createQRImage(mData.QRCODE_VALUE_URL, 600, 600);
        iv_code.setImageBitmap(qrCodeBitmap);
    }

    private int labelType = Constants.PRINT_TYPE_10;

    private void changeLabelType() {
        ImageView iv_10x10 = findViewById(R.id.iv_10x10);
        ImageView iv_15x15 = findViewById(R.id.iv_15x15);
        ImageView iv_20x20 = findViewById(R.id.iv_20x20);

        iv_10x10.setImageResource(labelType == Constants.PRINT_TYPE_10 ? R.mipmap.ic_selected : R.mipmap.ic_unselected);
        iv_15x15.setImageResource(labelType == Constants.PRINT_TYPE_15 ? R.mipmap.ic_selected : R.mipmap.ic_unselected);
        iv_20x20.setImageResource(labelType == Constants.PRINT_TYPE_20 ? R.mipmap.ic_selected : R.mipmap.ic_unselected);
        SPUtils.put(mContext, "labelType", labelType);
    }


    private void handleQrcode() {
        if (!TextUtils.isEmpty(mData.QRCODE_URL)) {
            mData.QRCODE_VALUE_URL = mData.QRCODE_URL;
            if (mData.QRCODE_URL.contains("b=")) {
                mData.QRCODE_VALUE = mData.QRCODE_URL.split("b=")[1];
            }
        } else {
            mData.QRCODE_VALUE_URL = mData.QRCODE_VALUE;
        }
    }


    private List<LabelInfoBeanV3> printDataList = new ArrayList<>();

    private boolean isMul = false;

    protected void mulPrint(List<LabelInfoBeanV3> list) {
        printDataList = list;
        if (printDataList.size() > 0) {
            isMul = true;
            mData = printDataList.get(0);
            handleQrcode();
            KLog.e("PrinterVCode:" + PrinterVCode);
            KLog.e("PrinterVCode:" + PrinterPCode);
            if (PrinterVCode == usbVid4) {
                if (PrinterPCode == usbPid385) {
                    mDsPrinterConnAsyncTask = new DsPrinterConnAsyncTask();
                    mDsPrinterConnAsyncTask.execute("-1");
                } else {
                    dk320PrintTask = new DsDk320PrintAsyncTask();
                    dk320PrintTask.execute(mData);
                }
            } else if (!TextUtils.isEmpty(SelectedMac)) {
                if (SelectedName.contains(Constants.BLUETOOTH_DP385)) {
                    mDsPrinterConnAsyncTask = new DsPrinterConnAsyncTask();
                    mDsPrinterConnAsyncTask.execute(SelectedMac);
                } else if (SelectedName.contains(Constants.BLUETOOTH_DL)) {
                    mDl2800PrinterConnAsyncTask = new Dl2800PrinterConnAsyncTask();
                    mDl2800PrinterConnAsyncTask.execute(SelectedMac);
                } else if (SelectedName.contains(Constants.BLUETOOTH_DP12370)) {
//                    mDl2800PrinterConnAsyncTask = new Dl2800PrinterConnAsyncTask();
//                    mDl2800PrinterConnAsyncTask.execute(SelectedMac);

                    dp12370PrintTask = new Dp12370PrintAsyncTask();
                    dp12370PrintTask.execute();

                }
            }
        }

    }


    //打印德佟标签
    private String printDp12370Label() {
        boolean isPrintSuccess;
        int orientation = 180;
        Bitmap bmp = LabelBitmapGenerator.createLabelBitmap(mContext, mData, labelType);
        // 获取打印数据并进行打印
        if (bmp != null) {
            isPrintSuccess = printBitmap(bmp, getPrintParam(1, orientation));
        } else {
            ToastUtil.showToast(mContext, "打印失败！");
            isPrintSuccess = false;
        }
        return isPrintSuccess ? "正在打印请稍后..." : getString(R.string.str_conn_state_disconnect);
    }


    // 打印图片
    private boolean printBitmap(Bitmap bitmap, Bundle param) {

        if (dp12370Api == null) {
            connectDp12370();
        }

        // 打印
        return dp12370Api.printBitmap(bitmap, param);
    }

    private LPAPI dp12370Api;
    // 用于处理各种通知消息，刷新界面的handler
    private final Handler mHandler = new Handler();

    // 连接打印机成功时操作
    private void onPrinterConnected(IDzPrinter.PrinterAddress printer) {
        // 连接打印机成功时，刷新界面提示，保存相关信息
        hideLoading();
        ToastUtil.showToast(mContext, "连接打印机成功！");
    }

    // 连接打印机操作提交失败、打印机连接失败或连接断开时操作
    private void onPrinterDisconnected() {
        // 连接打印机操作提交失败、打印机连接失败或连接断开时，刷新界面提示
        hideLoading();
        ToastUtil.showToast(mContext, "连接打印机失败！");
    }

    // DP12370 打印机操作相关的回调函数。
    private final LPAPI.Callback mCallback = new LPAPI.Callback() {

        //****************************************************************************************************************************************
        // 所有回调函数都是在打印线程中被调用，因此如果需要刷新界面，需要发送消息给界面主线程，以避免互斥等繁琐操作。
        //****************************************************************************************************************************************

        // 打印机连接状态发生变化时被调用
        @Override
        public void onStateChange(IDzPrinter.PrinterAddress arg0, IDzPrinter.PrinterState arg1) {
            final IDzPrinter.PrinterAddress printer = arg0;
            switch (arg1) {
                case Connected:
                case Connected2:

                    // 打印机连接成功，发送通知，刷新界面提示
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onPrinterConnected(printer);
                        }
                    });
                    break;

                case Disconnected:
                    // 打印机连接失败、断开连接，发送通知，刷新界面提示
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onPrinterDisconnected();
                        }
                    });
                    break;

                default:
                    break;
            }
        }

        // 蓝牙适配器状态发生变化时被调用
        @Override
        public void onProgressInfo(IDzPrinter.ProgressInfo arg0, Object arg1) {
        }

        @Override
        public void onPrinterDiscovery(IDzPrinter.PrinterAddress printerAddress, Object o) {

        }

        // 打印标签的进度发生变化是被调用
        @Override
        public void onPrintProgress(IDzPrinter.PrinterAddress address, IDzPrinter.PrintData bitmapData, IDzPrinter.PrintProgress progress, Object addiInfo) {
            switch (progress) {
                case Success:
                    // 打印标签成功，发送通知，刷新界面提示
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            hideLoading();
                            ToastUtil.showToast(mContext, "打印成功！");
                        }
                    });
                    break;

                case Failed:
                    // 打印标签失败，发送通知，刷新界面提示
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            hideLoading();
                            ToastUtil.showToast(mContext, "打印失败！");
                        }
                    });
                    break;

                default:

                    break;
            }
        }
    };


    private void connectDp12370() {
        dp12370Api = LPAPI.Factory.createInstance(mCallback);
        List<IDzPrinter.PrinterAddress> pairedPrinters = dp12370Api.getAllPrinterAddresses(null);
        IDzPrinter.PrinterAddress printer = pairedPrinters.get(0);
        if (printer != null) {
            // 连接选择的打印机
            dp12370Api.openPrinterByAddress(printer);
        }
    }

    // 获取打印时需要的打印参数，不设置就默认跟随打印机设置
    private Bundle getPrintParam(int copies, int orientation) {
        Bundle param = new Bundle();

        // 打印页面旋转角度
        if (orientation != 0) {
            param.putInt(IDzPrinter.PrintParamName.PRINT_DIRECTION, orientation);
        }

        // 打印份数
        if (copies > 1) {
            param.putInt(IDzPrinter.PrintParamName.PRINT_COPIES, copies);
        }

        return param;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @OnClick({R.id.ll_print, R.id.ll_back, R.id.ll_10x10, R.id.ll_15x15, R.id.ll_20x20})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.ll_10x10:
                labelType = Constants.PRINT_TYPE_10;
                changeLabelType();
                break;

            case R.id.ll_15x15:
                labelType = Constants.PRINT_TYPE_15;
                changeLabelType();
                break;

            case R.id.ll_20x20:
                labelType = Constants.PRINT_TYPE_20;
                changeLabelType();
                break;

            //返回
            case R.id.ll_back:
                finish();
                break;

            // 打印
            case R.id.ll_print:
                if (!TextUtils.isEmpty(SelectedMac) || PrinterVCode == usbVid4) {
                    mulPrint(printDataList);
                    ll_print.setVisibility(View.INVISIBLE);
                } else {
                    ToastUtil.showToast(mContext, "打印机未连接");
                }

                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (109 == requestCode) {
            if (resultCode == RESULT_CANCELED) {
            } else {
                doConnectBt();
            }
        }
    }


    //得实异步连接蓝牙设备并打印
    private class DsPrinterConnAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String SelectedMac = params[0];
            ESCPOS smartPrint = null;
            if (TextUtils.equals(SelectedMac, "-1")) {//usb 385
                smartPrint = PrinterUtils.getInstance().initDpUsbPrintLib(mContext);
            } else {//蓝牙 385
                smartPrint = PrinterUtils.getInstance().initBtlPrintLib(mContext, SelectedMac);
            }

            if (smartPrint != null) {
                return NewPrintContentUtilsV3.dp385PrintBitmap(mContext, smartPrint, mData, labelType) ? "正在打印请稍后..." : getString(R.string.str_conn_state_disconnect);
            } else {
                PrinterUtils.getInstance().closeBltPrinter();
                return getString(R.string.str_conn_state_disconnect);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            KLog.e(result);

            if (!TextUtils.isEmpty(result)) {
                KLog.e(result);
                ToastUtil.showToast(mContext, result);

                if (!getString(R.string.str_conn_state_disconnect).equals(result)) {
                    printAndNext();
                } else {
                    SPUtils.put(mContext, Constants.PRINTER_MAC, "");
                    SPUtils.put(mContext, Constants.PRINTER_NAME, "");
                }

            }

        }
    }

    private ZPL zpl = null;

    //得实dl2800异步连接蓝牙设备并打印
    private class Dl2800PrinterConnAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String SelectedMac = params[0];
            if (zpl == null) {
                zpl = PrinterUtils.getInstance().initZplBtlPrintLib(mContext, SelectedMac);
            }

            if (zpl != null) {
                return NewPrintContentUtilsV3.dlndkPrinter(mContext, zpl, mData, labelType, false) ? "正在打印请稍后..." : getString(R.string.str_conn_state_disconnect);
            } else {
                zpl = null;
                PrinterUtils.getInstance().closeBltPrinter();
                return getString(R.string.str_conn_state_disconnect);
            }

        }

        @Override
        protected void onPostExecute(String result) {
            KLog.e(result);
            hideLoading();
            if (!TextUtils.isEmpty(result)) {
                KLog.e(result);
                ToastUtil.showToast(mContext, result);
                if (!getString(R.string.str_conn_state_disconnect).equals(result)) {
                    printAndNext();
                } else {
                    SPUtils.put(mContext, Constants.PRINTER_MAC, "");
                    SPUtils.put(mContext, Constants.PRINTER_NAME, "");
                }
            }
        }

    }


    //得实dk320打印任务
    private class DsDk320PrintAsyncTask extends AsyncTask<LabelInfoBeanV3, Void, String> {

        @Override
        protected String doInBackground(LabelInfoBeanV3... params) {
            LabelInfoBeanV3 currentData = params[0];
            if (zplDkPrinter != null) {
                return NewPrintContentUtilsV3.dlndkPrinter(mContext, zplDkPrinter, currentData, labelType, true) ? "正在打印请稍后..." : getString(R.string.str_conn_state_disconnect);
            } else {
                zplDkPrinter = PrinterUtils.getInstance().initDkUsbPrintLib(mContext);
                return getString(R.string.str_conn_state_disconnect);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            KLog.e(result);

            if (!TextUtils.isEmpty(result)) {
                ToastUtil.showToast(mContext, result);

                if (!getString(R.string.str_conn_state_disconnect).equals(result)) {
                    printAndNext();
                }

            }

        }
    }


    //得实dl2800异步连接蓝牙设备并打印
    private class Dp12370PrintAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return printDp12370Label();
        }

        @Override
        protected void onPostExecute(String result) {
            KLog.e(result);
            hideLoading();
            if (!TextUtils.isEmpty(result)) {
                KLog.e(result);
                ToastUtil.showToast(mContext, result);
                if (!getString(R.string.str_conn_state_disconnect).equals(result)) {
                    printAndNext();
                } else {
                    SPUtils.put(mContext, Constants.PRINTER_MAC, "");
                    SPUtils.put(mContext, Constants.PRINTER_NAME, "");
                }
            }
        }

    }


    /**
     * 打印等待处理，并打印下一章
     */
    private void printAndNext() {
        if (isMul) {
            if (printDataList.size() > 0 && (dk320PrintTask != null || mDsPrinterConnAsyncTask != null) || mDl2800PrinterConnAsyncTask != null || dp12370PrintTask!=null) {

                printDataList.remove(0);

                if (printDataList.size() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (ll_print_info != null && ll_print != null) {
                                        ll_print_info.setVisibility(View.INVISIBLE);
                                        ll_print.setVisibility(View.VISIBLE);
                                        isMul = false;
                                        ToastUtil.showToast(mContext, "打印完成！");
                                        finish();
                                    }

                                }
                            });
                        }
                    }, 5000);
                } else {
                    if (ll_print_info != null && tv_print_status != null) {
                        ll_print_info.setVisibility(View.VISIBLE);
                        tv_print_status.setText(getString(R.string.print_status_str, allNum, allNum - printDataList.size(), printDataList.size()));
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mulPrint(printDataList);
                        }
                    }, 5000);
                }

            }
        }
    }


    private static final String TAG = BasePrintLabelActivity.class.getSimpleName();
    ArrayList<String> per = new ArrayList<>();
    protected UsbManager usbManager;
    private static final int REQUEST_CODE = 0x004;

    public USBPrinter mPrinter;
    private UsbDevice mUsbDevice;
    public static boolean isConnected;

    private PendingIntent mPermissionIntent;
    private String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH
    };


    private static final int usbVid1 = 1137;//第一款
    private static final int usbVid2 = 1155;//第二款
    private static final int usbVid3 = 11575;//第三款
    private static final int usbVid4 = 10343;//第四款

    private static final int usbPid320 = 5413;//得实dk320
    private static final int usbPid385 = 5442;//得实dp385


    /**
     * 判断打印机所使用指令是否是ESC指令
     */
    private int id = 0;

    /**
     * 注册广播
     * Registration broadcast
     */
    protected void initBroadcast() {
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);//USB访问权限广播
        filter.addAction(ACTION_USB_DEVICE_DETACHED);//USB线拔出
        filter.addAction(ACTION_QUERY_PRINTER_STATE);//查询打印机缓冲区状态广播，用于一票一控
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);//与打印机连接状态
        filter.addAction(ACTION_USB_DEVICE_ATTACHED);//USB线插入
        registerReceiver(mUsbReceiver, filter);
    }


    private void checkPermission() {
        for (String permission : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, permission)) {
                per.add(permission);
            }
        }
    }

    private void requestPermission() {
        if (per.size() > 0) {
            String[] p = new String[per.size()];
            ActivityCompat.requestPermissions(this, per.toArray(p), REQUEST_CODE);
        }
    }

    private int PrinterVCode = -1;
    private int PrinterPCode = -1;

    protected void checkUsb() {

        List<UsbDevice> usbDeviceList = UsbUtil.getPrinterUsbDeviceList(mContext);
        if (usbDeviceList != null && !usbDeviceList.isEmpty()) {
            mUsbDevice = usbDeviceList.get(0);
            PrinterVCode = mUsbDevice.getVendorId();
            PrinterPCode = mUsbDevice.getProductId();
            //判断USB设备是否有权限
            if (usbManager != null && usbManager.hasPermission(mUsbDevice)) {
                usbConn(mUsbDevice);
            } else {//请求权限
                mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                usbManager.requestPermission(mUsbDevice, mPermissionIntent);
            }
        }

    }


    public UsbPort mOldPrinter;


    /**
     * usb连接
     *
     * @param device
     */
    private void usbConn(UsbDevice device) {

        switch (PrinterVCode) {
            case usbVid1:
                mOldPrinter = new UsbPort(mContext, device);
                isConnected = mOldPrinter.openPort();
                if (isConnected) {
                    ToastUtil.showToast(mContext, "USB" + getString(R.string.str_conn_state_connected));
                }
                break;

            case usbVid2:

                mUsbDevice = device;
                mPrinter = new USBPrinter(mContext);
                mPrinter.setCurrentPrintType(PrinterType.Printer_104);
                mPrinter.setEncoding("GBK");
                isConnected = mPrinter.openConnection(device);

                if (isConnected) {
                    ToastUtil.showToast(mContext, "USB" + getString(R.string.str_conn_state_connected));
                }
                break;

            case usbVid4:
                mUsbDevice = device;
                if (mUsbDevice.getProductId() == usbPid385) {
                    espDpPrinter = PrinterUtils.getInstance().initDpUsbPrintLib(mContext);
                    if (espDpPrinter != null) {
                        ToastUtil.showToast(mContext, "DP-385" + getString(R.string.str_conn_state_connected));
                    } else {
                        ToastUtil.showToast(mContext, "DP-385" + getString(R.string.str_conn_state_disconnect));
                    }
                } else if (mUsbDevice.getProductId() == usbPid320) {
                    zplDkPrinter = PrinterUtils.getInstance().initDkUsbPrintLib(mContext);
                    if (zplDkPrinter != null) {
                        ToastUtil.showToast(mContext, "DK-320" + getString(R.string.str_conn_state_connected));
                    } else {
                        ToastUtil.showToast(mContext, "DK-320" + getString(R.string.str_conn_state_disconnect));
                    }
                }
                break;

            case usbVid3:
            default:
                Intent intent = new Intent(this, PosprinterService.class);
                bindService(intent, conn, BIND_AUTO_CREATE);
                break;
        }

    }


    public void closeConnection() {
        if (mPrinter != null) {
            mPrinter.closeConnection();
            mPrinter = null;
        }

        if (mOldPrinter != null) {
            mOldPrinter.closePort();
            mOldPrinter = null;
        }

        if (mUsbDevice != null) {
            mUsbDevice = null;
        }
        isConnected = false;
    }


    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG, action);
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        Log.e(TAG, "获取权限成功：" + mUsbDevice.getDeviceName());
                        if (!isConnected) {
                            usbConn(mUsbDevice);
                        }
                    } else {
                        Log.d(TAG, "permission denied for device " + mUsbDevice);
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                checkUsb();
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                closeConnection();
                KLog.e(getString(R.string.str_conn_state_disconnect));
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");

        if (isLandscape) {
            if (mUsbReceiver != null) {
                KLog.e("unregisterReceiver mUsbReceiver");
                unregisterReceiver(mUsbReceiver);
            }

            if (usbManager != null) {
                usbManager = null;
                DeviceConnFactoryManager.closeAllPort();
                closeConnection();
            }

            if (binder != null) {
                binder.DisconnectCurrentPort(new TaskCallback() {
                    @Override
                    public void OnSucceed() {

                    }

                    @Override
                    public void OnFailed() {

                    }
                });
                unbindService(conn);
            }

            if (mPrintLib != null) {
                if (mPrintLib.isConnected()) {
                    mPrintLib.disconnection();
                }
                mPrintLib = null;
            }

            if (mDsPrinterConnAsyncTask != null) {
                mDsPrinterConnAsyncTask.cancel(true);
                mDsPrinterConnAsyncTask = null;
            }

            if (mDl2800PrinterConnAsyncTask != null) {
                mDl2800PrinterConnAsyncTask.cancel(true);
                mDl2800PrinterConnAsyncTask = null;
            }

            if (dk320PrintTask != null) {
                dk320PrintTask.cancel(true);
                dk320PrintTask = null;
            }

            if (dp12370PrintTask != null) {
                dp12370PrintTask.cancel(true);
                dp12370PrintTask = null;
            }


        } else {
//            if (btPrinter != null) {
//                btPrinter = null;
//            }
        }


        PrinterUtils.getInstance().closeDpUsbPrinter();
        PrinterUtils.getInstance().closeDkUsbPrinter();
        BleManager.getInstance().disconnectAllDevice();


    }


    public static IMyBinder binder;//IMyBinder接口，所有可供调用的连接和发送数据的方法都封装在这个接口内
    //bindService的参数conn
    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            //绑定成功
            binder = (IMyBinder) service;

            binder.ConnectUsbPort(getApplicationContext(), mUsbDevice.getDeviceName(), new TaskCallback() {
                @Override
                public void OnSucceed() {
                    isConnected = true;
                    Toast.makeText(getApplicationContext(), "USB" + getString(R.string.str_conn_state_connected), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnFailed() {
                    isConnected = false;
                    KLog.e(getString(R.string.str_conn_state_disconnect));
                }
            });
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果交给 Helper 处理
        bluetoothPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 根据蓝牙名称和mac匹配对应设备
     * */
    private void matchDevice() {
        String DeviceName = (String) SPUtils.get(mContext, Constants.PRINTER_NAME, "");
        String DeviceMac = (String) SPUtils.get(mContext, Constants.PRINTER_MAC, "");

        if (TextUtils.isEmpty(DeviceName)) {
            doConnectBt();
        } else {
            if (DeviceName.contains(Constants.BLUETOOTH_DP385) || DeviceName.contains(Constants.BLUETOOTH_DL)) {
                SelectedMac = DeviceMac;
                SelectedName = DeviceName;
            } else if (DeviceName.contains(Constants.BLUETOOTH_DP12370)) {
                SelectedMac = DeviceMac;
                SelectedName = DeviceName;
                connectDp12370();
            }
        }
    }


}
