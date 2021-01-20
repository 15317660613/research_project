package com.adc.da.research.project.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.project.entity.MemberInfoEO;
import com.adc.da.research.project.entity.MemberInfoHistoryEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_MEMBER_INFO_HISTORY MemberInfoHistoryEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface MemberInfoHistoryEODao extends BaseDao<MemberInfoHistoryEO> {
    /**
     * 批量新增
     *
     * @param memberInfoEOS
     */
    void batchInsertSelective(@Param("memberInfoEOS") List<MemberInfoHistoryEO> memberInfoEOS);

    List<MemberInfoHistoryEO> selectAllByProjectId(@Param("projectId")String projectId);

    void deleteByProjectId(@Param("projectId")String projectId);

}
