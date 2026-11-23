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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android_print_sdk.PrinterType;
import com.android_print_sdk.usb.USBPrinter;
import com.clj.fastble.BleManager;
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
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.presenter.PrintLabelPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.printer.DeviceConnFactoryManager;
import com.grkj.ytjqy_pad.mvp.view.activity.printer.SkLabelBean;
import com.grkj.ytjqy_pad.utils.common.BluetoothPermissionHelper;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.PermissionUtils;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.qrcode.QrCodeUtils;
import com.jxprint.BTPrinterManager;
import com.jxprint.Global;
import com.jxprint.bean.PrintDataBean;
import com.jxprint.config.PrintConfig;
import com.jxprint.converter.AtkinsonBitmapConverter;
import com.jxprint.enumdata.PageModeEnum;
import com.jxprint.enumdata.PageSizeEnum;
import com.jxprint.enumdata.PrintingStateEnum;
import com.jxprint.pos.Pos;
import com.socks.library.KLog;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.TaskCallback;
import net.posprinter.service.PosprinterService;

import java.util.ArrayList;
import java.util.Date;
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
public class NewPrintLabelActivity extends BaseActivity<PrintLabelPresenter> implements PrintLabelPresenter.View {
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

    //数字识别码
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

    //标签规格选择
    @BindView(R.id.ll_bqgg)
    LinearLayout ll_bqgg;


    //数字识别码二维码
    @BindView(R.id.iv_code)
    ImageView iv_code;

    //tsc打印
    @BindView(R.id.ll_tsc_print)
    LinearLayout ll_tsc_print;

    private LabelInfoBeanV3 mData = new LabelInfoBeanV3();
    private zp_cpcl_BluetoothPrinter btPrinter;//蓝牙打印机

    private ZPL zplDkPrinter = null;
    private ZPL zplDlPrinter = null;
    private ESCPOS espDpPrinter = null;

    private String dp385Mac = "";
    private String dp12370Mac = "";
    private String cc4Mac = "";
    private String dl2800Mac = "";
    private String jxPrinterMac = "";
    private LPAPI dp12370Api;

    @Override
    protected int getLayout() {
        String str = getIntent().getStringExtra(Constants.JUMP_KEY);
        mData = GsonUtil.GsonToBean(str, LabelInfoBeanV3.class);

        if (!TextUtils.isEmpty(mData.QRCODE_URL)) {
            mData.QRCODE_VALUE_URL = mData.QRCODE_URL;
            if (mData.QRCODE_URL.contains("b=")) {
                mData.QRCODE_VALUE = mData.QRCODE_URL.split("b=")[1];
            }
        } else {
            mData.QRCODE_VALUE_URL = mData.QRCODE_VALUE;
        }

        return R.layout.activity_new_print_label;
    }

    @Override
    protected void initEventAndData() {
        initCode();
        labelType = (int) SPUtils.get(mContext, "labelType", Constants.PRINT_TYPE_10);
        changeLabelType();
        initNewLabelData();
        mPresenter.getButtonList();


//        PermissionUtils.requestBluetoothPermission(this, new PermissionUtils.OnBooleanListener() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    /**
     * 连接蓝牙
     */
    @SuppressLint("MissingPermission")
    private void doConnectBt() {
        BluetoothManager bluetoothmanger = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothadapter = bluetoothmanger.getAdapter();
        cc4Mac = "";
        Set<BluetoothDevice> bondedDevices = bluetoothadapter.getBondedDevices();
        final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (BluetoothDevice device : bondedDevices) {
            Map<String, String> map = new HashMap<String, String>();
            KLog.e("device" + device.getName());
            if (Constants.checkBluetoothInList(device)) {
                KLog.e("DeviceName:" + device.getName());
                map.put("DeviceName", device.getName());
                map.put("BDAddress", device.getAddress());
                map.put("BondState", device.getBondState() + "");
                list.add(map);
            }

            if (device.getName().contains("JXPRINTER")) {
                BTPrinterManager.getInstance().connectBluetoothDevice(device);
            }
        }

        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).get("DeviceName").contains(Constants.BLUETOOTH_CC4)) {
                    cc4Mac = list.get(i).get("BDAddress");
                }
                if (list.get(i).get("DeviceName").contains(Constants.BLUETOOTH_DP385)) {
                    dp385Mac = list.get(i).get("BDAddress");
                    ll_bqgg.setVisibility(View.VISIBLE);
                }
                if (list.get(i).get("DeviceName").contains(Constants.BLUETOOTH_DP12370)) {
                    dp12370Mac = list.get(i).get("BDAddress");
                    ll_bqgg.setVisibility(View.VISIBLE);
                }
                if (list.get(i).get("DeviceName").contains(Constants.BLUETOOTH_DL)) {
                    dl2800Mac = list.get(i).get("BDAddress");
                    ll_bqgg.setVisibility(View.VISIBLE);
                }

                if (list.get(i).get("DeviceName").contains(Constants.BLUETOOTH_JX_PRINTER)) {
                    jxPrinterMac = list.get(i).get("BDAddress");
                    ll_bqgg.setVisibility(View.GONE);
                }
            }
            if (list.size() > 1) {
                btnDevice_Click();
            }
        }

        if (ll_bqgg.getVisibility() == View.GONE) {
            labelType = Constants.PRINT_TYPE_10;
            changeLabelType();
        }

    }


    //选择蓝牙秤设备
    protected void btnDevice_Click() {
        // TODO: 2023/12/5
//        final BltSelectDialog _deviceDialog = new BltSelectDialog(mContext);
//        _deviceDialog.onConfirmListener = new BltSelectDialog.OnConfirmListener() {
//            @Override
//            public void confirm(BluetoothDevice device) {
//                if (device.getName().contains("CC4")) {
//                    SelectedBDAddress = device.getAddress();
//                } else if (device.getName().contains("DP-385")) {
//                    SelectedMac = device.getAddress();
//                    ToastUtil.showToast(mContext,"正在连接打印机...");
//                    showLoading();
//                    DsPrinterEnableAsyncTask task = new DsPrinterEnableAsyncTask();
//                    task.execute(SelectedMac);
//                }
//                _deviceDialog.dismiss();
//            }
//        };
//        _deviceDialog.show();
    }


    //初始化新款标签数据
    private void initNewLabelData() {
        tv_item1.setText(mData.FWXXMC);
        tv_item2.setText(mData.FWDLBH + "-" + mData.FWDLMC);
        tv_item3.setText(mData.FWXLBH);
        tv_item4.setText(mData.RKSJ);
        tv_item5.setText(mData.RKL + mData.JLDW);
        tv_item6.setText(mData.WLXZ);
        tv_item8.setText(mData.ZYWRCF);
        tv_item9.setText(mData.WXCF);
        tv_item10.setText(mData.ZYSX);
        tv_item11.setText(mData.QRCODE_VALUE);
        tv_item12.setText(mData.ENTERNAME);
        tv_item14.setText(mData.LXRNAME + "/" + mData.LXRPHONE);
        tv_item16.setText(mData.REMARK);

        //毒性
        CheckBox cb_dx = findViewById(R.id.cb_dx);

        //反应性
        CheckBox cb_fyx = findViewById(R.id.cb_fyx);

        //易燃性
        CheckBox cb_yrx = findViewById(R.id.cb_yrx);

        //腐蚀性
        CheckBox cb_fsx = findViewById(R.id.cb_fsx);


        if (!TextUtils.isEmpty(mData.FWTX)) {
            String[] fwtxArr = mData.FWTX.split(",");

            for (int i = 0; i < fwtxArr.length; i++) {
                switch (fwtxArr[i]) {
                    case "毒性":
                        cb_dx.setBackgroundResource(R.mipmap.ic_checked_black);
                        mData.WXCF_DX = "1";
                        break;
                    case "反应性":
                        cb_fyx.setBackgroundResource(R.mipmap.ic_checked_black);
                        mData.WXCF_FYX = "1";
                        break;
                    case "易燃性":
                        cb_yrx.setBackgroundResource(R.mipmap.ic_checked_black);
                        mData.WXCF_YRX = "1";
                        break;
                    case "腐蚀性":
                        cb_fsx.setBackgroundResource(R.mipmap.ic_checked_black);
                        mData.WXCF_FSX = "1";
                        break;
                }
            }
        }

        //生成溯源二维码
        String ip = SPUtils.get(mContext, "IP", "").toString();
        String port = SPUtils.get(mContext, "Port", "").toString();
        String contractName = SPUtils.get(mContext, "CONTRACT_NAME", "http://").toString();
        String historyStr = contractName + ip + ":" + (Integer.valueOf(port) + 1) + "/web-waste/traceSrouce/traceSource.html?qrcodeValue=" + mData.QRCODE_VALUE;
        Bitmap qrCodeBitmap2 = QrCodeUtils.createQRImage(historyStr, 600, 600);
        mData.HISTORY_CODE = historyStr;
        ImageView iv_history_code = findViewById(R.id.iv_history_code);
        iv_history_code.setImageBitmap(qrCodeBitmap2);
    }


    public SkLabelBean printSkLabel(LabelInfoBeanV3 mData) {
        SkLabelBean labelBean = new SkLabelBean();
        labelBean.hwName = mData.FWXXMC;
        labelBean.hwTypeLabel = mData.FWDLBH + "-" + mData.FWDLMC;
        labelBean.hwCode = mData.FWXLBH;
        labelBean.physicalState = mData.WLXZ;
        labelBean.mainComponents = mData.ZYWRCF;
        labelBean.harmfulComponents = mData.WXCF;
        labelBean.notes = mData.ZYSX;
        labelBean.hwUuid = mData.QRCODE_VALUE;
        labelBean.operatorCompany = mData.ENTERNAME;
        labelBean.operatorPhone = mData.LXRNAME + "/" + mData.LXRPHONE;

        // 产生日期
        if (!TextUtils.isEmpty(mData.RKSJ)) {
            String date = mData.RKSJ.substring(0, 11);
            labelBean.genDate = date;
        }

        labelBean.productionQuantityLabel = mData.RKL + mData.JLDW;
        labelBean.printRemark = mData.REMARK;
        labelBean.qrCode = mData.QRCODE_VALUE_URL;

        if (!TextUtils.isEmpty(mData.FWTX)) {
            String[] fwtxArr = mData.FWTX.split(",");
            for (int i = 0; i < fwtxArr.length; i++) {
                switch (fwtxArr[i]) {
                    case "毒性":
                        labelBean.flagDx = Constants.CHECK_URL;
                        break;
                    case "反应性":
                        labelBean.flagFyx = Constants.CHECK_URL;
                        break;
                    case "易燃性":
                        labelBean.flagYrx = Constants.CHECK_URL;
                        break;
                    case "腐蚀性":
                        labelBean.flagFsx = Constants.CHECK_URL;
                        break;
                }
            }
        }

        return labelBean;
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
        initTopToolbar("标签信息");

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

//        PermissionUtils.requestBluetoothPermissions(this, REQUEST_BLUETOOTH_PERMISSIONS);
//        PermissionUtils.setOnBooleanListener(new PermissionUtils.OnBooleanListener() {
//            @Override
//            public void onSuccess() {
//                matchDevice();
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//        });
    }

    //生成二维码
    private void initCode() {
        Bitmap qrCodeBitmap = QrCodeUtils.createQRImage(mData.QRCODE_VALUE_URL, 600, 600);
        iv_code.setImageBitmap(qrCodeBitmap);
    }


    private int labelType = Constants.PRINT_TYPE_20;
    private boolean isPrintable = true;

    //切换标签规格
    private void changeLabelType() {
        ImageView iv_10x10 = findViewById(R.id.iv_10x10);
        ImageView iv_15x15 = findViewById(R.id.iv_15x15);
        ImageView iv_20x20 = findViewById(R.id.iv_20x20);

        iv_10x10.setImageResource(labelType == Constants.PRINT_TYPE_10 ? R.mipmap.ic_selected : R.mipmap.ic_unselected);
        iv_15x15.setImageResource(labelType == Constants.PRINT_TYPE_15 ? R.mipmap.ic_selected : R.mipmap.ic_unselected);
        iv_20x20.setImageResource(labelType == Constants.PRINT_TYPE_20 ? R.mipmap.ic_selected : R.mipmap.ic_unselected);
        SPUtils.put(mContext, "labelType", labelType);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @OnClick({R.id.ll_print, R.id.ll_back, R.id.ll_10x10, R.id.ll_15x15, R.id.ll_20x20, R.id.ll_tsc_print})
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

            // tsc打印
            case R.id.ll_tsc_print:
                SkLabelBean labelBean = printSkLabel(mData);
                Map<String, String> params = GsonUtil.GsonToMaps(GsonUtil.GsonToString(labelBean));
                mPresenter.printLabel(params);
//                skConfigAndPrint();
                break;

            // 打印
            case R.id.ll_print:

                if (!isPrintable) {
                    ToastUtil.showToast(mContext, "请勿频繁点击！");
                    return;
                }
                isPrintable = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isPrintable = true;
                    }
                }, 3000);

                if (!TextUtils.isEmpty(jxPrinterMac)) { //河南打印机已连接
                    labelType = Constants.PRINT_TYPE_20;
                    printJxPic();
                    return;
                }

                if (labelType == Constants.PRINT_TYPE_10) {
                    if (mUsbDevice != null && isLandscape) {//usb已连接d
                        printUsbLabel();
                    } else if (!TextUtils.isEmpty(cc4Mac)) { //cc4已连接
                        printCC4Label();
                    } else if (!TextUtils.isEmpty(dp385Mac)) { //得实已连接
                        printDpLabel();
                    } else if (!TextUtils.isEmpty(dp12370Mac)) { //德佟已连接
                        printDp12370Label();
                    } else if (!TextUtils.isEmpty(dl2800Mac)) { //得实dl2800已连接
                        printDlLabel();
                    } else {
                        ToastUtil.showToast(mContext, getString(R.string.str_conn_state_disconnect));
                    }
                } else {

                    if (usbVid4 == PrinterVCode) {
                        printUsbLabel();
                    } else {
                        showLoading();
                        if (!TextUtils.isEmpty(dp385Mac)) { //得实已连接
                            printDpLabel();
                        }
                        if (!TextUtils.isEmpty(dp12370Mac)) { //德佟已连接
                            printDp12370Label();
                        } else if (!TextUtils.isEmpty(dl2800Mac)) { //得实dl2800已连接
                            printDlLabel();
                        }
                    }

                }

                break;
        }
    }

    private void setDefaultMac(String mac) {
        SPUtils.put(mContext, "BLT_MAC", mac);
    }


    //打印得实标签
    private void printDpLabel() {
        if (!TextUtils.isEmpty(dp385Mac)) {
            DsPrinterConnAsyncTask task = new DsPrinterConnAsyncTask();
            task.execute(dp385Mac);

        }
    }

    //打印得实标签
    private void printDlLabel() {
        if (!TextUtils.isEmpty(dl2800Mac)) {
            Dl2800PrinterConnAsyncTask task = new Dl2800PrinterConnAsyncTask();
            task.execute(dl2800Mac);
        }
    }

    //打印德佟标签
    private void printDp12370Label() {
        int orientation = 0;
        Bitmap bmp = LabelBitmapGenerator.createLabelBitmap(mContext, mData, labelType);
        // 获取打印数据并进行打印
        if (bmp != null) {
            if (printBitmap(bmp, getPrintParam(1, orientation))) {
                return;
            }
        }
        hideLoading();
        ToastUtil.showToast(NewPrintLabelActivity.this, "打印失败！");
    }


    private BtPrinterConnAsyncTask cc4Task;

    //打印芝科标签
    private void printCC4Label() {
        isFirstTask = true;
        showLoading();
        cc4Task = new BtPrinterConnAsyncTask();
        cc4Task.execute(cc4Mac);

        //如果同时拥有得实打印机，就设置超时打印超时任务
        if (!TextUtils.isEmpty(dp385Mac)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (cc4Task.getStatus() == AsyncTask.Status.RUNNING) {
                        cc4Task.cancel(true);
                        printDpLabel();
                    }
                }
            }, 5000);
        }
    }

    //打印USB标签
    private void printUsbLabel() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                switch (PrinterVCode) {

                    //一代
                    case usbVid1:
                        try {
                            mOldPrinter.writeDataImmediately(NewPrintContentUtilsV3.addLabel(mData), 0, NewPrintContentUtilsV3.addLabel(mData).size());

                        } catch (Exception e) {//异常中断发送

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideLoading();
                                    ToastUtil.showToast(mContext, "打印失败");
                                }
                            });
                        }
                        break;

                    //三代
                    case usbVid3:
                        binder.WriteSendData(new TaskCallback() {
                            @Override
                            public void OnSucceed() {
                                Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void OnFailed() {
                                Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_SHORT).show();

                            }
                        }, new ProcessData() {
                            @Override
                            public List<byte[]> processDataBeforeSend() {
                                return NewPrintContentUtilsV3.printLabel3(mData);
                            }
                        });
                        break;


                    //四代
                    case usbVid4:

                        if (PrinterPCode == usbPid320) {
                            if (zplDkPrinter != null) {
                                showLoading();
                                new Thread(() -> {
                                    boolean isSuccess = NewPrintContentUtilsV3.dlndkPrinter(mContext, zplDkPrinter, mData, labelType, true);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (isSuccess) {
                                                ToastUtil.showToast(mContext, "正在打印请稍后...");
                                            } else {
                                                ToastUtil.showToast(mContext, getString(R.string.str_conn_state_disconnect));
                                            }
                                            hideLoading();
                                        }
                                    });


                                }).start();
                            }
                        } else {
                            if (espDpPrinter != null) {
                                showLoading();
                                new Thread(() -> {
                                    boolean isSuccess = NewPrintContentUtilsV3.dp385PrintBitmap(mContext, espDpPrinter, mData, labelType);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (isSuccess) {
                                                ToastUtil.showToast(mContext, "正在打印请稍后...");
                                            } else {
                                                ToastUtil.showToast(mContext, getString(R.string.str_conn_state_disconnect));
                                            }
                                            hideLoading();
                                        }
                                    });


                                }).start();
                            }
                        }

                        break;


                    //二代
                    case usbVid2:
                    default:
                        NewPrintContentUtilsV3.printLabel(mPrinter, mData);
                        break;
                }
            }
        });
    }


    private boolean isConnectBltPrinter = false;
    private long clickTime;
    private boolean isFirstTask = false;

    @Override
    public void showError(String msg) {
        ToastUtil.showToast(mContext, msg);
    }

    @Override
    public void printLabelSuccess(String bean) {
        ToastUtil.showToast(mContext, "正在打印请稍后...");
    }

    @Override
    public void isShowTsc(boolean isShowTsc) {
        if (isShowTsc) {
            ll_tsc_print.setVisibility(View.VISIBLE);
        } else {
            ll_tsc_print.setVisibility(View.GONE);
        }
    }

    //异步连接蓝牙设备并打印
    private class BtPrinterConnAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String SelectedBDAddressb = params[0];
            if (btPrinter == null) {
                btPrinter = new zp_cpcl_BluetoothPrinter(mContext);
            }
            if (isFirstTask) {
                clickTime = System.currentTimeMillis();
            }

            if (isConnectBltPrinter) {
                return true;
            } else {
                isConnectBltPrinter = btPrinter.connect(SelectedBDAddressb);
                return isConnectBltPrinter;
            }

        }

        @Override
        protected void onPostExecute(Boolean result) {
            KLog.e(result);
            //关闭定时任务
            if (result) {
                long time = System.currentTimeMillis() - clickTime;
                KLog.e("打印用时" + time);
                hideLoading();
                isConnectBltPrinter = true;
                NewPrintContentUtilsV3.btPrint(btPrinter, mData);
            } else {
                if (System.currentTimeMillis() - clickTime < 20000) {
                    isFirstTask = false;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cc4Task = new BtPrinterConnAsyncTask();
                            cc4Task.execute(cc4Mac);
                        }
                    }, 1000);


                } else {
                    if (!TextUtils.isEmpty(dp385Mac)) {
                        DsPrinterConnAsyncTask task = new DsPrinterConnAsyncTask();
                        task.execute(dp385Mac);
                    } else {
                        hideLoading();
                        ToastUtil.showToast(mContext, getString(R.string.str_conn_state_disconnect));
                    }
                }
            }
        }
    }

    private ESCPOS escpos = null;
    private ZPL zpl = null;

    private long currentTime;

    //得实异步连接蓝牙设备并打印
    private class DsPrinterConnAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            currentTime = System.currentTimeMillis();
            if (escpos == null) {
                escpos = PrinterUtils.getInstance().initBtlPrintLib(mContext, dp385Mac);
            }
            KLog.e("smartPrint:" + GsonUtil.GsonToString(dp385Mac));
            if (escpos != null) {
                return NewPrintContentUtilsV3.dp385PrintBitmap(mContext, escpos, mData, labelType) ? "正在打印请稍后..." : getString(R.string.str_conn_state_disconnect);
            } else {
                PrinterUtils.getInstance().closeBltPrinter();
                return getString(R.string.str_conn_state_disconnect);
            }

        }

        @Override
        protected void onPostExecute(String result) {
            KLog.e(result);
            hideLoading();
            long newTime = System.currentTimeMillis();
            String duration = (newTime - currentTime) + "毫秒";
            KLog.e("打印用时：" + duration);
            if (!TextUtils.isEmpty(result)) {
                ToastUtil.showToast(mContext, result);

                if (getString(R.string.str_conn_state_disconnect).equals(result)) {
                    SPUtils.put(mContext, Constants.PRINTER_MAC, "");
                    SPUtils.put(mContext, Constants.PRINTER_NAME, "");
                }
            }
        }

    }


    //得实dl2800异步连接蓝牙设备并打印
    private class Dl2800PrinterConnAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            currentTime = System.currentTimeMillis();
            if (zpl == null) {
                zpl = PrinterUtils.getInstance().initZplBtlPrintLib(mContext, dl2800Mac);
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
            long newTime = System.currentTimeMillis();
            String duration = (newTime - currentTime) + "毫秒";
            KLog.e("打印用时：" + duration);
            if (!TextUtils.isEmpty(result)) {
                ToastUtil.showToast(mContext, result);
                if (getString(R.string.str_conn_state_disconnect).equals(result)) {
                    SPUtils.put(mContext, Constants.PRINTER_MAC, "");
                    SPUtils.put(mContext, Constants.PRINTER_NAME, "");
                }
            }
        }

    }


    public void printJxPic() {
        showLoading();
        PrintDataBean printDataBean = new PrintDataBean();
        printDataBean.setPrintCopies(1);//设置打印份数，不设置模式为1
        printDataBean.setAlignment(0);//设置左中右位置，0左 1中 2右
        printDataBean.getBitmapList().add(LabelBitmapGenerator.createLabelBitmap(mContext, mData, Constants.PRINT_TYPE_20));//添加图片
        printDataBean.setPotency(3);//五挡 1 2 3 4 5 不设置默认3
        printDataBean.setPageModeEnum(PageModeEnum.Mode_Three);
        printDataBean.setCustomizeWidth(2000);
        printDataBean.setPushForPrint(0);//0为普通，1为高级撕纸，不设置默认为普通
        printDataBean.setPageSizeEnum(PageSizeEnum.Customize);

        Bundle bundle = new Bundle();
        bundle.putParcelable(Global.OBJECT1, printDataBean);
        BTPrinterManager.getInstance()
                .setSuspendOHTWhenPrinting(true)
                .setBitmapAlgorithmConverter(new AtkinsonBitmapConverter())
//                .setRt12PrintModeEnum(RT12PrintModeEnum.Thermal_Mode)
                .setPrinterNormalCheckType(PrintConfig.PrinterNormalCheckType.Check_Repeatedly)
                .setUnNormalResponseOnce(true)//设置成true后，打印过程的异常回调只会回调一次
                .setNeedStepAnyValue(true)//设置成true后，才能设置打印完成后走纸
//                .setStepAnyValue(5)//配合上面的setNeedStepAnyValue使用，值的具体值为，例如设置为50，打印完成后会走纸50*0.125mm

                .startPrint(Global.CMD_POS_PRINTPICTURE_LIST, bundle, new Pos.IProgressListener() {
                    @Override
                    public void onCompleted() {
                        Log.d("", "==onPower===打印完成=" + Thread.currentThread().getName());
                        //打印完成回调
                        runOnUiThread(() -> {
                            hideLoading();
                            ToastUtil.showToast(mContext, "打印成功！");
                            KLog.e("打印完成-" + new Date());
                            KLog.e("打印完成");
                        });
                    }

                    /**
                     *
                     * @param processedPages 当前打印到第几页
                     * @param totalPages     总页数
                     * @param processed      当页传输进度
                     * @param printCopies    总共打印的份数
                     * @param curCopies      当前在打印的份数
                     */
                    @Override
                    public void onProgress(int processedPages, int totalPages, int processed, int printCopies, int curCopies) {
                        runOnUiThread(() -> {
                            KLog.e("正在打印" + processedPages + "/" + totalPages + "=当页传输进度=" + processed + "=" + "总共" + printCopies + "份=" + "正在打印第" + curCopies + "份");
                        });

                    }

                    /**
                     *
                     * @param printingStateEnum
                     */
                    @Override
                    public void onPrintingState(PrintingStateEnum printingStateEnum) {
                        if (printingStateEnum == PrintingStateEnum.State_Initializing) {
                            Log.d("onPower", "=" + printingStateEnum.toString() + "=threadName=" + Thread.currentThread().getName());
                        }
                        hideLoading();
                        runOnUiThread(() -> {
                            Log.d("onPrintingState", "====" + printingStateEnum.toString());
                            //打印过程状态回调
                            switch (printingStateEnum) {
                                case State_Error:
                                    hideLoading();
                                    ToastUtil.showToast(mContext, "打印出错,请重试");
                                    KLog.e("打印出错");
                                    break;
                                //初始化中
                                case State_Initializing:
                                    KLog.e("初始化中。。。");
                                    break;
                                //过热
                                case State_Head_Over_Hot:
                                    KLog.e("打印中。。。【目前过热】");
                                    KLog.e("于" + new Date() + "过热中断打印");

                                    break;
                                //缺纸
                                case State_Without_Page:
                                    if (BTPrinterManager.getInstance().isPrinting()) {
                                        KLog.e("请放入打印纸");
                                    }

                                    break;
                                //翻盖未合上，处于开启状态
                                case State_Cover_Up:
                                    KLog.e("请关闭合盖");
                                    break;

                                //热敏模式却有碳架子
                                case State_Thermal_Mode_Mode_Have_Carbon_Frame:
                                    KLog.e("当前热敏模式-请拿掉碳架");
                                    break;
                                //热转印模式没有碳架
                                case State_Without_Carbon_Frame:
                                    KLog.e("当前热转印模式-请放入碳架");
                                    break;
                                //打印过程开启翻盖
                                case State_Cover_Up_When_Printing:
                                    KLog.e("于" + new Date() + "翻盖中断打印");
                                    break;
                                //打印过程蓝牙连接断开
                                case State_Printing_Process_BT_Disconnected:
                                    KLog.e("于" + new Date() + "蓝牙中断");
                                    break;
                                //主动取消打印
                                case State_Cancel_Print:
                                    KLog.e("于" + new Date() + "取消打印");
                                    break;
                                //正在取消打印打印
                                case State_Canceling_Print:
                                    KLog.e("取消打印中");
                                    break;
                                //打印机机正常，开始下发数据
                                case State_Thermal_Mode_Normal:
                                case State_Hot_Transfer_Mode_Normal:
                                    KLog.e("打印中。。。");
                                    break;
                                //正在检测纸张
                                case State_Printer_Pager_Checking:
                                    //纸张检测次数倒计时，如果设置为5次，则从5开始倒计时，
                                    //在所设置的次数都检测到有纸（中间有出现无纸则会重新倒计时检测），
                                    //倒计时结束状态会变成State_Thermal_Mode_Normal，即开始下发数据打印
                                    int index = printingStateEnum.getIndex();
                                    KLog.e("正在检测耗材。。。" + printingStateEnum.getIndex());
                                    break;
                            }
                        });

                    }

                    /**
                     *
                     * @param bitmap 最终下发的bitmap
                     * @param i 当前打印页， 起始为1
                     */
                    @Override
                    public void onFinalSendBitmap(Bitmap bitmap, int i) {

                    }
                });

    }


    private static final String TAG = NewPrintLabelActivity.class.getSimpleName();
    ArrayList<String> per = new ArrayList<>();
    private UsbManager usbManager;
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
    private void initBroadcast() {
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

    private boolean isNewPrinter = true;

    private int PrinterVCode = -1;
    private int PrinterPCode = -1;

    private void checkUsb() {

        List<UsbDevice> usbDeviceList = UsbUtil.getPrinterUsbDeviceList(mContext);
        if (usbDeviceList != null && !usbDeviceList.isEmpty()) {
            mUsbDevice = usbDeviceList.get(0);
            PrinterVCode = mUsbDevice.getVendorId();
            PrinterPCode = mUsbDevice.getProductId();

            KLog.e("PrinterVCode:" + PrinterVCode);

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
                        ll_bqgg.setVisibility(View.VISIBLE);
                        ToastUtil.showToast(mContext, "DP-385" + getString(R.string.str_conn_state_connected));
                    } else {
                        ToastUtil.showToast(mContext, "DP-385" + getString(R.string.str_conn_state_disconnect));
                    }
                } else if (mUsbDevice.getProductId() == usbPid320) {
                    zplDkPrinter = PrinterUtils.getInstance().initDkUsbPrintLib(mContext);
                    if (zplDkPrinter != null) {
                        ll_bqgg.setVisibility(View.VISIBLE);
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
            int version = Build.VERSION.SDK_INT;

            if (Build.VERSION.SDK_INT >= 29) {
                return;
            }

            if (ACTION_USB_PERMISSION.equals(action)) {//
                synchronized (this) {
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (!isConnected) {
                            usbConn(mUsbDevice);
                            Log.e(TAG, "获取权限成功：" + mUsbDevice.getDeviceName());
                        }
                    } else {
                        Log.d(TAG, "permission denied for device " + mUsbDevice);
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                checkUsb();
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                closeConnection();
                ToastUtil.showToast(mContext, "USB" + getString(R.string.str_conn_state_disconnect));
            }
        }
    };


    @Override
    protected PrintLabelPresenter createPresenter() {
        return new PrintLabelPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");

        if (isLandscape) {
            if (mUsbReceiver != null) {
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

                if (conn != null) {
                    unbindService(conn);
                    conn = null;
                }
            }


        } else {
//            if (btPrinter != null) {
//                btPrinter.disconnect();
//                btPrinter = null;
//            }

        }


        if (cc4Task != null && cc4Task.getStatus() == AsyncTask.Status.RUNNING) {
            cc4Task.cancel(true);
        }

        if (dp12370Api != null) {
            dp12370Api.quit();
        }

        PrinterUtils.getInstance().closeDpUsbPrinter();
//        PrinterUtils.getInstance().closeBltPrinter();
        PrinterUtils.getInstance().closeDkUsbPrinter();

        BleManager.getInstance().disconnectAllDevice();

    }


    public static IMyBinder binder;//IMyBinder接口，所有可供调用的连接和发送数据的方法都封装在这个接口内
    //bindService的参数conn
    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {

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
                    Toast.makeText(getApplicationContext(), "USB" + getString(R.string.str_conn_state_disconnect), Toast.LENGTH_SHORT).show();
                }
            });


        }
    };


    // 用于处理各种通知消息，刷新界面的handler
    private final Handler mHandler = new Handler();

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

        if(pairedPrinters!=null && pairedPrinters.size()!=0){
            IDzPrinter.PrinterAddress printer = pairedPrinters.get(0);
            if (printer != null) {
                dp12370Api.openPrinterByAddress(printer);
            }
        }
    }


    // 连接打印机成功时操作
    private void onPrinterConnected(IDzPrinter.PrinterAddress printer) {
        // 连接打印机成功时，刷新界面提示，保存相关信息
        hideLoading();
        ToastUtil.showToast(NewPrintLabelActivity.this, "连接打印机成功！");
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


    // 打印图片
    private boolean printBitmap(Bitmap bitmap, Bundle param) {

        if (dp12370Api == null) {
            connectDp12370();
        }

        // 打印
        return dp12370Api.printBitmap(bitmap, param);
    }


    // 连接打印机操作提交失败、打印机连接失败或连接断开时操作
    private void onPrinterDisconnected() {
        // 连接打印机操作提交失败、打印机连接失败或连接断开时，刷新界面提示
        hideLoading();
        ToastUtil.showToast(NewPrintLabelActivity.this, "连接打印机失败！");
    }


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
            if (DeviceName.contains(Constants.BLUETOOTH_CC4)) {
                cc4Mac = DeviceMac;
            }
            if (DeviceName.contains(Constants.BLUETOOTH_DP385)) {
                dp385Mac = DeviceMac;
                ll_bqgg.setVisibility(View.VISIBLE);
            }
            if (DeviceName.contains(Constants.BLUETOOTH_DP12370)) {
                dp12370Mac = DeviceMac;

//                connectDp12370();
                ll_bqgg.setVisibility(View.VISIBLE);
            }
            if (DeviceName.contains(Constants.BLUETOOTH_DL)) {
                dl2800Mac = DeviceMac;
                ll_bqgg.setVisibility(View.VISIBLE);
            }
            if (DeviceName.contains(Constants.BLUETOOTH_JX_PRINTER)) {
                jxPrinterMac = DeviceMac;
                ll_bqgg.setVisibility(View.VISIBLE);
            }

            doConnectBt();
        }
    }


}
