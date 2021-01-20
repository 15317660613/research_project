package com.adc.da.listener.listener.task;

import com.adc.da.listener.dao.ListenerOrgDao;
import com.adc.da.listener.utils.ListenerUtils;
import com.adc.da.research.entity.MemberEO;
import com.adc.da.research.service.MemberEOService;
import com.adc.da.util.exception.AdcDaBaseException;
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
import static com.adc.da.listener.utils.ListenerUtils.ACT_PROJECT_ID;
import static com.adc.da.listener.utils.ListenerUtils.ACT_SPECIAL_PROCESS_FLAG;
import static com.adc.da.listener.utils.RoleIdUtils.ROLE_ID_DEPT_DIRECTOR;
import static com.adc.da.listener.utils.RoleIdUtils.ROLE_ID_DEPT_LEADER;

/**
 * 更改受理组信息，需要 ACT_DEPT_ID
 * ${deptRSPListener}
 * 仅用于 科研类流程
 * 结项
 * 中止
 * 启动会
 * 科研项目验收申请表
 * 科研项目跟踪情况调查
 *
 * @author Lee Kwanho 李坤澔
 */
@Component("deptRSPListener")
@Slf4j
public class DeptRSPListener implements TaskListener, Serializable {

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
            AdcIdentityLinkCmd adcIdentityLinkCmd = new AdcIdentityLinkCmd((TaskEntity) delegateTask, roleId);
            // 删除受理组设置
            List<String> deptIds = listenerOrgDao.getOrgAndParent(departmentId);
            boolean isNotUpdate = true;

            /*
             * 3 为本部
             * 中汽研-》数据资源中心-》本部 / 市场部/财务部/管理部
             */
            if (3 != deptIds.size() || !ROLE_ID_DEPT_LEADER.equals(roleId)) {
                for (String deptId : deptIds) {
                    List<String> userId = listenerOrgDao.getUserWithDeptAndRole(deptId, roleId);
                    if (CollectionUtils.isNotEmpty(userId)) {
                        delegateTask.deleteGroupIdentityLink(candidate.getGroupId(), IdentityLinkType.CANDIDATE);
                        adcIdentityLinkCmd.setDepartId(deptId);
                        // 新增受理组，只有需要新增包含 DepartId 的记录时需要这样操作
                        managementService.executeCommand(adcIdentityLinkCmd);
                        isNotUpdate = false;
                        break;
                    }
                }
            }
            if (isNotUpdate) {
                checkSpecialCase(delegateTask, deptIds, candidate, adcIdentityLinkCmd);
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

    @Autowired
    private transient MemberEOService memberEOService;

    /**
     * 对本部及以上的项目做额外处理
     *
     * @param delegateTask
     * @param deptIds
     * @param candidate
     * @param adcIdentityLinkCmd
     */
    private void checkSpecialCase(DelegateTask delegateTask, List<String> deptIds, AdcIdentityLinkEntity candidate,
        AdcIdentityLinkCmd adcIdentityLinkCmd) {
        /*
         * 科研项目 结项 中止 启动会 科研项目验收申请表 科研项目跟踪情况调查
         */
        int length = deptIds.size();
        if (2 == length) {
            /*
             * 主任科研项目
             */
            delegateTask.deleteGroupIdentityLink(candidate.getGroupId(), IdentityLinkType.CANDIDATE);

            String projectId = (String) delegateTask.getVariable(ACT_PROJECT_ID);
            if (null == projectId) {

                Map<String, Object> jsonMap = ListenerUtils
                    .initGlobalFormDataMap((String) delegateTask.getVariable(Constants.GLOBAL_FORM_DATA));
                projectId = null;
                for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
                    String key = entry.getKey();
                    if (key.contains("selectResearch_") && key.contains("_proid")) {
                        projectId = (String) entry.getValue();
                        break;
                    }
                }
                /* 针对 科研项目考核单独处理 */
                if (null == projectId
                    && delegateTask.getProcessDefinitionId()
                                   .contains("pe1a156885e60478a95c29dcf768ef722")) {
                    projectId = (String) jsonMap.get("selectBusiness_1562120976490_proid");
                }
            }
            List<MemberEO> memberList = memberEOService.getMemberInfoWithSort(projectId);

            if (CollectionUtils.isNotEmpty(memberList)) {
                delegateTask.setAssignee(memberList.get(0).getMemberNameId());
            } else {
                throw new AdcDaBaseException("memberList is null");
            }

            delegateTask.setName("项目负责人审批");

        } else if (3 == length) {
            /*
             * 总监 科研项目
             * "Z8GCB8URTR"
             */

            delegateTask.deleteGroupIdentityLink(candidate.getGroupId(), IdentityLinkType.CANDIDATE);
            adcIdentityLinkCmd.setDepartId(deptIds.get(0));
            adcIdentityLinkCmd.setGroupId(ROLE_ID_DEPT_DIRECTOR);

            // 新增受理组，只有需要新增包含 DepartId 的记录时需要这样操作
            managementService.executeCommand(adcIdentityLinkCmd);
            delegateTask.setName("总监审批");

        }
        delegateTask.setVariable(ACT_SPECIAL_PROCESS_FLAG, true);
    }
}
