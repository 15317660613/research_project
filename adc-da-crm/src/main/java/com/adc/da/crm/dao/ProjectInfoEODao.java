package com.adc.da.crm.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.base.page.BasePage;
import com.adc.da.crm.entity.ProjectInfoEO;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>PROJECT_INFO ProjectInfoEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ProjectInfoEODao extends BaseDao<ProjectInfoEO> {

    int updateByPrimaryKeyAndDelFlag(@Param("primaryKey") String primaryKey, @Param("delFlag") int delFlag);

    List<ProjectInfoEO> queryCRMListByPage(BasePage pager);

    int queryCRMListByCount(BasePage page);

}
