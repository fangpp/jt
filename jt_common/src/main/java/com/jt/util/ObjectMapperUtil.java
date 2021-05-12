package com.jt.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;
import org.springframework.stereotype.Component;

public class ObjectMapperUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    //1.对象转化为JSON
    public static String toJSON(Object obj){
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            //将检查异常,转化为运行时异常
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //2.JSON转化为对象  传递什么样的类型,返回什么样的对象???
    public static <T> T toObject(String json, Class<T> target){
        try {
            return MAPPER.readValue(json, target);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
