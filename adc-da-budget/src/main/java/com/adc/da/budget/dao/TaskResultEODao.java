package com.adc.da.budget.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.budget.entity.TaskResultEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>PF_TASK_RESULT TaskResultEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-09-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface TaskResultEODao extends BaseDao<TaskResultEO> {


    List<TaskResultEO> selectByTaskId(String value);
    void deleteByPrimaryKeys(@Param("list") List<String> list);
    public void deleteAllByTaskId(String taskId);

}
