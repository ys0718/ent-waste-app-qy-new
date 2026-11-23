package com.grkj.module_hardware.weight;

import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_ATTACHED;
import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.felhr.usbserial.CDCSerialDevice;
import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.socks.library.KLog;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 自动连接USB秤串口服务
 */
public class UsbWeighService extends Service {
    public static final String TAG = "UsbWeighService";
    public static final String ACTION_USB_READY = "com.felhr.serialport.USB_READY";
    public static final String ACTION_USB_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    public static final String ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
    public static final String ACTION_USB_NOT_SUPPORTED = "com.felhr.serialport.USB_NOT_SUPPORTED";
    public static final String ACTION_NO_USB = "com.felhr.serialport.NO_USB";
    public static final String ACTION_USB_PERMISSION_GRANTED = "com.felhr.serialport.USB_PERMISSION_GRANTED";
    public static final String ACTION_USB_PERMISSION_NOT_GRANTED = "com.felhr.serialport.USB_PERMISSION_NOT_GRANTED";
    public static final String ACTION_USB_DISCONNECTED = "com.felhr.serialport.USB_DISCONNECTED";
    public static final String ACTION_CDC_DRIVER_NOT_WORKING = "com.felhr.serialport.ACTION_CDC_DRIVER_NOT_WORKING";
    public static final String ACTION_USB_DEVICE_NOT_WORKING = "com.felhr.serialport.ACTION_USB_DEVICE_NOT_WORKING";
    public static final String ACTION_PRINT = "com.felhr.serialport.PRINT";
    public static final int MESSAGE_FROM_SERIAL_PORT = 0;
    public static final int CTS_CHANGE = 1;
    public static final int DSR_CHANGE = 2;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static final int BAUD_RATE = 9600; // BaudRate. Change this value if you need
    public static boolean SERVICE_CONNECTED = false;

    private IBinder binder = new UsbBinder();

    private Context context;
    private Handler mHandler;
    private UsbManager usbManager;
    private UsbDevice device;
    private UsbDeviceConnection connection;
    private UsbSerialDevice serialPort;

    private boolean serialPortConnected;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);//线程池管理线程

    private UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {
        @Override
        public void onReceivedData(byte[] arg0) {
            try {
                String data = new String(arg0, "UTF-8");
                if (mHandler != null)
                    mHandler.obtainMessage(MESSAGE_FROM_SERIAL_PORT, data).sendToTarget();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

    private UsbSerialInterface.UsbCTSCallback ctsCallback = new UsbSerialInterface.UsbCTSCallback() {
        @Override
        public void onCTSChanged(boolean state) {
            if (mHandler != null) mHandler.obtainMessage(CTS_CHANGE).sendToTarget();
        }
    };

    private UsbSerialInterface.UsbDSRCallback dsrCallback = new UsbSerialInterface.UsbDSRCallback() {
        @Override
        public void onDSRChanged(boolean state) {
            if (mHandler != null) mHandler.obtainMessage(DSR_CHANGE).sendToTarget();
        }
    };

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context mContent, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) return;
            if (TextUtils.equals(action, ACTION_USB_PERMISSION)) {
                Bundle b = intent.getExtras();
                boolean granted = b != null && b.getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED, false);
                if (granted) {
                    mContent.sendBroadcast(new Intent(ACTION_USB_PERMISSION_GRANTED));

                    if (usbManager != null && device != null) {
                        connection = usbManager.openDevice(device);
                        executeThreadPool(() -> {
                            serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
                            if (serialPort != null) {
                                if (serialPort.open()) {
                                    serialPortConnected = true;
                                    serialPort.setBaudRate(BAUD_RATE);
                                    serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                                    serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                                    serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                                    serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                                    serialPort.read(mCallback);
                                    serialPort.getCTS(ctsCallback);
                                    serialPort.getDSR(dsrCallback);
                                    context.sendBroadcast(new Intent(ACTION_USB_READY));
                                } else {
                                    context.sendBroadcast(new Intent(serialPort instanceof CDCSerialDevice ? ACTION_CDC_DRIVER_NOT_WORKING : ACTION_USB_DEVICE_NOT_WORKING));
                                }
                            } else {
                                context.sendBroadcast(new Intent(ACTION_USB_NOT_SUPPORTED));
                            }
                        });
                    }


                } else {
                    mContent.sendBroadcast(new Intent(ACTION_USB_PERMISSION_NOT_GRANTED));
                }
            } else if (TextUtils.equals(action, ACTION_USB_ATTACHED)) {
//                if (!serialPortConnected) findSerialPortDevice();todo
            } else if (TextUtils.equals(action, ACTION_USB_DETACHED)) {
                mContent.sendBroadcast(new Intent(ACTION_USB_DISCONNECTED));
                if (serialPortConnected) {
                    serialPort.close();
                }
                serialPortConnected = false;
            }
        }
    };

    @Override
    public void onCreate() {
        this.context = this;
        serialPortConnected = false;
        UsbWeighService.SERVICE_CONNECTED = true;
        setFilter();
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        findSerialPortDevice();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (serialPort != null) serialPort.close();
        unregisterReceiver(usbReceiver);
        UsbWeighService.SERVICE_CONNECTED = false;
    }

    public void write(byte[] data) {
        if (serialPort != null) {
            serialPort.write(data);
        }
    }

    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public static final int NEW_USB_PID = 29987;

    private void findSerialPortDevice() {
        // This snippet will try to open the first encountered usb device connected, excluding usb root hubs
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            // first, dump the hashmap for diagnostic purposes
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                KLog.e("findSerialPortDevice" + device);
                int deviceVID = device.getVendorId();
                int devicePID = device.getProductId();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    KLog.e("findSerialPortDevice3" + device);
                    if ((UsbSerialDevice.isSupported(device) || devicePID == NEW_USB_PID) && !TextUtils.isEmpty(device.getProductName()) && !device.getProductName().contains("Printer")) {
                        KLog.e("findSerialPortDevice2" + device);
                        sendBroadcast(new Intent(ACTION_PRINT).putExtra("device", device).putExtra("isSupported", (UsbSerialDevice.isSupported(device)) || devicePID == NEW_USB_PID));
                        // There is a supported device connected - request permission to access it.
                        requestUserPermission();
                        break;
                    } else {
                        connection = null;
                        device = null;
                    }
                }
            }
            if (device == null) {
                sendBroadcast(new Intent(ACTION_NO_USB));
            }
        } else {
            Log.d(TAG, "findSerialPortDevice() usbManager returned empty device list.");
            // There is no USB devices connected. Send an intent to WeightMainActivity
            sendBroadcast(new Intent(ACTION_NO_USB));
        }
    }

    private void setFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(ACTION_USB_DETACHED);
        filter.addAction(ACTION_USB_ATTACHED);
        registerReceiver(usbReceiver, filter);
    }

    private void requestUserPermission() {
        Log.d(TAG, String.format("requestUserPermission(%X:%X)", device.getVendorId(), device.getProductId()));
        if (usbManager != null)
            usbManager.requestPermission(device, PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0));
    }

    public class UsbBinder extends Binder {
        public UsbWeighService getService() {
            return UsbWeighService.this;
        }
    }

    //执行线程
    void executeThreadPool(OnThreadListener mOnThreadListener) {
        if (fixedThreadPool == null) fixedThreadPool = Executors.newFixedThreadPool(10);
        fixedThreadPool.execute(() -> {
            if (mOnThreadListener != null) mOnThreadListener.onRunThread();
        });
    }

    interface OnThreadListener {
        void onRunThread();
    }

}