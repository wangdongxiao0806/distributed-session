package com.project.wdx.distributedsession.utils;

import com.alibaba.fastjson.JSONObject;

public class SerializerUtils {

    public static String serialize(Object obj){
        return JSONObject.toJSONString(obj);
    }

    public static <T>T deSerialize(String str,Class<T> clazz){
        return JSONObject.parseObject(str,clazz);
    }
}
