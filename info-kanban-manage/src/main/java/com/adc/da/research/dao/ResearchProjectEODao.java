package com.adc.da.research.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.pad.entity.PadOperationManageEO;
import com.adc.da.research.entity.ResearchProjectEO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>DB_RESEARCH_PROJECT ResearchProjectEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-07-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ResearchProjectEODao extends BaseDao<ResearchProjectEO> {

    int insertList(@Param("list") List<ResearchProjectEO> list);

    BigDecimal sumSelfFundsByYearAndResearchProjectIdList(@Param("year") Integer year, @Param("list") List<String> list);
    BigDecimal sumNationFundsByResearchProjectIdList(@Param("list") List<String> list);
    BigDecimal sumSelfFundsByResearchProjectIdList(@Param("list") List<String> list);
    BigDecimal sumAllFundsByResearchProjectIdList(@Param("list") List<String> list);

    BigDecimal sumNationFundsByIdList(@Param("list") List<String> list);
    BigDecimal sumSelfFundsByIdList(@Param("list") List<String> list);
    BigDecimal sumAllFundsByIdList(@Param("list") List<String> list);

    List<ResearchProjectEO> queryNationFundsTop5(@Param("list") List<String> list);
    List<ResearchProjectEO> querySelfFundsTop5(@Param("list") List<String> list);
    List<ResearchProjectEO> queryAllFundsTop5(@Param("list") List<String> list);
    List<ResearchProjectEO>  queryByIdList(@Param("list") List<String> list);
    void deleteLogicInBatch(@Param("list") List<String> list);

    void deleteLogicAll();

    List<ResearchProjectEO> queryAllFundsGroupByOrgName(@Param("list") List<String> list);
    List<ResearchProjectEO> querySelfFundsGroupByOrgName(@Param("list") List<String> list);
    List<ResearchProjectEO> queryNationFundsGroupByOrgName(@Param("list") List<String> list);

    List<String> getResearchProjectIdList();


    List<ResearchProjectEO> queryByResearchProjectIdList(@Param("list") List<String> list);

    List<ResearchProjectEO> queryListByDangerLevel(@Param("level") String level);

}
