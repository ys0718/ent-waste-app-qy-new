package com.grkj.module_hardware.weight;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.socks.library.KLog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class WeightBltSession extends Thread {
    private BluetoothDevice _device = null;
    private BluetoothSocket _socket = null;
    private OutputStream _outStream;
    private InputStream _inStream = null;
    public boolean IsConnect = false;
    public String Name = "";
    public String Address = "";
    Handler _handler;
    private Object lock = new Object();


    private WeightBltSession() {

    }

    public static class WeightBltSessionInstance {
        private static final WeightBltSession INSTANCE = new WeightBltSession();
    }


    public static WeightBltSession getInstance() {
        return WeightBltSessionInstance.INSTANCE;
    }


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
            Log.e("TAG","Exception: "+e);
            return;
        }
    }


    public WeightBltSession(BluetoothDevice _device, Handler _handler){
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
            Log.e("TAG","Exception: "+e);
            return;
        }
    }


    //连接socket
    public void connect(){


        new Thread(){
            @Override
            public void run(){
                try {
                    synchronized (lock) {
                        if (_socket != null && _handler != null) {
                            if (_socket.isConnected()) {
                                return;
                            }
                            _socket.connect();  // 会阻塞线程，建议在子线程中进行
                            _outStream = _socket.getOutputStream();
                            _inStream = _socket.getInputStream();
                            IsConnect = true;

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
    public void disconnect(){
        if (_socket != null) {
            _handler = null;
            _device  = null;
            IsConnect = false;
            try {
                _socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isFirstData = true;


    @Override
    public void run(){

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
                if (len > 0 && _handler!=null) {
                    Message msg = _handler.obtainMessage();
                    msg.what = 0;
                    try {
                        if (TextUtils.equals(_device.getName(), "WF610A")) { //适配本田蓝牙
                            msg.obj = new String(buffer,"UTF-8");
                        }else {
                            String weight = reverseString(new String(buffer, "UTF-8"));
                            msg.obj = weight;
                        }


                    } catch (UnsupportedEncodingException e) {
                    }

                    if (_handler!=null){
//                        _handler.sendMessageDelayed(msg,1000);
                        if (isFirstData){
                            isFirstData = false;
                            _handler.sendMessage(msg);
                        }else {
                            _handler.sendMessageDelayed(msg,2000);
                        }
                    }

                }
            }
        }

    }


    //蓝牙秤数据格式转换
    private String reverseString(String s){
        if (TextUtils.isEmpty(s)) {
            return "0.00";
        }
        s = s.substring(1,8);
        KLog.e(s);
        return Double.valueOf((new StringBuffer(s).reverse().toString())).toString();
    }

    public void Send(String _value) throws IOException{
        _outStream.write(_value.getBytes());
    }

    public void Close() throws IOException{
        IsConnect = false;
        _socket.close();
    }
}