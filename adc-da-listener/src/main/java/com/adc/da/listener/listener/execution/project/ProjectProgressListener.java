package com.adc.da.listener.listener.execution.project;

import com.adc.da.listener.utils.RSProjectBusinessKey;
import com.adc.da.progress.dao.ProjectNameEODao;
import com.adc.da.progress.entity.ProjectNameEO;
import com.adc.da.progress.entity.ProjectRateEO;
import com.adc.da.progress.page.ProjectNameEOPage;
import com.adc.da.progress.service.ProjectRateEOService;
import com.adc.da.research.service.EndBaseEOService;
import com.adc.da.research.service.InfoEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.GsonUtil;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adc.da.budget.constant.ProjectType.BUSINESS_PROJECT;
import static com.adc.da.budget.constant.ProjectType.NO_BUSINESS_PROJECT;
import static com.adc.da.listener.utils.RSProjectBusinessKey.RS_PROJECT_ID;
import static com.adc.da.research.utils.HiConstant.BUSINESS_STATUS;
import static com.adc.da.research.utils.RSPendingStatus.RS_PENDING_IN_PROCESS;

/**
 * describe:
 * 项目进度初始化流程
 *
 * ${projectProgressListener}
 *
 * @author 李坤澔
 *     date 2019-07-23
 */
@Component("projectProgressListener")
@Slf4j
public class ProjectProgressListener implements ExecutionListener {

    @Autowired
    private transient ProjectNameEODao projectNameEODao;

    @Autowired
    private transient ProjectRateEOService projectRateEOService;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        ProjectRateEO projectRateEO = new ProjectRateEO();
        /*设置流程实例id*/
        projectRateEO.setProcInstId(delegateExecution.getProcessInstanceId());

        /*
         *  select_1561724244496_proid
         *  设置projectId
         */
        String jsonData = (String) delegateExecution.getVariable("globalFormData");
        @SuppressWarnings("unchecked")
        Map<String, String> map = (HashMap<String, String>) GsonUtil.fromJson(jsonData, HashMap.class);
        String[] projectId = new String[2];

        boolean specialFlag = isSpecialProcess(delegateExecution);
        /* MM3TP78PBQ:RSC_M74BSW4TDU */
        String businessKey = delegateExecution.getProcessBusinessKey();
        String[] key;
        /* 科研类，外部表单 */
        if (StringUtils.isNotEmpty(businessKey) && businessKey.contains("RS")) {
            key = RSProjectBusinessKey.spitBusinessKey(businessKey);
            projectId[NO_BUSINESS_PROJECT] = key[RS_PROJECT_ID];
        } else {
            /* 自定义表单 */
            initProjectIdByCustomerForm(projectId, map);
            key = new String[2];
        }

        if (StringUtils.isNotEmpty(projectId[NO_BUSINESS_PROJECT])) {
            projectRateEO.setProjectId(projectId[NO_BUSINESS_PROJECT]);
        } else if (StringUtils.isNotEmpty(projectId[BUSINESS_PROJECT])) {
            projectRateEO.setProjectId(projectId[BUSINESS_PROJECT]);
        } else {
            log.warn("ProjectProgressListener projectId is null");
            throw new AdcDaBaseException("项目进度初始化，项目信息获取异常");
        }

        /*
         * 设置ProcessNameId
         */
        ProjectNameEOPage queryInfo = new ProjectNameEOPage();
        String[] processDefinition = delegateExecution.getProcessDefinitionId().split(":");
        queryInfo.setProcDefKey(processDefinition[0]);
        List<ProjectNameEO> projectNameEOList = projectNameEODao.queryByList(queryInfo);
        if (CollectionUtils.isNotEmpty(projectNameEOList)) {
            projectRateEO.setProcessNameId(projectNameEOList.get(0).getId());
        } else {
            log.warn("ProjectProgressListener projectNameEOList is null");
            throw new AdcDaBaseException("项目进度初始化失败");
        }

        /*
         *  设置时间
         */
        Date nowDate = new Date();
        projectRateEO.setCreateTime(nowDate);
        projectRateEO.setModifiedTime(nowDate);

        projectRateEOService.insert(projectRateEO);
        /*
         *  变更流程 发起流程后更新状态
         */
        if (specialFlag) {
            /*
             * key 0 projectId
             * key 1 businessKey
             */
            infoEOService.checkAndUpdateStatus(key[1], key[0], RS_PENDING_IN_PROCESS);
            /*
             * 更新对应的基础表状态
             */
            endBaseEOService.updateHiBaseEOStatus(key[1], BUSINESS_STATUS, key[1].contains("RSC"));
        }
    }

    @Autowired
    private transient EndBaseEOService endBaseEOService;

    @Autowired
    private transient InfoEOService infoEOService;

    /**
     * 涉及科研 变更 与 结项 流程
     *
     * @param delegateExecution
     * @return
     */
    private boolean isSpecialProcess(DelegateExecution delegateExecution) {
        String procKey = delegateExecution.getProcessDefinitionId().split(":")[0];
        String[] specialKeyArray = {"pa0001fb0970341808dd0869ab05c8a3c",
            "p9d12f5b27f9a482c8772de52d2825334"};
        return Arrays.asList(specialKeyArray).contains(procKey);
    }

    /**
     * 自定义表单类 获取项目 id
     *
     * @param projectId
     * @param map
     */
    private void initProjectIdByCustomerForm(String[] projectId, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            /*
             *  经营类项目id 获取
             */
            if (!key.contains("_milepost")
                && !key.contains("_leader")
                && key.contains("_proid")) {

                projectId[BUSINESS_PROJECT] = entry.getValue();
            }
            if (key.contains("projectId")){
                projectId[BUSINESS_PROJECT] = entry.getValue();
            }
            /*
             * 科研类项目id 获取  涉及 中期检查，节点检查，等
             */
            if (key.contains("selectBusiness_") && key.contains("_proid")) {
                projectId[NO_BUSINESS_PROJECT] = entry.getValue();
            }

        }
    }

}
