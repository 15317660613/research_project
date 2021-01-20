package com.adc.da.listener.listener.execution.department;

import com.adc.da.budget.utils.JsonUtil;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.adc.da.listener.utils.ListenerUtils.ACT_DEPT_ID;
import static com.adc.da.workflow.utils.contants.Constants.GLOBAL_FORM_DATA;

/**
 * 针对流程管理系列流程 根据组织选择控件获取 dept_ID
 * ${setActDeptIDListener}
 *
 * @author Lee Kwanho 李坤澔
 */
@Component("setActDeptIDListener")
@Slf4j
public class SetActDeptIDListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String jsonData = (String) delegateExecution.getVariable(GLOBAL_FORM_DATA);
        Map<String, String> map = JsonUtil.jsonToBean(jsonData, HashMap.class);
        if (map != null) {

            /*
             *  根据表单获取 部门id , 针对 科研项目验收申请表 做额外处理
             */
            String deptId = "";
            if (delegateExecution.getProcessDefinitionId().contains("p96700fc6b9844ffd95e149291d486a95")) {
                deptId = map.get("inputorgselect_1571366464091_id");
            } else {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    if (key.contains("inputorgselect_") && key.contains("_id")) {
                        deptId = entry.getValue();
                        break;
                    }
                }
            }

            if (StringUtils.isNotEmpty(deptId)) {
                delegateExecution.setVariable(ACT_DEPT_ID, deptId);
            } else {
                throw new AdcDaBaseException("SetActDeptIDListener globalFormData inputorgselect_xxxx_id is null");
            }
        } else {
            throw new AdcDaBaseException("SetActDeptIDListener globalFormData   is null");
        }
    }

}

