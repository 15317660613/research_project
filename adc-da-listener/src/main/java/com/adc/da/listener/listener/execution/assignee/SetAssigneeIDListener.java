package com.adc.da.listener.listener.execution.assignee;

import com.adc.da.budget.utils.JsonUtil;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.adc.da.listener.utils.ListenerUtils.ACT_ASSIGNEE_ID;
import static com.adc.da.workflow.utils.contants.Constants.GLOBAL_FORM_DATA;

/**
 * 针对流程管理系列流程
 * ${setAssigneeIDListener}
 *
 * @author Lee Kwanho 李坤澔
 */
@Component("setAssigneeIDListener")
@Slf4j
public class SetAssigneeIDListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String jsonData = (String) delegateExecution.getVariable(GLOBAL_FORM_DATA);
        Map<String, String> map = JsonUtil.jsonToBean(jsonData, HashMap.class);
        if (map != null) {

            String formItemKey = "";
            for (String key : map.keySet()) {
                if (key.contains("inputuserselect_") && key.contains("_usid")) {
                    formItemKey = key;
                    break;
                }
            }

            String actAssigneeId = map.get(formItemKey);
            if (StringUtils.isNotEmpty(actAssigneeId)) {
                delegateExecution.setVariable(ACT_ASSIGNEE_ID, actAssigneeId);
            } else {
                throw new AdcDaBaseException("SetActDeptIDListener globalFormData " + formItemKey + " is null");
            }
        } else {
            throw new AdcDaBaseException("SetActDeptIDListener globalFormData   is null");
        }
    }

}

