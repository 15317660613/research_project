package com.adc.da.budget.dao;

import com.adc.da.budget.entity.OrgWithLevelEO;
import com.adc.da.budget.entity.StatisticsEntity;
import com.adc.da.sys.entity.OrgEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 查看所有部门
 *
 * @author liuzixi
 *     date 2019-03-07
 */
public interface OrgListDao {

    /**
     * 查看所有部门
     *
     * @return
     * @author liuzixi
     *     date 2019-03-07
     */
    List<OrgEO> listAllOrg();
    List<OrgEO> listAllOrgWithDeleted(); // 被删除的也查出来

    List<OrgEO> selectOrgEOByIdList(@Param("list") List<String> list);

    OrgEO queryOrgEOByOrgId(String orgId);

    //查询用户对应的机构
    List<String> queryOrgIdByUid(String userId);

    List<StatisticsEntity> queryChildOrgByOrgId(String orgId);

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

    //查询机构和子机构所属的员工id
    List<String> queryDepEmps(String queryDepEmps, String orgType);

    //查询用户对应的机构
    List<StatisticsEntity> queryOrgByUserId(String userId);

    List<OrgEO> queryByParentId(String parent);

    //查询用户对应的组
    List<StatisticsEntity> querygGroupByUserId(String userId);

    //add by liqiushi 20190326
    //根据员工查询部门机构信息
    List<StatisticsEntity> queryAllDeps();

    //查询所有
    List<StatisticsEntity> queryAllGroups();

    String queryAdcOrgId(String name);

    /**
     * 根据父id查询子级
     *
     * @param parentId
     * @return
     */
    List<OrgEO> getOrgEOByPidAsc(String parentId);

    List<String> getOrgEOByPid(String parentId);

    List<String> getAllOrgIdByPid(String parentId);

    /**
     * 组织机构和用户联查 查询用户
     *
     * @param orgId
     * @return
     * @author Lee Kwanho 李坤澔
     */
    List<StatisticsEntity> queryUserByDepIdAndUserName(@Param("userName") String userName,
        @Param("orgId") String orgId);

    /**
     * 获取 org以及subOrg的id
     *
     * @param orgId
     * @return
     */
    List<String> getOrgAndSubOrgIds(String orgId);

    /**
     * pid
     * name为模糊
     *
     * @param orgName
     * @return ID, ORG_NAME
     * @author Lee Kwanho 李坤澔
     *     date 2019-05-30
     **/
    List<StatisticsEntity> queryOrgByNameAndPid(@Param("orgName") String orgName, @Param("parentId") String parentId);

    /**
     * @param orgName 组织机构名称
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-05-30
     **/
    List<OrgEO> getOrgEOByName(String orgName);

    /**
     * 查询用户组织机构，同时返回所在组织的树深度，以及是否是叶子节点，根节点取的是parentId 为 0的记录，详细参考EO描述
     * 返回的结果集，为根据组织树降序排列(Oracle 递归函数的Level 字段)，若用户同属本部与部门，则第一条记录为部门的信息，根节点的Level为1
     *
     * @param id UserId
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-05-31
     * @see OrgWithLevelEO
     **/
    List<OrgWithLevelEO> getUserOrgWhitLeafAndLev(String id);

    List<OrgWithLevelEO> getMinUserOrgIdInfo();

    /**
     * 查询部门以及下级部门，到部级别
     * @return
     */
    List<OrgWithLevelEO> getOrgAndSubOrgIdsWithLevel(String topNodeId);
}
