package com.adc.da.statistics.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <b>功能：</b>ST_TASK_WORKTIME TaskWorktimeEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Getter
@Setter
public class TaskWorktimeEO extends ProjectWorktimeEO {

    private String taskId;

    /**
     * 这个字段存的是childrenTaskId，而非TaskId
     */
    private String parentTaskId;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>taskId -> task_id</li>
     * <li>businessId -> business_id</li>
     * <li>parentTaskId -> parent_task_id</li>
     * <li>dailyTime -> daily_time</li>
     * <li>createTime -> create_time</li>
     * <li>worktime -> worktime</li>
     * <li>departmentId -> department_id</li>
     * <li>extinfo1 -> extinfo1</li>
     * <li>extinfo2 -> extinfo2</li>
     * <li>extinfo3 -> extinfo3</li>
     * <li>extinfo4 -> extinfo4</li>
     * <li>extinfo5 -> extinfo5</li>
     * <li>extinfo6 -> extinfo6</li>
     * <li>projectId -> project_id</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     */

}
