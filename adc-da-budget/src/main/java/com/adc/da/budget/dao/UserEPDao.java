package com.adc.da.budget.dao;

import com.adc.da.budget.entity.UserEPEntity;
import com.adc.da.sys.entity.UserEO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserEPDao {

    //根据数USName和删除的状态去筛选人
     List<UserEPEntity> checkUserExist(@Param("array") String[] usNames);

    List<UserEPEntity> queryAllUserIdAndName();

    //根据数USName和删除的状态去筛选人
    List<UserEPEntity> checkUserExistById(@Param("array") String[] usNames);


    // 根据用户名查用户
    UserEPEntity queryUserByName(String name);


    // 根据用户名查询用户
    List<UserEPEntity> queryUserByQuery(UserEPEntity userEPEntity);

    //根据id查询用户
    UserEPEntity queryUserById(String userId);

    /**
     *
     * 查询所有用户ID
     * @return
     */
    List<String> queryAllUsid();

    String queryProjectAdmin(String contactAddress);

    //查询上级部长
    List<String> querySuperLeader(String orgId);


    List<String> queryAllUsIdByDeptId(String orgId);

    @MapKey("USID")
    Map queryUserIdAndNameByIds( @Param("userIds") List<String> userIds);

    List<UserEPEntity> queryUserIdAndNameByIdList( @Param("userIds") List<String> userIds);

    List<String> queryUserIdByNameByLike(@Param("userName") String userName );

    List<String> queryUserIdByNameByNotLike(@Param("userName") String userName );

    List<String> queryUserIdByNameByEquals(@Param("userName") String userName );

    List<String> queryUserIdByNameByNotEquals(@Param("userName") String userName );

    List<String> queryUserIdByNameAndOrgId(@Param("userName") String userName , @Param("orgId") String orgId);

    List<Map<String,Object>> getEmployeeInfoMapList();
    String getUserNameByUserId(String userId);
    Map<String,String> getProjectMemberMapById(String userId);
    List<String> getAllEmpIds();

}
