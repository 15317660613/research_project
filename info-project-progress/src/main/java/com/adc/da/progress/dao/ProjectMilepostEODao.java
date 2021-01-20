package com.adc.da.progress.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.progress.entity.ProjectMilepostEO;
import com.adc.da.progress.page.MyProjectMilepostEOPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>PR_PROJECT_MILEPOST ProjectMilepostEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ProjectMilepostEODao extends BaseDao<ProjectMilepostEO> {
    int insertList(List<ProjectMilepostEO> list);

    List<ProjectMilepostEO> page4Tips(MyProjectMilepostEOPage page) ;

    List<ProjectMilepostEO> selectByStageId(@Param("stageId") String stageId);

    Integer queryCountByName(@Param("milepostName") String milepostName,@Param("projectId") String projectId);

    List<ProjectMilepostEO> selectByManagerId(@Param("managerId") String managerId);

    Integer query4TipsByCount(MyProjectMilepostEOPage page);

    List<ProjectMilepostEO> getByProjectIdList(@Param("projectIdList") List<String> projectIdList);

    List<ProjectMilepostEO> getByProjectId(@Param("projectId") String projectId);

    List<String> getProjectIdListByManagerId(@Param("managerId")  String managerId);

}
