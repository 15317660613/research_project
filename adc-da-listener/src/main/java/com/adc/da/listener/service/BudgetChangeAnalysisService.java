package com.adc.da.listener.service;

import com.adc.da.budget.utils.JsonUtil;
import com.adc.da.form.dao.AdcFormDao;
import com.adc.da.form.entity.AdcFormEO;
import com.adc.da.listener.utils.FormEOInit;
import com.adc.da.processform.dao.ProjectContractInvoiceEODao;
import com.adc.da.progress.dao.ProjectBudgetChangeEODao;
import com.adc.da.progress.entity.ProjectBudgetChangeEO;
import com.adc.da.progress.page.ProjectBudgetChangeEOPage;
import com.adc.da.progress.service.ProjectBudgetChangeEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import com.adc.da.workflow.utils.contants.Constants;
import com.adc.da.workflow.utils.contants.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.FormService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.form.StartFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adc.da.listener.utils.FormType.PROJECT_ID;
import static com.adc.da.listener.utils.FormType.PROJECT_NAME;

/**
 * describe:
 * 项目管理-业务承接-项目成本预算管理02-2 ServiceTask 执行类
 * 包含扩展表格的 解析方案
 * ${budgetChangeAnalysisService}
 *
 * @author 李坤澔
 *     date 2018-11-30
 */
@Component("budgetChangeAnalysisService")
@Slf4j
public class BudgetChangeAnalysisService implements JavaDelegate {

    @Autowired
    private AdcFormDao adcFormDao;

    @Autowired
    private ProjectBudgetChangeEODao dao;

    @Autowired
    private ProjectBudgetChangeEOService budgetChangeService;

    /**
     * 表单数据
     */
    private Map<String, String> jsonMap;

    /**
     * 表单内容数据
     */
    private Map<String, String> formMap;

    @Autowired
    private FormService formService;

    @Autowired
    private ProjectContractInvoiceEODao projectContractInvoiceEODao;

    @Override
    public void execute(DelegateExecution execution) {

        try {
            /*
             * 启动表单id获取
             */
            StartFormData startFormData = formService.getStartFormData(execution.getProcessDefinitionId());
            String formKey;

            if (startFormData != null) {
                formKey = startFormData.getFormKey();
            } else {
                throw new AdcDaBaseException(ErrorType.TASK_GET_FORM_ERROR);
            }

            AdcFormEO adcFormEO = adcFormDao.selectByPrimaryKey(formKey);
            /*
             * 对表单控件名称与id进行绑定
             */
            formMap = FormEOInit.initFormMap(adcFormEO);

            /*
             *  控件id与控件value进行绑定
             */
            String jsonData = (String) execution.getVariable(Constants.GLOBAL_FORM_DATA);
            jsonMap = JsonUtil.jsonToBean(jsonData, HashMap.class);
            baseFormDataAnalysis();
            String id = UUID.randomUUID10();

            ProjectBudgetChangeEO eo = new ProjectBudgetChangeEO();
            eo.setId(id);
            eo.setProjectId(projectData[PROJECT_ID]);
            eo.setProjectName(projectData[PROJECT_NAME]);


            /*
             * 组装各项成本
             */
            initResultEO(eo);

            ProjectBudgetChangeEOPage page = new ProjectBudgetChangeEOPage();
            page.setProjectId(projectData[PROJECT_ID]);
            List<ProjectBudgetChangeEO> list = dao.queryByList(page);

            if (CollectionUtils.isNotEmpty(list)) {
                /*
                 *  进行更新操作
                 * */
                eo.setId(list.get(0).getId());
                dao.updateByPrimaryKeySelective(eo);

            } else {
                /*
                 * 进行存储
                 */
                dao.insert(eo);
            }

        } catch (Exception e) {
            log.error("BudgetChangeAnalysisService Exception  ", e);
            throw new AdcDaBaseException("BudgetChangeAnalysisService Exception ", e.getMessage());
        }

    }

    /**
     * 组装EO数据，分为 经营类与非经营类
     */
    private void initResultEO(ProjectBudgetChangeEO resultEO) {
        String[] costStr = new String[4];
        BigDecimal[] cost = new BigDecimal[5];
        /*
         *  人工
         */
        int labor = 0;
        costStr[labor] = "人工成本";
        /*
         * 采购
         */
        int purchase = 1;
        costStr[purchase] = "采购成本";
        /*
         * 跨部门
         */
        int crossSectoral = 2;
        costStr[crossSectoral] = "部门成本";

        /*
         * 其他费用
         */
        int other = 3;
        costStr[other] = "其他费用";
        /*
         * 总成本
         */
        int amount = 4;
        cost[amount] = BigDecimal.valueOf(0.00);

        int len = 4;
        for (int i = 0; i < len; i++) {
            if (null != formMap.get(costStr[i])) {
                if (StringUtils.isNotEmpty(jsonMap.get(formMap.get(costStr[i])))) {
                    cost[i] = new BigDecimal(jsonMap.get(formMap.get(costStr[i])));
                } else {
                    cost[i] = BigDecimal.valueOf(0);
                }
                cost[amount] = cost[amount].add(cost[i]);
            }
        }

        resultEO.setPersonCost(cost[labor]);
        resultEO.setOtherCost(cost[other]);
        resultEO.setCooperationCost(cost[crossSectoral]);
        resultEO.setPurchaseCost(cost[purchase]);
        resultEO.setAmountCount(cost[amount]);
    }

    /**
     * 项目信息
     */
    private String[] projectData;

    /**
     *
     */
    private void baseFormDataAnalysis() {
        projectData = new String[2];
        for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (!key.contains("_milepost")
                && !key.contains("_leader")
                && key.contains("_proid")) {
                projectData[PROJECT_ID] = value;
            } else if (key.contains("_proname")) {
                projectData[PROJECT_NAME] = value;
            }

        }
    }
}
