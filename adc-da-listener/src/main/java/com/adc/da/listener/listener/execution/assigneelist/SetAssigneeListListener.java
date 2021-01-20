package com.adc.da.listener.listener.execution.assigneelist;

import com.adc.da.listener.dao.ListenerOrgDao;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.adc.da.listener.utils.ListenerUtils.ASSIGNEE_LIST;

/**
 * 针对流程管理系列流程，  中心领导会签执行下列操作
 * ${setAssigneeListListener}
 *
 * @author Lee Kwanho 李坤澔
 */
@Component("setAssigneeListListener")
@Slf4j
public class SetAssigneeListListener implements ExecutionListener {

    @Autowired
    private transient ListenerOrgDao listenerOrgDao;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        //主任id
        List<String> idList = listenerOrgDao.getUserByRole("6UPXXJBFX5");

        if (CollectionUtils.isNotEmpty(idList)) {
            delegateExecution.setVariable(ASSIGNEE_LIST, idList);
        } else {
            throw new AdcDaBaseException("idList is null role id = 6UPXXJBFX5 ");
        }
    }

}

