package com.adc.da.progress.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.progress.dao.ProjectBudgetChangeEODao;
import com.adc.da.progress.entity.ProjectBudgetChangeEO;


/**
 *
 * <br>
 * <b>功能：</b>PR_PROJECT_BUDGET_CHANGE ProjectBudgetChangeEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("projectBudgetChangeEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProjectBudgetChangeEOService extends BaseService<ProjectBudgetChangeEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectBudgetChangeEOService.class);

    @Autowired
    private ProjectBudgetChangeEODao dao;

    public ProjectBudgetChangeEODao getDao() {
        return dao;
    }


    public ProjectBudgetChangeEO selectByProjectId(String projectId){
        return dao.selectByProjectId(projectId);
    }

}
