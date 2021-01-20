package com.adc.da.project.dao;

import com.adc.da.project.entity.StatisticsEntity;
import com.adc.da.sys.entity.OrgEO;

import java.util.List;

/**
 * 查看所有部门
 *
 * @author liuzixi
 * date 2019-03-07
 */
public interface OrgProDao {

    /**
     * 查看所有部门
     * @return
     * @author liuzixi
     * date 2019-03-07
     */
    List<OrgEO> listAllOrg();
    //根据机构名查询机构
    List<StatisticsEntity> queryOrgByName(String orgName);
    //查询机构及子机构的员工
    List<StatisticsEntity> queryEmpByOrgTree(String orgId);

    //查询机构下所有员工
    List<String> queryEmpByOrgId(String orgId);
    //查出本部下所有部门
    List<StatisticsEntity> queryChildEmp(String orgId);
    //查询所有子机构
    List<StatisticsEntity> queryAllChildGroups(String orgId);
    //查询机构和子机构所属的员工id (组级的机构)
    List<String> queryGroupEmps(String orgId);
    List<String> queryEmpByGropId(String orgId);
    //查机构名
    String queryOrgName(String orgId);


    //查询部门下一级的大组
    List<StatisticsEntity> queryChildGroup(String orgId);
    //查询机构下的员工id
    List<String> queryEmpByOrg(String orgId);
    //查询机构和子机构所属的员工id
    List<String> queryDepEmps(String queryDepEmps, String orgType);
    //查询用户对应的机构
    List<StatisticsEntity> queryOrgByUserId(String userId);
    //查询用户对应的组
    List<StatisticsEntity> querygGroupByUserId(String userId);



    //add by liqiushi 20190326
    //根据员工查询部门机构信息
    List<StatisticsEntity> queryAllDeps();
    //查询所有
    List<StatisticsEntity> queryAllGroups();
}
