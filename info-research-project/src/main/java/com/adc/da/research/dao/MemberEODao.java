package com.adc.da.research.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.entity.MemberEO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>RS_PROJECT_MEMBER MemberEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface MemberEODao extends BaseDao<MemberEO> {
    int insertSelectiveAll(@Param("list") List<MemberEO> list);

    /**
     * 删除
     *
     * @param value
     * @return
     */
    @Delete("DELETE FROM RS_PROJECT_MEMBER WHERE research_project_id_ = #{value}")
    int deleteByProjectId(String value);
}
