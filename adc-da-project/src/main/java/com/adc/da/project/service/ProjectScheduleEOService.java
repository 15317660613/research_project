package com.adc.da.project.service;

import com.adc.da.project.dao.OrgProDao;
import com.adc.da.project.entity.ProjectPlanEO;
import com.adc.da.project.entity.StatisticsEntity;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.project.dao.ProjectScheduleEODao;
import com.adc.da.project.entity.ProjectScheduleEO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>PROJECT_SCHEDULE ProjectScheduleEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-04-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("projectScheduleEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProjectScheduleEOService extends BaseService<ProjectScheduleEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectScheduleEOService.class);

    @Autowired
    private ProjectScheduleEODao dao;

    @Autowired
    private UserEODao userEODao ;

    @Autowired
    private OrgProDao orgProDao ;

    public ProjectScheduleEODao getDao() {
        return dao;
    }

//   public List<ProjectScheduleEO> selectByDeptAndProType(String department , String projectType ){
//
//        return dao.selectByDeptAndProType(department ,  projectType );
//    }

    public List<ProjectScheduleEO> selectByDeptProTypeAndProcessInstId(String userId , String classType,String projectType ,String processInstId){
        UserEO userEO = null ;
        List<ProjectScheduleEO> projectScheduleEOArrayList = new ArrayList<>() ;

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
            projectScheduleEOArrayList.addAll(dao.selectByDeptProTypeAndProcessInstId(orgEOId, classType, projectType, processInstId));
        }

        if(CollectionUtils.isNotEmpty(projectScheduleEOArrayList)){
            StringBuilder remark = new StringBuilder();
            remark.append(projectScheduleEOArrayList.get(0).getRemark());
            for (ProjectScheduleEO projectScheduleEO:projectScheduleEOArrayList) {
                if(StringUtils.isNotEmpty(projectScheduleEO.getRemark())){
                    int pos = projectScheduleEO.getRemark().lastIndexOf("^^");
                    String remarkTep = projectScheduleEO.getRemark().substring(pos+2);
                    String endRemark = remark.toString().substring(pos+2);
                    String prefix = "";
                    if(StringUtils.isNotEmpty(endRemark)){
                        prefix = "||";
                    }
                    if(!remark.toString().contains(remarkTep)){
                        remark.append(prefix+remarkTep);
                    }
                }
            }
            projectScheduleEOArrayList.get(0).setRemark(remark.toString());
        }
        return projectScheduleEOArrayList;
    }


    public List<ProjectScheduleEO> selectProTypeAndProcessInstId(String projectType ,String classType,String processInstId){

        return  dao.selectByProTypeAndProcessInstId( projectType , classType ,processInstId );

    }



}
