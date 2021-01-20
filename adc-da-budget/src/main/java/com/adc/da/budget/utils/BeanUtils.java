package com.adc.da.budget.utils;

import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author qichunxu
 */

@Slf4j
public class BeanUtils {
    private static org.springframework.beans.BeanUtils beanUtils;

    public static void copyPropertiesIgnoreNullValue(Object source, Object target) {
        beanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) { emptyNames.add(pd.getName()); }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 赋值操作
     * @param source 源对象
     * @param destinationClass 目标Class对象
     * @param <T>
     * @return
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        try {
            //获取源对象所有的字段
            Field[] srcFields = source.getClass().getDeclaredFields();
            //创建需要copy的对象
            T obj = destinationClass.newInstance();
            Field[] toFields = destinationClass.getDeclaredFields();
            //遍历源对象的字段数组
            for (Field srcField : srcFields) {
                //遍历目标对象的字段数组
                for (Field toField : toFields) {
                    //如果字段匹配 进行赋值操作
                    if (StringUtils.equals(srcField.getName(), toField.getName())) {
                        srcField.setAccessible(true);
                        Object value = srcField.get(source);
                        toField.setAccessible(true);
                        toField.set(obj,value);
                    }
                }
            }
            return obj;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
