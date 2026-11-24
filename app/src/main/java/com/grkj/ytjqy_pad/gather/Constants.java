package com.grkj.ytjqy_pad.gather;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;

import com.grkj.ytjqy_pad.utils.common.AppUtils;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by yusam on 2022/3/19
 * 公用常量类
 */


public class Constants {

    public static final String DEC_KEY = "gdgfgdgf";
    public static final String DEC_KEY2 = "gdsgf001";

    public static final String TYPE_CZ = "1";
    public static final String TYPE_SM = "2";

    public static final String DEFAULT_PORT = "10002";//湛江
    public static final String DEFAULT_IP = "iot-gr.com";//湛江


    public static final int VERSION_CODE = 1;
    public static final boolean IS_DEBUG = false;//测试版本true  正式版本false
    public static final int VERSION_CODE_DEFAULT = 1;
    public static final int VERSION_CODE_SH = 2;

    /**
     * 保存在手机里的SP文件名
     */
    public static final String FILE_NAME = "sys_sp";

    public static final String[] devicesList = {"720*1344","720*1391", "720*1280", "720*1440", "720*1356", "720*1424", "720*1342", "720*1396", "720*1352"};

    /**
     * 常量
     */
    public static final String TOKEN = "token";
    public static final String TYPE_YTJ = "ytj";
    public static final String TYPE_SCZD = "sczd";
    public static final String ROWS_KEY = "rows";
    public static final String PAGE_KEY = "page";
    public static final String ROWS_VALUE = "9";
    public static final String JUMP_KEY = "data";
    public static final String IS_COPY = "is_copy";
    public static final String MENU_TYPE = "menuType";
    public static final String APP_VERSION_CODE = "app_version_code";
    public static final int SCAN_REQUEST_CODE = 1011;
    public static final String BLT_CONNECTED_ADDRESS = "blt_connected_address";
    public static final String KEY_RK = "RKysjbr";
    public static final String KEY_CK = "CKysjbr";
    public static final String KEY_JBRSJ = "jbrsj";
    public static final String KEY_ZCJBRSJ = "zcjbrsj";
    public static final String KEY_JZ = "Jzqksm";

    public static final String KEY_YSDWMC = "ysdwmc";

    public static final String KEY_YSBMJBR = "ysbmjbr";

    public static final String IS_MENU_V1 = "is_menu_v1";
    public static final String KEY_CKBMJBR = "ckbmjbr";
    public static final String IS_HN_MENU = "is_hn_menu";
    public static final String SELECT_DWID = "xwqyDwid";
    public static final String MAINID = "mainid";
    public static final String ZCSSID = "zcssid";
    public static final String TIME = "time";
    public static final String DATA_PARENT = "data_parent";

    public static final String LAST_MENU_INDEX = "last_menu_index";

    public static final String LAST_JSDW_BEAN = "last_jsdw_bean";


    /**
     * 环境
     */
    public static final String EV_GUANGDONG = "gd";
    public static final String EV_HUNAN = "hn";
    public static final String EV_SHANDONG = "sd";
    public static final String EV_NEIMENG = "mg";
    public static final String EV_FOSHAN= "fs";

    /**
     * 顺控打印相关配置
     * */
    public static final String printerId = "862170069517348";
    public static final String appId = "201839a629ee4432882399e6d8429f84";
    public static final String appSecret = "cb8deed1e0e14ebaafe5bd7a46b0716c";
    public static final String shareCode = "379723";
    public static final String templateId = "629";
    public static final String SK_DWID = "2023122509272164593572";
    public static final String CHECK_URL = "20240815001/1725269012956.jpg";



    public static final int PRINT_TYPE_10 = 0;
    public static final int PRINT_TYPE_15 = 1;
    public static final int PRINT_TYPE_20 = 2;

    /**
     * 登录
     */
    public static final String IS_REMEMBER = "is_remember";
    public static final String LOGIN_NAME = "login_name";
    public static final String OFFICE_ID = "office_id";
    public static final String DWID = "dwid";
    public static final String PASSWORD = "password";
    public static final String USER_INFO = "user_info";
    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "user_id";
    public static final String DEVICE_CODE = "device_code";
    public static final String ENVIRONMENT = "environment";


    public static final String BLUETOOTH_CC4 = "CC4";
    public static final String BLUETOOTH_DP385 = "DP-38";
    public static final String BLUETOOTH_DL = "DL";
    public static final String BLUETOOTH_JX_PRINTER = "JXPRINTER";
    public static final String BLUETOOTH_DP12370 = "DP12370";


    @SuppressLint("MissingPermission")
    public static boolean checkBluetoothInList(BluetoothDevice device){
        boolean isInBtList = false;
        if (device.fetchUuidsWithSdp() && !TextUtils.isEmpty(device.getName()) && (device.getName().contains(Constants.BLUETOOTH_CC4)
                || device.getName().contains(Constants.BLUETOOTH_DP385)|| device.getName().contains(Constants.BLUETOOTH_DP12370)
                || device.getName().contains(Constants.BLUETOOTH_DL)|| device.getName().contains(Constants.BLUETOOTH_JX_PRINTER))) {
            isInBtList = true;
        }
        return isInBtList;
    }


    //这是实现网络缓存的路径
    public static final String PATH_DATA = App.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";

    public static final String PRINTER_NAME = "printer_name";
    public static final String PRINTER_MAC = "printer_mac";


    public static final String LAST_ZCSS = "last_zcss";


    public static String kgToTon(String jldw,String value){
        String result = value;
        if ("千克".equals(jldw)){
            result =  new BigDecimal(value).divide(new BigDecimal(1000)).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
        }

        return result;
    }

    /**
     * 判断设备
     */
    public static boolean judgeDevice() {
        boolean isDevice = false;
        String resolution = AppUtils.getDeviceResolution(App.getContext());
        //手持设备设置中显示大小不为标准时 高度数值有所不同，宽度数值保持为720 T1/i6310/8876WB
        for (String s : Constants.devicesList) {
            if (s.equals(resolution)) {
                isDevice = true;
                break;
            }
        }

        return isDevice;
    }




}
