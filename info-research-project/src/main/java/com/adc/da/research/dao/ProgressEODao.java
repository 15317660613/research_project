package com.adc.da.research.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.entity.ProgressEO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>RS_PROJECT_PROGRESS ProgressEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-23 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ProgressEODao extends BaseDao<ProgressEO> {
    int insertSelectiveAll(@Param("list") List<ProgressEO> list);

    /**
     * 删除
     *
     * @param value
     * @return
     */
    @Delete("DELETE FROM RS_PROJECT_PROGRESS WHERE research_project_id_ = #{value}")
    int deleteByProjectId(String value);

    /**
     * 获取进行中/已完成的 项目进度
     * setExtInfo1(0)
     * setExtInfo2("-1")
     *
     * @param value
     * @return
     */
    List<ProgressEO> getRunningProjectInfo(String value);

}
