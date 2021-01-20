package com.adc.da.listener.listener.execution.approve;

import com.adc.da.budget.entity.Project;
import com.adc.da.form.dao.AdcFormDao;
import com.adc.da.form.entity.AdcFormEO;
import com.adc.da.listener.utils.FormEOInit;
import com.adc.da.listener.utils.ListenerUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.workflow.utils.contants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.FormService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.form.StartFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

import static com.adc.da.listener.utils.FormKeyWord.UNDERLINE;
import static com.adc.da.listener.utils.FormKeyWord.US_ID;
import static com.adc.da.listener.utils.FormKeyWord.YES;
import static com.adc.da.listener.utils.FormType.INT_BEGIN;
import static com.adc.da.listener.utils.FormType.INT_END;
import static com.adc.da.listener.utils.FormType.INT_NEW;
import static com.adc.da.listener.utils.FormType.INT_OLD;
import static com.adc.da.listener.utils.ListenerUtils.HIDE_A;
import static com.adc.da.workflow.utils.contants.TaskCompleteType.APPROVE;

/**
 * 针对流程管理系列流程  项目变更流程
 * [项目管理-03-4]-部门负责人审核
 * ${setProjectChangeApproveListener}
 *
 * @author Lee Kwanho 李坤澔
 */
@Component
@Slf4j
public class SetProjectChangeApproveListener implements ExecutionListener {

    private Map<String, String> formMap;

    private transient Map<String, Object> jsonMap;

    @Autowired
    private transient AdcFormDao adcFormDao;

    @Autowired
    private transient FormService formService;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        if ("agree".equals(delegateExecution.getVariable("approve"))) {

            StartFormData startFormData = formService.getStartFormData(delegateExecution.getProcessDefinitionId());
            String formKey;

            if (startFormData != null) {
                formKey = startFormData.getFormKey();
            } else {
                throw new AdcDaBaseException("form no Found");
            }
            AdcFormEO adcFormEO = adcFormDao.selectByPrimaryKey(formKey);
            formMap = FormEOInit.initFormMap(adcFormEO);

            /*
             * 获取表单数据
             */
            jsonMap = ListenerUtils
                .initGlobalFormDataMap(delegateExecution.getVariable(Constants.GLOBAL_FORM_DATA, String.class));

            Project project;
            project = new Project();

            project.setModifyTime(new Date());



            /*  进行负责人判断      */
            if (changeCheck("项目组负责人变更")) {
                String newLeaderId = (String) jsonMap.get(formMap.get("新负责人") + UNDERLINE + US_ID);

                delegateExecution.setVariable("newLeaderId", newLeaderId);
                delegateExecution.setVariable(APPROVE, "hide_b");
                return;
            }

            /*  部门变更      */
            if (changeCheck("项目承担部门变更")) {
                return;
            }

            //取消时间变更校验，时间变更到负责人结束


            /*         * 预算成本         */
            if (changeCheck("项目成本预算变更") && checkBudget()) {
                return;
            }

            /*
             * 都未超额，则完成流程
             */
            delegateExecution.setVariable(APPROVE, HIDE_A);

        }
    }

    /**
     * 进行成本预算的判断，如果新总额大于原，返回true
     */
    private boolean checkBudget() {

        String[] costStr = new String[2];
        float[] cost = new float[2];

        costStr[INT_NEW] = "新成本预算总金额";
        costStr[INT_OLD] = "原成本预算总金额";

        String[] s = new String[2];

        s[INT_NEW] = (String) jsonMap.get(formMap.get(costStr[INT_NEW]));
        s[INT_OLD] = (String) jsonMap.get(formMap.get(costStr[INT_OLD]));

        if (StringUtils.isNotEmpty(s[INT_NEW])) {
            cost[INT_NEW] = Float.parseFloat(s[INT_NEW]);
        } else {
            cost[INT_NEW] = 0.0F;
        }

        if (StringUtils.isNotEmpty(s[INT_OLD])) {
            cost[INT_OLD] = Float.parseFloat(s[INT_OLD]);
        } else {
            cost[INT_OLD] = 0.0F;
        }

        return cost[INT_NEW] > cost[INT_OLD];

    }

    /**
     * 针对项目 时间变更进行调整
     * 若项目延期，返回true
     *
     * @return
     */
    private boolean setProjectDate() {
        String[][] time = new String[2][2];

        time[INT_NEW][INT_BEGIN] = (String) jsonMap.get(formMap.get("新开始时间"));
        time[INT_NEW][INT_END] = (String) jsonMap.get(formMap.get("新结束时间"));

        time[INT_OLD][INT_BEGIN] = (String) jsonMap.get(formMap.get("原开始时间"));
        time[INT_OLD][INT_END] = (String) jsonMap.get(formMap.get("原结束时间"));

        Date[][] date = new Date[2][2];
        long[] timeArea = new long[2];
        try {

            if (StringUtils.isNotEmpty(time[INT_NEW][INT_END])) {
                date[INT_NEW][INT_END] = DateUtils.stringToDate(time[INT_NEW][INT_END], DateUtils.YYYY_MM_DD_EN);
                date[INT_NEW][INT_BEGIN] = DateUtils.stringToDate(time[INT_NEW][INT_BEGIN], DateUtils.YYYY_MM_DD_EN);
                timeArea[INT_NEW] = date[INT_NEW][INT_END].getTime() - date[INT_NEW][INT_BEGIN].getTime();
            } else {
                throw new AdcDaBaseException("项目时间变更，项目起止时间 不能为空");
            }
            /*
             * 原结束时间为空，判断认为不延期
             */
            if (StringUtils.isNotEmpty(time[INT_OLD][INT_END]) && StringUtils.isNotEmpty(time[INT_OLD][INT_BEGIN])) {
                date[INT_OLD][INT_END] = DateUtils.stringToDate(time[INT_OLD][INT_END], DateUtils.YYYY_MM_DD_EN);
                date[INT_OLD][INT_BEGIN] = DateUtils.stringToDate(time[INT_OLD][INT_BEGIN], DateUtils.YYYY_MM_DD_EN);
                timeArea[INT_OLD] = date[INT_OLD][INT_END].getTime() - date[INT_OLD][INT_BEGIN].getTime();

            } else {
                return false;
            }

        } catch (Exception e) {
            throw new AdcDaBaseException("项目时间变更，项目起止时间输入异常");
        }

        return date[INT_NEW][INT_END].getTime() > date[INT_OLD][INT_END].getTime()
            || timeArea[INT_NEW] > timeArea[INT_OLD];
    }

    /**
     * 用于判断变更的单选框
     *
     * @param field
     * @return
     */
    private boolean changeCheck(String field) {
        return YES.equals(jsonMap.get(formMap.get(field)));
    }
}

