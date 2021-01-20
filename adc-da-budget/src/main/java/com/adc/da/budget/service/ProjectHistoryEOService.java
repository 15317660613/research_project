package com.adc.da.budget.service;

import com.adc.da.budget.entity.Project;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.dao.ProjectHistoryEODao;
import com.adc.da.budget.entity.ProjectHistoryEO;

import java.util.Date;


/**
 *
 * <br>
 * <b>功能：</b>_PROJECT_HISTORY ProjectHistoryEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("projectHistoryEOService")
@Transactional(rollbackFor = Throwable.class)
public class ProjectHistoryEOService extends BaseService<ProjectHistoryEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectHistoryEOService.class);

    @Autowired
    private ProjectHistoryEODao dao;
    @Autowired
    private BeanMapper beanMapper;

    public ProjectHistoryEODao getDao() {
        return dao;
    }

    public int operateProjectHistory(Project projectEO,String operateType) {
        ProjectHistoryEO projectHistoryEO = beanMapper.map(projectEO,ProjectHistoryEO.class);
        projectHistoryEO.setId(UUID.randomUUID());
        projectHistoryEO.setOperateDate(new Date());
        projectHistoryEO.setOperateUser(UserUtils.getUserId());
        projectHistoryEO.setProjectName(projectEO.getName());
        String[] projectMemeber = projectEO.getProjectMemberIds();
        String projectMemeberIds = StringUtils.join(projectMemeber,",");
        projectHistoryEO.setProjectMemberIds(projectMemeberIds);
        if(StringUtils.equals("insert",operateType)) {
            projectHistoryEO.setOperateType("insert");
        }else if(StringUtils.equals("delete",operateType)) {
            projectHistoryEO.setOperateType("delete");
        }
        return dao.insertSelective(projectHistoryEO);
    }

    public int operateProjectHistory(Project projectEO,String operateType,String operateTime) {
        ProjectHistoryEO projectHistoryEO = beanMapper.map(projectEO,ProjectHistoryEO.class);
        projectHistoryEO.setId(UUID.randomUUID());
        projectHistoryEO.setProjectId(projectEO.getId());
        projectHistoryEO.setOperateTime(operateTime);
        projectHistoryEO.setOperateDate(new Date());
        projectHistoryEO.setOperateUser(UserUtils.getUserId());
        projectHistoryEO.setProjectName(projectEO.getName());
        projectHistoryEO.setProjectMemberIds(StringUtils.join(projectEO.getProjectMemberIds(),","));
        if(StringUtils.equals("update1",operateType)) {
            //保存修改之前的状态
            projectHistoryEO.setOperateType("update1");
        }else if(StringUtils.equals("update2",operateType)) {
            //保存修改之后的状态
            projectHistoryEO.setOperateType("update2");
        }
        return dao.insertSelective(projectHistoryEO);
    }

}
