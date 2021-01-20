package com.adc.da.listener.listener.task;

import com.adc.da.listener.dao.ListenerOrgDao;
import com.adc.da.listener.utils.ListenerUtils;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.workflow.expansion.cmd.AdcIdentityLinkCmd;
import com.adc.da.workflow.expansion.entity.AdcIdentityLinkEntity;
import com.adc.da.workflow.utils.contants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ManagementService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.adc.da.listener.utils.ListenerUtils.ACT_DEPT_ID;

/**
 * 更改受理组信息，需要 ACT_DEPT_ID
 * ${deptListener}
 *
 * @author Lee Kwanho 李坤澔
 */
@Component("deptListener")
@Slf4j
public class DeptListener implements TaskListener, Serializable {

    private static final long serialVersionUID = 2190559253653576032L;

    @Autowired
    private transient ManagementService managementService;

    @Autowired
    private transient ListenerOrgDao listenerOrgDao;

    /**
     * 用于修改受理组的 deptId
     *
     * @param delegateTask
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        /*
         * 获取新的 departmentId
         */
        String departmentId = (String) delegateTask.getVariable(ACT_DEPT_ID);
        //获取预设的受理组信息
        Set<IdentityLink> candidates = delegateTask.getCandidates();
        if (candidates != null && !candidates.isEmpty()) {
            AdcIdentityLinkEntity candidate = (AdcIdentityLinkEntity) candidates.iterator().next();

            String roleId = candidate.getGroupId();

            AdcIdentityLinkCmd adcIdentityLinkCmd = new AdcIdentityLinkCmd(
                (TaskEntity) delegateTask,
                roleId);

            // 删除受理组设置

            List<String> deptIds = listenerOrgDao.getOrgAndParent(departmentId);

            boolean isUpdate = false;
            for (String deptId : deptIds) {
                List<String> userId = listenerOrgDao.getUserWithDeptAndRole(deptId, roleId);
                if (CollectionUtils.isNotEmpty(userId)) {
                    delegateTask.deleteGroupIdentityLink(candidate.getGroupId(), IdentityLinkType.CANDIDATE);
                    adcIdentityLinkCmd.setDepartId(deptId);
                    // 新增受理组，只有需要新增包含 DepartId 的记录时需要这样操作
                    managementService.executeCommand(adcIdentityLinkCmd);
                    isUpdate = true;
                    break;
                }
            }
            if (!isUpdate) {
                String proDefKey = delegateTask.getProcessDefinitionId().split(":")[0];
                /*
                 * 针对3-1流程做额外处理
                 */
                if ("pdc2fb60b480c413982c876bedcda0249".equals(proDefKey)) {

                    Map<String, Object> jsonMap = ListenerUtils
                        .initGlobalFormDataMap((String) delegateTask.getVariable(Constants.GLOBAL_FORM_DATA));
                    delegateTask.deleteGroupIdentityLink(candidate.getGroupId(), IdentityLinkType.CANDIDATE);
                    delegateTask.setAssignee((String) jsonMap.get("inputuserselect_1564473799414_usid"));

                }

            }

        }

        /*
         *  如果需要新增单独的受理组用户
         *   Set<String> candidateUsers = new HashSet<>()
         *   candidateUsers.add("用户id")
         *   delegateTask.addCandidateUsers(candidateUsers)
         *
         */

    }

}

