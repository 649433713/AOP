package com.nju.aop.utils;

import com.nju.aop.enums.CodeEnum;

/**
 * @author yinywf
 * Created on 2017/10/20
 */
public class EnumUtil {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {

        for (T each : enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }

        return null;

    }
}
