package com.adc.da.listener.service;

import com.adc.da.listener.entity.MilestoneEO;
import com.adc.da.listener.utils.FormEOInit;
import com.adc.da.listener.utils.ListenerUtils;
import com.adc.da.progress.dao.ProjectMilepostEODao;
import com.adc.da.progress.dao.ProjectMilepostResultEODao;
import com.adc.da.progress.dao.ProjectNameEODao;
import com.adc.da.progress.dao.ProjectRateEODao;
import com.adc.da.progress.entity.ProjectMilepostEO;
import com.adc.da.progress.entity.ProjectMilepostResultEO;
import com.adc.da.progress.entity.ProjectNameEO;
import com.adc.da.progress.page.ProjectNameEOPage;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import com.adc.da.workflow.utils.contants.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.adc.da.listener.utils.FormKeyWord.VALUE;
import static com.adc.da.listener.utils.FormType.PROJECT_ID;
import static com.adc.da.listener.utils.FormType.PROJECT_NAME;

/**
 * describe:
 * 里程碑 以及 交付物 解析 serviceTask
 * ${milestoneAnalysisService}
 *
 * @author 李坤澔
 *     date 2019-08-01
 */
@Component("milestoneAnalysisService")
@Slf4j
public class MilestoneAnalysisService implements JavaDelegate {

    /**
     * 里程碑
     */
    @Autowired
    private ProjectMilepostEODao projectMilepostEODao;

    /**
     * 交付物
     */
    @Autowired
    private ProjectMilepostResultEODao projectMilepostResultEODao;

    @Autowired
    private ProjectRateEODao projectRateEODao;

    @Autowired
    private ProjectNameEODao projectNameEODao;

    @Override
    public void execute(DelegateExecution execution) {

        String processDefinitionKey = execution.getProcessDefinitionId().split(":")[0];

        /*
         * 获取表单数据
         */
        Map<String, Object> map = ListenerUtils
            .initGlobalFormDataMap((String) execution.getVariable(Constants.GLOBAL_FORM_DATA));
        /*
         * 表单项目id项目名称初始化
         */
        String[] formData = FormEOInit.baseFormDataAnalysis(map);

        /*
         * 获取里程碑详细信息,先转回json，再转成 List<MileStoneEO>
         */
        Gson gson = new Gson();
        String jsonString = gson.toJson(map.get("milepostArr"), ArrayList.class);
        Type typeArrayList = new TypeToken<ArrayList<MilestoneEO>>() {
        }.getType();

        List<MilestoneEO> milestoneEOList = gson.fromJson(jsonString, typeArrayList);

        ProjectMilepostEO baseEO = new ProjectMilepostEO();

        baseEO.setProjectId(formData[PROJECT_ID]);
        baseEO.setProjectName(formData[PROJECT_NAME]);
        ProjectNameEOPage projectNameEOPage = new ProjectNameEOPage();
        projectNameEOPage.setProcDefKey(processDefinitionKey);
        List<ProjectNameEO> projectNameEOList = projectNameEODao.queryByList(projectNameEOPage);
        if (CollectionUtils.isNotEmpty(projectNameEOList)) {
            String stageId = projectNameEOList.get(0).getStageOrderId();
            baseEO.setStageId(formData[PROJECT_ID] + stageId);
        }
        String startTime;
        String endTime;
        String[] timePeriod;
        String resultEOId;
        String dateInfo;
        List<ProjectMilepostEO> resultList = new ArrayList<>(milestoneEOList.size());

        List<ProjectMilepostResultEO> subResultList = new ArrayList<>();

        for (MilestoneEO eo : milestoneEOList) {
            ProjectMilepostEO resultEO = new ProjectMilepostEO();
            BeanUtils.copyProperties(baseEO, resultEO);

            /*
             * 优先校验里程碑起止时间
             */
            if (null != eo.getDate()) {
                dateInfo = eo.getDate().get(VALUE);
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
            resultEOId = UUID.randomUUID10();

            resultEO.setId(resultEOId);
            resultEO.setMilepostBeginTime(DateUtils.stringToDate(startTime, DateUtils.YYYY_MM_DD_EN));
            resultEO.setMilepostEndTime(DateUtils.stringToDate(endTime, DateUtils.YYYY_MM_DD_EN));

            /*
             * 设置负责人信息
             */
            resultEO.setMilepostManagerId(eo.getUserIdValue());
            resultEO.setMilepostManagerName(eo.getUserNameValue());
            /*
             *里程碑名称与目标
             */
            resultEO.setMilepostName(eo.getNameValue());
            resultEO.setMilepostTarget(eo.getTargetValue());

            resultList.add(resultEO);

            /*
             * 交付物初始化
             */
            List<String> outcomesValue = eo.getOutcomesValue();

            ProjectMilepostResultEO subBaseEO = new ProjectMilepostResultEO();
            subBaseEO.setMilepostId(resultEOId);

            subResultList.addAll(setSubResult(subBaseEO, outcomesValue));

        }

        if (CollectionUtils.isNotEmpty(resultList)) {
            projectMilepostEODao.insertList(resultList);
            if (CollectionUtils.isNotEmpty(subResultList)) {
                projectMilepostResultEODao.insertList(subResultList);
            }

        } else {
            log.warn("milestone is null");
        }
    }

    /**
     * 对交付物进行初始化,
     *
     * @param subBaseEO
     * @param outcomesValue
     */
    private List<ProjectMilepostResultEO> setSubResult(ProjectMilepostResultEO subBaseEO, List<String> outcomesValue) {
        List<ProjectMilepostResultEO> subResultList = new ArrayList<>();

        for (String outcomeValue : outcomesValue) {
            ProjectMilepostResultEO subResult = new ProjectMilepostResultEO();

            BeanUtils.copyProperties(subBaseEO, subResult);

            subResult.setId(UUID.randomUUID10());
            subResult.setFileName(outcomeValue);
            subResultList.add(subResult);
        }
        return subResultList;
    }

}
