package com.adc.da.listener.listener.execution.assigneelist;

import com.adc.da.listener.dao.ListenerOrgDao;
import com.adc.da.listener.utils.ListenerUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.el.FixedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.adc.da.listener.utils.RSProjectProcessType.RS_PROJECT_CHANGE;
import static com.adc.da.listener.utils.RSProjectProcessType.RS_PROJECT_END;

/**
 * 针对流程管理系列流程，
 * 科研变更流程 pa0001fb0970341808dd0869ab05c8a3c
 * 用于知会
 * 科研项目办公室成员-除朱向雷 （数据中心总工角色）
 * ${setAssigneeListResearch}
 *
 * @author Lee Kwanho 李坤澔
 */
@Component("setAssigneeListResearch")
@Slf4j
public class SetAssigneeListResearch implements ExecutionListener {

    /**
     * 角色id 包含的 可以从流程图中注入
     *
     */
    private FixedValue roleIdF;

    /**
     * 标注项目类型
     */
    private FixedValue typeF;

    /**
     * 角色id 排除的角色id 可以从流程图中注入
     */
    private FixedValue roleIdNotF;

    /**
     * assigneeList assigneeList1 等，用于流程中带有多个多实例节点的情况
     */
    private FixedValue assigneeListName;

    @Autowired
    private transient ListenerOrgDao listenerOrgDao;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        /* 默认值 科委会成员  */
        String roleId = ListenerUtils
            .initFixedValueToString("YLJU3T4S6U", roleIdF);


        /* 默认值 科研项目管理办公室成员  */
        String assigneeListNameLoc = ListenerUtils
            .initFixedValueToString(ListenerUtils.ASSIGNEE_LIST, assigneeListName);

        /*
         *  默认 为变更流程
         *  注：这个参数应在流程图中强制配置，若流程图不配置，会引起冲突问题
         */
        String type = ListenerUtils
            .initFixedValueToString(RS_PROJECT_CHANGE, typeF);

        //科研项目管理办公室成员id
        List<String> idList = listenerOrgDao.getUserByRole(roleId);

        /*
         * 处理
         */
        switch (type) {
            case RS_PROJECT_CHANGE:
                doBudgetChange(idList, delegateExecution, assigneeListNameLoc, roleId);
                break;
            case RS_PROJECT_END:
                budgetEnd(idList, delegateExecution, assigneeListNameLoc, roleId);
                break;
            default:
                throw new AdcDaBaseException("case not found");
        }

    }

    /**
     * 项目变更
     *
     * @param idList
     * @param delegateExecution
     * @param assigneeListNameLoc
     * @param roleId
     */
    private void doBudgetChange(List<String> idList, DelegateExecution delegateExecution, String assigneeListNameLoc,
        String roleId) {
        /* 数据中心总工 */
        String roleIdNot = ListenerUtils
            .initFixedValueToString("NV8AJSNSCB", roleIdNotF);
        List<String> idNotList = listenerOrgDao.getUserByRole(roleIdNot);
        if (CollectionUtils.isNotEmpty(idList) && CollectionUtils.isNotEmpty(idNotList)) {
            idList.removeAll(idNotList);
            delegateExecution.setVariable(assigneeListNameLoc, idList);
        } else {
            log.warn("idList is null role id = {} or id = {}", roleId, roleIdNot);
            throw new AdcDaBaseException("查询不到角色对应的用户");
        }
    }

    /**
     * 项目结项
     *
     * @param idList
     * @param delegateExecution
     * @param assigneeListNameLoc
     * @param roleId
     */
    private void budgetEnd(List<String> idList, DelegateExecution delegateExecution, String assigneeListNameLoc,
        String roleId) {

        if (CollectionUtils.isNotEmpty(idList)) {
            delegateExecution.setVariable(assigneeListNameLoc, idList);
        } else {
            log.warn("idList is null role id = {} ", roleId);
            throw new AdcDaBaseException("查询不到角色对应的用户");
        }
    }

}

