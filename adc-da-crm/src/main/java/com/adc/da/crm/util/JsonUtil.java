package com.adc.da.crm.util;

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
     * @param T 类
     * @return
     */
    public static <T> T jsonToBean(String json, Class T){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return (T)objectMapper.readValue(json, T);
        } catch (IOException e) {
            logger.error("json processing exception",e);
            //throw (new ServiceException(1, null, "json转对象失败"));
            return null;
        }
    }
}
