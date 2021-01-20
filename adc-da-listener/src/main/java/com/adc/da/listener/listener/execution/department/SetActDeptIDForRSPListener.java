package com.adc.da.listener.listener.execution.department;

import com.adc.da.listener.utils.RSProjectBusinessKey;
import com.adc.da.research.dao.InfoEODao;
import com.adc.da.research.entity.InfoEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.adc.da.listener.utils.FormType.PROJECT_ID;
import static com.adc.da.listener.utils.ListenerUtils.ACT_DEPT_ID;
import static com.adc.da.listener.utils.ListenerUtils.ACT_PROJECT_ID;

/**
 * 针对流程管理系列流程
 * 科研项目设置所属部门
 *  仅用于 变更 - 结项 两个流程
 * ${setActDeptIDForRSPListener}
 *
 * @author Lee Kwanho 李坤澔
 */
@Component("setActDeptIDForRSPListener")
@Slf4j
public class SetActDeptIDForRSPListener implements ExecutionListener {

    @Autowired
    private transient InfoEODao infoEODao;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String businessKey = delegateExecution.getProcessBusinessKey();
        String[] key = RSProjectBusinessKey.spitBusinessKey(businessKey);
        InfoEO infoEO = infoEODao.selectByPrimaryKey(key[PROJECT_ID]);
        if (null == infoEO) {
            throw new AdcDaBaseException("key " + key[PROJECT_ID] + "not found ");
        }
        String deptId = infoEO.getOwnDepartmentId();
        if (StringUtils.isNotEmpty(deptId)) {
            delegateExecution.setVariable(ACT_DEPT_ID, deptId);
            delegateExecution.setVariable(ACT_PROJECT_ID, key[PROJECT_ID]);
        } else {
            throw new AdcDaBaseException("infoEO.getOwnDepartmentId  null");
        }

    }

}

