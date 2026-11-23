package com.grkj.ytjqy_pad.utils.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期处理工具类
 */

public class DateUtil {


    /**
     * 格式化日期
     *
     * @return
     */
    public static String formatDate(String data) {
        String str;
        if (!StringUtils.isEmpty(data)) {
//            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = fmt.parse(data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            str = fmt.format(date);
        } else {
            str = "2000-01-01";
        }
        return str;
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static String formatToDate(String data) {
        String str;
        if (!StringUtils.isEmpty(data)) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = fmt.parse(data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            str = fmt.format(date);
        } else {
            str = "";
        }
        return str;
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static String formatTime(String data) {
        String str;
        if (!StringUtils.isEmpty(data)) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = fmt.parse(data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            str = fmt.format(date);
        } else {
            str = "2000-01-01 00:00:00";
        }

        return str;
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static String formatToTime(String data) {
        String str;
        if (!StringUtils.isEmpty(data)) {
            String s = formatTime(data);
            str = s.substring(s.length() - 8, s.length());
        } else {
            str = "00:00:00";
        }

        return str;
    }

    /**
     * Date转String
     */
    public static String DateTrnsferString(Date date) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getCurrentDate() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }

    public static String getCurrentDateAndTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }

    public static String getCurrentTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }

    public static String getCurrentYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return sdf.format(date);
    }

}
