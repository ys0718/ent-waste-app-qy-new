package com.grkj.module_hardware.utils;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class UsbUtil {
    public UsbUtil() {
    }

    public static List<UsbDevice> getPrinterUsbDeviceList(Context context) {
        List<UsbDevice> list = getUsbDeviceList(context);
        if (list != null && !list.isEmpty()) {
            Iterator iterator = list.iterator();

            while(iterator.hasNext()) {
                UsbDevice usbDevice = (UsbDevice)iterator.next();
                if (!checkUsbDevicePidVid(usbDevice) && !checkUsbVid(usbDevice)) {
                    iterator.remove();
                }
            }

            return list;
        } else {
            return list;
        }
    }

    private static boolean checkUsbDevicePidVid(UsbDevice dev) {
        int pid = dev.getProductId();
        int vid = dev.getVendorId();
        KLog.e("checkUsbDevicePidVid:" + dev.getProductId());
        KLog.e("checkUsbDevicePidVid:" + dev.getVendorId());
        return vid == 10343 && pid == 5413 || vid == 34918 && pid == 256 || vid == 1137 && pid == 85 || vid == 6790 && pid == 30084 || vid == 26728 && pid == 256 || vid == 26728 && pid == 512 || vid == 26728 && pid == 256 || vid == 26728 && pid == 768 || vid == 26728 && pid == 1024 || vid == 26728 && pid == 1280 || vid == 26728 && pid == 1536 ||vid == 10343 && pid == 5442;
    }

    private static boolean checkUsbVid(UsbDevice usbDevice) {
        int[] xprinterUsbID = new int[]{1659, 1046, 7358, 1155, 8137, 1003, 11575, 1208, 22304, 26728, 11652};
        int vid = usbDevice.getVendorId();
        Log.i("USB", "usb info is " + usbDevice.toString());
        int[] var6 = xprinterUsbID;
        int var5 = xprinterUsbID.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            int id = var6[var4];
            if (vid == id) {
                return true;
            }
        }

        return false;
    }

    public static List<UsbDevice> getUsbDeviceList(Context context) {
        List<UsbDevice> temList = new ArrayList();
        UsbManager mUsbManager = getUsbManager(context);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator deviceIterator = deviceList.values().iterator();
        if (deviceIterator == null) {
            return temList;
        } else {
            while(deviceIterator.hasNext()) {
                UsbDevice device = (UsbDevice)deviceIterator.next();
                temList.add(device);
            }

            return temList;
        }
    }

    public static UsbManager getUsbManager(Context context) {
        return (UsbManager)context.getSystemService("usb");
    }
}