package com.adc.da.listener.listener.execution.approve;

import com.adc.da.budget.utils.JsonUtil;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.workflow.utils.contants.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.adc.da.listener.utils.ListenerUtils.HIDE_A;
import static com.adc.da.workflow.utils.contants.TaskCompleteType.APPROVE;

/**
 * 针对流程管理系列流程 针对单选框控件
 * [项目管理-业务承接-项目成本预算管理02-2]-部门负责人审核
 * ${setRadioApproveListener}
 *
 * @author Lee Kwanho 李坤澔
 */
@Component
@Slf4j
public class SetRadioApproveListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        /*针对同意的情况进行二次判断*/
        if ("agree".equals(delegateExecution.getVariable("approve"))) {
            String jsonData = (String) delegateExecution.getVariable("globalFormData");
            Map<String, String> map = JsonUtil.jsonToBean(jsonData, HashMap.class);
            if (map != null) {

                String formItemKey = "";
                for (String key : map.keySet()) {
                    if (key.contains("radio_")) {
                        formItemKey = key;
                        break;
                    }
                }

                String radio = map.get(formItemKey);
                /*不符合关建指标进行本部长审批*/
                if ("否".equals(radio)) {
                    delegateExecution.setVariable(APPROVE, HIDE_A);
                }

            } else {
                throw new AdcDaBaseException(ErrorType.TASK_GET_FORM_ERROR);
            }
        }
    }

}

