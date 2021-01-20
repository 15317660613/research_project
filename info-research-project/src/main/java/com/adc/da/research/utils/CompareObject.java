package com.adc.da.research.utils;

import com.adc.da.research.entity.HiBaseInterface;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-10-25
 */
public class CompareObject {
    private CompareObject() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 只对比，不修改，用于新增
     */
    public static final int READ_ONLY = 0;

    /**
     * 对比数据，并且覆盖不同项
     */
    public static final int WRITE_DIFFERENT = 1;

    public static <T extends HiBaseInterface> StringBuilder compareHiField(T target, T source, int level) {
        /*
         * 不对下列字段进行对比校验
         */
        String[] ignoreArray = new String[] {"procBusinessKey", "mask", "id", "researchProjectId"};
        Map<String, List<Object>> differentFieldMap = compareFields(target, source, ignoreArray, level);
        StringBuilder builder = new StringBuilder();
        for (String s : differentFieldMap.keySet()) {
            builder.append(s).append(',');
        }
        return builder;
    }

    /**
     * 对比俩对象
     *
     * @param obj1
     * @param obj2
     * @param ignoreArr
     * @param level     复写级别，0为只对比，1为对比 并且覆盖
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-10-28
     **/
    public static Map<String, List<Object>> compareFields(Object obj1, Object obj2, String[] ignoreArr, int level) {
        try {
            Map<String, List<Object>> map = new HashMap<>();
            List<String> ignoreList = initList(ignoreArr);
            if (obj1.getClass() == obj2.getClass()) {
                //只有两个对象都是同一类型才有可比性
                Class clazz = obj1.getClass();
                //获取object的属性描述
                PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {
                    compare(obj1, obj2, pd, ignoreList, map, level);
                }
            }
            return map;
        } catch (Exception e) {
            throw new AdcDaBaseException(e.getMessage());
        }
    }

    /**
     * 对比
     *
     * @param obj1
     * @param obj2
     * @param pd
     * @param ignoreList
     * @param map
     * @param level
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static void compare(Object obj1, Object obj2, PropertyDescriptor pd, List<String> ignoreList,
        Map<String, List<Object>> map, int level) throws IllegalAccessException, InvocationTargetException {
        String name = pd.getName();
        if (!ignoreList.contains(name)) {
            //获取属性的get方法
            Method readMethod = pd.getReadMethod();
            //在obj1上调用get方法等同于获得obj1的属性值
            Object value1 = readMethod.invoke(obj1);
            //在obj2上调用get方法等同于获得obj2的属性值
            Object value2 = readMethod.invoke(obj2);
            if (value1 != null || value2 != null) {
                if (value1 == null) {
                    /*
                     * 对String 类型增加额外判断
                     */
                    if ((!(value2 instanceof String)) || (StringUtils.isNotEmpty(value2))) {
                        //不一致
                        List<Object> list = new ArrayList<>();
                        list.add(null);
                        list.add(value2);
                        map.put(name, list);
                        /*
                         * 执行覆写操作校验
                         */
                        checkWrite(level, obj1, value2, pd);
                    } else if (StringUtils.isEmpty(value2)) {
                        /*
                         * 执行覆写操作校验
                         *  对传入的  "" 进行替换工作
                         */
                        checkWrite(level, obj1, value2, pd);
                    }

                } else if (!value1.equals(value2)) {
                    List<Object> list = new ArrayList<>();
                    list.add(value1);
                    list.add(value2);
                    map.put(name, list);
                    /*
                     * 执行覆写操作校验
                     */
                    checkWrite(level, obj1, value2, pd);
                }
            }
        }
    }

    /**
     * * 执行覆写操作
     *
     * @param level
     * @param obj1
     * @param value
     * @param pd
     * @throws Exception
     */
    private static void checkWrite(int level, Object obj1, Object value, PropertyDescriptor pd)
        throws IllegalAccessException, InvocationTargetException {
        if (WRITE_DIFFERENT == level) {
            Method writeMethod = pd.getWriteMethod();
            writeMethod.invoke(obj1, value);
        }
    }

    private static List<String> initList(String[] ignoreArr) {
        List<String> ignoreList;
        if (ignoreArr != null && ignoreArr.length > 0) {
            ignoreList = new ArrayList<>(Arrays.asList(ignoreArr));
        } else {
            ignoreList = new ArrayList<>();
        }
        return ignoreList;
    }
}
