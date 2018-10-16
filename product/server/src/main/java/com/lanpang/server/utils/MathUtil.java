package com.lanpang.server.utils;

/**
 * 金额比较是否相等，精确到0.01  相等返回true
 * Created by
 * 2018-07-09 16:56
 */
public class MathUtil {

    private static final Double MONEY_RANGE = 0.01;

    /**
     * 比较2个金额是否相等
     *
     * @param d1
     * @param d2
     * @return
     */
    public static Boolean equals(Double d1, Double d2) {
        Double result = Math.abs(d1 - d2);
        if (result < MONEY_RANGE) {
            return true;
        } else {
            return false;
        }
    }
}
