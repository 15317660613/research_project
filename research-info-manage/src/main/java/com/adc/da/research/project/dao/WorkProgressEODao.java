package com.adc.da.research.project.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.project.entity.ImplementationProcFileEO;
import com.adc.da.research.project.entity.WorkProgressEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_WORK_PROGRESS WorkProgressEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface WorkProgressEODao extends BaseDao<WorkProgressEO> {

    /**
     * 批量新增
     *
     * @param workProgressEOS
     */
    void batchInsertSelective(@Param("workProgressEOS") List<WorkProgressEO> workProgressEOS);

    void deleteByProjectId(@Param("projectId")String projectId);

    void deleteLogicInBatch(List<String> ids);

    //    更新项目备注
    Integer submitFile(ImplementationProcFileEO file);

    WorkProgressEO getWorkId(String id);

}
