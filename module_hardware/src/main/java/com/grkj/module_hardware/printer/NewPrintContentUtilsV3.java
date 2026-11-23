package com.grkj.module_hardware.printer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android_print_sdk.usb.USBPrinter;
import com.dascom.print.PrintCommands.ESCPOS;
import com.dascom.print.PrintCommands.ZPL;
import com.dascom.print.Utils.CanvasUtils;
import com.dascom.print.Utils.HalfTone;
import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.grkj.module_hardware.R;
import com.grkj.module_hardware.qrcode.QrCodeUtils;
import com.grkj.module_hardware.utils.DeviceUtils;
import com.socks.library.KLog;

import net.posprinter.utils.BitmapToByteData;
import net.posprinter.utils.DataForSendToPrinterTSC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

import zpCPCLSDK.zpCPCLSDK.zp_cpcl_BluetoothPrinter;

public class NewPrintContentUtilsV3 {
    private static String TAG = "NewPrintContentUtils";

    //打印demo
    public static void print104Label(USBPrinter iPrinter) {
        iPrinter.setPage(800, 800);
        iPrinter.receive();
        iPrinter.printText("CENTER\r\n");
        iPrinter.drawText(0, 24, 0, 0, 0, "危险废物");
        iPrinter.printText("LEFT\r\n");
        iPrinter.drawBox(2, 0, 24, 800, 784);
        iPrinter.drawText(0, 24, 0, 16, 40, "主要成分：汞、铅、锌");
        iPrinter.drawText(0, 24, 0, 16, 90, "化学名称：螯合固化飞");
        iPrinter.drawText(0, 24, 0, 16, 140, "危废八位码：772-002-18");
        iPrinter.drawLine(2, 0, 168, 500, 168);
        iPrinter.drawText(0, 24, 0, 16, 176, "危险情况：毒性");
        iPrinter.drawLine(2, 0, 280, 500, 280);
        try {
            iPrinter.drawText(16, 288, 500, 88, "安全措施：对泄露区域进行隔离，设置警示牌，避免沾染飞灰，应急救援人员穿戴后套、口罩等。", 0, 0, 0, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        iPrinter.drawLine(2, 0, 384, 800, 384);
        try {
            iPrinter.drawText(16, 392, 500, 50, "危废产生单位：瀚蓝（蓝平）固废处理有限公司。", 0, 0, 0, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        iPrinter.drawText(0, 24, 0, 16, 466, "地址：福建省南平市建阳市回姚村");
        iPrinter.drawText(0, 24, 0, 16, 514, "电话：13333333333  联系人：翁书霆");
        iPrinter.drawText(0, 24, 0, 16, 562, "批次：瀚蓝蓝平-PW-20200304-0002");
        iPrinter.drawText(0, 24, 0, 16, 610, "数量：0.011吨");
        iPrinter.drawText(0, 24, 0, 16, 658, "产生日期：2020-03-04 14:49");

        iPrinter.drawText(0, 24, 0, 600, 40, "危险类别");
        iPrinter.drawLine(2, 508, 24, 508, 784);
        iPrinter.drawQrCode(516, 442, 6, 2, "http://gj.hbwgj.net/web/storage/batchInfoNew.html?v=1&a=350703&b=%E7%80%9A%E8%93%9D%E5%8D%97%E5%B9%B3-PW-20200304-0002", 0);
        iPrinter.labelPrint(0, 1);
    }


    /**
     * 标签打印（第一款）
     *
     * @return
     */
    public static Vector<Byte> addLabel(LabelInfoBeanV3 bean) {
        LabelCommand tsc = new LabelCommand();
//        tsc.addFeed(3);
        // 设置标签尺寸宽高，按照实际尺寸设置 单位mm
        tsc.addSize(100, 100);
        // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0 单位mm
        tsc.addGap(5);
        // 设置打印方向
//        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);
        // 开启带Response的打印，用于连续打印
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);
        // 设置原点坐标
        tsc.addReference(0, 0);

        // 撕纸模式开启
        tsc.addTear(EscCommand.ENABLE.OFF);
        // 切纸模式开启
        tsc.addCutter(EscCommand.ENABLE.OFF);
        // 清除打印缓冲区
        tsc.addCls();

        //设置浓度
        tsc.addDensity(LabelCommand.DENSITY.DNESITY9);


//        int height = 52;
//        int startY = 13;
//        int leftX = 30;
//        int rightX = 420;


        int height = 50;
        int mulHeight = 88;
        int startY = 65;
        int leftX = 160;
        int rightX = 420;
        int newHeight = 0;


        //溯源二维码
//        tsc.addQRCode(670,40,LabelCommand.EEC.LEVEL_L,2,LabelCommand.ROTATION.ROTATION_0,bean.HISTORY_CODE);

        newHeight = startY + height;

        // 废物名称
        tsc.addText(leftX, newHeight, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                bean.FWXXMC);


        newHeight = startY + height * 2;

        //废物类别
        tsc.addText(leftX, newHeight, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                bean.FWDLBH + "-" + bean.FWDLMC);


        newHeight = startY + height * 3;
        // 废物代码
        tsc.addText(leftX, newHeight,
                LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, bean.FWXLBH);


        // 废物形态
        tsc.addText(rightX, newHeight,
                LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, bean.WLXZ);


        newHeight = startY + height * 4;


//        tsc.addText(leftX,newHeight,
//                LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,LabelCommand.ROTATION.ROTATION_0,LabelCommand.FONTMUL.MUL_1,LabelCommand.FONTMUL.MUL_1,bean.ZYWRCF);


        // 主要成分
        if (!TextUtils.isEmpty(bean.ZYWRCF) && bean.ZYWRCF.length() > 36) {
            String strSta = bean.ZYWRCF.substring(0, 18);
            String strEnd = bean.ZYWRCF.substring(18, 35) + "..";

            tsc.addText(leftX, newHeight,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, strSta);
            //换行
            tsc.addText(leftX, newHeight + 40,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, strEnd);

        } else if (!TextUtils.isEmpty(bean.ZYWRCF) && bean.ZYWRCF.length() > 18) {
            String strSta = bean.ZYWRCF.substring(0, 18);
            String strEnd = bean.ZYWRCF.substring(18);

            tsc.addText(leftX, newHeight,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, strSta);
            //换行
            tsc.addText(leftX, newHeight + 40,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, strEnd);

        } else {
            tsc.addText(leftX, newHeight,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, bean.ZYWRCF);
        }


        if (!TextUtils.isEmpty(bean.WXCF_DX)) {
            // 毒性
            tsc.addText(630, newHeight,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "√");
        }


        if (!TextUtils.isEmpty(bean.WXCF_FYX)) {
            // 反应性
            tsc.addText(720, newHeight,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "√");
        }


        // 毒性
        tsc.addText(628, newHeight,
                LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "□");


        // 反应性
        tsc.addText(718, newHeight,
                LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "□");


        newHeight = newHeight + mulHeight;

        if (!TextUtils.isEmpty(bean.WXCF_YRX)) {
            // 易燃性
            tsc.addText(630, newHeight + 20,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "√");

        }

        if (!TextUtils.isEmpty(bean.WXCF_FSX)) {
            // 腐蚀性
            tsc.addText(720, newHeight + 20,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "√");
        }


        // 易燃性
        tsc.addText(628, newHeight + 20,
                LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "□");


        // 腐蚀性
        tsc.addText(718, newHeight + 20,
                LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "□");


        // 有害成分
        if (!TextUtils.isEmpty(bean.WXCF) && bean.WXCF.length() > 36) {
            String strSta = bean.WXCF.substring(0, 18);
            String strEnd = bean.WXCF.substring(18, 35) + "..";

            tsc.addText(leftX, newHeight,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, strSta);
            //换行
            tsc.addText(leftX, newHeight + 40,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, strEnd);

        } else if (!TextUtils.isEmpty(bean.WXCF) && bean.WXCF.length() > 18) {
            String strSta = bean.WXCF.substring(0, 18);
            String strEnd = bean.WXCF.substring(18);

            tsc.addText(leftX, newHeight,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, strSta);
            //换行
            tsc.addText(leftX, newHeight + 40,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, strEnd);

        } else {
            tsc.addText(leftX, newHeight,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, bean.WXCF);
        }


        newHeight = newHeight + mulHeight;


        // 注意事项
        if (!TextUtils.isEmpty(bean.ZYSX) && bean.ZYSX.length() > 50) {
            String strSta = bean.ZYSX.substring(0, 25);
            String strEnd = bean.ZYSX.substring(25, 49) + "..";

            tsc.addText(leftX, newHeight,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, strSta);
            //换行
            tsc.addText(leftX, newHeight + 40,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, strEnd);

        } else if (!TextUtils.isEmpty(bean.ZYSX) && bean.ZYSX.length() > 25) {
            String strSta = bean.ZYSX.substring(0, 25);
            String strEnd = bean.ZYSX.substring(25);

            tsc.addText(leftX, newHeight,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, strSta);
            //换行
            tsc.addText(leftX, newHeight + 40,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, strEnd);

        } else {
            tsc.addText(leftX, newHeight,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, bean.ZYSX);
        }


        newHeight = newHeight + mulHeight;

        // 数字识别码
        tsc.addText(leftX + 30, newHeight,
                LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, bean.QRCODE_VALUE);

        newHeight = newHeight + height;

        //产生(收集)单位
        String entername = bean.ENTERNAME;
        if (!TextUtils.isEmpty(bean.ENTERNAME) && bean.ENTERNAME.length() > 17) {
            entername = bean.ENTERNAME.substring(0, 16) + "..";
        }
        tsc.addText(leftX + 60, newHeight,
                LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, entername);

        //绘制数字识别码二维码
//        tsc.addQRCode(600,newHeight+10,LabelCommand.EEC.LEVEL_L,getQrcodeWidth(bean.QRCODE_VALUE_URL,6),LabelCommand.ROTATION.ROTATION_0,bean.QRCODE_VALUE_URL);

        Bitmap qrCodeBitmap = QrCodeUtils.createQRImage(bean.QRCODE_VALUE_URL, 220, 220);
        tsc.addBitmap(570, newHeight - 10, LabelCommand.BITMAP_MODE.XOR, 220, qrCodeBitmap);
        newHeight = newHeight + height;

        //联系人
        tsc.addText(leftX + 100, newHeight,
                LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, bean.LXRNAME + "/" + bean.LXRPHONE);


        newHeight = newHeight + height;

        // 产生日期
        if (!TextUtils.isEmpty(bean.RKSJ)) {
            String date = bean.RKSJ.substring(0, 11);
            tsc.addText(leftX, newHeight,
                    LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, date);
        }


        // 废物总量
        tsc.addText(rightX, newHeight,
                LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, bean.RKL + bean.JLDW);


        newHeight = newHeight + height;
        //备注
        String newStr = bean.REMARK;
        if (!TextUtils.isEmpty(bean.REMARK) && bean.REMARK.length() > 21) {
            newStr = bean.REMARK.substring(0, 20) + "..";
        }

        tsc.addText(leftX - 40, newHeight,
                LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, newStr);

        // 打印标签
        tsc.addPrint(1, 1);

        Vector<Byte> datas = tsc.getCommand();
        // 发送数据
        return datas;
    }


    //打印标签（第二款）
    public static void printLabel(USBPrinter iPrinter, LabelInfoBeanV3 bean) {
        if (iPrinter == null) {
            return;
        }

        iPrinter.setPage(1000, 1000);

        int fontNaID = 24;
        int height = 50;
        int startY = 20;
        int leftX = 160;
        int rightX = 420;
        int mulHeight = 88;
        int newHeight = 0;


        //溯源二维码
//        iPrinter.drawQrCode(600,10,5,2,bean.HISTORY_CODE,0);

        newHeight = startY + height;

        // 废物名称
        iPrinter.drawText(0, fontNaID, 0, leftX, newHeight,
                bean.FWXXMC);


        newHeight = startY + height * 2;

        //废物类别
        iPrinter.drawText(0, fontNaID, 0, leftX, newHeight,
                bean.FWDLBH + "-" + bean.FWDLMC);


        newHeight = startY + height * 3;
        // 废物代码
        iPrinter.drawText(0, fontNaID, 0, leftX, newHeight,
                bean.FWXLBH);


        // 废物形态
        iPrinter.drawText(0, fontNaID, 0, rightX, newHeight,
                bean.WLXZ);


        newHeight = startY + height * 4;


        // 主要成分
//        iPrinter.drawText(0,fontNaID,0,leftX,newHeight,
//                bean.ZYWRCF);


        if (!TextUtils.isEmpty(bean.ZYWRCF) && bean.ZYWRCF.length() > 36) {
            String strSta = bean.ZYWRCF.substring(0, 18);
            String strEnd = bean.ZYWRCF.substring(18, 35) + "..";

            iPrinter.drawText(0, fontNaID, 0, leftX, newHeight,
                    strSta);
            //换行
            iPrinter.drawText(0, fontNaID, 0, leftX, newHeight + 40,
                    strEnd);

        } else if (!TextUtils.isEmpty(bean.ZYWRCF) && bean.ZYWRCF.length() > 18) {
            String strSta = bean.ZYWRCF.substring(0, 18);
            String strEnd = bean.ZYWRCF.substring(18);

            iPrinter.drawText(0, fontNaID, 0, leftX, newHeight,
                    strSta);
            //换行
            iPrinter.drawText(0, fontNaID, 0, leftX, newHeight + 40,
                    strEnd);

        } else {
            iPrinter.drawText(0, fontNaID, 0, leftX, newHeight,
                    bean.ZYWRCF);
        }


        if (!TextUtils.isEmpty(bean.WXCF_DX)) {
            // 毒性
            iPrinter.drawText(0, fontNaID, 0, 630, newHeight,
                    "√");
        }


        if (!TextUtils.isEmpty(bean.WXCF_FYX)) {
            // 反应性
            iPrinter.drawText(0, fontNaID, 0, 720, newHeight,
                    "√");
        }


        // 毒性
        iPrinter.drawText(0, fontNaID, 0, 628, newHeight,
                "□");


        // 反应性
        iPrinter.drawText(0, fontNaID, 0, 718, newHeight,
                "□");


        newHeight = newHeight + mulHeight;

        if (!TextUtils.isEmpty(bean.WXCF_YRX)) {
            // 易燃性
            iPrinter.drawText(0, fontNaID, 0, 630, newHeight + 20,
                    "√");

        }

        if (!TextUtils.isEmpty(bean.WXCF_FSX)) {
            // 腐蚀性
            iPrinter.drawText(0, fontNaID, 0, 720, newHeight + 20,
                    "√");
        }


        // 易燃性
        iPrinter.drawText(0, fontNaID, 0, 628, newHeight + 20,
                "□");


        // 腐蚀性
        iPrinter.drawText(0, fontNaID, 0, 718, newHeight + 20,
                "□");


        // 有害成分
        if (!TextUtils.isEmpty(bean.WXCF) && bean.WXCF.length() > 36) {
            String strSta = bean.WXCF.substring(0, 18);
            String strEnd = bean.WXCF.substring(18, 35) + "..";

            iPrinter.drawText(0, fontNaID, 0, leftX, newHeight,
                    strSta);
            //换行
            iPrinter.drawText(0, fontNaID, 0, leftX, newHeight + 40,
                    strEnd);

        } else if (!TextUtils.isEmpty(bean.WXCF) && bean.WXCF.length() > 18) {
            String strSta = bean.WXCF.substring(0, 18);
            String strEnd = bean.WXCF.substring(18);

            iPrinter.drawText(0, fontNaID, 0, leftX, newHeight,
                    strSta);
            //换行
            iPrinter.drawText(0, fontNaID, 0, leftX, newHeight + 40,
                    strEnd);

        } else {
            iPrinter.drawText(0, fontNaID, 0, leftX, newHeight,
                    bean.WXCF);
        }


        newHeight = newHeight + mulHeight;


        // 注意事项
        if (!TextUtils.isEmpty(bean.ZYSX) && bean.ZYSX.length() > 50) {
            String strSta = bean.ZYSX.substring(0, 25);
            String strEnd = bean.ZYSX.substring(25, 49) + "..";

            iPrinter.drawText(0, fontNaID, 0, leftX, newHeight,
                    strSta);
            //换行
            iPrinter.drawText(0, fontNaID, 0, leftX, newHeight + 40,
                    strEnd);

        } else if (!TextUtils.isEmpty(bean.ZYSX) && bean.ZYSX.length() > 25) {
            String strSta = bean.ZYSX.substring(0, 25);
            String strEnd = bean.ZYSX.substring(25);

            iPrinter.drawText(0, fontNaID, 0, leftX, newHeight,
                    strSta);
            //换行
            iPrinter.drawText(0, fontNaID, 0, leftX, newHeight + 40,
                    strEnd);

        } else {
            iPrinter.drawText(0, fontNaID, 0, leftX, newHeight,
                    bean.ZYSX);
        }


        newHeight = newHeight + mulHeight;

        // 数字识别码
        iPrinter.drawText(0, fontNaID, 0, leftX + 30, newHeight,
                bean.QRCODE_VALUE);

        newHeight = newHeight + height;

        //产生(收集)单位
        String entername = bean.ENTERNAME;
        if (!TextUtils.isEmpty(bean.ENTERNAME) && bean.ENTERNAME.length() > 17) {
            entername = bean.ENTERNAME.substring(0, 16) + "..";
        }
        iPrinter.drawText(0, fontNaID, 0, leftX + 60, newHeight,
                entername);

        int width = getQrcodeWidth(bean.QRCODE_VALUE_URL, 5);
        //绘制数字识别码二维码
        iPrinter.drawQrCode(600, newHeight + 10, width, width, bean.QRCODE_VALUE_URL, 0);

//        iPrinter.drawQrCode(600,startY+height * 10+13,5,5,bean.QRCODE_VALUE,0);
        newHeight = newHeight + height;

        //联系人
        iPrinter.drawText(0, fontNaID, 0, leftX + 100, newHeight,
                bean.LXRNAME + "/" + bean.LXRPHONE);


        newHeight = newHeight + height;

        // 产生日期
        if (!TextUtils.isEmpty(bean.RKSJ)) {
            String date = bean.RKSJ.substring(0, 11);
            iPrinter.drawText(0, fontNaID, 0, leftX, newHeight,
                    date);
        }


        // 废物总量
        iPrinter.drawText(0, fontNaID, 0, rightX, newHeight,
                bean.RKL + bean.JLDW);


        newHeight = newHeight + height;
        //备注
        String newStr = bean.REMARK;
        if (!TextUtils.isEmpty(bean.REMARK) && bean.REMARK.length() > 21) {
            newStr = bean.REMARK.substring(0, 20) + "..";
        }

        iPrinter.drawText(0, fontNaID, 0, leftX - 40, newHeight,
                newStr);


        iPrinter.labelPrint(0, 1);
    }


    //打印标签（第三款）
    public static ArrayList<byte[]> printLabel3(LabelInfoBeanV3 bean) {

//        int height = 52;
//        int startY = 23;
//        int leftX = 30;
//        int rightX = 420;
//        int fontNaID = 24;
        String fontName = "TSS24.BF2";


        int height = 50;
        int mulHeight = 92;
        int startY = 60;
        int leftX = 160;
        int rightX = 440;
        int newHeight = 0;

        ArrayList<byte[]> list = new ArrayList<byte[]>();


        //通过工具类得到一个指令的byte[]数据,以文本为例
        //首先得设置size标签尺寸,宽60mm,高30mm,也可以调用以dot或inch为单位的方法具体换算参考编程手册
        byte[] data0 = DataForSendToPrinterTSC.sizeBymm(100,
                100);

        list.add(data0);
        //设置Gap,同上
        list.add(DataForSendToPrinterTSC.gapBymm(2, 0));
        //清除缓存
        list.add(DataForSendToPrinterTSC.cls());
        list.add(DataForSendToPrinterTSC.direction(1));


//        list.add(DataForSendToPrinterTSC.qrCode(660,40,"M",2,"A",0,bean.HISTORY_CODE));

        newHeight = startY + height;

        // 废物名称
        list.add(DataForSendToPrinterTSC.text(leftX, newHeight, fontName, 0, 1, 1,
                bean.FWXXMC));


        newHeight = startY + height * 2;

        //废物类别
        list.add(DataForSendToPrinterTSC.text(leftX, newHeight, fontName, 0, 1, 1,
                bean.FWDLBH + "-" + bean.FWDLMC));


        newHeight = startY + height * 3;
        // 废物代码
        list.add(DataForSendToPrinterTSC.text(leftX, newHeight,
                fontName, 0, 1, 1, bean.FWXLBH));


        // 废物形态
        list.add(DataForSendToPrinterTSC.text(rightX, newHeight,
                fontName, 0, 1, 1, bean.WLXZ));


        newHeight = startY + height * 4;


//        // 主要成分
//        list.add(DataForSendToPrinterTSC.text(leftX,newHeight,
//                fontName,0,1,1,bean.ZYWRCF));


        // 主要成分
        if (!TextUtils.isEmpty(bean.ZYWRCF) && bean.ZYWRCF.length() > 36) {
            String strSta = bean.ZYWRCF.substring(0, 18);
            String strEnd = bean.ZYWRCF.substring(18, 35) + "..";

            list.add(DataForSendToPrinterTSC.text(leftX, newHeight,
                    fontName, 0, 1, 1, strSta));
            //换行
            list.add(DataForSendToPrinterTSC.text(leftX, newHeight + 40,
                    fontName, 0, 1, 1, strEnd));

        } else if (!TextUtils.isEmpty(bean.ZYWRCF) && bean.ZYWRCF.length() > 18) {
            String strSta = bean.ZYWRCF.substring(0, 18);
            String strEnd = bean.ZYWRCF.substring(18);

            list.add(DataForSendToPrinterTSC.text(leftX, newHeight,
                    fontName, 0, 1, 1, strSta));
            //换行
            list.add(DataForSendToPrinterTSC.text(leftX, newHeight + 40,
                    fontName, 0, 1, 1, strEnd));

        } else {
            list.add(DataForSendToPrinterTSC.text(leftX, newHeight,
                    fontName, 0, 1, 1, bean.ZYWRCF));
        }


        if (!TextUtils.isEmpty(bean.WXCF_DX)) {
            // 毒性
            list.add(DataForSendToPrinterTSC.text(630, newHeight,
                    fontName, 0, 1, 1, "√"));
        }


        if (!TextUtils.isEmpty(bean.WXCF_FYX)) {
            // 反应性
            list.add(DataForSendToPrinterTSC.text(720, newHeight,
                    fontName, 0, 1, 1, "√"));
        }


        // 毒性
        list.add(DataForSendToPrinterTSC.text(628, newHeight,
                fontName, 0, 1, 1, "□"));


        // 反应性
        list.add(DataForSendToPrinterTSC.text(718, newHeight,
                fontName, 0, 1, 1, "□"));


        newHeight = newHeight + mulHeight;

        if (!TextUtils.isEmpty(bean.WXCF_YRX)) {
            // 易燃性
            list.add(DataForSendToPrinterTSC.text(630, newHeight + 20,
                    fontName, 0, 1, 1, "√"));

        }

        if (!TextUtils.isEmpty(bean.WXCF_FSX)) {
            // 腐蚀性
            list.add(DataForSendToPrinterTSC.text(720, newHeight + 20,
                    fontName, 0, 1, 1, "√"));
        }


        // 易燃性
        list.add(DataForSendToPrinterTSC.text(628, newHeight + 20,
                fontName, 0, 1, 1, "□"));


        // 腐蚀性
        list.add(DataForSendToPrinterTSC.text(718, newHeight + 20,
                fontName, 0, 1, 1, "□"));


        // 有害成分
        if (!TextUtils.isEmpty(bean.WXCF) && bean.WXCF.length() > 36) {
            String strSta = bean.WXCF.substring(0, 18);
            String strEnd = bean.WXCF.substring(18, 35) + "..";

            list.add(DataForSendToPrinterTSC.text(leftX, newHeight,
                    fontName, 0, 1, 1, strSta));
            //换行
            list.add(DataForSendToPrinterTSC.text(leftX, newHeight + 40,
                    fontName, 0, 1, 1, strEnd));

        } else if (!TextUtils.isEmpty(bean.WXCF) && bean.WXCF.length() > 18) {
            String strSta = bean.WXCF.substring(0, 18);
            String strEnd = bean.WXCF.substring(18);

            list.add(DataForSendToPrinterTSC.text(leftX, newHeight,
                    fontName, 0, 1, 1, strSta));
            //换行
            list.add(DataForSendToPrinterTSC.text(leftX, newHeight + 40,
                    fontName, 0, 1, 1, strEnd));

        } else {
            list.add(DataForSendToPrinterTSC.text(leftX, newHeight,
                    fontName, 0, 1, 1, bean.WXCF));
        }


        newHeight = newHeight + mulHeight;


        // 注意事项
        if (!TextUtils.isEmpty(bean.ZYSX) && bean.ZYSX.length() > 50) {
            String strSta = bean.ZYSX.substring(0, 25);
            String strEnd = bean.ZYSX.substring(25, 49) + "..";

            list.add(DataForSendToPrinterTSC.text(leftX, newHeight,
                    fontName, 0, 1, 1, strSta));
            //换行
            list.add(DataForSendToPrinterTSC.text(leftX, newHeight + 40,
                    fontName, 0, 1, 1, strEnd));

        } else if (!TextUtils.isEmpty(bean.ZYSX) && bean.ZYSX.length() > 25) {
            String strSta = bean.ZYSX.substring(0, 25);
            String strEnd = bean.ZYSX.substring(25);

            list.add(DataForSendToPrinterTSC.text(leftX, newHeight,
                    fontName, 0, 1, 1, strSta));
            //换行
            list.add(DataForSendToPrinterTSC.text(leftX, newHeight + 40,
                    fontName, 0, 1, 1, strEnd));

        } else {
            list.add(DataForSendToPrinterTSC.text(leftX, newHeight,
                    fontName, 0, 1, 1, bean.ZYSX));
        }


        newHeight = newHeight + mulHeight;

        // 数字识别码
        list.add(DataForSendToPrinterTSC.text(leftX + 30, newHeight,
                fontName, 0, 1, 1, bean.QRCODE_VALUE));

        newHeight = newHeight + height;


        //产生(收集)单位

        String entername = bean.ENTERNAME;

        if (!TextUtils.isEmpty(bean.ENTERNAME) && bean.ENTERNAME.length() > 14) {
            entername = bean.ENTERNAME.substring(0, 13) + "..";
        }

        list.add(DataForSendToPrinterTSC.text(leftX + 60, newHeight,
                fontName, 0, 1, 1, entername));


//        list.add(DataForSendToPrinterTSC.qrCode(590,newHeight+10,"M",3,"A",0,bean.QRCODE_VALUE));


        Bitmap qrCodeBitmap = QrCodeUtils.createQRImage(bean.QRCODE_VALUE_URL, 220, 220);
        list.add(DataForSendToPrinterTSC.bitmap(560, newHeight - 20, 1, qrCodeBitmap, BitmapToByteData.BmpType.Grey));


        newHeight = newHeight + height;

        //联系人
        list.add(DataForSendToPrinterTSC.text(leftX + 100, newHeight,
                fontName, 0, 1, 1, bean.LXRNAME + "/" + bean.LXRPHONE));


        newHeight = newHeight + height;

        // 产生日期
        if (!TextUtils.isEmpty(bean.RKSJ)) {
            String date = bean.RKSJ.substring(0, 11);
            list.add(DataForSendToPrinterTSC.text(leftX, newHeight,
                    fontName, 0, 1, 1, date));
        }


        // 废物总量
        list.add(DataForSendToPrinterTSC.text(rightX, newHeight,
                fontName, 0, 1, 1, bean.RKL + bean.JLDW));


        newHeight = newHeight + height;
        //备注
        String newStr = bean.REMARK;
        if (!TextUtils.isEmpty(bean.REMARK) && bean.REMARK.length() > 21) {
            newStr = bean.REMARK.substring(0, 20) + "..";
        }

        list.add(DataForSendToPrinterTSC.text(leftX - 40, newHeight,
                fontName, 0, 1, 1, newStr));


        //打印
        list.add(DataForSendToPrinterTSC.print(1));

        return list;
    }

    private static int getQrcodeWidth(String qrcodeUrl, int normalWidth) {
        int width = normalWidth;
        if (qrcodeUrl.contains("http")) {
            width = 4;
        }
        return width;
    }


    //得实标签蓝牙打印内容
    public static void dsPrint(ESCPOS iPrinter, LabelInfoBeanV3 bean) {
//        iPrinter.pageSetup(1000,800);

        iPrinter.setCharacterSize(1, 1);
        iPrinter.setKanjiMode();

        int height = 100;
        int startY = 32;
        int leftX = 150;
        int rightX = 420;
        int fontSize = 2;
        int rotate = 180;
        int bold = 0;
        int newHeight = 0;
        int mulHeight = 92;

        iPrinter.setLineSpacing(height);
        iPrinter.setEmphasizedMode(true);
        iPrinter.setPrintAreaWidth(1000);

        iPrinter.printLineFeed();

        newHeight = startY + height;


        bean.FWXXMC = "废物名称";


        // 废物名称
        iPrinter.printText(bean.FWXXMC, leftX, newHeight);//        bean.FWXXMC,fontSize,rotate,bold,false,false,0);


        newHeight = startY + height * 2;

        bean.FWXXMC = "废物类别";
        bean.FWDLMC = "3";
        //废物类别
        iPrinter.printText(bean.FWDLBH + "-" + bean.FWDLMC, leftX, newHeight);//        bean.FWDLBH+"-"+bean.FWDLMC,fontSize,rotate,bold,false,false,0);

        bean.FWXLBH = "废物代码";
        newHeight = startY + height * 3;
        // 废物代码
        iPrinter.printText(bean.FWXLBH, leftX, newHeight);//        bean.FWXLBH,fontSize,rotate,bold,false,false,0);

        bean.WLXZ = "废物形态";
        // 废物形态
        iPrinter.printText(bean.WLXZ, rightX, newHeight);//        bean.WLXZ,fontSize,rotate,bold,false,false,0);

        newHeight = startY + height * 4;

        bean.ZYWRCF = "主要成分";
        // 主要成分
        iPrinter.printText(bean.ZYWRCF, leftX, newHeight);//        bean.ZYWRCF,fontSize,rotate,bold,false,false,0);

        if (!TextUtils.isEmpty(bean.WXCF_DX)) {
            // 毒性
            iPrinter.printText("√", 632, newHeight);
        }


        if (!TextUtils.isEmpty(bean.WXCF_FYX)) {
            // 反应性
            iPrinter.printText("√", 722, newHeight);
        }


        // 毒性
        iPrinter.printText("□", 628, newHeight);


        // 反应性
        iPrinter.printText("□", 718, newHeight);


        newHeight = newHeight + mulHeight;

        if (!TextUtils.isEmpty(bean.WXCF_YRX)) {
            // 易燃性
            iPrinter.printText("√", 632, newHeight + 20);//            "√",fontSize,rotate,bold,false,false,0);

        }

        if (!TextUtils.isEmpty(bean.WXCF_FSX)) {
            // 腐蚀性
            iPrinter.printText("√", 722, newHeight + 20);//            "√",fontSize,rotate,bold,false,false,0);
        }


        // 易燃性
        iPrinter.printText("□", 628, newHeight + 20);//        "□",fontSize,rotate,bold,false,false,0);


        // 腐蚀性
        iPrinter.printText("□", 718, newHeight + 20);//        "□",fontSize,rotate,bold,false,false,0);


        bean.WXCF = "有害成分";
        // 有害成分
        if (!TextUtils.isEmpty(bean.WXCF) && bean.WXCF.length() > 36) {
            String strSta = bean.WXCF.substring(0, 18);
            String strEnd = bean.WXCF.substring(18, 35) + "..";

            iPrinter.printText(strSta, leftX, newHeight);//            strSta,fontSize,rotate,bold,false,false,0);
            //换行
            iPrinter.printText(strEnd, leftX, newHeight + 40);//            strEnd,fontSize,rotate,bold,false,false,0);

        } else if (!TextUtils.isEmpty(bean.WXCF) && bean.WXCF.length() > 18) {
            String strSta = bean.WXCF.substring(0, 18);
            String strEnd = bean.WXCF.substring(18);

            iPrinter.printText(strSta, leftX, newHeight);//            strSta,fontSize,rotate,bold,false,false,0);
            //换行
            iPrinter.printText(strEnd, leftX, newHeight + 40);//            strEnd,fontSize,rotate,bold,false,false,0);

        } else {
            iPrinter.printText(bean.WXCF, leftX, newHeight);//            bean.WXCF,fontSize,rotate,bold,false,false,0);
        }


        newHeight = newHeight + mulHeight;

        bean.ZYSX = "注意事项";
        // 注意事项
        if (!TextUtils.isEmpty(bean.ZYSX) && bean.ZYSX.length() > 50) {
            String strSta = bean.ZYSX.substring(0, 25);
            String strEnd = bean.ZYSX.substring(25, 49) + "..";

            iPrinter.printText(strSta, leftX, newHeight);//            strSta,fontSize,rotate,bold,false,false,0);
            //换行
            iPrinter.printText(strEnd, leftX, newHeight + 40);//            strEnd,fontSize,rotate,bold,false,false,0);

        } else if (!TextUtils.isEmpty(bean.ZYSX) && bean.ZYSX.length() > 25) {
            String strSta = bean.ZYSX.substring(0, 25);
            String strEnd = bean.ZYSX.substring(25);

            iPrinter.printText(strSta, leftX, newHeight);//            strSta,fontSize,rotate,bold,false,false,0);
            //换行
            iPrinter.printText(strEnd, leftX, newHeight + 40);//            strEnd,fontSize,rotate,bold,false,false,0);

        } else {
            iPrinter.printText(bean.ZYSX, leftX, newHeight);//            bean.ZYSX,fontSize,rotate,bold,false,false,0);
        }


        newHeight = newHeight + mulHeight;

        bean.QRCODE_VALUE = "数字识别码";
        // 数字识别码
        iPrinter.printText(bean.QRCODE_VALUE, leftX + 30, newHeight);

        newHeight = newHeight + height;


        //产生(收集)单位
        bean.ENTERNAME = "产生(收集)单位";
        String entername = bean.ENTERNAME;
        if (!TextUtils.isEmpty(bean.ENTERNAME) && bean.ENTERNAME.length() > 14) {
            entername = bean.ENTERNAME.substring(0, 13) + "..";
        }
        iPrinter.printText(entername, leftX + 60, newHeight);//        entername,fontSize,rotate,bold,false,false,0);


        //iPrinter.drawQrCode(600,newHeight,bean.QRCODE_VALUE,0,5,1);

        newHeight = newHeight + height;

        bean.LXRNAME = "联系人";
        bean.LXRPHONE = "联系人电话";
        //联系人
        iPrinter.printText(bean.LXRNAME + "/" + bean.LXRPHONE, leftX + 100, newHeight);//        bean.LXRNAME+"/"+bean.LXRPHONE,fontSize,rotate,bold,false,false,0);


        newHeight = newHeight + height;

        bean.RKSJ = "2023-06-13 17:18:30";
        // 产生日期
        if (!TextUtils.isEmpty(bean.RKSJ)) {
            String date = bean.RKSJ.substring(0, 11);
            iPrinter.printText(date, leftX, newHeight);//            date,fontSize,rotate,bold,false,false,0);
        }

        bean.RKL = "12";
        bean.JLDW = "吨";
        // 废物总量
        iPrinter.printText(bean.RKL + bean.JLDW, rightX, newHeight);//        bean.RKL+bean.JLDW,fontSize,rotate,bold,false,false,0);

        bean.REMARK = "备注111111123323";
        newHeight = newHeight + height;
        //备注
        String newStr = bean.REMARK;
        if (!TextUtils.isEmpty(bean.REMARK) && bean.REMARK.length() > 21) {
            newStr = bean.REMARK.substring(0, 20) + "..";
        }

        iPrinter.printText(newStr, leftX - 40, newHeight);
//                newStr,fontSize,rotate,bold,false,false,0);


    }

    private static Bitmap mBitmap;

    public static final int PRINT_TYPE_10 = 0;
    public static final int PRINT_TYPE_15 = 1;
    public static final int PRINT_TYPE_20 = 2;


    //dl2800/dk320
    public static boolean dlndkPrinter(Context mContext, ZPL iPrinter, LabelInfoBeanV3 bean, int type, boolean isV4Robot) {
        int height = 800;
        View view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10, null, false);
        int width = 800;

        LinearLayout mMainLayout = view.findViewById(R.id.mMainLayout);
        LinearLayout ll_fwdm = view.findViewById(R.id.ll_fwdm);
        LinearLayout ll_qrcode = view.findViewById(R.id.ll_qrcode);

        //是否翻转
        boolean isReversal = false;


        if (isV4Robot) {
            switch (type) {
                case PRINT_TYPE_10:
                    width = 800;
                    height = 800;
//                    isReversal = true;
                    view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10, null, false);
                    mMainLayout = view.findViewById(R.id.mMainLayout);
                    ll_qrcode = view.findViewById(R.id.ll_qrcode);
                    ll_fwdm = view.findViewById(R.id.ll_fwdm);
                    ll_qrcode.setPadding(0, 0, 18, 8);
//                    ll_fwdm.setPadding(0, 0, 0, 8);
                    mMainLayout.setPadding(-20, 70, 20, 0);
                    break;
                case PRINT_TYPE_15:
                    width = 1200;
                    height = 1120;
                    view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_15x15, null, false);
                    mMainLayout = view.findViewById(R.id.mMainLayout);
                    LinearLayout ll_wxtx = view.findViewById(R.id.ll_wxtx);
                    ll_wxtx.setPadding(12, 200, 50, 8);
                    mMainLayout.setPadding(10, 105, 0, 0);
                    break;
                case PRINT_TYPE_20:
                    width = 1600;
                    height = 1520;
                    view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_20x20_dk320, null, false);
                    mMainLayout = view.findViewById(R.id.mMainLayout);
                    ll_fwdm = view.findViewById(R.id.ll_fwdm);
                    ll_qrcode = view.findViewById(R.id.ll_qrcode);
                    ll_qrcode.setPadding(0, 8, 0, 8);
                    ll_fwdm.setPadding(0, 0, 0, 10);
                    mMainLayout.setPadding(0, 160, 0, 0);
                    break;
            }

        } else {
            float dp = getDensity((Activity) mContext);
            KLog.e("密度", "initEventAndData: " + dp);
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            KLog.e("高度", "initEventAndData: " + dm.heightPixels);
            KLog.e("宽度", "initEventAndData: " + dm.widthPixels);

            switch (type) {
                case PRINT_TYPE_10:
                    width = 800;
                    height = 720;
                    isReversal = true;

                    if (dp == 204) {
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10_pad, null, false);
                    } else if (DeviceUtils.isL2sDevice(mContext)) {//L2s
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10, null, false);
                        mMainLayout = view.findViewById(R.id.mMainLayout);
                        ll_qrcode = view.findViewById(R.id.ll_qrcode);
                        ll_fwdm = view.findViewById(R.id.ll_fwdm);
                        ll_qrcode.setPadding(0, 0, 0, 2);
                        ll_fwdm.setPadding(0, 0, 0, 0);
                        LinearLayout ll_remark = view.findViewById(R.id.ll_remark);
                        LinearLayout ll_barcode = view.findViewById(R.id.ll_barcode);
                        ll_remark.setPadding(0, -8, 0, 0);
                        ll_barcode.setPadding(20, 0, 0, 0);
                        mMainLayout.setPadding(16, 2, -20, 0);
                    } else {

                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10, null, false);

//                        mMainLayout = view.findViewById(R.id.mMainLayout);
//                        ll_qrcode = view.findViewById(R.id.ll_qrcode);
//                        ll_fwdm = view.findViewById(R.id.ll_fwdm);
//                        ll_qrcode.setPadding(0, 0, 0, 2);
//                        ll_fwdm.setPadding(0, 0, 0, 5);
//                        mMainLayout.setPadding(16, 10, -20, 0);

//                        if (dm.widthPixels <= 720 || dm.heightPixels<=800) {
//                            view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10, null, false);
//                        } else {
//                            view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10v3, null, false);
//                        }
                    }


                    break;
                case PRINT_TYPE_15:
                    width = 1200;
                    height = 1120;
                    if (dp == 204) {
                        height = 1140;
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_15x15_pad, null, false);
                    } else {
                        if (dm.widthPixels <= 720 || dm.heightPixels <= 800) {
                            view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_15x15, null, false);
                        } else {
                            view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_15x15v3, null, false);
                        }
                    }
                    break;
                case PRINT_TYPE_20:
                    width = 1600;
                    height = 1520;
                    if (dp == 204) {
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_20x20_pad, null, false);
                    } else if (DeviceUtils.isL2sDevice(mContext)) {//L2s
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_20x20, null, false);
                        mMainLayout = view.findViewById(R.id.mMainLayout);
                        ll_qrcode = view.findViewById(R.id.ll_qrcode);
                        ll_fwdm = view.findViewById(R.id.ll_fwdm);
                        ll_qrcode.setPadding(0, -10, 0, -10);
                        ll_fwdm.setPadding(0, -10, 0, -10);
//                        mMainLayout.setPadding(0, 170, 0, 0);
                    } else {
                        if (dm.widthPixels <= 720 || dm.heightPixels <= 800) {
                            view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_20x20, null, false);
                        } else {
                            view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_20x20v3, null, false);
                        }

                    }
                    break;
            }
        }


        initNewLabelData(mContext, view, bean);
        mBitmap = createBitmap(mContext, view, width, height);
        mBitmap.setDensity(160);

        if (isReversal) {
            Matrix m = new Matrix();
            m.postRotate(180);
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m,
                    true);
        }
        Bitmap aaa = HalfTone.FloydSteinberg(CanvasUtils.BackgroundWhite(mBitmap));

//        Bitmap aaa = LabelBitmapGenerator.createLabelBitmap(mContext, bean, type);
//
//        if (isReversal) {
//            Matrix m = new Matrix();
//            m.postRotate(180);
//            aaa = Bitmap.createBitmap(aaa, 0, 0, aaa.getWidth(), aaa.getHeight(), m,
//                    true);
//        }

        iPrinter.setLabelStart();
        iPrinter.setLabelWidth(width);
        iPrinter.setLabelLength(height);
        //打印图片
        iPrinter.printBitmap(0, 0, aaa);
        boolean b = iPrinter.setLabelEnd();
        return b;
    }

    //设备获取图片
    public static Bitmap getBitmapByDevice(Context mContext, LabelInfoBeanV3 bean, int type) {
        int height = 800;
        View view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10, null, false);
        int width = 800;

        LinearLayout mMainLayout = view.findViewById(R.id.mMainLayout);
        LinearLayout ll_fwdm = view.findViewById(R.id.ll_fwdm);
        LinearLayout ll_qrcode = view.findViewById(R.id.ll_qrcode);

        //是否翻转
        boolean isReversal = false;


            float dp = getDensity((Activity) mContext);
            KLog.e("密度", "initEventAndData: " + dp);
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            KLog.e("高度", "initEventAndData: " + dm.heightPixels);
            KLog.e("宽度", "initEventAndData: " + dm.widthPixels);

            switch (type) {
                case PRINT_TYPE_10:
                    width = 800;
                    height = 800;
                    isReversal = true;

                    if (DeviceUtils.isL2sDevice(mContext)) {//L2s
                        height = 790;
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10_dp12370_l2s, null, false);
                    }else if (DeviceUtils.isShykDevice(mContext)){
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10v3, null, false);
                    }else {
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10_dp12370, null, false);
                    }

                    break;
                case PRINT_TYPE_15:
                    width = 1200;
                     if (DeviceUtils.isL2sDevice(mContext)) {//L2s
                        height = 1250;
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_15x15_dp12370, null, false);
                    }else if (DeviceUtils.isShykDevice(mContext)){
                         height = 1200;
                         view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_15x15v3, null, false);
//                         mMainLayout = view.findViewById(R.id.mMainLayout);
//                        ll_qrcode = view.findViewById(R.id.ll_qrcode);
//                        ll_fwdm = view.findViewById(R.id.ll_fwdm);
//                        ll_qrcode.setPadding(0, -10, 0, -10);
//                        ll_fwdm.setPadding(10, -10, 0, -10);
//                         mMainLayout.setPadding(0, 130, 0, 0);
                     }else {
                         height = 1200;
                         view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_15x15_dp12370, null, false);
                         mMainLayout = view.findViewById(R.id.mMainLayout);
                         mMainLayout.setPadding(280,190, 0,0);
                     }

                    break;
                case PRINT_TYPE_20:
                    width = 1600;
                    height = 1520;

                    if (DeviceUtils.isL2sDevice(mContext)) {//L2s
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_20x20, null, false);
                        mMainLayout = view.findViewById(R.id.mMainLayout);
                        ll_qrcode = view.findViewById(R.id.ll_qrcode);
                        ll_fwdm = view.findViewById(R.id.ll_fwdm);
                        ll_qrcode.setPadding(0, -10, 0, -10);
                        ll_fwdm.setPadding(10, -10, 0, -10);
                        mMainLayout.setPadding(0, 120, 0, 0);
                    }else if (DeviceUtils.isShykDevice(mContext)){
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_20x20v3, null, false);
                        mMainLayout = view.findViewById(R.id.mMainLayout);
                        mMainLayout.setPadding(0, 130, 0, 0);
                    }else {
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_20x20_dp12370, null, false);
                    }

//                    else {
//                        if (dm.widthPixels <= 720 || dm.heightPixels <= 800) {
//                            height = 1600;
//                            view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_20x20, null, false);
//                        } else {
//                            view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_20x20v3, null, false);
//                        }
//
//                    }
                    break;
            }


        initNewLabelData(mContext, view, bean);
        mBitmap = createBitmap(mContext, view, width, height);
        mBitmap.setDensity(160);

        if (isReversal) {
            Matrix m = new Matrix();
            m.postRotate(180);
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m,
                    true);
        }
        Bitmap aaa = HalfTone.FloydSteinberg(CanvasUtils.BackgroundWhite(mBitmap));
        return aaa;
    }


    //dl2800/dk320
    public static Bitmap getBitmap(Context mContext, LabelInfoBeanV3 bean, int type) {
        int height = 800;
        View view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10, null, false);
        int width = 800;

        LinearLayout mMainLayout = view.findViewById(R.id.mMainLayout);
        LinearLayout ll_fwdm = view.findViewById(R.id.ll_fwdm);
        LinearLayout ll_qrcode = view.findViewById(R.id.ll_qrcode);

        //是否翻转
        boolean isReversal = false;
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        KLog.e("高度", "initEventAndData: " + dm.heightPixels);
        KLog.e("宽度", "initEventAndData: " + dm.widthPixels);

        switch (type) {
            case PRINT_TYPE_10:
                width = 800;
                height = 720;
                if (dm.widthPixels <= 720) {
                    view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10, null, false);
                } else {
                    view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10v3, null, false);
                }
                isReversal = false;
                break;
            case PRINT_TYPE_15:
                width = 1200;
                height = 1200;

                if (dm.widthPixels <= 720) {
                    view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_15x15, null, false);
                } else {
                    view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_15x15v3, null, false);
                }

                break;
            case PRINT_TYPE_20:
                width = 1600;
                height = 1520;
                isReversal = true;
                view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_20x20v3, null, false);
                break;
        }


        initNewLabelData(mContext, view, bean);
        mBitmap = createBitmap(mContext, view, width, height);
        mBitmap.setDensity(160);

        if (isReversal) {
            Matrix m = new Matrix();
            m.postRotate(180);
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m,
                    true);
        }
        Bitmap aaa = HalfTone.FloydSteinberg(CanvasUtils.BackgroundWhite(mBitmap));
        return aaa;
    }


    //新款标签蓝牙打印内容
    public static boolean dp385PrintBitmap(Context mContext, ESCPOS iPrinter, LabelInfoBeanV3 bean, int type) {
        boolean b = false;

        int width = 800;
        int height = 800;

        View view = null;

        //是否翻转
        boolean isReversal = true;

        float dp = getDensity((Activity) mContext);
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        KLog.e("高度", "initEventAndData: " + dm.heightPixels);
        KLog.e("宽度", "initEventAndData: " + dm.widthPixels);

        LinearLayout mMainLayout;
        LinearLayout ll_fwdm;
        LinearLayout ll_qrcode;

        switch (type) {
            case PRINT_TYPE_10:
                width = 800;
                height = 720;
                isReversal = false;

                if (dp == 204) {
                    view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10_pad, null, false);
                } else if (DeviceUtils.isL2sDevice(mContext)) {//L2s
                    view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10, null, false);
                    mMainLayout = view.findViewById(R.id.mMainLayout);
                    ll_qrcode = view.findViewById(R.id.ll_qrcode);
                    ll_fwdm = view.findViewById(R.id.ll_fwdm);
                    ll_qrcode.setPadding(0, -10, 0, 2);
                    ll_fwdm.setPadding(0, 0, 0, 0);
                    LinearLayout ll_remark = view.findViewById(R.id.ll_remark);
                    LinearLayout ll_barcode = view.findViewById(R.id.ll_barcode);
                    ll_remark.setPadding(0, -8, 0, 0);
                    ll_barcode.setPadding(10, 0, 0, 0);
                    mMainLayout.setPadding(-20, 20, 0, 0);
                } else {
                    if (dm.widthPixels <= 720 || dm.heightPixels <= 800) {
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10, null, false);
                    } else {
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_10x10v3, null, false);
                    }
                }

                break;
            case PRINT_TYPE_15:
                width = 1200;
                height = 1120;
                if (dp == 204) {
                    height = 1140;
                    view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_15x15_pad, null, false);
                } else {
                    if (dm.widthPixels <= 720 || dm.heightPixels <= 800) {
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_15x15, null, false);
                    } else {
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_15x15v3, null, false);
                    }
                }
                break;

            case PRINT_TYPE_20:
                width = 1600;
                height = 1520;
                if (dp == 204) {
                    view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_20x20_pad, null, false);
                } else if (DeviceUtils.isL2sDevice(mContext)) {//L2s
                    view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_20x20, null, false);
                    mMainLayout = view.findViewById(R.id.mMainLayout);
                    ll_qrcode = view.findViewById(R.id.ll_qrcode);
                    ll_fwdm = view.findViewById(R.id.ll_fwdm);
                    ll_qrcode.setPadding(0, -10, 0, -10);
                    ll_fwdm.setPadding(0, -10, 0, -10);
//                        mMainLayout.setPadding(0, 170, 0, 0);
                } else {
                    if (dm.widthPixels <= 720 || dm.heightPixels <= 800) {
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_20x20, null, false);
                    } else {
                        view = LayoutInflater.from(mContext).inflate(R.layout.bitmap_test_20x20v3, null, false);
                    }

                }
                break;
        }

        initNewLabelData(mContext, view, bean);


        mBitmap = createBitmap(mContext, view, width, height);

        if (isReversal) {
            Matrix m = new Matrix();
            m.postRotate(180);
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m,
                    true);
        }

        iPrinter.setPrintAreaWidth(width);

        Bitmap aaa = HalfTone.FloydSteinberg(CanvasUtils.BackgroundWhite(mBitmap));

        b = iPrinter.printBitmap((aaa), 0, 0, true, 2);
        b = iPrinter.feedPrintPosition();
        return b;

    }


    private static float getDensity(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.densityDpi;
    }


    public static void saveBitmap(Context context, Bitmap bitmap, String bitName) {

        String fileName;

        File file;

        if (Build.BRAND.equals("Xiaomi")) {// 小米手机

            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + bitName;

        } else {// Meizu 、Oppo

            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + bitName;

        }


        file = new File(fileName);

        if (file.exists()) {

            file.delete();

        }


        FileOutputStream out;

        try {

            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
                // 插入图库
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), bitName, null);
            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        // 发送广播，通知刷新图库的显示
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));

    }


    private static void initNewLabelData(Context mContext, View view, LabelInfoBeanV3 mData) {

        TextView tv_item1 = view.findViewById(R.id.tv_item1);
        TextView tv_item2 = view.findViewById(R.id.tv_item2);
        TextView tv_item3 = view.findViewById(R.id.tv_item3);
        TextView tv_item4 = view.findViewById(R.id.tv_item4);
        TextView tv_item5 = view.findViewById(R.id.tv_item5);
        TextView tv_item6 = view.findViewById(R.id.tv_item6);
        TextView tv_item8 = view.findViewById(R.id.tv_item8);
        TextView tv_item9 = view.findViewById(R.id.tv_item9);
        TextView tv_item10 = view.findViewById(R.id.tv_item10);
        TextView tv_item11 = view.findViewById(R.id.tv_item11);
        TextView tv_item12 = view.findViewById(R.id.tv_item12);
        TextView tv_item14 = view.findViewById(R.id.tv_item14);
        TextView tv_item16 = view.findViewById(R.id.tv_item16);

        tv_item1.setText(mData.FWXXMC);
        tv_item2.setText(mData.FWDLBH + "-" + mData.FWDLMC);
        tv_item3.setText(mData.FWXLBH);

        // 产生日期
        if (!TextUtils.isEmpty(mData.RKSJ)) {
            String date = mData.RKSJ.substring(0, 11);
            tv_item4.setText(date);
        }
        tv_item5.setText(mData.RKL + mData.JLDW);
        tv_item6.setText(mData.WLXZ);
        tv_item8.setText(mData.ZYWRCF);
        tv_item9.setText(mData.WXCF);
        tv_item10.setText(mData.ZYSX);
        tv_item11.setText(mData.QRCODE_VALUE);

        if (!TextUtils.isEmpty(mData.ENTERNAME) && mData.ENTERNAME.length() > 20) {
            tv_item12.setMaxLines(2);
        }
        tv_item12.setText(mData.ENTERNAME);
        tv_item14.setText(mData.LXRNAME + "/" + mData.LXRPHONE);
        tv_item16.setText(mData.REMARK);
//        // TODO: 2023/9/5
//        tv_item8.setText("备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试");
//        tv_item9.setText("备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试");
//        tv_item10.setText("备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试");
//        tv_item16.setText("备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试备注测试");


        //毒性
        CheckBox cb_dx = view.findViewById(R.id.cb_dx);

        //反应性
        CheckBox cb_fyx = view.findViewById(R.id.cb_fyx);

        //易燃性
        CheckBox cb_yrx = view.findViewById(R.id.cb_yrx);

        //腐蚀性
        CheckBox cb_fsx = view.findViewById(R.id.cb_fsx);


        if (!TextUtils.isEmpty(mData.FWTX)) {
            String[] fwtxArr = mData.FWTX.split(",");

            for (int i = 0; i < fwtxArr.length; i++) {
                switch (fwtxArr[i]) {
                    case "毒性":
                        cb_dx.setBackgroundResource(R.mipmap.ic_checked_black);
                        mData.WXCF_DX = "1";
                        break;
                    case "反应性":
                        cb_fyx.setBackgroundResource(R.mipmap.ic_checked_black);
                        mData.WXCF_FYX = "1";
                        break;
                    case "易燃性":
                        cb_yrx.setBackgroundResource(R.mipmap.ic_checked_black);
                        mData.WXCF_YRX = "1";
                        break;
                    case "腐蚀性":
                        cb_fsx.setBackgroundResource(R.mipmap.ic_checked_black);
                        mData.WXCF_FSX = "1";
                        break;
                }
            }
        }

        //生成溯源二维码
//        String ip = SPUtils.get(mContext,"IP","").toString();
//        String port = SPUtils.get(mContext,"Port","").toString();
//        String contractName = SPUtils.get(mContext,"CONTRACT_NAME","http://").toString();
//        String historyStr = contractName+ip+":"+(Integer.valueOf(port)+1)+"/web-waste/traceSrouce/traceSource.html?qrcodeValue="+mData.QRCODE_VALUE;
//        Bitmap qrCodeBitmap2 = QrCodeUtils.createQRImage(historyStr,600,600);
//        mData.HISTORY_CODE = historyStr;
//        ImageView iv_history_code = view.findViewById(R.id.iv_history_code);
//        iv_history_code.setImageBitmap(qrCodeBitmap2);

        ImageView iv_code = view.findViewById(R.id.iv_code);
        Bitmap qrCodeBitmap = QrCodeUtils.createQRImage(mData.QRCODE_VALUE_URL, 1200, 1200);
        iv_code.setImageBitmap(qrCodeBitmap);
    }


    public static Bitmap createBitmap(Context context, View v, int width, int height) {
        //测量使得view指定大小
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        v.measure(measuredWidth, measuredHeight);
        //调用layout方法布局后，可以得到view的尺寸大小
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        v.draw(c);
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return bmp;
    }


    //新款标签蓝牙打印内容
    public static void btPrint(zp_cpcl_BluetoothPrinter iPrinter, LabelInfoBeanV3 bean) {
        iPrinter.pageSetup(1000, 800);
        int height = 50;
        int startY = 20;
        int leftX = 160;
        int rightX = 440;
        int fontSize = 2;
        int rotate = 180;
        int bold = 0;
        int newHeight = 0;
        int mulHeight = 92;


//        iPrinter.drawQrCode(490,10,bean.HISTORY_CODE,0,2,1);

        newHeight = startY + height;

        // 废物名称
        iPrinter.drawText(leftX, newHeight,
                bean.FWXXMC, fontSize, rotate, bold, false, false, 0);


        newHeight = startY + height * 2;

        //废物类别
        iPrinter.drawText(leftX, newHeight,
                bean.FWDLBH + "-" + bean.FWDLMC, fontSize, rotate, bold, false, false, 0);


        newHeight = startY + height * 3;
        // 废物代码
        iPrinter.drawText(leftX, newHeight,
                bean.FWXLBH, fontSize, rotate, bold, false, false, 0);


        // 废物形态
        iPrinter.drawText(rightX, newHeight,
                bean.WLXZ, fontSize, rotate, bold, false, false, 0);


        newHeight = startY + height * 4;


        // 主要成分
//        iPrinter.drawText(leftX,newHeight,
//                bean.ZYWRCF,fontSize,rotate,bold,false,false,0);


        if (!TextUtils.isEmpty(bean.ZYWRCF) && bean.ZYWRCF.length() > 36) {
            String strSta = bean.ZYWRCF.substring(0, 18);
            String strEnd = bean.ZYWRCF.substring(18, 35) + "..";

            iPrinter.drawText(leftX, newHeight,
                    strSta, fontSize, rotate, bold, false, false, 0);
            //换行
            iPrinter.drawText(leftX, newHeight + 40,
                    strEnd, fontSize, rotate, bold, false, false, 0);

        } else if (!TextUtils.isEmpty(bean.ZYWRCF) && bean.ZYWRCF.length() > 18) {
            String strSta = bean.ZYWRCF.substring(0, 18);
            String strEnd = bean.ZYWRCF.substring(18);

            iPrinter.drawText(leftX, newHeight,
                    strSta, fontSize, rotate, bold, false, false, 0);
            //换行
            iPrinter.drawText(leftX, newHeight + 40,
                    strEnd, fontSize, rotate, bold, false, false, 0);

        } else {
            iPrinter.drawText(leftX, newHeight,
                    bean.ZYWRCF, fontSize, rotate, bold, false, false, 0);
        }


        if (!TextUtils.isEmpty(bean.WXCF_DX)) {
            // 毒性
            iPrinter.drawText(632, newHeight,
                    "√", fontSize, rotate, bold, false, false, 0);
        }


        if (!TextUtils.isEmpty(bean.WXCF_FYX)) {
            // 反应性
            iPrinter.drawText(722, newHeight,
                    "√", fontSize, rotate, bold, false, false, 0);
        }


        // 毒性
        iPrinter.drawText(628, newHeight,
                "□", fontSize, rotate, bold, false, false, 0);


        // 反应性
        iPrinter.drawText(718, newHeight,
                "□", fontSize, rotate, bold, false, false, 0);


        newHeight = newHeight + mulHeight;

        if (!TextUtils.isEmpty(bean.WXCF_YRX)) {
            // 易燃性
            iPrinter.drawText(632, newHeight + 20,
                    "√", fontSize, rotate, bold, false, false, 0);

        }

        if (!TextUtils.isEmpty(bean.WXCF_FSX)) {
            // 腐蚀性
            iPrinter.drawText(722, newHeight + 20,
                    "√", fontSize, rotate, bold, false, false, 0);
        }


        // 易燃性
        iPrinter.drawText(628, newHeight + 20,
                "□", fontSize, rotate, bold, false, false, 0);


        // 腐蚀性
        iPrinter.drawText(718, newHeight + 20,
                "□", fontSize, rotate, bold, false, false, 0);


        // 有害成分
        if (!TextUtils.isEmpty(bean.WXCF) && bean.WXCF.length() > 36) {
            String strSta = bean.WXCF.substring(0, 18);
            String strEnd = bean.WXCF.substring(18, 35) + "..";

            iPrinter.drawText(leftX, newHeight,
                    strSta, fontSize, rotate, bold, false, false, 0);
            //换行
            iPrinter.drawText(leftX, newHeight + 40,
                    strEnd, fontSize, rotate, bold, false, false, 0);

        } else if (!TextUtils.isEmpty(bean.WXCF) && bean.WXCF.length() > 18) {
            String strSta = bean.WXCF.substring(0, 18);
            String strEnd = bean.WXCF.substring(18);

            iPrinter.drawText(leftX, newHeight,
                    strSta, fontSize, rotate, bold, false, false, 0);
            //换行
            iPrinter.drawText(leftX, newHeight + 40,
                    strEnd, fontSize, rotate, bold, false, false, 0);

        } else {
            iPrinter.drawText(leftX, newHeight,
                    bean.WXCF, fontSize, rotate, bold, false, false, 0);
        }


        newHeight = newHeight + mulHeight;


        // 注意事项
        if (!TextUtils.isEmpty(bean.ZYSX) && bean.ZYSX.length() > 50) {
            String strSta = bean.ZYSX.substring(0, 25);
            String strEnd = bean.ZYSX.substring(25, 49) + "..";

            iPrinter.drawText(leftX, newHeight,
                    strSta, fontSize, rotate, bold, false, false, 0);
            //换行
            iPrinter.drawText(leftX, newHeight + 40,
                    strEnd, fontSize, rotate, bold, false, false, 0);

        } else if (!TextUtils.isEmpty(bean.ZYSX) && bean.ZYSX.length() > 25) {
            String strSta = bean.ZYSX.substring(0, 25);
            String strEnd = bean.ZYSX.substring(25);

            iPrinter.drawText(leftX, newHeight,
                    strSta, fontSize, rotate, bold, false, false, 0);
            //换行
            iPrinter.drawText(leftX, newHeight + 40,
                    strEnd, fontSize, rotate, bold, false, false, 0);

        } else {
            iPrinter.drawText(leftX, newHeight,
                    bean.ZYSX, fontSize, rotate, bold, false, false, 0);
        }

        newHeight = newHeight + mulHeight;

        // 数字识别码
        iPrinter.drawText(leftX + 30, newHeight,
                bean.QRCODE_VALUE, fontSize, rotate, bold, false, false, 0);

        newHeight = newHeight + height;


        //产生(收集)单位

        String entername = bean.ENTERNAME;
        if (!TextUtils.isEmpty(bean.ENTERNAME) && bean.ENTERNAME.length() > 17) {
            entername = bean.ENTERNAME.substring(0, 16) + "..";
        }
        iPrinter.drawText(leftX + 60, newHeight,
                entername, fontSize, rotate, bold, false, false, 0);


        iPrinter.drawQrCode(595, newHeight, bean.QRCODE_VALUE_URL, 0, getQrcodeWidth(bean.QRCODE_VALUE_URL, 5), 1);

        newHeight = newHeight + height;

        //联系人
        iPrinter.drawText(leftX + 100, newHeight,
                bean.LXRNAME + "/" + bean.LXRPHONE, fontSize, rotate, bold, false, false, 0);


        newHeight = newHeight + height;

        // 产生日期
        if (!TextUtils.isEmpty(bean.RKSJ)) {
            String date = bean.RKSJ.substring(0, 11);
            iPrinter.drawText(leftX, newHeight,
                    date, fontSize, rotate, bold, false, false, 0);
        }


        // 废物总量
        iPrinter.drawText(rightX, newHeight,
                bean.RKL + bean.JLDW, fontSize, rotate, bold, false, false, 0);


        newHeight = newHeight + height;
        //备注
        String newStr = bean.REMARK;
        if (!TextUtils.isEmpty(bean.REMARK) && bean.REMARK.length() > 21) {
            newStr = bean.REMARK.substring(0, 20) + "..";
        }

        iPrinter.drawText(leftX - 40, newHeight,
                newStr, fontSize, rotate, bold, false, false, 0);


        if (iPrinter != null) {
            iPrinter.print(rotate, 1);
        }

    }

}
