package com.adc.da.research.project.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.project.entity.CheckTargetHistoryEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_CHECK_TARGET_HISTORY CheckTargetHistoryEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface CheckTargetHistoryEODao extends BaseDao<CheckTargetHistoryEO> {
    /**
     * 批量新增
     *
     * @param checkTargetEOS
     */
    void batchInsertSelective(@Param("checkTargetEOS") List<CheckTargetHistoryEO> checkTargetEOS);

    void deleteByProjectId(@Param("projectId")String projectId,@Param("checkTypeId")String checkTypeId);

    void  deleteAllByProjectId(@Param("projectId")String projectId);

}
