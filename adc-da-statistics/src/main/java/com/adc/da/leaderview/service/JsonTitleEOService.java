package com.adc.da.leaderview.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.constant.Role;
import com.adc.da.leaderview.dao.JsonTitleEODao;
import com.adc.da.leaderview.entity.JsonTitleEO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.dao.ProjectMenuEODao;
import com.adc.da.research.entity.MenuEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <br>
 * <b>功能：</b>ST_JSON_TITLE JsonTitleEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-09-24 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("jsonTitleEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class JsonTitleEOService extends BaseService<JsonTitleEO, String> {

    @Autowired
    private JsonTitleEODao dao;

    public JsonTitleEODao getDao() {
        return dao;
    }

    @Autowired
    private ProjectMenuEODao projectMenuEODao;

    /**
     * 查询菜单，包含层级关系
     *
     * @return
     * @throws Exception
     */
    public List<MenuEO> queryByListWithLevelLeaderView() {

        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登录过期");
        }

        Subject subject = SecurityUtils.getSubject();
        /*
         * 定义三位从左往右
         *  1 中心
         *  2 科研
         *  3 部长
         */
        final String deptLeader = "1";
        final String committeeMember = "2";
        final String directOr = "3";

        Set<String> querySet = new HashSet<>(8);
        /*
         * 普通员工
         */
        querySet.add("0");

        boolean isCommitteeMember = subject.hasRole(Role.COMMITTEE_MEMBER);
        boolean isDepLeader = subject.hasRole(Role.BU_ZHANG) || subject.hasRole(Role.BEN_BU_ZHANG);

        if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ZHU_REN)||subject.hasRole(Role.PROJECT_ADMIN)) {
            /*
             * 主任，管理员、项目管理员
             */
            querySet.add(deptLeader);
            querySet.add(committeeMember);
            querySet.add(directOr);
        } else if (isCommitteeMember || isDepLeader) {
            if (isDepLeader) {
                querySet.add(deptLeader);
            }
            if (isCommitteeMember) {
                querySet.add(committeeMember);
            }
        }

        return projectMenuEODao.queryByListWithLevelSpecial("VID000", querySet);

    }
}
