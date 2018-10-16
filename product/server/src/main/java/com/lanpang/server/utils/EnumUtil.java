package com.lanpang.server.utils;


import com.lanpang.server.enums.CodeEnum;

/**
 * 根据在架状态 获得枚举的值
 * Created by 杨浩
 * 2017-07-16 18:36
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
