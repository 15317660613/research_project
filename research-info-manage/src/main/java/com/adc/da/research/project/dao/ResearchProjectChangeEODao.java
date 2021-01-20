package com.adc.da.research.project.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.project.entity.ResearchProjectChangeEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_RESEARCH_PROJECT_CHANGE ResearchProjectChangeEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ResearchProjectChangeEODao extends BaseDao<ResearchProjectChangeEO> {

    /**
     * 批量新增
     *
     * @param researchProjectChangeEOS
     */
    void batchInsertSelective(@Param("researchProjectChangeEOS") List<ResearchProjectChangeEO> researchProjectChangeEOS);

    //根据projectId 删除
    void deleteByProjectId(@Param("projectId")String projectId);

}
