package com.adc.da.listener.service;

import com.adc.da.file.dao.MyFileEODao;
import com.adc.da.file.entity.MyFileEO;
import com.adc.da.listener.utils.ListenerUtils;
import com.adc.da.progress.dao.ProjectMilepostEODao;
import com.adc.da.progress.entity.ProjectMilepostEO;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.workflow.utils.contants.Constants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * describe:
 * 里程碑 以及 交付物 解析 serviceTask
 * ${milestoneResultAnalysisService}
 *
 * @author 李坤澔
 *     date 2019-08-01
 */
@Component("milestoneResultAnalysisService")
@Slf4j
public class MilestoneResultAnalysisService implements JavaDelegate {

    @Autowired
    private MyFileEODao dao;

    @Autowired
    private ProjectMilepostEODao milepostEODao;

    @Override
    public void execute(DelegateExecution execution) {

        /*
         * 获取表单数据
         */
        Map<String, Object> jsonMap = ListenerUtils
            .initGlobalFormDataMap((String) execution.getVariable(Constants.GLOBAL_FORM_DATA));
        Gson gson = new Gson();
        String jsonString = gson.toJson(jsonMap.get("milestoneResult"), ArrayList.class);
        Type typeList = new TypeToken<ArrayList<LinkedTreeMap<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> jsonMapLevelTwo = gson.fromJson(jsonString, typeList);

        MyFileEO myFileEO = new MyFileEO();
        for (Map<String, Object> map : jsonMapLevelTwo) {
            if (null != map.get("fileId")) {
                myFileEO.setFileId((String) map.get("fileId"));
                myFileEO.setRemark("");
                /*
                 * 只更新 ForeignId
                 */
                dao.updateByPrimaryKeySelective(myFileEO);
            }
        }
        String milestoneId = (String) jsonMap.get("milestoneInfo_id");

        if (StringUtils.isNotEmpty(milestoneId)) {
            ProjectMilepostEO projectMilepostEO = new ProjectMilepostEO();
            projectMilepostEO.setId(milestoneId);
            projectMilepostEO.setFinishStatus(9);
            projectMilepostEO.setFinishTime(new Date());
            milepostEODao.updateByPrimaryKeySelective(projectMilepostEO);
        }

    }

}
