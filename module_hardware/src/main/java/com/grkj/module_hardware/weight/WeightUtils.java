package com.grkj.module_hardware.weight;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.grkj.module_hardware.utils.GsonUtil;
import com.grkj.module_hardware.utils.SPUtils;
import com.ll.sdk.library.hlweigh.serialport.WeightManager;
import com.socks.library.KLog;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

/**
 * Create by seny on 2023/11/28
 * Description: 称重工具类
 **/
public class WeightUtils {

    //是否为称重入库
    private boolean isWeight = false;

    //是否获取重量并更新到入库量中
    private boolean isGetWeight = false;

    private boolean isNew = false;

    //是否点击清除
    protected boolean isClear;

    //单位转换相关变量
    private int compareSize = 1000;
    private int compareFloatSize = 4;
    private static final int COMPARE_SIZE_DUN = 1000;
    private static final int COMPARE_SIZE_KG = 1;


    private BluetoothDevice _device = null;
    private boolean isFirstGetWeight = true;
    protected WeightBltSession _session = null;//蓝牙连接线程

    //蓝牙秤数据读取处理器
    public Handler blt_handler = null;

    protected UsbWeighService usbService;
    public Handler mUsbHandler;

    public boolean isWeight() {
        return isWeight;
    }

    public void setWeight(boolean weight) {
        isWeight = weight;
    }

    public boolean isGetWeight() {
        return isGetWeight;
    }

    public void setGetWeight(boolean getWeight) {
        isGetWeight = getWeight;
    }

    public boolean isClear() {
        return isClear;
    }

    public void setClear(boolean clear) {
        isClear = clear;
    }


    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    private WeightUtils() {
    }

    public static class WeightUtilsInstance {
        private static final WeightUtils INSTANCE = new WeightUtils();
    }

    public static WeightUtils getInstance() {
        return WeightUtilsInstance.INSTANCE;
    }


    //初始化富钜源usb秤
    public void initFJYWeight(Handler handler) {
        this.mUsbHandler = handler;
        WeightManager.getInstance().openSerialPort();

        //设置监听
        WeightManager.getInstance().setWeightChangeListener(new WeightManager.WeightChangeListener() {
            @Override
            public void weightChange(Integer weigh) {
                KLog.e("setWeightChangeListener:" + weigh);
                setFjyUsbWeightView(tv_weight1, tv_weight2, tv_weight3, et_item8, weigh.toString());
            }
        });

        //延迟执行
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //多次定时读取重量
                WeightManager.getInstance().startReadWeightRepeat();
            }
        }, 200);

    }


    //富钜源USB称重控件处理
    public void setFjyUsbWeightView(TextView tv_weight1, TextView tv_weight2, EditText tv_weight3, TextView et_item8, String data) {
        if (tv_weight2 == null) {
            return;
        }
        Double weight2 = new BigDecimal(data).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tv_weight2.setText(weight2 + "");//显示重量
        if (isClear || TextUtils.isEmpty(tv_weight2.getText().toString())) {
            tv_weight2.setText("0.000");//显示重量
            isClear = false;
        }

        weight2 = Double.valueOf(tv_weight2.getText().toString());

        Double weight3 = 0.0;
        if (!TextUtils.isEmpty(tv_weight3.getText().toString())) {
            weight3 = Double.valueOf(tv_weight3.getText().toString());
        } else {
            weight3 = 0.0;
        }

        Double weight1 = new BigDecimal(weight2).subtract(new BigDecimal(weight3)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tv_weight1.setText(weight1 + "");
        if (isGetWeight) {
            et_item8.setText(new BigDecimal(weight1).divide(new BigDecimal(compareSize)).setScale(compareFloatSize, BigDecimal.ROUND_HALF_UP).toString());
            isGetWeight = false;
        }

        if (isNew) {
            et_item8.setText(new BigDecimal(weight1).divide(new BigDecimal(compareSize)).setScale(compareFloatSize, BigDecimal.ROUND_HALF_UP).toString());
        }

    }


    //初始化蓝牙秤
    public void initBltWeight() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        String address = SPUtils.get(mContext, "blt_connected_address", "").toString();
        String xshqName = SPUtils.get(mContext, HexUtil.WEIGHT_NAME, "").toString();
        String xshqMac = SPUtils.get(mContext, HexUtil.WEIGHT_MAC, "").toString();

        if (bondedDevices.size() > 0 && !TextUtils.isEmpty(address)) {
            for (BluetoothDevice device : bondedDevices) {
                if (address.equals(device.getAddress())) {
                    isFirstGetWeight = true;
                    _device = device;
                    btnConnect_Click(_device);
                } else {
                    if (!TextUtils.isEmpty(xshqName)) {
                        connectBlt(xshqMac);
                    } else {
                        scanBlt();
                    }
                }
            }
        } else if (!TextUtils.isEmpty(xshqName)) {
            connectBlt(xshqMac);
        } else {
            scanBlt();
        }
    }

    /**
     * 判断设备
     */
    private boolean isT2Device() {
        boolean isDevice = false;
        String resolution = getDeviceResolution(mContext);
        //根据当前3款手持终端设备分辨率判断
        //手持设备设置中显示大小不为标准时 高度数值有所不同，宽度数值保持为720 T1/i6310/8876WB
        String[] devicesList = {"720*1424"};

        for (String s : devicesList) {
            if (s.equals(resolution)) {
                isDevice = true;
                break;
            }
        }

        return isDevice;
    }

    /**
     * 获取屏幕分辨率
     */
    public static String getDeviceResolution(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(metrics);

            int widthPixels = metrics.widthPixels;
            int heightPixels = metrics.heightPixels;

            return widthPixels + "*" + heightPixels;
        }

        return "Unknown";
    }


    private BleDevice weightBle;

    private void scanBlt() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                KLog.e("scanResultList:" + GsonUtil.GsonToString(scanResultList));
                if (scanResultList.size() > 0) {
                    BleDevice device = scanResultList.get(0);
                    SPUtils.put(mContext, HexUtil.WEIGHT_MAC, device.getMac());
                    SPUtils.put(mContext, HexUtil.WEIGHT_NAME, device.getName());
                    connectBlt(device.getMac());
                }
            }

            @Override
            public void onScanStarted(boolean success) {
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
            }
        });
    }

    /**
     * 对指定mac蓝牙连接
     */
    private void connectBlt(String mac) {


        BleManager.getInstance().connect(mac, new BleGattCallback() {
            @Override
            public void onStartConnect() {
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                KLog.e("蓝牙连接失败！");
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {

                if (blt_handler == null) {
                    blt_handler = new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what) {
                                //蓝牙秤实时数据
                                case 0:
                                    Log.e("TAG", "handleMessage: " + msg.obj);
                                    String weight = (String) msg.obj;
                                    setBltWeightView(weight);
                                    break;
                                //蓝牙连接状态
                                case 1:
                                    if (weightBle != null) {
                                        KLog.e("蓝牙称连接成功！");
                                        isFirstGetWeight = false;
                                    } else {
                                        KLog.e("蓝牙称连接失败！");
                                        initBltWeight();
                                    }
                                    break;
                            }

                        }
                    };
                }


                Message msg = blt_handler.obtainMessage();
                msg.what = 1;
                blt_handler.sendMessage(msg);
                weightBle = bleDevice;
                notifyData(weightBle);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                KLog.e("蓝牙断开连接！");
            }
        });
    }


    /**
     * 接收蓝牙回调
     */
    private synchronized void notifyData(BleDevice bleDevice) {
        BleManager.getInstance().notify(
                bleDevice,
                HexUtil.WEIGHT_SERVICE_UUID,
                HexUtil.WEIGHT_WRITE_UUID,
                new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        KLog.e("获取蓝牙数据成功！");
                    }

                    @Override
                    public void onNotifyFailure(BleException exception) {
                        KLog.e("获取蓝牙数据失败！");
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        String dataStr = HexUtil.formatASCIIString(data);
                        String weight = dataStr.substring(2, 8);
                        weight = new BigDecimal(weight).doubleValue() + "";
                        KLog.e("dataStr:" + weight);

                        Message msg = blt_handler.obtainMessage();
                        msg.what = 0;
                        msg.obj = weight;
                        blt_handler.sendMessage(msg);
                    }
                }
        );
    }


    //蓝牙秤启动连接线程
    public void btnConnect_Click(BluetoothDevice device) {
        this._device = device;
        KLog.e("蓝牙称已连接");
        if (TextUtils.isEmpty(device.getAddress())) {
            return;
        }


        if (_session == null) {
            blt_handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        //蓝牙秤实时数据
                        case 0:
                            Log.e("TAG", "handleMessage: " + msg.obj);
                            String weight = (String) msg.obj;
                            if (!TextUtils.isEmpty(weight) && _session != null && _session.IsConnect) {
                                setBltWeightView(weight);
                            }

                            SPUtils.put(mContext, "blt_connected_device", GsonUtil.GsonToString(device));

                            break;
                        //蓝牙连接状态
                        case 1:
                            if (_session != null && _session.IsConnect && _session.getState() != Thread.State.RUNNABLE) {
                                isFirstGetWeight = false;
                                _session.start();
                            } else {
                                KLog.e("蓝牙称连接失败！");
                                initBltWeight();
                            }
                            break;
                    }

                }
            };

            _session = WeightBltSession.getInstance();
            _session.initData(_device, blt_handler);
            _session.connect();
        }
    }


    //蓝牙称重控件处理
    public void setBltWeightView(String data) {
        Double weight2 = 0.0;
        Double weight3 = 0.0;

        KLog.e("setBltWeightView:" + data);
        KLog.e("compareFloatSize:" + compareFloatSize);
        KLog.e("compareSize:" + compareSize);
        if (_device != null && TextUtils.equals(_device.getName(), "WF610A")) { //适配本田蓝牙

            if (data.contains("*") && data.length() > 7) {
                String newData = (data.replace(" ", "")).substring(3, 7);
                KLog.e("newData:" + newData);
                data = new BigDecimal(newData).doubleValue() + "";
            }

            if (data.contains(":") && data.length() > 9) {
                String newData = (data.replace(" ", "")).substring(3, 9);
                KLog.e("newData:" + newData);
                data = new BigDecimal(newData).doubleValue() + "";
            }

        }
//        else {
//            data = reverseString(data);
//        }

        if (tv_weight2 == null) {
            return;
        }

        if (data.contains("-")) {
            tv_weight2.setText("0.000");//显示重量
        } else {
            tv_weight2.setText(data);//显示重量
        }

        if (isClear) {
            tv_weight2.setText("0.000");//显示重量
            isClear = false;
        }

        weight2 = Double.valueOf(tv_weight2.getText().toString());

        if (!TextUtils.isEmpty(tv_weight3.getText().toString())) {
            weight3 = Double.valueOf(tv_weight3.getText().toString());
        } else {
            weight3 = 0.0;
        }

        Double weight1 = new BigDecimal(weight2).subtract(new BigDecimal(weight3)).setScale(compareFloatSize, BigDecimal.ROUND_HALF_UP).doubleValue();
        tv_weight1.setText(weight1 + "");
        if (isGetWeight) {
            et_item8.setText(new BigDecimal(weight1).divide(new BigDecimal(compareSize)).setScale(compareFloatSize, BigDecimal.ROUND_HALF_UP).toString());
        }
        if (isNew) {
            et_item8.setText(new BigDecimal(weight1).divide(new BigDecimal(compareSize)).setScale(compareFloatSize, BigDecimal.ROUND_HALF_UP).toString());
        }
    }

    private TextView tv_weight1;
    private TextView tv_weight2;
    private EditText tv_weight3;
    private TextView et_item8;
    private Context mContext;

    public void initView(Context mContext, TextView tv_weight1, TextView tv_weight2, EditText tv_weight3, TextView et_item8, String jldw) {
        this.mContext = mContext;
        this.tv_weight1 = tv_weight1;
        this.tv_weight2 = tv_weight2;
        this.tv_weight3 = tv_weight3;
        this.et_item8 = et_item8;
//        setJldwInfo(jldw);
    }

    //初始化usb秤
    public void initUsbWeight(Handler handler) {
        this.mUsbHandler = handler;
        setFilters();
        startService(UsbWeighService.class, usbConnection, null);

        tv_weight3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Double weight2 = 0.0;
                if (!TextUtils.isEmpty(tv_weight2.getText().toString())) {
                    weight2 = Double.valueOf(tv_weight2.getText().toString());
                } else {
                    weight2 = 0.0;
                }
                setUsbWeightView(weight2.toString());
            }
        });
    }

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbWeighService.SERVICE_CONNECTED) {
            Intent startService = new Intent(mContext, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            mContext.startService(startService);
        }
        Intent bindingIntent = new Intent(mContext, service);
        mContext.bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbWeighService.UsbBinder) arg1).getService();
            usbService.setHandler(mUsbHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };


    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbWeighService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbWeighService.ACTION_NO_USB);
        filter.addAction(UsbWeighService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbWeighService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbWeighService.ACTION_USB_PERMISSION_NOT_GRANTED);
        filter.addAction(UsbWeighService.ACTION_PRINT);
        mContext.registerReceiver(mUsbReceiver, filter);
    }

    public int devicePID = 0;


    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) return;
            switch (action) {
                case UsbWeighService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
//                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbWeighService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
//                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbWeighService.ACTION_NO_USB: // NO USB CONNECTED
//                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbWeighService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
//                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbWeighService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
//                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
                case UsbWeighService.ACTION_PRINT:
                    UsbDevice usbDevice = intent.getParcelableExtra("device");
                    boolean isSupported = intent.getBooleanExtra("isSupported", false);
                    devicePID = usbDevice.getProductId();
                    KLog.e("devicePID:" + devicePID);
                    break;
            }
        }
    };


    //USB称重控件处理
    public void setUsbWeightView(String data) {
        Double weight2 = Double.valueOf(pareData(data));
        if (data.contains("WS") && weight2 != 0) {
            if (data.contains("-")) {
                tv_weight2.setText("0.000");//显示重量
                usbService.write(orderZero());
            } else {
                tv_weight2.setText(pareData(data));//显示重量
            }
        }
        if (isClear) {
            tv_weight2.setText("0.000");//显示重量
            usbService.write(orderZero());
            isClear = false;
        }

        weight2 = Double.valueOf(tv_weight2.getText().toString());

        Double weight3 = 0.0;
        if (!TextUtils.isEmpty(tv_weight3.getText().toString())) {
            weight3 = Double.valueOf(tv_weight3.getText().toString());
        } else {
            weight3 = 0.0;
        }

        Double weight1 = new BigDecimal(weight2).subtract(new BigDecimal(weight3)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tv_weight1.setText(weight1 + "");
        if (isGetWeight) {
            et_item8.setText(new BigDecimal(weight1).divide(new BigDecimal(compareSize)).setScale(compareFloatSize, BigDecimal.ROUND_HALF_UP).toString());
            if (data.contains("WS")) {
                isGetWeight = false;
            }
        }

        if (isNew) {
            et_item8.setText(new BigDecimal(weight1).divide(new BigDecimal(compareSize)).setScale(compareFloatSize, BigDecimal.ROUND_HALF_UP).toString());
        }

    }


    public void setJldwInfo(String jldw) {
        if ("吨".equals(jldw)) {
            compareSize = COMPARE_SIZE_DUN;
            compareFloatSize = 4;
        } else {
            compareSize = COMPARE_SIZE_KG;
//            compareFloatSize = 1;
            compareFloatSize = 2;
        }

        SPUtils.put(mContext, "JLDW", jldw);

    }


    /**
     * USB称数据解析
     */
    private static String pareData(String msg) {
        if (null != msg && msg.length() >= 9) {
            try {
                return new DecimalFormat("#0.000").format(Integer.parseInt(msg.substring(3, 9)) / 10.0);
            } catch (Exception e) {
                return "0.000";
            }
        }
        return "0.000\n";
    }


    /**
     * 将秤盘上的重量置为零重基准
     */
    public byte[] orderZero() {
        byte[] orderZero = new byte[7];
        orderZero[0] = 0X01;
        orderZero[1] = 0X22;
        orderZero[2] = 0X00;
        orderZero[3] = 0X00;
        orderZero[4] = 0X00;
        orderZero[5] = 0X00;
        orderZero[6] = 0X23;
        return orderZero;
    }


    /**
     * 数据格式转换
     */
    public String reverseString(String s) {
        if (!TextUtils.isEmpty(s) && s.length() >= 8) {
            s = s.substring(1, 8);
            try {
                return Double.valueOf((new StringBuffer(s).reverse().toString())).toString();
            } catch (NumberFormatException e) {
                return "0.00";
            }

        } else {
            return "0.00";
        }
    }


    public void stopWeight() {
        if (mUsbHandler != null) {
            mUsbHandler.removeCallbacks(null);
        }

        if (WeightManager.getInstance() != null) {
            WeightManager.getInstance().stopReadWeight();
        }

        if (_session != null) {
            _session.disconnect();
//            KLog.e("蓝牙已断开");
            if (blt_handler != null) {
                blt_handler.removeCallbacksAndMessages(null);
            }
            _session = null;
            blt_handler = null;
        }

        if (weightBle != null) {
            BleManager.getInstance().disconnect(weightBle);
        }
    }


    public void onDestroy() {
        if (isWeight) {
            if (usbService != null) {
                mContext.unregisterReceiver(mUsbReceiver);
                mContext.unbindService(usbConnection);
                usbService = null;
            }

            if (weightBle != null) {
                BleManager.getInstance().disconnect(weightBle);
            }

        }
    }

}
