package com.grkj.ytjqy_pad.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.utils.common.BluetoothPermissionHelper;
import com.grkj.module_hardware.weight.SppBluetoothScale;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@SuppressLint("InflateParams")
public class MainActivity extends Activity {

    private static final UUID SERVICE_UUID = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb"); // 电池服务示例，请替换为实际称的服务UUID
    private static final UUID CHARACTERISTIC_UUID = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb"); // 示例，请替换为实际重量特征UUID
    private SppBluetoothScale scaleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scaleHelper = new SppBluetoothScale(getApplicationContext(), new SppBluetoothScale.Callback() {
            @Override
            public void onConnectionStateChange(boolean connected) {
                runOnUiThread(() -> {
                    if (connected) {
                        Toast.makeText(MainActivity.this, "已连接", Toast.LENGTH_SHORT).show();
                        KLog.e( "已连接");
                        // 连接成功后，开始接收重量
//                        scaleHelper.startReceivingWeight(SERVICE_UUID, CHARACTERISTIC_UUID, true);
                    } else {
                        KLog.e( "已断开");
//                        Toast.makeText(MainActivity.this, "已断开", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onWeightReceived(float weight, byte[] rawData) {
                // 在主线程回调
                Log.e("重量", "weight = " + weight);
                // 更新UI
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MainActivity.this, "错误: " + error, Toast.LENGTH_SHORT).show();
            }
        });


        bluetoothPermissionHelper = new BluetoothPermissionHelper(this, new BluetoothPermissionHelper.Callback() {
            @Override
            public void onPermissionsGranted(int requestCode) {
                // 例如：开始扫描、连接等

                BluetoothManager bluetoothmanger = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                BluetoothAdapter bluetoothadapter = bluetoothmanger.getAdapter();
                Set<BluetoothDevice> bondedDevices = bluetoothadapter.getBondedDevices();
                final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                for (BluetoothDevice device : bondedDevices) {
                    Map<String, String> map = new HashMap<String, String>();
                    KLog.e("device" + device.getName());
                    KLog.e("device" + device.getAddress());
                }

            }

            @Override
            public void onPermissionsDenied(int requestCode, List<String> deniedPermissions) {
                // 权限被拒绝，可根据需求提示用户或降级处理
                // 可以检查是否勾选“不再询问”，并引导用户去设置页面
            }
        });
        bluetoothPermissionHelper.requestPermissions(REQUEST_BLUETOOTH_PERMS);


        String mac = "98:DA:20:09:73:B8"; // 替换为实际设备MAC
        scaleHelper.connect(mac);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scaleHelper.disconnect();
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
