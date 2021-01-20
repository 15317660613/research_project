package com.adc.da.budget.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonUtil {
    private final static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static String bean2Json(Object obj){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("json processing exception",e);
            return null;
        }
    }

    /**
     * json转对象
     * @param json
     * @param clazz 类
     * @return
     *
     */
    public static <T> T jsonToBean(String json, Class clazz){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return (T)objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.error("json processing exception",e);
          
            return null;
        }
    }
}
