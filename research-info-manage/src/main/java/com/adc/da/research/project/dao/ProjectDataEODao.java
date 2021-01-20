package com.adc.da.research.project.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.base.page.BasePage;
import com.adc.da.research.project.entity.ProjectDataEO;
import com.adc.da.research.project.page.ProjectDataEOPage;
import com.adc.da.research.project.vo.ProjectContractInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_PROJECT_DATA ProjectDataEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ProjectDataEODao extends BaseDao<ProjectDataEO> {

    int declarePage4TipsCount(BasePage page);

    List<ProjectDataEO> declarePage4Tips(BasePage page);

    ProjectDataEO selectByProjectId(String id);

    int selectProjectDataCount(String id);

    void updateProjectStatusByProjectId(@Param("id") String id,@Param("projectStatus") String projectStatus);

    String getIdByName(@Param("reportingUnitName") String  reportingUnitName);

    //    获取项目中止页面的信息
    List<ProjectDataEO> getSuspendPage(ProjectDataEOPage page);

    List<ProjectDataEO> queryByPageForFunds(ProjectDataEOPage page);

    Integer queryByCountForFunds(ProjectDataEOPage page);

    List<ProjectDataEO>queryByListForFunds(ProjectDataEOPage page);

    List<ProjectDataEOPage> getProjectContractInfo(ProjectContractInfoVO page);

    //项目变更列表
    List<ProjectDataEO>  queryByChangePage(ProjectDataEOPage page);

    List<ProjectDataEO>  queryDeclareByPage(ProjectDataEOPage page);

    int queryDeclareByCount(ProjectDataEOPage page);
}
