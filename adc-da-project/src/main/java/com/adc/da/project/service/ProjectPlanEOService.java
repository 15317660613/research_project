package com.adc.da.project.service;

import com.adc.da.project.dao.OrgProDao;
import com.adc.da.project.dao.ProjectScheduleEODao;
import com.adc.da.project.entity.ProjectScheduleEO;
import com.adc.da.project.entity.StatisticsEntity;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.project.dao.ProjectPlanEODao;
import com.adc.da.project.entity.ProjectPlanEO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>PROJECT_PLAN ProjectPlanEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-04-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("projectPlanEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProjectPlanEOService extends BaseService<ProjectPlanEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectPlanEOService.class);

    @Autowired
    private ProjectPlanEODao dao;

    @Autowired
    private UserEODao userEODao ;

    @Autowired
    private OrgProDao orgProDao ;

    public ProjectPlanEODao getDao() {
        return dao;
    }


    public List<ProjectPlanEO> selectByDeptAndProType(String userId , String classType,String projectType ,String processInstId){
        UserEO userEO = null ;
        List<ProjectPlanEO> projectPlanEOArrayList = new ArrayList<>() ;

        userEO = userEODao.getUserWithRoles(userId);

        List<String> newOrgEOIdList = new ArrayList<>();

        List<OrgEO> myOrgEOList = userEO.getOrgEOList() ;
        List<OrgEO> allOrgList = orgProDao.listAllOrg();

        if(projectType.contains("s")){
            for (OrgEO allO : allOrgList){
                newOrgEOIdList.add(allO.getId());
            }
            projectType = projectType.substring(1);
        } else {
            for (OrgEO allO : allOrgList){
                for (OrgEO orgEO : myOrgEOList){
                    newOrgEOIdList.add(orgEO.getId());
                    if (allO.getParentIds().contains(orgEO.getId())){
                        newOrgEOIdList.add(allO.getId());
                    }
                }
            }
        }

        HashSet<String> hashSet = new HashSet<>(newOrgEOIdList);

        newOrgEOIdList = new ArrayList<>(hashSet);

        for (String orgEOId: newOrgEOIdList) {
            projectPlanEOArrayList.addAll(dao.selectByDeptProTypeAndProcessInstId(orgEOId, classType, projectType, processInstId));
        }


        return projectPlanEOArrayList;
    }

    public List<ProjectPlanEO> selectProTypeAndProcessInstId(String projectType ,String classType,String processInstId){
        return  dao.selectByProTypeAndProcessInstId( projectType , classType ,processInstId );
    }


    public ProjectPlanEO selectProjectPlanByIdAndProcessInsId(String id ,String processInstId){
        return  dao.selectProjectPlanByIdAndProcessInsId( id ,processInstId );
    }


}
