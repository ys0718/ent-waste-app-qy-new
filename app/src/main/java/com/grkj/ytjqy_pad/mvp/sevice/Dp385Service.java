package com.grkj.ytjqy_pad.mvp.sevice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.grkj.module_hardware.printer.PrinterUtils;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.utils.common.SPUtils;

/**
 * Create by seny on 2024/9/13
 * Description: dp385打印机服务
 **/
public class Dp385Service extends Service {

    private Context mContext;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        connectDevice();
        return super.onStartCommand(intent, flags, startId);
    }

    private void connectDevice() {
        String mac = (String) SPUtils.get(mContext, Constants.PRINTER_MAC, "");
        PrinterUtils.getInstance().initBtlPrintLib(mContext, mac);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
