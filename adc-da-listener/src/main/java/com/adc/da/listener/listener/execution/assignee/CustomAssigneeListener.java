package com.adc.da.listener.listener.execution.assignee;

import com.adc.da.budget.utils.JsonUtil;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.el.FixedValue;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.adc.da.workflow.utils.contants.Constants.GLOBAL_FORM_DATA;

/**
 * 针对 项目管理--项目实施阶段-项目实施计划管理（软件类）03-2
 * 可以用于其他流程，需要指定时间戳以及变量名
 *
 * ${customAssigneeListener}
 * 设置软开负责人，针对
 * 上传交付物审批
 *
 * @author Lee Kwanho 李坤澔
 */
@Component("customAssigneeListener")
@Slf4j
public class CustomAssigneeListener implements ExecutionListener {

    /**
     * 用于 全局变量中的 key
     */
    private FixedValue fieldName;

    /**
     * 用于设置 assignee变量名称
     */
    private FixedValue assigneeName;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String jsonData = (String) delegateExecution.getVariable(GLOBAL_FORM_DATA);
        Map<String, String> map = JsonUtil.jsonToBean(jsonData, HashMap.class);

        if (map != null) {

            String[] ctrlNameArray = fieldName.getExpressionText().split(",");
            String[] assigneeNameArray = assigneeName.getExpressionText().split(",");
            int len = ctrlNameArray.length;
            for (int i = 0; i < len; i++) {
                String ctrlName = ctrlNameArray[i];
                String assigneeId = map.get(ctrlName);
                if (StringUtils.isNotEmpty(assigneeId)) {
                    delegateExecution.setVariable(assigneeNameArray[i], assigneeId);
                } else {
                    throw new AdcDaBaseException(
                        "CustomAssigneeListener globalFormData formKey " + fieldName.getExpressionText() + "is null");
                }
            }
        } else {
            throw new AdcDaBaseException("CustomAssigneeListener globalFormData   is null");
        }
    }

}

