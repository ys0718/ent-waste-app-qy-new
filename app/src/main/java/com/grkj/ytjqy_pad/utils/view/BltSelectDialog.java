package com.grkj.ytjqy_pad.utils.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.grkj.ytjqy_pad.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by yusam on 2022/3/7.
 * 自定义的带圆角的对话框
 */

public class BltSelectDialog extends AlertDialog {


    private Context mContext;
    public OnConfirmListener onConfirmListener;
    private RecyclerView DeviceView;
    private BluetoothAdapter mBluetoothAdapter = null;
    public ArrayList<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();

    /**
     * 一个参数的构造方法
     *
     * @param context 上下文对象
     */
    public BltSelectDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_weight);
        //设置背景透明，不然会出现白色直角问题
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        //初始化布局控件
        initView();
        //确定和取消按钮的事件监听
        initEvent();
        //设置参数必须在show之后，不然没有效果
        WindowManager.LayoutParams params = getWindow().getAttributes();
        getWindow().setAttributes(params);
    }


    /**
     * 初始化布局文件及设置参数
     */
    private void initView() {
        DeviceView = findViewById(R.id.DeviceView);
        findViewById(R.id.ll_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 确定及取消点击事件
     */
    private void initEvent() {
        BlueInit();
        onResume();
    }


    // 初始化蓝牙
    private void BlueInit() {
        // 获取蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 请求开启蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity) mContext).startActivityForResult(enableBtIntent, 1);
        }
    }

    protected void onResume() {
        initRecycleView();
        // 将已配对的设备添加到列表中
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        deviceList.clear();
        if (pairedDevices.size() > 0) {
            String[] nameList = new String[pairedDevices.size()];
            int i = 0;
            for (BluetoothDevice device : pairedDevices) {
                deviceList.add(device);
                nameList[i] = device.getName() + "\n" + device.getAddress();
                i++;
            }
            adapter.notifyDataSetChanged();
        }
    }


    private CommonAdapter<BluetoothDevice> adapter;//企业列表适配器

    /**
     * 企业列表初始化
     */
    private void initRecycleView() {
        deviceList = new ArrayList<>();
        DeviceView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new CommonAdapter<BluetoothDevice>(mContext, R.layout.item_blt_info, deviceList) {
            @Override
            protected void convert(ViewHolder holder, final BluetoothDevice bean, final int position) {
                if (bean != null) {
                    holder.setText(R.id.tv_item1,  bean.getName() + "(" + bean.getAddress()+")");
                }
            }
        };
        adapter.setOnItemClickListener(new DeviceClick());
        DeviceView.setAdapter(adapter);
    }


    //事件按钮触发
    public class DeviceClick implements MultiItemTypeAdapter.OnItemClickListener {
        @Override
        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
            onConfirmListener.confirm(deviceList.get(position));
        }

        @Override
        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
            return false;
        }
    }

    public interface OnConfirmListener {
        public void confirm(BluetoothDevice device);
    }

}
