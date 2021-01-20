package com.adc.da.research.project.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.project.entity.WorkProgressEO;
import com.adc.da.research.project.entity.WorkProgressHistoryEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_WORK_PROGRESS_HISTORY WorkProgressHistoryEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface WorkProgressHistoryEODao extends BaseDao<WorkProgressHistoryEO> {
    /**
     * 批量新增
     *
     * @param workProgressEOS
     */
    void batchInsertSelective(@Param("workProgressEOS") List<WorkProgressHistoryEO> workProgressEOS);

    void deleteByProjectId(@Param("projectId")String projectId);

}
