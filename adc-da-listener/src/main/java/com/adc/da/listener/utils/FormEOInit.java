package com.adc.da.listener.utils;

import com.adc.da.form.entity.AdcFormEO;

import java.util.HashMap;
import java.util.Map;

import static com.adc.da.listener.utils.FormType.PROJECT_ID;
import static com.adc.da.listener.utils.FormType.PROJECT_NAME;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-08-02
 */
public class FormEOInit {

    private FormEOInit() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 针对自定义表单的 控件 名称 与 控件id进行绑定
     *
     * @param adcFormEO
     * @return
     */
    public static Map<String, String> initFormMap(AdcFormEO adcFormEO) {

        Map<String, String> formMap = new HashMap<>();

        String[] columnID = adcFormEO.getColumnID().split(",");
        String[] columnName = adcFormEO.getColumnName().split(",");
        int length = columnName.length;
        for (int i = 0; i < length; i++) {
            formMap.put(columnName[i], columnID[i]);
        }
        return formMap;
    }

    /**
     * 解析外层json数据，projectId以及projectName
     *
     * @param baseMap
     */
    public static String[] baseFormDataAnalysis(Map<String, Object> baseMap) {
        String[] formData = new String[2];
        for (Map.Entry<String, Object> entry : baseMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            /* 过滤里程碑相关，以及负责人相关的数据 */
            if (!key.contains("_milepost") && !key.contains("_leader")) {
                /* 项目Id后缀 */
                if (key.contains("_proid")) {
                    formData[PROJECT_ID] = (String) value;
                } else if (key.contains("_proname")) {
                    /* 项目名称后缀 */
                    formData[PROJECT_NAME] = (String) value;
                }
                if (key.contains("projectId")){
                    formData[PROJECT_ID] = (String) value;
                }
            }
        }

        return formData;
    }

}
