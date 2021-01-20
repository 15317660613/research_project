package com.adc.da.budget.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.budget.entity.ProjectContractInvoiceEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>PF_PROJECT_CONTRACT_INVOICE ProjectContractInvoiceEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface MyProjectContractInvoiceEODao extends BaseDao<ProjectContractInvoiceEO> {
    int insertSelectiveAll(@Param("list") List<ProjectContractInvoiceEO> projectContractInvoiceEOList);

    List<ProjectContractInvoiceEO> queryByContractId(@Param("contractId") String contractId);

    List<ProjectContractInvoiceEO> queryByProjectId(@Param("projectId") String projectId);


    /**
     * 根据合同编号查询当前年份的合同金额
     */
    ProjectContractInvoiceEO queryCurrentYearInvoiceAmountByContractId(@Param("contractId") String contractId,@Param("invoiceDateStart") String invoiceDateStart,@Param("invoiceDateEnd") String invoiceDateEnd);

            /*@Param("invoiceDateStart") String invoiceDateStart,
            @Param("invoiceDateEnd") String invoiceDateEnd,*/
            Double queryCurrentYearInvoiceAmountByProjectIdList(@Param("list") List<String> list,
                    @Param("invoiceDateStart") String invoiceDateStart,
                    @Param("invoiceDateEnd") String invoiceDateEnd);
    List<ProjectContractInvoiceEO> selectByYear(@Param("year") int year);
    List<ProjectContractInvoiceEO> queryAll();

}
