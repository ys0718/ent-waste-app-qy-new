package com.grkj.ytjqy_pad.utils.common;

import android.text.TextUtils;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * 字符串相关工具类
 * Created by yusam on 2018/7/23.
 */
public class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static DecimalFormat df = new DecimalFormat("#0.000");

    /**
     * 字符串拼接,线程安全
     */
    public static String buffer(String... array) {
        StringBuffer s = new StringBuffer();
        for (String str : array) {
            s.append(str);
        }
        return s.toString();
    }


    /**
     * 去掉小数点后无用0
     * */
    public static String formatDecimalStr(String str) {
        if (str.indexOf(".") > 0) {
            str = str.replaceAll("0+?$", "");//去掉后面无用的零
            str = str.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }
        return str;
    }

    /**
     * 字符串拼接,线程不安全,效率高
     */
    public static String builder(String... array) {
        StringBuilder s = new StringBuilder();
        for (String str : array) {
            s.append(str);
        }
        return s.toString();
    }

    /**
     * 剪切并返回前半部分
     */
    public static String SplitString(String oldString, String containString) {
        String[] strings = oldString.split(containString);
        return strings[0];
    }

    /**
     * 字符串替换,从0开始,end不包括
     */
    public static String replaceWith(String src, int start, int end, String replaceStr) {

        if (start < 0) {
            throw new StringIndexOutOfBoundsException(start);
        }

        if (end > src.length()) {
            throw new StringIndexOutOfBoundsException(end);
        }

        try {

            String startStr;
            if (start <= 1) {
                startStr = "";
            } else {
                startStr = src.substring(0, start);
            }
            String endStr;
            if (end >= src.length() - 1) {
                endStr = "";
            } else {
                endStr = src.substring(end, src.length());
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(startStr);

            for (int i = 0; i < end - start; i++) {

                stringBuilder.append(replaceStr);

            }

            return stringBuilder.append(endStr).toString();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return src;

    }


    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) {
            return true;
        }
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return (a == b) || (b != null) && (a.length() == b.length()) && a.regionMatches(true, 0, b, 0, b.length());
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 空值设置文字提示
     *
     * @param mView
     * @param mKeyWord 文本内容
     * @param mMessage 提示消息
     */
    public static void setIsNotEmptyText(TextView mView, String mKeyWord, String mMessage) {
        if (TextUtils.isEmpty(mKeyWord)) {
            mView.setText(mMessage);
        } else {
            if (TextUtils.isEmpty(mKeyWord.trim())) {
                mView.setText(mMessage);
            } else {
                mView.setText(mKeyWord);
            }
        }
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) {
            return s;
        }
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    public static String substringBetween(String str, String open, String close) {
        if (!TextUtils.isEmpty(str)) {
            int start = str.indexOf(open);
            if (start != -1) {
                int end = str.indexOf(close, start + open.length());
                if (end != -1) {
                    return str.substring(start + open.length(), end);
                }
            }

        }
        return null;
    }

}