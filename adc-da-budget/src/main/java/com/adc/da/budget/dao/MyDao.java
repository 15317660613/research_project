package com.adc.da.budget.dao;

import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MyDao {

    List<String> getUserIdByUserName(@Param("usname") String usname);

    List<String> getUserIdByUserNameLike(@Param("usname") String usname);

    String getUserNameByUserId(@Param("usid") String usid);

    Float getWorkTimeByTaskId(@Param("taskId") String taskId);

    List<String> getTaskIdsBy1WorkTime(@Param("workTime") String workTime, @Param("workTimeOperator") String workTimeOperator);

    List<String> getTaskIdsByWorkTimeRange(@Param("workTimeStart") float workTimeStart, @Param("workTimeStartOperator") String workTimeStartOperator, @Param("workTimeEnd") float workTimeEnd, @Param("workTimeEndOperator") String workTimeEndOperator);
}