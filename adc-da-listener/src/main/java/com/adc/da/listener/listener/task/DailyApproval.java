package com.adc.da.listener.listener.task;

import com.adc.da.listener.dao.ListenerOrgDao;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.FixedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

import static com.adc.da.listener.utils.ListenerUtils.ACT_DEPT_ID;

/**
 * 用于获取用户的上一级领导审批
 * date 2019/07/29 日报审批流程修改，暂无使用
 * ${Auditor}
 * ${dailyApproval}
 *
 * @author Lee Kwanho 李坤澔
 */
@Component("dailyApproval")
@Slf4j
public class DailyApproval implements TaskListener, Serializable {
    @Autowired
    private transient ListenerOrgDao listenerOrgDao;

    private static final long serialVersionUID = 2559253653576032L;

    /** 管理部索引 */
    private static final int MANAGEMENT = 0;

    /** 财务部索引 */
    private static final int FINANCE = 1;

    /** 市场部索引 */
    private static final int MARKETING = 2;

    /** 组织变量 主任 本部长 部长 高级经理  组长 员工 */
    private FixedValue roleAuditors;

    /**
     * 初始值
     * 0 主任
     * 1 总监
     * 2 部长
     * 3 高级经理
     * 4 组长
     * 5 员工
     */
    private String[] roleAuditorsId = new String[] {"6UPXXJBFX5", "Z8GCB8URTR", "JU9TENYSEY",
        "VSNCXSEBT9", "ZX6JHFWD3X", "UA45W569GQ"};

    /** 特殊领导变量 正-主任  财务部-主任  市场部-主任 */
    private FixedValue directorChief;

    /** 郑继虎 陈平 惠怡静 */
    private String[] directorChiefId = new String[] {"PPHKSRXAA9", "4YDX2TD4W9", "2DS5RTRMVN"};

    /** 直属部门变量  管理部 财务部 市场部 */
    private FixedValue directDepartment;

    /** 直属部门 管理部 财务部 市场部 */
    private String[] directDepartmentId = new String[] {"2C5YBGGLAE", "TSU528YSYZ", "JAD87PGP8V"};

    /**
     * 受理人
     */
    private String auditor;

    /**
     * 发起人
     */
    private String initiator;

    /**
     * 发起人所属的组织以及上级
     */
    private List<String> deptIds;

    /**
     * 设置受理人员
     *
     * @param delegateTask
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        /*
         * 获取 departmentId,发起人所在部门
         */
        String deptInitiator = (String) delegateTask.getVariable(ACT_DEPT_ID);
        /*
         * 获取发起人
         */
        initiator = (String) delegateTask.getVariable("initiator");


        /*
         * 初始化四个角色Id
         */
        init();

        /*
         * 获取组织以及上级
         */
        deptIds = listenerOrgDao.getOrgAndParent(deptInitiator);

        /*
         * 用户的角色id
         */
        List<String> roleIds = listenerOrgDao.getRoleByUser(initiator);

        /* 初始化变量 */
        auditor = "";

        if (roleIds.contains(roleAuditorsId[0])
            || roleIds.contains(roleAuditorsId[1])
            || (roleIds.contains(roleAuditorsId[2]) && (deptIds.contains(directDepartmentId[MANAGEMENT])))) {
            /*主任级  本部长 管理部部长 由 郑继虎审批*/
            auditor = directorChiefId[MANAGEMENT];

        } else if ((roleIds.contains(roleAuditorsId[2]) && (deptIds.contains(directDepartmentId[MARKETING])))) {
            /* 市场部部长 由 惠怡静审批 */
            auditor = directorChiefId[MARKETING];

        } else if ((roleIds.contains(roleAuditorsId[2]) && (deptIds.contains(directDepartmentId[FINANCE])))) {
            /* 财务部部长 由陈平审批*/
            auditor = directorChiefId[FINANCE];

        } else if (roleIds.contains(roleAuditorsId[2])) {
            /*部长交由本部长审批*/
            searchLeaderForStaff(1);

        } else if (roleIds.contains(roleAuditorsId[3])) {
            /*高级经理由部长审批*/
            searchLeaderForStaff(2);

        } else if (roleIds.contains(roleAuditorsId[4])) {
            /*
             * 组长角色
             * 先找寻高级经理，若不存在，则由部长审批
             * */
            searchLeaderForStaff(3);

        } else if (roleIds.contains(roleAuditorsId[5])) {
            /*
             * 员工角色
             * 先找组长
             * */
            searchLeaderForStaff(4);
        } else {
            log.warn("user {} leader not found",initiator);
            /* 不属于上述情况，抛出异常 */
            throw new AdcDaBaseException("用户 领导查询失败");
        }

        delegateTask.setAssignee(auditor);

    }

    /**
     * 查询办理用户的方法
     *
     * @param leaderIndex
     */
    private void searchLeaderForStaff(int leaderIndex) {

        while (true) {
            if (searchLeader(deptIds, initiator, leaderIndex)) {
                /*查到用户，返回结果*/
                return;
            } else {
                if (2 < leaderIndex) {
                    leaderIndex--;
                } else {
                    log.warn("role {}   for user {} leader not found", roleAuditorsId[leaderIndex], initiator);
                    throw new AdcDaBaseException("领导信息获取失败");
                }
            }
        }

    }

    /**
     * 进行查询 deptId 中进行角色搜索
     *
     * @param deptIds   发起人所属的部门以及上级部门
     * @param initiator 发起人
     * @param index     用于确定角色索引  详见 roleAuditorsId
     * @return 如果找到对应的受理人返回true 否则返回false
     */
    private boolean searchLeader(List<String> deptIds, String initiator, int index) {
        List<String> auditors;
        for (String deptId : deptIds) {
            auditors = listenerOrgDao.getUserWithDeptAndRole(deptId, roleAuditorsId[index]);
            if (CollectionUtils.isNotEmpty(auditors)) {
                if (auditors.size() > 1) {
                    return false;
                }
                auditor = auditors.get(0);
                return true;
            }
        }
        log.warn(" role {} for user {} leader not found", roleAuditorsId[index], initiator);
        return false;

    }

    /**
     * 变量初始化
     */
    private void init() {
        if (checkFixedValue(roleAuditors)) {
            /*角色初始化*/
            initGlobalVariable(roleAuditors, roleAuditorsId);
        }
        if (checkFixedValue(directorChief)) {
            /*领导初始化*/
            initGlobalVariable(directorChief, directorChiefId);
        }
        if (checkFixedValue(directDepartment)) {
            /*直属部门初始化*/
            initGlobalVariable(directDepartment, directDepartmentId);
        }
    }

    /**
     * 全局变量 初始化
     *
     * @param fixedValue
     * @param globalVariable
     */
    private void initGlobalVariable(FixedValue fixedValue, String[] globalVariable) {
        String[] str = fixedValue.getExpressionText().split(",");
        int length = str.length;
        for (int i = 0; i < length; i++) {
            if (StringUtils.isNotEmpty(str[i])) {
                globalVariable[i] = str[i];
            }
        }
    }

    /**
     * 进行fixedValue校验，若满足返回true
     *
     * @param fixedValue
     * @return
     */
    private boolean checkFixedValue(FixedValue fixedValue) {
        return (null != fixedValue && StringUtils.isNotEmpty(fixedValue.getExpressionText()));
    }
}

