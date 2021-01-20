package com.adc.da.listener.service;

import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.repository.UserWithProjectsRepository;
import com.adc.da.form.dao.AdcFormDao;
import com.adc.da.progress.dao.ProjectBudgetChangeEODao;
import com.adc.da.progress.dao.ProjectMilepostEODao;
import com.adc.da.progress.dao.ProjectMilepostResultEODao;
import com.adc.da.progress.service.ProjectBudgetChangeEOService;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-08-02
 */
public class MilestoneResultAnalysisServiceTest {

    @Before
    public void before() throws Exception {
        jsonData = "{\n"
            + "    \"milestoneInfo_id\": \"SQQNVP4C43\",\n"
            + "    \"milestoneResult\": [\n"
            + "        {\n"
            + "            \"id\": \"SQQNVP4C431\",\n"
            + "            \"milepostId\": null,\n"
            + "            \"milepostVersion\": 0,\n"
            + "            \"fileId\": \"SZJZF6ZELE\",\n"
            + "            \"resultFileName\": \"hgaha.jpg\",\n"
            + "            \"fileName\": \"del\",\n"
            + "            \"fileSize\": \"427B\",\n"
            + "            \"uploadUserName\": \"管理员1号\",\n"
            + "            \"uploadUserId\": \"GHVRTMA9H2\",\n"
            + "            \"uploadTime\": 1565137458446,\n"
            + "            \"LAY_TABLE_INDEX\": 0\n"
            + "        },\n"
            + "        {\n"
            + "            \"id\": \"SQQNVP4C4355\",\n"
            + "            \"milepostId\": null,\n"
            + "            \"milepostVersion\": 0,\n"
            + "            \"fileId\": \"2M6YQKQ29U\",\n"
            + "            \"resultFileName\": \"fadjh.pdf\",\n"
            + "            \"fileName\": \"bumenyuangongguanli@2x\",\n"
            + "            \"fileSize\": \"1KB\",\n"
            + "            \"uploadUserName\": \"管理员1号\",\n"
            + "            \"uploadUserId\": \"GHVRTMA9H2\",\n"
            + "            \"uploadTime\": 1565137462572,\n"
            + "            \"LAY_TABLE_INDEX\": 1\n"
            + "        }\n"
            + "    ]\n"
            + "}";

    }

    private String jsonData;


    @Test
    public void a() throws Exception {
        Gson gson = new Gson();

        Type type = new TypeToken<LinkedTreeMap<String, Object>>() {
        }.getType();

        Map<String, Object>   jsonMap = gson.fromJson(jsonData, type);

        String jsonString = gson.toJson(jsonMap.get("milestoneResult"), ArrayList.class);
        Type typeList = new TypeToken<ArrayList<LinkedTreeMap<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> jsonMapLevelTwo = gson.fromJson(jsonString, typeList);


        for (Map<String,Object> map:jsonMapLevelTwo){
             map.get("id");
            map.get("fileId");
        }

        //todo save ts_file
        log.println(jsonMapLevelTwo);

    }

}
