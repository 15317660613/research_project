package com.adc.da.research.project.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.project.entity.MemberInfoEO;
import com.adc.da.research.project.page.MemberInfoEOPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_MEMBER_INFO MemberInfoEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface MemberInfoEODao extends BaseDao<MemberInfoEO> {
    /*
     * 查询统计人员职称学历
     * */
    List<MemberInfoEO> queryCountByJob(MemberInfoEOPage page);

    /**
     * 批量新增
     *
     * @param memberInfoEOS
     */
    void batchInsertSelective(@Param("memberInfoEOS") List<MemberInfoEO> memberInfoEOS);

    List<MemberInfoEO> selectAllByProjectId(@Param("projectId")String projectId);

    void deleteByProjectId(@Param("projectId")String projectId);

    void deleteMajorMemberByProjectId(@Param("projectId")String projectId);

    void deleteByProjectIdAndMemberUserId(@Param("projectId") String projectId, @Param("memberUserId") String memberUserId);
}
