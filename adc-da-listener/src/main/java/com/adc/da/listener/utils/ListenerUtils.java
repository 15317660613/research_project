package com.adc.da.listener.utils;

import com.adc.da.util.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.activiti.engine.impl.el.FixedValue;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-11-04
 */
public class ListenerUtils {

    private ListenerUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 受理组变量名
     */
    public static final String ASSIGNEE_LIST = "assigneeList";

    /**
     * 受理人
     */
    public static final String ACT_ASSIGNEE_ID = "ACT_ASSIGNEE_ID";

    /**
     * 部门id
     */
    public static final String ACT_DEPT_ID = "ACT_DEPT_ID";

    /**
     * 特殊流程标识
     */
    public static final String ACT_SPECIAL_PROCESS_FLAG = "ACT_SPECIAL_PROCESS_FLAG";

    /**
     * 对应 条件  隐藏操作
     */
    public static final String HIDE_A = "hide_a";

    /**
     * 项目id
     */
    public static final String ACT_PROJECT_ID = "ACT_PROJECT_ID";

    /**
     * 检查fixedValue 是否为空
     *
     * @param fixedValue
     * @return
     */
    public static boolean fixedValueIsNull(FixedValue fixedValue) {
        return null == fixedValue || StringUtils.isEmpty(fixedValue.getExpressionText());
    }

    /**
     * 初始化FixedValue为String
     *
     * @param defaultValue
     * @param fixedValue
     * @return
     */
    public static String initFixedValueToString(String defaultValue, FixedValue fixedValue) {
        String target;
        if (ListenerUtils.fixedValueIsNull(fixedValue)) {
            /* 默认值 科委会成员  */
            target = defaultValue;
        } else {
            target = fixedValue.getExpressionText();
        }
        return target;
    }

    /**
     * init jsonData
     * @param jsonData
     * @return
     */
    public static Map<String, Object> initGlobalFormDataMap(String jsonData) {

        Gson gson = new Gson();
        Type type = new TypeToken<LinkedTreeMap<String, Object>>() {
        }.getType();

        return gson.fromJson(jsonData, type);
    }
}
