package com.adc.da.processform.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.processform.entity.ProjectContractInvoiceEO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <br>
 * <b>功能：</b>PF_PROJECT_CONTRACT_INVOICE ProjectContractInvoiceEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ProjectContractInvoiceEODao extends BaseDao<ProjectContractInvoiceEO> {
    int insertSelectiveAll(@Param("list") List<ProjectContractInvoiceEO> projectContractInvoiceEOList);
    int deleteByProjectId(@Param("list") List<String> projectId);

    int deleteByContractInvoiceId(@Param("ids") List<String> ids);

    List<ProjectContractInvoiceEO> selectByYear(@Param("year") int year);

    List<ProjectContractInvoiceEO> sumByYearGroupByBusinessDeptId(@Param("year") int year);

    ProjectContractInvoiceEO sumByYear(@Param("year") int year);

    List< ProjectContractInvoiceEO> sumByYearGroupByMonth(@Param("year") int year);

    ProjectContractInvoiceEO sumByDateBetween( @Param("beginTime")Date beginTime , @Param("endTime")Date endTime );

    BigDecimal sumActualInvoiceAmountByDateBetween( @Param("beginTime")Date beginTime , @Param("endTime")Date endTime );
    BigDecimal sumChangeInvoiceAmountByDateBetween( @Param("beginTime")Date beginTime , @Param("endTime")Date endTime );
    BigDecimal sumOriginInvoiceAmountByDateBetween( @Param("beginTime")Date beginTime , @Param("endTime")Date endTime );
    BigDecimal sumBackInvoiceAmountByDateBetween( @Param("beginTime")Date beginTime , @Param("endTime")Date endTime );


    List<ProjectContractInvoiceEO> selectByYearAndMonth(@Param("year") int year, @Param("month") int month);


    List<ProjectContractInvoiceEO> sumByYearAndMonthGroupByBusinessDeptId(@Param("year") int year, @Param("month") int month);

    ProjectContractInvoiceEO sumByYearAndMonth(@Param("year") int year, @Param("month") int month);

    List<ProjectContractInvoiceEO> queryAll();
    BigDecimal sumActualInvoiceAmountByYear(@Param("year") int year);
    BigDecimal sumChangeInvoiceAmountByYear(@Param("year") int year);
    BigDecimal sumOriginInvoiceAmountByYear(@Param("year") int year);
    BigDecimal sumBackInvoiceAmountByYear(@Param("year") int year);
    BigDecimal sumActualInvoiceAmountByYearAndMonth(@Param("year") int year, @Param("month") int month);
    BigDecimal sumChangeInvoiceAmountByYearAndMonth(@Param("year") int year, @Param("month") int month);

    BigDecimal sumOriginInvoiceAmountByYearAndMonth(@Param("year") int year, @Param("month") int month);
    BigDecimal sumBackInvoiceAmountByYearAndMonth(@Param("year") int year, @Param("month") int month);

}
