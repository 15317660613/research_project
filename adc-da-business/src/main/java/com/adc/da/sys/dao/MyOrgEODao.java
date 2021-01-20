package com.adc.da.sys.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 组织机构Dao层, 具体sql在xml中
 *
 * @author comments created by Lee Kwanho
 * @see mybatis.mapper.sys
 */
public interface MyOrgEODao extends BaseDao<OrgEO> {
    /**
     * 根据机构名称查询机构ID
     */
     String getOrgIdByOrgName(@Param("orgName") String orgName);

    /**
     * 根据机构名称带本部list
     */
    List<OrgEO> queryBigDeptList();


    List<Map> querySubDeptPersonCount();

    List<String> queryOrgPidById(@Param("ids") List<String> ids);

    List<String> queryOrgChildIdsByPids(@Param("ids") List<String> ids);

    List<OrgEO> listAll();


    Integer queryDeptPersonCount(@Param("deptId") String deptId);


    OrgEO selectByPrimaryKey(@Param("deptId") String deptId) ;

}
