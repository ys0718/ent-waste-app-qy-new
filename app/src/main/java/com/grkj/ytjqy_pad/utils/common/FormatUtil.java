package com.grkj.ytjqy_pad.utils.common;

import android.app.Activity;
import android.util.Log;

import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.gather.Constants;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * Created by yusam on 2018/7/19
 */

public class FormatUtil {

//    public static final DecimalFormat mDecimalFormat = new DecimalFormat("###,##0.000");
    public static final DecimalFormat mDecimalFormat = new DecimalFormat("0.000");

    public static final DecimalFormat mIntegerFormat = new DecimalFormat("#");


    public static String changeToDecimal(Double value){

        return mDecimalFormat.format(value);
    }

    public static String changeToInteger(Double value){
        return mIntegerFormat.format(value);
    }

    /**
     * 将服务器回传的图片路径源地址替换成BASE_URL源地址
     *
     * @param localUrl 服务器回传地址
     * @return 转化后地址或者无效空字符
     */
    public static String changeLocalUrlToServer(String localUrl) {
        String serverUrl = "";
        if (Pattern.compile("https?://.*/.*").matcher(localUrl).matches()) { // 匹配路径是否有效
            if (localUrl.contains("https")) {
                serverUrl = localUrl.substring(9); // 去除https://
            } else {
                serverUrl = localUrl.substring(8); // 去除http://
            }
            serverUrl = serverUrl.substring(serverUrl.indexOf('/') + 1); // 去除错误源地址, 新地址后带'/'
            serverUrl = (String) SPUtils.get (App.getContext (),"URL","")+ serverUrl; // 拼接新地址
        } else {
            Log.e("FormatUtil: ", "the url is invalid");
        }
        return serverUrl;
    }

}
