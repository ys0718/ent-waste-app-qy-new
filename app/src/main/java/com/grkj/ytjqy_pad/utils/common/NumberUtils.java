package com.grkj.ytjqy_pad.utils.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {

    private NumberUtils() {
    }

    private static final int SCALE = 3;
    private static final String ZERO = "0.000";

    /**
     * 传入 String，为空返回 "0.000"，否则返回保留 3 位小数的 String（四舍五入，末尾补 0）。
     *
     * 示例：
     *   null        -> "0.000"
     *   ""          -> "0.000"
     *   "   "       -> "0.000"
     *   "1.2"       -> "1.200"
     *   "1.23456"   -> "1.235"
     *   "12"        -> "12.000"
     *   "-0.0001"   -> "0.000"
     */
    public static String formatNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            return ZERO;
        }
        String s = value.trim();
        try {
            BigDecimal bd = new BigDecimal(s).setScale(SCALE, RoundingMode.HALF_UP);
            // 处理 -0.000 这类情况，统一返回 "0.000"
            if (bd.signum() == 0) {
                return ZERO;
            }
            return bd.toPlainString();
        } catch (NumberFormatException e) {
            // 非数字：默认抛异常。若希望容错，改成 return ZERO; 即可
            throw new IllegalArgumentException("不是合法数字: " + value, e);
        }
    }

}
