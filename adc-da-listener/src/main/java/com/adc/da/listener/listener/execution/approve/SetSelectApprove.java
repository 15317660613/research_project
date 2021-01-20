package com.adc.da.listener.listener.execution.approve;

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

import static com.adc.da.listener.utils.ListenerUtils.HIDE_A;
import static com.adc.da.workflow.utils.contants.TaskCompleteType.APPROVE;

/**
 * describe:
 * 用于 科研4-1  决定审批级别，
 * 普通 经理审批结束
 * 重要 部长审批结束
 * 非常重要 本部长审批结束
 * select_1561964267034
 *
 * ${setSelectApprove}
 *
 * @author 李坤澔
 *     date 2019-07-08
 */

@Component
@Slf4j
public class SetSelectApprove implements ExecutionListener {

    /** 控件名称 */
    private FixedValue fieldName;

    /**
     * 选择框级别 普通 重要 非常重要
     * 配置监听的时候请配上这个属性
     */
    private FixedValue level;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        /*针对同意的情况进行二次判断*/
        if ("agree".equals(delegateExecution.getVariable("approve"))) {
            /* 选择框级别 普通 重要 非常重要*/
            String levelStr;
            /* 控件名称 */
            String formItemKey;

            if (null != fieldName && StringUtils.isNotEmpty(fieldName.getExpressionText())) {
                formItemKey = fieldName.getExpressionText();
            } else {
                formItemKey = "select_1561964267034";
            }
            if (null != level && StringUtils.isNotEmpty(level.getExpressionText())) {
                levelStr = level.getExpressionText();
            } else {
                levelStr = "普通";
            }
            String jsonData = (String) delegateExecution.getVariable("globalFormData");
            Map<String, String> map = JsonUtil.jsonToBean(jsonData, HashMap.class);
            if (map != null) {

                String value = map.get(formItemKey);
                /*不符合关建指标进行本部长审批*/
                if (levelStr.equals(value)) {
                    delegateExecution.setVariable(APPROVE, HIDE_A);
                }

            } else {
                throw new AdcDaBaseException("SetActDeptIDListener globalFormData   is null");
            }
        }
    }

}