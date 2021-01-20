package com.adc.da.crm.util;

/**
 * @description
 * @author ZhengZhiwei
 * @date create at 16:33 2018/11/28
 * @modified by
 */

import com.adc.da.crm.annotation.MatchField;
import com.adc.da.crm.annotation.MatchFieldCollection;
import com.adc.da.form.entity.AdcFormDataEO;
import com.adc.da.util.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * //解析自定义表单的json数据
 *
 * @author ZhengZhiwei
 * @date 2018-11-28
 */
public class AutoMatchMethodArgumentUtil {

    private final static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private final static String SEARCH_STR  = "_expand_";

    private static Map<String, String> columnMap;
    private static Map map;

    public static Object json2entity(AdcFormDataEO adcFormDataEO, Class clazz){
        try {
            //通过表单id获取表单信息
            //拆分表单字段名与字段id
            String[] columnName = adcFormDataEO.getAdcFormEO().getColumnName().split(",");
            String[] columnId= adcFormDataEO.getAdcFormEO().getColumnID().split(",");
            columnMap = new HashMap<>();
            //字段名的元素个数与字段id的元素个数是一致的
            if(columnName.length != columnId.length) {
                return null;
            }
            //组合map
            for (int i = 0; i < columnName.length; i++) {
                columnMap.put(columnName[i], columnId[i]);
            }
            String jsonData = adcFormDataEO.getFormContent();
            //解析JSON串转为Map对象保存
            map = JsonUtil.jsonToBean(jsonData, HashMap.class);
            if (map == null || map.isEmpty()) return null;

            //处理可扩展的表单数据
//            String clazzSimpleName = clazz.getSimpleName();
//            Map finalMap = MapDataHandlingUtil.dealExpandData(map, clazzSimpleName, columnMap);


            //开始匹配
            return  matchField(clazz, null);

            //====================================================================

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    private static Object matchField(Class clazz, String suffix) throws ClassNotFoundException, InstantiationException, IllegalAccessException{

        //生成注解参数对应的类对象
        Class autoMatchClass = Class.forName(clazz.getName());
        //获取所生成类的所有字段
        Field[] field = autoMatchClass.getDeclaredFields();
        if (field == null || field.length == 0) return null;
        //获取类实例
        Object o = autoMatchClass.newInstance();
        for (Field fie : field) {
            //如果字段上有@MatchField注解
            if (fie.isAnnotationPresent(MatchField.class)) {
                MatchField userField = fie.getAnnotation(MatchField.class);
                //获取匹配的字段值
                String matchfield = userField.value();
                //如果字段注解名称存在
                if (columnMap.containsKey(matchfield)) {
                    //字段权限设置为可访问
                    fie.setAccessible(true);
                    //获取参数值
                    String k = columnMap.get(matchfield);
                    if (StringUtils.contains(k, "file")) {
                        k += "_fileid";
                    }
                    if (StringUtils.contains(k, "user")) {
                        k += "_usid";
                    }
                    if (StringUtils.contains(k, "torg")) {
                        k += "_id";
                    }
                    if(suffix != null){
                        k += SEARCH_STR + suffix;
                    }
                    Object v = map.get(k);
                    //判断字段是否是日期类型 如果是需要转化参数值格式
                    String type = fie.getType().getSimpleName();
                    if (StringUtils.equals(type, "Date")) {
                        Date date = DateUtils.stringToDate(v.toString(), "yyyy-MM-dd");
                        //给字段赋值
                        fie.set(o, date);
                    }else if (StringUtils.equals(type, "Long")) {
                        Long l = v == null? null : StringUtils.equals(v.toString(),"")?0l:Long.parseLong(v.toString());
                        //给字段赋值
                        fie.set(o, l);
                    } else {
                        //给字段赋值
                        fie.set(o, v);
                    }
                }
            }else if(fie.isAnnotationPresent(MatchFieldCollection.class)){
                //list集合
                MatchFieldCollection userField = fie.getAnnotation(MatchFieldCollection.class);
                //获取泛型类型
                Type genericType = fie.getGenericType();
                if(genericType == null)
                    return null;
                if((genericType instanceof ParameterizedType) == false){
                    return null;
                }
                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                Class<?> actualTypeArgument = (Class<?>)parameterizedType.getActualTypeArguments()[0];
                Set<String> expandSet = new HashSet<>();
                //所有去重 input_1543303738513_expand_6fFbmte6
                Iterator entries = map.entrySet().iterator();
                List<Object> fieldCollection = new ArrayList<>();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    String key = entry.getKey().toString();
                    //筛选出key中带expand的元素
                    if(StringUtils.contains(key, SEARCH_STR)){
                        //分割前缀后缀
                        String[] columnName = key.split(SEARCH_STR);
                        if(expandSet.add(columnName[1]) == false)
                            continue;
                        //检查是否是对应的实体
                        Field[] fields = Class.forName(actualTypeArgument.getName()).getDeclaredFields();
                        for (Field fieldItem : fields){
                            if (fieldItem.isAnnotationPresent(MatchField.class)) {
                                MatchField entityField = fieldItem.getAnnotation(MatchField.class);
                                //获取匹配的字段值
                                String matchfield = entityField.value();
                                if (columnMap.get(matchfield).equals(columnName[0])){
                                    fieldCollection.add(matchField(actualTypeArgument, columnName[1]));
                                    break;
                                }
                            }
                        }
                    }
                }
                fie.setAccessible(true);
                fie.set(o, fieldCollection);
            }
        }
        return o;
    }

}
