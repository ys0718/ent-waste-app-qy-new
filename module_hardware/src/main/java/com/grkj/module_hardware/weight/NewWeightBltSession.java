package com.grkj.module_hardware.weight;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.socks.library.KLog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class NewWeightBltSession extends Thread {
    private BluetoothDevice _device = null;
    private BluetoothSocket _socket = null;
    private OutputStream _outStream;
    private InputStream _inStream = null;
    public boolean IsConnect = false;
    public String Name = "";
    public String Address = "";
    public Handler _handler;
    private Object lock = new Object();


    public void initData(BluetoothDevice _device, Handler _handler) {
        this._handler = _handler;
        this._device = _device;
        this.Name = this._device.getName();
        this.Address = this._device.getAddress();
        IsConnect = false;
        try {
            // 蓝牙串口服务对应的UUID。如使用的是其它蓝牙服务，需更改下面的字符串
            // UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            _socket = _device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        } catch (Exception e) {
            Log.e("TAG", "Exception: " + e);
            return;
        }
    }


    public NewWeightBltSession(BluetoothDevice _device) {
        this._device = _device;
        this.Name = this._device.getName();
        this.Address = this._device.getAddress();
        _handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    //蓝牙秤实时数据
                    case 0:
                        Log.e("TAG", "handleMessage: " + msg.obj);
                        String weight = (String) msg.obj;
                        if (!TextUtils.isEmpty(weight) && IsConnect) {
                            if (getDataCallback != null) {
                                getDataCallback.getData(weight);
                            }
                        }
                        break;
                    //蓝牙连接状态
                    case 1:
                        if (IsConnect) {
                            start();
                        } else {
                            KLog.e("蓝牙称连接失败！");
                        }
                        break;
                }
            }
        };

        try {
            // 蓝牙串口服务对应的UUID。如使用的是其它蓝牙服务，需更改下面的字符串
            // UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            _socket = _device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            IsConnect = true;
            connect();
        } catch (Exception e) {
            Log.e("TAG", "Exception: " + e);
            return;
        }
    }


    //连接socket
    public void connect() {


        new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (lock) {
                        if (_socket != null && _handler != null && IsConnect) {
                            if (_socket.isConnected()) {
                                return;
                            }
                            _socket.connect();  // 会阻塞线程，建议在子线程中进行
                            _outStream = _socket.getOutputStream();
                            _inStream = _socket.getInputStream();

                            Message msg = _handler.obtainMessage();
                            msg.what = 1;
                            _handler.sendMessage(msg);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    //关闭socket
    public void disconnect() {
        IsConnect = false;
        interrupt();
        if (_socket != null) {
            if (_handler != null) {
                _handler.removeCallbacksAndMessages(null);
                _handler = null;
            }
            _device = null;
            try {
                _socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void run() {

        if (IsConnect) {
            byte[] buffer = new byte[1024];
            int len = 0;
            while (true) {
                //从InputStream读取
                try {
                    len = _inStream.read(buffer);
                } catch (IOException e) {
                    continue;
                }
                if (len > 0 && _handler != null) {
                    Message msg = _handler.obtainMessage();
                    msg.what = 0;
                    try {
                        String weight = reverseString(new String(buffer, "UTF-8"));
                        msg.obj = weight;
                    } catch (UnsupportedEncodingException e) {
                    }

                    if (_handler != null) {
//                        _handler.sendMessageDelayed(msg,1000);
                        _handler.sendMessage(msg);
                    }

                }
            }
        }

    }


    //蓝牙秤数据格式转换
    private String reverseString(String s) {
        if (TextUtils.isEmpty(s)) {
            return "0.00";
        }
        s = s.substring(1, 8);
        KLog.e(s);
        return Double.valueOf((new StringBuffer(s).reverse().toString())).toString();
    }

    public void Send(String _value) throws IOException {
        _outStream.write(_value.getBytes());
    }

    public void Close() throws IOException {
        IsConnect = false;
        _socket.close();
    }


    private GetDataCallback getDataCallback;

    public void setDataCallback(GetDataCallback getDataCallback) {
        this.getDataCallback = getDataCallback;
    }

    public interface GetDataCallback {
        void getData(String weight);
    }

}