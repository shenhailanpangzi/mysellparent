package com.lanpang.apigateway.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * json格式化
 * Created by 杨浩
 * 2018-07-04 01:30
 */
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * ObjectMapper的Json转化器
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object fromJson(String string, Class classtype) {
        try {
            return objectMapper.readValue(string, classtype);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object fromJson(String string, TypeReference typeReference) {
        try {
            return objectMapper.readValue(string, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
