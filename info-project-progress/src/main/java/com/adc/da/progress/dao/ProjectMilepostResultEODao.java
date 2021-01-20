package com.adc.da.progress.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.progress.entity.ProjectMilepostResultEO;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>PR_PROJECT_MILEPOST_RESULT ProjectMilepostResultEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ProjectMilepostResultEODao extends BaseDao<ProjectMilepostResultEO> {

    int insertList(List<ProjectMilepostResultEO> list);

    List<ProjectMilepostResultEO> selectByMilepostId(String milepostId);

    int deleteByMilepostIdIn(List<String> list);
}
