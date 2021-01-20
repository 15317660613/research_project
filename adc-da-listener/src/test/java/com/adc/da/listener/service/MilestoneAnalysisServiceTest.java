package com.adc.da.listener.service;

import com.adc.da.listener.entity.MilestoneEO;
import com.adc.da.progress.entity.ProjectMilepostEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class MilestoneAnalysisServiceTest {
    @Test
    public void a() throws Exception {
        String json = "{\n"
            + "    \"select_1564473766397\": \"(卡达克)力帆集团ELV法规年度运维服务合同-CAMDS\",\n"
            + "    \"inputorgselect_1564473772934\": \"软件开发部\",\n"
            + "    \"inputuserselect_1564473799414\": \"管理员1号\",\n"
            + "    \"file_1564473803912_fileid\": \"\",\n"
            + "    \"textarea_1564473828525\": \"\",\n"
            + "    \"input_1564477208736_milepost_1\": \"里程碑名称1\",\n"
            + "    \"inputuserselect_1564477408015_milepost_1\": \"管理员1号\",\n"
            + "    \"textarea_1564477452951_milepost_1\": \"里程碑目标1\",\n"
            + "    \"datetime_1564550941239_milepost_1\": \"2019-07-16 - 2019-08-16\",\n"
            + "    \"input_1564539333000_milepost_1_1\": \"预计成果物11\",\n"
            + "    \"input_1564559385842_milepost_1_2\": \"预计成果物12\",\n"
            + "    \"input_1564559381985_milepost_2\": \"里程碑名称2\",\n"
            + "    \"inputuserselect_1564559381985_milepost_2\": \"管理员1号\",\n"
            + "    \"textarea_1564559381985_milepost_2\": \"里程碑目标2\",\n"
            + "    \"datetime_1564559381985_milepost_2\": \"2019-07-22 - 2019-08-22\",\n"
            + "    \"input_1564559381985_milepost_2_1\": \"预计成果物21\",\n"
            + "    \"input_1564559405331_milepost_2_2\": \"预计成果物22\",\n"
            + "    \"milepostArr\": [\n"
            + "        {\n"
            + "            \"name\": {\n"
            + "                \"value\": \"里程碑名称1\",\n"
            + "                \"name\": \"input_1564477208736_milepost_1\"\n"
            + "            },\n"
            + "            \"user\": {\n"
            + "                \"usid\": \"用户id\",\n"
            + "                \"name\": \"input_1564477208736_milepost_1\"\n"
            + "            },\n"
            + "            \"target\": {\n"
            + "                \"value\": \"里程碑目标1\",\n"
            + "                \"name\": \"textarea_1564477452951_milepost_1\"\n"
            + "            },\n"
            + "            \"date\": {\n"
            + "                \"value\": \"2019-07-16 - 2019-08-16\",\n"
            + "                \"name\": \"datetime_1564550941239_milepost_1\"\n"
            + "            },\n"
            + "            \"outcomes\": [\n"
            + "                {\n"
            + "                    \"value\": \"预计成果物11\",\n"
            + "                    \"name\": \"input_1564539333000_milepost_1_1\"\n"
            + "                },\n"
            + "                {\n"
            + "                    \"value\": \"预计成果物12\",\n"
            + "                    \"name\": \"input_1564559385842_milepost_1_2\"\n"
            + "                }\n"
            + "            ]\n"
            + "        },\n"
            + "        {\n"
            + "            \"name\": {\n"
            + "                \"value\": \"里程碑名称2\",\n"
            + "                \"name\": \"input_1564559381985_milepost_2\"\n"
            + "            },\n"
            + "            \"user\": {\n"
            + "                \"name\": \"inputuserselect_1564559381985_milepost_2\"\n"
            + "            },\n"
            + "            \"target\": {\n"
            + "                \"value\": \"里程碑目标2\",\n"
            + "                \"name\": \"textarea_1564559381985_milepost_2\"\n"
            + "            },\n"
            + "            \"date\": {\n"
            + "                \"value\": \"2019-07-22 - 2019-08-22\",\n"
            + "                \"name\": \"datetime_1564559381985_milepost_2\"\n"
            + "            },\n"
            + "            \"outcomes\": [\n"
            + "                {\n"
            + "                    \"value\": \"预计成果物21\",\n"
            + "                    \"name\": \"input_1564559381985_milepost_2_1\"\n"
            + "                },\n"
            + "                {\n"
            + "                    \"value\": \"预计成果物22\",\n"
            + "                    \"name\": \"input_1564559405331_milepost_2_2\"\n"
            + "                }\n"
            + "            ]\n"
            + "        }\n"
            + "    ],\n"
            + "    \"milepostList\": 2,\n"
            + "    \"select_1564473766397_proid\": \"TXYFPTP8E5\",\n"
            + "    \"select_1564473766397_proname\": \"(卡达克)力帆集团ELV法规年度运维服务合同-CAMDS\",\n"
            + "    \"inputorgselect_1564473772934_id\": \"MEGMW98MVJ\",\n"
            + "    \"inputorgselect_1564473772934_name\": \"软件开发部\",\n"
            + "    \"inputuserselect_1564473799414_usid\": \"GHVRTMA9H2\",\n"
            + "    \"inputuserselect_1564473799414_usname\": \"管理员1号\",\n"
            + "    \"inputuserselect_1564477408015_milepost_1_usid\": \"GHVRTMA9H2\",\n"
            + "    \"inputuserselect_1564477408015_milepost_1_usname\": \"管理员1号\",\n"
            + "    \"inputuserselect_1564559381985_milepost_2_usid\": \"GHVRTMA9H2\",\n"
            + "    \"inputuserselect_1564559381985_milepost_2_usname\": \"管理员1号\"\n"
            + "}";

        Gson gson = new Gson();

        Type type = new TypeToken<LinkedTreeMap<String, Object>>() {
        }.getType();

        Map<String, Object> map = gson.fromJson(json, type);

        baseFormDataAnalysis(map);

        /*
         * 获取里程碑详细信息
         */

        String jsonString = gson.toJson(map.get("milepostArr"), ArrayList.class);
        Type type1 = new TypeToken<ArrayList<MilestoneEO>>() {
        }.getType();

        List<MilestoneEO> milestoneEOList = gson.fromJson(jsonString, type1);

        log.println(jsonString);
        ProjectMilepostEO baseEO = new ProjectMilepostEO();

        baseEO.setProjectId(formData[PROJECT_ID]);
        baseEO.setProjectName(formData[PROJECT_NAME]);

        String startTime;
        String endTime;
        String[] timePeriod;
        String id;
        String dateInfo;
        List<ProjectMilepostEO> resultList = new ArrayList<>(milestoneEOList.size());
        for (MilestoneEO eo : milestoneEOList) {
            ProjectMilepostEO resultEO = new ProjectMilepostEO();
            BeanUtils.copyProperties(baseEO, resultEO);

            /*
             * 优先校验里程碑起止时间
             */
            if (null != eo.getDate()) {
                dateInfo = eo.getDate().get("value");
                if (StringUtils.isNotEmpty(dateInfo)) {
                    timePeriod = dateInfo.split(" - ");
                    startTime = timePeriod[0];
                    endTime = timePeriod[1];

                } else {
                    throw new AdcDaBaseException("里程碑日期为空");
                }
            } else {
                throw new AdcDaBaseException("里程碑日期为空");
            }
            id = UUID.randomUUID10();

            resultEO.setId(id);
            resultEO.setMilepostBeginTime(DateUtils.stringToDate(startTime, DateUtils.YYYY_MM_DD_EN));
            resultEO.setMilepostEndTime(DateUtils.stringToDate(endTime, DateUtils.YYYY_MM_DD_EN));

            resultEO.setMilepostManagerId(eo.getUserIdValue());
            resultEO.setMilepostVersion(0);
            resultEO.setFinishStatus(0);
            resultEO.setMilepostName(eo.getNameValue());
            resultEO.setMilepostTarget(eo.getTargetValue());

            resultList.add(resultEO);
        }
        return;
    }

    private String[] formData;

    private static final int PROJECT_ID = 0;

    private static final int PROJECT_NAME = 1;

    private void baseFormDataAnalysis(Map<String, Object> baseMap) {
        formData = new String[2];
        for (Map.Entry<String, Object> entry : baseMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (!key.contains("_milepost") && key.contains("_proid")) {
                formData[PROJECT_ID] = (String) value;
            } else if (key.contains("_proname")) {
                formData[PROJECT_NAME] = (String) value;
            }
            if (key.contains("projectId")){
                formData[PROJECT_ID] = (String) value;
            }
        }
    }

}