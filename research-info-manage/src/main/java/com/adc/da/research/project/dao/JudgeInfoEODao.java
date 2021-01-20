package com.adc.da.research.project.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.project.entity.JudgeInfoEO;
import com.adc.da.research.project.entity.ScoreInfoEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_JUDGE_INFO JudgeInfoEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface JudgeInfoEODao extends BaseDao<JudgeInfoEO> {
    /**
     * 批量新增
     *
     * @param judgeInfoEOS
     */
    void batchInsertSelective(@Param("judgeInfoEOS") List<JudgeInfoEO> judgeInfoEOS);
    /**
     * 根据project id 删除
     */
   // void deleteByProjectId(String projectId);
    /**
     * 根据ProjectId修改
     */
    void updateByProjectId(JudgeInfoEO judgeInfoEO);
    /**
     * 根据ProjectId和专家id修改
     */
    void updateByProjectIdAndExpertUserId(JudgeInfoEO judgeInfoEO);

    String getRatingRulesIdByProjectId(@Param("project_id") String projectId);
    /**
     * 根据project id 删除
     */
    void  deleteByProjectId(@Param("projectId")String projectId);
    /**
     * 根据project id 查询
     */
    List<JudgeInfoEO> queryListByProjectId(@Param("projectId")String projectId);
}
