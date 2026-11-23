package com.grkj.module_hardware.weight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 经典蓝牙（SPP）秤工具类（支持定时查询 + 数据节流 + 自动重连）
 * <p>
 * 功能特性：
 * 1. 连接 SPP 蓝牙设备，自动读取数据。
 * 2. 支持定时发送查询指令（主动查询模式）。
 * 3. 支持数据节流：无论设备推送频率多高，都可按固定间隔回调最新重量。
 * 4. 连接失败后自动重连（间隔 1 秒，无限重试，直至成功或外部主动断开）。
 * </p>
 *
 * @author YourName
 * @version 3.1
 */
public class SppBluetoothScale {
    private static final String TAG = "SppBluetoothScale";

    /** 标准 SPP 服务 UUID */
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//    private static final UUID SPP_UUID = UUID.fromString("0000fff1-0000-1000-8000-00805F9B34FB");

    private final Context context;
    private  Callback callback;
    private  Handler mainHandler;
    private BluetoothAdapter bluetoothAdapter;

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Thread readThread;
    private volatile boolean isConnected = false;
    private boolean isFirstData = true;

    private DataParser dataParser;

    // 定时查询相关
    private ScheduledExecutorService queryScheduler;

    // 数据节流相关
    private long throttleIntervalMs = 0;
    private volatile float latestWeight = -1f;
    private volatile byte[] latestRawData;
    private final AtomicBoolean throttleScheduled = new AtomicBoolean(false);
    private final Runnable throttleRunnable = () -> {
        throttleScheduled.set(false);
        if (callback != null && latestRawData != null) {
            callback.onWeightReceived(latestWeight, latestRawData);
        }
    };

    // ========== 自动重连相关 ==========
    private String lastDeviceAddress;                     // 最后一次连接的设备地址
    private ScheduledExecutorService reconnectExecutor;   // 重连任务调度器
    private ScheduledFuture<?> reconnectFuture;           // 当前待执行的重连任务
    private final AtomicBoolean isConnecting = new AtomicBoolean(false); // 防止并发连接
    private final Object reconnectLock = new Object();    // 重连任务同步锁

    private int reconnectCount = 0;

    public interface Callback {
        void onConnectionStateChange(boolean connected);
        void onWeightReceived(float weight, byte[] rawData);
        void onError(String error);
    }

    public interface DataParser {
        float parseWeight(byte[] data);
    }

    public SppBluetoothScale(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        setThrottleInterval(2);
    }

    public void setDataParser(DataParser parser) {
        this.dataParser = parser;
    }

    public void setThrottleInterval(long seconds) {
        this.throttleIntervalMs = seconds > 0 ? seconds * 1000 : 0;
    }

    public boolean isConnected() {
        return isConnected;
    }

    /**
     * 连接到指定 MAC 地址的设备（主动调用时，会取消当前所有重连任务并断开已有连接）
     */
    public void connect(String deviceAddress) {
        // 取消任何正在进行的重连任务
        cancelReconnect();
        // 如果已有连接，主动断开
        if (isConnected) {
            disconnect();
        }
        // 保存地址，供重连使用
        this.lastDeviceAddress = deviceAddress;

        if (bluetoothAdapter == null) {
            postError("设备不支持蓝牙");
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            postError("蓝牙未开启");
            return;
        }

        device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        if (device == null) {
            postError("无效的设备地址");
            return;
        }

        // 开始连接（在独立线程中执行）
        startConnectThread();
    }

    /**
     * 断开连接，并取消所有重连任务
     */
    public void disconnect() {
        cancelReconnect();           // 取消待执行的重连
        closeAndCleanup(true);       // 关闭资源并通知状态变化
    }

    /**
     * 释放所有资源（关闭 Socket、停止读取线程、停止定时查询）
     * @param notifyDisconnect 是否回调 onConnectionStateChange(false)
     */
    private void closeAndCleanup(boolean notifyDisconnect) {
        // 停止定时查询
        stopPeriodicQuery();

        // 中断读取线程
        if (readThread != null) {
            readThread.interrupt();
            readThread = null;
        }

        // 关闭 Socket 及相关流
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "关闭 Socket 异常", e);
            }
            socket = null;
        }
        inputStream = null;
        outputStream = null;

        // 重置标志
        isConnected = false;
        isFirstData = true;

        if (mainHandler!=null){
            // 清除节流任务
            mainHandler.removeCallbacks(throttleRunnable);
        }

        throttleScheduled.set(false);

        if (notifyDisconnect) {
            postConnectionState(false);
        }
    }

    /**
     * 启动一个连接线程（实际执行连接操作）
     */
    private void startConnectThread() {
        // 防止多个连接线程同时运行
        if (!isConnecting.compareAndSet(false, true)) {
            Log.d(TAG, "已有连接线程正在执行，忽略本次请求");
            return;
        }
        new Thread(() -> {
            try {
                connectSocket();
            } finally {
                isConnecting.set(false);
            }
        }).start();
    }

    /**
     * 释放所有资源，清除回调，断开连接（用于页面销毁时）
     */
    public void release() {
        this.callback = null;          // 避免内存泄漏
        disconnect();                  // 断开连接并释放资源
        if (mainHandler!=null){
            mainHandler.removeCallbacksAndMessages(null);
            mainHandler = null;
        }
    }

    /**
     * 执行实际的 Socket 连接操作（在子线程中运行）
     */
    @SuppressLint("MissingPermission")
    private void connectSocket() {
        try {
            socket = device.createRfcommSocketToServiceRecord(SPP_UUID);
            socket.connect();
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            // 连接成功，取消任何待执行的重连任务
            cancelReconnect();

            isConnected = true;
            isFirstData = true;
            postConnectionState(true);
            startReadThread();
            reconnectCount = 0;
        } catch (IOException e) {
            Log.e(TAG, "连接失败", e);

            if(reconnectCount<5 && callback!=null){ //重连5次
                postError("连接失败，尝试重新连接...");
                // 清理本次失败产生的资源（不通知断开，因为状态本来就是断开）
                closeAndCleanup(false);
                // 调度 3 秒后自动重连
                scheduleReconnect();
            }else {
//                postError("连接失败，请检查是否打开蓝牙地磅！");
            }
            reconnectCount ++;
        }
    }

    /**
     * 启动读取线程（持续监听输入流）
     */
    private void startReadThread() {
        readThread = new Thread(() -> {
            byte[] buffer = new byte[1024];
            int len;
            while (isConnected && !Thread.currentThread().isInterrupted()) {
                try {
                    len = inputStream.read(buffer);
                    if (len > 0) {
                        byte[] data = new byte[len];
                        System.arraycopy(buffer, 0, data, 0, len);
                        handleReceivedData(data);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "读取数据异常", e);

//                    postError("读取重量失败，请检查蓝牙表头是否开启！");

//                    if (isConnected) {
//                        Log.e(TAG, "读取数据异常", e);
//                        postError("读取数据失败: " + e.getMessage());
//                        // 读取异常通常意味着连接已断开，触发重连
//                        closeAndCleanup(false);
//                        scheduleReconnect();
//                    }
//                    break;
                }
            }
        });
        readThread.start();
    }

    // ========== 自动重连相关方法 ==========

    /**
     * 取消所有待执行的重连任务
     */
    private void cancelReconnect() {
        synchronized (reconnectLock) {
            if (reconnectFuture != null) {
                reconnectFuture.cancel(false);
                reconnectFuture = null;
            }
            if (reconnectExecutor != null) {
                reconnectExecutor.shutdownNow();
                reconnectExecutor = null;
            }
        }
    }

    /**
     * 调度 1 秒后执行重连
     */
    private void scheduleReconnect() {
        // 如果已经连接，不需要重连
        if (isConnected) {
            return;
        }
        // 如果没有保存设备地址，无法重连
        if (lastDeviceAddress == null) {
            return;
        }
        synchronized (reconnectLock) {
            if (reconnectExecutor == null || reconnectExecutor.isShutdown()) {
                reconnectExecutor = Executors.newSingleThreadScheduledExecutor();
            }
            // 取消之前的重连任务（如果有）
            if (reconnectFuture != null) {
                reconnectFuture.cancel(false);
            }
            reconnectFuture = reconnectExecutor.schedule(() -> {
                // 重连前再次确认连接状态和地址
                if (!isConnected && lastDeviceAddress != null && !isConnecting.get()) {
                    Log.d(TAG, "开始自动重连...");
                    // 重新获取设备对象
                    if (bluetoothAdapter != null) {
                        device = bluetoothAdapter.getRemoteDevice(lastDeviceAddress);
                        if (device != null) {
                            startConnectThread();
                        } else {
                            postError("自动重连失败：无效的设备地址");
                        }
                    } else {
                        postError("自动重连失败：蓝牙适配器不可用");
                    }
                }
            }, 1, TimeUnit.SECONDS);
        }
    }

    // ========== 其他原有方法（未做重大修改） ==========

    public void send(String data) throws IOException {
        if (outputStream != null && isConnected) {
            outputStream.write(data.getBytes());
        } else {
            throw new IOException("连接未建立或已断开");
        }
    }

    public void startPeriodicQuery(long intervalSeconds, String command) {
        stopPeriodicQuery();
        queryScheduler = Executors.newSingleThreadScheduledExecutor();
        queryScheduler.scheduleAtFixedRate(() -> {
            if (isConnected && outputStream != null) {
                try {
                    outputStream.write(command.getBytes());
                } catch (IOException e) {
                    postError("发送查询命令失败: " + e.getMessage());
                }
            }
        }, 0, intervalSeconds, TimeUnit.SECONDS);
    }

    public void stopPeriodicQuery() {
        if (queryScheduler != null) {
            queryScheduler.shutdown();
            queryScheduler = null;
        }
    }

    private void handleReceivedData(byte[] data) {
        float weight;
        if (dataParser != null) {
            weight = dataParser.parseWeight(data);
        } else {
            weight = defaultParseWeight(data);
        }

        latestWeight = weight;
        latestRawData = data;

        if (mainHandler!=null){
            if (isFirstData) {
                isFirstData = false;
                mainHandler.post(() -> {
                    if (callback != null) {
                        callback.onWeightReceived(weight, data);
                    }
                });
            } else {
                if (throttleIntervalMs > 0) {
                    if (throttleScheduled.compareAndSet(false, true)) {
                        mainHandler.postDelayed(throttleRunnable, throttleIntervalMs);
                    }
                } else {
                    mainHandler.post(() -> {
                        if (callback != null) {
                            callback.onWeightReceived(weight, data);
                        }
                    });
                }
            }
        }


    }

    @SuppressLint("MissingPermission")
    private float defaultParseWeight(byte[] data) {
        try {
            String rawString = new String(data, "UTF-8");
            if (device != null && TextUtils.equals(device.getName(), "WF610A")) {
                String str = rawString.trim();

                if (str.contains("*8")){ //值不稳定时
                    str = str.replace("*8", "*0");
                }

                if (str.contains("*2")){//负数时
                    str = "0";
                }
                if (str.contains("*0")){
                    if (str.contains("\r")){//有多条数据返回时，取\r前面的的一条数据，并将最后一位0去掉
                        str = str.substring(0, str.indexOf("0\r")).replaceAll("\\D+", "");
                        str = new BigDecimal(str).toString();
                    }else {
                        str = str.replaceAll("\\D+", "");
                        str = str.substring(0,str.length()-1);
                    }
                }


                if (str.length() == 10 && str.contains("+0")){
                    str = str.substring(1,str.length()-3);
                }

                if (str.length() == 10){
                    str = str.substring(1,str.length()-4);
                }

                return Float.parseFloat(str);
            }else  if (device != null && TextUtils.equals(device.getName(), "BT-YH")) {
                String str = getBetweenEquals(rawString);
                String reversed = new StringBuilder(str).reverse().toString();
                return Float.parseFloat(reversed);
            }else {
                if (rawString.length() >= 8) {
                    String sub = rawString.substring(1, 8);
                    String reversed = new StringBuilder(sub).reverse().toString();
                    return Float.parseFloat(reversed);
                }
            }
        } catch (UnsupportedEncodingException | NumberFormatException | StringIndexOutOfBoundsException e) {
            Log.e(TAG, "默认解析失败", e);
        }
        return -1f;
    }

    private void postConnectionState(boolean connected) {
        if (mainHandler!=null){
            mainHandler.post(() -> {
                if (callback != null) callback.onConnectionStateChange(connected);
            });
        }
    }

    public static String getBetweenEquals(String str) {
        int first = str.indexOf('=');          // 第一个 = 的位置
        if (first == -1) return "";            // 没有等号
        int second = str.indexOf('=', first + 1); // 第二个 = 的位置
        if (second == -1) return "";            // 只有一个等号
        return str.substring(first + 1, second); // 提取两个等号之间的内容
    }


    private void postError(String error) {

        if (mainHandler!=null){
            mainHandler.post(() -> {
                if (callback != null) callback.onError(error);
            });
        }


    }
}