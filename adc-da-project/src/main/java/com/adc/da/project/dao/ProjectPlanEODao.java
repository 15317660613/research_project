package com.adc.da.project.dao;

import com.adc.da.project.entity.ProjectPlanEO;
import com.adc.da.base.dao.BaseDao;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>PROJECT_PLAN ProjectPlanEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-04-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ProjectPlanEODao extends BaseDao<ProjectPlanEO> {


    List<ProjectPlanEO> selectByDeptProTypeAndProcessInstId(String department , String classType , String projectType  , String processInstId) ;
    List<ProjectPlanEO> selectByProTypeAndProcessInstId( String projectType  ,String classType, String processInstId) ;
    ProjectPlanEO selectProjectPlanByIdAndProcessInsId(String id ,String processInstId);
}
