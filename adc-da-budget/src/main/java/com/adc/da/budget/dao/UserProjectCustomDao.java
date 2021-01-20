package com.adc.da.budget.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.budget.entity.UserProjectCustom;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserProjectCustomDao extends BaseDao<UserProjectCustom> {

    int delete(String id);

    int insert(UserProjectCustom userProjectCustom);

    int insertDynamic(UserProjectCustom userProjectCustom);

    int updateDynamic(UserProjectCustom userProjectCustom);

    int update(UserProjectCustom userProjectCustom);

    UserProjectCustom selectById(String id);

   /* List<UserProjectCustom> findPageWithResult(UserProjectCustomDTO userProjectCustomDTO);

    Integer findPageWithCount(UserProjectCustomDTO userProjectCustomDTO);*/
    List<UserProjectCustom> findAll(@Param("userid") String userid);

    List<String> findAllHideBusinessId(@Param("userid") String userid);

    List<String> findAllHideProjectId(@Param("userid") String userid);

    List<UserProjectCustom> findAllByType(@Param("userid") String userid,@Param("type") String type);

    List<UserProjectCustom> findByStatus(@Param("status") String status,@Param("userid") String userid);

    List<UserProjectCustom> findHideByStatusAndTypeAndUserId(@Param("status") String status,@Param("type") String type,@Param("userid") String userid);

   UserProjectCustom findUserProjectCustomByBussinessId(@Param("businessid") String businessid,@Param("type") String type,@Param("userid") String userid);

    List<UserProjectCustom> findUserProjectCustomListByBussinessIdAndUserId(@Param("businessid") String businessid, @Param("userid") String userid);

   UserProjectCustom findUserProjectCustomByProjectId(@Param("projectid") String projectid,@Param("type") String type,@Param("userid") String userid);
   UserProjectCustom findUserProjectCustomByTaskId(@Param("taskid") String taskid,@Param("type") String type,@Param("userid") String userid);
   UserProjectCustom findUserProjectCustomByChildtaskId(@Param("childtaskid") String childtaskid,@Param("type") String type,@Param("userid") String userid);
   // 增加3个级联删除方法
    // 根据budgetId 和userId 删除某个用户在budget下所有隐藏显示的数据
    int deleteByBudgetIdAndUserId(@Param("businessid") String businessid,@Param("userid") String userid);
    // 根据projectId 和userId 删除某个用户在project下所有隐藏显示的数据
    int deleteByProjectIdAndUserId(@Param("projectid") String projectid,@Param("userid") String userid);
    // 根据taskId 和userId 删除某个用户在task下所有隐藏显示的数据
    int deleteByTaskIdAndUserId(@Param("taskid") String taskid,@Param("userid") String userid);

     int insertList(@Param("list") List<UserProjectCustom> list);
}
