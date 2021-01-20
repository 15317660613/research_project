package com.adc.da.listener.listener.execution.assignee;

import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.utils.JsonUtil;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.adc.da.workflow.utils.contants.Constants.GLOBAL_FORM_DATA;

/**
 * describe:
 * ${setProjectManager}
 *
 * @author 李坤澔
 *     date 2019-07-08
 */

@Component("setProjectManager")
@Slf4j
public class SetProjectManager implements ExecutionListener {

    @Autowired
    private transient BudgetEODao budgetEODao;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String jsonData = (String) delegateExecution.getVariable(GLOBAL_FORM_DATA);
        Map<String, String> map = JsonUtil.jsonToBean(jsonData, HashMap.class);
        if (map != null) {

            String formItemKey = "";
            for (String key : map.keySet()) {
                if (key.contains("selectBusiness_") && key.contains("_proid")) {
                    formItemKey = key;
                    break;
                }
            }
            String budgetId = map.get(formItemKey);
            String projectManager;
            if (StringUtils.isNotEmpty(budgetId)) {
                BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(budgetId);

                if (null != budgetEO) {
                    projectManager = budgetEO.getPm();
                } else {
                    throw new AdcDaBaseException("projectId  " + budgetId + " not found");
                }
            } else {
                throw new AdcDaBaseException("formItemKey " + formItemKey + " not found");
            }

            if (StringUtils.isNotEmpty(projectManager)) {
                delegateExecution.setVariable("ProjectManager", projectManager);
            } else {
                throw new AdcDaBaseException("SetActDeptIDListener globalFormData  " + formItemKey + "is null");
            }
        } else {
            throw new AdcDaBaseException("SetActDeptIDListener globalFormData   is null");
        }
    }

}
