package com.grkj.module_hardware.printer;


import android.content.Context;

import com.dascom.dc2300.DsCardPrint;
import com.dascom.dc2300.DsCardPrintLib;
import com.dascom.print.PrintCommands.ESCPOS;
import com.dascom.print.PrintCommands.ZPL;
import com.example.tscdll.TSCActivity;

/**
 * Create by seny on 2023/11/29
 * Description: 打印机工具类
 **/
public class PrinterUtils {


    //得实打印类-USB
    private ZPL dkUsbPrinter;
    //得实打印机SDK-dk USB
    private DsCardPrintLib mDkUsbPrintLib = null;
    //得实打印机SDK-dp USB
    private DsCardPrintLib mDpUsbPrintLib = null;

    //得实打印类-蓝牙
    private ESCPOS bltPrinter;

    //得实打印类-蓝牙
    private ZPL zplBltPrinter;

    //得实打印类-DP USB
    private ESCPOS dpUsbPrinter;
    //得实打印机SDK-USB
    private DsCardPrintLib mBltPrintLib = null;

    private PrinterUtils() {
    }

    public static class PrinterUtilsInstance {
        private static final PrinterUtils INSTANCE = new PrinterUtils();
    }

    public static PrinterUtils getInstance() {
        return PrinterUtils.PrinterUtilsInstance.INSTANCE;
    }


    //初始化得实usb lib
    public ZPL initDkUsbPrintLib(Context mContext) {
//        if (mDkUsbPrintLib == null){
//            mDkUsbPrintLib = DsCardPrint.init(mContext).getInstance().usb().build().connection();
//        }
//
//        if (mDkUsbPrintLib.isConnected()) {
//            if (dkUsbPrinter == null) {
//                dkUsbPrinter = new ZPL(mDkUsbPrintLib);
//            }
//        }

        if (mDkUsbPrintLib == null){
            mDkUsbPrintLib = DsCardPrint.init(mContext).getInstance().usb().build().connection();
        }
        if (mDkUsbPrintLib.isConnected()) {
            dkUsbPrinter = new ZPL(mDkUsbPrintLib);
        }

        return dkUsbPrinter;
    }

    //初始化得实usb dp lib
    public ESCPOS initDpUsbPrintLib(Context mContext) {
        if (mDpUsbPrintLib == null) {
            mDpUsbPrintLib = DsCardPrint.init(mContext).getInstance().usb().build().connection();
        }

//        if (mDkUsbPrintLib!=null){
//            mDkUsbPrintLib.disconnection();
//            mDkUsbPrintLib = null;
//        }

//        mDpUsbPrintLib = DsCardPrint.init(mContext).getInstance().usb().build().connection();
        if (mDpUsbPrintLib.isConnected()) {
            if (dpUsbPrinter == null) {
                dpUsbPrinter = new ESCPOS(mDpUsbPrintLib);
            }
        }
        return dpUsbPrinter;
    }

    //销毁
    public void closeDpUsbPrinter() {
        if (dpUsbPrinter != null) {
            dpUsbPrinter = null;
        }
        if (mDpUsbPrintLib != null) {
            mDpUsbPrintLib.disconnection();
            mDpUsbPrintLib = null;
        }
    }

    //销毁
    public void closeDkUsbPrinter() {
//        if (dkUsbPrinter != null) {
//            dkUsbPrinter = null;
//        }
//        if (mDkUsbPrintLib != null) {
//            mDkUsbPrintLib.disconnection();
//            mDkUsbPrintLib = null;
//        }
    }

    //初始化得实蓝牙lib
    public ESCPOS initBtlPrintLib(Context mContext, String macAddress) {
        if (this.mBltPrintLib == null) {
            this.mBltPrintLib = DsCardPrint.init(mContext).bluetooth(macAddress).build().connection();
            setDkUsbPrintLib(this.mBltPrintLib);
        }

//        if (mBltPrintLib != null) {
//            mBltPrintLib.disconnection();
//            mBltPrintLib = null;
//            bltPrinter = null;
//        }
//
//        this.mBltPrintLib = DsCardPrint.init(mContext).bluetooth(macAddress).build().connection();
//        setDkUsbPrintLib(this.mBltPrintLib);
        if (this.mBltPrintLib.isConnected()) {
            if (this.bltPrinter == null) {
                this.bltPrinter = new ESCPOS(this.mBltPrintLib);
            }
        }
        return this.bltPrinter;
    }

    //初始化得实蓝牙lib
    public ZPL initZplBtlPrintLib(Context mContext, String macAddress) {
        if (this.mBltPrintLib == null) {
            this.mBltPrintLib = DsCardPrint.init(mContext).bluetooth(macAddress).build().connection();
            setDkUsbPrintLib(this.mBltPrintLib);
        }

        if (this.mBltPrintLib.isConnected()) {
            if (this.zplBltPrinter == null) {
                this.zplBltPrinter = new ZPL(this.mBltPrintLib);
            }
        }
        return this.zplBltPrinter;
    }



    public void initSkBltPrinter(){
        TSCActivity TscDll = new TSCActivity();
        TscDll.openport("00:19:0E:A0:04:E1");
        TscDll.downloadpcx("UL.PCX");
        TscDll.downloadbmp("Triangle.bmp");
        TscDll.downloadttf("ARIAL.TTF");
        TscDll.setup(70, 110, 4, 4, 0, 0, 0);
        TscDll.clearbuffer();
        TscDll.sendcommand("SET TEAR ON\n");
        TscDll.sendcommand("SET COUNTER @1 1\n");
        TscDll.sendcommand("@1 = \"0001\"\n");
        TscDll.sendcommand("TEXT 100,300,\"3\",0,1,1,@1\n");
        TscDll.sendcommand("PUTPCX 100,300,\"UL.PCX\"\n");
        TscDll.sendcommand("PUTBMP 100,520,\"Triangle.bmp\"\n");
        TscDll.sendcommand("TEXT 100,760,\"ARIAL.TTF\",0,15,15,\"THIS IS ARIAL FONT\"\n");
        TscDll.barcode(100, 100, "128", 100, 1, 0, 3, 3, "123456789");
        TscDll.printerfont(100, 250, "3", 0, 1, 1, "987654321");
//        String status = TscDll.printerstatus();
//TscDll.sendfile("zpl.txt");
        TscDll.printlabel(2, 1);
        TscDll.closeport(5000);
    }



    public DsCardPrintLib getDkUsbPrintLib() {
        return mDkUsbPrintLib;
    }

    public void setDkUsbPrintLib(DsCardPrintLib mUsbPrintLib) {
        this.mDkUsbPrintLib = mUsbPrintLib;
    }

    public DsCardPrintLib getBltPrintLib() {
        return mBltPrintLib;
    }

    public void setBltPrintLib(DsCardPrintLib mBltPrintLib) {
        this.mBltPrintLib = mBltPrintLib;
    }


    public void printDp385Content(){

    }

    //销毁
    public void closeBltPrinter() {
        if (mBltPrintLib != null) {
            mBltPrintLib.disconnection();
            mBltPrintLib = null;
        }

        if (bltPrinter != null) {
            bltPrinter = null;
        }

        if (zplBltPrinter != null) {
            zplBltPrinter = null;
        }
    }


}
