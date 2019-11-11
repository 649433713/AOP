package com.nju.aop.utils;

/**
 * @author yinywf
 * Created on 2017/10/20
 */
public class MathUtil {

    private static final Double MONEY_RANGE = 0.01;

    /**
     * function:比较两个金额是否相等
     * parameters:
     * throw:
     * Created by yinywf
     */
    public static Boolean equals(Double d1, Double d2) {
        Double result = Math.abs(d1 - d2);
        if (result < MONEY_RANGE) {
            return true;
        }else {
            return false;
        }
    }
}
