package com.adc.da.research.project.dao;


import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.project.entity.ScoreInfoEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_SCORE_INFO ScoreInfoEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ScoreInfoEODao extends BaseDao<ScoreInfoEO> {
    /**
     * 批量新增
     *
     * @param scoreInfoEOS
     */
    void batchInsertSelective(@Param("scoreInfoEOS") List<ScoreInfoEO> scoreInfoEOS);

    void updateByProjectId( ScoreInfoEO scoreInfoEOS);
    void  deleteByProjectId(@Param("projectId")String projectId);

    List<ScoreInfoEO> queryListByProjectId(@Param("projectId")String projectId);

}
