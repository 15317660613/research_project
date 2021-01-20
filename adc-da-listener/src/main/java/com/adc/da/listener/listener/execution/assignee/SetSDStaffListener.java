package com.adc.da.listener.listener.execution.assignee;

import com.adc.da.budget.utils.JsonUtil;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.workflow.utils.contants.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adc.da.workflow.utils.contants.Constants.GLOBAL_FORM_DATA;

/**
 * SDStaff 针对 项目管理--项目实施阶段-项目实施计划管理（软件类）03-2
 * ${setSDStaffListener}
 * 设置软开负责人，针对
 *
 * @author Lee Kwanho 李坤澔
 */
@Component
@Slf4j
public class SetSDStaffListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String jsonData = (String) delegateExecution.getVariable(GLOBAL_FORM_DATA);
        Map<String, String> map = JsonUtil.jsonToBean(jsonData, HashMap.class);
        if (map != null) {

            List<String> idList = new ArrayList<>(3);
            for (String key : map.keySet()) {
                if (key.contains("inputuserselect_") && key.contains("_usid")) {
                    idList.add(key);
                }
            }
            String deptId = map.get(idList.get(2));
            if (StringUtils.isNotEmpty(deptId)) {
                delegateExecution.setVariable("SDStaff", deptId);
            } else {
                log.warn("SetActDeptIDListener globalFormData inputorgselect_1561424149419_id is null");
                throw new AdcDaBaseException(ErrorType.TASK_GET_FORM_ERROR);
            }
        } else {
            throw new AdcDaBaseException(ErrorType.TASK_GET_FORM_ERROR);
        }
    }

}

