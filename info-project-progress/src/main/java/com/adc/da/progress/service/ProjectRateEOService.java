package com.adc.da.progress.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.constant.ProjectType;
import com.adc.da.progress.dao.ProjectRateEODao;
import com.adc.da.progress.entity.ProjectRateEO;
import com.adc.da.progress.page.ProjectRateEOPage;
import com.adc.da.util.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <br>
 * <b>功能：</b>PR_PROJECT_RATE ProjectRateEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("projectRateEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
@Slf4j
public class ProjectRateEOService extends BaseService<ProjectRateEO, String> {

    @Autowired
    private ProjectRateEODao dao;

    public ProjectRateEODao getDao() {
        return dao;
    }

    public void deleteByPrimaryKeysInFlag(List<String> ids) {
        dao.deleteByPrimaryKeysInFlag(ids);
    }

    @Override
    public int insert(ProjectRateEO projectRateEO) throws Exception {
        projectRateEO.setId(UUID.randomUUID10());
        return dao.insert(projectRateEO);
    }

    /**
     * 创建初始记录
     *
     * @param projectId   项目id
     * @param projectType 科研类 2  经营类 0
     * @throws Exception
     * @see ProjectType
     */
    public ProjectRateEO initProjectProgressData(String projectId, String projectType) throws Exception {
        ProjectRateEOPage queryPage = new ProjectRateEOPage();
        queryPage.setProjectId(projectId);
        queryPage.setProcInstId("-1");
        queryPage.setProcessNameId(projectType);
        /*
         *  进行重复校验
         */
        ProjectRateEO projectRateEO = new ProjectRateEO();
        if (dao.queryByCount(queryPage) == 0) {

            projectRateEO.setProjectId(projectId);
            projectRateEO.setProcInstId("-1");
            projectRateEO.setProcessNameId(projectType);
            Date nowDate = new Date();
            projectRateEO.setCreateTime(nowDate);
            projectRateEO.setModifiedTime(nowDate);
            //projectRateEO.setId(UUID.randomUUID10());
            this.insert(projectRateEO);
        } else {
            log.error("Duplicate project Type which id is {}", projectId);
        }
        return  projectRateEO;
    }

}
