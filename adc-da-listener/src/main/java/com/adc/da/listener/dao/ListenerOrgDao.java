package com.adc.da.listener.dao;

import com.adc.da.listener.entity.OrgEOLevel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-06-25
 */
public interface ListenerOrgDao {

    /**
     * 获取组织以及组织的上级组织id
     *
     * @param orgId
     * @return
     */
    List<String> getOrgAndParent(@Param("orgId") String orgId);

    /**
     * 获取组织以及组织的下级组织id
     *
     * @param orgId
     * @return
     */
    List<String> getOrgAndChild(@Param("orgId") String orgId);

    /**
     * 获取用户同时满足组织id和角色的，不去重
     *
     * @param orgId
     * @param roleId
     * @return
     */
    List<String> getUserWithDeptAndRole(@Param("orgId") String orgId, @Param("roleId") String roleId);

    /**
     * 获取用户的orgId
     *
     * @param userId
     * @return
     */
    List<String> getUserOrgId(@Param("userId") String userId);

    /**
     * 获取用户的orgId与组织的level，level值越低则org级别越高，如中汽研的level是1
     *
     * @param userId
     * @param rootOrgId
     * @return
     */
    List<OrgEOLevel> getUserOrgIdWithLevel(@Param("userId") String userId, @Param("rootOrgId") String rootOrgId);

    /**
     * 获取指定组织的用户id数组
     *
     * @param roleId
     * @return
     */
    List<String> getUserByRole(@Param("roleId") String roleId);

    /**
     * 获取指定用户的角色
     *
     * @param userId
     * @return
     */
    List<String> getRoleByUser(@Param("userId") String userId);

}
