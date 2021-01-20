package com.adc.da.listener.listener.execution.department;

import com.adc.da.listener.dao.ListenerOrgDao;
import com.adc.da.listener.entity.OrgEOLevel;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.el.FixedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.adc.da.listener.utils.ListenerUtils.ACT_DEPT_ID;

/**
 * 针对流程管理系列流程 设置全局变量 ACT_DEPT_ID
 * ${setDeptIDByInitiatorListener}
 *
 * @author Lee Kwanho 李坤澔
 */
@Component("setDeptIDByInitiatorListener")
@Slf4j
public class SetDeptIDByInitiatorListener implements ExecutionListener {

    @Autowired
    private transient ListenerOrgDao listenerOrgDao;

    /**
     * 流程图监听器 的类属性 可以用于替换 该方法中的orgId
     */
    FixedValue fieldName;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String rootOrgId;

        if (null == fieldName || StringUtils.isEmpty(fieldName.getExpressionText())) {
            /* 数据中心 */
            rootOrgId = "MH8JQV5TSN";
        } else {
            rootOrgId = fieldName.getExpressionText();
        }

        String initiator = (String) delegateExecution.getVariable("initiator");
        List<OrgEOLevel> orgList = listenerOrgDao.getUserOrgIdWithLevel(initiator, rootOrgId);
        if (CollectionUtils.isNotEmpty(orgList)) {
            String deptId = orgList.get(0).getId();
            if (StringUtils.isNotEmpty(deptId)) {
                delegateExecution.setVariable(ACT_DEPT_ID, deptId);
            } else {
                throw new AdcDaBaseException("SetActDeptIDByInitiatorListener deptId  is null");
            }
        } else {
            throw new AdcDaBaseException("SetActDeptIDByInitiatorListener  orgList   is null");
        }
    }
}

