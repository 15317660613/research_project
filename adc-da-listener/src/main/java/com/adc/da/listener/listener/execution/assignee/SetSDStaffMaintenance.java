package com.adc.da.listener.listener.execution.assignee;

import com.adc.da.listener.utils.ListenerUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.workflow.utils.contants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * SDStaff 针对 项目管理---项目运维阶段-05
 * ${setSDStaffMaintenance}
 * 设置软开负责人，针对
 *
 * @author Lee Kwanho 李坤澔
 */
@Component
@Slf4j
public class SetSDStaffMaintenance implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        Map<String, Object> map = ListenerUtils
            .initGlobalFormDataMap((String) delegateExecution.getVariable(Constants.GLOBAL_FORM_DATA));

        if (map != null) {

            String formItemKey = "";
            for (String key : map.keySet()) {
                if (key.contains("inputuserselect_") && key.contains("_usid")) {
                    formItemKey = key;
                    break;
                }
            }

            String actAssigneeId = (String) map.get(formItemKey);
            if (StringUtils.isNotEmpty(actAssigneeId)) {
                delegateExecution.setVariable("SDStaff", actAssigneeId);
            } else {
                throw new AdcDaBaseException("SetActDeptIDListener globalFormData " + formItemKey + " is null");
            }
        } else {
            throw new AdcDaBaseException("SetActDeptIDListener globalFormData   is null");
        }
    }

}

